package at.nukular.core

import android.app.Activity
import android.app.Application
import android.os.Bundle

class AppLifecycleTracker(
    listener: AppStateCallback? = null,
) : Application.ActivityLifecycleCallbacks {

    interface AppStateCallback {
        fun onAppMovedToForeground()
        fun onAppMovedToBackground()
    }

    private var numStarted = 0
    private val _listeners: MutableList<AppStateCallback> = mutableListOf()
    val listeners: List<AppStateCallback> = _listeners

    init {
        listener?.let { _listeners.add(it) }
    }

    override fun onActivityStarted(activity: Activity) {
        if (numStarted == 0) {
            listeners.forEach {
                it.onAppMovedToForeground()
            }
        }
        numStarted++
    }


    override fun onActivityStopped(activity: Activity) {
        numStarted--
        if (numStarted == 0) {
            listeners.forEach {
                it.onAppMovedToBackground()
            }
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }
}