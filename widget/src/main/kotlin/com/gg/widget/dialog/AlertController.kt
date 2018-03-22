package com.gg.widget.dialog

import android.content.Context
import android.content.DialogInterface
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import java.lang.ref.WeakReference


/**
 *  Creator : GG
 *  Time    : 2017/10/25
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
class AlertController(dialog: AlertDialog, window: Window) {

    private var mAlertDialog: AlertDialog? = dialog
    private var mViewHelper: DialogViewHelper? = null
    private var mWindow: Window? = window

    fun getAlertDialog(): AlertDialog? = mAlertDialog


    fun getWindow(): Window? = mWindow

    fun setViewHelper(viewHelper: DialogViewHelper) {
        mViewHelper = viewHelper
    }

    /**
     * 设置文本的显示
     *
     * @param viewId
     * @param text
     */
    fun setText(viewId: Int, text: CharSequence) {
        mViewHelper?.setText(viewId, text)
    }

    /**
     * 设置点击事件
     *
     * @param viewId
     * @param weakReference
     */
    fun setOnClickListener(viewId: Int, weakReference: WeakReference<View.OnClickListener>) {
        mViewHelper?.setOnClickListener(viewId, weakReference)
    }


    fun <T : View> getView(viewId: Int): T? {
        return mViewHelper?.getView(viewId)
    }

    fun setVisibility(viewId: Int, type: Int) {
        mViewHelper?.setVisibility(viewId, type)
    }


    class AlertParams(var mContext: Context, //主题的id
                      var mThemeResId: Int) {
        //点击空白是否能够取消
        var mCancelable = true
        //dialog的取消监听
        var mOnCancelListener: DialogInterface.OnCancelListener? = null
        //dialog的消失监听
        var mOnDismissListener: DialogInterface.OnDismissListener? = null
        //dialog的按键监听
        var mOnKeyListener: DialogInterface.OnKeyListener? = null
        //显示的布局
        var mView: View? = null
        //布局的id
        var mViewLayoutResId = 0
        //存放字体的修改
        var mTextArray = SparseArray<CharSequence>()
        //存放点击事件
        var mClickArray = SparseArray<WeakReference<View.OnClickListener>>()

        var mVisibilityArray = SparseArray<Int>()
        //设置宽度
        var mWidth = ViewGroup.LayoutParams.WRAP_CONTENT
        //添加动画
        var mAnimation = 0
        //位置
        var mGravity = Gravity.CENTER
        //设置高度
        var mHeight = ViewGroup.LayoutParams.WRAP_CONTENT

        fun apply(alert: AlertController) {


            var viewHelper: DialogViewHelper? = null
            //            1.设置布局
            if (mViewLayoutResId != 0)
                viewHelper = DialogViewHelper(mContext, mViewLayoutResId)

            if (mView != null)
                viewHelper = DialogViewHelper(mView!!)

            if (viewHelper == null)
                throw IllegalArgumentException("请设置setContentView方法 传入布局或布局id")

            alert.getAlertDialog()?.setContentView(viewHelper.getContentView())

            //设置Controller 的辅助类
            alert.setViewHelper(viewHelper)

            //            2.设置文本

            val textArraySize = mTextArray.size()
            for (i in 0 until textArraySize) {
                viewHelper.setText(mTextArray.keyAt(i), mTextArray.valueAt(i))
            }


            //            3.设置点击事件
            val clickArraySize = mClickArray.size()
            for (i in 0 until clickArraySize) {
                viewHelper.setOnClickListener(mClickArray.keyAt(i), mClickArray.valueAt(i))
            }
            val visibilityArraySize = mVisibilityArray.size()
            for (i in 0 until visibilityArraySize) {
                viewHelper.setVisibility(mVisibilityArray.keyAt(i), mVisibilityArray.valueAt(i))
            }


            //            4.设置自定义的一些效果   动画 从底部弹出  默认动画

            val window = alert.getWindow()

            window?.setGravity(mGravity)

            val params = window?.attributes
            params?.width = mWidth
            params?.height = mHeight

            window?.setAttributes(params)

            if (mAnimation != 0) {
                window?.setWindowAnimations(mAnimation)
            }


        }


    }
}