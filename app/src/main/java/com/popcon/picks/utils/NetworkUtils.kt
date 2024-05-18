package com.popcon.picks.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log

object NetworkUtils {
    private val TAG = NetworkUtils::class.java.name
    @SuppressLint("NewApi")
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.d(TAG, "NetworkCapabilities = TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.d(TAG, "NetworkCapabilities = TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.d(TAG, "NetworkCapabilities = TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }
}