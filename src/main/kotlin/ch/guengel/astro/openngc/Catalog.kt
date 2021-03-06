package ch.guengel.astro.openngc

import ch.guengel.astro.coordinates.GeographicCoordinates
import ch.guengel.astro.coordinates.toHorizontalCoordinates
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.time.OffsetDateTime
import kotlin.math.ceil

class Catalog(val entries: List<NgcEntry>) {
    private val chunkSize by lazy { ceil(entries.size.toDouble() / numberOfThreads).toInt() }

    val size: Int
        get() = entries.size

    fun find(block: (NgcEntry) -> Boolean): List<NgcEntry> = entries.filter(block)

    fun findExtendedEntries(
        observerCoordinates: GeographicCoordinates,
        observerDateTime: OffsetDateTime,
        predicate: (ExtendedNgcEntry) -> Boolean,
    ): List<ExtendedNgcEntry> = runBlocking {
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
        ngcEntryList: List<NgcEntry>,
    ) = ngcEntryList.filter { it.equatorialCoordinates != null }
        .map { entry ->
            ExtendedNgcEntry(
                entry,
                entry.equatorialCoordinates!!.toHorizontalCoordinates(observerCoordinates,
                    observerDateTime),
                observerDateTime,
                observerCoordinates
            )
        }

    fun extendEntries(
        observerCoordinates: GeographicCoordinates,
        observerDateTime: OffsetDateTime,
        ngcEntryList: List<NgcEntry>,
    ): List<ExtendedNgcEntry> = if (ngcEntryList.size > 1_000) {
        runBlocking {
            ngcEntryList
                .chunked(chunkSize)
                .map { entrySubList ->
                    async(threadPoolContext) {
                        enrichEntryList(observerCoordinates, observerDateTime, entrySubList)
                    }
                }.awaitAll()
                .flatten()
        }
    } else {
        enrichEntryList(observerCoordinates, observerDateTime, ngcEntryList)
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

