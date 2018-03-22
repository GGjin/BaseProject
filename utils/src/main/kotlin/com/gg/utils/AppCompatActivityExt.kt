package com.gg.utils

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import java.util.regex.Pattern

/**
 *  Creator : GG
 *  Time    : 2017/10/16
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain : The `fragment` is added to the container view with id `frameId`. The operation is
 *            performed by the `fragmentManager`.
 */
fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, @IdRes frameId: Int) {
    supportFragmentManager.transact {
        replace(frameId, fragment)
    }
}

/**
 * The `fragment` is added to the container view with tag. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.addFragmentToActivity(fragment: Fragment, tag: String) {
    supportFragmentManager.transact {
        add(fragment, tag)
    }
}

fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(toolbarId))
    supportActionBar?.run {
        action()
    }
}

fun FragmentTransaction.addAnimation() {
    setCustomAnimations(
            R.anim.slide_right_in,
            R.anim.slide_left_out,
            R.anim.slide_left_in,
            R.anim.slide_right_out
    )
}
fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(viewModelClass)
/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}

fun Context.toast(str: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, str, length).show()
}

fun Context.toast(int: Int, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, int, length).show()
}

fun AppCompatActivity.context(): Context = this

fun Fragment.toast(str: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, str, length).show()
}

fun Fragment.toast(int: Int, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, int, length).show()
}

fun String.findCarIdByUrl(param: String): String {
    val mc = Pattern.compile("(^|\\?|&)$param=([^&|#]*)(&|$|#)").matcher(this)
    return if (mc.find()) {
        mc.group(2)
    } else ""
}