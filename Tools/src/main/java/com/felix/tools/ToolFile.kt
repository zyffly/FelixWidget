package com.felix.tools

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
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
fun String.saveToFile(path: String, name: String): File? {
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

/**
 * 通过Uri获取file
 */
fun Uri.getFileByUri(context: Context): File? {
    try {
        getAbsolutePath(context)?.let {
            return File(it)
        }
    } catch (e: Exception) {
        ToolLog.printStackTrace(e)
    }
    return null
}

private fun Uri.getAbsolutePath(context: Context): String? {
    if (DocumentsContract.isDocumentUri(context, this)) {
        if (this.isExternalStorageDocument()) {
            val docId = DocumentsContract.getDocumentId(this)
            val split = docId.split(":").toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }
        } else if (this.isDownloadsDocument()) {
            val id = DocumentsContract.getDocumentId(this)
            val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"), id.toLong()
            )
            return getDataColumn(context, contentUri, null, null)
        } else if (this.isMediaDocument()) {
            val docId = DocumentsContract.getDocumentId(this)
            val split = docId.split(":").toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            when (type) {
                "image" -> {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }

                "video" -> {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }

                "audio" -> {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
            }
            val selection = MediaStore.Images.Media._ID + "=?"
            val selectionArgs = arrayOf(split[1])
            return getDataColumn(context, contentUri, selection, selectionArgs)
        }
    } // MediaStore (and general)
    else if ("content".equals(this.scheme, ignoreCase = true)) {
        // Return the remote address
        return if (this.isGoogleAppPhotos()) {
            this.lastPathSegment
        } else getDataColumn(context, this, null, null)
    } else if ("file".equals(this.scheme, ignoreCase = true)) {
        return this.path
    }
    return null
}

private fun getDataColumn(
    context: Context,
    uri: Uri?,
    selection: String?,
    selectionArgs: Array<String>?
): String? {
    var cursor: Cursor? = null
    val column = MediaStore.Images.Media.DATA
    val projection = arrayOf(column)
    try {
        cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val index = cursor.getColumnIndexOrThrow(column)
            return cursor.getString(index)
        }
    } finally {
        cursor?.close()
    }
    return null
}

private fun Uri.isExternalStorageDocument(): Boolean {
    return "com.android.externalstorage.documents" == this.authority
}

private fun Uri.isDownloadsDocument(): Boolean {
    return "com.android.providers.downloads.documents" == this.authority
}

private fun Uri.isMediaDocument(): Boolean {
    return "com.android.providers.media.documents" == this.authority
}

private fun Uri.isGoogleAppPhotos(): Boolean {
    return "com.google.android.apps.photos.content" == this.authority
}

