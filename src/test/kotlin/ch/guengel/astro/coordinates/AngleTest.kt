package ch.guengel.astro.coordinates

import assertk.assertThat
import assertk.assertions.*
import org.junit.jupiter.api.Test

internal class AngleTest {
    @Test
    fun `should convert to decimal degree`() {
        val positiveAngle = Angle(182, 31, 27.0)
        assertThat(positiveAngle.asDecimal()).isCloseTo(182.524167, MAX_DELTA)

        val zeroAngle = Angle(0, 0, 0.0)
        assertThat(zeroAngle.asDecimal()).isCloseTo(0.0, MAX_DELTA)

        val negativeAngle = Angle(-182, 31, 27.0)
        assertThat(negativeAngle.asDecimal()).isCloseTo(-182.524167, MAX_DELTA)
    }

    @Test
    fun shouldConvertFromDecimal() {
        val positiveAngle = Angle.of(182.524167)
        assertThat(positiveAngle.degrees).isEqualTo(182)
        assertThat(positiveAngle.arcMinutes).isEqualTo(31)
        assertThat(positiveAngle.arcSeconds).isCloseTo(27.001200, MAX_DELTA)

        val zeroAngle = Angle.of(0.0)
        assertThat(zeroAngle.degrees).isEqualTo(0)
        assertThat(zeroAngle.arcMinutes).isEqualTo(0)
        assertThat(zeroAngle.arcSeconds).isCloseTo(0.0, MAX_DELTA)

        val negativeAngle = Angle.of(-182.524167)
        assertThat(negativeAngle.degrees).isEqualTo(-182)
        assertThat(negativeAngle.arcMinutes).isEqualTo(31)
        assertThat(negativeAngle.arcSeconds).isCloseTo(27.001200, MAX_DELTA)
    }

    @Test
    fun `should validate input`() {
        assertThat { Angle(-359, 0, 0.0) }.isSuccess()
        assertThat { Angle(359, 0, 0.0) }.isSuccess()
        assertThat { Angle(-360, 0, 0.0) }.isFailure().hasClass(IllegalArgumentException::class)
        assertThat { Angle(360, 0, 0.0) }.isFailure().hasClass(IllegalArgumentException::class)

        assertThat { Angle(0, 0, 0.0) }.isSuccess()
        assertThat { Angle(0, 59, 0.0) }.isSuccess()
        assertThat { Angle(0, -1, 0.0) }.isFailure().hasClass(IllegalArgumentException::class)
        assertThat { Angle(0, 60, 0.0) }.isFailure().hasClass(IllegalArgumentException::class)

        assertThat { Angle(0, 0, 0.0) }.isSuccess()
        assertThat { Angle(0, 0, 59.999999) }.isSuccess()
        assertThat { Angle(0, 0, -0.1) }.isFailure().hasClass(IllegalArgumentException::class)
        assertThat { Angle(0, 0, 60.0) }.isFailure().hasClass(IllegalArgumentException::class)
    }

    @Test
    fun `should correctly convert to string`() {
        assertThat(Angle(1, 1, 1.00005).toString()).isEqualTo("+001°01'01.0001\"")
        assertThat(Angle(1, 1, 0.0).toString()).isEqualTo("+001°01'00.0000\"")
        assertThat(Angle(-1, 1, 0.0).toString()).isEqualTo("-001°01'00.0000\"")
        assertThat(Angle(-359, 59, 59.0001).toString()).isEqualTo("-359°59'59.0001\"")
    }


    @Test
    fun `should compare to`() {
        assertThat(
            Angle(1, 0, 1.1) > Angle(1, 0, 1.0)
        ).isTrue()

        assertThat(
            Angle(1, 0, 1.0) < Angle(1, 0, 1.1)
        ).isTrue()

        assertThat(
            Angle(1, 0, 1.1) == Angle(1, 0, 1.1)
        ).isTrue()

        assertThat(
            Angle(-1, 0, 1.1) < Angle(-1, 0, 1.0)
        ).isTrue()

        assertThat(
            Angle(-1, 0, 1.0) > Angle(-1, 0, 1.1)
        ).isTrue()
    }
}
