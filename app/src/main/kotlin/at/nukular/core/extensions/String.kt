package at.nukular.core.extensions

import android.text.Spannable
import android.util.Base64


/**
 * Sets the given span if possible
 * If an exception occurred (e.g. IndexOutOfBoundsException) leave as is and write stackTrace
 */
fun Spannable.setSpanSafely(what: Any, start: Int, end: Int, flags: Int) {
    try {
        setSpan(what, start, end, flags)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun String.safeFirst(): String = if (isEmpty()) "" else first().toString()


fun String.base64Encoded(): String = Base64.encodeToString(this.toByteArray(), Base64.DEFAULT)
fun String.base64Decoded(): String = Base64.decode(this, Base64.DEFAULT).toString()
fun String.dropCommaIfFirst(): String = if (indexOf(",") == 0) drop(1) else this