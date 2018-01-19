package com.anpxd.ewalker.utils

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.databinding.BindingAdapter
import android.support.design.widget.Snackbar
import android.view.View
import com.anpxd.ewalker.view.ScrollChildSwipeRefreshLayout
import com.example.android.architecture.blueprints.todoapp.SingleLiveEvent

/**
 * Creator : GG
 * Date    : 2018/1/12
 * Mail    : gg.jin.yu@gmai.com
 * Explain :
 */
/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).show()
}

/**
 * Triggers a snackbar message when the value contained by snackbarTaskMessageLiveEvent is modified.
 */
fun View.setupSnackbar(lifecycleOwner: LifecycleOwner,
                       snackbarMessageLiveEvent: SingleLiveEvent<Int>, timeLength: Int) {
    snackbarMessageLiveEvent.observe(lifecycleOwner, Observer {
        it?.let { showSnackbar(context.getString(it), timeLength) }
    })
}
/**
 * Reloads the data when the pull-to-refresh is triggered.
 *
 * Creates the `android:onRefresh` for a [SwipeRefreshLayout].
 */
@BindingAdapter("android:onRefresh")
fun ScrollChildSwipeRefreshLayout.setSwipeRefreshLayoutOnRefreshListener(
        viewModel: LoadData) {
    setOnRefreshListener { viewModel.loadData(true) }
}