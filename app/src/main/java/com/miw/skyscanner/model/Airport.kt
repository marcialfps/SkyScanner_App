package com.miw.skyscanner.model

import com.miw.skyscanner.utils.ConversionHelper
import org.ksoap2.serialization.SoapObject

class Airport (
    var code: String? = null, var name: String? = null, var city: String? = null, var country: String? = null,
    var phone: String? = null, var postalCode: String? = null, var location: Coordinate? = null,
    var forecast: Forecast? = null) {

    constructor(soapAirport: SoapObject) : this() {
        code = soapAirport.getPrimitivePropertyAsString("Code")
        name = soapAirport.getPrimitivePropertyAsString("Name")
        city = soapAirport.getPrimitivePropertyAsString("City")
        country = soapAirport.getPrimitivePropertyAsString("Country")
        phone = soapAirport.getPrimitivePropertyAsString("Phone")
        postalCode = soapAirport.getPrimitivePropertyAsString("PostalCode")
        location = Coordinate(soapAirport.getProperty("Location") as SoapObject)
//        forecast = Forecast(soapAirport.getProperty("Weather") as SoapObject)
    }

    override fun toString(): String {
        return "Airport(code=$code, name=$name, " +
                "city=$city, country=$country, " +
                "phone=$phone, postalCode=$postalCode, " +
                "location=$location, forecast=$forecast)"
    }


}