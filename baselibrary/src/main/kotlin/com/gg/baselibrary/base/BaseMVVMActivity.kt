package com.anpxd.baselibrary.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import com.anpxd.baselibrary.utils.AppManager
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import org.greenrobot.eventbus.EventBus

/**
 * Creator : GG
 * Time    : 2017/10/14
 * Mail    : gg.jin.yu@gmail.com
 * Explain :
 */
open class BaseMVVMActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        AppManager.getInstance().attach(this)


    }

    override fun onDestroy() {
        super.onDestroy()

        if (useEventBus())
            EventBus.getDefault().unregister(this)

        AppManager.getInstance().detach(this)
    }

    open fun useEventBus(): Boolean = false


    override fun onStart() {
        super.onStart()
        if (useEventBus() && !EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
    }


}
