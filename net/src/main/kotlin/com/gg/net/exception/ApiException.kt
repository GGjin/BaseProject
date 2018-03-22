package com.gg.net.exception

/**
 *  Creator : GG
 *  Time    : 2017/10/17
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain : 对外统一的异常
 */
class ApiException(throwable: Throwable,
                   val errorCode: Int,
                   var errorMsg: String? = null) : Exception(throwable) {
    override fun toString(): String {
        return "ApiException(errorCode=$errorCode, errorMsg=$errorMsg)"
    }
}