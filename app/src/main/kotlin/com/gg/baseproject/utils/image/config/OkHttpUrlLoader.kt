package com.anpxd.imageloader.config

import com.bumptech.glide.load.Options
import okhttp3.Call
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import java.io.InputStream

/**
 *  Creator : GG
 *  Time    : 2017/10/17
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
class OkHttpUrlLoader(var client: Call.Factory) :ModelLoader<GlideUrl,InputStream>{
    override fun buildLoadData(model: GlideUrl, width: Int, height: Int, options: Options?): ModelLoader.LoadData<InputStream>? {
        return ModelLoader.LoadData(model,OkHttpStreamFetcher(client,model))
    }
    override fun handles(model: GlideUrl?)=true

    companion object{
        /**
         * 一个OkHttpUrlLoader的工厂
         */
        var factory=object : ModelLoaderFactory<GlideUrl, InputStream> {
            @Volatile private var internalClient: Call.Factory? = null
            private var client: Call.Factory? = null
            init {
                client=getInternalClient()
            }
            private fun getInternalClient(): Call.Factory? {
                if (internalClient == null) {
                    synchronized(this) {
                        if (internalClient == null) {
                            internalClient = OkHttpProvider.createOkHttpClient()
                        }
                    }
                }
                return internalClient
            }
            override fun build(multiFactory: MultiModelLoaderFactory?): ModelLoader<GlideUrl, InputStream> {
                return  OkHttpUrlLoader(client!!)
            }
            override fun teardown() {
            }
        }
    }
}