package com.gg.widget.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.gg.widget.R
import com.gg.widget.dialog.AlertController
import java.lang.ref.WeakReference


/**
 *  Creator : GG
 *  Time    : 2017/10/25
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
class AlertDialog private constructor(context: Context, themeResId: Int) : Dialog(context, themeResId) {

    var mAlert: AlertController = AlertController(this, window)

    /**
     * 设置文本的显示
     *
     * @param viewId
     * @param text
     */
    fun setText(viewId: Int, text: CharSequence) {
        mAlert.setText(viewId, text)
    }

    /**
     * 设置点击事件
     *
     * @param viewId
     * @param weakReference
     */
    fun setOnClickListener(viewId: Int, weakReference: WeakReference<View.OnClickListener>) {
        mAlert.setOnClickListener(viewId, weakReference)
    }


    fun <T : View> getView(viewId: Int): T? {
        return mAlert.getView(viewId)
    }

    fun setVisibility(viewId: Int, type: Int) {
        mAlert.setVisibility(viewId, type)
    }

    class Builder
    /**
     * Creates a builder for an alert dialog that uses an explicit theme
     * resource.
     *
     *
     * The specified theme resource (`themeResId`) is applied on top
     * of the parent `context`'s theme. It may be specified as a
     * style resource containing a fully-populated theme, such as
     * [android.R.style.Theme_Material_Dialog], to replace all
     * attributes in the parent `context`'s theme including primary
     * and accent colors.
     *
     *
     * To preserve attributes such as primary and accent colors, the
     * `themeResId` may instead be specified as an overlay theme such
     * as [android.R.style.ThemeOverlay_Material_Dialog]. This will
     * override only the window attributes necessary to style the alert
     * window as a dialog.
     *
     *
     * Alternatively, the `themeResId` may be specified as `0`
     * to use the parent `context`'s resolved value for
     * [android.R.attr.alertDialogTheme].
     *
     * @param context    the parent context
     * @param themeResId the resource ID of the theme against which to inflate
     * this dialog, or `0` to use the parent
     * `context`'s default alert dialog theme
     */
    @JvmOverloads constructor(context: Context, themeResId: Int = R.style.dialog) {
        private val P: AlertController.AlertParams

        init {
            P = AlertController.AlertParams(context, themeResId)
        }


        /**
         * Set a custom view resource to be the contents of the Dialog. The
         * resource will be inflated, adding all top-level views to the screen.
         *
         * @param layoutResId Resource ID to be inflated.
         * @return this Builder object to allow for chaining of calls to set
         * methods
         */
        fun setContentView(layoutResId: Int): Builder {
            P.mView = null
            P.mViewLayoutResId = layoutResId
            return this
        }


        fun setContentView(view: View): Builder {
            P.mView = view
            P.mViewLayoutResId = 0
            return this
        }


        /**
         * Sets whether the dialog is cancelable or not.  Default is true.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setCancelable(cancelable: Boolean): Builder {
            P.mCancelable = cancelable
            return this
        }

        fun setVisibilty(viewId: Int, type: Int): Builder {
            P.mVisibilityArray.put(viewId, type)
            return this
        }

        /**
         * Sets the callback that will be called if the dialog is canceled.
         *
         *
         *
         * Even in a cancelable dialog, the dialog may be dismissed for reasons other than
         * being canceled or one of the supplied choices being selected.
         * If you are interested in listening for all cases where the dialog is dismissed
         * and not just when it is canceled, see
         * [setOnDismissListener][.setOnDismissListener].
         *
         * @return This Builder object to allow for chaining of calls to set methods
         * @see .setCancelable
         * @see .setOnDismissListener
         */
        fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener): Builder {
            P.mOnCancelListener = onCancelListener
            return this
        }

        /**
         * Sets the callback that will be called when the dialog is dismissed for any reason.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener): Builder {
            P.mOnDismissListener = onDismissListener
            return this
        }

        /**
         * Sets the callback that will be called if a key is dispatched to the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        fun setOnKeyListener(onKeyListener: DialogInterface.OnKeyListener): Builder {
            P.mOnKeyListener = onKeyListener
            return this
        }


        fun setText(viewId: Int, text: CharSequence): Builder {
            P.mTextArray.put(viewId, text)
            return this
        }


        fun setOnClickListener(viewId: Int, listener: View.OnClickListener): Builder {
            P.mClickArray.put(viewId, WeakReference(listener))
            return this
        }


        /**
         * 设置是否是全屏
         *
         * @return
         */
        fun fullWidth(): Builder {
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT
            return this
        }

        /**
         * 设置从底部弹出
         *
         * @param isAnimation
         * @return
         */
        fun fromBottom(isAnimation: Boolean): Builder {
            if (isAnimation) {
                P.mAnimation = R.style.dialog_from_bottom_anim
            }

            P.mGravity = Gravity.BOTTOM
            return this
        }

        /**
         * 设置宽高
         *
         * @param width
         * @param height
         * @return
         */
        fun setWidthAndHeight(width: Int, height: Int): Builder {
            P.mWidth = width
            P.mHeight = height
            return this
        }


        /**
         * 添加默认动画
         *
         * @return
         */
        fun addDefaultAnimation(): Builder {
            P.mAnimation = R.style.dialog_animation_fade
            return this
        }

        /**
         * 提供更改动画的方法
         *
         * @param animationId
         * @return
         */
        fun setAnimation(animationId: Int): Builder {
            P.mAnimation = animationId
            return this
        }


        /**
         * Creates an [AlertDialog] with the arguments supplied to this
         * builder.
         *
         *
         * Calling this method does not display the dialog. If no additional
         * processing is needed, [.show] may be called instead to both
         * create and display the dialog.
         */
        fun create(): AlertDialog {
            // Context has already been wrapped with the appropriate theme.
            val dialog = AlertDialog(P.mContext, P.mThemeResId)
            P.apply(dialog.mAlert)
            dialog.setCancelable(P.mCancelable)
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true)
            }
            dialog.setOnCancelListener(P.mOnCancelListener)
            dialog.setOnDismissListener(P.mOnDismissListener)
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener)
            }
            return dialog
        }

        /**
         * Creates an [AlertDialog] with the arguments supplied to this
         * builder and immediately displays the dialog.
         *
         *
         * Calling this method is functionally identical to:
         * <pre>
         * AlertDialog dialog = builder.create();
         * dialog.show();
        </pre> *
         */
        fun show(): AlertDialog {
            val dialog = create()
            dialog.show()
            return dialog
        }
    }
    /**
     * Creates a builder for an alert dialog that uses the default alert
     * dialog theme.
     *
     *
     * The default alert dialog theme is defined by
     * [android.R.attr.alertDialogTheme] within the parent
     * `context`'s theme.
     *
     * @param context the parent context
     */

}