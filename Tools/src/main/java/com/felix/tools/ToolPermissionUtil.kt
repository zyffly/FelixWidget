package com.felix.tools

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * Created by zyf on 2023-05-23 18:33
 * Describe:
 */


/**
 * 检查sd卡读写权限
 */
fun checkSDCardPermission(context: Context): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
    return true
}


/**
 * 检查相机权限
 */
fun Context.checkCameraPermission(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
    return true
}

/**
 * 检查相机权限
 */
fun Context.checkAudioRecordPermission(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }
    return true
}

/**
 * 批量检测权限
 */
fun Array<String>.checkPermissions(context: Context): Array<String>? {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        return null
    }
    val arrayList = ArrayList<String>()
    forEach {
        if (!it.checkPermission(context)) {
            arrayList.add(it)
        }
    }

    if (arrayList.isEmpty()) {
        return null
    }
    return arrayList.toTypedArray()
}

/**
 * 检测权限
 */
fun String.checkPermission(context: Context): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        return ContextCompat.checkSelfPermission(
            context, this
        ) == PackageManager.PERMISSION_GRANTED
    }
    return true
}

/**
 * 权限申请
 */
fun ComponentActivity.registerRequestPermissionResult(callback: ActivityResultCallback<Boolean>) =
    registerForActivityResult(ActivityResultContracts.RequestPermission(), callback)

/**
 * 多个权限申请
 */
fun ComponentActivity.registerRequestMultiplePermissionResult(callback: ActivityResultCallback<Map<String, Boolean>>) =
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), callback)


/**
 * 权限申请
 */
fun Fragment.registerRequestPermissionResult(callback: ActivityResultCallback<Boolean>) =
    registerForActivityResult(ActivityResultContracts.RequestPermission(), callback)

/**
 * 多个权限申请
 */
fun Fragment.registerRequestMultiplePermissionResult(callback: ActivityResultCallback<Map<String, Boolean>>) =
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), callback)


