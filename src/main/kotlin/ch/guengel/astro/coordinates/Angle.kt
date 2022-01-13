package ch.guengel.astro.coordinates

import kotlin.math.absoluteValue

/**
 * Represent an angle in degrees, arc minutes and arc seconds.
 */
data class Angle(val degrees: Int, val arcMinutes: Int, val arcSeconds: Double) {

    init {
        require(degrees in -359..359) { "degrees must be in [-359,359]" }
        require(arcMinutes in 0..59) { "arc minutes must be in [0,59]" }
        require(arcSeconds >= 0.0 && arcSeconds < 60.0) { "arc seconds must be greater than or equal 0.0 and less than 60.0" }
    }

    fun asDecimal(): Double = if (degrees >= 0) {
        degrees + (arcMinutes + (arcSeconds / SECONDS_PER_MINUTE)) / MINUTES_PER_HOUR
    } else {
        degrees - (arcMinutes + (arcSeconds / SECONDS_PER_MINUTE)) / MINUTES_PER_HOUR
    }

    operator fun compareTo(other: Angle): Int {
        val difference = asDecimal() - other.asDecimal()
        if (difference > 0) {
            return 1
        }

        if (difference < 0) {
            return -1
        }

        return 0
    }

    override fun toString(): String = "%+04d°%02d'%#05.2f\"".format(degrees, arcMinutes, arcSeconds)

    companion object {
        fun of(decimalDegrees: Double): Angle {
            val minutes = decimalDegrees.absoluteValue.getFractionalPart() * MINUTES_PER_HOUR
            val seconds = minutes.getFractionalPart() * SECONDS_PER_MINUTE
            return Angle(decimalDegrees.toInt(), minutes.toInt(), seconds)
        }
    }
}
