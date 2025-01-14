package at.nukular.core.extensions

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import androidx.core.os.BundleCompat
import java.io.Serializable

@Suppress("DEPRECATION")
fun <T : Parcelable> Bundle?.getParcelableCompat(key: String, clazz: Class<T>): T? {
    if (this == null) {
        return null
    }

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        BundleCompat.getParcelable(this, key, clazz)
    } else {
        getParcelable(key)
    }
}

@Suppress("DEPRECATION")
fun <T : Parcelable> Bundle?.getParcelableArrayCompat(key: String, clazz: Class<T>): Array<out Parcelable>? {
    if (this == null) {
        return null
    }

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        BundleCompat.getParcelableArray(this, key, clazz)
    } else {
        getParcelableArray(key)
    }
}

@Suppress("DEPRECATION")
fun <T : Parcelable> Bundle?.getParcelableArrayListCompat(key: String, clazz: Class<T>): List<T>? {
    if (this == null) {
        return null
    }

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
       BundleCompat.getParcelableArrayList(this, key, clazz)
    } else {
        getParcelableArrayList(key)
    }
}


@Suppress("DEPRECATION")
fun <T : Parcelable> Bundle?.getSparseParcelableArrayCompat(key: String, clazz: Class<T>): SparseArray<T>? {
    if (this == null) {
        return null
    }

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
       BundleCompat.getSparseParcelableArray(this,key, clazz)
    } else {
        getSparseParcelableArray(key)
    }
}

@Suppress("DEPRECATION", "UNCHECKED_CAST")
fun <T : Serializable> Bundle?.getSerializableCompat(key: String, clazz: Class<T>): T? {
    if (this == null) {
        return null
    }

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, clazz)
    } else {
        getSerializable(key) as? T
    }
}