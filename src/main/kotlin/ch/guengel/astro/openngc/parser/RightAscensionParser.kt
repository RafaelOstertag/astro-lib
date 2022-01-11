package ch.guengel.astro.openngc.parser

import ch.guengel.astro.coordinates.RightAscension
import ch.guengel.astro.openngc.InternalError
import ch.guengel.astro.openngc.ParserError

object RightAscensionParser {
    private val regularExpression =
        Regex("(?<hours>[0-9]{2}):(?<minutes>[0-9]{2}):(?<seconds>[0-9.]+)")

    fun parse(value: String): RightAscension {
        val result =
            regularExpression.matchEntire(value) ?: throw ParserError("Cannot extract right ascension from '$value'")
        val matchGroups = result.groups

        val hours = matchGroups["hours"]?.value?.toInt() ?: throw InternalError("'hours' match group missing")
        val minutes = matchGroups["minutes"]?.value?.toInt() ?: throw InternalError("'minutes' match group missing")
        val seconds = matchGroups["seconds"]?.value?.toDouble() ?: throw InternalError("'seconds' match group missing")
        return RightAscension(hours, minutes, seconds)
    }
}
