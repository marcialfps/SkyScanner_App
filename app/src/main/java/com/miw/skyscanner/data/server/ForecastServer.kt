package com.miw.skyscanner.data.server

import com.miw.skyscanner.data.datasources.ForecastDataSource
import com.miw.skyscanner.data.db.AirportRepository
import com.miw.skyscanner.data.db.ForecastRepository
import com.miw.skyscanner.model.AirportForecastList
import com.miw.skyscanner.ws.CallWebService

class ForecastServer() : ForecastDataSource {

    private val forecastRepository: ForecastRepository = ForecastRepository()
    private val airportRepository: AirportRepository = AirportRepository()

    override fun requestForecastByAirportCode(airportCode: String): AirportForecastList? {
        val forecasts = CallWebService().callWeather(airportCode)
        val airportInfo = AirportForecastList.requestAirportInfo(airportCode)
        if (airportInfo != null) {
            val converted = ServerDataMapper.convertToDomain(airportCode, forecasts)
            if (converted != null) {
                forecastRepository.saveForecast(converted)
            }
        }
        return forecastRepository.requestForecastByAirportCode(airportCode);
    }

}