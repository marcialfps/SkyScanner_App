package com.miw.skyscanner.data.datasources

import com.miw.skyscanner.model.Forecast
import com.miw.skyscanner.model.ForecastList

interface ForecastDataSource {
    fun requestForecastByAirportCode(airportCode: String, date: Long): ForecastList?
}