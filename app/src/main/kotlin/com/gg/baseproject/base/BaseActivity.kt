package com.anpxd.baselibrary.base

import android.content.pm.ActivityInfo
import android.databinding.DataBindingUtil.setContentView
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
abstract class BaseActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        AppManager.getInstance().attach(this)

        initArguments()

        initTitle()

        initView()

        initData()

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



    /**
     * 获取布局id
     *
     * @return
     */
    protected abstract fun getLayoutRes(): Int


    /**
     * 获取传入数据
     */
    protected abstract fun initArguments()

    /**
     * 初始化头部
     */
    protected abstract fun initTitle()

    /**
     * 初始化View
     */
    protected abstract fun initView()

    /**
     * 初始化数据
     */
    protected abstract fun initData()

}
