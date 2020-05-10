package com.miw.skyscanner.data.datasources

import com.miw.skyscanner.model.AirportForecastList

interface ForecastDataSource {
    fun requestForecastByAirportCode(airportCode: String): AirportForecastList?
}