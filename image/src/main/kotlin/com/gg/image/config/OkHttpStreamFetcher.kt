package com.gg.image.config

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.HttpException
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.util.ContentLengthInputStream
import com.bumptech.glide.util.Synthetic
import okhttp3.*
import java.io.IOException
import java.io.InputStream

/**
 *  Creator : GG
 *  Time    : 2017/10/17
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
class OkHttpStreamFetcher(var client: Call.Factory, var url: GlideUrl) : DataFetcher<InputStream> {
    var tag = OkHttpStreamFetcher::class.java.simpleName
    @Synthetic
    var stream: InputStream? = null
    @Synthetic var responseBody: ResponseBody? = null
    @Volatile private var call: Call? = null

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        var requestBuilder = Request.Builder().url(url.toStringUrl())
        //添加header
        for (headerEntry in url.headers.entries) {
            requestBuilder.addHeader( headerEntry.key, headerEntry.value)
        }
        var request = requestBuilder.build()
        call = client.newCall(request)
        call?.enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException) {
                callback.onLoadFailed(e)
            }
            override fun onResponse(call: Call, response: Response) {
                responseBody= response.body()
                if (response.isSuccessful) {//请求成功
                    var contentLength = responseBody!!.contentLength()
                    stream = ContentLengthInputStream.obtain(responseBody?.byteStream(), contentLength)
                    callback.onDataReady(stream)
                } else {//请求失败
                    callback.onLoadFailed(HttpException(response.message(), response.code()))
                }
            }
        })
    }


    override fun getDataSource(): DataSource {
        return  DataSource.REMOTE
    }

    /**
     * 清空
     */
    override fun cleanup() {
        try {
            stream?.close()
        }catch (e: IOException){
            e.printStackTrace()
        }
        responseBody?.close()
    }

    override fun getDataClass(): Class<InputStream> {
        return  InputStream::class.java
    }

    /**
     * 取消
     */
    override fun cancel() {
        var  localCall=call
        if (localCall!=null){
            localCall.cancel()
        }
    }
}