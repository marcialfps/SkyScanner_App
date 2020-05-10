package com.miw.skyscanner.model

import com.miw.skyscanner.utils.ConversionHelper
import org.ksoap2.serialization.SoapObject
import java.time.LocalDateTime
import java.util.*
import kotlin.properties.Delegates

class Plane (var planeStatus: PlaneStatus? = null, cIcao24: String? = null, private var departureAirport : Airport? = null,
             var departureAirportCode: String? = null, var arrivalAirport: Airport? = null,
             var arrivalAirportCode: String? = null, var forecast: Forecast? = null,
             var departureTime: LocalDateTime? = null, var arrivalTime: LocalDateTime? = null,
             var departureDistance: Int? = null, var arrivalDistance: Int? = null) {

    // Keep plane status in sync with the plane
    var icao24: String? by Delegates.observable(cIcao24?.toUpperCase(Locale.ROOT))
    { _, _, newCode ->
        planeStatus?.icao24 = newCode
    }

    constructor(soapPlane: SoapObject, fullDetail: Boolean = false) : this() {
        // Fill basic properties
        icao24 = soapPlane.getPrimitivePropertyAsString("Icao24").toUpperCase(Locale.ROOT)
        if (soapPlane.hasProperty("DepartureAirportCode"))
            departureAirportCode = soapPlane.getPrimitivePropertyAsString("DepartureAirportCode")

        if (soapPlane.hasProperty("ArrivalAirportCode"))
            arrivalAirportCode = soapPlane.getPrimitivePropertyAsString("ArrivalAirportCode")

        if (soapPlane.hasProperty("DepartureTime"))
            departureTime =
                ConversionHelper.dateFromTimestamp(
                soapPlane.getPrimitivePropertyAsString("DepartureTime").toIntOrNull())


        if (soapPlane.hasProperty("ArrivalTime"))
            arrivalTime =
                ConversionHelper.dateFromTimestamp(
                    soapPlane.getPrimitivePropertyAsString("ArrivalTime").toIntOrNull())

        if (soapPlane.hasProperty("DepartureDistance"))
            departureDistance =
                soapPlane.getPrimitivePropertyAsString("DepartureDistance").toIntOrNull()

        if (soapPlane.hasProperty("DepartureDistance"))
            arrivalDistance =
                soapPlane.getPrimitivePropertyAsString("ArrivalDistance").toIntOrNull()

        // Fill complex properties if needed
        if (fullDetail) {
            planeStatus = PlaneStatus(soapPlane.getProperty("Status") as SoapObject)
            arrivalAirport = Airport(soapPlane.getProperty("ArrivalAirport") as SoapObject)
            departureAirport = Airport(soapPlane.getProperty("DepartureAirport") as SoapObject)
        }
    }



    override fun toString(): String {
        return "Plane(planeStatus=$planeStatus, icao24='$icao24', " +
                "departureAirport=$departureAirport, departureAirportCode=$departureAirportCode, " +
                "arrivalAirport=$arrivalAirport, arrivalAirportCode=$arrivalAirportCode, " +
                "forecast=$forecast, departureTime=$departureTime, arrivalTime=$arrivalTime, " +
                "departureDistance=$departureDistance, arrivalDistance=$arrivalDistance)"
    }
}
