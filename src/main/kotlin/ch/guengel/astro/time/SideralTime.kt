package ch.guengel.astro.time

import ch.guengel.astro.coordinates.*
import java.time.OffsetDateTime
import kotlin.math.pow

fun OffsetDateTime.toGST(): Time {
    // Based on https://thecynster.home.blog/2019/11/04/calculating-sidereal-time/ retrieved on 2022-01-05
    val ut = toUT()
    val julianDateTimeUTC = ut.toJulianDateTimeUTC()
    val julianDateTimeTT = ut.toJulianDateTimeTT()

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

fun gstToLST(gst: Time, geographicCoordinates: GeographicCoordinates): Time {
    val gstDecimalHours = gst.asDecimal()
    val longitudeHours = geographicCoordinates.longitude.asDecimal() / DEGREES_PER_HOUR
    val lst = gstDecimalHours + longitudeHours
    val lstNormalized = lst.mod(24.0)
    return Time.of(lstNormalized)
}
