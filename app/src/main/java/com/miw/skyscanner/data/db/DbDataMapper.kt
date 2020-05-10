package com.miw.skyscanner.data.db

import android.util.Log
import com.miw.skyscanner.data.db.entities.AirportEntity
import com.miw.skyscanner.data.db.entities.ForecastEntity
import com.miw.skyscanner.data.db.entities.PlaneEntity
import com.miw.skyscanner.model.*
import com.miw.skyscanner.utils.ConversionHelper

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

    fun convertToDomain(plane: PlaneEntity) = Plane(null, plane.icao24, null,
        plane.departureAirportCode, null, plane.arrivalAirportCode, null,
        ConversionHelper.dateFromTimestamp(plane.departureTime),
        ConversionHelper.dateFromTimestamp(plane.arrivalTime),
        plane.departureDistance, plane.departureDistance)

    fun convertPlaneFromDomain(plane: Plane): PlaneEntity {
        Log.v("dbdatamapper", plane.toString())
        return PlaneEntity(
            plane.icao24 ?: "",
            plane.departureAirportCode ?: "",
            plane.arrivalAirportCode ?: "",
            ConversionHelper.timestampFromDate(plane.departureTime) ?: 0,
            ConversionHelper.timestampFromDate(plane.arrivalTime) ?: 0,
            plane.departureDistance ?: 0,
            plane.arrivalDistance ?: 0
        )
    }
}
