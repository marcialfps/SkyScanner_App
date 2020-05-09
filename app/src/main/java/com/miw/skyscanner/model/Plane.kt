package com.miw.skyscanner.model

import com.miw.skyscanner.utils.ConversionHelper
import org.ksoap2.serialization.SoapObject
import java.time.LocalDateTime
import kotlin.properties.Delegates

class Plane (var planeStatus: PlaneStatus? = null, cIcao24: String? = null, var departureAirport : Airport? = null,
             var departureAirportCode: String? = null, var arrivalAirportCode: String? = null,
             var arrivalAirport: Airport? = null, var forecast: Forecast? = null,
             var departureTime: LocalDateTime? = null, var arrivalTime: LocalDateTime? = null,
             var departureDistance: Int? = null, var arrivalDistance: Int? = null) {

    // Keep plane status in sync with the plane
    var icao24: String? by Delegates.observable(cIcao24) { _, _, newCode ->
        planeStatus?.icao24 = newCode
    }

    constructor(soapPlane: SoapObject) : this() {
        planeStatus = PlaneStatus(soapPlane.getProperty("Status") as SoapObject)
        icao24 = soapPlane.getPrimitivePropertyAsString("Icao24")
        departureAirport = Airport(soapPlane.getProperty("DepartureAirport") as SoapObject)
        arrivalAirport = Airport(soapPlane.getProperty("ArrivalAirport") as SoapObject)
        departureAirportCode = soapPlane.getPrimitivePropertyAsString("DepartureAirportCode")
        arrivalAirportCode = soapPlane.getPrimitivePropertyAsString("ArrivalAirportCode")
        departureTime =
            ConversionHelper.dateFromTimestamp(
                soapPlane.getPrimitivePropertyAsString("DepartureTime").toIntOrNull())

        arrivalTime =
            ConversionHelper.dateFromTimestamp(
                soapPlane.getPrimitivePropertyAsString("ArrivalTime").toIntOrNull())

        departureDistance =
            soapPlane.getPrimitivePropertyAsString("DepartureDistance").toIntOrNull()

        arrivalDistance =
            soapPlane.getPrimitivePropertyAsString("ArrivalDistance").toIntOrNull()
    }



    override fun toString(): String {
        return "Plane(planeStatus=$planeStatus, icao24='$icao24', " +
                "departureAirport=$departureAirport, departureAirportCode=$departureAirportCode, " +
                "arrivalAirport=$arrivalAirport, arrivalAirportCode=$arrivalAirportCode, " +
                "forecast=$forecast, departureTime=$departureTime, arrivalTime=$arrivalTime, " +
                "departureDistance=$departureDistance, arrivalDistance=$arrivalDistance)"
    }
}
