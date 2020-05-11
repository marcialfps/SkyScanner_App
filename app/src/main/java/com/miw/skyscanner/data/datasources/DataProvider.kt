package com.miw.skyscanner.data.datasources

import android.util.Log
import com.miw.skyscanner.data.db.AirportRepository
import com.miw.skyscanner.data.db.ForecastRepository
import com.miw.skyscanner.data.db.PlaneRepository
import com.miw.skyscanner.data.server.AirportServer
import com.miw.skyscanner.data.server.ForecastServer
import com.miw.skyscanner.data.server.PlaneServer
import com.miw.skyscanner.model.Airport
import com.miw.skyscanner.model.AirportForecastList
import com.miw.skyscanner.model.Forecast
import com.miw.skyscanner.model.Plane
import com.miw.skyscanner.utils.ConversionHelper
import java.text.SimpleDateFormat
import java.util.*

object DataProvider {
    private val SOURCES_FORECAST = listOf(ForecastRepository(), ForecastServer())
    private val SOURCES_AIRPORT = listOf(AirportRepository(), AirportServer())
    private val SOURCES_PLANE = listOf(PlaneRepository(), PlaneServer())

    fun requestForecastByAirportCode(airportCode: String): AirportForecastList? {
        for (source in SOURCES_FORECAST) {
            //First we try to obtain the forecast from the repo
            val result = source.requestForecastByAirportCode(airportCode)
            val dayFormatter = SimpleDateFormat("dd")
            val today = dayFormatter.format(requestCurrentForecastByAirportCode(airportCode)?.time?.times(
                1000
            ))
            if (result != null) {
                result.forecasts?.forEach {
                    val day = dayFormatter.format(it.time * 1000)
                    //At least we have the two next days
                    if (day.toInt() == today.toInt() + 2) {
                        return result
                    }
                }
            }
        }
        return null
    }

    fun requestCurrentForecastByAirportCode(airportCode: String): Forecast? {
        //We obtain the current time rounded to the hour
        val cal = Calendar.getInstance()
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val currentTime = cal.timeInMillis/1000
        for (source in SOURCES_FORECAST) {
            val result = source.requestForecastByAirportCode(airportCode)
            if (result != null) {
                result.forecasts?.forEach {
                    Log.v("currentForecast", "Forecast time: ${it.time}")
                    if (it.time >= currentTime)
                        return it
                }
            }
        }
        return null
    }

    fun requestAirportByAirportCode(airportCode: String): Airport? {
        for (source in SOURCES_AIRPORT) {
            val result = source.requestAirportByAirportCode(airportCode)
            Log.v("forecastProvider", result.toString())
            if (result != null)
                return result
        }
        return null
    }

    fun requestPlanesByAirportCode(airportCode: String, isArrivals: Boolean = false,
                                   forceQueryServer: Boolean = false): List<Plane>? {

        var sources = SOURCES_PLANE

        // Only use the server sources if querying the server is forced
        if (forceQueryServer) sources = sources.filterIsInstance<PlaneServer>()

        for (source in sources) {
            val result = source.requestPlanesByAirportCode(airportCode, isArrivals,
                resetTable = forceQueryServer)
            if (result != null)
                if (source is PlaneRepository) {
                    // If the earliest record record we have cached is already from yesterday,
                    // we should update the planes from the server and not for cache
                    val dateToEarly =
                        if (isArrivals) ConversionHelper.isHoursBeforeNow(result[0].arrivalTime, 0)
                        else ConversionHelper.isHoursBeforeNow(result[0].departureTime, 0)

                    if (!dateToEarly)
                        return result
                }
                else
                    return result
        }
        return null
    }
}