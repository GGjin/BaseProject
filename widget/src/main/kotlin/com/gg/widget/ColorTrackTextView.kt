package com.gg.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.TextView

/**
 *  Creator : GG
 *  Time    : 2017/11/10
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
class ColorTrackTextView : TextView {


    //原始颜色的画笔
    private val mOriginalPaint: Paint
    //改变颜色的画笔
    private val mChangePaint: Paint
    //进度
    private var mCurrentProcess: Float = 0f
    //变化方向
    private var mDirection = Direction.LEFT_TO_RIGHT

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val array = context?.obtainStyledAttributes(attrs, R.styleable.ColorTrackTextView)
        val mOriginalColor = array?.getColor(R.styleable.ColorTrackTextView_originalColor, textColors.defaultColor)
        val mChangeColor = array?.getColor(R.styleable.ColorTrackTextView_changeColor, textColors.defaultColor)
        array?.recycle()
        mOriginalPaint = getPaintByColor(mOriginalColor!!)
        mChangePaint = getPaintByColor(mChangeColor!!)
    }


    private fun getPaintByColor(colorRes: Int): Paint {
        return Paint().apply {
            color = colorRes
            isAntiAlias = true
            //防抖动
            isDither = true
            textSize = this@ColorTrackTextView.textSize
        }
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
        val middle =(mCurrentProcess * width).toInt()
        when (mDirection) {
            Direction.LEFT_TO_RIGHT -> {
                drawText(canvas, mChangePaint , 0, middle)
                drawText(canvas, mOriginalPaint , middle, width)
            }
            Direction.RIGHT_TO_LEFT -> {
                drawText(canvas, mOriginalPaint, 0, width - middle)
                drawText(canvas, mChangePaint, width - middle, width)
            }
        }

    }

    private fun drawText(canvas: Canvas?, paint: Paint, start: Int, end: Int) {
        canvas?.save()

        val rect = Rect(start, 0, end, height)
        canvas?.clipRect(rect)

        val bound = Rect()
        paint.getTextBounds(text.toString(), 0, text.toString().length, bound)
        val x = width / 2 - bound.width() / 2

        val fontMetrics = paint.fontMetricsInt
        val baseLine = height / 2 - fontMetrics.bottom / 2 - fontMetrics.top / 2

        canvas?.drawText(text.toString(), x.toFloat(), baseLine.toFloat(), paint)
        canvas?.restore()
    }

    fun setCurrentProcess(process: Float) {
        this.mCurrentProcess = process
        invalidate()
    }

    fun setOriginalColor(color: Int) {
        mOriginalPaint.color = color
    }

    fun setChangeColor(color: Int) {
        mChangePaint.color = color
    }

    fun setDirection(d: Direction) {
        mDirection = d
    }

    enum class Direction {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT
    }

}