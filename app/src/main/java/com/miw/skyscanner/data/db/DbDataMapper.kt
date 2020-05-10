package com.miw.skyscanner.data.db

import com.miw.skyscanner.data.db.entities.AirportEntity
import com.miw.skyscanner.data.db.entities.ForecastEntity
import com.miw.skyscanner.model.Forecast
import com.miw.skyscanner.model.ForecastList

object DbDataMapper {
    fun convertToDomain(airportCode: String, airportName: String, forecasts: List<ForecastEntity>) =
        ForecastList(airportCode, airportName, forecasts.map { convertForecastToDomain(it) })

    private fun convertForecastToDomain(forecast: ForecastEntity) =
        Forecast(forecast.time, forecast.main, forecast.description, forecast.temperature,
        forecast.temperatureMax, forecast.temperatureMin, forecast.pressure,
        forecast.humidity, forecast.windSpeed, forecast.windDirection, forecast.cloudiness)

    fun convertFromDomain(forecastList: ForecastList): AirportEntity {
        val daily = forecastList.dailyForecast.map { convertForecastFromDomain(it) }
        val airport = AirportEntity(forecastList.airportCode, forecastList.airportName)
        airport.forecasts = daily
        return airport
    }

    private fun convertForecastFromDomain(forecast: Forecast) =
        ForecastEntity(forecast.time, forecast.main, forecast.description, forecast.temperature,
        forecast.temperatureMax, forecast.temperatureMin, forecast.pressure,
        forecast.humidity, forecast.windSpeed, forecast.windDirection, forecast.cloudiness)
}