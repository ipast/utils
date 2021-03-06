package com.ipast.utils.ecrash

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.util.Log
import com.ipast.utils.file.FileUtil
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author gang.cheng
 * @description :异常捕获机制
 * @date :2021/4/14
 */
open class CrashHandler : Thread.UncaughtExceptionHandler {
    companion object {
        protected val TAG = CrashHandler::class.simpleName
        protected const val CRASH_DIR = "crash"//应用崩溃日志存放的目录

        // 崩溃日志存储位置及文件
        protected const val FILE_NAME_CRASH = "crash_"
        protected const val FILE_NAME_SUFFIX = ".log"

        @JvmStatic
        val instance: CrashHandler by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CrashHandler()
        }

    }

    private var isDebug: Boolean = true//是否为debug模式,debug模式下将日志保存到SD卡中
    private var mDefaultCrashHandler: Thread.UncaughtExceptionHandler? = null
    private var mCtx: Context? = null
    private var filepath: String? = null
    private var isThrowException: Boolean = false//是否将异常抛给系统默认处理类处理

    fun init(context: Context, isDebug: Boolean) {
        val path = context.getExternalFilesDir(CRASH_DIR)!!.absolutePath
        init(context, path, isDebug)
    }

    fun init(context: Context, filepath: String, isDebug: Boolean) {
        this.mCtx = context.applicationContext
        this.filepath = filepath
        this.isDebug = isDebug
        this.mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    fun setThrowException(isThrowException: Boolean) {
        this.isThrowException = isThrowException
    }

    /**
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统将会自动调用#uncaughtException方法
     *
     * @param t
     * @param e
     */
    override fun uncaughtException(t: Thread, e: Throwable) {
        if (!handleException(e) && mDefaultCrashHandler != null) {
            mDefaultCrashHandler!!.uncaughtException(t, e)
        } else {
            Thread.sleep(3000)
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 异常处理
     *
     * @param e
     * @return
     */
    fun handleException(e: Throwable): Boolean {
        if (isDebug) {
            Log.e(TAG, Log.getStackTraceString(e))
            saveException(e)
        } else {
            uploadException2Server(e)
        }
        return !isThrowException
    }

    /**
     * 保存异常信息到SD卡
     * 默认保存路径：mnt/sdcard/Android/data/{packageName}/files/crash/
     *
     * @param e
     */
    @SuppressLint("SimpleDateFormat")
    protected fun saveException(e: Throwable) {
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            Log.w(TAG, "sdcard unmounted")
            return
        }
        // val dir = mCtx!!.getExternalFilesDir(CRASH_DIR)!!.absolutePath
        val current = System.currentTimeMillis()
        val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(current))
        FileUtil.createFolder(filepath!!)
        val file = File(filepath!! + "/" + FILE_NAME_CRASH + time + FILE_NAME_SUFFIX)
        val pw = PrintWriter(BufferedWriter(FileWriter(file)))
        pw.println(time)
        collectDeviceInfo(pw)
        pw.println()
        //ex.getCause().printStackTrace(pw);
        e.printStackTrace(pw)
        /*  var cause = e.cause
          while (cause != null) {
              cause.printStackTrace(pw)
              cause = cause.cause
          }*/
        pw.close()
    }

    /**
     * 收集手机相关信息
     *
     * @param pw
     * @throws PackageManager.NameNotFoundException
     */
    protected fun collectDeviceInfo(pw: PrintWriter) {
        val pm = mCtx!!.packageManager
        val pi = pm.getPackageInfo(mCtx!!.packageName, PackageManager.GET_ACTIVITIES)
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);

        //android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);

        //手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);

        //手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);

        //cpu架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
    }

    /**
     * 上传异常信息到服务器
     *
     * @param e
     */
    protected fun uploadException2Server(e: Throwable) {

    }
}