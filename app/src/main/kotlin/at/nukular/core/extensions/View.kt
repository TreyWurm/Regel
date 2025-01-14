package at.nukular.core.extensions

import android.annotation.SuppressLint
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.view.View
import timber.log.Timber

/**
 * Sets the view's alpha value to 0 (fully transparent)
 *
 * **Note** You can chain calls to [visible], [gone]
 */
fun View.transparent(): View {
    this.alpha = 0f
    return this
}

/**
 * Sets the view's alpha value to 1(fully opaque)
 *
 * **Note** You can chain calls to [visible], [gone]
 */
fun View.opaque(): View {
    this.alpha = 1f
    return this
}

/**
 * Set the view's visibility to [View.VISIBLE]
 *
 * **Note** this does not change the alpha value. Chain/prepend with [opaque], [transparent]  e.g.
 * ```
 * View.opaque().visible()
 * ```
 * @see [opaque]
 * @see [transparent]
 */
fun View.visible(): View {
    this.visibility = View.VISIBLE
    return this
}

/**
 * Set the view's visibility to [View.VISIBLE] as well as the view's alpha to fully opaque
 *
 * ```
 * View.opaque().visible()
 * ```
 * @see [visible]
 * @see [opaque]
 * @see [transparent]
 */
fun View.fullVisible(): View {
    opaque().visible()
    return this
}

/**
 * Set the view's visibility to [View.GONE]
 *
 * **Note** this does not change the alpha value. Chain with [opaque], [transparent]  e.g.
 * ```
 * View.transparent().gone()
 * ```
 * @see [opaque]
 * @see [transparent]
 */
fun View.gone(): View {
    this.visibility = View.GONE
    return this
}


/**
 * Set the view's visibility to [View.GONE]
 *
 * **Note** this does not change the alpha value. Chain with [opaque], [transparent]  e.g.
 * ```
 * View.transparent().gone()
 * ```
 * @see [opaque]
 * @see [transparent]
 */
fun View.gone(gone: Boolean): View {
    if (gone) this.gone() else this.visible()
    return this
}


/**
 * Set the view's visibility to [View.INVISIBLE]
 *
 * **Note** this does not change the alpha value. Chain with [opaque], [transparent]  e.g.
 * ```
 * View.transparent().gone()
 * ```
 * @see [opaque]
 * @see [transparent]
 */
fun View.invisible(): View {
    this.visibility = View.INVISIBLE
    return this
}

/**
 * Set the view's visibility to either [View.INVISIBLE] or [View.VISIBLE]
 *
 * **Note** this does not change the alpha value. Chain with [opaque], [transparent]  e.g.
 * ```
 * View.opaque().visible()
 * ```
 * @see [opaque]
 * @see [transparent]
 *
 * @param setInvisible True will result in [View.INVISIBLE]; False in [View.VISIBLE]
 */
fun View.invisible(setInvisible: Boolean): View {
    if (setInvisible) this.invisible() else this.visible()
    return this
}

/**
 * Set the view's visibility to either [View.VISIBLE] or [View.GONE]
 *
 * **Note** this does not change the alpha value. Chain with [opaque], [transparent]  e.g.
 * ```
 * View.opaque().visible()
 * ```
 * @see [opaque]
 * @see [transparent]
 *
 * @param setVisible True will result in [View.VISIBLE]; False in [View.GONE]
 */
fun View.visible(setVisible: Boolean): View {
    if (setVisible) this.visible() else this.gone()
    return this
}


@SuppressLint("LogNotTimber")
fun measureDimension(desiredSize: Int, measureSpec: Int): Int {
    var result: Int
    val specMode = View.MeasureSpec.getMode(measureSpec)
    val specSize = View.MeasureSpec.getSize(measureSpec)
    if (specMode == View.MeasureSpec.EXACTLY) {
        result = specSize
    } else {
        result = desiredSize
        if (specMode == View.MeasureSpec.AT_MOST) {
            result = result.coerceAtMost(specSize)
        }
    }
    if (result < desiredSize) {
        Timber.w("The view is too small, the content might get cut. Desired: $desiredSize, actual: $result")
    }
    return result
}

fun StaticLayout(text: CharSequence, textPaint: TextPaint, width: Int, alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL): StaticLayout {
    return StaticLayout.Builder.obtain(text, 0, text.length, textPaint, width.coerceAtLeast(0))
        .setAlignment(alignment)
        .setIncludePad(true)
        .setLineSpacing(0f, 1f)
        .build()
}

fun android.widget.TextView.isEllipsized(): Boolean {
    val l: Layout = layout ?: return false

    val lines = l.lineCount
    return (lines > 0) && l.getEllipsisCount(lines - 1) > 0
}


fun android.widget.TextView.calculateLineCount(lineWidth: Int): Int {
    val staticLayout = StaticLayout(text, paint, lineWidth, Layout.Alignment.ALIGN_NORMAL)
    return staticLayout.lineCount
}
