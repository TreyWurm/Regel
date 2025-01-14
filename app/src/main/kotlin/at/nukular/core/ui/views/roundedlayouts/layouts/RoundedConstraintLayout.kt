package at.nukular.core.ui.views.roundedlayouts.layouts

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import at.nukular.core.extensions.AppConfig
import at.nukular.core.ui.theming.Theme
import at.nukular.core.ui.views.UiComponent
import at.nukular.core.ui.views.roundedlayouts.LayoutVersionImplementation
import at.nukular.core.ui.views.roundedlayouts.RoundedLayout
import at.nukular.core.ui.views.roundedlayouts.RoundedLayout.Companion.DEFAULT_BACKGROUND_COLOR
import at.nukular.core.ui.views.roundedlayouts.RoundedLayout.Companion.DEFAULT_RADIUS
import at.nukular.core.ui.views.roundedlayouts.RoundedLayout.Companion.DEFAULT_STROKE_COLOR
import at.nukular.core.ui.views.roundedlayouts.RoundedLayout.Companion.DEFAULT_STROKE_WIDTH
import at.nukular.core.ui.views.roundedlayouts.RoundedLayoutOutlineProvider
import at.nukular.regel.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * Rounded ConstraintLayout
 *
 *
 * **ATTENTION**
 * Prior to API 30 we can only have same corner radii for clipping.
 * As setting [Outline.setConvexPath] would lead to [Outline.canClip] return false
 */
@AndroidEntryPoint
class RoundedConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs), RoundedLayout, UiComponent {

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


    override val outlineProvider: RoundedLayoutOutlineProvider = RoundedLayoutOutlineProvider()

    /**
     * Double the width of the border stroke, as drawing a path is using thickness/half as actual path middle
     */
    private var strokeWidthDouble = DEFAULT_STROKE_WIDTH * 2f
    private val layoutVersionImplementation: LayoutVersionImplementation
    private val pathStroke = Path()
    private val paintStroke = Paint()
    private val paintBackground = Paint()

    /**
     * With of border stroke
     */
    override var strokeWidth: Int = DEFAULT_STROKE_WIDTH
        set(value) {
            strokeWidthDouble = value * 2f
            paintStroke.strokeWidth = value.toFloat()
            field = value
            setPadding(strokeWidth, strokeWidth, strokeWidth, strokeWidth)
            layoutVersionImplementation.initBackground()
            requestLayout()
            invalidate()
        }

    override var strokeColor = DEFAULT_STROKE_COLOR
        set(value) {
            field = value
            paintStroke.color = value
            invalidate()
        }

    override var cornerRadius: Int = DEFAULT_RADIUS
        set(value) {
            field = value
            cornerRadii = IntArray(8) { value }
        }

    override var cornerRadii: IntArray = IntArray(8) { DEFAULT_RADIUS }
        set(value) {
            field = value
            outlineProvider.cornerRadii = cornerRadii
            layoutVersionImplementation.initBackground()
            requestLayout()
        }

    override var rlBackgroundColor = DEFAULT_BACKGROUND_COLOR
        set(value) {
            field = value
            paintBackground.color = value
            layoutVersionImplementation.initBackground()
            invalidate()
        }

    override var rippleColor = colorPrimaryLight
        set(value) {
            field = value
            layoutVersionImplementation.initBackground()
            invalidate()
        }


    init {
        layoutVersionImplementation = PostLollipop()

        if (attrs != null) {
            val array = context.obtainStyledAttributes(attrs, R.styleable.RoundedConstraintLayout)
            if (array.hasValue(R.styleable.RoundedConstraintLayout_rl_radius)) {
                cornerRadius = array.getDimensionPixelSize(
                    R.styleable.RoundedConstraintLayout_rl_radius,
                    DEFAULT_RADIUS
                )
            }

            if (array.hasValue(R.styleable.RoundedConstraintLayout_rl_radius_top_left)
                || array.hasValue(R.styleable.RoundedConstraintLayout_rl_radius_top_right)
                || array.hasValue(R.styleable.RoundedConstraintLayout_rl_radius_bottom_left)
                || array.hasValue(R.styleable.RoundedConstraintLayout_rl_radius_bottom_right)
            ) {

                val topLeft = array.getDimensionPixelSize(
                    R.styleable.RoundedConstraintLayout_rl_radius_top_left,
                    DEFAULT_RADIUS
                )
                val topRight = array.getDimensionPixelSize(
                    R.styleable.RoundedConstraintLayout_rl_radius_top_right,
                    DEFAULT_RADIUS
                )

                val bottomLeft = array.getDimensionPixelSize(
                    R.styleable.RoundedConstraintLayout_rl_radius_bottom_left,
                    DEFAULT_RADIUS
                )

                val bottomRight = array.getDimensionPixelSize(
                    R.styleable.RoundedConstraintLayout_rl_radius_bottom_right,
                    DEFAULT_RADIUS
                )

                cornerRadii = intArrayOf(
                    topLeft,
                    topLeft,
                    topRight,
                    topRight,
                    bottomRight,
                    bottomRight,
                    bottomLeft,
                    bottomLeft
                )
            }

            if (array.hasValue(R.styleable.RoundedConstraintLayout_rl_stroke_width)) {
                strokeWidth = array.getDimensionPixelSize(
                    R.styleable.RoundedConstraintLayout_rl_stroke_width,
                    DEFAULT_STROKE_WIDTH
                )
            }
            strokeWidthDouble = strokeWidth * 2f
            if (array.hasValue(R.styleable.RoundedConstraintLayout_rl_stroke_color)) {
                strokeColor = array.getColor(
                    R.styleable.RoundedConstraintLayout_rl_stroke_color,
                    DEFAULT_STROKE_COLOR
                )
            }

            rlBackgroundColor = if (isInEditMode) {
                array.getColor(
                    R.styleable.RoundedConstraintLayout_rl_background_color,
                    DEFAULT_BACKGROUND_COLOR
                )
            } else {
                array.getColor(
                    R.styleable.RoundedConstraintLayout_rl_background_color,
                    colorSurface
                )
            }
            array.recycle()
        }


        paintBackground.apply {
            flags = Paint.ANTI_ALIAS_FLAG
            style = Paint.Style.FILL
            color = rlBackgroundColor
            strokeWidth = strokeWidthDouble
        }

        paintStroke.apply {
            flags = Paint.ANTI_ALIAS_FLAG
            style = Paint.Style.STROKE
            color = strokeColor
            strokeWidth = strokeWidthDouble
        }


        layoutVersionImplementation.initBackground()
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        layoutVersionImplementation.onLayout(changed, left, top, right, bottom)
    }


    override fun dispatchDraw(canvas: Canvas) {
        layoutVersionImplementation.dispatchDraw(canvas)
    }

    fun callSuperDispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateOutlineProvider(w, h)
    }


    /**
     * Layout implementation for Build.Version >= Lollipop
     */
    inner class PostLollipop : LayoutVersionImplementation {
        override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
            pathStroke.reset()
            pathStroke.addRoundRect(
                0f,
                0f,
                width.toFloat(),
                height.toFloat(),
                cornerRadii.toFloatArray(),
                Path.Direction.CW
            )
        }

        override fun dispatchDraw(canvas: Canvas) {
            canvas.save()
            this@RoundedConstraintLayout.callSuperDispatchDraw(canvas)
            canvas.restore()
            canvas.drawPath(pathStroke, paintStroke)
        }

        override fun initBackground() {
            setPadding(strokeWidth, strokeWidth, strokeWidth, strokeWidth)
            setOutlineProvider(outlineProvider)
            val shapeDrawable = GradientDrawable()
            shapeDrawable.cornerRadii = cornerRadii.toFloatArray()
            shapeDrawable.setColor(rlBackgroundColor)
            val rippleDrawable =
                RippleDrawable(ColorStateList.valueOf(rippleColor), shapeDrawable, null)
            background = rippleDrawable
            clipToOutline = true
        }
    }
}