package at.nukular.core.extensions

import arrow.core.Either
import at.nukular.core.exceptions.Failure
import at.nukular.core.exceptions.OptionalActionFailedException
import at.nukular.core.exceptions.RequiredActionFailedException


fun <L, R> Either<L, List<R>>.getOrEmptyList(): List<R> =
    when (this) {
        is Either.Left -> listOf()
        is Either.Right -> value
    }

fun <L, R> Either<L, Set<R>>.getOrEmptySet(): Set<R> =
    when (this) {
        is Either.Left -> setOf()
        is Either.Right -> value
    }

fun <L, RK, RV> Either<L, Map<RK, RV>>.getOrEmptyMap(): Map<RK, RV> =
    when (this) {
        is Either.Left -> mapOf()
        is Either.Right -> value
    }

fun <L, R> Either<L, R>.getOrThrow(exception: Exception): R =
    when (this) {
        is Either.Left -> throw exception
        is Either.Right -> value
    }

fun <L, R> Either<L, R>.getOrThrow(wrapperExceptionBuilder: (L) -> Exception): R =
    when (this) {
        is Either.Left -> throw wrapperExceptionBuilder(value)
        is Either.Right -> value
    }

@Throws(RequiredActionFailedException::class)
fun <T> Either<Failure, T>.required(): T {
    return getOrThrow { RequiredActionFailedException(failure = it) }
}

@Throws(OptionalActionFailedException::class)
fun <T> Either<Failure, T>.optional(): T {
    return getOrThrow { OptionalActionFailedException(failure = it) }
}
