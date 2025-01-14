package at.nukular.core.model

import android.content.Context
import android.os.Parcelable
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

/**
 * Modified com.mikepenz.materialdrawer.holder.StringHolder to work with Parcelable
 *
 * Defines a custom holder class to support providing strings either as string or resource. Does not require a [Context] and will resolve the value when applying.
 */
@Parcelize
class ParcelableStringHolder(
    val textString: CharSequence? = null,
    val textRes: Int = -1,
) : Parcelable {

    constructor(text: CharSequence?) : this(textString = text, textRes = -1)

    constructor(@StringRes textRes: Int) : this(textString = null, textRes = textRes)

    /**
     * Applies the text to a [TextView]
     */
    fun applyTo(textView: TextView?) {
        when {
            textString != null -> textView?.text = textString
            textRes != -1 -> textView?.setText(textRes)
            else -> textView?.text = ""
        }
    }

    /**
     * Applies the [TextView] if no text given, hide the textView
     */
    fun applyToOrHide(textView: TextView?): Boolean {
        textView ?: return false
        return when {
            textString != null -> {
                textView.text = textString
                textView.visibility = View.VISIBLE
                true
            }

            textRes != -1 -> {
                textView.setText(textRes)
                textView.visibility = View.VISIBLE
                true
            }

            else -> {
                textView.visibility = View.GONE
                false
            }
        }
    }

    /**
     * Returns the text as [String]
     */
    fun getText(ctx: Context): String? {
        if (textString != null) {
            return textString.toString()
        } else if (textRes != -1) {
            return ctx.getString(textRes)
        }
        return null
    }

    companion object {
        /**
         * Helper to apply the text to a [TextView]
         */
        fun applyTo(text: ParcelableStringHolder?, textView: TextView?) {
            text?.applyTo(textView)
        }

        /**
         * Helper to apply the text to a [TextView] or hide if null
         */
        fun applyToOrHide(text: ParcelableStringHolder?, textView: TextView?): Boolean {
            return if (text != null) {
                text.applyToOrHide(textView)
            } else {
                textView?.visibility = View.GONE
                false
            }
        }
    }
}

fun String.toParcelableStingHolder(): ParcelableStringHolder = ParcelableStringHolder(this)
