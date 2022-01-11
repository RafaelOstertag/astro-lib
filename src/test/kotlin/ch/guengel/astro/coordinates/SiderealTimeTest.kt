package ch.guengel.astro.coordinates

import assertk.assertThat
import assertk.assertions.isCloseTo
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset

internal class SiderealTimeTest {
    @Test
    fun shouldCalculateGst() {
        val gst = OffsetDateTime.of(1969, 1, 5, 20, 5, 0, 0, ZoneOffset.ofHours(-5)).toGST()
        assertThat(gst.toDecimalHours()).isCloseTo(8.112740, MAX_DELTA)

        val gst1 =
            OffsetDateTime.of(1980, 4, 22, 14, 36, 51, (0.67 * NANO_SECONDS_PER_SECOND).toInt(), ZoneOffset.UTC).toGST()
        assertThat(gst1.hours).isEqualTo(4)
        assertThat(gst1.minutes).isEqualTo(40)
        assertThat(gst1.seconds).isCloseTo(5.233544675, MAX_DELTA)
    }

    @Test
    fun shouldCalculateLST() {
        val gst = Time(4, 40, 5.23)
        val lst = gstToLst(gst, GeographicCoordinates(Angle(0, 0, 0.0), Angle(-64, 0, 0.0)))

        assertThat(lst.hours).isEqualTo(0)
        assertThat(lst.minutes).isEqualTo(24)
        assertThat(lst.seconds).isCloseTo(5.23, MAX_DELTA)

        val gst1 = Time(8, 6, 45.8655332206644)
        val lst1 = gstToLst(gst1, GeographicCoordinates(Angle(0, 0, 0.0), Angle(-81, 23, 0.0)))
        assertThat(lst1.toDecimalHours()).isCloseTo(2.687184, MAX_DELTA)
    }
}
