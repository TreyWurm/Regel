package at.nukular.core.di.koin

import at.nukular.core.ui.theming.Theme
import org.koin.dsl.module

val appModule = module {
    single { Theme(get()) }
}