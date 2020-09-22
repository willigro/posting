package com.rittmann.androidtools.troubles

import android.annotation.SuppressLint
import android.os.Build
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * <pre>
 * author: Blankj
 * blog  : http://blankj.com
 * time  : 2016/09/27
 * desc  : utils about crash
</pre> *
 */
class CrashUtils private constructor() {
    ///////////////////////////////////////////////////////////////////////////
    // interface
    ///////////////////////////////////////////////////////////////////////////
    interface OnCrashListener {
        fun onCrash(crashInfo: String?, e: Throwable?)
    }

    companion object {
        private val FILE_SEP = System.getProperty("file.separator")
        private val DEFAULT_UNCAUGHT_EXCEPTION_HANDLER =
            Thread.getDefaultUncaughtExceptionHandler()

        /**
         * Initialization.
         */
        @SuppressLint("MissingPermission")
        fun init() {
            init("")
        }

        /**
         * Initialization
         *
         * @param crashDir The directory of saving crash information.
         */
        fun init(crashDir: File) {
            init(crashDir.absolutePath, null)
        }

        /**
         * Initialization
         *
         * @param onCrashListener The crash listener.
         */
        fun init(onCrashListener: OnCrashListener?) {
            init("", onCrashListener)
        }

        /**
         * Initialization
         *
         * @param crashDir        The directory of saving crash information.
         * @param onCrashListener The crash listener.
         */
        fun init(crashDir: File, onCrashListener: OnCrashListener?) {
            init(crashDir.absolutePath, onCrashListener)
        }
        /**
         * Initialization
         *
         * @param crashDirPath    The directory's path of saving crash information.
         * @param onCrashListener The crash listener.
         */
        /**
         * Initialization
         *
         * @param crashDirPath The directory's path of saving crash information.
         */
        @JvmOverloads
        fun init(
            crashDirPath: String?,
            onCrashListener: OnCrashListener? = null
        ) {
            val dirPath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath + "/Teste/"
            Thread.setDefaultUncaughtExceptionHandler(
                getUncaughtExceptionHandler(
                    dirPath,
                    onCrashListener
                )
            )
        }

        @SuppressLint("SimpleDateFormat")
        private fun getUncaughtExceptionHandler(
            dirPath: String,
            onCrashListener: OnCrashListener?
        ): Thread.UncaughtExceptionHandler {
            return Thread.UncaughtExceptionHandler { t: Thread?, e: Throwable? ->
                val time =
                    SimpleDateFormat("yyyy_MM_dd-HH_mm_ss").format(Date())
                val sb = StringBuilder()
                val head = """
                    ************* Log Head ****************
                    Time Of Crash      : $time
                    Device Manufacturer: ${Build.MANUFACTURER}
                    Device Model       : ${Build.MODEL}
                    Android Version    : ${Build.VERSION.RELEASE}
                    Android SDK        : ${Build.VERSION.SDK_INT}
                    ************* Log Head ****************


                    """.trimIndent()
                sb.append(head).append(ThrowableUtils.getFullStackTrace(e))
                val crashInfo = sb.toString()
                Log.e("Testando", crashInfo)
                write(crashInfo, dirPath, "$time.txt")
                onCrashListener?.onCrash(crashInfo, e)
                DEFAULT_UNCAUGHT_EXCEPTION_HANDLER?.uncaughtException(t, e)
            }
        }

        private fun write(value: String, path: String, name: String) {
            try {
                val root = File(path)
                if (root.exists().not()) {
                    Log.e("testando", "$path$name${root.mkdirs()}${root.mkdir()}")
                }
                val file = File(root.absolutePath, name).apply {
                    createNewFile()
                }
                val writer = FileWriter(file)
                writer.append(value)
                writer.flush()
                writer.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }
}