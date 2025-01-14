package at.nukular.core.extensions

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.ResolveInfoFlags
import android.content.pm.ResolveInfo
import android.os.Build


fun PackageManager.queryIntentActivitiesCompat(intent: Intent, flag: Int): List<ResolveInfo> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        queryIntentActivities(intent, ResolveInfoFlags.of(flag.toLong()))
    } else {
        @Suppress("DEPRECATION")
        queryIntentActivities(intent, flag)
    }
}