package com.felix.tools

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.InetAddresses
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader


/**
 * Created by zyf on 2023-05-18 18:13
 * Describe: 设备工具类
 */

/**
 * 是否是平板,宽度(长宽最短的一个)大于600dp就是平板，返回true
 */
fun Context.isTablet() = resources.getBoolean(R.bool.isTablet)

/**
 * 获取APP的targetSdkVersion
 */
fun Context.targetSdkVersion() = applicationContext.applicationInfo.targetSdkVersion


/**
 * 是否开启开发者模式
 */
fun Context.isOpenDevelopSetting() =
    Settings.Secure.getInt(contentResolver, Settings.Global.ADB_ENABLED, 0) > 0

/**
 * 是否开启adb调试
 */
fun Context.isOpenAdbSetting() =
    Settings.Secure.getInt(contentResolver, Settings.Global.ADB_ENABLED, 0) > 0

/**
 * root文件可能存在的路径类别
 */
private val ROOT_DIRS by lazy {
    arrayOf(
        "/su",
        "/su/bin/su",
        "/sbin/su",
        "/data/local/xbin/su",
        "/data/local/bin/su",
        "/data/local/su",
        "/system/xbin/su",
        "/system/bin/su",
        "/system/sd/xbin/su",
        "/system/bin/failsafe/su",
        "/system/bin/cufsdosck",
        "/system/xbin/cufsdosck",
        "/system/bin/cufsmgr",
        "/system/xbin/cufsmgr",
        "/system/bin/cufaevdd",
        "/system/xbin/cufaevdd",
        "/system/bin/conbb",
        "/system/xbin/conbb"
    )
}

/**
 * 是否root
 */
fun isRoot(): Boolean {
    //判断条件一,su文件是否存在
    ROOT_DIRS.forEach {
        if(File(it).exists()){
            return true
        }
    }
    //判断条件二,root权限管理APK是否存在
    if (File("/system/app/Superuser.apk").exists()) {
        return true
    }
    //判断条件三,执行su命令
    try {
        val fullResponse = ArrayList<String>()
        val localProcess = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
        val input = BufferedReader(InputStreamReader(localProcess.inputStream))
        var line: String? = null
        while (input.readLine().also { line = it } != null) {
            fullResponse.add(line!!)
        }
        return true
    } catch (e: Exception) {
        ToolLog.printStackTrace(e)
    }
     //判断条件三,判断Build.TAGS
    if (Build.TAGS != null && Build.TAGS.contains("test-keys")) {
        return true
    }
    return false
}
