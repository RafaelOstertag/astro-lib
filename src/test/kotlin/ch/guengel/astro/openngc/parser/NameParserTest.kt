package ch.guengel.astro.openngc.parser

import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import ch.guengel.astro.openngc.CatalogName
import ch.guengel.astro.openngc.ParserError
import org.junit.jupiter.api.Test

internal class NameParserTest {

    @Test
    fun `should parse catalog`() {
        assertThat(NameParser.parseCatalog("IC0080 NED01")).isEqualTo(CatalogName.IC)
        assertThat(NameParser.parseCatalog("IC0080")).isEqualTo(CatalogName.IC)
        assertThat(NameParser.parseCatalog("NGC0414 NED01")).isEqualTo(CatalogName.NGC)
        assertThat(NameParser.parseCatalog("NGC0414")).isEqualTo(CatalogName.NGC)
    }

    @Test
    fun `should throw error on parsing error`() {
        assertThat { NameParser.parseCatalog("xyz") }.isFailure().hasClass(ParserError::class)
    }

    @Test
    fun `should parse number`() {
        assertThat(NameParser.parseNumber("IC0080 NED01")).isEqualTo("0080")
        assertThat(NameParser.parseNumber("IC0080")).isEqualTo("0080")
        assertThat(NameParser.parseNumber("NGC0414 NED01")).isEqualTo("0414")
        assertThat(NameParser.parseNumber("NGC0414")).isEqualTo("0414")
    }
}
