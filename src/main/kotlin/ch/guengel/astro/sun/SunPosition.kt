package ch.guengel.astro.sun

import ch.guengel.astro.arcTanCorrectedDeg
import ch.guengel.astro.coordinates.*
import ch.guengel.astro.time.toJulianDateTimeUTC
import ch.guengel.astro.time.toUT
import java.time.OffsetDateTime
import kotlin.math.PI
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin

// Constants from The Astronomical Almanac 2000

// Sun's ecliptic longitude at the epach
private const val epsilon_g = 280.466069

// Sun's ecliptic longitude at perigee at the epoch
private const val omega_bar_g = 282.938346

// eccentricity of the Earth-Sun orbit
private const val e = 0.016708

object SunPosition {

    fun position(observerDateTime: OffsetDateTime): EquatorialCoordinates {
        val ut = observerDateTime.toUT()
        val julianDayNumber = ut.toJulianDateTimeUTC()
        val julianDaysElapsedSinceEpoch = julianDayNumber - DAYS_TO_J2000

        val meanAnomalySun = meanAnomaly(julianDaysElapsedSinceEpoch)

        val eclipticLongitude = eclipticLongitude(meanAnomalySun)
        val eclipticLongitudeInRadians = eclipticLongitude * RADIANS_PER_DEGREE

        val epsilonInRad = obliquityOfEclipticInRad(julianDaysElapsedSinceEpoch)

        val sinDelta = sin(epsilonInRad) * sin(eclipticLongitudeInRadians)
        val declination = asin(sinDelta) * DEGREES_PER_RADIAN

        val y = sin(eclipticLongitudeInRadians) * cos(epsilonInRad)
        val x = cos(eclipticLongitudeInRadians)

        val rightAscension = RightAscension.of(((arcTanCorrectedDeg(x, y)) / DEGREES_PER_HOUR).mod(24.0))

        return EquatorialCoordinates(rightAscension, Angle.of(declination))
    }


    internal fun meanAnomaly(daysSinceJ2000: Double): Double {
        val meanAnomalySun = ((360.0 * daysSinceJ2000) / 365.242191) + epsilon_g - omega_bar_g
        return meanAnomalySun.mod(360.0)
    }

    internal fun eclipticLongitude(meanAnomalySunInDegrees: Double): Double {
        val meanAnomalySunRadians = meanAnomalySunInDegrees * RADIANS_PER_DEGREE
        val approxCenter = (360.0 / PI) * e * sin(meanAnomalySunRadians)

        val trueAnomalySun = meanAnomalySunInDegrees + approxCenter
        val trueAnomalySunNormalized = trueAnomalySun.mod(360.0)

        return (trueAnomalySunNormalized + omega_bar_g).mod(360.0)
    }

}
