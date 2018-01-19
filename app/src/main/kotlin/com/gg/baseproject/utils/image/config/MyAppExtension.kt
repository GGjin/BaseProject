package com.anpxd.imageloader.config

import android.graphics.drawable.BitmapDrawable
import com.bumptech.glide.annotation.GlideExtension
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.decodeTypeOf
import com.gg.baseproject.R


/**
 *  Creator : GG
 *  Time    : 2017/10/17
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
@GlideExtension
object MyAppExtension {
    private val DECODE_TYPE_GIF = decodeTypeOf(GifDrawable::class.java).lock()
    private val DECODE_TYPE_BITMAP = decodeTypeOf(BitmapDrawable::class.java).lock()

    private val MINI_THUMB_SIZE = 100

//    @JvmStatic
//    @GlideType(GifDrawable::class)
//    fun asGif(requestBuilder: RequestBuilder<GifDrawable>) {
//        requestBuilder
//                .transition(DrawableTransitionOptions())
//                .apply(DECODE_TYPE_GIF)
//    }
//
//    @JvmStatic
//    @GlideType(BitmapDrawable::class)
//    fun asBitmap(requestBuilder: RequestBuilder<BitmapDrawable>) {
//        requestBuilder
//                .transition(DrawableTransitionOptions())
//                .apply(DECODE_TYPE_BITMAP)
//    }

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
        options.placeholder(R.drawable.ic_placeholder)
    }
}