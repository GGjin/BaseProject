package com.anpxd.ewalker.utils.upgrade

import android.app.Activity
import android.app.DownloadManager
import android.app.DownloadManager.Request
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import com.anpxd.baselibrary.utils.AppManager
import com.anpxd.ewalker.utils.toast
import com.anpxd.ewalker.utils.upgrade.receiver.DownloadReceiver
import com.anpxd.ewalker.utils.upgrade.receiver.NotificationClickReceiver
import com.gg.baseproject.BuildConfig
import com.gg.baseproject.bean.Version
import com.gg.baseproject.utils.net.EWalkerApi
import com.gg.net.ApiFactory
import com.gg.baseproject.Composers
import java.io.File


/**
 *  Creator : GG
 *  Time    : 2017/12/19
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
class AppUpgradeManager(var activity: Activity) : AppUpgrade {

    companion object {
        val WHAT_ID_INSTALL_APK = 1
        val WHAT_ID_DOWNLOAD_INFO = 2
    }

    private var isInit = false

    private lateinit var mAppContext: Context

    private val mDownloadPath: String by lazy { VersionUtils.getDownloadPath(activity.applicationContext) }

    private lateinit var mVersion: Version

    private lateinit var mDownLoadReceiver: DownloadReceiver

    private val mNotificationReceiver: NotificationClickReceiver by lazy { NotificationClickReceiver() }

    private lateinit var mDownloadManager: DownloadManager

    override fun register() {
        if (isInit)
            throw RuntimeException("AlreadyRegisterException")

        mAppContext = activity.applicationContext

        isInit = true

        mDownloadManager = mAppContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        mDownLoadReceiver = DownloadReceiver(mHandler = mDownloadHandler, mContext = mAppContext, mDownloadManager = mDownloadManager)

        mAppContext.registerReceiver(mDownLoadReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        mAppContext.registerReceiver(mNotificationReceiver, IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED))


    }

    override fun unRegister() {
        if (!isInit) {
            throw  RuntimeException("UnRegisterException")
        }


        mAppContext.unregisterReceiver(mDownLoadReceiver)
        mAppContext.unregisterReceiver(mNotificationReceiver)

        isInit = false
    }

    override fun defaultUpgrade() {
        AlertDialog.Builder(activity)
                .setTitle("版本升级")
                .setMessage("有新版本是否更新")
                .setCancelable(false)
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", { _, _ ->
                    downloadApk()
                })
                .create()
                .show()
    }

    override fun forcedUpgrade() {
        AlertDialog.Builder(activity)
                .setTitle("版本升级")
                .setMessage("有新版本需要强制更新，是否更新")
                .setCancelable(false)
                .setPositiveButton("确认", { _, _ ->
                    downloadApk()
                    AppManager.getInstance().finishAllActivity()
                })
                .create()
                .show()
    }


    override fun checkVersion() {
        ApiFactory.getApi(EWalkerApi::class.java)
                .getApk()
                .compose(Composers.handleError())
                .subscribe({
                    mVersion = it
                    if (it.apkVersion != VersionUtils.getVersionName(mAppContext)) {
                        when (it.apkForce) {
                            0 -> defaultUpgrade()

                            1 -> forcedUpgrade()
                        }
                    }
                }, {})
    }


    override fun downloadApk() {
        //先检查本地是否已经有需要升级版本的安装包，如有就不需要再下载
        val targetApkFile = File(mDownloadPath)
        if (targetApkFile.exists()) {
            val pm = mAppContext.packageManager
            val info = pm.getPackageArchiveInfo(mDownloadPath, PackageManager.GET_ACTIVITIES)
            if (info != null) {
                val versionCode = info.versionName
                //比较已下载到本地的apk安装包，与服务器上apk安装包的版本号是否一致
                if (mVersion.apkVersion == versionCode) {
                    //弹出框提示用户安装
                    mDownloadHandler.obtainMessage(WHAT_ID_INSTALL_APK, mDownloadPath).sendToTarget()
                    return
                }
            }
        }
        if (targetApkFile.exists()) {
            targetApkFile.delete()
        }

        val query = DownloadManager.Query()
        var downloadTaskId = VersionUtils.getDownloadTaskId()
        query.setFilterById(downloadTaskId)
        val cur = mDownloadManager.query(query)
        // 检查下载任务是否已经存在
        if (cur != null && cur.moveToFirst()) {
            val columnIndex = cur.getColumnIndex(DownloadManager.COLUMN_STATUS)
            val status = cur.getInt(columnIndex)
            if (DownloadManager.STATUS_PENDING == status || DownloadManager.STATUS_RUNNING == status || DownloadManager.STATUS_PAUSED == status) {
                cur.close()
                return
            }
        }

        cur?.close()
        val task = Request(Uri.parse(mVersion.apkUrl))
//        val task = Request(Uri.parse("http://a5.pc6.com/cx3/weixin.pc6.apk"))
        //定制Notification的样式
        val title = "最新版本:" + mVersion.apkVersion
        task.setTitle(title)
        task.setDescription(mVersion.apkRemark)
        //如果我们希望下载的文件可以被系统的Downloads应用扫描到并管理，我们需要调用Request对象的setVisibleInDownloadsUi方法，传递参数true
        task.setVisibleInDownloadsUi(true)
        //设置现在的文件可以被MediaScanner扫描到。
        task.allowScanningByMediaScanner()
        //设置是否允许手机在漫游状态下下载
        task.setAllowedOverRoaming(false)
        //限定在WiFi、手机流量下进行下载
        task.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        task.setMimeType("application/vnd.android.package-archive")
        //在通知栏通知下载中和下载完成下载完成后该Notification才会被显示3.0(11)以后才有该方法。
        //在下载过程中通知栏会一直显示该下载的Notification，在下载完成后该Notification会继续显示，直到用户点击该Notification或者消除该Notification
        task.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//        task.setNotificationVisibility(Request.VISIBILITY_HIDDEN)

        // 可能无法创建Download文件夹，如无sdcard情况，系统会默认将路径设置为/data/data/com.android.providers.downloads/cache/xxx.apk
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val apkName = VersionUtils.downloadTempName(mAppContext.packageName)
            task.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, apkName)
        }
        downloadTaskId = mDownloadManager.enqueue(task)
        VersionUtils.saveDownloadTaskId(downloadTaskId)
        mAppContext.toast("正在后台下载更新")
    }

    override fun installAPKFile(path: String) {
        if (TextUtils.isEmpty(path)) {
            mAppContext.toast("App安装文件不存在!")
            return
        }

        val apkFile = File(Uri.parse(path).path)
        if (!apkFile.exists()) {
            mAppContext.toast("App安装文件不存在!")
            return
        }

        val installIntent = Intent()
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        installIntent.action = Intent.ACTION_VIEW

        val apkFileUri: Uri
        // 在24及其以上版本，解决崩溃异常：
        // android.os.FileUriExposedException: file:///storage/emulated/0/xxx exposed beyond app through Intent.getData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            apkFileUri = FileProvider.getUriForFile(mAppContext, BuildConfig.APPLICATION_ID + ".provider", apkFile)
        } else {
            apkFileUri = Uri.fromFile(apkFile)
        }
        installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        installIntent.setDataAndType(apkFileUri, "application/vnd.android.package-archive")
        try {
            mAppContext.startActivity(installIntent)
        } catch (e: ActivityNotFoundException) {

        }

    }

    private val mDownloadHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                WHAT_ID_INSTALL_APK -> installAPKFile(msg.obj as String)

//                WHAT_ID_DOWNLOAD_INFO -> {
//                    val progress = msg.arg1 * 100 / msg.arg2
//                    val title = "最新版本:" /*+ mVersion.apkVersion*/
//                    var versionDesc = ""
////                    if (!TextUtils.isEmpty(latestVersion.getVersionDesc())) {
////                        versionDesc = latestVersion.getVersionDesc().replace("\n", "")
////                    }
//                    DownloadNotificationHelper.sendDefaultNotice(mAppContext, title, versionDesc, progress)
//                }
            }
        }
    }

}