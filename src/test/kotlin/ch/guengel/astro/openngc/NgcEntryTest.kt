package ch.guengel.astro.openngc

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.junit.jupiter.api.Test

internal class NgcEntryTest {
    @Test
    fun `should get name`() {
        assertThat(NgcEntry(NgcEntryId(CatalogName.NGC, "0001"), ObjectType.NON_EXISTENT).name).isEqualTo("NGC0001")
    }

    @Test
    fun isMessier() {
        assertThat(NgcEntry(NgcEntryId(CatalogName.NGC, "0001"),
            ObjectType.NON_EXISTENT,
            messier = 1).isMessier()).isTrue()
        assertThat(NgcEntry(NgcEntryId(CatalogName.NGC, "0002"),
            ObjectType.NON_EXISTENT,
            messier = null).isMessier()).isFalse()
    }
}
