package at.nukular.core.ui.views

import android.content.Context
import android.util.AttributeSet
import at.nukular.core.extensions.AppConfig
import at.nukular.core.ui.theming.Theme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class EditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : androidx.appcompat.widget.AppCompatEditText(context, attrs), UiComponent {

    // ================================================================================
    // region UiComponent

    @Inject
    override lateinit var theme: Theme

    // Custom View initialization, so we see a preview inside layout editor
    override val isViewInEditMode: Boolean = isInEditMode

    override var applyRipple: Boolean = true

    init {
        if (isInEditMode) {
            AppConfig.onConfigChanged(context, null)
        }
    }
    // endregion
}