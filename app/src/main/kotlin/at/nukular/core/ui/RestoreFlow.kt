package at.nukular.core.ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector


interface RestoreFlowInterface {
    /**
     * Flag indicating if this screen creating was due to a restore (e.g. screen rotation, killed in background, ...)
     *
     * This flag should be **set to true** as soon as you know, the screen got restored
     * and **set to FALSE** after everything has been initialized accordingly
     *
     * * [BaseActivityImpl] sets this flag in [android.app.Activity.onCreate] and [android.app.Activity.onPostCreate]
     * * [BaseFragmentImpl], [BaseBottomDialogFragmentImpl], [BaseDialogFragmentImpl] set this flag in [androidx.fragment.app.Fragment.onCreate] and [androidx.fragment.app.Fragment.onStart]
     */
    var isRestore: Boolean
}


class RestoreOnlyFlowImpl<T>(
    private val restoreInterface: RestoreFlowInterface,
    private val upstream: Flow<T>,
) : Flow<T> {
    override suspend fun collect(collector: FlowCollector<T>) {
        upstream.collect { value ->
            if (restoreInterface.isRestore) {
                collector.emit(value)
            }
        }
    }
}


/**
 * Returns flow which only upwards the values, if the [restoreFlowInterface] is doing a restore
 *
 * Note that you **PROBABLY** don't want to call this on [androidx.fragment.app.Fragment]'s [at.nukular.core.ui.viewmodel.UiState]
 * as fragments recreate their views every time
 *
 *  @see [RestoreFlowInterface.isRestore]
 */
fun <T> Flow<T>.onRestoreOnly(restoreFlowInterface: RestoreFlowInterface): Flow<T> = RestoreOnlyFlowImpl(restoreFlowInterface, this)
