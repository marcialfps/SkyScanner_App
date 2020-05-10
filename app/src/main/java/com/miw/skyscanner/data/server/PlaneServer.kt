package com.miw.skyscanner.data.server

import com.miw.skyscanner.data.datasources.PlaneDataSource
import com.miw.skyscanner.data.db.PlaneRepository
import com.miw.skyscanner.model.Plane
import com.miw.skyscanner.utils.PlaneComparator
import com.miw.skyscanner.ws.CallWebService
import java.time.LocalDateTime

class PlaneServer() : PlaneDataSource {
    private val planeRepository: PlaneRepository = PlaneRepository()
    private val currentTime = LocalDateTime.now()

    override fun requestPlanesByAirportCode(airportCode: String, isArrivals:Boolean): List<Plane>? {
        val planesInfo =
            CallWebService().callGetPlanesByAirport(isArrivals, airportCode)
            // Filter planes with valid data
            .filter {
                it.arrivalAirportCode != null && it.departureAirportCode != null &&
                        it.arrivalTime != null && it.departureTime != null &&
                        it.arrivalDistance != null && it.departureDistance != null
            }
            // The API returns flights 2 days old. Alter the date on purpose
            // to show flights today or tomorrow and be more realistic
            .map {
                if (isArrivals) it.arrivalTime = it.arrivalTime?.plusDays((1..2).random().toLong())
                else it.departureTime = it.departureTime?.plusDays((1..2).random().toLong())
                it
            }
            // Show only present and future flights
            .filter {
                if (isArrivals) it.arrivalTime!!.isAfter(currentTime)
                else it.departureTime!!.isAfter(currentTime)
            }
            // Sort flights by date
            .sortedWith(PlaneComparator(isArrivals))

        planesInfo.forEach{ planeRepository.savePlane(it) }
        return planeRepository.requestPlanesByAirportCode(airportCode, isArrivals)
    }

}