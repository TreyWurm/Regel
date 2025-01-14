package at.nukular.core.ui.viewmodel

import kotlinx.coroutines.flow.Flow

interface UiEventViewModel<T : UiEvent> {
    val uiEvents: Flow<T>

    suspend fun sendUiEvent(uiEvent: T)
}

