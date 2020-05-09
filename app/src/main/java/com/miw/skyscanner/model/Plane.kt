package com.miw.skyscanner.model

class Plane (var planeStatus: PlaneStatus? = null, var icao24: String? = null, var departureAirport : Airport? = null,
             var departureAirportCode: String? = null, var arrivalAirportCode: String? = null,
             var arrivalAirport: Airport? = null, var forecast: String? = null,
             var departureTime: Int? = null, var arrivalTime: Int? = null, var departureDistance: Int? = null,
             var arrivalDistance: Int? = null) {

    // TODO forecast should be Forecast type


    override fun toString(): String {
        return "Plane(planeStatus=$planeStatus, icao24='$icao24', " +
                "departureAirport=$departureAirport, departureAirportCode=$departureAirportCode, " +
                "arrivalAirport=$arrivalAirport, arrivalAirportCode=$arrivalAirportCode, " +
                "forecast=$forecast, departureTime=$departureTime, arrivalTime=$arrivalTime, " +
                "departureDistance=$departureDistance, arrivalDistance=$arrivalDistance)"
    }
}