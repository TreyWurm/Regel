package at.nukular.core.exceptions

import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureFailure] class.
 */
sealed class Failure {
    data class NetworkConnection(val throwable: Throwable) : Failure()

    data class InvalidParameter(
        val property: KProperty<*>? = null,
        val propertyClass: KClass<*>? = null,
    ) : Failure()

    data class InvalidJson(val responseBody: String? = null, val throwable: Throwable? = null) : Failure()
    data class InvalidResponse(val responseBody: String? = null, val throwable: Throwable? = null) : Failure()
    data class UnknownApiError(val throwable: Throwable) : Failure()
    data object Unsupported : Failure()

    data class MissingIntentArguments(val missingArguments: List<String>) : Failure()
    data class MissingRequiredUseCase(val throwable: Throwable? = null, val failure: Failure? = null) : Failure()

    data class MapperFailure(
        val mapper: KClass<*>,
        val property: KProperty<*>? = null,
        val propertyClass: KClass<*>? = null,
    ) : Failure()


    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure : Failure()
}


