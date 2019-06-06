package com.codding.test.startoverflowuser.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.google.gson.GsonBuilder
import timber.log.Timber
import java.util.*

fun getConnectionType(context: Context): NetWorkConnectionState {
    var result = NetWorkConnectionState.NONE
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        cm?.run {
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    result = NetWorkConnectionState.WIFI
                } else if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    result = NetWorkConnectionState.CELL
                }
            }
        }
    } else {
        cm?.run {
            cm.activeNetworkInfo?.run {
                if (type == ConnectivityManager.TYPE_WIFI) {
                    result = NetWorkConnectionState.WIFI
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    result = NetWorkConnectionState.CELL
                }
            }
        }
    }
    return result
}

fun getJsonPrettyData(data : Any) : String {
    try {
        return GsonBuilder().setPrettyPrinting().create().toJson(data)
    } catch (ignored: Exception) {
        Timber.e(ignored)
    }
    return ""
}