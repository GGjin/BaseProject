package com.anpxd.ewalker.view

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

/**
 * Creator : GG
 * Time    : 2017/11/19
 * Mail    : gg.jin.yu@gmail.com
 * Explain :
 */
class RectangleRelativeLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    // 宽高比
    private val RATIO = 16f / 9
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 父控件是否是固定值或者是match_parent
        val mode = MeasureSpec.getMode(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            // 得到父容器的宽度
            val parentWidth = MeasureSpec.getSize(widthMeasureSpec);
            // 得到子控件的宽度
            val childWidth = parentWidth - getPaddingLeft() - getPaddingRight();
            // 计算子控件的高度
            val childHeight =  (childWidth / RATIO + 0.5f)as Int
            // 计算父控件的高度
            val parentHeight = childHeight + getPaddingBottom() + getPaddingTop();
            // note 必须测量子控件,确定孩子的大小
            val childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            val childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
            measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);
            // 测量
            setMeasuredDimension(parentWidth, parentHeight);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }
}