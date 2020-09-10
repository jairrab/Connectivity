package com.github.jairrab.connectivity

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission

/**
 * Check device's network connectivity and speed
 *
 * @author emil http://stackoverflow.com/users/220710/emil
 */
class Connectivity(private val context: Context) {
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun getNetworkInfo() = getNetworkInfo(context)

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isConnected() = isConnected(context)

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isConnectedFast() = isConnectedFast(context)

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isConnectedMobile() = isConnectedMobile(context)

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isConnectedWifi() = isConnectedWifi(context)

    fun isConnectionFast(type: Int, subType: Int) = Companion.isConnectionFast(type, subType)

    companion object {
        /**
         * Get the network info
         *
         * Permissions required by ConnectivityManager.getActiveNetworkInfo:
         * `android.permission.ACCESS_NETWORK_STATE`
         *
         * @param context context or activity
         * @return the [NetworkInfo] NetworkInfo
         */
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun getNetworkInfo(context: Context): NetworkInfo? {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo
        }

        /**
         * Check if there is any connectivity.
         *
         * Requires permisission:
         * `android.permission.ACCESS_NETWORK_STATE`
         *
         * @param context context or activity
         * @return true if device is connexted to the network
         */
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun isConnected(context: Context): Boolean {
            val info = getNetworkInfo(context)
            return info != null && info.isConnected
        }

        /**
         * Check if there is fast connectivity
         *
         * Requires permisission:
         * `android.permission.ACCESS_NETWORK_STATE`
         *
         * @param context context or activity
         * @return true if connection is a fast network
         */
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun isConnectedFast(context: Context): Boolean {
            val info = getNetworkInfo(context)
            return info != null && info.isConnected && isConnectionFast(info.type, info.subtype)
        }

        /**
         * Check if there is any connectivity to a mobile network
         *
         * Requires permisission:
         * `android.permission.ACCESS_NETWORK_STATE`
         *
         * @param context context or activity
         * @return true if connected to mobile
         */
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun isConnectedMobile(context: Context): Boolean {
            val info = getNetworkInfo(context)
            return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_MOBILE
        }

        /**
         * Check if there is any connectivity to a Wifi network
         *
         * Requires permisission:
         * `android.permission.ACCESS_NETWORK_STATE`
         *
         * @param context context or activity
         * @return true if connected to wifi
         */
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        fun isConnectedWifi(context: Context): Boolean {
            val info = getNetworkInfo(context)
            return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_WIFI
        }

        /**
         * Check if the connection is fast
         *
         * @param type    type of connectivity as defined by [ConnectivityManager]
         * @param subType connectivity sub type as defined by [TelephonyManager]
         * @return true if connection is a fast network
         */
        fun isConnectionFast(type: Int, subType: Int): Boolean {
            return when (type) {
                ConnectivityManager.TYPE_WIFI -> true
                ConnectivityManager.TYPE_MOBILE -> when (subType) {
                    TelephonyManager.NETWORK_TYPE_1xRTT -> false // ~ 50-100 kbps
                    TelephonyManager.NETWORK_TYPE_CDMA -> false // ~ 14-64 kbps
                    TelephonyManager.NETWORK_TYPE_EDGE -> false // ~ 50-100 kbps
                    TelephonyManager.NETWORK_TYPE_EVDO_0 -> true // ~ 400-1000 kbps
                    TelephonyManager.NETWORK_TYPE_EVDO_A -> true // ~ 600-1400 kbps
                    TelephonyManager.NETWORK_TYPE_GPRS -> false // ~ 100 kbps
                    TelephonyManager.NETWORK_TYPE_HSDPA -> true // ~ 2-14 Mbps
                    TelephonyManager.NETWORK_TYPE_HSPA -> true // ~ 700-1700 kbps
                    TelephonyManager.NETWORK_TYPE_HSUPA -> true // ~ 1-23 Mbps
                    TelephonyManager.NETWORK_TYPE_UMTS -> true // ~ 400-7000 kbps
                    TelephonyManager.NETWORK_TYPE_EHRPD -> true // ~ 1-2 Mbps
                    TelephonyManager.NETWORK_TYPE_EVDO_B -> true // ~ 5 Mbps
                    TelephonyManager.NETWORK_TYPE_HSPAP -> true // ~ 10-20 Mbps
                    TelephonyManager.NETWORK_TYPE_IDEN -> false // ~25 kbps
                    TelephonyManager.NETWORK_TYPE_LTE -> true // ~ 10+ Mbps
                    TelephonyManager.NETWORK_TYPE_UNKNOWN -> false
                    else -> false
                }
                else -> false
            }
        }

        fun getInstance(context: Context): Connectivity {
            return Connectivity(context)
        }
    }
}