package at.nukular.core.extensions

import android.content.Intent
import android.os.Build
import android.os.Parcelable
import java.io.Serializable


@Suppress("DEPRECATION")
fun <T : Parcelable> Intent?.getParcelableArrayListCompat(key: String, clazz: Class<T>): List<T>? {
    if (this == null) {
        return null
    }

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableArrayListExtra(key, clazz)
    } else {
        getParcelableArrayListExtra(key)
    }
}

@Suppress("DEPRECATION", "UNCHECKED_CAST")
fun <T : Parcelable> Intent?.getParcelableArrayCompat(key: String, clazz: Class<T>): Array<T>? {
    if (this == null) {
        return null
    }

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableArrayExtra(key, clazz)
    } else {
        (getParcelableArrayExtra(key)) as Array<T>?
    }
}

@Suppress("DEPRECATION")
fun <T : Parcelable> Intent?.getParcelableCompat(key: String, clazz: Class<T>): T? {
    if (this == null) {
        return null
    }

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableExtra(key, clazz)
    } else {
        getParcelableExtra(key)
    }
}

@Suppress("DEPRECATION", "UNCHECKED_CAST")
fun <T : Serializable> Intent?.getSerializableCompat(key: String, clazz: Class<T>): T? {
    if (this == null) {
        return null
    }

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(key, clazz)
    } else {
        getSerializableExtra(key) as? T
    }
}

fun Intent.startedFromRecentApps(): Boolean {
    return flags and Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY == Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
}