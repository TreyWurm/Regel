package at.nukular.core.ui

import android.os.Bundle
import androidx.core.view.WindowCompat
import at.nukular.core.ui.theming.Theme
import at.nukular.core.ui.theming.Theme.Companion.darken
import at.nukular.core.ui.theming.ThemeInterface
import org.koin.android.ext.android.inject

abstract class ThemedActivity : BaseActivityImpl(), ThemeInterface {

    override val appTheme: Theme by inject()

    override fun applyTheme() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        window.statusBarColor = darken(appTheme.colorSurface, 0.1f)
        applyTheme()
    }
}