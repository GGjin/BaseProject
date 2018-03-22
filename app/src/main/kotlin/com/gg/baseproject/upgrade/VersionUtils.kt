package com.gg.baseproject.upgrade

import android.content.Context
import android.os.Environment
import com.gg.baseproject.App
import com.gg.baseproject.R
import com.gg.utils.DelegatesExt
import java.io.File

/**
 *  Creator : GG
 *  Time    : 2017/12/19
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
object VersionUtils {

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    fun getVersionName(context: Context): String {
        return try {
            val manager = context.packageManager
            val info = manager.getPackageInfo(context.packageName, 0)
            info.versionName
        } catch (e: Exception) {
            e.printStackTrace()
            context.getString(R.string.can_not_find_version_name)
        }

    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    fun getVersionCode(context: Context): Int {
        return try {
            val manager = context.packageManager
            val info = manager.getPackageInfo(context.packageName, 0)
            info.versionCode
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }

    }



    const val DOWNLOAD_TASK_ID = "download_task_id"

    var mTaskId: Long by DelegatesExt.preference(App.instance, DOWNLOAD_TASK_ID, 0)

    fun saveDownloadTaskId(id: Long) {
        mTaskId = id
    }

    fun getDownloadTaskId(): Long = mTaskId

    fun removeDownloadTaskId() {
        mTaskId = 0
    }

    fun downloadTempName(packageName: String): String {
        val tempFileName = "_temp@" + packageName
        return tempFileName + ".apk"
    }


    fun getDownloadPath(context: Context):String{
        val apkName = downloadTempName(context.packageName)
        var dirPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS).absolutePath
        dirPath = if (dirPath.endsWith(File.separator)) dirPath else dirPath + File.separator
        return dirPath + apkName
    }
}