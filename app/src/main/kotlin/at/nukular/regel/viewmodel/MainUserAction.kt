package at.nukular.regel.viewmodel

import java.time.LocalDate

sealed interface MainUserAction {
    data class NewDateSelected(val date: LocalDate) : MainUserAction
}