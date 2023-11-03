package com.felix.tools

import android.text.TextUtils
import android.util.Log

/**
 * Created by zyf on 17-10-29.
 */
object ToolLog {
    /**
     * 是否显示日志
     */
    private var mIsDebug = true

    @JvmStatic
    fun d(tag: String?, msg: String?) {
        if (mIsDebug && !TextUtils.isEmpty(msg)) Log.d(tag, msg!!)
    }

    @JvmStatic
    fun v(tag: String?, msg: String?) {
        if (mIsDebug && !TextUtils.isEmpty(msg)) Log.v(tag, msg!!)
    }

    @JvmStatic
    fun i(tag: String?, msg: String?) {
        if (mIsDebug && !TextUtils.isEmpty(msg)) Log.i(tag, msg!!)
    }

    @JvmStatic
    fun w(tag: String?, msg: String?) {
        if (mIsDebug && !TextUtils.isEmpty(msg)) Log.w(tag, msg!!)
    }

    @JvmStatic
    fun e(tag: String?, error: String?) {
        if (mIsDebug && !TextUtils.isEmpty(error)) Log.e(tag, error!!)
    }

    @JvmStatic
    fun printStackTrace(t: Throwable?) {
        if (mIsDebug && t != null) t.printStackTrace()
    }

    /**
     * 打印超长log
     *
     * @param tag
     * @param msg
     */
    fun l(tag: String, msg: String) {
        var log = msg
        if (mIsDebug && !TextUtils.isEmpty(msg)) {
            val maxStrLength = 2001 - tag.length
            while (msg.length > maxStrLength) {
                Log.e(tag, msg.substring(0, maxStrLength))
                log = msg.substring(maxStrLength)
            }
            //剩余部分
            Log.e(tag, log)
        }
    }

    /**
     * 是否打印日志
     */
    @JvmStatic
    fun isDebug() = mIsDebug

    /**
     * 设置APP开的启debug模式下是否打印日志
     */
    fun setDebug(value: Boolean) {
        mIsDebug = value
    }
}