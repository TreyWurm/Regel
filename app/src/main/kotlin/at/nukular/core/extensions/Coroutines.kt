package at.nukular.core.extensions

import arrow.core.Either
import at.nukular.core.exceptions.Failure
import at.nukular.core.exceptions.RequiredActionFailedException
import kotlinx.coroutines.Deferred
import timber.log.Timber

/**
 * Calls [Deferred.await] for a required value
 *
 * If the deferred computation yields an exception or results in an [Either.Left] the function
 * re-throws a [RequiredActionFailedException] wrapped around the root cause
 *
 * @return Result of type [Type]; throws exception otherwise
 * @throws RequiredActionFailedException
 */
suspend inline fun <reified Type, T : Either<Failure, Type>> Deferred<T>.awaitRequired(): Type {
    return try {
        val result = await()
        result.getOrThrow { RequiredActionFailedException(failure = it) }
    } catch (e: Exception) {
        Timber.w("Failed to await required deferred value of type: ${Type::class.qualifiedName}")
        throw if (e !is RequiredActionFailedException) {
            RequiredActionFailedException(cause = e)
        } else {
            Timber.w("${e.failure}")
            e
        }
    }
}


/**
 * Calls [Deferred.await] for an optional value
 *
 * If the deferred computation yields an exception or results in an [Either.Left] the function
 * returns null, consuming/catching potential thrown exceptions
 *
 * @return Result of type [Type]; null otherwise
 */
suspend inline fun <reified Type, T : Either<Failure, Type>> Deferred<T>.awaitOptional(): Type? {
    return try {
        val result = await()
        result.onLeft {
            Timber.w("Optional deferred value of type: ${Type::class.qualifiedName} yielded a failure")
            Timber.w("$it")
        }
        result.getOrNull()
    } catch (e: Exception) {
        Timber.w("Failed to await optional deferred value of type: ${Type::class.qualifiedName}")
        e.printStackTrace()
        null
    }
}

/**
 * Calls [Deferred.await] for an optional value
 *
 * If the deferred computation yields an exception or results in an [Either.Left] the function
 * returns an empty list, consuming/catching potential thrown exceptions
 *
 * @return Result of list of type [LT]; empty list otherwise
 */
suspend inline fun <reified LT : Any, T : Either<Failure, List<LT>>> Deferred<T>.awaitOptionalList(): List<LT> {
    return try {
        val result = await()
        result.onLeft {
            Timber.w("Optional deferred list of type: ${LT::class.qualifiedName} yielded a failure")
            Timber.w("$it")
        }
        result.getOrEmptyList()
    } catch (e: Exception) {
        Timber.w("Failed to await optional deferred list of type: ${LT::class.qualifiedName}")
        e.printStackTrace()
        emptyList()
    }
}

