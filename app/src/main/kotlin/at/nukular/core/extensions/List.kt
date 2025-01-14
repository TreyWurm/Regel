package at.nukular.core.extensions

fun <T> List<T>.forFirstItems(number: Int, block: (index: Int, item: T, subListSize: Int, totalSize: Int) -> Unit) {
    take(number)
        .let { sublist ->
            sublist.forEachIndexed { index, t ->
                block(index, t, sublist.size, this.size)
            }
        }
}

fun <T> List<T>.replaceFirst(newValue: (T) -> T, predicate: (T) -> Boolean): List<T> {
    val mutable = toMutableList()
    val index = mutable.indexOfFirst { predicate(it) }
    if (index != -1) {
        mutable[index] = newValue(mutable[index])
    }
    return mutable
}

fun <T> List<T>.replaceAll(newValue: (T) -> T, predicate: (T) -> Boolean): List<T> {
    val mutable = toMutableList()
    mutable.forEachIndexed { index, item ->
        if (predicate(item)) {
            mutable[index] = newValue(item)
        }
    }
    return mutable
}