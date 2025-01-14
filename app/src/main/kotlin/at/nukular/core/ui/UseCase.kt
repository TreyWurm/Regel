package at.nukular.core.ui

import arrow.core.Either
import at.nukular.core.exceptions.Failure
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface UseCase<in Params, out Output> {
    val dispatcher: CoroutineDispatcher

    suspend fun execute(params: Params): Either<Failure, Output>

    suspend operator fun invoke(params: Params): Either<Failure, Output> {
        return withContext(dispatcher) {
            execute(params)
        }
    }

    object None
}
