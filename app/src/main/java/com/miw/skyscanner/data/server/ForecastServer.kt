package com.miw.skyscanner.data.server

import com.miw.skyscanner.data.datasources.ForecastDataSource
import com.miw.skyscanner.model.Forecast
import com.miw.skyscanner.model.ForecastList
import com.miw.skyscanner.ws.CallWebService

class ForecastServer() : ForecastDataSource {
    override fun requestForecastByAirportCode(airportCode: String, date: Long): ForecastList? {
        CallWebService().callWeather(airportCode)
    }

}