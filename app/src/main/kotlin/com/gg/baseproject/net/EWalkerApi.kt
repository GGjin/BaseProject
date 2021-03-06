package com.gg.baseproject.net

import com.gg.baseproject.App
import com.gg.baseproject.bean.NullBean
import com.gg.baseproject.bean.Version
import com.gg.net.ApiFactory
import com.gg.baseproject.Response
import com.gg.baseproject.upgrade.VersionUtils
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

/**
 *  Creator : GG
 *  Time    : 2017/11/10
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
@ApiFactory.BaseUrl("http://xxxx.xxxx.com/")
interface EWalkerApi {

    /**
     * 更新检测结果
     */
    @Multipart
    @POST("controller/app/uploadResult")
    fun uploadDetectResult(@Part() detectionPrice: MultipartBody.Part,
                           @Part() carId: MultipartBody.Part?,
                           @Part() detectionId: MultipartBody.Part?,
                           @Part() uploadFile: MultipartBody.Part
    ): Observable<Response<NullBean>>



    /**
     * 获取版本更新信息
     */
    @FormUrlEncoded
    @POST("controller/getApk")
    fun getApk(
            @Field("apkKey") apkKey: String? = "",
            @Field("apkVersion") apkVersion: String? = VersionUtils.getVersionName(App.instance)
    ): Observable<Response<Version>>


}