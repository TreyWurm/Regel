package at.nukular.core.extensions

inline fun <T> Collection<T>?.isNotNullOrEmpty(): Boolean {
    return !isNullOrEmpty()
}

inline fun <T> Collection<T>?.indicesOf(predicate: (T) -> Boolean): List<Int> {
    if (this.isNullOrEmpty()) {
        return emptyList()
    }

    val indices = mutableListOf<Int>()
    forEachIndexed { index, item -> if (predicate(item)) indices.add(index) }
    return indices
}

fun <T> Collection<T>.contains(predicate: (T) -> Boolean): Boolean {
    return find { predicate(it) } != null
}


fun <T> Array<T>.contains(predicate: (T) -> Boolean): Boolean {
    return find { predicate(it) } != null
}