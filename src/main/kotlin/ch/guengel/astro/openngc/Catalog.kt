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
        geographicCoordinates: GeographicCoordinates,
        dateTime: OffsetDateTime,
        predicate: (ExtendedEntry) -> Boolean
    ): List<ExtendedEntry> = runBlocking {
        entries
            .chunked(chunkSize)
            .map {
                async(threadPoolContext) {
                    it.filter { it.equatorialCoordinates != null }
                        .map {
                            ExtendedEntry(
                                it,
                                it.equatorialCoordinates!!.toHorizonCoordinates(geographicCoordinates, dateTime),
                                dateTime,
                                geographicCoordinates
                            )
                        }
                        .filter(predicate)
                }
            }
            .awaitAll()
            .flatten()
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

