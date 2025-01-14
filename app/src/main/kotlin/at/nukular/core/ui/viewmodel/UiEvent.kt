package at.nukular.core.ui.viewmodel

import timber.log.Timber

interface UiEvent


inline fun UiEvent.log() {
    Timber.i("----------------------------------------")
    Timber.i("EVENT")
    Timber.i("${this::class.simpleName}")
}