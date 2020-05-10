package com.miw.skyscanner.data.server

import com.miw.skyscanner.data.datasources.AirportDataSource
import com.miw.skyscanner.data.db.AirportRepository
import com.miw.skyscanner.model.Airport
import com.miw.skyscanner.ws.CallWebService

class AirportServer : AirportDataSource {
    private val airportRepository: AirportRepository = AirportRepository()

    override fun requestAirportByAirportCode(airportCode: String): Airport? {
        val airportInfo =  CallWebService().callGetAirportByCode(airportCode)
        airportRepository.saveAirport(airportInfo)
        return airportRepository.requestAirportByAirportCode(airportCode)
    }

}