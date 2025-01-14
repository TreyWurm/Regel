package at.nukular.core.ui.theming

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.util.TypedValue
import android.widget.EditText
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.graphics.ColorUtils
import at.nukular.core.extensions.dpAsPxFloat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Theme @Inject constructor(
    @ApplicationContext val context: Context
) {
    var colorPrimary: Int = DEFAULT_PRIMARY
        private set
    var colorOnPrimary: Int = DEFAULT_ON_PRIMARY
        private set
    var colorPrimaryContainer: Int = DEFAULT_PRIMARY_CONTAINER
        private set
    var colorOnPrimaryContainer: Int = DEFAULT_ON_PRIMARY_CONTAINER
        private set

    var colorSecondary: Int = DEFAULT_SECONDARY
        private set
    var colorOnSecondary: Int = DEFAULT_ON_SECONDARY
        private set
    var colorSecondaryContainer: Int = DEFAULT_SECONDARY_CONTAINER
        private set
    var colorOnSecondaryContainer: Int = DEFAULT_ON_SECONDARY_CONTAINER
        private set

    var colorTertiary: Int = DEFAULT_TERTIARY
        private set
    var colorOnTertiary: Int = DEFAULT_ON_TERTIARY
        private set
    var colorTertiaryContainer: Int = DEFAULT_TERTIARY_CONTAINER
        private set
    var colorOnTertiaryContainer: Int = DEFAULT_ON_TERTIARY_CONTAINER
        private set

    var colorError: Int = DEFAULT_ERROR
        private set
    var colorOnError: Int = DEFAULT_ON_ERROR
        private set
    var colorErrorContainer: Int = DEFAULT_ERROR_CONTAINER
        private set
    var colorOnErrorContainer: Int = DEFAULT_ON_ERROR_CONTAINER
        private set

    var colorSuccess: Int = DEFAULT_SUCCESS
        private set
    var colorOnSuccess: Int = DEFAULT_ON_SUCCESS
        private set
    var colorSuccessContainer: Int = DEFAULT_SUCCESS_CONTAINER
        private set
    var colorOnSuccessContainer: Int = DEFAULT_ON_SUCCESS_CONTAINER
        private set

    var colorBackground: Int = DEFAULT_BACKGROUND
        private set
    var colorOnBackground: Int = DEFAULT_ON_BACKGROUND
        private set

    var colorSurface: Int = DEFAULT_SURFACE
        private set
    var colorOnSurface: Int = DEFAULT_ON_SURFACE
        private set
    var colorSurfaceVariant: Int = DEFAULT_SURFACE_VARIANT
        private set
    var colorOnSurfaceVariant: Int = DEFAULT_ON_SURFACE_VARIANT
        private set

    var colorGradientStart: Int = DEFAULT_GRADIENT_START
        private set
    var colorGradientEnd: Int = DEFAULT_GRADIENT_END
        private set

    var colorButtonGradientStart: Int = DEFAULT_BUTTON_GRADIENT_START
        private set
    var colorButtonGradientEnd: Int = DEFAULT_BUTTON_GRADIENT_END
        private set

    var colorDivider: Int = DEFAULT_DIVIDER
        private set

    var colorOutline: Int = DEFAULT_OUTLINE
        private set
    var colorOutlineVariant: Int = DEFAULT_OUTLINE_VARIANT
        private set

    fun isDarkTheme(): Boolean {
        return context.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }

    fun newTheme(theme: Theme) {
        colorPrimary = theme.colorPrimary
        colorOnPrimary = theme.colorOnPrimary
        colorPrimaryContainer = theme.colorPrimaryContainer
        colorOnPrimaryContainer = theme.colorOnPrimaryContainer
        colorSecondary = theme.colorSecondary
        colorOnSecondary = theme.colorOnSecondary
        colorSecondaryContainer = theme.colorSecondaryContainer
        colorOnSecondaryContainer = theme.colorOnSecondaryContainer
        colorTertiary = theme.colorTertiary
        colorOnTertiary = theme.colorOnTertiary
        colorTertiaryContainer = theme.colorTertiaryContainer
        colorOnTertiaryContainer = theme.colorOnTertiaryContainer
        colorError = theme.colorError
        colorOnError = theme.colorOnError
        colorErrorContainer = theme.colorErrorContainer
        colorOnErrorContainer = theme.colorOnErrorContainer
        colorBackground = theme.colorBackground
        colorOnBackground = theme.colorOnBackground
        colorSurface = theme.colorSurface
        colorOnSurface = theme.colorOnSurface
        colorSurfaceVariant = theme.colorSurfaceVariant
        colorOnSurfaceVariant = theme.colorOnSurfaceVariant
        colorGradientStart = theme.colorGradientStart
        colorGradientEnd = theme.colorGradientEnd
        colorDivider = theme.colorDivider
        colorOutline = theme.colorOutline
        colorOutlineVariant = theme.colorOutlineVariant
    }


    val colorPrimaryDark get() = darken(colorPrimary, .2f)
    val colorPrimaryLight get() = lighten(colorPrimary, .2f)
    val greyDisabled = Color.GRAY
    val colorOnSurfaceLight get() = lighten(colorOnSurface, .2f)
    val colorOnSurfaceDark get() = darken(colorOnSurface, .2f)
    val colorOnSurfaceTransparent get() = alpha(colorOnSurface, 0x1B)


    // ================================================================================
    // region Text

    val colorTextIntense get() = alpha(colorOnSurface, 0xFF) // 100%
    val colorTextLight get() = alpha(colorOnSurface, 0x8D) // 55%
    // endregion


    // ================================================================================
    // region Gradient

    val primaryGradient: GradientDrawable get() = createGradient() //.mutate() as GradientDrawable

    fun createGradient(
        orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.BL_TR,
        colorStart: Int = colorGradientStart,
        colorEnd: Int = colorGradientEnd,
        radii: FloatArray = FloatArray(8) { 16.dpAsPxFloat },
    ): GradientDrawable {
        return GradientDrawable(orientation, intArrayOf(colorStart, colorEnd)).apply { cornerRadii = radii }
    }
    // endregion


    // ================================================================================
    // region Ripple

    fun createRipple(
        @ColorInt pressed: Int = colorPrimaryLight,
        @ColorInt background: Int = colorSurface,
        mask: Drawable? = ColorDrawable(Color.WHITE),
    ): RippleDrawable {
        return RippleDrawable(ColorStateList.valueOf(pressed), ColorDrawable(background), mask)
    }

    fun createRippleWith(
        @ColorInt pressed: Int = colorPrimaryLight,
        background: Drawable = ColorDrawable(colorSurface),
        mask: Drawable? = null,
    ): RippleDrawable {
        return RippleDrawable(ColorStateList.valueOf(pressed), background, mask)
    }
    // endregion


    // ================================================================================
    // region EditText

    fun createCursorDrawable(@ColorInt color: Int = colorPrimary): Drawable {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.setColor(color)
        shape.setSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, context.resources.displayMetrics).toInt(), 0)
        return shape
    }

    fun colorEditText(editText: EditText, @ColorInt color: Int = colorPrimary) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            editText.backgroundTintList = ColorStateList.valueOf(color)
            editText.textCursorDrawable = createCursorDrawable(color)
            editText.highlightColor = ColorUtils.setAlphaComponent(color, 100)
            editText.textSelectHandle?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, BlendModeCompat.SRC_IN)
            editText.textSelectHandleLeft?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, BlendModeCompat.SRC_IN)
            editText.textSelectHandleRight?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, BlendModeCompat.SRC_IN)
        }
    }
    // endregion


    // ================================================================================
    // region Selector

    fun createColorStateListSelector(
        @ColorInt pressed: Int = colorPrimaryDark,
        @ColorInt focused: Int = colorPrimary,
        @ColorInt disabled: Int = greyDisabled,
        @ColorInt normal: Int = colorPrimary,
    ): ColorStateList {
        val colors = intArrayOf(pressed, focused, disabled, normal)
        val states = arrayOf(
            intArrayOf(android.R.attr.state_pressed),
            intArrayOf(android.R.attr.state_focused),
            intArrayOf(-android.R.attr.state_enabled),
            intArrayOf(),
        )
        return ColorStateList(states, colors)
    }
    // endregion


    companion object {

        @JvmStatic
        fun alpha(@ColorInt base: Int, @IntRange(from = 0x0, to = 0xFF) alpha: Int): Int {
            return ColorUtils.setAlphaComponent(base, alpha)
        }

        /**
         * Darkens a given color
         *
         * @param base base color
         * @param amount amount between 0 and 100
         * @return darken color
         */
        @JvmStatic
        fun darken(@ColorInt base: Int, @FloatRange(from = 0.0, to = 1.0) amount: Float): Int {
            val hsl = FloatArray(3)
            ColorUtils.colorToHSL(base, hsl)
            hsl[2] = (hsl[2] - amount).coerceAtLeast(0f)
            return ColorUtils.HSLToColor(hsl)
        }

        /**
         * Lightens a given color
         *
         * @param base base color
         * @param amount amount between 0 and 100
         * @return lightened
         */
        @JvmStatic
        fun lighten(@ColorInt base: Int, @FloatRange(from = 0.0, to = 1.0) amount: Float): Int {
            val hsl = FloatArray(3)
            ColorUtils.colorToHSL(base, hsl)
            hsl[2] = (hsl[2] + amount).coerceAtMost(1f)
            return ColorUtils.HSLToColor(hsl)
        }
    }
}
