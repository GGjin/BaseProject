package com.gg.baseproject.upgrade


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.SystemClock
import android.support.v4.app.NotificationCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gg.baseproject.R
import java.text.SimpleDateFormat
import java.util.*


/**
 *  Creator : GG
 *  Time    : 2017/12/19
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
object DownloadNotificationHelper {
    val NOTICE_ID_KEY = "NOTICE_ID"
    private val ACTION_CLICK_NOTIFICATION = "action.click.notification"
    private val NOTICE_DOWNLOAD_ID = 999

    private val time: String
        get() {
            val format = SimpleDateFormat("HH:mm", Locale.SIMPLIFIED_CHINESE)
            return format.format(Date())
        }

    fun sendDefaultNotice(context: Context, title: String, desc: String, progress: Int) {
        val builder = NotificationCompat.Builder(context)
        builder.setOngoing(false)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)

        var pendingIntent: PendingIntent? = null
        if (progress >= 100) {
            val requestCode = SystemClock.uptimeMillis().toInt()
            val intent = Intent(ACTION_CLICK_NOTIFICATION)
            intent.putExtra(NOTICE_ID_KEY, NOTICE_DOWNLOAD_ID)
            pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            builder.setContentText(context.getString(R.string.downloadFinish))
                    .setProgress(0, 0, false)
        } else {
            builder.setContentText(desc)
                    .setProgress(100, progress, false)
        }

        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent)
        }

        val notification = builder.build()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTICE_DOWNLOAD_ID, notification)
    }

    fun cancelNotification(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(NOTICE_DOWNLOAD_ID)
    }

    fun isDarkNotificationTheme(context: Context): Boolean {
        return !isSimilarColor(Color.BLACK, getNotificationColor(context))
    }

    /**
     * 获取通知栏颜色
     */
    fun getNotificationColor(context: Context): Int {
        val builder = NotificationCompat.Builder(context)
        val notification = builder.build()
        val layoutId = notification.contentView.layoutId
        val viewGroup = LayoutInflater.from(context).inflate(layoutId, null, false) as ViewGroup
        return if (viewGroup.findViewById<View>(android.R.id.title) != null) {
            (viewGroup.findViewById<View>(android.R.id.title) as TextView).currentTextColor
        } else findColor(viewGroup)
    }

    private fun findColor(viewGroupSource: ViewGroup): Int {
        var color = Color.TRANSPARENT
        val viewGroups :LinkedList<ViewGroup> = LinkedList()
        viewGroups.add(viewGroupSource)
        while (viewGroups.size > 0) {
            val viewGroup1 = viewGroups.first
            for (i in 0 until viewGroup1.childCount) {
                if (viewGroup1.getChildAt(i) is ViewGroup) {
                    viewGroups.add(viewGroup1.getChildAt(i) as ViewGroup)
                } else if (viewGroup1.getChildAt(i) is TextView) {
                    if ((viewGroup1.getChildAt(i) as TextView).currentTextColor != -1) {
                        color = (viewGroup1.getChildAt(i) as TextView).currentTextColor
                    }
                }
            }
            viewGroups.remove(viewGroup1)
        }
        return color
    }

    private fun isSimilarColor(baseColor: Int, color: Int): Boolean {
        val simpleBaseColor = baseColor or -0x1000000
        val simpleColor = color or -0x1000000
        val baseRed = Color.red(simpleBaseColor) - Color.red(simpleColor)
        val baseGreen = Color.green(simpleBaseColor) - Color.green(simpleColor)
        val baseBlue = Color.blue(simpleBaseColor) - Color.blue(simpleColor)
        val value = Math.sqrt((baseRed * baseRed + baseGreen * baseGreen + baseBlue * baseBlue).toDouble())
        return value < 180.0
    }
}