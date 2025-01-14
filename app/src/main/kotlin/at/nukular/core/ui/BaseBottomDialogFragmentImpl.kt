package at.nukular.core.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import at.nukular.core.ui.viewmodel.StateObjectViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import timber.log.Timber

abstract class BaseBottomDialogFragmentImpl :
    BottomSheetDialogFragment(),
    Arguments,
    BaseComponent {


    override var isRestore: Boolean = false


    override fun onAttach(context: Context) {
        super.onAttach(context)
        attachComponentInteractionListener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.i("onCreate: ${this::class.java.simpleName} savedInstanceState: $savedInstanceState")

        // Always define if this was a restore or not
        isRestore = savedInstanceState != null

        if (savedInstanceState != null) {
            // Restore should not return false in normal case, but if activity is destroyed before parsing the arguments in intentValid() and then null written to savedInstanceState
            if (!savedInstanceStateValid(savedInstanceState)) {
                Timber.w("SavedInstanceState arguments not valid. Going to try parsing the intent arguments")

                // In case restore did not succeed, try to parse data from the intent
                if (!intentArgumentsValid(arguments)) {
                    throw IllegalArgumentException("Neither savedInstanceState, nor intent arguments valid")
                }
            }
        } else {
            if (!intentArgumentsValid(arguments)) {
                throw IllegalArgumentException("Intent arguments not valid")
            }
        }

        super.onCreate(savedInstanceState)
        setupObservation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.i("onViewCreated: ${this::class.java.simpleName} savedInstanceState: $savedInstanceState")
        super.onViewCreated(view, savedInstanceState)
        (view.parent as View).setBackgroundColor(Color.TRANSPARENT)
        initViews(savedInstanceState)
        initRVs(savedInstanceState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Timber.i("onViewStateRestored: ${this::class.java.simpleName}")
        super.onViewStateRestored(savedInstanceState)

        initListeners()
    }

    override fun onStart() {
        super.onStart()

        // Reset flag afterwards
        isRestore = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Timber.i("onSaveInstanceState: ${this::class.java.simpleName}")
        (viewModel as? StateObjectViewModel<*>)?.persistStateObject()
        super.onSaveInstanceState(outState)
    }

    override fun onDetach() {
        detachComponentInteractionListener()
        super.onDetach()
    }

    override fun onDestroyView() {
        Timber.i("onDestroyView: ${this::class.java.simpleName}")
        (viewModel as? StateObjectViewModel<*>)?.persistStateObject()

        super.onDestroyView()
    }

    override fun onDestroy() {
        Timber.i("onDestroy: ${this::class.java.simpleName}")

        super.onDestroy()
    }

    override fun initListeners() {}
    override fun initRVs(bundle: Bundle?) {}
    override fun initViews(bundle: Bundle?) {}
    override fun setupObservation() {}


    // ================================================================================
    // region ComponentInteraction

    private fun attachComponentInteractionListener() {
        (this as? ComponentInteractionListener<*>)?.attachComponentInteractionListener()
    }

    private fun detachComponentInteractionListener() {
        (this as? ComponentInteractionListener<*>)?.detachComponentInteractionListener()
    }
    // endregion
}