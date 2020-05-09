package com.miw.skyscanner.model

import com.miw.skyscanner.R
import java.lang.StringBuilder
import kotlin.properties.Delegates

class Plane (var planeStatus: PlaneStatus? = null, cIcao24: String? = null, var departureAirport : Airport? = null,
             var departureAirportCode: String? = null, var arrivalAirportCode: String? = null,
             var arrivalAirport: Airport? = null, var forecast: Forecast? = null,
             var departureTime: Int? = null, var arrivalTime: Int? = null, var departureDistance: Int? = null,
             var arrivalDistance: Int? = null) {

    // Keep plane status in sync with the plane
    var icao24: String? by Delegates.observable(cIcao24) { _, _, newCode ->
        planeStatus?.icao24 = newCode
    }

    override fun toString(): String {
        return "Plane(planeStatus=$planeStatus, icao24='$icao24', " +
                "departureAirport=$departureAirport, departureAirportCode=$departureAirportCode, " +
                "arrivalAirport=$arrivalAirport, arrivalAirportCode=$arrivalAirportCode, " +
                "forecast=$forecast, departureTime=$departureTime, arrivalTime=$arrivalTime, " +
                "departureDistance=$departureDistance, arrivalDistance=$arrivalDistance)"
    }
}
