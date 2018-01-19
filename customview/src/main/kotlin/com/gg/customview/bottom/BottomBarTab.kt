package com.anpxd.ewalker.view.bottom

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import com.gg.customview.R

/**
 * Creator     : GG
 * Date        : 2017/12/28/028
 * Mail        : gg.jin.yu@gmai.com
 * Description :
 */
class BottomBarTab : FrameLayout {
    private var mIcon: ImageView? = null
    private var mContext: Context? = null

    private var mDefaultIcon: Int

    private var mChooseIcon: Int

    var tabPosition = -1
        set(position) {
            field = position
            if (position == 0) {
                isSelected = true
            }
        }


    constructor(context: Context, @DrawableRes defaultIcon: Int, @DrawableRes chooseIcon: Int) : this(context, null, defaultIcon, chooseIcon)

    constructor(context: Context, attrs: AttributeSet?, defaultIcon: Int, chooseIcon: Int) : this(context, attrs, 0, defaultIcon, chooseIcon)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defaultIcon: Int, chooseIcon: Int) : super(context, attrs, defStyleAttr) {
        mDefaultIcon = defaultIcon
        mChooseIcon = chooseIcon
        init(context)

    }

    private fun init(context: Context) {
        mContext = context
        val typedArray = context.obtainStyledAttributes(intArrayOf(R.attr.selectableItemBackgroundBorderless))
        val drawable = typedArray.getDrawable(0)
        setBackgroundDrawable(drawable)
        typedArray.recycle()

        mIcon = ImageView(context)
        val size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 27f, resources.displayMetrics).toInt()
        val params = FrameLayout.LayoutParams(size, size)
        params.gravity = Gravity.CENTER
        mIcon!!.setImageResource(mDefaultIcon)
        mIcon!!.layoutParams = params
        mIcon!!.setColorFilter(ContextCompat.getColor(context, R.color.tab_unselect))
        addView(mIcon)
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        if (selected) {
//            mIcon!!.setColorFilter(ContextCompat.getColor(mContext!!, R.color.colorPrimary))
            mIcon!!.setImageResource(mChooseIcon)
        } else {
//            mIcon!!.setColorFilter(ContextCompat.getColor(mContext!!, R.color.tab_unselect))
            mIcon!!.setImageResource(mDefaultIcon)
        }
    }
}