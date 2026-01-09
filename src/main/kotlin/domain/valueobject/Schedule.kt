package domain.valueobject

import java.time.DayOfWeek
import java.time.LocalTime

data class Schedule(
    private val openingTime: LocalTime,
    private val closingTime: LocalTime,
    private val openDays: Set<DayOfWeek>
) {

    fun isOpenAt(day: DayOfWeek, time: LocalTime): Boolean {
        if (!openDays.contains(day)) return false
        return time >= openingTime && time < closingTime
    }

    companion object {
        fun of(
            openingTime: LocalTime,
            closingTime: LocalTime,
            openDays: Set<DayOfWeek>
        ): Schedule = Schedule(openingTime, closingTime, openDays)
    }
}
