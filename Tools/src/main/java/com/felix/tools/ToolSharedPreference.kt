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
    fun getSharedPreferences(context: Context): SharedPreferences {
        mPreferences?.let {
            return it
        }
        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).apply {
            mPreferences = this
        }
    }

    /**
     * 保存数据公用类：String
     */
    @JvmStatic
    fun saveString(context: Context, key: String?, value: String?) {
        if (key.isNullOrEmpty() || value.isNullOrEmpty()) {
            return
        }
        val editor = getSharedPreferences(context).edit()
        editor.putString(key, value)
        editor.apply()
    }

    /**
     * 获取数据公用类:String
     */
    @JvmStatic
    @JvmOverloads
    fun getString(context: Context, key: String, default: String? = null): String? {
        if (TextUtils.isEmpty(key)) {
            return default
        }
        return getSharedPreferences(context).getString(key, default)
    }

    /**
     * 保存数据公用类:Int
     */
    @JvmStatic
    fun saveInt(context: Context, key: String, value: Int) {
        if (TextUtils.isEmpty(key)) {
            return
        }
        val editor = getSharedPreferences(context).edit()
        editor.putInt(key, value)
        editor.apply()
    }


    /**
     * 获取数据公用类:Int
     */
    @JvmStatic
    fun getInt(context: Context, key: String, default: Int): Int {
        if (TextUtils.isEmpty(key)) {
            return default
        }
        return getSharedPreferences(context).getInt(key, default)
    }

    /**
     * 保存数据公用类:Long
     */
    @JvmStatic
    fun saveLong(context: Context, key: String, value: Long) {
        if (TextUtils.isEmpty(key)) {
            return
        }
        val editor = getSharedPreferences(context).edit()
        editor.putLong(key, value)
        editor.apply()
    }

    /**
     * 获取数据公用类:Long
     */
    @JvmStatic
    fun getLong(context: Context, key: String, default: Long): Long {
        if (TextUtils.isEmpty(key)) {
            return default
        }
        return getSharedPreferences(context).getLong(key, default)
    }

    /**
     * 保存数据公用类:Boolean
     */
    @JvmStatic
    fun saveBoolean(context: Context, key: String, value: Boolean) {
        if (TextUtils.isEmpty(key)) {
            return
        }
        val editor = getSharedPreferences(context).edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    /**
     * 获取数据公用类:Boolean
     */
    @JvmStatic
    fun getBoolean(context: Context, key: String, default: Boolean): Boolean {
        if (TextUtils.isEmpty(key)) {
            return default
        }
        return getSharedPreferences(context).getBoolean(key, default)
    }

    @JvmStatic
    fun saveSerializable(context: Context, key: String, value: Serializable?) {
        if (TextUtils.isEmpty(key) || null == value) {
            return
        }
        try {
            val baos = ByteArrayOutputStream()
            val oos = ObjectOutputStream(baos)
            oos.writeObject(value)
            val objBase64 = String(Base64.encode(baos.toByteArray(), Base64.DEFAULT))
            saveString(context, key, objBase64)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun getSerializable(context: Context, key: String): Serializable? {
        try {
            val objBase64: String? = getString(context, key, null)
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
    fun clearValue(context: Context, vararg keys: String) {
        val preferences: SharedPreferences = getSharedPreferences(context)
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
    fun clearAll(context: Context) {
        val editor: SharedPreferences.Editor = getSharedPreferences(context).edit()
        editor.clear()
        editor.apply()
    }
}