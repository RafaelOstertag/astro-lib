package ch.guengel.astro.openngc

import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import org.junit.jupiter.api.Test

internal class ConstellationTest {
    @Test
    fun `find by abbreviation`() {
        assertThat(Constellation.findByAbbrev("And")).isEqualTo(Constellation.AND)
        assertThat { Constellation.findByAbbrev("xxx") }.isFailure().hasClass(NoSuchElementException::class)
    }
}
