package ch.guengel.astro.coordinates

import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset

internal class HoursMinutesSecondsTest {
    @Test
    fun `should fail on negative hour`() {
        assertThat { Time(-1, 0, 0.0) }.isFailure().hasClass(IllegalArgumentException::class)
        assertThat { HourAngle(-1, 0, 0.0) }.isFailure().hasClass(IllegalArgumentException::class)
        assertThat { RightAscension(-1, 0, 0.0) }.isFailure().hasClass(IllegalArgumentException::class)
    }

    @Test
    fun `should fail on exceeding hour`() {
        assertThat { Time(24, 0, 0.0) }.isFailure().hasClass(IllegalArgumentException::class)
        assertThat { HourAngle(24, 0, 0.0) }.isFailure().hasClass(IllegalArgumentException::class)
        assertThat { RightAscension(24, 0, 0.0) }.isFailure().hasClass(IllegalArgumentException::class)
    }

    @Test
    fun `should fail on negative minute`() {
        assertThat { Time(0, -1, 0.0) }.isFailure().hasClass(IllegalArgumentException::class)
        assertThat { HourAngle(0, -1, 0.0) }.isFailure().hasClass(IllegalArgumentException::class)
        assertThat { RightAscension(0, -1, 1.0) }.isFailure().hasClass(IllegalArgumentException::class)
    }

    @Test
    fun `should fail on exceeding minute`() {
        assertThat { Time(0, 60, 0.0) }.isFailure().hasClass(IllegalArgumentException::class)
        assertThat { HourAngle(0, 60, 0.0) }.isFailure().hasClass(IllegalArgumentException::class)
        assertThat { RightAscension(0, 60, 0.0) }.isFailure().hasClass(IllegalArgumentException::class)
    }

    @Test
    fun `should fail on negative second`() {
        assertThat { Time(0, 0, -0.1) }.isFailure().hasClass(IllegalArgumentException::class)
        assertThat { HourAngle(0, 0, -0.1) }.isFailure().hasClass(IllegalArgumentException::class)
        assertThat { RightAscension(0, 0, -0.1) }.isFailure().hasClass(IllegalArgumentException::class)
    }

    @Test
    fun `should fail on exceeding second`() {
        assertThat { Time(0, 0, 60.0) }.isFailure().hasClass(IllegalArgumentException::class)
        assertThat { HourAngle(0, 0, 60.0) }.isFailure().hasClass(IllegalArgumentException::class)
        assertThat { RightAscension(0, 0, 60.0) }.isFailure().hasClass(IllegalArgumentException::class)
    }

    @Test
    fun `should convert to local time`() {
        val expectedTime = LocalTime.of(20, 21, 22, 999990000)
        assertThat(Time(20, 21, 22.99999).asLocalTime()).isEqualTo(expectedTime)
        assertThat(HourAngle(20, 21, 22.99999).asLocalTime()).isEqualTo(expectedTime)
        assertThat(RightAscension(20, 21, 22.99999).asLocalTime()).isEqualTo(expectedTime)
    }

    @Test
    fun `should create valid time`() {
        assertThat { Time(0, 0, 0.0) }.isSuccess()
        assertThat { Time(23, 59, 59.99999) }.isSuccess()
    }

    @Test
    fun `should create valid hour angle`() {
        assertThat { HourAngle(0, 0, 0.0) }.isSuccess()
        assertThat { HourAngle(23, 59, 59.99999) }.isSuccess()
    }

    @Test
    fun `should create valid right ascension`() {
        assertThat { RightAscension(0, 0, 0.0) }.isSuccess()
        assertThat { RightAscension(23, 59, 59.99999) }.isSuccess()
    }

    @Test
    fun `should convert to decimal hours`() {
        val time = Time(12, 30, 2.0)
        assertThat(time.asDecimal()).isCloseTo(12.500555, MAX_DELTA)

        val hourAngle = HourAngle(12, 30, 2.0)
        assertThat(hourAngle.asDecimal()).isCloseTo(12.500555, MAX_DELTA)

        val rightAscension = RightAscension(12, 30, 2.0)
        assertThat(rightAscension.asDecimal()).isCloseTo(12.500555, MAX_DELTA)
    }

    @Test
    fun `should create time from decimal hours`() {
        val time = Time.of(12.5005555555)
        assertThat(time.hours).isEqualTo(12)
        assertThat(time.minutes).isEqualTo(30)
        assertThat(time.seconds).isCloseTo(2.0, MAX_DELTA)
    }

    @Test
    fun `should create hour angle from decimal hours`() {
        val hourAngle = HourAngle.of(12.5005555555)
        assertThat(hourAngle.hours).isEqualTo(12)
        assertThat(hourAngle.minutes).isEqualTo(30)
        assertThat(hourAngle.seconds).isCloseTo(2.0, MAX_DELTA)
    }

    @Test
    fun `should create right ascension from decimal hours`() {
        val rightAscension = RightAscension.of(12.5005555555)
        assertThat(rightAscension.hours).isEqualTo(12)
        assertThat(rightAscension.minutes).isEqualTo(30)
        assertThat(rightAscension.seconds).isCloseTo(2.0, MAX_DELTA)
    }

    @Test
    fun `should validate decimal hour`() {
        assertThat { Time.of(-0.1) }.isFailure().hasClass(IllegalArgumentException::class)
        assertThat { HourAngle.of(-0.1) }.isFailure().hasClass(IllegalArgumentException::class)
        assertThat { RightAscension.of(-0.1) }.isFailure().hasClass(IllegalArgumentException::class)

        assertThat { Time.of(24.0) }.isFailure().hasClass(IllegalArgumentException::class)
        assertThat { HourAngle.of(24.0) }.isFailure().hasClass(IllegalArgumentException::class)
        assertThat { RightAscension.of(24.0) }.isFailure().hasClass(IllegalArgumentException::class)
    }

    @Test
    fun `should convert time to angle`() {
        assertThat(Time(1, 2, 3.0).toAngle()).isEqualTo(Angle.of(15.5125))
    }

    @Test
    fun `should convert right ascension to hour angle`() {
        val observerDateTime =
            OffsetDateTime.of(1980, 4, 22, 14, 36, 51, (0.67 * NANO_SECONDS_PER_SECOND).toInt(), ZoneOffset.ofHours(-4))
        val observerHorizontalCoordinates = GeographicCoordinates(Angle(0, 0, 0.0), Angle(-64, 0, 0.0))

        val ra = RightAscension(18, 32, 21.0)
        val hourAngle = ra.toHourAngle(observerDateTime, observerHorizontalCoordinates)

        assertThat(hourAngle.asDecimal()).isCloseTo(HourAngle(9, 52, 23.659425575076).asDecimal(), MAX_DELTA)
    }

    @Test
    fun `should convert hour angle to right ascension`() {
        val observerDateTime =
            OffsetDateTime.of(1980, 4, 22, 14, 36, 51, (0.67 * NANO_SECONDS_PER_SECOND).toInt(), ZoneOffset.ofHours(-4))
        val observerHorizontalCoordinates = GeographicCoordinates(Angle(0, 0, 0.0), Angle(-64, 0, 0.0))

        val hourAngle = HourAngle(9, 52, 23.66)
        val rightAscension = hourAngle.toRightAscension(observerDateTime, observerHorizontalCoordinates)

        assertThat(rightAscension.asDecimal()).isCloseTo(
            RightAscension(18, 32, 20.999425575076803).asDecimal(),
            MAX_DELTA
        )
    }

    @Test
    fun `should convert to string`() {
        assertThat(Time(10, 11, 12.0005).toString()).isEqualTo("10:11:12.001")
        assertThat(HourAngle(10, 11, 12.13).toString()).isEqualTo("10:11:12.130")
        assertThat(RightAscension(10, 11, 12.13).toString()).isEqualTo("10:11:12.130")
    }
}
