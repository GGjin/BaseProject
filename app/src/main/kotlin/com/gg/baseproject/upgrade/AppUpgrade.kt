package com.gg.baseproject.upgrade

/**
 *  Creator : GG
 *  Time    : 2017/12/19
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
internal interface AppUpgrade {

    fun register()

    fun unRegister()

    fun checkVersion()

    fun defaultUpgrade()

    fun forcedUpgrade()

    fun downloadApk()

    fun installAPKFile(path: String)
}