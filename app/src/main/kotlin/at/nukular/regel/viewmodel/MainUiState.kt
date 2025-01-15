package at.nukular.regel.viewmodel

import java.time.LocalDate

data class MainUiState(
    val estimations: Set<LocalDate> = emptySet(),
    val pastDates: Set<LocalDate> = emptySet(),
) : at.nukular.core.ui.viewmodel.UiState