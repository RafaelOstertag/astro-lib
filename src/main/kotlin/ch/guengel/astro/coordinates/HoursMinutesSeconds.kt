package ch.guengel.astro.coordinates

import java.time.LocalTime
import java.time.OffsetDateTime

sealed class HoursMinutesSeconds(val hours: Int, val minutes: Int, val seconds: Double) {
    init {
        require(hours in 0..23) { "Hours must be in [0,23]" }
        require(minutes in 0..59) { "Minutes must be in [0,59]" }
        require(seconds >= 0.0 && seconds < 60.0) { "Seconds must be greater than or equal to 0.0 and less than 60.0" }
    }

    fun toDecimalHours(): Double = hours +
            ((minutes + (seconds / SECONDS_PER_MINUTE)) / MINUTES_PER_HOUR)

    fun asLocalTime(): LocalTime =
        LocalTime.of(hours, minutes, seconds.toInt(), (seconds.getFractionalPart() * NANO_SECONDS_PER_SECOND).toInt())

    override fun toString(): String = "%02d:%02d:%#06.3f".format(hours, minutes, seconds)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HoursMinutesSeconds

        if (hours != other.hours) return false
        if (minutes != other.minutes) return false
        if (seconds != other.seconds) return false

        return true
    }

    override fun hashCode(): Int {
        var result = hours
        result = 31 * result + minutes
        result = 31 * result + seconds.hashCode()
        return result
    }
}

private fun <T : HoursMinutesSeconds> fromDecimal(
    decimalHour: Double,
    factory: (hour: Int, minute: Int, second: Double) -> T,
): T {
    if (decimalHour < 0.0 || decimalHour >= 24.0) {
        throw IllegalArgumentException("Decimal hour must be greater than or equal to 0.0 and less than 24.0")
    }

    val minutes = decimalHour.getFractionalPart() * MINUTES_PER_HOUR
    val seconds = minutes.getFractionalPart() * SECONDS_PER_MINUTE
    return factory(decimalHour.toInt(), minutes.toInt(), seconds)
}

class Time(hour: Int, minute: Int, second: Double) : HoursMinutesSeconds(hour, minute, second) {
    fun toAngle(): Angle = Angle.of(toDecimalHours() * DEGREES_PER_HOUR)

    companion object {
        fun of(decimalHour: Double): Time = fromDecimal(decimalHour, ::Time)
    }
}

class HourAngle(hour: Int, minute: Int, second: Double) : HoursMinutesSeconds(hour, minute, second) {
    /**
     * Convert hour angle to right ascension.
     *
     * @param observerDateTime date time of the observer.
     * @param observerCoordinates horizonCoordinates of the observer.
     * @return [RightAscension] of this hour angle at `offsetDateTime` at `horizonCoordinates`.
     */
    fun toRightAscension(observerDateTime: OffsetDateTime, observerCoordinates: GeographicCoordinates): RightAscension {
        val gst = observerDateTime.toGST()
        val lst = gstToLST(gst, observerCoordinates)
        val hourAngleDecimal = toDecimalHours()
        val rightAscensionDecimalNonNormalized = lst.toDecimalHours() - hourAngleDecimal
        return RightAscension.of(if (rightAscensionDecimalNonNormalized < 0) rightAscensionDecimalNonNormalized + 24.0 else rightAscensionDecimalNonNormalized)
    }

    companion object {
        fun of(decimalHour: Double): HourAngle = fromDecimal(decimalHour, ::HourAngle)
    }
}

class RightAscension(hour: Int, minute: Int, second: Double) : HoursMinutesSeconds(hour, minute, second) {
    /**
     * Convert this right ascension to hour angle.
     *
     * @param observerDateTime date time of the observer.
     * @param observerCoordinates horizonCoordinates of the observer.
     * @return [HourAngle] of this right ascension at `offsetDateTime` at `horizonCoordinates`.
     */
    fun toHourAngle(observerDateTime: OffsetDateTime, observerCoordinates: GeographicCoordinates): HourAngle {
        val gst = observerDateTime.toGST()
        val lst = gstToLST(gst, observerCoordinates)
        val rightAscensionDecimal = toDecimalHours()
        val hourAngleDecimalNonNormalized = lst.toDecimalHours() - rightAscensionDecimal
        return HourAngle.of(if (hourAngleDecimalNonNormalized < 0) hourAngleDecimalNonNormalized + 24.0 else hourAngleDecimalNonNormalized)
    }

    companion object {
        fun of(decimalHour: Double): RightAscension = fromDecimal(decimalHour, ::RightAscension)
    }
}
