package com.anpxd.ewalker.view

import com.chad.library.adapter.base.loadmore.LoadMoreView
import com.gg.baseproject.R

/**
 *  Creator : GG
 *  Time    : 2017/11/29
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
class CustomLoadMoreView : LoadMoreView() {

    override fun getLayoutId(): Int {
        return R.layout.view_load_more
    }

    override fun getLoadingViewId(): Int {
        return R.id.load_more_loading_view
    }

    override fun getLoadFailViewId(): Int {
        return R.id.load_more_load_fail_view
    }

    override fun getLoadEndViewId(): Int {
        return R.id.load_more_load_end_view
    }
}