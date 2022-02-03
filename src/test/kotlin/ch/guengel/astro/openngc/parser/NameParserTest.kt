package ch.guengel.astro.openngc.parser

import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import ch.guengel.astro.openngc.CatalogName
import ch.guengel.astro.openngc.NgcEntryId
import ch.guengel.astro.openngc.ParserError
import org.junit.jupiter.api.Test

internal class NameParserTest {

    @Test
    fun `should parse name`() {
        val idWithAuxDesignation = NameParser.parseName("IC0080 NED01")
        assertThat(idWithAuxDesignation).isEqualTo(NgcEntryId(CatalogName.IC, "0080", "NED01"))

        val simpleId = NameParser.parseName("NGC71021")
        assertThat(simpleId).isEqualTo(NgcEntryId(CatalogName.NGC, "71021"))
    }

    @Test
    fun `should throw error on parsing error`() {
        assertThat { NameParser.parseName("xyz") }.isFailure().hasClass(ParserError::class)
    }
}
