package com.gg.baseproject.upgrade

import android.app.Activity


/**
 *  Creator : GG
 *  Time    : 2017/12/19
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
class AppUpgradeHelper(private var activity: Activity) {

    private val manager: AppUpgradeManager by lazy { AppUpgradeManager(activity) }


    fun checkVersion() {
        manager.register()
        manager.checkVersion()
    }

    fun detach() {
        manager.unRegister()
    }

}