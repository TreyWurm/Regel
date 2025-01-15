package at.nukular.core.extensions

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZonedDateTime


// ================================================================================
// region LocalDateTime

fun LocalTime?.isAfterOrEqual(toCompare: LocalTime?): Boolean {
    return if (this == null || toCompare == null) {
        false
    } else {
        return !isBefore(toCompare)
    }
}

fun LocalTime?.isBeforeOrEqual(toCompare: LocalTime?): Boolean {
    return if (this == null || toCompare == null) {
        false
    } else {
        return !isAfter(toCompare)
    }
}

fun LocalDate?.isAfterOrEqual(toCompare: LocalDate?): Boolean {
    return if (this == null || toCompare == null) {
        false
    } else {
        return !isBefore(toCompare)
    }
}

fun LocalDate?.isBeforeOrEqual(toCompare: LocalDate?): Boolean {
    return if (this == null || toCompare == null) {
        false
    } else {
        return !isAfter(toCompare)
    }
}

fun LocalDateTime?.isAfterOrEqual(toCompare: LocalDateTime?): Boolean {
    return if (this == null || toCompare == null) {
        false
    } else {
        return !isBefore(toCompare)
    }
}

fun LocalDateTime?.isBeforeOrEqual(toCompare: LocalDateTime?): Boolean {
    return if (this == null || toCompare == null) {
        false
    } else {
        return !isAfter(toCompare)
    }
}

fun LocalDate?.isToday(): Boolean {
    return if (this == null) {
        false
    } else {
        return isSameDay(LocalDate.now())
    }
}

fun LocalDateTime?.isToday(): Boolean {
    return if (this == null) {
        false
    } else {
        return isSameDay(LocalDateTime.now())
    }
}

fun LocalDate?.sameDay(toCompare: LocalDate?): Boolean {
    return if (this == null || toCompare == null) {
        false
    } else {
        return isSameDay(toCompare)
    }
}

fun LocalDateTime?.sameDay(toCompare: LocalDateTime?): Boolean {
    return if (this == null || toCompare == null) {
        false
    } else {
        return isSameDay(toCompare)
    }
}

/**
 * Check if 2 dates are the same year
 *
 * @param toCompare The date to compare
 * @return True if both dates are the same month; False otherwise
 */
fun LocalDateTime?.isSameYear(toCompare: LocalDateTime?): Boolean {
    return if (this == null || toCompare == null) {
        false
    } else {
        year == toCompare.year
    }
}

/**
 * Check if 2 dates are the same month (same year)
 *
 * @param toCompare The date to compare
 * @return True if both dates are the same month; False otherwise
 */
fun LocalDateTime?.isSameMonth(toCompare: LocalDateTime?): Boolean {
    return if (this == null || toCompare == null) {
        false
    } else {
        month == toCompare.month && year == toCompare.year
    }
}

/**
 * Check if 2 dates are the same day (same year)
 *
 * @param toCompare The date to compare
 * @return True if both dates are the same day; False otherwise
 */
fun LocalDateTime?.isSameDay(toCompare: LocalDateTime?): Boolean {
    return if (this == null || toCompare == null) {
        false
    } else {
        dayOfYear == toCompare.dayOfYear && year == toCompare.year
    }
}

fun LocalDate?.isWithinNextWeek(): Boolean {
    if (this == null) {
        return false
    }
    val now = LocalDate.now()
    return now.isBeforeOrEqual(this) && this.isBeforeOrEqual(now.plusDays(7))
}

fun LocalDateTime?.isWithinNextWeek(): Boolean {
    if (this == null) {
        return false
    }
    val now = LocalDateTime.now()
    return now.isBeforeOrEqual(this) && isBeforeOrEqual(now.plusDays(7))
}

fun LocalDate?.isWithinSameYear(toCompare: LocalDate = LocalDate.now()): Boolean {
    if (this == null) {
        return false
    }
    return isSameYear(toCompare)
}

fun LocalDateTime?.isWithinSameYear(toCompare: LocalDateTime = LocalDateTime.now()): Boolean {
    if (this == null) {
        return false
    }
    return isSameYear(toCompare)
}

fun LocalDateTime?.isWithinDateRange(start: LocalDateTime, end: LocalDateTime): Boolean {
    if (this == null) {
        return false
    }
    return (start.isBeforeOrEqual(this) && this.isBeforeOrEqual(end))
}

fun LocalDateTime?.isWithinDateTimeRange(start: LocalDateTime, end: LocalDateTime): Boolean {
    if (this == null) {
        return false
    }
    return (start.isBeforeOrEqual(this) && this.isBeforeOrEqual(end))
}

// endregion


// ================================================================================
// region ZonedDateTime

fun ZonedDateTime?.isAfterOrEqual(toCompare: ZonedDateTime?): Boolean {
    return if (this == null || toCompare == null) {
        false
    } else {
        return !isBefore(toCompare)
    }
}

fun ZonedDateTime?.isBeforeOrEqual(toCompare: ZonedDateTime?): Boolean {
    return if (this == null || toCompare == null) {
        false
    } else {
        return !isAfter(toCompare)
    }
}


fun ZonedDateTime?.isToday(): Boolean {
    return if (this == null) {
        false
    } else {
        return this.isSameDay(ZonedDateTime.now())
    }
}

fun ZonedDateTime?.isWithinNextWeek(): Boolean {
    val now = ZonedDateTime.now()
    return now.isBeforeOrEqual(this) && this.isBeforeOrEqual(now.plusDays(7))
}

fun ZonedDateTime?.isWithinSameYear(toCompare: ZonedDateTime = ZonedDateTime.now()): Boolean {
    return isSameYear(toCompare)
}


fun ZonedDateTime?.isWithinDateTimeRange(start: ZonedDateTime, end: ZonedDateTime): Boolean {
    if (this == null) {
        return false
    }
    return (start.isBeforeOrEqual(this) && this.isBeforeOrEqual(end))
}


/**
 * Check if 2 dates are the same day (same year)
 *
 * @param toCompare The date to compare
 * @return True if both dates are the same day; False otherwise
 */
fun ZonedDateTime?.isSameDay(toCompare: ZonedDateTime?): Boolean {
    return if (this == null || toCompare == null) {
        false
    } else {
        dayOfYear == toCompare.dayOfYear && year == toCompare.year
    }
}

/**
 * Check if 2 dates are the same year
 *
 * @param toCompare The date to compare
 * @return True if both dates are the same month; False otherwise
 */
fun ZonedDateTime?.isSameYear(toCompare: ZonedDateTime?): Boolean {
    return if (this == null || toCompare == null) {
        false
    } else {
        year == toCompare.year
    }
}

// endregion


// ================================================================================
// region LocalDate


fun LocalDate.weekOfMonth(): Int {
    var weekOfMonth = 1
    val yearMonth = YearMonth.from(this)
    for (i in 2..dayOfMonth) { // Skip first Monday as weekOfMonth already 1
        if (yearMonth.atDay(i).dayOfWeek == DayOfWeek.MONDAY) weekOfMonth++
    }
    return weekOfMonth
}

/**
 * Check if 2 dates are the same day (same year)
 *
 * @param toCompare The date to compare
 * @return True if both dates are the same day; False otherwise
 */
fun LocalDate?.isSameDay(toCompare: LocalDate?): Boolean {
    return if (this == null || toCompare == null) {
        false
    } else {
        dayOfYear == toCompare.dayOfYear && year == toCompare.year
    }
}

/**
 * Check if 2 dates are the same month (same year)
 *
 * @param toCompare The date to compare
 * @return True if both dates are the same month; False otherwise
 */
fun LocalDate?.isSameMonth(toCompare: LocalDate?): Boolean {
    return if (this == null || toCompare == null) {
        false
    } else {
        month == toCompare.month && year == toCompare.year
    }
}

fun LocalDate?.isSameMonth(toCompare: YearMonth?): Boolean {
    return if (this == null || toCompare == null) {
        false
    } else {
        month == toCompare.month && year == toCompare.year
    }
}

/**
 * Check if 2 dates are the same month (same year)
 *
 * @param toCompare The date to compare
 * @return True if both dates are the same month; False otherwise
 */
fun LocalDate?.isSameYear(toCompare: LocalDate?): Boolean {
    return if (this == null || toCompare == null) {
        false
    } else {
        year == toCompare.year
    }
}

fun LocalDate?.isWithinDateRange(start: LocalDate, end: LocalDate): Boolean {
    if (this == null) {
        return false
    }
    return (start.isBeforeOrEqual(this) && this.isBeforeOrEqual(end))
}

// endregion