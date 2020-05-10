package com.miw.skyscanner.data.db

import com.miw.skyscanner.data.datasources.ForecastDataSource
import com.miw.skyscanner.data.db.daos.AirportDao
import com.miw.skyscanner.data.db.daos.ForecastDao
import com.miw.skyscanner.model.AirportForecastList

class ForecastRepository: ForecastDataSource {
    private val forecastDao: ForecastDao? = AppDb.instance.forecastDao()
    private val airportDao: AirportDao? = AppDb.instance.airportDao()

    override fun requestForecastByAirportCode(airportCode: String): AirportForecastList? {
        val airport = airportDao?.getAirportByAirportCode(airportCode)
        if (airport != null) {
            val forecasts = forecastDao?.getForecastByAirportCode(airport.airportCode)
            if (forecasts != null && forecasts.isNotEmpty()) {
                return DbDataMapper.convertToDomain(airportCode, airport.name, forecasts)
            }
        }
        return null
    }

    fun saveForecast(airportForecastList: AirportForecastList) {
        if (airportDao != null && forecastDao != null) {
            val airport = airportDao.getAirportByAirportCode(airportForecastList.airportCode)
            airportDao.clear()
            AirportRepository().saveAirport(DbDataMapper.convertToDomain(
                DbDataMapper.convertForecastFromDomain(airportForecastList, airport)))
            airport.forecasts.forEach {
                it.airportIdCode = airport.airportCode
                forecastDao.insert(it)
            }
        }
    }
}