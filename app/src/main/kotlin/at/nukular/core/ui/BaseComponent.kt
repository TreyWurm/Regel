package at.nukular.core.ui

import android.os.Bundle
import androidx.lifecycle.ViewModel


interface BaseComponent :
    Arguments,
    RestoreFlowInterface {

    val viewModel: ViewModel?

    fun setupObservation()
    fun initViews(bundle: Bundle?)
    fun initRVs(bundle: Bundle?)
    fun initListeners()
}