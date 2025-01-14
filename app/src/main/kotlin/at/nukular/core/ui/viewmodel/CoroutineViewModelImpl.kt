package at.nukular.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import at.nukular.core.exceptions.Failure
import at.nukular.core.exceptions.OptionalActionFailedException
import at.nukular.core.exceptions.RequiredActionFailedException
import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber

abstract class CoroutineViewModelImpl : ViewModel() {

    protected val requiredJobsExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.w(
            "Required job caught: ${
                when (throwable) {
                    is RequiredActionFailedException -> throwable.failure.toString()
                    else -> throwable.message
                }
            }"
        )
        requiredUseCaseFailed(Failure.MissingRequiredUseCase(throwable = throwable, failure = (throwable as? RequiredActionFailedException)?.failure))
    }

    protected val optionalJobsExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.w(
            "Optional job caught: ${
                when (throwable) {
                    is OptionalActionFailedException -> throwable.failure.toString()
                    else -> throwable.message
                }
            }"
        )
    }

    protected fun requiredUseCaseFailed(failure: Failure) {
    }
}
