package ch.guengel.astro.coordinates

import kotlin.math.pow
import kotlin.math.sign

internal fun Double.getFractionalPart(): Double {
    val fractionalPartString = "0." + this.toString().split('.')[1]
    return fractionalPartString.toDouble() * this.sign
}

internal fun Double.toRadians() = this * RADIANS_PER_DEGREE

internal fun Double.toDegrees() = this * DEGREES_PER_RADIAN

internal fun obliquityOfEclipticInRad(julianDaysSinceJ2000: Double): Double {
    val T = julianDaysSinceJ2000 / 36_252.0
    return (23.439292 - (((46.815 * T) + (0.0006 * T.pow(2)) - (0.00181 * T.pow(3))) / 3_600.0)) * RADIANS_PER_DEGREE
}
