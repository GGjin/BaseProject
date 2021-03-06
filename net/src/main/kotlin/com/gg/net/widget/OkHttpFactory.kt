package com.gg.net.widget

import com.socks.library.KLog
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


/**
 *  Creator : GG
 *  Time    : 2017/10/17
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */

object OkHttpFactory {
    private var CONNECT_TIMEOUT_SECONDS = 20L
    private var READ_TIMEOUT_SECONDS = 20L
    private var WRITE_TIMEOUT_SECONDS = 20L

    val client: OkHttpClient by lazy { create() }

    fun create(customInterceptor: Interceptor? = null, enableLog: Boolean = true): OkHttpClient {
        val sslParams = com.gg.net.utils.HttpUtils.getSslSocketFactory(null, null, null)
        val loggingInterceptor = com.gg.net.widget.HttpLoggingInterceptor { chain, msg ->
            KLog.json("okHttp-${chain.request().url().uri().path}", msg)
        }.apply {
            this.level = com.gg.net.widget.HttpLoggingInterceptor.Level.BODY
        }

//        val cacheInterceptor = CacheInterceptor(AppManager.currentActivity().applicationContext)

        return OkHttpClient.Builder()
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .apply {
                    if (customInterceptor != null)
                        addInterceptor(customInterceptor)
                }
                .apply {
                    if (enableLog)
                        addInterceptor(loggingInterceptor)
                }
                .apply {
//                    addInterceptor(cacheInterceptor)
                }
                .build()
    }
}