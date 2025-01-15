package at.nukular.regel.viewmodel

import java.time.LocalDate

sealed interface MainUiEvent : at.nukular.core.ui.viewmodel.UiEvent {
    data class InitialDataLoaded(val pastDates: Set<LocalDate>, val estimations: Set<LocalDate>) : MainUiEvent
    data class NewEntry(val date: LocalDate) : MainUiEvent
    data object EntryExists : MainUiEvent
}