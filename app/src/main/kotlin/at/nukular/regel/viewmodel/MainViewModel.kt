package at.nukular.regel.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import at.nukular.core.extensions.contains
import at.nukular.core.ui.viewmodel.CoroutineViewModelImpl
import at.nukular.core.ui.viewmodel.StateObjectViewModel
import at.nukular.core.ui.viewmodel.UiEventViewModel
import at.nukular.regel.FileWriterReader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val fileWriterReader: FileWriterReader,
) : CoroutineViewModelImpl(),
    StateObjectViewModel<MainUiState>,
    UiEventViewModel<MainUiEvent> {

    override var stateObject: MainUiState = MainUiState()
    private val _uiState = MutableStateFlow(stateObject)
    override val uiState = _uiState as StateFlow<MainUiState>

    private val _uiEvents = Channel<MainUiEvent>()
    override val uiEvents = _uiEvents.receiveAsFlow()


    override fun persistStateObject() {
        _uiState.update { stateObject }
    }

    init {
        viewModelScope.launch {
            val pastDates = fileWriterReader.readEntriesFromFile().toSet()
            sendUiEvent(MainUiEvent.InitialDataLoaded(estimations = estimatedNextPeriods(pastDates), pastDates = pastDates))
        }
    }

    override suspend fun sendUiEvent(uiEvent: MainUiEvent) {
        when (uiEvent) {
            is MainUiEvent.EntryExists -> {}
            is MainUiEvent.InitialDataLoaded -> stateObject.updateVmState { it.copy(estimations = uiEvent.estimations, pastDates = uiEvent.pastDates) }
            is MainUiEvent.NewEntry -> stateObject.updateVmState { it.copy(pastDates = (it.pastDates + uiEvent.date).sortedDescending().toSet()) }
        }

        _uiEvents.send(uiEvent)
    }


    fun onUserAction(userAction: MainUserAction) {
        when (userAction) {
            is MainUserAction.NewDateSelected -> viewModelScope.launch { newDateSelectedActionImpl(userAction) }
        }
    }

    private suspend fun newDateSelectedActionImpl(userAction: MainUserAction.NewDateSelected) {
        when (stateObject.pastDates.contains { it == userAction.date }) {
            true -> sendUiEvent(MainUiEvent.EntryExists)
            false -> {
                fileWriterReader.addEntry(userAction.date)
                sendUiEvent(MainUiEvent.NewEntry(userAction.date))
            }
        }
    }

    private fun estimatedNextPeriods(pastDates: Set<LocalDate>): Set<LocalDate> {
        val daysBetween = mutableListOf<Int>()
        pastDates.forEachIndexed { index, date ->
            if (index + 1 > pastDates.size - 1) {
                return@forEachIndexed
            }

            val toCompare = pastDates.elementAt(index + 1)
            daysBetween.add(ChronoUnit.DAYS.between(toCompare, date).toInt())
        }

        Timber.i("estimatedNextPeriod: $daysBetween")

        val averageDaysBetween = daysBetween.average().toLong()
        val start = pastDates.firstOrNull() ?: return setOf(LocalDate.now())
        val nextPeriods = mutableSetOf<LocalDate>()
        for (i in 1..24) {
            nextPeriods.add(start.plusDays(averageDaysBetween * i))
        }

        return nextPeriods
    }
}