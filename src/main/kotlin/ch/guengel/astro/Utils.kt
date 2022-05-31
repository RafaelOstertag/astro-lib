package ch.guengel.astro

import ch.guengel.astro.coordinates.DEGREES_PER_RADIAN
import kotlin.math.atan

internal fun arcTanCorrectedDeg(x: Double, y: Double): Double {
    val adjustmentDegrees = if (y >= 0.0 && x >= 0.0) {
        0.0
    } else if (y >= 0.0 && x < 0.0) {
        180.0
    } else if (y < 0.0 && x >= 0.0) {
        360.0
    } else {
        180.0
    }

    return atan(y / x) * DEGREES_PER_RADIAN + adjustmentDegrees
}
