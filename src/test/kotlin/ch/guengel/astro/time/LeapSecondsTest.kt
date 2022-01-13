package ch.guengel.astro.time

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset

internal class LeapSecondsTest {
    @Test
    fun `should count leap seconds`() {
        assertThat(LeapSeconds.forDateTime(OffsetDateTime.of(1971, 12, 31, 0, 0, 0, 0, ZoneOffset.UTC))).isEqualTo(0)
        assertThat(LeapSeconds.forDateTime(OffsetDateTime.of(1972, 7, 1, 0, 0, 0, 0, ZoneOffset.UTC))).isEqualTo(1)
        assertThat(LeapSeconds.forDateTime(OffsetDateTime.of(1972, 12, 31, 0, 0, 0, 0, ZoneOffset.UTC))).isEqualTo(2)
        assertThat(LeapSeconds.forDateTime(OffsetDateTime.of(1991, 12, 31, 0, 0, 0, 0, ZoneOffset.UTC))).isEqualTo(16)

        assertThat(LeapSeconds.forDateTime(OffsetDateTime.now())).isEqualTo(27)
    }
}
