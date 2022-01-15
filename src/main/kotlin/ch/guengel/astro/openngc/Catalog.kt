package ch.guengel.astro.openngc

import ch.guengel.astro.coordinates.GeographicCoordinates
import ch.guengel.astro.coordinates.toHorizonCoordinates
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import java.time.OffsetDateTime
import kotlin.math.ceil

class Catalog(val entries: List<Entry>) {
    private val chunkSize by lazy { ceil(entries.size.toDouble() / numberOfThreads).toInt() }

    fun find(block: (Entry) -> Boolean): List<Entry> = entries.filter(block)

    fun findExtendedEntries(
        observerCoordinates: GeographicCoordinates,
        observerDateTime: OffsetDateTime,
        predicate: (ExtendedEntry) -> Boolean,
    ): List<ExtendedEntry> = runBlocking {
        entries
            .chunked(chunkSize)
            .map { entrySubList ->
                async(threadPoolContext) {
                    enrichEntryList(observerCoordinates, observerDateTime, entrySubList)
                        .filter(predicate)
                }
            }
            .awaitAll()
            .flatten()
    }

    private fun enrichEntryList(
        observerCoordinates: GeographicCoordinates,
        observerDateTime: OffsetDateTime,
        entryList: List<Entry>,
    ) = entryList.filter { it.equatorialCoordinates != null }
        .map { entry ->
            ExtendedEntry(
                entry,
                entry.equatorialCoordinates!!.toHorizonCoordinates(observerCoordinates,
                    observerDateTime),
                observerDateTime,
                observerCoordinates
            )
        }

    fun extendEntries(
        observerCoordinates: GeographicCoordinates,
        observerDateTime: OffsetDateTime,
        entryList: List<Entry>,
    ): List<ExtendedEntry> = if (entryList.size > 1_000) {
        runBlocking {
            entryList
                .chunked(chunkSize)
                .map { entrySubList ->
                    async(threadPoolContext) {
                        enrichEntryList(observerCoordinates, observerDateTime, entrySubList)
                    }
                }.awaitAll()
                .flatten()
        }
    } else {
        enrichEntryList(observerCoordinates, observerDateTime, entryList)
    }


    @OptIn(DelicateCoroutinesApi::class)
    private companion object {
        private val logger = LoggerFactory.getLogger(Catalog::class.java)
        private val threadPoolContext: ExecutorCoroutineDispatcher
        private val numberOfThreads = Runtime.getRuntime().availableProcessors()

        init {
            logger.info("Setup catalog thread pool with {} threads", numberOfThreads)

            threadPoolContext = newFixedThreadPoolContext(numberOfThreads, "Catalog-Thread-Pool")

            Runtime.getRuntime().addShutdownHook(
                Thread {
                    logger.info("Shutdown catalog thread pool")
                    threadPoolContext.close()
                }
            )
        }
    }
}

