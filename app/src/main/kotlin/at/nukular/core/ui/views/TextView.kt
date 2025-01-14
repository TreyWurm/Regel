package at.nukular.core.ui.views

import android.content.Context
import android.text.Spanned
import android.text.method.Touch
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.toSpannable
import at.nukular.core.extensions.AppConfig
import at.nukular.core.extensions.gone
import at.nukular.core.extensions.visible
import at.nukular.core.model.ParcelableStringHolder
import at.nukular.core.ui.theming.Theme
import at.nukular.regel.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


private const val STYLE_INTENSE = 0x10
private const val STYLE_LIGHT = 0x20
private const val STYLE_PRIMARY = 0x30
private const val STYLE_ON_PRIMARY = 0x40
private const val STYLE_ERROR = 0x50


@AndroidEntryPoint
class TextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : AppCompatTextView(context, attrs), UiComponent {

    init {
        if (isInEditMode) {
            AppConfig.onConfigChanged(context, null)
        }
    }


    @Inject
    override lateinit var theme: Theme


    // Custom View initialization, so we see a preview inside layout editor
    override val isViewInEditMode: Boolean = isInEditMode

    override var applyRipple: Boolean = false


    init {
        if (!isInEditMode) {
            setOnTouchListener(Test())
            setLinkTextColor(selectorPrimary)
        }
    }


    inner class Test : OnTouchListener {
        override fun onTouch(view: View, event: MotionEvent): Boolean {
            val widget = view as? TextView ?: return false
            val action = event.action
            var x = event.x.toInt()
            var y = event.y.toInt()

            x -= widget.totalPaddingLeft
            y -= widget.totalPaddingTop
            x += widget.scrollX
            y += widget.scrollY

            val layout = widget.layout
            val line = layout.getLineForVertical(y)
            val off = layout.getOffsetForHorizontal(line, x.toFloat())
            val spannable = text as? Spanned ?: return false
            val links = spannable.getSpans(off, off, ClickableSpan::class.java) ?: return false

            // If we have got no links we do not want to handle the touch event
            val firstLink = links.firstOrNull() ?: return false

            // Check for empty links, as LinkMovementMethod would remove selection if no links present
            return if (action == MotionEvent.ACTION_UP) {
                return when (firstLink) {
//                    is URLSpan -> {
//                        intentHandler.handleExternalUrl(widget.context, firstLink.url)
//                        true
//                    }
//
//                    is InAppClickableSpan -> {
//                        firstLink.onClick(view)
//                        true
//                    }

                    else -> this@TextView.onTouchEvent(event)
                }


            } else Touch.onTouchEvent(widget, spannable.toSpannable(), event)
        }
    }

    enum class Style(val style: Int) {
        INTENSE(STYLE_INTENSE),
        LIGHT(STYLE_LIGHT),
        PRIMARY(STYLE_PRIMARY),
        ON_PRIMARY(STYLE_ON_PRIMARY),
        ERROR(STYLE_ERROR),
        ;

        companion object {
            private val map = entries.associateBy(Style::style)
            fun fromInt(type: Int) = map[type]
        }
    }


    // ================================================================================
    // region TextView properties

    var style: Style = Style.LIGHT
        set(value) {
            field = value
            applyStyle()
            invalidate()
        }
    // endregion

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.TextView, 0, 0).apply {
            try {
                style = Style.fromInt(getInt(R.styleable.TextView_textViewStyle, STYLE_LIGHT))
                    ?: Style.LIGHT
                applyRipple = getBoolean(R.styleable.TextView_ripple, DEFAULT_RIPPLE)
                isEnabled = getBoolean(R.styleable.Button_android_enabled, true)

            } finally {
                recycle()
            }
        }

        applyStyle()
    }

    private fun applyStyle() {
        applyRippleIfRequired(this)

        when (style) {
            Style.INTENSE -> setTextColor(colorTextIntense)
            Style.LIGHT -> setTextColor(colorTextLight)
            Style.PRIMARY -> setTextColor(colorPrimary)
            Style.ON_PRIMARY -> setTextColor(colorOnPrimary)
            Style.ERROR -> setTextColor(colorError)
        }
    }

    fun setText(stringHolder: ParcelableStringHolder) {
        stringHolder.applyTo(this)
    }

    fun setTextOrHide(stringHolder: ParcelableStringHolder?) {
        if (stringHolder == null || (stringHolder.textString.isNullOrBlank() && stringHolder.textRes == 0)) {
            gone()
        } else {
            setText(stringHolder)
            visible()
        }
    }

    fun setTextOrHide(string: CharSequence?) {
        if (string.isNullOrBlank()) {
            gone()
        } else {
            text = string
            visible()
        }
    }
}