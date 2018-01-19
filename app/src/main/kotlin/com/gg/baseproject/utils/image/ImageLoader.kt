package com.anpxd.ewalker.utils.image

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.anpxd.imageloader.config.GlideApp
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.gg.baseproject.utils.image.config.CircleBitmapTarget

/**
 *  Creator : GG
 *  Time    : 2017/10/16
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
object ImageLoader {

    fun loadImage(view: ImageView, url: String) {
        loadImage(view, url, null)
    }

    val TAG = "ImageLoader"

    /**
     * 异步加载图片
     *
     * @param url 图片路径
     * @param placeholderRes 占位图
     */
    fun loadImage(imageView: ImageView, url: String?, placeholderRes: Int?) {
        loadImage(imageView, url, placeholderRes, null)
    }

    /**
     * 异步加载图片, 带回调
     *
     * @param url 图片路径
     * @param placeholderRes 占位图
     * @param callback 图片加载完成回调
     */
    fun loadImage(imageView: ImageView, url: String?, placeholderRes: Int?, callback: Callback? = null) {
        imageView.setImageResource(placeholderRes ?: 0)

        val context = imageView.context
        if (!isValid(context, url))
            return

        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .placeholder(placeholderRes ?: 0)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .into(object : BitmapImageViewTarget(imageView) {
                    /**
                     * [onLoadFailed run some times](https://github.com/bumptech/glide/issues/1764)
                     */
                    internal var hasFailed = false

                    override fun onLoadStarted(placeholder: Drawable?) {
                        super.onLoadStarted(placeholder)
                        callback?.onLoadingStarted(url, imageView)
                    }

                    override fun onResourceReady(bitmap: Bitmap?, transition: Transition<in Bitmap>?) {
                        super.onResourceReady(bitmap, transition)
                        callback?.onLoadingComplete(url, imageView, bitmap)
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        if (hasFailed)
                            return
                        hasFailed = true

                        callback?.onLoadingFailed(url, imageView)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        super.onLoadCleared(placeholder)
                        callback?.onLoadingCancelled(url, imageView)
                    }
                })
    }

    /**
     * 异步加载圆角图片, 使用 Glide 内置的圆角裁剪 —— bitmapTransform.

     * 注意: bitmapTransform 与 外在的裁剪不兼容。即: [issues 54](https://github.com/wasabeef/glide-transformations/issues/54)

     * 1. 此时的 imageView 不能是自定义的圆角View.
     * 2. scaleType 也不能设。

     * 否则重复裁剪, 会有 bug.

     * @param url 图片路径
     * @param placeholderRes 占位图
     * @param radiusPixels 圆角像素值
     */
    fun loadRoundImage(imageView: ImageView, url: String?, placeholderRes: Int?, radiusPixels: Int) {
        imageView.setImageResource(placeholderRes ?: 0)

        val context = imageView.context
        if (!isValid(context, url))
            return

        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .placeholder(placeholderRes ?: 0)
                .error(placeholderRes ?: 0)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(object : CircleBitmapTarget(imageView){})
    }

    /**
     * 异步加载图片, 带回调(不涉及 ImageView)
     * @param url 图片路径
     * *
     * @param callback 图片加载完成回调
     */
    fun loadImage(context: Context, url: String, callback: Callback?) {
        if (!isValid(context, url))
            return

        val imageView: ImageView? = null

        GlideApp.with(context)
                .asBitmap()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .encodeFormat(Bitmap.CompressFormat.PNG)
                .into(object : SimpleTarget<Bitmap>() {
                    internal var hasFailed = false

                    override fun onLoadStarted(placeholder: Drawable?) {
                        super.onLoadStarted(placeholder)
                        callback?.onLoadingStarted(url, imageView)
                    }

                    override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>) {
                        callback?.onLoadingComplete(url, imageView, bitmap)
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        if (hasFailed)
                            return
                        hasFailed = true

                        callback?.onLoadingFailed(url, imageView)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        super.onLoadCleared(placeholder)
                        callback?.onLoadingCancelled(url, imageView)
                    }
                })
    }

    /**
     * reason: [RequestManagerRetriever.assertNotDestroyed]
     * [Issue #138: Getting a crash in Glide 3.3 library](https://github.com/bumptech/glide/issues/138) */
    private fun isValid(context: Context, url: String?): Boolean {
        if (context is Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && context.isDestroyed)
                return false
        }

        if (TextUtils.isEmpty(url))
            return false

        return true
    }

    class Callback {
        /**
         * 下载开始
         * @param url
         * *
         * @param imageView
         */
        fun onLoadingStarted(url: String?, imageView: View?) {
        }

        /**
         * 下载失败
         * @param url
         * *
         * @param imageView
         */
        fun onLoadingFailed(url: String?, imageView: View?) {
        }

        /**
         * 下载成功
         * @param url
         * *
         * @param imageView
         * *
         * @param bitmap
         */
        fun onLoadingComplete(url: String?, imageView: View?, bitmap: Bitmap?) {
        }

        /**
         * 下载取消
         * @param url
         * *
         * @param imageView
         */
        fun onLoadingCancelled(url: String?, imageView: View?) {
        }
    }

}