package ch.guengel.astro.coordinates

import java.time.OffsetDateTime
import java.time.ZoneOffset

private enum class EndOfMonth {
    JUN,
    DEC
}

private fun createLeapSecondDate(year: Int, endOfMonth: EndOfMonth): OffsetDateTime =
    OffsetDateTime.of(
        year,
        if (endOfMonth == EndOfMonth.JUN) 6 else 12,
        if (endOfMonth == EndOfMonth.JUN) 30 else 31,
        0,
        0,
        0,
        0,
        ZoneOffset.UTC
    )


object LeapSeconds {
    // Obtained from https://en.m.wikipedia.org/wiki/Leap_second on 2022-01-05
    private val leapSeconds = listOf(
        createLeapSecondDate(1972, EndOfMonth.JUN),
        createLeapSecondDate(1972, EndOfMonth.DEC),
        createLeapSecondDate(1973, EndOfMonth.DEC),
        createLeapSecondDate(1974, EndOfMonth.DEC),
        createLeapSecondDate(1975, EndOfMonth.DEC),
        createLeapSecondDate(1976, EndOfMonth.DEC),
        createLeapSecondDate(1977, EndOfMonth.DEC),
        createLeapSecondDate(1978, EndOfMonth.DEC),
        createLeapSecondDate(1979, EndOfMonth.DEC),

        createLeapSecondDate(1981, EndOfMonth.JUN),
        createLeapSecondDate(1982, EndOfMonth.JUN),
        createLeapSecondDate(1983, EndOfMonth.JUN),
        createLeapSecondDate(1985, EndOfMonth.JUN),
        createLeapSecondDate(1987, EndOfMonth.DEC),
        createLeapSecondDate(1989, EndOfMonth.DEC),

        createLeapSecondDate(1990, EndOfMonth.DEC),
        createLeapSecondDate(1992, EndOfMonth.JUN),
        createLeapSecondDate(1993, EndOfMonth.JUN),
        createLeapSecondDate(1994, EndOfMonth.JUN),
        createLeapSecondDate(1995, EndOfMonth.DEC),
        createLeapSecondDate(1997, EndOfMonth.JUN),
        createLeapSecondDate(1998, EndOfMonth.DEC),

        createLeapSecondDate(2005, EndOfMonth.DEC),
        createLeapSecondDate(2008, EndOfMonth.DEC),

        createLeapSecondDate(2012, EndOfMonth.JUN),
        createLeapSecondDate(2015, EndOfMonth.JUN),
        createLeapSecondDate(2016, EndOfMonth.DEC),
    )

    /**
     * Get number of leap seconds for a particular date/time.
     *
     * @param dateTime date/time to count amount of leap seconds for.
     */
    fun forDateTime(dateTime: OffsetDateTime) = leapSeconds.count { it <= dateTime }
}


