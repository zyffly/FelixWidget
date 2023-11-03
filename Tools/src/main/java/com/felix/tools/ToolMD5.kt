package com.felix.tools

import java.security.MessageDigest
import java.util.Locale


/**
 * Created by FelixZhou on 2023/9/12.
 * Dsc :
 */
object ToolMD5 {
    /**
     * 获取字符串的MD5
     * @param message   加密明文
     * @param upperCase MD%是大写还是小写  true 大写  false 小写
     * @return
     */
    fun String.getMD5(upperCase: Boolean = false): String {
        var md5str = ""
        try {
            val md = MessageDigest.getInstance("MD5")
            val input = this.toByteArray()
            val buff = md.digest(input)
            md5str = bytesToHex(buff, upperCase)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return md5str
    }

    private fun bytesToHex(bytes: ByteArray, upperCase: Boolean): String {
        val md5str = StringBuffer()
        var digital: Int
        for (i in bytes.indices) {
            digital = bytes[i].toInt()
            if (digital < 0) {
                digital += 256
            }
            if (digital < 16) {
                md5str.append("0")
            }
            md5str.append(Integer.toHexString(digital))
        }
        return if (upperCase) {
            md5str.toString().uppercase(Locale.getDefault())
        } else md5str.toString().lowercase(Locale.getDefault())
    }
}