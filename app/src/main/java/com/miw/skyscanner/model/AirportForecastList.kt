package com.miw.skyscanner.model

import com.miw.skyscanner.data.datasources.DataProvider

data class AirportForecastList(val airportCode: String, val forecasts: List<Forecast>) {
    companion object {
        fun requestForecast(airportCode: String): AirportForecastList? {
            return DataProvider.requestForecastByAirportCode(airportCode)
        }

        fun requestCurrentWeather(airportCode: String): Forecast? {
            return DataProvider.requestCurrentForecastByAirportCode(airportCode)
        }

        fun requestAirportInfo(airportCode: String): Airport? {
            return DataProvider.requestAirportByAirportCode(airportCode)
        }
    }
}