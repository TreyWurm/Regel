package at.nukular.core.extensions

import at.nukular.core.extensions.TimberExtensions.getFullName
import eu.davidea.flexibleadapter.BuildConfig
import timber.log.Timber
import java.util.regex.Pattern
import kotlin.reflect.KCallable


private const val DEFAULT_INDENT = 4
private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")


fun prependIndent(text: String = "", indentLevel: Int = 0): String {
    var indent = ""
    for (i in 0 until indentLevel * DEFAULT_INDENT) {
        indent += " "
    }
    return indent + text
}


fun Timber.Forest.indentV(message: String, indentLevel: Int = 0) {
    Timber.Forest.v(prependIndent(message, indentLevel))
}


fun Timber.Forest.indentD(message: String, indentLevel: Int = 0) {
    Timber.Forest.d(prependIndent(message, indentLevel))
}

fun Timber.Forest.indentI(message: String, indentLevel: Int = 0) {
    Timber.Forest.i(prependIndent(message, indentLevel))
}

fun Timber.Forest.indentW(message: String, indentLevel: Int = 0) {
    Timber.Forest.w(prependIndent(message, indentLevel))
}

fun Timber.Forest.indentE(message: String, indentLevel: Int = 0) {
    Timber.Forest.e(prependIndent(message, indentLevel))
}

fun Timber.Forest.indentWTF(message: String, indentLevel: Int = 0) {
    Timber.Forest.wtf(prependIndent(message, indentLevel))
}


/**
 * Log a verbose message
 * If we are on debug build log class+function name using reflection
 */
fun Timber.Forest.nameV(message: String) {
    if (BuildConfig.DEBUG) {
        Timber.Forest.v(getFullName() + ": " + message)
    } else {
        Timber.Forest.v(message)
    }
}

/**
 * Log a debug message
 * If we are on debug build log class+function name using reflection
 */
fun Timber.Forest.nameD(message: String) {
    if (BuildConfig.DEBUG) {
        Timber.Forest.d(getFullName() + ": " + message)
    } else {
        Timber.Forest.d(message)
    }
}

/**
 * Log a info message
 * If we are on debug build log class+function name using reflection
 */
fun Timber.Forest.nameI(message: String) {
    if (BuildConfig.DEBUG) {
        Timber.Forest.i(getFullName() + ": " + message)
    } else {
        Timber.Forest.i(message)
    }
}

/**
 * Log a warning message
 * If we are on debug build log class+function name using reflection
 */
object TimberExtensions {
    private val fqcnIgnore = listOf(
        TimberExtensions::class.java.name,
    )

    fun createStackElementTag(element: StackTraceElement): String {
        var tag = element.className.substringAfterLast('.')
        val m = ANONYMOUS_CLASS.matcher(tag)
        if (m.find()) {
            tag = m.replaceAll("")
        }

        return tag
    }

    internal fun getFullName(): String {
        val stackTrace = Throwable().stackTrace
        createStackElementTag(stackTrace.first { it.className !in fqcnIgnore })
        return ""
    }

    fun Timber.Forest.nameW(message: String, kFunction: KCallable<*>) {
        if (BuildConfig.DEBUG) {
            Timber.Forest.w(getFullName() + ": " + message)
        } else {
            Timber.Forest.w(message)
        }
    }
}

/**
 * Log an error message
 * If we are on debug build log class+function name using reflection
 */
fun Timber.Forest.nameE(message: String) {
    if (BuildConfig.DEBUG) {
        Timber.Forest.e(getFullName() + ": " + message)
    } else {
        Timber.Forest.e(message)
    }
}

/**
 * Log an assert message
 * If we are on debug build log class+function name using reflection
 */
fun Timber.Forest.nameWTF(message: String) {
    if (BuildConfig.DEBUG) {
        Timber.Forest.wtf(getFullName() + ": " + message)
    } else {
        Timber.Forest.wtf(message)
    }
}