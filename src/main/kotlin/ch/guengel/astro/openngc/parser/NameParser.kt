package ch.guengel.astro.openngc.parser

import ch.guengel.astro.openngc.CatalogName
import ch.guengel.astro.openngc.NgcEntryId
import ch.guengel.astro.openngc.ParserError

object NameParser {
    fun parseName(name: String): NgcEntryId {
        val components = name.split(" ", limit = 2)
        val catalogName = parseCatalog(components[0])
        val number = parseCatalogNumber(components[0])
        return NgcEntryId(catalogName, number, if (components.size > 1) components[1] else "")
    }

    private fun parseCatalog(value: String): CatalogName =
        when {
            value.startsWith("IC") -> CatalogName.IC
            value.startsWith("NGC") -> CatalogName.NGC
            else -> throw ParserError("$value is neither IC or NGC catalogName")
        }

    private fun parseCatalogNumber(value: String): String {
        return value.removePrefix("NGC").removePrefix("IC")
    }

}
