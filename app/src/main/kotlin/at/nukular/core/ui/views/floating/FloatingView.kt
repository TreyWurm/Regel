package at.nukular.core.ui.views.floating

import at.nukular.core.ui.theming.Theme
import at.nukular.core.ui.views.UiComponent

interface FloatingView : UiComponent {
    val colorFloatingBackground: Int get() = Theme.alpha(colorOnSurface, 0xD0)
    val colorFloatingContent: Int get() = colorSurface
}