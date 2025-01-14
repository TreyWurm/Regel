package at.nukular.core.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import at.nukular.core.ui.listeners.toolbar.ToolbarListener
import at.nukular.core.ui.viewmodel.StateObjectViewModel
import timber.log.Timber

abstract class BaseActivityImpl : AppCompatActivity(), BaseComponent, ToolbarListener, RefreshComponent {


    override var isRestore: Boolean = false

    override var doRefreshData: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.i("onCreate: ${this::class.java.simpleName} savedInstanceState: $savedInstanceState")


        // Always define if this was a restore or not
        isRestore = savedInstanceState != null

        // Check if intent/arguments are valid
        // If not throws exception
        if (savedInstanceState != null) {
            // Restore should not return false in normal case, but if activity is destroyed before parsing the arguments in intentValid() and then null written to savedInstanceState
            if (!savedInstanceStateValid(savedInstanceState)) {
                Timber.w("SavedInstanceState arguments not valid. Going to try parsing the intent arguments")

                // In case restore did not succeed, try to parse data from the intent
                if (!intentArgumentsValid(intent.extras)) {
                    throw IllegalArgumentException("Neither savedInstanceState, nor intent arguments valid")
                }
            }
        } else {
            if (!intentArgumentsValid(intent.extras)) {
                throw IllegalArgumentException("Intent arguments not valid")
            }
        }

        super.onCreate(savedInstanceState)

        setupObservation()
        val rootView = createViewBindings()
        if (rootView != null) {
            setContentView(rootView)
        }
        initViews(savedInstanceState)
        initListeners()
        initRVs(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Reset flag afterwards
        isRestore = false
    }


    override fun onResume() {
        Timber.i("onResume: ${javaClass.simpleName}")
        super.onResume()

        if (doRefreshData) {
            refreshData()
        }

        doRefreshData = true
    }

    override fun onPause() {
        Timber.i("onPause: ${javaClass.simpleName}")

        super.onPause()
    }

    override fun onStop() {
        Timber.i("onStop: ${javaClass.simpleName}")

        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Timber.i("onSaveInstanceState: ${javaClass.simpleName}")
        (viewModel as? StateObjectViewModel<*>)?.persistStateObject()

        super.onSaveInstanceState(outState)

    }

    override fun onDestroy() {
        Timber.i("onDestroy: ${this::class.java.simpleName}")

        super.onDestroy()
    }


    // ================================================================================
    // region ToolbarListener

    override fun onNavigationBackClicked() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun onActionClicked(id: Int) {}
    // endregion


    abstract fun createViewBindings(): View?
    override fun initListeners() {}
    override fun initRVs(bundle: Bundle?) {}
    override fun initViews(bundle: Bundle?) {}
    override fun setupObservation() {}
}