package com.anpxd.ewalker.utils

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

/**
 * Creator : GG
 * Date    : 2018/1/12
 * Mail    : gg.jin.yu@gmai.com
 * Explain :
 */
class ViewModelFactory private constructor(
        private val application: Application

) : ViewModelProvider.NewInstanceFactory() {

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
                INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                    INSTANCE ?: ViewModelFactory(application)
                            .also { INSTANCE = it }
                }


        fun destroyInstance() {
            INSTANCE = null
        }
    }
}