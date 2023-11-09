package com.felix.tools

/**
 * Created by FelixZhou on 2023/9/11.
 * Dsc :
 */
object Tool {
    fun setDebug(value: Boolean) {
        ToolLog.setDebug(value)
    }

    fun getVersionName() = BuildConfig.VERSION_NAME

    fun getVersionCode() = BuildConfig.VERSION_CODE

    fun getVersionDsc() = BuildConfig.VERSION_DSC

    fun getVersion() = "${getVersionName()} ${getVersionCode()}  ${getVersionDsc()}"
}