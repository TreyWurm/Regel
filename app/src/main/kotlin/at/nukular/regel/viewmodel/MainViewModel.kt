package at.nukular.regel.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import at.nukular.core.extensions.contains
import at.nukular.core.ui.viewmodel.CoroutineViewModelImpl
import at.nukular.core.ui.viewmodel.StateObjectViewModel
import at.nukular.core.ui.viewmodel.UiEventViewModel
import at.nukular.regel.FileWriterReader
import at.nukular.regel.model.VHUEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
            sendUiEvent(MainUiEvent.InitialDataLoaded(fileWriterReader.readEntriesFromFile().map { VHUEntry(it) }.toSet()))
        }
    }

    override suspend fun sendUiEvent(uiEvent: MainUiEvent) {
        when (uiEvent) {
            is MainUiEvent.EntryExists -> {}
            is MainUiEvent.InitialDataLoaded -> stateObject.updateVmState { it.copy(vhus = uiEvent.vhus) }
            is MainUiEvent.NewEntry -> stateObject.updateVmState { it.copy(vhus = (it.vhus + VHUEntry(uiEvent.date)).sortedByDescending { it.date }.toSet()) }
        }

        _uiEvents.send(uiEvent)
    }


    fun onUserAction(userAction: MainUserAction) {
        when (userAction) {
            is MainUserAction.NewDateSelected -> viewModelScope.launch { newDateSelectedActionImpl(userAction) }
        }
    }

    private suspend fun newDateSelectedActionImpl(userAction: MainUserAction.NewDateSelected) {
        when (stateObject.vhus.contains { it.date == userAction.date }) {
            true -> sendUiEvent(MainUiEvent.EntryExists)
            false -> {
                fileWriterReader.addEntry(userAction.date)
                sendUiEvent(MainUiEvent.NewEntry(userAction.date))
            }
        }
    }
}