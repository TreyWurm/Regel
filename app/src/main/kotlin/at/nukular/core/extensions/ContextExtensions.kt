package at.nukular.core.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent

fun Context.isValid(): Boolean {
    if (this is Activity) {
        return !this.isDestroyed && !this.isFinishing
    }
    return true
}


fun Context.createActivityIntent(
    target: Class<*>,
    flags: Int? = null,
    forwardIntent: Intent? = null,
): Intent {
    val intent = Intent(this, target)
    forwardIntent?.let {
        intent.action = it.action
        intent.putExtras(it)
    }
    flags?.let { intent.flags = it }
    return intent
}

fun Context.startActivity(
    target: Class<*>,
    flags: Int? = null,
    forwardIntent: Intent? = null,
) {
    val starter = createActivityIntent(target, flags, forwardIntent)
    this.startActivity(starter)
}