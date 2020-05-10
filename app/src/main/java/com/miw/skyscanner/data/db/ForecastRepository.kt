package com.miw.skyscanner.data.db

import com.miw.skyscanner.data.datasources.ForecastDataSource
import com.miw.skyscanner.data.db.daos.AirportDao
import com.miw.skyscanner.data.db.daos.ForecastDao
import com.miw.skyscanner.model.ForecastList
import java.util.*

class ForecastRepository: ForecastDataSource {
    private val forecastDao: ForecastDao? = ForecastDb.instance.forecastDao()
    private val airportDao: AirportDao? = ForecastDb.instance.airportDao()

    override fun requestForecastByAirportCode(airportCode: String, date: Long): ForecastList? {
        val airport = airportDao?.getAirportByAirportCode(airportCode)
        if (airport != null) {
            val cal = Calendar.getInstance()
            cal.timeInMillis = date
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            val timeInMillis = cal.timeInMillis
            val airportId = airport.airportCode
            val forecasts = forecastDao?.getForecastByAirportCodeAndDate(airportId, timeInMillis)
            if (forecasts != null) {
                return DbDataMapper.convertToDomain(airportCode, airport.name, forecasts)
            }
        }
        return null
    }

    fun saveForecast(forecastList: ForecastList) {
        if (airportDao != null && forecastDao != null) {
            airportDao.clear()

            val airport = DbDataMapper.convertFromDomain(forecastList)
            val airportId = airportDao.insert(airport)
            airport.forecasts.forEach {
                it.airportId = airportId
                forecastDao.insert(it)
            }
        }
    }
}