package ch.guengel.astro.coordinates

import kotlin.math.sign

internal fun Double.getFractionalPart(): Double {
    val fractionalPartString = "0." + this.toString().split('.')[1]
    return fractionalPartString.toDouble() * this.sign
}

internal fun Double.toRadians() = this * RADIANS_PER_DEGREE

internal fun Double.toDegrees() = this * DEGREES_PER_RADIAN

