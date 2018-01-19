package com.anpxd.ewalker.utils

import android.content.Context
import com.socks.library.KLog
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File

/**
 *  Creator : GG
 *  Time    : 2017/12/5
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
class UpdateImageUtils {

    companion object {

        fun updateImage(context: Context, localPath: String,
                        carId: String , imageKey: String, listener: UpdateListener) {
            Luban.with(context).load(localPath)
                    .setCompressListener(object : OnCompressListener {
                        override fun onSuccess(file: File?) {
                            KLog.w("compress", "压缩成功" + file?.length()?.shr(10))

                            // 根据文件格式封装文件
                            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)

                            val body = MultipartBody.Part.createFormData("uploadFile", file?.name, requestFile)

//                            ApiFactory.getApi(EWalkerApi::class.java)
//                                    .uploadFile(body,
//                                            imageKey = imageKey,
//                                            carId = carId)
//                                    .compose(Composers.handleError())
//                                    .subscribe({
//                                        listener.onSucceed(/*UrlConfig.IMAGE_URL +*/ it.pathUrl)
////                                        imageUrl = UrlConfig.IMAGE_URL + it.pathUrl
////                                        KLog.w("url", "------->" + imageUrl)
//                                    }, {
//                                        listener.onFail(e = null)
//                                    })
                        }

                        override fun onError(e: Throwable?) {
                            KLog.w("compress", "压缩失败")
                            listener.onFail(e!!)
                        }

                        override fun onStart() {
                            KLog.w("compress", "压缩开始")
                        }
                    }).launch()
        }

    }

}