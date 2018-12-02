package com.gg.baseproject.upgrade.receiver

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Handler
import com.gg.baseproject.upgrade.AppUpgradeManager
import com.gg.baseproject.upgrade.VersionUtils

/**
 *  Creator : GG
 *  Time    : 2017/12/19
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
class DownloadReceiver(private var mContext: Context, private val mDownloadManager: DownloadManager, private val mHandler: Handler) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val completeId = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)
        val downloadId = VersionUtils.getDownloadTaskId()
        if (completeId != downloadId)
            return
        val query = DownloadManager.Query()
        query.setFilterById(downloadId)
        val cursor = mDownloadManager.query(query)
        if (cursor == null || !cursor.moveToFirst()) {
            return
        }

        val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)

        if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
            updateProgress()
            val uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
            mHandler.obtainMessage(AppUpgradeManager.WHAT_ID_INSTALL_APK, uriString).sendToTarget()
        } else {
            mContext.toast("下载App最新版本失败!")
        }
        VersionUtils.removeDownloadTaskId()
        cursor.close()

    }

    private fun updateProgress() {
        val downloadId = VersionUtils.getDownloadTaskId()
        val byteAndState = getBytesAndStatus(downloadId)
        mHandler.sendMessage(mHandler.obtainMessage(AppUpgradeManager.WHAT_ID_DOWNLOAD_INFO, byteAndState[0], byteAndState[1]))
    }

    /**
     * 通过query查询下载状态，包括已下载数据大小，总大小，下载状态
     *
     * @param downloadId 下载任务id
     */
    private fun getBytesAndStatus(downloadId: Long): IntArray {
        val bytesAndStatus = intArrayOf(-1, -1, 0)
        val query = DownloadManager.Query().setFilterById(downloadId)
        var cursor: Cursor? = null
        try {
            cursor = mDownloadManager.query(query)
            if (cursor != null && cursor.moveToFirst()) {
                //已经下载文件大小
                bytesAndStatus[0] = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                //下载文件的总大小
                bytesAndStatus[1] = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
            }
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
        return bytesAndStatus
    }
}