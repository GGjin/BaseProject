package com.gg.utils

import android.view.View

/**
 * Creator : GG
 * Date    : 2018/2/24
 * Mail    : gg.jin.yu@gmai.com
 * Explain :
 */
abstract class OnMultiClickListener : View.OnClickListener {

    abstract fun onMultiClick(v: View)

    override fun onClick(v: View) {
        val curClickTime = System.currentTimeMillis()
        if (curClickTime - lastClickTime >= MIN_CLICK_DELAY_TIME) {
            // 超过点击间隔后再将lastClickTime重置为当前点击时间
            lastClickTime = curClickTime
            onMultiClick(v)
        }
    }

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private val MIN_CLICK_DELAY_TIME = 1000
    private var lastClickTime: Long = 0
}