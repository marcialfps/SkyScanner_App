package com.miw.skyscanner.data.server

import com.miw.skyscanner.model.Forecast
import com.miw.skyscanner.model.AirportForecastList

object ServerDataMapper {
    fun convertToDomain(airportCode: String, forecasts: List<Forecast>) =
        AirportForecastList(airportCode, forecasts)
}