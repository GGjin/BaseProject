package com.gg.widget.dialog

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import java.lang.ref.WeakReference


/**
 *  Creator : GG
 *  Time    : 2017/10/25
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
class DialogViewHelper {

    private var mContentView: View
    private var mViews: SparseArray<WeakReference<View>> = SparseArray()

    constructor(view: View) {
        mContentView = view
    }

    constructor(context: Context, themeResId: Int) {
        mContentView = LayoutInflater.from(context).inflate(themeResId, null, false)
    }

    fun setText(id: Int, text: CharSequence) {
        val tv = getView<TextView>(id)
        if (tv != null) {
            tv.text = text
        }
    }


    fun setOnClickListener(id: Int, weakReference: WeakReference<View.OnClickListener>) {
        val view = getView<View>(id)
        val listenter = weakReference.get()

        if (view != null) {
            view.setOnClickListener(listenter)
        }
    }

    fun setVisibility(viewId: Int, type: Int) {
        getView<View>(viewId)?.visibility = type
    }

    fun <T : View> getView(id: Int): T? {
        val weakReference = mViews[id]
        val view: View?
        if (weakReference != null) {
            view = weakReference.get()!!
        } else {
            view = mContentView.findViewById(id)
            if (view != null) {
                mViews.put(id, WeakReference(view))
            }
        }
        return view as T
    }

    fun getContentView(): View {
        return mContentView
    }

}