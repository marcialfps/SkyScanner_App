package com.miw.skyscanner.model

import org.ksoap2.serialization.SoapObject

class Forecast(var time: Long = 0,  var main: String = "", var description: String = "",
               var temperature: Double = 0.0, var temperatureMax: Double = 0.0,
               var temperatureMin: Double = 0.0, var pressure: Int = 0, var humidity: Int = 0,
               var windSpeed: Double = 0.0, var windDirection: Double = 0.0, var cloudiness: Int = 0
)
{
    constructor(soapObject: SoapObject) : this() {
        time = java.lang.Long.parseLong(soapObject.getPrimitivePropertyAsString("Time"))
        main = soapObject.getPrimitivePropertyAsString("Main")
        description = soapObject.getPrimitivePropertyAsString("Description")
        temperature = soapObject.getPrimitivePropertyAsString("Temperature").toDouble()
        temperatureMax = soapObject.getPrimitivePropertyAsString("TemperatureMax").toDouble()
        temperatureMin = soapObject.getPrimitivePropertyAsString("TemperatureMin").toDouble()
        pressure = soapObject.getPrimitivePropertyAsString("Pressure").toInt()
        humidity = soapObject.getPrimitivePropertyAsString("Humidity").toInt()
        windSpeed = soapObject.getPrimitivePropertyAsString("WindSpeed").toDouble()
        windDirection = soapObject.getPrimitivePropertyAsString("WindDirection").toDouble()
        cloudiness = soapObject.getPrimitivePropertyAsString("Cloudiness").toInt()
    }

}

