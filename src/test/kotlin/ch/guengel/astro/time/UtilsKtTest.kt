package ch.guengel.astro.time

import assertk.assertThat
import assertk.assertions.isCloseTo
import ch.guengel.astro.coordinates.MAX_DELTA
import org.junit.jupiter.api.Test
import java.time.LocalTime

internal class UtilsKtTest {
    @Test
    fun shouldConvertToDecimalHours() {
        val decimalHours = LocalTime.of(18, 31, 27).toDecimalHours()

        assertThat(decimalHours).isCloseTo(18.524167, MAX_DELTA)
    }
}
