package com.gg.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

/**
 * Creator : GG
 * Date    : 2018/1/18
 * Mail    : gg.jin.yu@gmai.com
 * Explain :
 */
class RectangleLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    //宽度占比
    private var width_weight = 1
    //高度占比
    private var height_weight = 1

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RectangleLayout, defStyleAttr, 0);

        width_weight = typedArray.getInt(R.styleable.RectangleLayout_width_weight, 1)
        height_weight = typedArray.getInt(R.styleable.RectangleLayout_height_weight, 1)

        typedArray.recycle()

    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //获取父容器允许的宽高
        val maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        val widthMode = MeasureSpec.getMode(widthMeasureSpec);
        val maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        val heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取测量的宽高
        val mWidth = getMeasuredWidth();
        val mHeight = getMeasuredHeight();
        //最终设定的宽高
        var width = 0;
        var height = 0;
        //根据MeasureSpec模式的不同，宽高取值不同
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            //当都为wrap时，宽高取最大值
            if (mWidth * height_weight >= mHeight * width_weight) {
                width = mWidth;
                height = (width * height_weight / width_weight).toInt()
            } else {
                height = mHeight;
                width = (height * width_weight / height_weight).toInt()
            }
        } else if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            //当都为固定值或match时，宽高取最小值，以保证不会越过设定的最大范围
            if (mWidth * height_weight <= mHeight * width_weight) {
                width = mWidth;
                height = (width * height_weight / width_weight).toInt()
            } else {
                height = mHeight;
                width = (height * width_weight / height_weight).toInt()
            }
        } else if (widthMode == MeasureSpec.EXACTLY) {
            //当一项设为固定值或match时，以这条为标准
            width = mWidth;
            height = (width * height_weight / width_weight).toInt()
        } else if (heightMode == MeasureSpec.EXACTLY) {
            //当一项设为固定值或match时，以这条为标准
            height = mHeight;
            width = (height * width_weight / height_weight).toInt()
        }
        //最终和父容器允许的宽高比较，最大不超过父容器的约束
        if (maxWidth < width) {
            width = maxWidth;
            height = (width * height_weight / width_weight).toInt()
        }
        if (maxHeight < height) {
            height = maxHeight;
            width = (height * width_weight / height_weight).toInt()
        }
        //将最终的宽高设定为容器的宽高
        setMeasuredDimension(width, height);
    }
}