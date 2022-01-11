package ch.guengel.astro.coordinates

import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.JulianFields
import kotlin.math.pow

private const val DAYS_TO_J2000 = 2_451_545.0

internal fun OffsetDateTime.toGST(): Time {
    // Based on https://thecynster.home.blog/2019/11/04/calculating-sidereal-time/ retrieved on 2022-01-05
    val ut = this.atZoneSameInstant(ZoneId.of("GMT"))
    val zeroHourGMTSameDate = ZonedDateTime.of(ut.toLocalDate(), LocalTime.of(0, 0, 0, 0), ZoneId.of("GMT"))
    val julianDateUTC = (zeroHourGMTSameDate.getLong(JulianFields.JULIAN_DAY) - 0.5)
    val julianDateTimeUTC = julianDateUTC + (ut.toLocalTime().toDecimalHours() / 24.0)
    val julianDateTimeTT = julianDateUTC +
            (ut.toLocalTime()
                .toDecimalHours() + ((LeapSeconds.forDateTime(ut.toOffsetDateTime()) + 32.184) / 3_600.0)) / 24

    val Du = julianDateTimeUTC - DAYS_TO_J2000
    val thetaOfDu = TWO_PI * (0.7790572732640 + 1.00273781191135448 * Du)
    val thetaOfDuNormalized = thetaOfDu.mod(TWO_PI)
    val thetaOfDuNormalizedInDegrees = thetaOfDuNormalized * DEGREES_PER_RADIAN
    val thetaOfDuInHours = thetaOfDuNormalizedInDegrees / DEGREES_PER_HOUR

    val T = (julianDateTimeTT - DAYS_TO_J2000) / 36_525.0
    val gmstPolynomialOfT = 0.014506 +
            (4612.156534 * T) +
            (1.3915817 * T.pow(2.0)) -
            (0.00000044 * T.pow(3.0)) -
            (0.000029956 * T.pow(4.0)) -
            (0.0000000368 * T.pow(5.0))
    val gmstPolynomialOfTInDegrees = (gmstPolynomialOfT / 3600.0).mod(360.0)
    val gmstPolynomialOfTInHours = gmstPolynomialOfTInDegrees / 15.0
    val gmst = thetaOfDuInHours + gmstPolynomialOfTInHours
    val gmstNormalized = gmst.mod(24.0)

    return Time.of(gmstNormalized)
}

internal fun gstToLst(gst: Time, geographicCoordinates: GeographicCoordinates): Time {
    val gstDecimalHours = gst.toDecimalHours()
    val longitudeHours = geographicCoordinates.longitude.asDecimal() / DEGREES_PER_HOUR
    val lst = gstDecimalHours + longitudeHours
    val lstNormalized = lst.mod(24.0)
    return Time.of(lstNormalized)
}
