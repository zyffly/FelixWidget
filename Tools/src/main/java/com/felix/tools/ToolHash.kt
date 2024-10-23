package com.felix.tools

import java.io.File
import java.io.FileInputStream
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

/**
 * 获取字符串的SHA256
 * @param upperCase SHA256是大写还是小写  true 大写  false 小写
 * @return
 */
fun String.getSHA256(upperCase: Boolean = false): String {
    var sha256str = ""
    try {
        val sha256 = MessageDigest.getInstance("SHA-256")
        val input = this.toByteArray(StandardCharsets.UTF_8)
        val buff = sha256.digest(input)
        sha256str = bytesToHex(buff, upperCase)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return sha256str
}

/**
 * 获取文件的SHA256
 * @param upperCase SHA256是大写还是小写  true 大写  false 小写
 * @return
 */
fun File.getSHA256(upperCase: Boolean = false): String {
    var sha256str = ""
    try {
        val sha256 = MessageDigest.getInstance("SHA-256")
        val fis = FileInputStream(this)
        val buffer = ByteArray(1024)
        var len: Int
        while (fis.read(buffer, 0, 1024).also { len = it } != -1) {
            sha256.update(buffer, 0, len)
        }
        val buff = sha256.digest()
        sha256str = bytesToHex(buff, upperCase)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return sha256str
}

private fun bytesToHex(bytes: ByteArray, upperCase: Boolean): String {
    val hashString = bytes.joinToString("") { "%02x".format(it) }
    return if (upperCase) {
        hashString.uppercase(Locale.getDefault())
    } else hashString.lowercase(Locale.getDefault())
}