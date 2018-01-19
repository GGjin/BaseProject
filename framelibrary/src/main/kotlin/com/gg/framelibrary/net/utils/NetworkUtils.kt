package com.anpxd.framelibrary.net.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Creator : GG
 * Time    : 2017/11/13
 * Mail    : gg.jin.yu@gmail.com
 * Explain :
 */
object NetworkUtils{

    @SuppressLint("MissingPermission")
    fun isNetConneted(context: Context):Boolean{
        val connectManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo : NetworkInfo?= connectManager.activeNetworkInfo
        if(networkInfo==null){
            return  false
        }else{
            return networkInfo.isAvailable&& networkInfo.isConnected
        }

    }

    @SuppressLint("MissingPermission")
    fun isNetworkConnected(context: Context, typeMobile : Int): Boolean{
        if(!isNetConneted(context)){
            return false
        }
        val connectManager  = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo : NetworkInfo = connectManager.getNetworkInfo(typeMobile)
        if(networkInfo==null){
            return false;
        }else{
            return  networkInfo.isConnected && networkInfo.isAvailable
        }
    }

    fun isPhoneNetConnected(context: Context): Boolean {
        val typeMobile = ConnectivityManager.TYPE_MOBILE
        return isNetworkConnected(context, typeMobile)
    }

    fun isWifiNetConnected(context: Context) : Boolean{
        val typeMobile = ConnectivityManager.TYPE_WIFI
        return isNetworkConnected(context, typeMobile)
    }



}