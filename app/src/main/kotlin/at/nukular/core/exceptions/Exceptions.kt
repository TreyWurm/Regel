package at.nukular.core.exceptions

class RequiredActionFailedException(message: String? = null, cause: Throwable? = null, val failure: Failure? = null) : IllegalStateException(message, cause)
class OptionalActionFailedException(message: String? = null, cause: Throwable? = null, val failure: Failure? = null) : IllegalStateException(message, cause)