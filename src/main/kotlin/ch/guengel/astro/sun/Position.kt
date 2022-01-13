package ch.guengel.astro.sun

import ch.guengel.astro.coordinates.*
import ch.guengel.astro.time.toJulianDateTimeUTC
import ch.guengel.astro.time.toUT
import java.time.OffsetDateTime
import kotlin.math.*

// Constants from The Astronomical Almanac 2000

// Sun's ecliptic longitude at the epach
private const val epsilon_g = 280.466069
private const val epsilon_g_rad = epsilon_g * RADIANS_PER_DEGREE

// Sun's ecliptic longitude at perigee at the epoch
private const val omega_bar_g = 282.938346

// eccentricity of the Earth-Sun orbit
private const val e = 0.016708

fun sunPosition(observerDateTime: OffsetDateTime): EquatorialCoordinates {
    val ut = observerDateTime.toUT()
    val julianDayNumber = ut.toJulianDateTimeUTC()
    val julianDaysElapsedSinceEpoch = julianDayNumber - DAYS_TO_J2000

    val meanAnomalySun = ((360.0 * julianDaysElapsedSinceEpoch) / 365.242191) + epsilon_g - omega_bar_g
    val meanAnomalySunNormalized = meanAnomalySun.mod(360.0)
    val meanAnomalySunRadians = meanAnomalySunNormalized * RADIANS_PER_DEGREE

    val approxCenter = (360.0 / PI) * e * sin(meanAnomalySunRadians)

    val trueAnomalySun = meanAnomalySun + approxCenter
    val trueAnomalySunNormalized = trueAnomalySun.mod(360.0)

    val eclipticLongitude = (trueAnomalySunNormalized + omega_bar_g).mod(360.0)
    val eclipticLongitudeInRadians = eclipticLongitude * RADIANS_PER_DEGREE

    val T = julianDaysElapsedSinceEpoch / 36_252.0
    val epsilonInRad =
        (23.439292 - (((46.815 * T) + (0.0006 * T.pow(2)) - (0.00181 * T.pow(3))) / 3_600.0)) * RADIANS_PER_DEGREE

    val sinDelta = sin(epsilonInRad) * sin(eclipticLongitudeInRadians)
    val declination = asin(sinDelta) * DEGREES_PER_RADIAN

    val tanAlpha =
        (sin(eclipticLongitudeInRadians) * cos(epsilonInRad)) / cos(eclipticLongitudeInRadians)

    val rightAscension = RightAscension.of(((atan(tanAlpha) * DEGREES_PER_RADIAN) / DEGREES_PER_HOUR).mod(24.0))

    return EquatorialCoordinates(rightAscension, Angle.of(declination))
}
