package com.miw.skyscanner.data.datasources

import com.miw.skyscanner.model.Airport

interface AirportDataSource {
    fun requestAirportByAirportCode(airportCode: String): Airport?
}