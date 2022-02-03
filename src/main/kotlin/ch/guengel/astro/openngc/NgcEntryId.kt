package ch.guengel.astro.openngc

data class NgcEntryId(val catalogName: CatalogName, val catalogNumber: String, val auxiliaryDesignation: String = "") {
    override fun toString(): String {
        val id = "${catalogName.name}${catalogNumber}"
        return if (auxiliaryDesignation.isNotBlank()) "$id $auxiliaryDesignation" else id
    }
}
