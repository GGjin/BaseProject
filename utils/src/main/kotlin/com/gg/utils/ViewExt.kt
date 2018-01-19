package com.anpxd.ewalker.utils

import android.support.design.widget.Snackbar
import android.view.View

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
