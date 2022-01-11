package ch.guengel.astro.openngc.parser

import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import ch.guengel.astro.coordinates.Angle
import ch.guengel.astro.openngc.ParserError
import org.junit.jupiter.api.Test

internal class DeclinationParserTest {
    @Test
    fun `should parse right ascension`() {
        assertThat(DeclinationParser.parse("-16:48:12.4")).isEqualTo(Angle(-16, 48, 12.4))
        assertThat(DeclinationParser.parse("+29:41:55.8")).isEqualTo(Angle(29, 41, 55.8))
    }

    @Test
    fun `should throw correct error`() {
        assertThat { DeclinationParser.parse("29:41:55.8") }.isFailure().hasClass(ParserError::class)
        assertThat { DeclinationParser.parse("+9:41:55.8") }.isFailure().hasClass(ParserError::class)
        assertThat { DeclinationParser.parse("-9:41:55.8") }.isFailure().hasClass(ParserError::class)
        assertThat { DeclinationParser.parse("+29:1:55.8") }.isFailure().hasClass(ParserError::class)
    }
}
