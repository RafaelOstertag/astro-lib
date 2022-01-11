package ch.guengel.astro.openngc.parser

import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import ch.guengel.astro.coordinates.RightAscension
import ch.guengel.astro.openngc.ParserError
import org.junit.jupiter.api.Test

internal class RightAscensionParserTest {
    @Test
    fun `should parse right ascension`() {
        assertThat(RightAscensionParser.parse("01:08:10.84")).isEqualTo(RightAscension(1, 8, 10.84))
    }

    @Test
    fun `should throw correct error`() {
        assertThat { RightAscensionParser.parse("1:08:10.84") }.isFailure().hasClass(ParserError::class)
        assertThat { RightAscensionParser.parse("01:8:10.84") }.isFailure().hasClass(ParserError::class)
    }
}
