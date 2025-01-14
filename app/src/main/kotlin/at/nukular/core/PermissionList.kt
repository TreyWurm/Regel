package at.nukular.core

sealed class PermissionList : HashMap<String, Status>() {
    fun allGranted(): Boolean = all { it.value == Status.GRANTED }
    fun anyGranted(): Boolean = any { it.value == Status.GRANTED }
    fun noneGranted(): Boolean = none { it.value == Status.GRANTED }
}

class LocationPermissionList : PermissionList() {
    companion object {
        fun fromPermissions(vararg permissions: String): PermissionList {
            val permissionList = LocationPermissionList()
            permissions.forEach { permissionList[it] = Status.TBD }
            return permissionList
        }
    }
}

class BluetoothPermissionList : PermissionList() {
    companion object {
        fun fromPermissions(vararg permissions: String): PermissionList {
            val permissionList = BluetoothPermissionList()
            permissions.forEach { permissionList[it] = Status.TBD }
            return permissionList
        }
    }
}


enum class Status {
    GRANTED,
    DENIED,
    TBD,
    ;
}