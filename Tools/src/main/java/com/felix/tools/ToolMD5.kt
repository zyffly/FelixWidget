package com.felix.tools

import java.io.File
import java.io.FileInputStream
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.Locale


/**
 * Created by FelixZhou on 2023/9/12.
 * Dsc :
 */
/**
 * 获取字符串的MD5
 * @param upperCase MD5是大写还是小写  true 大写  false 小写
 * @return
 */
fun String.getMD5(upperCase: Boolean = false): String {
    var md5str = ""
    try {
        val md = MessageDigest.getInstance("MD5")
        val input = this.toByteArray(StandardCharsets.UTF_8)
        val buff = md.digest(input)
        md5str = bytesToHex(buff, upperCase)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return md5str
}

private fun bytesToHex(bytes: ByteArray, upperCase: Boolean): String {
    val bigInt = BigInteger(1, bytes)
    var md5 = bigInt.toString(16)
    while (md5.length < 32) {
        md5 = "0$md5"
    }
    return if (upperCase) {
        md5.toString().uppercase(Locale.getDefault())
    } else md5.toString().lowercase(Locale.getDefault())
}

/**
 * 获取文件的MD5
 * @param upperCase MD5是大写还是小写  true 大写  false 小写
 * @return
 */
fun File.getMD5(upperCase: Boolean = false): String {
    var md5str = ""
    try {
        val md = MessageDigest.getInstance("MD5")
        val fis = FileInputStream(this)
        val buffer = ByteArray(1024)
        var len: Int
        while (fis.read(buffer, 0, 1024).also { len = it } != -1) {
            md.update(buffer, 0, len)
        }
        val buff = md.digest()
        md5str = bytesToHex(buff, upperCase)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return md5str
}