package at.nukular.core.extensions

import androidx.appcompat.app.AppCompatDelegate
import java.util.*

/**
 * The current selected locale **LIMITED** to a [Locale] with german language or [Locale.US]
 *
 * The locale is defined by
 * 1. Get the current app locale
 * 2. Get the default locale otherwise
 *
 * The limitation is defined by
 * 1. Check if it's language is german -> returns the locale
 * 2. Returns [Locale.US] in otherwise
 */
val CURRENT_LOCALE: Locale get() {
    val applicationLocales = AppCompatDelegate.getApplicationLocales()
    val currentLocale = applicationLocales.get(0) ?: Locale.getDefault()
    return when (currentLocale.language) {
        Locale.GERMAN.language -> currentLocale
        else -> Locale.US
    }
}
