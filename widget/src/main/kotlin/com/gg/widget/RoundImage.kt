package com.gg.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Creator     : GG
 * Date        : 2017/12/28/028
 * Mail        : gg.jin.yu@gmai.com
 * Description :
 */
class RoundImage :ImageView {
    private var mRoundViewDelegate: RoundViewDelegate? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        if (mRoundViewDelegate == null) {
            mRoundViewDelegate = RoundViewDelegate(this, getContext())
        }
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        if (mRoundViewDelegate == null) {
            mRoundViewDelegate = RoundViewDelegate(this, getContext())
        }
    }

    constructor(context: Context) : super(context) {
        if (mRoundViewDelegate == null) {
            mRoundViewDelegate = RoundViewDelegate(this, getContext())
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int,
                          bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val w = width
        val h = height
        mRoundViewDelegate!!.roundRectSet(w, h)
    }

    override fun draw(canvas: Canvas) {
        mRoundViewDelegate!!.canvasSetLayer(canvas)
        super.draw(canvas)
        canvas.restore()
    }
}