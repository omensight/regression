package com.alphemsoft.education.regression.util

import android.content.Context
import android.webkit.PermissionRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.alphemsoft.education.regression.constants.PermissionConstants

object PermissionHandler {
//    private val callbacks: MutableMap<String, () -> Unit> = HashMap()

    fun checkPermissions(context: Context, vararg permission: String): Boolean {
        var granted = true
        var index = 0
        while (granted && permission.size > index) {
            granted = granted &&
                    (ContextCompat.checkSelfPermission(context, permission[index++])
                            == PermissionChecker.PERMISSION_GRANTED)
        }

        return granted
    }

    fun checkSinglePermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PermissionChecker.PERMISSION_GRANTED
    }

    fun checkAcceptedPermissions(
        permissions: Array<out String>,
        grantResults: IntArray
    ): Boolean {
        var accepted = true
        var index = 0
        while (accepted && index < permissions.size){
            accepted = accepted && (grantResults[index++] == PermissionChecker.PERMISSION_GRANTED)
        }
        return accepted
    }

}
