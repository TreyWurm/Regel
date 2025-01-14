package at.nukular.core

interface PermissionListener {
    fun onPermissionsChanged(permissionList: PermissionList)
}