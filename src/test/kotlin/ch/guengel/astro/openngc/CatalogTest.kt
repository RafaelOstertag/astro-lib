package ch.guengel.astro.openngc

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import ch.guengel.astro.coordinates.Angle
import ch.guengel.astro.coordinates.GeographicCoordinates
import ch.guengel.astro.coordinates.NUMBER_OF_OPENNGC_ENTRIES
import ch.guengel.astro.coordinates.NUMBER_OF_OPENNGC_ENTRIES_WITH_COORDINATES
import ch.guengel.astro.openngc.parser.CSVParser
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.io.File
import java.time.OffsetDateTime
import java.time.ZoneOffset

internal class CatalogTest {
    private val logger = LoggerFactory.getLogger(CatalogTest::class.java)
    val catalog = CSVParser.parse(File("src/test/resources/NGC.csv"))

    @Test
    fun `should find entries`() {
        val andromeda = catalog.find { it.isMessier() && it.messier == 31 }
        assertThat(andromeda).hasSize(1)

        val all = catalog.find { true }
        assertThat(all).hasSize(NUMBER_OF_OPENNGC_ENTRIES)

        val none = catalog.find { false }
        assertThat(none).isEmpty()
    }

    @RepeatedTest(10)
    fun `should find extended entries`() {
        val observerCoordinates = GeographicCoordinates(Angle(47, 32, 53.0), Angle(8, 50, 7.0))
        val dateTime = OffsetDateTime.of(2022, 1, 7, 20, 0, 0, 0, ZoneOffset.ofHours(1))

        var t0 = System.nanoTime()
        var result = catalog.findExtendedEntries(observerCoordinates, dateTime) {
            (it.entry.vMag?.compareTo(3.0) ?: 0) < 0
        }
        logger.info("Took {} ms to get {} results", (System.nanoTime() - t0) / 1_000_000, result.size)

        assertThat(result).hasSize(6)
        result.forEach {
            assertThat(it.dateTime).isEqualTo(dateTime)
            assertThat(it.geographicCoordinates).isEqualTo(observerCoordinates)
        }

        t0 = System.nanoTime()
        result = catalog.findExtendedEntries(observerCoordinates, dateTime) { true }
        logger.info("Took {} ms to get {} results", (System.nanoTime() - t0) / 1_000_000, result.size)

        assertThat(result).hasSize(NUMBER_OF_OPENNGC_ENTRIES_WITH_COORDINATES)
        result.forEach {
            assertThat(it.dateTime).isEqualTo(dateTime)
            assertThat(it.geographicCoordinates).isEqualTo(observerCoordinates)
        }

        t0 = System.nanoTime()
        result = catalog.findExtendedEntries(observerCoordinates, dateTime) { false }
        logger.info("Took {} ms to get {} results", (System.nanoTime() - t0) / 1_000_000, result.size)

        assertThat(result).isEmpty()
    }
}
