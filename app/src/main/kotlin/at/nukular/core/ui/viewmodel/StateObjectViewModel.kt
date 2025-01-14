package at.nukular.core.ui.viewmodel

import kotlinx.coroutines.flow.StateFlow


/**
 * ====================================================================
 *
 * **CURRENT THOUGHTS**
 *
 * 1. Maybe we do not want the UI to collect the [uiState] for changes to the UI, but only use it to re the UI's state.
 * Changes to the UI should maybe be propagated via [UiEvent]. The [StateObjectViewModel] exists so we always have the UI's current state
 * stored, that can be used to recreate on orientation changes ([uiState]) and activity/fragment restore (([androidx.lifecycle.SavedStateHandle])
 *
 * 2. Not 100% thought through. UI changes that originate from an user action (scrolling, typing in a query, ...) should
 * **not** trigger an [UiEvent]. [UiState] changes should only be caused
 *
 *
 * ====================================================================
 *
 *
 *
 * Interface for [androidx.lifecycle.ViewModel] that want to have a stateful approach
 *
 * * The views current state is represented by the [stateObject]
 * * The [uiState] provides a collectable [kotlinx.coroutines.flow.StateFlow] that has the [stateObject] as value
 *
 * Use the [StateObjectViewModel] to maintain the UI state when doing recreation of the UI only. So ViewModel outlives the UI
 * (e.g. orientation changes)
 *
 * Use the ViewModels [androidx.lifecycle.SavedStateHandle] when you need to store data (e.g. search queries; ...)
 * that needs to be persisted even after ViewModels destruction (e.g. restore after app was killed in the background)
 *
 *
 *
 * ***!!! IMPORTANT !!!***
 *
 * It could happen (you probably even prefer) that you do not want to update [uiState] for changes that affect only
 * parts of a specific view or even a single item (e.g. a single RecyclerView's item updates its content; more
 * paginated items get loaded; ...) In that case you do NOT want to notify the collectors of a new value, as they
 * could probably not handle those fine actions well.
 *
 * **Instead** use the [StateObjectViewModel] in combination with [UiEventViewModel]. The [UiEvent] can be used to
 * notify the collectors about smaller/fine grained updates to the UI.
 *
 * You still have to update the [stateObject] and make sure to update the [uiState] when the UI gets destroyed, to
 * make sure you have the latest state (e.g. screen orientation change). [persistStateObject] can be used to do so.
 * [at.nukular.core.ui.BaseFragmentImpl] already calls this function in its onDestroyView() method. If not
 * extending from it make sure to call it yourself at an appropriate time
 */
interface StateObjectViewModel<T : UiState> {
    /**
     * The current state of the ViewModel
     *
     * Making this one var to have a default [updateVmState] function
     *
     * At the cost of having a public setter ;(
     */
    var stateObject: T

    /**
     * Collectable flow that has the state as value
     *
     * [stateObject] and [uiState]'s value might differ
     *
     * see [StateObjectViewModel]
     */
    val uiState: StateFlow<T>


    /**
     * Use this function to update the [uiState] to the current [stateObject]
     *
     * Only call this **AFTER** you disconnected your observers (see [StateObjectViewModel])
     *
     * This
     */
    fun persistStateObject()

    fun T.updateVmState(block: (T) -> T) {
        stateObject = block(this)
    }
}


