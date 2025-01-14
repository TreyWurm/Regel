package at.nukular.core.ui

import androidx.fragment.app.Fragment
import kotlin.reflect.cast

interface ComponentInteractionListener<T> {
    var listener: T?

    fun attachComponentInteractionListener()
    fun detachComponentInteractionListener(){
        listener = null
    }
}

inline fun <reified T : Any> ComponentInteractionListener<T>.attachComponentInteractionListener(fragment: Fragment) {
    listener = try {
        when {
            fragment.parentFragment != null -> T::class.cast(fragment.parentFragment)
            else -> T::class.cast(fragment.context)
        }
    } catch (e: Exception) {
        null
    }

    if (listener == null) throw IllegalArgumentException("Parent of ${this::class.java} must implement ${T::class.java}")
}