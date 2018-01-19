package com.gg.baseproject

import com.anpxd.ewalker.utils.GsonUtil

/**
 *  Creator : GG
 *  Time    : 2017/11/10
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
class Response<T> {

    var code = -1          // errorCode
    var msg = ""          // errorMsg
    var result: T? = null    // data

    override fun toString(): String = GsonUtil.toJson(this)
}