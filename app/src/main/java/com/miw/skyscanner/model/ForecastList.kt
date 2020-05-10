package com.miw.skyscanner.model

import com.miw.skyscanner.data.datasources.ForecastProvider

data class ForecastList(val airportCode: String, val airportName: String, val dailyForecast: List<Forecast>) {
    companion object {
        fun requestForecast(): ForecastList? {
            return ForecastProvider.requestForecast()
        }
    }
}