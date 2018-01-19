package com.anpxd.imageloader.config

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 *  Creator : GG
 *  Time    : 2017/10/17
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
internal class OkHttpProvider{
    companion object{
        /**
         * 自定义配置OkHttpClient
         */
        fun createOkHttpClient(): OkHttpClient {
            var  builder= OkHttpClient.Builder()
            var  loggingInterceptor= HttpLoggingInterceptor()
            loggingInterceptor.level= HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)

            return builder.build()
        }
    }
}