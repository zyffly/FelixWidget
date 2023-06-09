package com.felix.tools

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.content.FileProvider
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader


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

/**
 * 获取APP缓存图片目录路径（app专用缓存目录）
 * @return 绝对路径
 */
fun Context.getImageDirPath(): String? {
    getDirPath()?.let {
        return "$it${File.separator}image"
    }
    return null
}

/**
 * 获取APP缓存视频目录路径（app专用缓存目录）
 * @return 绝对路径
 */
fun Context.getVideoDirPath(): String? {
    getDirPath()?.let {
        return "$it${File.separator}video"
    }
    return null
}

/**
 * 获取APP缓存音频目录路径（app专用缓存目录）
 * @return 绝对路径
 */
fun Context.getAudioDirPath(): String? {
    getDirPath()?.let {
        return "$it${File.separator}audio"
    }
    return null
}

/**
 * 创建图片文件
 */
fun Context.createImageFile(): File? {
    getImageDirPath()?.apply {
        return createFile(this, getTime().toString(), ".jpg")
    }
    return null
}

/**
 * 创建视频文件
 */
fun Context.createVideoFile(name: String): File? {
    getVideoDirPath()?.apply {
        return createFile(this, name, ".mp4")
    }
    return null
}

/**
 * 创建音频文件
 */
fun Context.createAudioFile(name: String): File? {
    getAudioDirPath()?.apply {
        return createFile(this, name, ".mp3")
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
 * 保存图片并返回路径
 */
fun Bitmap.saveToFile(context: Context): String? {
    context.getImageDirPath()?.let {
        val path = "$it${File.separator}${getTime()}.jpg"
        if (saveToFile(path)) return path
    }
    return null
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
        Log.w("readAssetFile", "IOException:${e.message}")
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
                var line: String? = null
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
        Log.e("File.readFile", "Exception:${e.message}")
    }
    return null
}
