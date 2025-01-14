package at.nukular.core.extensions

import androidx.lifecycle.MutableLiveData

/**
 * Updates the value of the [MutableLiveData] object, **ENSURING** the value gets only
 * updated, if it's **NOT** null
 *
 * @return [Unit]
 * @see [updateLiveData]
 * @see [updateValue]
 */
fun <T> MutableLiveData<T>.update(function: (T) -> T) {
    value?.let(function)
}

/**
 * Updates the value of the [MutableLiveData] object, **ENSURING** the value gets only
 * updated, if it's **NOT** null
 *
 * @return The [MutableLiveData] object itself
 * @see [update]
 * @see [updateValue]
 */
fun <T> MutableLiveData<T>.updateLiveData(function: (T) -> T): MutableLiveData<T> {
    value?.let(function)
    return this
}

/**
 * Updates the value of the [MutableLiveData] object, **ENSURING** the value gets only
 * updated, if it's **NOT** null
 *
 * @return The updated value if present; null otherwise
 * @see [update]
 * @see [updateLiveData]
 */
inline fun <T> MutableLiveData<T>.updateValue(function: (T) -> T): T? {
    return value?.let(function)
}

