package at.nukular.regel.viewmodel

import at.nukular.regel.model.VHUEntry
import java.time.LocalDate

sealed interface MainUiEvent : at.nukular.core.ui.viewmodel.UiEvent {
    data class InitialDataLoaded(val vhus: Set<VHUEntry>) : MainUiEvent
    data class NewEntry(val date: LocalDate) : MainUiEvent
    data object EntryExists : MainUiEvent
}