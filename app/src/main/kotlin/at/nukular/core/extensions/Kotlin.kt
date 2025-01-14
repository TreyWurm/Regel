package at.nukular.core.extensions

inline fun <T> T.with(block: (T) -> Unit): T {
    also(block)
    return this
}