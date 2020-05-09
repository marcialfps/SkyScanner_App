package com.miw.skyscanner.model

import com.miw.skyscanner.utils.ConversionHelper
import org.ksoap2.serialization.SoapObject

class PlaneStatus (var icao24: String? = null, var lastUpdate: Int? = null, var location: Coordinate? = null,
                   var altitude: Double? = null, var speed: Double? = null, var onGround:Boolean? = null,
                   var verticalRate: Double? = null, var ascending: Boolean? = null) {

    constructor(soapStatus: SoapObject) : this() {
        icao24 = soapStatus.getPrimitivePropertyAsString("Icao24")
        lastUpdate = soapStatus.getPrimitivePropertyAsString("LastUpdate").toIntOrNull()
        location = Coordinate(soapStatus.getProperty("Location") as SoapObject)
        altitude = soapStatus.getPrimitivePropertyAsString("Altitude").toDoubleOrNull()
        speed = soapStatus.getPrimitivePropertyAsString("Speed").toDoubleOrNull()
        onGround =
            ConversionHelper.toBooleanOrNull(soapStatus.getPrimitivePropertyAsString("OnGround"))
        verticalRate = soapStatus.getPrimitivePropertyAsString("VerticalRate").toDoubleOrNull()
        ascending =
            ConversionHelper.toBooleanOrNull(soapStatus.getPrimitivePropertyAsString("Ascending"))

    }

    override fun toString(): String {
        return "PlaneStatus(icao24='$icao24', lastUpdate=$lastUpdate, " +
                "location=$location, altitude=$altitude, " +
                "speed=$speed, onGround=$onGround, " +
                "verticalRate=$verticalRate, ascending=$ascending)"
    }
}