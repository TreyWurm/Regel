package at.nukular.regel

import android.text.format.DateFormat
import at.nukular.core.extensions.CURRENT_LOCALE
import java.time.format.DateTimeFormatter

object Constants {
    val dayOfMonthMonthAbbrYear = DateTimeFormatter.ofPattern(DateFormat.getBestDateTimePattern(CURRENT_LOCALE, "yyyy MMM dd"))
}