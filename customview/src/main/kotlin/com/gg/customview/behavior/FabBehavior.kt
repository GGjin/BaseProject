package com.anpxd.ewalker.view.behavior

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View


/**
 *  Creator : GG
 *  Time    : 2017/11/1
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
class FabBehavior constructor(context: Context, attributeSet: AttributeSet) : BaseBehavior<FloatingActionButton>(context, attributeSet) {

    private var  listenerAnimatorEndBuild = ListenerAnimatorEndBuild()

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout,
                                     child: FloatingActionButton,
                                     directTargetChild: View,
                                     target: View, axes: Int, type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }


    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout,
                                child: FloatingActionButton,
                                target: View,
                                dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int,
                                type: Int) {
//        if (dyConsumed > 0 && dyUnconsumed == 0) {
//            KLog.d("behavior", "上滑中。。。")
//        }
//        if (dyConsumed == 0 && dyUnconsumed > 0) {
//            KLog.d("behavior", "到边界了还在上滑。。。")
//        }
//        if (dyConsumed < 0 && dyUnconsumed == 0) {
//            KLog.d("behavior", "下滑中。。。")
//        }
//        if (dyConsumed == 0 && dyUnconsumed < 0) {
//            KLog.d("behavior", "到边界了，还在下滑。。。")
//        }

        //页面向上滑动像素数大于0 || 拉到底还在向上拉 && 退出动画是否正在执行 && FAB按钮当前显示中
        if ((dyConsumed > 0 || dyUnconsumed > 0) && listenerAnimatorEndBuild.isFinish&& child.visibility == View.VISIBLE) {
            //隐藏Fab
            scaleHide(child,listenerAnimatorEndBuild)
        } else if ((dyConsumed < 0 || dyUnconsumed < 0) && child.visibility != View.VISIBLE) {
            //显示Fab按钮
            scaleShow(child,null)
        }
    }

}