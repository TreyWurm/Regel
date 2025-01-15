package at.nukular.core.ui.views.calendar

import java.time.YearMonth

interface CalendarListener {
    fun monthScrolled(month: YearMonth)
}