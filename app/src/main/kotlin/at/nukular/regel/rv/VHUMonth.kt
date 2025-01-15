package at.nukular.regel.rv

import java.time.LocalDate
import java.time.YearMonth

data class VHUMonth(
    val month: YearMonth,
    val selectedDays: Set<LocalDate>,
)