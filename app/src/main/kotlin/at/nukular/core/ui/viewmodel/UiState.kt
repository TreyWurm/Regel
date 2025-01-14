package at.nukular.core.ui.viewmodel

import timber.log.Timber

/**
 * The UiState
 *
 * **IMPORTANT** implementing classes need to make sure they implement
 * * [kotlin.Any.equals]
 * * [kotlin.Any.hashCode]
 *
 * as [kotlinx.coroutines.flow.MutableStateFlow.compareAndSet] uses the equals function
 * to determine, if the Flow's value needs an update.
 *
 * If equals() returns true, no new value will be set/propagated to the observers
 *
 * **data class ...** may not have the appropriate behaviour, but still fulfills the interface requirements.
 * Additionally you like to have the free copy functionality from data classes, so you may like to override those 2 functions
 */
interface UiState {
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}

inline fun UiState.log() {
    Timber.i("----------------------------------------")
    Timber.i("STATE CHANGE")
    Timber.i("$this")
}