package ch.guengel.astro.coordinates

import java.time.LocalTime
import kotlin.math.sign

internal fun Double.getFractionalPart(): Double {
    val fractionalPartString = "0." + this.toString().split('.')[1]
    return fractionalPartString.toDouble() * this.sign
}

internal fun Double.toRadians() = this * RADIANS_PER_DEGREE

internal fun Double.toDegrees() = this * DEGREES_PER_RADIAN

internal fun LocalTime.toDecimalHours(): Double = this.let { time ->
    time.hour + ((time.minute + ((time.second + time.nano / NANO_SECONDS_PER_SECOND.toDouble()) / SECONDS_PER_MINUTE)) / MINUTES_PER_HOUR)
}
