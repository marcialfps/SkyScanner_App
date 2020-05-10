package com.miw.skyscanner.data.db

import com.miw.skyscanner.data.datasources.AirportDataSource
import com.miw.skyscanner.data.db.daos.AirportDao
import com.miw.skyscanner.model.Airport

class AirportRepository: AirportDataSource {
    private val airportDao: AirportDao? = AppDb.instance.airportDao()

    override fun requestAirportByAirportCode(airportCode: String): Airport? {
        val airport = airportDao?.getAirportByAirportCode(airportCode)
        if (airport != null) {
            return DbDataMapper.convertToDomain(airport)
        }
        return null
    }

    fun saveAirport(airport: Airport) {
        if (airportDao != null) {
            airportDao.clear()
            val airportEntity = DbDataMapper.convertAirportFromDomain(airport)
            airportDao.insert(airportEntity)
        }
    }
}