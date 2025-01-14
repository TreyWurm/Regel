package at.nukular.core.ui.views

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import at.nukular.core.extensions.AppConfig
import at.nukular.core.ui.theming.Theme
import at.nukular.regel.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs), UiComponent {

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

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.ConstraintLayout, 0, 0).apply {
            try {
                applyRipple = getBoolean(R.styleable.ConstraintLayout_ripple, true)

            } finally {
                recycle()
            }
        }

        applyStyle()
    }

    private fun applyStyle() {
        applyRippleIfRequired(this)

    }
}