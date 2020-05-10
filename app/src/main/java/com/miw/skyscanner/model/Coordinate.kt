package com.miw.skyscanner.model

import com.google.android.gms.maps.model.LatLng
import org.ksoap2.serialization.SoapObject

class Coordinate (cLatitude: Double?, cLongitude: Double?) {

    // Prevent invalid values
    var latitude: Double? = 0.0
        set(value) {
            field = if (value == null || value < -90 || value > 90) 0.0
            else value
        }

    var longitude: Double? = 0.0
        set(value) {
            field = if (value == null || value < -180 || value > 180) 0.0
            else value
        }

    init {
        latitude = cLatitude
        longitude = cLongitude
    }

    fun getLatLng(): LatLng {
        return LatLng(latitude!!, longitude!!)
    }

    constructor(soapCoordinate: SoapObject) : this(null, null) {
        latitude = soapCoordinate.getPrimitivePropertyAsString("Latitude").toDoubleOrNull()
        longitude = soapCoordinate.getPrimitivePropertyAsString("Longitude").toDoubleOrNull()
    }


    override fun toString(): String {
        return "Coordinate =>\n\tLatitude: $latitude, Longitude: $longitude"
    }

}