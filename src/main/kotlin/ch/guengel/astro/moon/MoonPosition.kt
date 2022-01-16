package ch.guengel.astro.moon

import ch.guengel.astro.arcTanCorrectedDeg
import ch.guengel.astro.coordinates.*
import ch.guengel.astro.sun.SunPosition
import ch.guengel.astro.time.toJulianDateTimeTT
import ch.guengel.astro.time.toUT
import java.time.OffsetDateTime
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

// Constants from The Astronomical Almanac 2000

// Moon's ecliptic longitude of the ascending node at the epoch
private const val omegaZeroDeg = 125.044522

// Moon's ecliptic longitude at the epoch
private const val gammaZeroDeg = 218.316433

// Moon's ecliptic longitude at perigee at the epoch
private const val omegaBarZeroDeg = 83.353451

// Inclination of the Moon's orbit with respect to the ecliptic
private const val iota_rad = 5.1453964 * RADIANS_PER_DEGREE

object MoonPosition {

    fun position(observerDateTime: OffsetDateTime): EquatorialCoordinates {
        val ut = observerDateTime.toUT()
        val julianDateTimeTT = ut.toJulianDateTimeTT()
        val julianDaysSinceJ2000 = julianDateTimeTT - DAYS_TO_J2000

        val meanAnomalySun = SunPosition.meanAnomaly(julianDaysSinceJ2000)
        val sunEclipticLongitude = SunPosition.eclipticLongitude(meanAnomalySun)

        val trueEclipticLongitude = trueEclipticLongitude(julianDaysSinceJ2000, meanAnomalySun, sunEclipticLongitude)

        // Moon's mean ecliptic longitude of the ascending node
        val omegaDeg = (omegaZeroDeg - 0.0529539 * julianDaysSinceJ2000).mod(360.0)

        // Moon's corrected ecliptic longitude of the ascending node
        val omegaPrime = omegaDeg - 0.16 * sin(meanAnomalySun * RADIANS_PER_DEGREE)

        val y = sin((trueEclipticLongitude - omegaPrime) * RADIANS_PER_DEGREE) * cos(iota_rad)
        val x = cos((trueEclipticLongitude - omegaPrime) * RADIANS_PER_DEGREE)

        val moonEclipticLongitude = (omegaPrime + arcTanCorrectedDeg(x, y)).mod(360.0)
        val moonEclipticLatitude =
            asin(sin((trueEclipticLongitude - omegaPrime) * RADIANS_PER_DEGREE) * sin(iota_rad)) * DEGREES_PER_RADIAN

        val epsilonInRad = obliquityOfEclipticInRad(julianDaysSinceJ2000)

        val moonEclipticLongitudeInRad = moonEclipticLongitude * RADIANS_PER_DEGREE
        val moonEclipticLatitudeInRad = moonEclipticLatitude * RADIANS_PER_DEGREE
        val sinDeclination =
            sin(moonEclipticLatitudeInRad) * cos(epsilonInRad) + cos(moonEclipticLatitudeInRad) * sin(epsilonInRad) * sin(
                moonEclipticLongitudeInRad)

        val declination = Angle.of(asin(sinDeclination) * DEGREES_PER_RADIAN)

        val rightAscension = RightAscension.of(arcTanCorrectedDeg(cos(moonEclipticLongitudeInRad),
            sin(moonEclipticLongitudeInRad) * cos(epsilonInRad) - tan(moonEclipticLatitudeInRad) * sin(epsilonInRad)) / DEGREES_PER_HOUR)

        return EquatorialCoordinates(rightAscension, declination)
    }

    internal fun trueEclipticLongitude(
        julianDaysSinceJ2000: Double,
        meanAnomalySun: Double,
        sunEclipticLongitude: Double,
    ): Double {
        // Moon's mean ecliptic longitude
        val moonMeanEclipticLongitudeDeg = (13.176339686 * julianDaysSinceJ2000 + gammaZeroDeg).mod(360.0)

        // Moon's mean anomaly
        val meanAnomalyMoon =
            (moonMeanEclipticLongitudeDeg - 0.1114041 * julianDaysSinceJ2000 - omegaBarZeroDeg).mod(360.0)

        // annual equation correction
        val sinMeanAnomalySun = sin(meanAnomalySun * RADIANS_PER_DEGREE)
        val annualEquationCorrectionDeg = 0.1858 * sinMeanAnomalySun

        // eviction correction
        val evictionCorrectionDeg =
            1.2739 * sin((2 * (moonMeanEclipticLongitudeDeg - sunEclipticLongitude) - meanAnomalyMoon) * RADIANS_PER_DEGREE)

        // mean anomaly correction
        val meanAnomalyCorrectionDeg =
            meanAnomalyMoon + evictionCorrectionDeg - annualEquationCorrectionDeg - 0.37 * sinMeanAnomalySun

        // Moon true anomaly
        val moonTrueAnomalyDeg = (6.2886 * sin(meanAnomalyCorrectionDeg * RADIANS_PER_DEGREE)) +
                (0.214 * sin(2 * meanAnomalyCorrectionDeg * RADIANS_PER_DEGREE))

        // corrected Moon's mean ecliptic longitude
        val correctedMeanEclipticLongitude =
            moonMeanEclipticLongitudeDeg + evictionCorrectionDeg + moonTrueAnomalyDeg - annualEquationCorrectionDeg

        // Moon's variation correction
        val variationCorrection =
            0.6583 * sin((2 * (correctedMeanEclipticLongitude - sunEclipticLongitude)) * RADIANS_PER_DEGREE)

        // Moon's true ecliptic longitude
        return correctedMeanEclipticLongitude + variationCorrection
    }
}
