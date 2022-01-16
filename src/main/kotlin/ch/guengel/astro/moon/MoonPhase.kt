package ch.guengel.astro.moon

import ch.guengel.astro.coordinates.DAYS_TO_J2000
import ch.guengel.astro.coordinates.RADIANS_PER_DEGREE
import ch.guengel.astro.sun.SunPosition
import ch.guengel.astro.time.toJulianDateTimeTT
import ch.guengel.astro.time.toUT
import java.time.OffsetDateTime
import kotlin.math.cos

object MoonPhase {
    fun percentage(observerDateTime: OffsetDateTime): Double {
        val ut = observerDateTime.toUT()
        val julianDateTimeTT = ut.toJulianDateTimeTT()
        val julianDaysSinceJ2000 = julianDateTimeTT - DAYS_TO_J2000

        val meanAnomalySun = SunPosition.meanAnomaly(julianDaysSinceJ2000)
        val sunEclipticLongitude = SunPosition.eclipticLongitude(meanAnomalySun)

        val trueEclipticLongitude =
            MoonPosition.trueEclipticLongitude(julianDaysSinceJ2000, meanAnomalySun, sunEclipticLongitude)

        val age = (trueEclipticLongitude - sunEclipticLongitude) * RADIANS_PER_DEGREE

        return (1.0 - cos(age)) / 2.0
    }
}
