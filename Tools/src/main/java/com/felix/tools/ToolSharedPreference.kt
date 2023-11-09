package com.felix.tools

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Base64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
 * Created by FelixZhou on 2023/9/11.
 * Dsc :
 */
object ToolSharedPreference {

    private var mPreferences: SharedPreferences? = null

    @JvmStatic
    fun Context.getSharedPreferences(): SharedPreferences {
        mPreferences?.let {
            return it
        }
        return getSharedPreferences(packageName, Context.MODE_PRIVATE).apply {
            mPreferences = this
        }
    }

    /**
     * 保存数据公用类：String
     */
    @JvmStatic
    fun Context.saveSpString(key: String, value: String) {
        if (key.isEmpty() || value.isEmpty()) {
            return
        }
        val editor = getSharedPreferences().edit()
        editor.putString(key, value)
        editor.apply()
    }

    /**
     * 获取数据公用类:String
     */
    @JvmStatic
    @JvmOverloads
    fun Context.getSpString(key: String, default: String? = null): String? {
        if (TextUtils.isEmpty(key)) {
            return default
        }
        return getSharedPreferences().getString(key, default)
    }

    /**
     * 保存数据公用类:Int
     */
    @JvmStatic
    fun Context.saveSpInt(key: String, value: Int) {
        if (TextUtils.isEmpty(key)) {
            return
        }
        val editor = getSharedPreferences().edit()
        editor.putInt(key, value)
        editor.apply()
    }


    /**
     * 获取数据公用类:Int
     */
    @JvmStatic
    fun Context.getSpInt(key: String, default: Int): Int {
        if (TextUtils.isEmpty(key)) {
            return default
        }
        return getSharedPreferences().getInt(key, default)
    }

    /**
     * 保存数据公用类:Long
     */
    @JvmStatic
    fun Context.saveSpLong(key: String, value: Long) {
        if (TextUtils.isEmpty(key)) {
            return
        }
        val editor = getSharedPreferences().edit()
        editor.putLong(key, value)
        editor.apply()
    }

    /**
     * 获取数据公用类:Long
     */
    @JvmStatic
    fun Context.getSpLong(key: String, default: Long): Long {
        if (TextUtils.isEmpty(key)) {
            return default
        }
        return getSharedPreferences().getLong(key, default)
    }

    /**
     * 保存数据公用类:Boolean
     */
    @JvmStatic
    fun Context.saveSpBoolean(key: String, value: Boolean) {
        if (TextUtils.isEmpty(key)) {
            return
        }
        val editor = getSharedPreferences().edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    /**
     * 获取数据公用类:Boolean
     */
    @JvmStatic
    fun Context.getSpBoolean(key: String, default: Boolean): Boolean {
        if (TextUtils.isEmpty(key)) {
            return default
        }
        return getSharedPreferences().getBoolean(key, default)
    }

    @JvmStatic
    fun Context.saveSpSerializable(key: String, value: Serializable) {
        if (TextUtils.isEmpty(key)) {
            return
        }
        try {
            val bos = ByteArrayOutputStream()
            val oos = ObjectOutputStream(bos)
            oos.writeObject(value)
            val objBase64 = String(Base64.encode(bos.toByteArray(), Base64.DEFAULT))
            saveSpString(key, objBase64)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun Context.getSpSerializable(key: String): Serializable? {
        if (TextUtils.isEmpty(key)) {
            return null
        }
        try {
            val objBase64: String? = getSpString(key, null)
            if (TextUtils.isEmpty(objBase64)) {
                return null
            }
            val base64: ByteArray = Base64.decode(objBase64, Base64.DEFAULT)
            val bais = ByteArrayInputStream(base64)
            val bis = ObjectInputStream(bais)
            return bis.readObject() as Serializable?
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 清空本地缓存中某个值
     *
     * @param context
     * @param keys
     */
    @JvmStatic
    fun Context.clearValue(vararg keys: String) {
        val preferences: SharedPreferences = getSharedPreferences()
        val editor = preferences.edit()
        keys.forEach {
            if (TextUtils.isEmpty(it)) {
                return@forEach
            }
            if (preferences.contains(it)) {
                editor.remove(it)
            }
        }
        editor.apply()
    }

    @JvmStatic
    fun Context.clearAll() {
        val editor: SharedPreferences.Editor = getSharedPreferences().edit()
        editor.clear()
        editor.apply()
    }
}