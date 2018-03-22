package com.gg.widget.behavior

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.util.AttributeSet
import android.view.View

/**
 *  Creator : GG
 *  Time    : 2017/11/1
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
open class BaseBehavior<T : View> constructor(context: Context,attrs:AttributeSet): CoordinatorLayout.Behavior<T>(){

    private val mFastInterpolator = FastOutSlowInInterpolator()


    // 显示view
    fun scaleShow(view: View,l :ListenerAnimatorEndBuild?) {
        view.visibility = View.VISIBLE
        ViewCompat.animate(view)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1.0f)
                .setDuration(500)
                .setListener(l?.build())
                .setInterpolator(mFastInterpolator)
                .start()
    }


    // 隐藏view
    fun scaleHide(view: View,l:ListenerAnimatorEndBuild?) {
        ViewCompat.animate(view)
                .scaleX(0.0f)
                .scaleY(0.0f)
                .alpha(0.0f)
                .setDuration(500)
                .setInterpolator(mFastInterpolator)
                .setListener(l?.build())
                .start()
    }


    class ListenerAnimatorEndBuild {
        // 记录View移出动画是否执行完。
        private var isOutExecute = false

        private val outAnimatorListener: ViewPropertyAnimatorListener

        // View移出动画是否执行完。
        val isFinish: Boolean
            get() = !isOutExecute

        init {
            outAnimatorListener = object : ViewPropertyAnimatorListener {
                override fun onAnimationStart(view: View) {
                    isOutExecute = true
                }

                override fun onAnimationEnd(view: View) {
                    view.visibility = View.INVISIBLE
                    isOutExecute = false
                }

                override fun onAnimationCancel(view: View) {
                    isOutExecute = false
                }
            }
        }

        // 返回ViewPropertyAnimatorListener。
        fun build(): ViewPropertyAnimatorListener {
            return outAnimatorListener
        }
    }

}