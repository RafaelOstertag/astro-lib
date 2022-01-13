package ch.guengel.astro.coordinates

import assertk.assertThat
import assertk.assertions.isCloseTo
import org.junit.jupiter.api.Test
import kotlin.math.PI

internal class UtilsKtTest {

    @Test
    fun shouldGetFractionalPart() {
        assertThat((0.0).getFractionalPart()).isCloseTo(0.0, MAX_DELTA)
        assertThat((0.5).getFractionalPart()).isCloseTo(0.5, MAX_DELTA)
        assertThat((-0.5).getFractionalPart()).isCloseTo(-0.5, MAX_DELTA)
        assertThat((1.0 / 3.0).getFractionalPart()).isCloseTo(0.33333333, MAX_DELTA)
    }

    @Test
    fun shouldConvertDegreesToRadians() {
        assertThat((180.0).toRadians()).isCloseTo(PI, MAX_DELTA)
        assertThat((-180.0).toRadians()).isCloseTo(-PI, MAX_DELTA)
        assertThat((45.0).toRadians()).isCloseTo(RADIANS_PER_DEGREE * 45.0, MAX_DELTA)
    }
}
