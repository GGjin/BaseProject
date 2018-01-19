package com.anpxd.imageloader.config

import com.bumptech.glide.annotation.GlideExtension
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.request.RequestOptions


/**
 *  Creator : GG
 *  Time    : 2017/10/17
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
@GlideExtension
object MyAppExtension {

    private val MINI_THUMB_SIZE = 100

    @JvmStatic
    @GlideOption
    fun miniThumb(options: RequestOptions) {
        options.fitCenter().override(MINI_THUMB_SIZE)
    }

    @JvmStatic
    @GlideOption
    fun miniThumb(options: RequestOptions, size: Int) {
        options.fitCenter().override(size)
    }

    @JvmStatic
    @GlideOption
    fun placeholder(options: RequestOptions) {
//        options.placeholder(R.drawable.ic_placeholder)
    }
}