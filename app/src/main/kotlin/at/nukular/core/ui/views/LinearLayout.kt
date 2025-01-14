package at.nukular.core.ui.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.LinearLayoutCompat
import at.nukular.core.extensions.AppConfig
import at.nukular.core.ui.theming.Theme
import at.nukular.regel.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : LinearLayoutCompat(context, attrs), UiComponent {

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
        context.theme.obtainStyledAttributes(attrs, R.styleable.LinearLayout, 0, 0).apply {
            try {
                applyRipple = getBoolean(R.styleable.LinearLayout_ripple, true)

            } finally {
                recycle()
            }
        }

        applyRippleIfRequired(this)
    }
}