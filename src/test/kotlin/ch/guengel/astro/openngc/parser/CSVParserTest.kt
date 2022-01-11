package ch.guengel.astro.openngc.parser

import assertk.assertThat
import assertk.assertions.hasSize
import ch.guengel.astro.coordinates.NUMBER_OF_OPENNGC_ENTRIES
import org.junit.jupiter.api.Test
import java.io.File

internal class CSVParserTest {
    @Test
    fun `should parse catalog`() {
        val catalog = CSVParser.parse(File("src/test/resources/NGC.csv"))
        assertThat(catalog.entries).hasSize(NUMBER_OF_OPENNGC_ENTRIES)
    }
}
