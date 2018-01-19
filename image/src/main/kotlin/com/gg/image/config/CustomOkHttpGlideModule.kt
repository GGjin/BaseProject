package com.anpxd.imageloader.config

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.LibraryGlideModule
import java.io.InputStream

/**
 *  Creator : GG
 *  Time    : 2017/10/17
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
@GlideModule
internal class CustomOkHttpGlideModule : LibraryGlideModule(){
    override fun registerComponents(context: Context?, glide: Glide?, registry: Registry?) {
        registry?.replace(GlideUrl::class.java, InputStream::class.java,OkHttpUrlLoader.factory)
    }
}