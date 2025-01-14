package at.nukular.core.extensions

import android.content.res.ColorStateList


fun Int.toColorStateList(): ColorStateList {
    return ColorStateList.valueOf(this)
}