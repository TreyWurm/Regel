package at.nukular.core.extensions

import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics
import android.util.TypedValue
import kotlin.math.floor

object AppConfig {
    var density = 1f
    var displayMetrics = DisplayMetrics()

    fun onConfigChanged(context: Context, newConfiguration: Configuration?) {
        density = context.resources.displayMetrics.density
        displayMetrics = context.resources.displayMetrics
    }
}

val Int.dpAsPx: Int get() = if (this == 0) 0 else floor(AppConfig.density * this.toDouble()).toInt()

val Int.pxAsDp: Int get() = if (this == 0) 0 else floor(this.toDouble() / AppConfig.density).toInt()

val Int.dpAsPxFloat: Float get() = if (this == 0) 0f else floor(AppConfig.density * this.toDouble()).toFloat()

val Float.dpAsPx: Int get() = if (this == 0f) 0 else floor(AppConfig.density * this.toDouble()).toInt()

val Float.pxAsDp: Int get() = if (this == 0f) 0 else floor(this.toDouble() / AppConfig.density).toInt()

val Float.dpAsPxFloat: Float get() = if (this == 0f) 0f else floor(AppConfig.density * this.toDouble()).toFloat()

val Float.pxAsDpFloat: Float get() = if (this == 0f) 0f else floor(this.toDouble() / AppConfig.density).toFloat()

val Int.spAsPx: Int get() = if (this == 0) 0 else floor(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, toFloat(), AppConfig.displayMetrics)).toInt()

val Int.spAsPxFloat: Float get() = if (this == 0) 0f else floor(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, toFloat(), AppConfig.displayMetrics))