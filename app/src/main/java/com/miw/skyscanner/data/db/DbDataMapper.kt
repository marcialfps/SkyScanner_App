package com.miw.skyscanner.data.db

import android.util.Log
import com.miw.skyscanner.data.db.entities.AirportEntity
import com.miw.skyscanner.data.db.entities.ForecastEntity
import com.miw.skyscanner.model.Airport
import com.miw.skyscanner.model.Coordinate
import com.miw.skyscanner.model.Forecast
import com.miw.skyscanner.model.AirportForecastList

object DbDataMapper {
    fun convertToDomain(airportCode: String, airportName: String, forecasts: List<ForecastEntity>) =
        AirportForecastList(airportCode, forecasts.map { convertForecastToDomain(it) })

    private fun convertForecastToDomain(forecast: ForecastEntity) =
        Forecast(forecast.time, forecast.main, forecast.description, forecast.temperature,
        forecast.temperatureMax, forecast.temperatureMin, forecast.pressure,
        forecast.humidity, forecast.windSpeed, forecast.windDirection, forecast.cloudiness)

    fun convertForecastFromDomain(airportForecastList: AirportForecastList, airport: AirportEntity): AirportEntity {
        val forecasts = airportForecastList.forecasts.map { convertForecastFromDomain(it) }
        airport.forecasts = forecasts
        return airport
    }

    private fun convertForecastFromDomain(forecast: Forecast) =
        ForecastEntity(forecast.time, forecast.main, forecast.description, forecast.temperature,
        forecast.temperatureMax, forecast.temperatureMin, forecast.pressure,
        forecast.humidity, forecast.windSpeed, forecast.windDirection, forecast.cloudiness)

    fun convertToDomain(airport: AirportEntity) = Airport(airport.airportCode, airport.name,
        airport.city, airport.country, airport.phone, airport.postalCode, Coordinate(airport.latitude,
        airport.longitude), null)

    fun convertAirportFromDomain(airport: Airport): AirportEntity {
        Log.v("dbdatamapper", airport.toString())
        return AirportEntity(
            airport.code ?: "",
            airport.name ?: "", airport.city ?: "", airport.country ?: "",
            airport.phone ?: "", airport.postalCode ?: "",
            airport.location?.latitude ?: 0.0, airport.location?.longitude ?: 0.0
        )
    }
}
