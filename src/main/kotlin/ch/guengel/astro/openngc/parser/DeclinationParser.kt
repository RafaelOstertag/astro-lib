package ch.guengel.astro.openngc.parser

import ch.guengel.astro.coordinates.Angle
import ch.guengel.astro.openngc.ParserError

object DeclinationParser {
    private val regularExpression =
        Regex("(?<degrees>(?:\\+|-)[0-9]{2}):(?<minutes>[0-9]{2}):(?<seconds>[0-9.]+)")

    fun parse(value: String): Angle {
        val result =
            regularExpression.matchEntire(value) ?: throw ParserError("Cannot extract declination from '$value'")
        val matchGroups = result.groups

        val degrees = matchGroups["degrees"]?.value?.toInt() ?: throw InternalError("'degrees' match group missing")
        val minutes = matchGroups["minutes"]?.value?.toInt() ?: throw InternalError("'minutes' match group missing")
        val seconds = matchGroups["seconds"]?.value?.toDouble() ?: throw InternalError("'seconds' match group missing")
        return Angle(degrees, minutes, seconds)
    }
}
