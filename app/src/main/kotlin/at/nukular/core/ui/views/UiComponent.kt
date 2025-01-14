package at.nukular.core.ui.views

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.*
import android.view.View
import androidx.annotation.ColorInt
import at.nukular.core.extensions.dpAsPx
import at.nukular.core.extensions.dpAsPxFloat
import at.nukular.core.extensions.toColorStateList
import at.nukular.core.ui.theming.*


internal const val DEFAULT_RIPPLE = false


/**
 * Helper for wrapping everything that has to do with custom views theming
 *
 * Either
 * * Implement [UiComponent] for custom views extending existing android components e.g. [android.widget.TextView], ...
 * * Extend [BaseUiComponent] for totally custom views extending [android.view.View]
 *
 * This class wraps all access to colors to also work with Android-Studios Layout-Preview when creating layout xml
 *
 * In case of a Layout-Preview the default color values are used; Otherwise the selected theme colors
 *
 *
 * Also has functionality for creating common used things for views like selectors, gradients, ...
 *
 * For new custom views you need to
 * * Inject the view using the current injection framework
 * * **ONLY FOR HILT** Annotate the new view with "@AndroidEntryPoint"
 * * Property inject the theme, as constructor injection is not possible
 * * As we cannot create a base class, and same signatures are forbidden: override the [isViewInEditMode] property with the view's isInEditMode
 * * If you want to use the Screen extensions e.g. [dpToPx][at.nukular.core.extensions.dpAsPx] and you do not extend from [BaseUiComponent],
 * you need to set the [at.nukular.core.extensions.AppConfig] to get the right sizes in the Layout-Preview
 *
 *
 *
 * ## Example of a view implementing [UiComponent]
 * ```
 * @AndroidEntryPoint (if you are using Hilt)
 * class ExampleView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr), UiComponent {
 *
 *     @Inject
 *     override lateinit var theme: Theme
 *
 *     override val isViewInEditMode: Boolean = isInEditMode
 *     override val applyRipple: Boolean = false
 *
 *     init {
 *         if (isInEditMode) {
 *             AppConfig.onConfigChanged(context, null)
 *         }
 *         // If you are NOT using Hilt inject the view
 *         else {
 *             CitiesApplication.getInstance().appComponent.inject(this)
 *         }
 *
 *     }
 *
 *     ... YOUR IMPLEMENTATION
 * ```
 *
 *
 * ## Example of a view extending [BaseUiComponent]
 * ```
 * @AndroidEntryPoint (if you are using Hilt)
 * class ExampleView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr), UiComponent {
 *
 *    // If you are NOT using Hilt inject the view
 *    init {
 *        if (!isInEditMode) {
 *            CitiesApplication.getInstance().appComponent.inject(this)
 *        }
 *    }
 *
 *     ... YOUR IMPLEMENTATION
 * ```
 */
interface UiComponent {
    /**
     * Every custom view needs to inject a [Theme] to be able to apply themed colors as applicable
     */
    val theme: Theme

    val isViewInEditMode: Boolean

    val applyRipple: Boolean

    fun applyRippleIfRequired(view: View, rippleDrawable: RippleDrawable = ripplePrimary) {
        if (applyRipple) {
            view.background = rippleDrawable
        }
    }

    val colorPrimary: Int get() = if (isViewInEditMode) DEFAULT_PRIMARY else theme.colorPrimary
    val colorOnPrimary: Int get() = if (isViewInEditMode) DEFAULT_ON_PRIMARY else theme.colorOnPrimary
    val colorPrimaryContainer: Int get() = if (isViewInEditMode) DEFAULT_PRIMARY_CONTAINER else theme.colorPrimaryContainer
    val colorOnPrimaryContainer: Int get() = if (isViewInEditMode) DEFAULT_ON_PRIMARY_CONTAINER else theme.colorOnPrimaryContainer

    val colorSecondary: Int get() = if (isViewInEditMode) DEFAULT_SECONDARY else theme.colorSecondary
    val colorOnSecondary: Int get() = if (isViewInEditMode) DEFAULT_ON_SECONDARY else theme.colorOnSecondary
    val colorSecondaryContainer: Int get() = if (isViewInEditMode) DEFAULT_SECONDARY_CONTAINER else theme.colorSecondaryContainer
    val colorOnSecondaryContainer: Int get() = if (isViewInEditMode) DEFAULT_ON_SECONDARY_CONTAINER else theme.colorOnSecondaryContainer

    val colorTertiary: Int get() = if (isViewInEditMode) DEFAULT_TERTIARY else theme.colorTertiary
    val colorOnTertiary: Int get() = if (isViewInEditMode) DEFAULT_ON_TERTIARY else theme.colorOnTertiary
    val colorTertiaryContainer: Int get() = if (isViewInEditMode) DEFAULT_TERTIARY_CONTAINER else theme.colorTertiaryContainer
    val colorOnTertiaryContainer: Int get() = if (isViewInEditMode) DEFAULT_ON_TERTIARY_CONTAINER else theme.colorOnTertiaryContainer

    val colorError: Int get() = if (isViewInEditMode) DEFAULT_ERROR else theme.colorError
    val colorOnError: Int get() = if (isViewInEditMode) DEFAULT_ON_ERROR else theme.colorOnError
    val colorErrorContainer: Int get() = if (isViewInEditMode) DEFAULT_ERROR_CONTAINER else theme.colorErrorContainer
    val colorOnErrorContainer: Int get() = if (isViewInEditMode) DEFAULT_ON_ERROR_CONTAINER else theme.colorOnErrorContainer

    val colorSuccess: Int get() = if (isViewInEditMode) DEFAULT_SUCCESS else theme.colorSuccess
    val colorOnSuccess: Int get() = if (isViewInEditMode) DEFAULT_ON_SUCCESS else theme.colorOnSuccess
    val colorSuccessContainer: Int get() = if (isViewInEditMode) DEFAULT_SUCCESS_CONTAINER else theme.colorSuccessContainer
    val colorOnSuccessContainer: Int get() = if (isViewInEditMode) DEFAULT_ON_SUCCESS_CONTAINER else theme.colorOnSuccessContainer

    val colorBackground: Int get() = if (isViewInEditMode) DEFAULT_BACKGROUND else theme.colorBackground
    val colorOnBackground: Int get() = if (isViewInEditMode) DEFAULT_ON_BACKGROUND else theme.colorOnBackground

    val colorSurface: Int get() = if (isViewInEditMode) DEFAULT_SURFACE else theme.colorSurface
    val colorOnSurface: Int get() = if (isViewInEditMode) DEFAULT_ON_SURFACE else theme.colorOnSurface
    val colorSurfaceVariant: Int get() = if (isViewInEditMode) DEFAULT_SURFACE_VARIANT else theme.colorSurfaceVariant
    val colorOnSurfaceVariant: Int get() = if (isViewInEditMode) DEFAULT_ON_SURFACE_VARIANT else theme.colorOnSurfaceVariant

    val colorDivider: Int get() = if (isViewInEditMode) DEFAULT_DIVIDER else theme.colorDivider

    val colorOutline: Int get() = if (isViewInEditMode) DEFAULT_OUTLINE else theme.colorOutline
    val colorOutlineVariant: Int get() = if (isViewInEditMode) DEFAULT_OUTLINE_VARIANT else theme.colorOutlineVariant

    val colorGradientStart: Int get() = if (isViewInEditMode) DEFAULT_GRADIENT_START else theme.colorGradientStart
    val colorGradientEnd: Int get() = if (isViewInEditMode) DEFAULT_GRADIENT_END else theme.colorGradientEnd

    val colorButtonGradientStart: Int get() = if (isViewInEditMode) DEFAULT_BUTTON_GRADIENT_START else theme.colorButtonGradientStart
    val colorButtonGradientEnd: Int get() = if (isViewInEditMode) DEFAULT_BUTTON_GRADIENT_END else theme.colorButtonGradientEnd

    val colorTextIntense: Int get() = if (isViewInEditMode) ((colorOnSurface and 0x00ffffff) or (0xFF shl 24)) else theme.colorTextIntense
    val colorTextLight: Int get() = if (isViewInEditMode) ((colorOnSurface and 0x00ffffff) or (0x8D shl 24)) else theme.colorTextLight

    val colorGreyDisabled: Int
        get() = if (isViewInEditMode) {
            Color.GRAY
        } else {
            theme.greyDisabled
        }

    val colorPrimaryLight: Int
        get() = if (isViewInEditMode) {
            Theme.lighten(colorPrimary, .2f)
        } else {
            theme.colorPrimaryLight
        }

    val colorPrimaryDark: Int
        get() = if (isViewInEditMode) {
            Theme.darken(colorPrimary, .2f)
        } else {
            theme.colorPrimaryDark
        }

    val selectorPrimary: ColorStateList
        get() =
            if (isViewInEditMode) {
                val colors = intArrayOf(colorGreyDisabled, colorPrimary)
                val states = arrayOf(
                    intArrayOf(-android.R.attr.state_enabled),
                    intArrayOf(),
                )
                ColorStateList(states, colors)
            } else {
                val colors = intArrayOf(
                    theme.colorPrimaryDark,
                    theme.greyDisabled,
                    theme.colorPrimary
                )
                val states = arrayOf(
                    intArrayOf(android.R.attr.state_selected),
                    intArrayOf(-android.R.attr.state_enabled),
                    intArrayOf(),
                )
                ColorStateList(states, colors)
            }


    // ================================================================================
    // region Gradient

    fun createGradient(
        orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.BL_TR,
        colorStart: Int = colorGradientStart,
        colorEnd: Int = colorGradientEnd,
        radii: FloatArray = FloatArray(8) { 16.dpAsPxFloat },
    ): GradientDrawable {
        return if (isViewInEditMode) {
            GradientDrawable(orientation, intArrayOf(colorStart, colorEnd)).apply { cornerRadii = radii }
        } else {
            theme.createGradient(orientation, colorStart, colorEnd, radii)
        }
    }
    // endregion

    val ripplePrimary: RippleDrawable get() = if (isViewInEditMode) RippleDrawable(colorPrimary.toColorStateList(), ColorDrawable(colorSurface), null) else theme.createRipple()
    val colorOnSurfaceTransparent: Int get() = if (isViewInEditMode) Color.GRAY else theme.colorOnSurfaceTransparent

    fun createRipple(
        @ColorInt pressed: Int = colorPrimaryLight,
        @ColorInt background: Int = colorSurface,
        mask: Drawable? = null,
    ): RippleDrawable {
        return if (isViewInEditMode)
            RippleDrawable(pressed.toColorStateList(), ColorDrawable(background), mask)
        else theme.createRipple(pressed, background, mask)
    }

    fun createRippleWith(
        @ColorInt pressed: Int = colorPrimaryLight,
        background: Drawable = ColorDrawable(colorSurface),
        mask: Drawable? = null,
    ): RippleDrawable {
        return if (isViewInEditMode)
            RippleDrawable(pressed.toColorStateList(), background, mask)
        else theme.createRippleWith(pressed, background, mask)
    }


    fun createStateListDrawable(
        radii: FloatArray = FloatArray(8) { 0f },
        @ColorInt pressed: Int = colorPrimaryDark,
        @ColorInt focused: Int = colorPrimary,
        @ColorInt disabled: Int = colorGreyDisabled,
        @ColorInt normal: Int = colorPrimary,
    ): StateListDrawable {
        val res = StateListDrawable()
        res.addState(intArrayOf(android.R.attr.state_pressed), GradientDrawable().apply {
            cornerRadii = radii
            color = ColorStateList.valueOf(pressed)
        })
        res.addState(intArrayOf(android.R.attr.state_focused), GradientDrawable().apply {
            cornerRadii = radii
            color = ColorStateList.valueOf(focused)
        })
        res.addState(intArrayOf(-android.R.attr.state_enabled), GradientDrawable().apply {
            cornerRadii = radii
            color = ColorStateList.valueOf(disabled)
        })
        res.addState(intArrayOf(), GradientDrawable().apply {
            cornerRadii = radii
            color = ColorStateList.valueOf(normal)
        })
        return res
    }

    fun createCursorDrawable(
        @ColorInt color: Int = colorPrimary,
    ): Drawable {
        return if (isViewInEditMode) {
            val shape = GradientDrawable()
            shape.shape = GradientDrawable.RECTANGLE
            shape.setColor(color)
            shape.setSize(2.dpAsPx, 0)
            shape
        } else {
            theme.createCursorDrawable(color)
        }
    }

    fun createColorStateListSelector(
        @ColorInt pressed: Int = colorPrimaryDark,
        @ColorInt focused: Int = colorPrimary,
        @ColorInt disabled: Int = colorGreyDisabled,
        @ColorInt normal: Int = colorPrimary,
    ): ColorStateList {
        return if (isViewInEditMode) {
            val colors = intArrayOf(pressed, focused, disabled, normal)
            val states = arrayOf(
                intArrayOf(android.R.attr.state_pressed),
                intArrayOf(android.R.attr.state_focused),
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf(),
            )
            ColorStateList(states, colors)
        } else {
            theme.createColorStateListSelector(pressed, focused, disabled, normal)
        }

    }


    // ================================================================================
    // region ColorStyle

    fun getColorStyleColor(colorStyle: ColorStyle): Int = when (colorStyle) {
        ColorStyle.DISABLED -> colorGreyDisabled
        ColorStyle.ERROR -> colorError
        ColorStyle.ON_PRIMARY -> colorOnPrimary
        ColorStyle.ON_SURFACE -> colorOnSurface
        ColorStyle.PRIMARY -> colorPrimary
        ColorStyle.SUCCESS -> colorSuccess
        ColorStyle.SURFACE -> colorSurface
        ColorStyle.TEXT_INTENSE -> colorTextIntense
        ColorStyle.TEXT_LIGHT -> colorTextLight
    }
    // endregion
}