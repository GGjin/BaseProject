package com.gg.baseproject

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import android.text.TextUtils
import android.util.Log
import com.anpxd.ewalker.utils.GsonUtil
import com.anpxd.framelibrary.net.ApiFactory
import com.anpxd.framelibrary.net.widget.OkHttpFactory
import com.anpxd.framelibrary.utils.DelegatesExt
import com.gg.baseproject.utils.AppConstant
import com.gg.baseproject.utils.Utils
import com.gg.baseproject.utils.net.Response
import com.gg.baseproject.utils.bean.User
import com.google.gson.reflect.TypeToken
import okhttp3.Interceptor
import okhttp3.ResponseBody
import org.greenrobot.eventbus.EventBus


/**
 *  Creator : GG
 *  Time    : 2017/10/16
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
class App : MultiDexApplication() {

    companion object {
        lateinit var instance: App

    }

    init {
        instance = this

    }

    private var userInfo: String by DelegatesExt.preference(this, AppConstant.USER, "")

    override fun onCreate() {
        super.onCreate()
        EventBus.builder().installDefaultEventBus()
        initNet()
        if (applicationInfo.packageName == Utils.getCurProcessName(applicationContext)) {
            if (Utils.isApkInDebug(this)) {
            }
        }
    }

    private fun initNet() {
        ApiFactory.okHttpClient = OkHttpFactory.create(Interceptor {
            val originRequest = it.request()
            // 添加 header 以及公共的 GET 参数
            val newRequest = originRequest.newBuilder()
                    .url(originRequest.url().newBuilder()
//                            .addQueryParameter("uid", LocalUser.userToken?.user_id?:"unlogin")
//                            .addQueryParameter("token", LocalUser.userToken?.token?:"")
//                            .addQueryParameter("device_id", DeviceUtils.getAndroidID())
//                        .addQueryParameter("src", "android")
                            .build()
                    ).build()

            /** 处理不规范的返回值
             *  <-- 400 Bad Request
             *  {
             *      "code": 2,
             *      "msg": "密码错误",
             *      "result": []             // 应该返回 空对象{}, 否则 Json 解析异常
             *  }
             */
            val response = it.proceed(newRequest)
            response.newBuilder()
                    .apply {
                        val originBody = response.body()
                        var json = originBody?.string()

                        var res: Response<Any>? = null

                        try {
                            res = GsonUtil.fromJson(json.toString(), object : TypeToken<Response<Any>>() {}.type)
                        } catch (e: Exception) {
                            Log.e("initNet", "interceptor response" + e)
                        }
                        try {
                            res = GsonUtil.fromJson(json.toString(), object : TypeToken<Response<List<Any>>>() {}.type)
                        } catch (e: Exception) {
                            Log.e("initNet", "interceptor response" + e)
                        }

                        // 不成功，则移除 "result" 字段
                        if (1 != res?.code) {
                            res?.result = null

                        }
                        json = GsonUtil.toJson(res!!)
                        this.body(ResponseBody.create(originBody?.contentType(), json))
                    }
                    .apply {
                        this.code(if (response.code() in 400..500) 200 else response.code())
                    }
                    .build()
        })
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    fun getUser(): User? = if (TextUtils.isEmpty(userInfo)) null else GsonUtil.jsonToBean(userInfo, User::class.java)!!

    fun setUser(user: User) {
        userInfo = GsonUtil.toJson(user)
    }

    fun setUser(userInfo: String) {
        this.userInfo = userInfo
    }


}