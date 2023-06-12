package com.felix.tools

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import java.net.NetworkInterface
import java.net.SocketException


/**
 * Created by zyf on 2023-06-05 15:59
 * Describe: 网路工具栏
 */

/**
 * 是否开启vpn
 */
fun Context.isUseVpn(): Boolean {
    if(isUseVpn1()) return true
    return isUseVpn2()
}

private fun Context.isUseVpn1(): Boolean {
    getSystemService(Context.CONNECTIVITY_SERVICE)?.let { manager ->
        manager as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.getNetworkCapabilities(manager.activeNetwork)?.let {
                return it.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
            }
        } else {
            manager.getNetworkInfo(ConnectivityManager.TYPE_VPN)?.let {
                return it.isConnected
            }
        }
    }
    return false
}

private fun isUseVpn2(): Boolean {
    try {
        val networkInterfaces = NetworkInterface.getNetworkInterfaces()
        while (networkInterfaces.hasMoreElements()) {
            val name = networkInterfaces.nextElement().name
            if (name == "tun0" || name == "ppp0") {
                return true
            }
        }
    } catch (e: SocketException) {
        e.printStackTrace()
    }
    return false
}

/**
 * 检测wifi代理，一般用于抓包的
 */
fun isUseProxy(): Boolean {
    val proxyAddress = System.getProperty("http.proxyHost");
    val portStr = System.getProperty("http.proxyPort") ?: "-1"
    val proxyPort = Integer.parseInt(portStr)
    return (!proxyAddress.isNullOrEmpty()) && (proxyPort != -1)
}

/**
 * 判断是否有网络连接
 *
 * @param context
 * @return
 */
fun Context.isNetworkConnected(): Boolean {
    getSystemService(Context.CONNECTIVITY_SERVICE)?.let { manager ->
        manager as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.getNetworkCapabilities(manager.activeNetwork)?.let {
                return it.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            }
        } else {
            manager.activeNetworkInfo?.let {
                if(it.isConnected){
                    return it.state == NetworkInfo.State.CONNECTED
                }
            }
        }
    }
    return false
}

/**
 * 获取当前网络状态
 */
fun Context.networkStatus(): Boolean {
    getSystemService(Context.CONNECTIVITY_SERVICE)?.let { manager ->
        manager as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.getNetworkCapabilities(manager.activeNetwork)?.let {
                return it.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
            }
        } else {
            val info = manager.getNetworkInfo(ConnectivityManager.TYPE_VPN)
            return info?.isConnected ?: false
        }
    }
    return false
}