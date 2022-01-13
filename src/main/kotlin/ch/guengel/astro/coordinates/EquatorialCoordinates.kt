package ch.guengel.astro.coordinates

import java.time.OffsetDateTime
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin

data class EquatorialCoordinates(val rightAscension: RightAscension, val declination: Angle)

fun EquatorialCoordinates.toHorizonCoordinates(
    observerCoordinates: GeographicCoordinates,
    observerDateTime: OffsetDateTime,
): HorizontalCoordinates {
    val hourAngleInRadians =
        (this.rightAscension.toHourAngle(observerDateTime, observerCoordinates).asDecimal() * DEGREES_PER_HOUR)
            .toRadians()
    val declinationInRadians = declination.asDecimal().toRadians()
    val latitudeInRadians = observerCoordinates.latitude.asDecimal().toRadians()
    val sineOfAltitude = (sin(declinationInRadians) * sin(latitudeInRadians)) +
            (cos(declinationInRadians) * cos(latitudeInRadians) * cos(hourAngleInRadians))
    val altitudeInRadians = asin(sineOfAltitude)

    val cosineAzimuth = (sin(declinationInRadians) - (sin(latitudeInRadians) * sin(altitudeInRadians))) /
            (cos(latitudeInRadians) * cos(altitudeInRadians))
    val azimuthInRadiansNonNormalized = acos(cosineAzimuth)

    val azimuthInRadians =
        if (sin(hourAngleInRadians) < 0) azimuthInRadiansNonNormalized else TWO_PI - azimuthInRadiansNonNormalized

    val altitudeInDegrees = altitudeInRadians.toDegrees()
    val azimuthInDegrees = azimuthInRadians.toDegrees()

    return HorizontalCoordinates(Angle.of(altitudeInDegrees), Angle.of(azimuthInDegrees))
}
