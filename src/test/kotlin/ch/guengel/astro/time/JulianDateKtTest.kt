package ch.guengel.astro.time

import assertk.assertThat
import assertk.assertions.isCloseTo
import assertk.assertions.isEqualTo
import ch.guengel.astro.coordinates.MAX_DELTA
import org.junit.jupiter.api.Test
import java.time.*

internal class JulianDateKtTest {
    @Test
    fun `should convert to UT`() {
        val offsetDateTime = OffsetDateTime.of(2022, 1, 13, 19, 45, 12, 0, ZoneOffset.ofHours(1))
        val ut = offsetDateTime.toUT()
        assertThat(ut).isEqualTo(ZonedDateTime.of(2022, 1, 13, 18, 45, 12, 0, ZoneId.of("GMT")))
    }

    @Test
    fun `should get zero hour GMT at same date`() {
        val zonedDateTime = ZonedDateTime.of(2022, 1, 13, 18, 45, 12, 0, ZoneId.of("GMT"))
        val zeroHourGMTSameDate = zonedDateTime.asZeroHourGMTSameDate()
        assertThat(zeroHourGMTSameDate).isEqualTo(ZonedDateTime.of(zonedDateTime.toLocalDate(),
            LocalTime.of(0, 0, 0, 0),
            ZoneId.of("GMT")))
    }

    @Test
    fun `should get Julian Date UTC`() {
        val zoneDateTime = ZonedDateTime.of(2022, 1, 13, 18, 45, 12, 0, ZoneId.of("GMT"))
        assertThat(zoneDateTime.toJulianDateUTC()).isCloseTo(2459592.5, MAX_DELTA)
    }

    @Test
    fun `should get Julian Date Time UTC`() {
        val zoneDateTime = ZonedDateTime.of(2022, 1, 13, 18, 45, 12, 0, ZoneId.of("GMT"))
        assertThat(zoneDateTime.toJulianDateTimeUTC()).isCloseTo(2459593.281388889, MAX_DELTA)
    }

    @Test
    fun `should get Julian Date Time UTC TT`() {
        val zoneDateTime = ZonedDateTime.of(2022, 1, 13, 18, 45, 12, 0, ZoneId.of("GMT"))
        assertThat(zoneDateTime.toJulianDateTimeTT()).isCloseTo(2459593.282073889, MAX_DELTA)
    }
}
