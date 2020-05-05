package com.miw.skyscanner.utils

import android.content.Context
import android.net.ConnectivityManager

class WSUtils {
    companion object {
        const val SOAP_HOST = "ec2-18-222-182-217.us-east-2.compute.amazonaws.com"
        const val SOAP_PORT = 8080
        const val SOAP_FILE = "/SkyScannerWS/SkyScannerWS.asmx?"
        const val SOAP_TIMEOUT = 30000
        const val SOAP_NAMESPACE = "http://ws.skyscanner/"
        const val METHOD_LOGIN = "Login"

        fun isConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }
    }
}