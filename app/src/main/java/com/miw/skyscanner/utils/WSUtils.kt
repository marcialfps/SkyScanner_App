package com.miw.skyscanner.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class WSUtils {
    companion object {
        const val SOAP_HOST = "ec2-18-222-182-217.us-east-2.compute.amazonaws.com"
        const val SOAP_PORT = 8080
        const val SOAP_FILE = "/SkyScannerWS/SkyScannerWS.asmx?"
        const val SOAP_TIMEOUT = 30000
        const val SOAP_NAMESPACE = "http://ws.skyscanner/"
        const val METHOD_LOGIN = "Login"
        const val METHOD_REGISTER = "AddUser"
        const val METHOD_GET_WEATHER_BY_AIRPORT = "GetWeatherByAirport"
        const val METHOD_GET_WEATHER_FORECAST_BY_AIRPORT = "GetFullWeatherForecastByAirport"

        // Planes & Airports
        const val METHOD_PLANES_CLOSE_TO_AIRPORT = "GetPlanesCloseToAirport"
        const val METHOD_AIRPORT_BY_CODE= "GetAirportByCode"
        const val METHOD_PLANES_BY_ARRIVAL= "GetPlanesByArrival"
        const val METHOD_PLANES_BY_DEPARTURE= "GetPlanesByDeparture"

        //Add a constant with the name of each method

    }
}

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val actNw =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

    return when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}
