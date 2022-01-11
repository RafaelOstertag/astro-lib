package ch.guengel.astro.openngc.parser

import ch.guengel.astro.openngc.CatalogName
import ch.guengel.astro.openngc.ParserError

object NameParser {
    fun parseCatalog(value: String): CatalogName =
        when {
            value.startsWith("IC") -> CatalogName.IC
            value.startsWith("NGC") -> CatalogName.NGC
            else -> throw ParserError("$value is neither IC or NGC catalogName")
        }

    fun parseNumber(value: String): String {
        val components = value.split(" ")
        return components[0].removePrefix("NGC").removePrefix("IC")
    }
}
