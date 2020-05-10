package com.miw.skyscanner.data.datasources

import com.miw.skyscanner.model.Plane

interface PlaneDataSource {
    fun requestPlanesByAirportCode(airportCode: String, isArrivals: Boolean = true): List<Plane>?
}