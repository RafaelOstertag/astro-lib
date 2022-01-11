package ch.guengel.astro.openngc

import assertk.assertThat
import assertk.assertions.hasClass
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import org.junit.jupiter.api.Test

internal class ObjectTypeTest {
    @Test
    fun `should find by abbreviation`() {
        assertThat(ObjectType.findByAbbrev("*")).isEqualTo(ObjectType.STAR)
        assertThat { ObjectType.findByAbbrev("xxx") }.isFailure().hasClass(NoSuchElementException::class)
    }
}
