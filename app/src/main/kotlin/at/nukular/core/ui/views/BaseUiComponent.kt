package at.nukular.core.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import at.nukular.core.extensions.AppConfig
import at.nukular.core.ui.theming.Theme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseUiComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), UiComponent {

    @Inject
    override lateinit var theme: Theme

    // Custom View initialization, so we see a preview inside layout editor
    override val isViewInEditMode: Boolean = isInEditMode

    override var applyRipple: Boolean = false

    init {
        if (isInEditMode) {
            AppConfig.onConfigChanged(context, null)
        }
    }
}