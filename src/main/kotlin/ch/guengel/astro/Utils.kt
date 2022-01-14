package ch.guengel.astro

import ch.guengel.astro.coordinates.DEGREES_PER_RADIAN
import kotlin.math.atan

internal fun arcTanCorrectedDeg(x: Double, y: Double): Double {
    var adjustmentDegrees = 0.0
    if (y >= 0.0 && x >= 0.0) {
        adjustmentDegrees = 0.0
    } else if (y >= 0.0 && x < 0.0) {
        adjustmentDegrees = 180.0
    } else if (y < 0.0 && x >= 0.0) {
        adjustmentDegrees = 360.0
    } else {
        adjustmentDegrees = 180.0
    }

    return atan(y / x) * DEGREES_PER_RADIAN + adjustmentDegrees
}
