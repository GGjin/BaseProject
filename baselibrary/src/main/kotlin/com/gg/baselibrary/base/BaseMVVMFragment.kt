package com.anpxd.baselibrary.base

import com.trello.rxlifecycle2.components.support.RxFragment
import org.greenrobot.eventbus.EventBus

/**
 * Creator : GG
 * Time    : 2017/10/29
 * Mail    : gg.jin.yu@gmail.com
 * Explain :
 */
open class BaseMVVMFragment : RxFragment() {

    open fun useEventBus(): Boolean = false

    override fun onStart() {
        super.onStart()
        if (useEventBus())
            EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        if (useEventBus())
            EventBus.getDefault().unregister(this)
    }

}