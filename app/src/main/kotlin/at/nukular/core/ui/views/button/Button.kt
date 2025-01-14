package at.nukular.core.ui.views.button

import android.animation.AnimatorInflater
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import at.nukular.core.extensions.*
import at.nukular.core.ui.theming.Theme
import at.nukular.core.ui.views.BaseUiComponent
import at.nukular.regel.R
import kotlin.math.ceil
import kotlin.math.max


private const val STYLE_FILLED = 0x10
private const val STYLE_FILLED_ELEVATED = 0x11
private const val STYLE_FILLED_ELEVATED_GREY = 0x12
private const val STYLE_FILLED_BOTTOM = 0x13
private const val STYLE_OUTLINED = 0x20
private const val STYLE_OUTLINED_ELEVATED = 0x21
private const val STYLE_SURFACE_ELEVATED = 0x30
private const val STYLE_TEXT = 0x40


/**
 * Custom button view supporting text AND/OR icon.
 * Currently supports the following styles
 * * FILLED
 * * FILLED_ELEVATED
 * * OUTLINED
 * * OUTLINED_ELEVATED
 *
 * &nbsp;
 *
 * **Note** When using an elevated style make sure to set clipChildren and clipToPadding to false in the parent layout, as shadow animation might get clipped otherwise
 *
 * &nbsp;
 *
 * For an animated connect button use [ConnectButton]
 */
class Button @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : BaseUiComponent(context, attrs) {



    private inner class OutlineProvider : ViewOutlineProvider() {
        val stylesWithoutRadius = listOf(
            Style.FILLED_BOTTOM,
            Style.TEXT,
        )

        var width: Int = 0
        var height: Int = 0
        var cornerRadius: Float = 0f
            set(value) {
                field = if (stylesWithoutRadius.contains(style)) {
                    0f
                } else {
                    value
                }
            }

        override fun getOutline(view: View, outline: Outline) {
            // Bottom + Text style have no rounded corners
            if (style == Style.FILLED_BOTTOM || style == Style.TEXT) {
                outline.setRoundRect(0, 0, width, height, 0f)
            } else {
                outline.setRoundRect(0, 0, width, height, cornerRadius)
            }
        }
    }

    enum class Style(val style: Int) {
        FILLED(STYLE_FILLED),
        FILLED_ELEVATED(STYLE_FILLED_ELEVATED),
        FILLED_GREY(STYLE_FILLED_ELEVATED_GREY),
        FILLED_BOTTOM(STYLE_FILLED_BOTTOM),
        OUTLINED(STYLE_OUTLINED),
        OUTLINED_ELEVATED(STYLE_OUTLINED_ELEVATED),
        SURFACE_ELEVATED(STYLE_SURFACE_ELEVATED),
        TEXT(STYLE_TEXT);

        companion object {
            private val map = values().associateBy(Style::style)
            fun fromInt(type: Int) = map[type]
        }
    }


    /*
     * ================================================================================
     * Button
     */
    var style: Style = Style.FILLED_ELEVATED
        set(value) {
            if (field == value) {
                return
            }
            field = value
            applyStyle()
            background = createBackground(style)
            invalidate()
        }
    private val buttonHeightPx: Int = context.resources.getDimensionPixelSize(R.dimen.btn_min_height)
    private val marginHorizontalPx: Float = context.resources.getDimension(R.dimen.btn_spacing_horizontal)
    private val marginIconText: Float = context.resources.getDimension(R.dimen.btn_spacing_text_icon)
    private val connectOutlineProvider = OutlineProvider()
    private var cornerRadii: FloatArray = FloatArray(8) { 0.dpAsPxFloat }

    /*
     * ================================================================================
     * Icon
     */
    var icon: Drawable? = null
        set(value) {
            field = value
            applyStyle()
            invalidate()
        }

    fun setIcon(@DrawableRes id: Int) {
        icon = AppCompatResources.getDrawable(context, id)
    }


    private val iconSizePx: Float = context.resources.getDimension(R.dimen.btn_icon_size)
    private var offsetIconHorizontalPx: Float = 0f
    private var offsetIconVerticalPx: Float = (buttonHeightPx - iconSizePx) / 2f

    /**
     * The actual size of the icon if present; 0 if no icon
     */
    private val actualIconSizePx: Float get() = icon?.let { iconSizePx } ?: 0f

    /**
     * The actual size of the margin between text and icon if present; 0 if no icon
     */
    private val actualMarginTextIconPx: Float get() = if (icon != null && slText != null) marginIconText else 0f


    /*
     * ================================================================================
     * Text
     */
    var text: String? = null
        set(value) {
            field = value
            setupText()
            applyStyle()
            requestLayout()
        }

    private var slText: StaticLayout? = null
    private var textWidth: Float = 0f
    private var offsetTextVerticalPx: Float = 0f
    private var offsetTextHorizontalPx: Float = 0f
    private val paintText = TextPaint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        textSize = context.resources.getDimension(R.dimen.btn_text_size)
        typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
    }

    /**
     * If defined, used as text color; Otherwise the default(primary) color is used
     */
    private val customTextColor: Int?

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.Button, 0, 0).apply {
            try {
                style = Style.fromInt(getInt(R.styleable.Button_customButtonStyle, 1)) ?: Style.FILLED_ELEVATED
                text = getString(R.styleable.Button_android_text)
                icon = getDrawable(R.styleable.Button_android_icon)
                isEnabled = getBoolean(R.styleable.Button_android_enabled, true)


                customTextColor = if (hasValue(R.styleable.Button_textColor)) {
                    getColor(R.styleable.Button_textColor, colorPrimary)
                } else {
                    null
                }


            } finally {
                recycle()
            }
        }

        // As we do not know width specs at the time of construction these values could change during the onMeasure() function
        setupText()

        outlineProvider = connectOutlineProvider
        clipToOutline = true

        background = createBackground(style)
        applyStyle()
    }

    init {
        // Need to reapply enabled state as we are setting textColors in 2nd init block
        isEnabled = isEnabled
    }

    private fun setupText() {
        text?.let {
            textWidth = paintText.measureText(it)
            slText = StaticLayout(it, paintText, textWidth.toInt())
        }
    }

    private fun actualTextColor(color: Int): Int = customTextColor ?: color

    private fun applyStyle() {
        when (style) {
            Style.FILLED, Style.FILLED_BOTTOM -> {
                icon?.apply {
                    setBounds(0, 0, iconSizePx.toInt(), iconSizePx.toInt())
                    setTint(actualTextColor(colorOnPrimary))
                }
                paintText.color = actualTextColor(colorOnPrimary)
            }

            Style.FILLED_ELEVATED -> {
                icon?.apply {
                    setBounds(0, 0, iconSizePx.toInt(), iconSizePx.toInt())
                    setTint(actualTextColor(colorOnPrimary))
                }
                paintText.color = actualTextColor(colorOnPrimary)
                stateListAnimator = AnimatorInflater.loadStateListAnimator(context, R.animator.btn_state_list)
            }

            Style.FILLED_GREY -> {
                icon?.apply {
                    setBounds(0, 0, iconSizePx.toInt(), iconSizePx.toInt())
                    setTint(actualTextColor(colorPrimary))
                }
                paintText.color = actualTextColor(colorPrimary)
                stateListAnimator = AnimatorInflater.loadStateListAnimator(context, R.animator.btn_state_list)
            }

            Style.OUTLINED -> {
                icon?.apply {
                    setBounds(0, 0, iconSizePx.toInt(), iconSizePx.toInt())
                    setTint(actualTextColor(colorPrimary))
                }
                paintText.color = actualTextColor(colorPrimary)
            }

            Style.OUTLINED_ELEVATED -> {
                icon?.apply {
                    setBounds(0, 0, iconSizePx.toInt(), iconSizePx.toInt())
                    setTint(actualTextColor(colorPrimary))
                }
                paintText.color = actualTextColor(colorPrimary)
                stateListAnimator = AnimatorInflater.loadStateListAnimator(context, R.animator.btn_state_list)
            }

            Style.SURFACE_ELEVATED -> {
                icon?.apply {
                    setBounds(0, 0, iconSizePx.toInt(), iconSizePx.toInt())
                    setTint(actualTextColor(colorPrimary))
                }
                paintText.color = actualTextColor(colorPrimary)
                stateListAnimator = AnimatorInflater.loadStateListAnimator(context, R.animator.btn_state_list)
            }

            Style.TEXT -> {
                icon?.apply {
                    setBounds(0, 0, iconSizePx.toInt(), iconSizePx.toInt())
                    setTint(actualTextColor(colorPrimary))
                }
                paintText.color = actualTextColor(colorPrimary)
            }
        }
    }

    private fun createBackground(style: Style): Drawable {
        return when (style) {
            Style.FILLED, Style.FILLED_ELEVATED -> createFilledBackground()
            Style.FILLED_BOTTOM -> createBottomBackground()
            Style.OUTLINED, Style.OUTLINED_ELEVATED -> createOutlinedBackground()
            Style.SURFACE_ELEVATED, Style.TEXT -> createSurfaceBackground()
            Style.FILLED_GREY -> createGreyBackground()
        }
    }

    private fun createFilledBackground(): Drawable {
        val stateListDrawable = StateListDrawable()
        stateListDrawable.addState(intArrayOf(-android.R.attr.state_enabled), GradientDrawable().apply {
            color = colorGreyDisabled.toColorStateList()
        })
        stateListDrawable.addState(intArrayOf(), createGradient(colorStart = colorButtonGradientStart, colorEnd = colorButtonGradientEnd))
        return RippleDrawable(colorPrimaryLight.toColorStateList(), stateListDrawable, null)
    }

    private fun createBottomBackground(): Drawable {
        val stateListDrawable = StateListDrawable()
        stateListDrawable.addState(intArrayOf(-android.R.attr.state_enabled), GradientDrawable().apply {
            color = colorGreyDisabled.toColorStateList()
        })
        stateListDrawable.addState(intArrayOf(), createGradient(colorStart = colorButtonGradientStart, colorEnd = colorButtonGradientEnd, radii = FloatArray(8) { 0f }))
        return RippleDrawable(colorPrimaryLight.toColorStateList(), stateListDrawable, null)
    }

    private fun createSurfaceBackground(): Drawable {
        val gradientDrawable = GradientDrawable().apply {
            color = colorSurface.toColorStateList()
        }

        return RippleDrawable(colorPrimaryLight.toColorStateList(), gradientDrawable, null)
    }

    private fun createOutlinedBackground(): Drawable {
        val gradientDrawable = GradientDrawable().apply {
            color = Color.TRANSPARENT.toColorStateList()
        }

        return RippleDrawable(colorPrimaryLight.toColorStateList(), gradientDrawable, null)
    }


    private fun createGreyBackground(): Drawable {
        val gradientDrawable = GradientDrawable().apply {
            color = 0XFFF1F1F1.toInt().toColorStateList()
        }

        return RippleDrawable(Theme.lighten(0XFFF1F1F1.toInt(), 0.1f).toColorStateList(), gradientDrawable, null)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val specMode = MeasureSpec.getMode(widthMeasureSpec)
        val specSize = MeasureSpec.getSize(widthMeasureSpec)

        // Make sure we we have at least the required space in case we got a floating point value for the width
        var desiredWidth: Int = ceil(marginHorizontalPx + actualIconSizePx + actualMarginTextIconPx + textWidth + marginHorizontalPx).toInt()

        // If the current button content would be greater as what can be displayed we need to try doing with a multiline text
        if (specSize < desiredWidth && (specMode == MeasureSpec.AT_MOST || specMode == MeasureSpec.EXACTLY)) {
            val availableSpaceForText = (specSize - (actualIconSizePx + actualMarginTextIconPx + marginHorizontalPx * 2)).coerceAtLeast(0f)
            text?.let {
                slText = StaticLayout(it, paintText, availableSpaceForText.toInt())
                textWidth = availableSpaceForText
            }
            desiredWidth = ceil(actualIconSizePx + availableSpaceForText.toInt() + marginHorizontalPx * 2).toInt()
        }

        val width = measureDimension(desiredWidth, widthMeasureSpec)

        val textHeight = slText?.height ?: run {
            val fm: Paint.FontMetrics = paintText.fontMetrics
            val height = fm.bottom - fm.top + fm.leading
            height.toInt()
        }
        val desiredHeight = max(buttonHeightPx, textHeight + context.resources.getDimensionPixelSize(R.dimen.btn_spacing_vertical) * 2)
        val height = measureDimension(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(width, height)
        updateCornerRadii()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val buttonMinWidthPx = marginHorizontalPx + actualIconSizePx + actualMarginTextIconPx + textWidth + marginHorizontalPx
        val spacing = (w - buttonMinWidthPx) / 2f

        icon?.let {
            offsetIconHorizontalPx = spacing + marginHorizontalPx
            offsetIconVerticalPx = (h - iconSizePx) / 2f
        }

        slText?.let {
            offsetTextHorizontalPx = spacing + marginHorizontalPx + actualIconSizePx + actualMarginTextIconPx
            offsetTextVerticalPx = (h - it.height) / 2f
        }

        connectOutlineProvider.let {
            it.width = w
            it.height = h
            // Corner validation happens in OutlineProvider
            it.cornerRadius = h / 2f
        }

        // Outlined backgrounds only
        // As we have the stroke set on our background drawable, we have to update the corners accordingly
        // Use the corner radius from the OutlineProvider as it's sanitized
        // Allocate here so we do not allocate in onMeasure
        cornerRadii = FloatArray(8) { connectOutlineProvider.cornerRadius }
        updateCornerRadii()
    }

    private fun updateCornerRadii() {
        ((background as? RippleDrawable)?.getDrawable(0) as? GradientDrawable)?.cornerRadii = cornerRadii
    }

    override fun onDraw(canvas: Canvas) {
        icon?.drawTranslated(canvas, offsetIconHorizontalPx, offsetIconVerticalPx)
        slText?.drawTranslated(canvas, offsetTextHorizontalPx, offsetTextVerticalPx)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (style == Style.OUTLINED || style == Style.OUTLINED_ELEVATED) {
            if (enabled) {
                paintText.color = actualTextColor(colorPrimary)
                icon?.setTint(actualTextColor(colorPrimary))
            } else {
                paintText.color = selectorPrimary.getColorForState(intArrayOf(-android.R.attr.state_enabled), selectorPrimary.defaultColor)
                icon?.setTint(selectorPrimary.getColorForState(intArrayOf(-android.R.attr.state_enabled), selectorPrimary.defaultColor))
            }
        }
    }
}