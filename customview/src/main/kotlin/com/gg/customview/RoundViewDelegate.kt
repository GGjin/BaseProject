package com.gg.customview

import android.content.Context
import android.graphics.*
import android.graphics.Canvas.ALL_SAVE_FLAG
import android.view.View


/**
 * Creator     : GG
 * Date        : 2017/12/28/028
 * Mail        : gg.jin.yu@gmai.com
 * Description : 圆角View 代理 如果想改为圆形view，修改rect_adius为很大的数字，比如10000
 */
class RoundViewDelegate(private val mView: View?, private val mContext: Context) {
    private val roundRect = RectF()
    private var rect_radius = 1000f  //单位为像素
    private val maskPaint = Paint()
    private val zonePaint = Paint()

    init {
        init()
    }

    private fun init() {
        maskPaint.isAntiAlias = true
        maskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        //
        zonePaint.isAntiAlias = true
        zonePaint.color = Color.WHITE
    }

    /**
     * 从新设置圆角
     * @param adius
     */
    fun setRectAdius(adius: Float) {
        rect_radius = adius
        if (mView != null) {
            mView.invalidate()
        }
    }

    /**
     * 圆角区域设置
     * @param width
     * @param height
     */
    fun roundRectSet(width: Int, height: Int) {
        roundRect.set(0f, 0f, width.toFloat(), height.toFloat())
    }

    /**
     * 画布区域裁剪
     * @param canvas
     */
    fun canvasSetLayer(canvas: Canvas) {
        canvas.saveLayer(roundRect, zonePaint, Canvas.ALL_SAVE_FLAG)
        canvas.drawRoundRect(roundRect, rect_radius, rect_radius, zonePaint)
        //
        canvas.saveLayer(roundRect, maskPaint, Canvas.ALL_SAVE_FLAG)
    }
}