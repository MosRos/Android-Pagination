/*
 * *
 *  * Created by Moslem Rostami on 6/19/20 12:19 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/19/20 12:19 PM
 *
 */

package com.morostami.androidpagination.data.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.morostami.androidpagination.presentation.MainApp

object NetworkUtils {

    fun hasNetworkConnection(): Boolean {
        val connectivityManager =
            MainApp.instance.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }

    fun isConnected() : Boolean {
        val connMgr = MainApp.instance.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var isWifiConn: Boolean = false
        var isMobileConn: Boolean = false
        connMgr.allNetworks.forEach { network ->
            connMgr.getNetworkInfo(network).apply {
                if (type == ConnectivityManager.TYPE_WIFI) {
                    isWifiConn = isWifiConn or isConnected
                }
                if (type == ConnectivityManager.TYPE_MOBILE) {
                    isMobileConn = isMobileConn or isConnected
                }
            }
        }
        return isWifiConn || isMobileConn
    }

    fun isOnline(): Boolean {
        val connMgr = MainApp.instance.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

}