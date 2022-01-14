package ch.guengel.astro

import assertk.assertThat
import assertk.assertions.isCloseTo
import ch.guengel.astro.coordinates.MAX_DELTA
import org.junit.jupiter.api.Test

internal class UtilsKtTest {
    @Test
    fun `should compute arctan corrected`() {
        assertThat(arcTanCorrectedDeg(0.5, 1.0)).isCloseTo(63.434948, MAX_DELTA)
        assertThat(arcTanCorrectedDeg(-0.5, -1.0)).isCloseTo(243.434949, MAX_DELTA)
        assertThat(arcTanCorrectedDeg(-0.5, 1.0)).isCloseTo(116.565051, MAX_DELTA)
        assertThat(arcTanCorrectedDeg(0.5, -1.0)).isCloseTo(296.565051, MAX_DELTA)
    }
}
