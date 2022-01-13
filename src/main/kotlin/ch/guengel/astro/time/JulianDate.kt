package ch.guengel.astro.time

import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.JulianFields

fun OffsetDateTime.toUT(): ZonedDateTime = this.atZoneSameInstant(ZoneId.of("GMT"))

fun ZonedDateTime.asZeroHourGMTSameDate(): ZonedDateTime =
    ZonedDateTime.of(toLocalDate(), LocalTime.of(0, 0, 0, 0), ZoneId.of("GMT"))

fun ZonedDateTime.toJulianDateUTC(): Double = asZeroHourGMTSameDate().getLong(JulianFields.JULIAN_DAY) - 0.5

fun ZonedDateTime.toJulianDateTimeUTC(): Double = toJulianDateUTC() + (this.toLocalTime().toDecimalHours() / 24.0)

fun ZonedDateTime.toJulianDateTimeTT(): Double = toJulianDateUTC() +
        (this.toLocalTime()
            .toDecimalHours() + ((LeapSeconds.forDateTime(this.toOffsetDateTime()) + 32.184) / 3_600.0)) / 24.0
