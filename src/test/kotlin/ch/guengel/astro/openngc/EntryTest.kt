package ch.guengel.astro.openngc

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.junit.jupiter.api.Test

internal class EntryTest {
    @Test
    fun `should get name`() {
        assertThat(Entry(CatalogName.NGC, "0001", ObjectType.NON_EXISTENT).name).isEqualTo("NGC0001")
    }

    @Test
    fun isMessier() {
        assertThat(Entry(CatalogName.NGC, "0001", ObjectType.NON_EXISTENT, messier = 1).isMessier()).isTrue()
        assertThat(Entry(CatalogName.NGC, "0001", ObjectType.NON_EXISTENT, messier = null).isMessier()).isFalse()
    }
}
