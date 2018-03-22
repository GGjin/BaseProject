package com.gg.baseproject.upgrade.receiver

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.gg.baseproject.upgrade.VersionUtils

/**
 *  Creator : GG
 *  Time    : 2017/12/19
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
class NotificationClickReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val completeIds: LongArray = intent?.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS)!!
        val mDownloadId = VersionUtils.getDownloadTaskId()
        if (completeIds.isEmpty() || completeIds.size < 0) {
            openDownloadPage(context)
            return
        }

        completeIds.forEach {
            if (mDownloadId == it) {
                openDownloadPage(context)
                return
            }
        }
    }

    private fun openDownloadPage(context: Context?) {
        context?.startActivity(Intent(DownloadManager.ACTION_VIEW_DOWNLOADS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }
}