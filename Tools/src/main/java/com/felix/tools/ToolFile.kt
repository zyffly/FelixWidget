package com.felix.tools

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.content.FileProvider
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter


/**
 * 获取APP缓存目录绝对路径（app专用缓存目录）
 * @return 绝对路径
 */
fun Context.getDirPath(): String? {
    externalCacheDir?.let {
        if (it.exists() || it.mkdirs()) {
            return it.absolutePath
        }
    }
    cacheDir?.let {
        if (it.exists() || it.mkdirs()) {
            return it.absolutePath
        }
    }
    return null
}

fun createFile(path: String, name: String, suffix: String) =
    try {
        path.mkdirs()
        File("$path${File.separator}${name}$suffix").apply {
            if (exists()) {
                delete()
            }
            createNewFile()
        }
    } catch (e: Exception) {
        ToolLog.printStackTrace(e)
        null
    }


/**
 * 创建目录
 */
fun String.mkdirs() {
    val file = File(this)
    if (!file.exists()) {
        file.mkdirs()
    }
}

/**
 * 保存图片到指定路径文件
 */
fun Bitmap.saveToFile(path: String): Boolean {
    path.mkdirs()
    val file = File(path)
    if (file.exists()) {
        file.delete()
    }
    try {
        file.createNewFile()
        // 0-100 100为不压缩
        val os = ByteArrayOutputStream()
        // 把压缩后的数据存放到os中
        compress(Bitmap.CompressFormat.JPEG, 100, os)

        val fos = FileOutputStream(file)
        fos.write(os.toByteArray())
        fos.flush()
        fos.close()
    } catch (e: Exception) {
        ToolLog.printStackTrace(e)
        return false
    }
    return true
}

/**
 * 路径转换Uri
 */
fun File.toUri(context: Context): Uri =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(
            context,
            "${context.applicationContext.packageName}.fileProvider",
            this
        )
    } else {
        Uri.fromFile(this)
    }

/**
 * 读取json文件输出string类型
 *
 * @param context
 * @param txt
 * @return
 */
fun String.readAssetFile(context: Context): String? {
    try {
        val stringBuilder = StringBuilder()
        //获取assets资源管理器
        val assetManager = context.assets
        //通过管理器打开文件并读取
        BufferedReader(
            InputStreamReader(
                assetManager.open(this)
            )
        ).run {
            var line: String?
            do {
                line = readLine()
                if (line != null) {
                    stringBuilder.append(line)
                } else {
                    break
                }
            } while (true)
            close()
            return stringBuilder.toString()
        }
    } catch (e: IOException) {
        ToolLog.printStackTrace(e)
    }
    return null
}

/**
 * 读取指定路径的文件内容
 */
fun String.readFile(): String? {
    File(this).apply {
        if (exists()) {
            return readFile()
        }
    }
    return null
}


/**
 * 读取文件内容
 */
fun File.readFile(): String? {
    try {
        val stringBuilder = StringBuilder()
        FileInputStream(this).apply {
            val inputStreamReader = InputStreamReader(this, "GBK")
            BufferedReader(inputStreamReader).apply {
                var line: String?
                //分行读取
                do {
                    line = readLine()
                    if (line != null) {
                        stringBuilder.append(line)
                    } else {
                        break
                    }
                } while (true)
                close()
            }
            inputStreamReader.close()
        }
        return stringBuilder.toString()
    } catch (e: Exception) {
        ToolLog.printStackTrace(e)
    }
    return null
}

/**
 * 数据写入文件
 */
fun String.saveToFile(path: String,name: String): File? {
    try {
        path.mkdirs()
        val fos = FileOutputStream("$path${File.separator}$name")
        val writer = BufferedWriter(OutputStreamWriter(fos))
        writer.write(this)
        writer.close()
        val file = File(path)
        if (file.exists()) {
            return file
        }
    } catch (e: Exception) {
        ToolLog.printStackTrace(e)
    }
    return null
}

