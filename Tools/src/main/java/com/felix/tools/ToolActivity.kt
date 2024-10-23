package com.felix.tools

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.CallSuper

fun Context.startAct(cls: Class<*>) {
    startActivity(Intent(this, cls))
}

inline fun Context.startAct(cls: Class<*>, putExtra: (intent: Intent) -> Intent) {
    startActivity(putExtra(Intent(this, cls)))
}

fun Activity.launchAct(launcher: ActivityResultLauncher<Intent>, cls: Class<*>) {
    launcher.launch(Intent(this, cls))
}

inline fun Activity.launchAct(launcher: ActivityResultLauncher<Intent>, cls: Class<*>, putExtra: (intent: Intent) -> Intent?) {
    launcher.launch(putExtra(Intent(this, cls)))
}

fun ActivityResultLauncher<Intent>.launchAct(context: Context, cls: Class<*>) {
    launch(Intent(context, cls))
}

inline fun ActivityResultLauncher<Intent>.launchAct(context: Context, cls: Class<*>, putExtra: (intent: Intent) -> Intent?) {
    launch(putExtra(Intent(context, cls)))
}

/**
 * 标准注册Activity回调，代替startActivityForResult
 */
fun ComponentActivity.registerStandardActivityResult(
        callback: ActivityResultCallback<ActivityResult>) =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult(), callback)

/**
 * 调用系统相机
 */
fun ComponentActivity.registerTakePictureActivityResult(callback: ActivityResultCallback<Boolean>) =
        registerForActivityResult(ActivityResultContracts.TakePicture(), callback)


private class Album : ActivityResultContract<Unit?,Uri?>() {

    override fun createIntent(context: Context, input: Unit?): Intent {
        return Intent(Intent.ACTION_PICK).setType("image/*")
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return intent?.data
    }
}

/**
 * 调用系统图库
 */
fun ComponentActivity.registerAlbumActivityResult(callback: ActivityResultCallback<Uri?>) =
        registerForActivityResult(Album(), callback)


/**
 * 调用系统录像
 */
fun ComponentActivity.registerCaptureVideoActivityResult(callback: ActivityResultCallback<Boolean>) =
    registerForActivityResult(ActivityResultContracts.CaptureVideo(), callback)