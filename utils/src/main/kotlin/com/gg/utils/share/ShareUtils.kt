package com.gg.utils.share

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.text.TextUtils
import android.util.Log

/**
 * Creator : GG
 * Date    : 2018/5/24
 * Mail    : gg.jin.yu@gmai.com
 * Explain :
 */
class ShareUtils private constructor(builder: Builder) {

    /**
     * Current activity
     */
    private val activity: Activity?

    /**
     * Share content type
     */
    @ShareContentType
    private val contentType: String

    /**
     * Share title
     */
    private var title: String? = null

    /**
     * Share file Uri
     */
    private val shareFileUriList: ArrayList<Uri>

    /**
     * Share content text
     */
    private val contentText: String?

    /**
     * Share to special component PackageName
     */
    private val componentPackageName: String?

    /**
     * Share to special component ClassName
     */
    private val componentClassName: String?

    /**
     * Share complete onActivityResult requestCode
     */
    private val requestCode: Int

    /**
     * Forced Use System Chooser
     */
    private val forcedUseSystemChooser: Boolean


    private val shareDescription: String?

    init {
        this.activity = builder.activity
        this.contentType = builder.contentType
        this.title = builder.title
        this.shareFileUriList = builder.shareFileUriList
        this.contentText = builder.textContent
        this.componentPackageName = builder.componentPackageName
        this.componentClassName = builder.componentClassName
        this.requestCode = builder.requestCode
        this.forcedUseSystemChooser = builder.forcedUseSystemChooser
        this.shareDescription = builder.shareDescription
    }

    /**
     * shareBySystem
     */
    fun shareBySystem() {
        if (checkShareParam()) {
            var chooserIntent = createShareIntent()

            if (chooserIntent == null) {
                Log.e(TAG, "shareBySystem cancel.")
                return
            }

            if (title == null) {
                title = ""
            }
            val intent = Intent.createChooser(chooserIntent.removeAt(0), "选择要分享到的平台")
            if (intent != null) {
                intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, chooserIntent.toTypedArray<Parcelable>())
                activity?.startActivity(intent)
            }
//            if (forcedUseSystemChooser) {
////                val chooserIntent = Intent.createChooser(shareIntent.removeAt(0), title)
////                if (chooserIntent != null) {
////                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, shareIntent.toTypedArray<Parcelable>())
//                activity?.startActivity(shareIntent)
////                }
//                shareIntent = Intent.createChooser(shareIntent, title)
//
//
//                if (shareIntent!!.resolveActivity(activity!!.packageManager) != null) {
//                    try {
//                        if (requestCode != -1) {
//                            activity.startActivityForResult(shareIntent, requestCode)
//                        } else {
//                            activity.startActivity(shareIntent)
//                        }
//                    } catch (e: Exception) {
//                        Log.e(TAG, Log.getStackTraceString(e))
//                    }
//
//                }
//            }
        }
    }

    private fun createShareIntent(): ArrayList<Intent>? {
        var shareIntent: Intent? = Intent()
        shareIntent!!.action = Intent.ACTION_SEND_MULTIPLE
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        shareIntent.addCategory("android.intent.category.DEFAULT")

        if (!TextUtils.isEmpty(this.componentPackageName) && !TextUtils.isEmpty(componentClassName)) {
            val comp = ComponentName(componentPackageName!!, componentClassName!!)
            shareIntent.component = comp
        }

        val targetedShareIntents = ArrayList<Intent>()

        when (contentType) {
            ShareContentType.TEXT -> {
                shareIntent.putExtra(Intent.EXTRA_TEXT, contentText)
                shareIntent.type = "text/plain"
            }
            ShareContentType.IMAGE, ShareContentType.AUDIO, ShareContentType.VIDEO, ShareContentType.FILE -> {
                shareIntent.action = Intent.ACTION_SEND_MULTIPLE
                shareIntent.addCategory("android.intent.category.DEFAULT")
                shareIntent.type = contentType
                shareIntent.putExtra(Intent.EXTRA_STREAM, shareFileUriList)
                if (!shareDescription.isNullOrBlank())
                    shareIntent.putExtra("Kdescription", shareDescription)
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                Log.d(TAG, "Share uri: " + shareFileUriList!!.toString())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    shareIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    val resInfoList = activity!!.packageManager.queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY)
                    for (resolveInfo in resInfoList) {
                        val packageName = resolveInfo.activityInfo.packageName
                        shareFileUriList.forEach {
                            activity.grantUriPermission(packageName, it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                    }
                }
                targetedShareIntents.add(shareIntent)
            }
            else -> {
                Log.e(TAG, "$contentType is not support share type.")
                shareIntent = null
            }
        }

        return targetedShareIntents
    }


    private fun checkShareParam(): Boolean {
        if (this.activity == null) {
            Log.e(TAG, "activity is null.")
            return false
        }

        if (TextUtils.isEmpty(this.contentType)) {
            Log.e(TAG, "Share content type is empty.")
            return false
        }

        if (ShareContentType.TEXT == contentType) {
            if (TextUtils.isEmpty(contentText)) {
                Log.e(TAG, "Share text context is empty.")
                return false
            }
        } else {
            if (this.shareFileUriList.isEmpty()) {
                Log.e(TAG, "Share file path is null.")
                return false
            }
        }

        return true
    }

    class Builder(val activity: Activity) {
        @ShareContentType
        var contentType = ShareContentType.FILE
        var title: String? = null
        var componentPackageName: String? = null
        var componentClassName: String? = null
        val shareFileUriList: ArrayList<Uri> by lazy { arrayListOf<Uri>() }
        var textContent: String? = null
        var requestCode = -1
        var forcedUseSystemChooser = true
        var shareDescription = ""

        /**
         * Set Content Type
         * @param contentType [ShareContentType]
         * @return Builder
         */
        fun setContentType(@ShareContentType contentType: String): Builder {
            this.contentType = contentType
            return this
        }

        /**
         * Set Title
         * @param title title
         * @return Builder
         */
        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        /**
         * Set share file path
         * @param shareFileUri shareFileUri
         * @return Builder
         */
        fun setShareFileUri(shareFileUri: Uri): Builder {
            this.shareFileUriList.add(shareFileUri)
            return this
        }

        fun setShareFileUri(uriList: ArrayList<Uri>): Builder {
            this.shareFileUriList.addAll(uriList)
            return this
        }

        /**
         * Set text content
         * @param textContent  textContent
         * @return Builder
         */
        fun setTextContent(textContent: String): Builder {
            this.textContent = textContent
            return this
        }

        /**
         * Set Share To Component
         * @param componentPackageName componentPackageName
         * @param componentClassName componentPackageName
         * @return Builder
         */
        fun setShareToComponent(componentPackageName: String, componentClassName: String): Builder {
            this.componentPackageName = componentPackageName
            this.componentClassName = componentClassName
            return this
        }

        /**
         * Set onActivityResult requestCode, default value is -1
         * @param requestCode requestCode
         * @return Builder
         */
        fun setOnActivityResult(requestCode: Int): Builder {
            this.requestCode = requestCode
            return this
        }

        /**
         * Forced Use System Chooser To Share
         * @param enable default is true
         * @return Builder
         */
        fun forcedUseSystemChooser(enable: Boolean): Builder {
            this.forcedUseSystemChooser = enable
            return this
        }

        fun setDescription(description: String): Builder {
            this.shareDescription = description
            return this
        }

        /**
         * build
         * @return ShareUtils
         */
        fun build(): ShareUtils {
            return ShareUtils(this)
        }

    }

    companion object {

        private val TAG = "ShareUtils"
    }
}
