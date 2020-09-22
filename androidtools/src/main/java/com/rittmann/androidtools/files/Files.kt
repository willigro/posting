package com.rittmann.androidtools.files

import android.os.Environment
import android.util.Log
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


object Files {

    private const val sBufferSize = 524288
    val ROOT_PATH: String = Environment.getExternalStorageDirectory().absolutePath

    fun write(value: String, path: String, name: String) {
        try {
            val root = File(path)
            if (!root.exists()) {
                root.mkdirs()
            }
            val gpxfile = File(root, name)
            val writer = FileWriter(gpxfile)
            writer.append(value)
            writer.flush()
            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Write file from input stream.
     *
     * Code from com.blankj.utilcode.util
     *
     * @param file     The file.
     * @param inputStream       The input stream.
     * @param append   True to append, false otherwise.
     * @param listener The progress update listener.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromIS(
        file: File,
        inputStream: InputStream?,
        append: Boolean,
        listener: OnProgressUpdateListener?
    ): Boolean {
        if (inputStream == null || !createOrExistsFile(file)) {
            Log.e("FileIOUtils", "create file <$file> failed.")
            return false
        }
        var os: OutputStream? = null
        return try {
            os = BufferedOutputStream(
                FileOutputStream(file, append),
                sBufferSize
            )
            if (listener == null) {
                val data =
                    ByteArray(sBufferSize)
                var len: Int
                while (inputStream.read(data).also { len = it } != -1) {
                    os.write(data, 0, len)
                }
            } else {
                val totalSize = inputStream.available().toDouble()
                var curSize = 0
                listener.onProgressUpdate(0.0)
                val data =
                    ByteArray(sBufferSize)
                var len: Int
                while (inputStream.read(data).also { len = it } != -1) {
                    os.write(data, 0, len)
                    curSize += len
                    listener.onProgressUpdate(curSize / totalSize)
                }
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } finally {
            try {
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                os?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Create a file if it doesn't exist, otherwise do nothing.
     *
     * Code from com.blankj.utilcode.util
     *
     * @param file The file.
     * @return `true`: exists or creates successfully<br></br>`false`: otherwise
     */
    fun createOrExistsFile(file: File?): Boolean {
        if (file == null) return false
        if (file.exists()) return file.isFile
        return if (!createOrExistsDir(file.parentFile)) false else try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Create a directory if it doesn't exist, otherwise do nothing.
     *
     * Code from com.blankj.utilcode.util
     *
     * @param file The file.
     * @return `true`: exists or creates successfully<br></br>`false`: otherwise
     */
    fun createOrExistsDir(file: File?): Boolean {
        return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
    }

    interface OnProgressUpdateListener {
        fun onProgressUpdate(progress: Double)
    }
}