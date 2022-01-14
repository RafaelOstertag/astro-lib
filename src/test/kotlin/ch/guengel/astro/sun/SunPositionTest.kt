package ch.guengel.astro.sun

import assertk.assertThat
import assertk.assertions.isCloseTo
import ch.guengel.astro.coordinates.MAX_DELTA
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset

internal class SunPositionTest {
    @Test
    fun `should get sun position`() {
        val observerTime = OffsetDateTime.of(2015, 2, 5, 12, 0, 0, 0, ZoneOffset.ofHours(-5))
        val equatorialCoordinates = SunPosition.position(observerTime)
        assertThat(equatorialCoordinates.rightAscension.asDecimal()).isCloseTo(21.267773, MAX_DELTA)
        assertThat(equatorialCoordinates.declination.asDecimal()).isCloseTo(-15.871231, MAX_DELTA)
    }
}
