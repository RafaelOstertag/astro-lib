package ch.guengel.astro.moon

import assertk.assertThat
import assertk.assertions.isCloseTo
import ch.guengel.astro.coordinates.MAX_DELTA
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset

internal class MoonPhaseTest {
    @Test
    fun `should calculate moon phase`() {
        var observerDateTime = OffsetDateTime.of(2022, 1, 2, 12, 0, 0, 0, ZoneOffset.ofHours(1))
        assertThat(MoonPhase.percentage(observerDateTime)).isCloseTo(0.0017775, MAX_DELTA)

        observerDateTime = OffsetDateTime.of(2022, 1, 9, 18, 30, 0, 0, ZoneOffset.ofHours(1))
        assertThat(MoonPhase.percentage(observerDateTime)).isCloseTo(0.4913302, MAX_DELTA)

        observerDateTime = OffsetDateTime.of(2022, 1, 9, 18, 40, 0, 0, ZoneOffset.ofHours(1))
        assertThat(MoonPhase.percentage(observerDateTime)).isCloseTo(0.492021, MAX_DELTA)

        observerDateTime = OffsetDateTime.of(2022, 1, 25, 18, 50, 0, 0, ZoneOffset.ofHours(1))
        assertThat(MoonPhase.percentage(observerDateTime)).isCloseTo(0.484886, MAX_DELTA)

        observerDateTime = OffsetDateTime.of(2022, 1, 31, 18, 58, 0, 0, ZoneOffset.ofHours(1))
        assertThat(MoonPhase.percentage(observerDateTime)).isCloseTo(0.003830, MAX_DELTA)
    }
}
