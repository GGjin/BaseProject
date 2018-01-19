package com.anpxd.imageloader.config

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.*
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader
import java.io.InputStream
import java.util.regex.Pattern

/**
 *  Creator : GG
 *  Time    : 2017/10/17
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
internal class CustomBaseGlideUrlLoader(concreteLoader: ModelLoader<GlideUrl, InputStream>, modelCache: ModelCache<String, GlideUrl>): BaseGlideUrlLoader<String>(concreteLoader,modelCache){
    /**
     * Url的匹配规则
     */
    val patern= Pattern.compile("__w-((?:-?\\d+)+)__")
    /**
     * 控制需要加载图片的尺寸大小
     */
    override fun getUrl(model: String, width: Int, height: Int, options: Options): String {
        var  m=patern.matcher(model)
        var bestBucket: Int
        if (m.find()){
            var  found=m.group(1).split("-")
            for (item in found){
                bestBucket=item.toInt()
                if (bestBucket>=width) break
            }
        }
        return model
    }
    override fun handles(model: String?)=true
    companion object{
        val urlCache= ModelCache<String, GlideUrl>(150)
        val factory=object: ModelLoaderFactory<String, InputStream> {
            override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<String, InputStream> {
                return CustomBaseGlideUrlLoader(multiFactory.build(GlideUrl::class.java, InputStream::class.java), urlCache)
            }
            override fun teardown() {
            }
        }
    }
}