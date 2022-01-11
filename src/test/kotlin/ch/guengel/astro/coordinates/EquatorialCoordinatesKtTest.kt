package ch.guengel.astro.coordinates

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset

internal class EquatorialCoordinatesKtTest {

    @Test
    fun `should convert horizontal coordinates`() {
        val observerDateTime = OffsetDateTime.of(2022, 1, 5, 18, 15, 28, 0, ZoneOffset.ofHours(+1))
        val observerCoordinates = GeographicCoordinates(Angle(47, 32, 53.0), Angle(8, 50, 7.0))
        val spica = EquatorialCoordinates(RightAscension(13, 26, 20.2), Angle(-11, 16, 26.70))

        val horizonCoordinates = spica.toHorizonCoordinates(observerCoordinates, observerDateTime)
        assertThat(horizonCoordinates).isEqualTo(
            HorizontalCoordinates(
                Angle(-53, 0, 40.273091456961595),
                Angle(345, 53, 27.87733463028)
            )
        )
    }
}
