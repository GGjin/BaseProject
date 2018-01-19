package com.anpxd.baselibrary.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle2.components.support.RxFragment
import org.greenrobot.eventbus.EventBus

/**
 * Creator : GG
 * Time    : 2017/10/29
 * Mail    : gg.jin.yu@gmail.com
 * Explain :
 */
abstract class BaseFragment : RxFragment() {


    var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(getLayoutResId(), container, false)
        return rootView
    }

    abstract fun getLayoutResId(): Int

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