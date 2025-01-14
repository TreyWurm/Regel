package at.nukular.regel.viewmodel

import at.nukular.regel.model.VHUEntry
import timber.log.Timber
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class MainUiState(
    val vhus: Set<VHUEntry> = emptySet(),
) : at.nukular.core.ui.viewmodel.UiState {

    fun estimatedNextPeriod(): LocalDate {
        val daysBetween = mutableListOf<Int>()
        vhus.forEachIndexed { index, vhu ->
            if (index + 1 > vhus.size - 1) {
                return@forEachIndexed
            }

            val toCompare = vhus.elementAt(index + 1).date
            daysBetween.add(ChronoUnit.DAYS.between(toCompare, vhu.date).toInt())
        }

        Timber.i("estimatedNextPeriod: $daysBetween")

        val averageDaysBetween = daysBetween.average().toLong()
        return vhus.firstOrNull()?.date?.plusDays(averageDaysBetween) ?: LocalDate.now()
    }
}