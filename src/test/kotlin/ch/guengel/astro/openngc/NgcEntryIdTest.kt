package ch.guengel.astro.openngc

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

internal class NgcEntryIdTest {

    @Test
    fun `should correctly convert to string`() {
        val regularId = NgcEntryId(CatalogName.IC, "0001")
        assertThat(regularId.toString()).isEqualTo("IC0001")

        val idWithLongNumber = NgcEntryId(CatalogName.NGC, "12345")
        assertThat(idWithLongNumber.toString()).isEqualTo("NGC12345")

        val idWithAuxiliaryDesignation = NgcEntryId(CatalogName.NGC, "7893", "NED01")
        assertThat(idWithAuxiliaryDesignation.toString()).isEqualTo("NGC7893 NED01")
    }
}
