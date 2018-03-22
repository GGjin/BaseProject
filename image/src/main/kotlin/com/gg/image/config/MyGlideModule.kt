package com.gg.image.config

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule

/**
 *  Creator : GG
 *  Time    : 2017/10/16
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
@GlideModule
internal class MyGlideModule : AppGlideModule() {
    /**
     * 设置内存缓存大小10M
     */
    val  cacheSize=10*1024*1024

    override fun applyOptions(context: Context?, builder: GlideBuilder) {
        builder.setMemoryCache(LruResourceCache(cacheSize.toLong()))
    }
    /**
     * 注册一个String类型的BaseGlideUrlLoader
     */
    override fun registerComponents(context: Context?, glide: Glide, registry: Registry) {
        registry.append(String::class.java, java.io.InputStream::class.java,
                CustomBaseGlideUrlLoader.Companion.factory)
    }
    /**
     * 关闭解析AndroidManifest
     */
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}