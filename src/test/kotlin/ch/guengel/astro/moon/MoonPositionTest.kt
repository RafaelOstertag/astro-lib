package ch.guengel.astro.moon

import assertk.assertThat
import assertk.assertions.isCloseTo
import ch.guengel.astro.coordinates.MAX_DELTA
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset

internal class MoonPositionTest {
    @Test
    fun `should get moon position`() {
        val observerDateTime = OffsetDateTime.of(2015, 1, 1, 22, 0, 0, 0, ZoneOffset.ofHours(-5))

        val moonEquatorialCoordinates = MoonPosition.position(observerDateTime)
        assertThat(moonEquatorialCoordinates.rightAscension.asDecimal()).isCloseTo(4.257664, MAX_DELTA)
        assertThat(moonEquatorialCoordinates.declination.asDecimal()).isCloseTo(17.2469998, MAX_DELTA)
    }
}
