package at.nukular.core

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ActivityPermissionManager(private val activity: Activity) {

    private var listener: PermissionListener? = activity as? PermissionListener

    private val lifecycleListener: Application.ActivityLifecycleCallbacks by lazy {
        object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

            override fun onActivityStarted(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {
                if (this@ActivityPermissionManager.activity == activity) {
                    listener = null
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {}
        }
    }

    init {
        if (listener != null) {
            addLifecycleListener()
        }
    }

    private fun addLifecycleListener() {
        activity.application.registerActivityLifecycleCallbacks(lifecycleListener)
    }


    // ====================================================================================================================================================================================
    // region SMS Permissions

    fun requestSMSPermissions(requestCode: Int = REQUEST_SMS) {
        checkOrRequestPermissions(requestCode = requestCode, LocationPermissionList.fromPermissions(SMS_SEND_PERMISSION))
    }

    fun isSMSPermissionGranted(): Boolean = isPermissionGranted(SMS_SEND_PERMISSION)
    // endregion


    // ====================================================================================================================================================================================
    // region Generic

    private fun runtimePermissionRequired(): Boolean = true

    private fun isPermissionGranted(permission: String): Boolean = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED


    private fun shouldAskPermission(permission: String): Boolean {
        if (runtimePermissionRequired()) {
            val permissionResult = ContextCompat.checkSelfPermission(activity, permission)
            return permissionResult != PackageManager.PERMISSION_GRANTED
        }
        return false
    }
    // endregion

    private fun checkOrRequestPermissions(requestCode: Int, permissionList: PermissionList) {
        // Only check permissions for SDK >= 23
        if (runtimePermissionRequired().not()) {
            return
        }

        // Check if permissions are already granted
        val iterator = permissionList.keys.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            val permissionResult = ContextCompat.checkSelfPermission(activity, key)
            if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                permissionList[key] = Status.GRANTED
            }
        }

        // If all permissions are granted we can continue our normal app flow
        if (permissionList.allGranted()) {
            listener?.onPermissionsChanged(permissionList)
            return
        }

        // If not we need to request the missing permissions
        ActivityCompat.requestPermissions(activity, permissionList.keys.toTypedArray(), requestCode)
    }


    companion object {
        const val REQUEST_SMS = 0x100

        const val SMS_SEND_PERMISSION = Manifest.permission.SEND_SMS
    }
}