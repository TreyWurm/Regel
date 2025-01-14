package at.nukular.core.extensions

import android.content.Context
import android.content.res.Configuration
import kotlin.math.floor

object AppConfig {
    var density = 1f
    var fontDensity = 1f

    fun onConfigChanged(context: Context, newConfiguration: Configuration?) {
        val configuration = newConfiguration ?: context.resources.configuration

        density = context.resources.displayMetrics.density
        fontDensity = context.resources.displayMetrics.scaledDensity
    }
}

val Int.dpAsPx: Int get() = if (this == 0) 0 else floor(AppConfig.density * this.toDouble()).toInt()

val Int.pxAsDp: Int get() = if (this == 0) 0 else floor(this.toDouble() / AppConfig.density).toInt()

val Int.dpAsPxFloat: Float get() = if (this == 0) 0f else floor(AppConfig.density * this.toDouble()).toFloat()

val Float.dpAsPx: Int get() = if (this == 0f) 0 else floor(AppConfig.density * this.toDouble()).toInt()

val Float.pxAsDp: Int get() = if (this == 0f) 0 else floor(this.toDouble() / AppConfig.density).toInt()

val Float.dpAsPxFloat: Float get() = if (this == 0f) 0f else floor(AppConfig.density * this.toDouble()).toFloat()

val Float.pxAsDpFloat: Float get() = if (this == 0f) 0f else floor(this.toDouble() / AppConfig.density).toFloat()

val Int.spAsPx: Int get() = if (this == 0) 0 else floor(AppConfig.fontDensity * this.toDouble()).toInt()

val Float.spAsPx: Float get() = if (this == 0f) 0f else floor(AppConfig.fontDensity * this.toDouble()).toFloat()

val Float.spAsPxInt: Int get() = this.spAsPx.toInt()