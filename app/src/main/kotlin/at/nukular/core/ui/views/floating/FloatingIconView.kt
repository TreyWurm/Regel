package at.nukular.core.ui.views.floating

import android.content.Context
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView

import at.nukular.regel.R
import at.nukular.core.extensions.AppConfig
import at.nukular.core.extensions.toColorStateList
import at.nukular.core.ui.drawables.ColoredCircleDrawable
import at.nukular.core.ui.theming.Theme
import at.nukular.core.ui.views.DEFAULT_RIPPLE
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Almost identical with [ImageView]
 *
 * **BUT** sets the default [setImageTintList] to [colorPrimary]
 */
@AndroidEntryPoint
class FloatingIconView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : AppCompatImageView(context, attrs), FloatingView {

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
            if (drawable == null) {
                setBackgroundResource(android.R.drawable.ic_dialog_info)
            }
        }
    }
    // endregion {

    init {
        context.obtainStyledAttributes(attrs, R.styleable.FloatingIconView, 0, 0).apply {
            try {
                applyRipple = getBoolean(R.styleable.FloatingIconView_ripple, DEFAULT_RIPPLE)
            } finally {
                recycle()
            }
        }

        applyStyle()
    }



    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        applyStyle()
    }


    fun applyStyle() {
        imageTintList = colorSurface.toColorStateList()
        if (applyRipple) {
            applyRippleIfRequired(this, createRippleWith(background = ColoredCircleDrawable(colorFloatingBackground), mask = ShapeDrawable(OvalShape())))
        } else {
            background = ColoredCircleDrawable(colorFloatingBackground)
        }
    }
}