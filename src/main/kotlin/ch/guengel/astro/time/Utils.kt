package ch.guengel.astro.time

import ch.guengel.astro.coordinates.MINUTES_PER_HOUR
import ch.guengel.astro.coordinates.NANO_SECONDS_PER_SECOND
import ch.guengel.astro.coordinates.SECONDS_PER_MINUTE
import java.time.LocalTime

internal fun LocalTime.toDecimalHours(): Double = this.let { time ->
    time.hour + ((time.minute + ((time.second + time.nano / NANO_SECONDS_PER_SECOND.toDouble()) / SECONDS_PER_MINUTE)) / MINUTES_PER_HOUR)
}
