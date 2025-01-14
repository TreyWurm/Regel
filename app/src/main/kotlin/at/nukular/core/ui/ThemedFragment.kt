package at.nukular.core.ui

import android.os.Bundle
import android.view.View
import at.nukular.core.ui.theming.Theme
import at.nukular.core.ui.theming.ThemeInterface
import javax.inject.Inject

abstract class ThemedFragment : BaseFragmentImpl(), ThemeInterface {

    @Inject
    override lateinit var appTheme: Theme

    override fun applyTheme() {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyTheme()
    }
}