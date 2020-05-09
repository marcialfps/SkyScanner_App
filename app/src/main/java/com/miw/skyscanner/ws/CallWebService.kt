package com.miw.skyscanner.ws

import android.util.Log
import com.miw.skyscanner.model.*
import com.miw.skyscanner.utils.SSLConnection
import com.miw.skyscanner.utils.WSUtils
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpsTransportSE
import java.lang.Double.parseDouble
import java.lang.Integer.parseInt
import java.lang.Long.parseLong

class CallWebService {
    private fun callAPI(propertiesMap: Map<String, Any>, methodName: String): SoapSerializationEnvelope {
        val soapAction = WSUtils.SOAP_NAMESPACE + methodName
        val soapObject = SoapObject(WSUtils.SOAP_NAMESPACE, methodName)

        propertiesMap.forEach { (k, v) -> soapObject.addProperty(k, v) }

        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
        envelope.setOutputSoapObject(soapObject)
        envelope.dotNet = true

        val httpsTransportSE = HttpsTransportSE(WSUtils.SOAP_HOST,
            WSUtils.SOAP_PORT, WSUtils.SOAP_FILE, WSUtils.SOAP_TIMEOUT)

        val sslConnection = SSLConnection()
        sslConnection.allowAllSSL()

        httpsTransportSE.call(soapAction, envelope)
        return envelope
    }

    fun callLogin(username: String, password: String): User {
        val envelope = callAPI(mapOf("username" to username, "password" to password), WSUtils.METHOD_LOGIN)
        val response = envelope.response as SoapObject
        val user = User()
        with (response) {
            user.username = getPrimitivePropertyAsString("Username")
            user.name = getPrimitivePropertyAsString("Name")
            user.surname = getPrimitivePropertyAsString("Surname")
            user.password = getPrimitivePropertyAsString("Password")
            user.email = getPrimitivePropertyAsString("Mail")
            user.airportCode = getPrimitivePropertyAsString("Airport")
        }
        return user
    }

    fun callRegister(username: String, name: String, surname: String, email: String,
                     airportCode: String, password: String): User {
        val envelope = callAPI(
            mapOf(
                "username" to username, "name" to name, "surname" to surname,
                "mail" to email, "airport" to airportCode, "password" to password
            ), WSUtils.METHOD_REGISTER
        )
        val response = envelope.response as SoapObject
        val user = User()
        with (response) {
            user.username = getPrimitivePropertyAsString("Username")
            user.name = getPrimitivePropertyAsString("Name")
            user.surname = getPrimitivePropertyAsString("Surname")
            user.password = getPrimitivePropertyAsString("Password")
            user.email = getPrimitivePropertyAsString("Mail")
            user.airportCode = getPrimitivePropertyAsString("Airport")
        }
        return user
    }

    fun callCurrentWeather(airportCode: String) : Forecast {
        val envelope = callAPI(mapOf("airportCode" to airportCode),
            WSUtils.METHOD_GET_WEATHER_BY_AIRPORT)
        val response: SoapObject = envelope.response as SoapObject
        return convertToForecast(response)
    }

    fun callWeather(airportCode: String): List<Forecast> {
        val envelope = callAPI(mapOf("airportCode" to airportCode),
            WSUtils.METHOD_GET_WEATHER_FORECAST_BY_AIRPORT)
        val response: SoapObject = envelope.response as SoapObject
        val forecastCount = response.propertyCount
        Log.v("response", forecastCount.toString())

        val forecasts = mutableListOf<Forecast>()

        for (i in 0 until forecastCount) {
            var soapWeather: SoapObject = response.getProperty(i) as SoapObject
            forecasts.add(convertToForecast(soapWeather))
        }

        return forecasts.toList()
    }

    private fun convertToForecast(soapObject: SoapObject): Forecast {
        val forecast = Forecast()
        with(forecast) {
            time = parseLong(soapObject.getPrimitivePropertyAsString("Time"))
            main = soapObject.getPrimitivePropertyAsString("Main")
            description = soapObject.getPrimitivePropertyAsString("Description")
            temperature = parseDouble(soapObject.getPrimitivePropertyAsString("Temperature"))
            temperatureMax = parseDouble(soapObject.getPrimitivePropertyAsString("TemperatureMax"))
            temperatureMin = parseDouble(soapObject.getPrimitivePropertyAsString("TemperatureMin"))
            pressure = parseInt(soapObject.getPrimitivePropertyAsString("Pressure"))
            humidity = parseInt(soapObject.getPrimitivePropertyAsString("Humidity"))
            windSpeed = parseDouble(soapObject.getPrimitivePropertyAsString("WindSpeed"))
            windDirection = parseDouble(soapObject.getPrimitivePropertyAsString("WindDirection"))
            cloudiness = parseInt(soapObject.getPrimitivePropertyAsString("Cloudiness"))
        }
        return forecast
    }

    // Airports and planes
    private fun callAPIPlanesClose(propertiesMap: Map<String, Any>): List<Plane> {
        val envelope = callAPI(propertiesMap, WSUtils.METHOD_PLANES_CLOSE_TO_AIRPORT)
        val response: SoapObject = envelope.response as SoapObject
        val planesCount = response.propertyCount

        val planes = mutableListOf<Plane>()

        for (i in 0 until planesCount) {
            val soapPlane: SoapObject = response.getProperty(i) as SoapObject
            val plane = Plane()

            // The API only returns the planes' statuses and other limited info
            with (plane){
                planeStatus =
                    PlaneStatus(soapPlane.getProperty("Status") as SoapObject)
                departureTime =
                    soapPlane.getPrimitivePropertyAsString("DepartureTime").toIntOrNull()
                arrivalTime =
                    soapPlane.getPrimitivePropertyAsString("ArrivalTime").toIntOrNull()
                departureDistance =
                    soapPlane.getPrimitivePropertyAsString("DepartureDistance").toIntOrNull()
                arrivalDistance =
                    soapPlane.getPrimitivePropertyAsString("ArrivalDistance").toIntOrNull()
            }
            planes.add(plane)
        }
        return planes.toList()
    }

    fun callGetPlanesClose(airportCode: String): List<Plane> {
        return callAPIPlanesClose(
            mapOf("airportCode" to airportCode)
        )
    }

    private fun callAPIAirportByCode(propertiesMap: Map<String, Any>): Airport {
        val envelope = callAPI(propertiesMap, WSUtils.METHOD_AIRPORT_BY_CODE)
        val response: SoapObject = envelope.response as SoapObject

        return Airport(response)
    }

    fun callGetAirportByCode(airportCode: String): Airport {
        return callAPIAirportByCode(
            mapOf("airportCode" to airportCode)
        )
    }

    //Add a new method for each call. It must receive the params and create a map with the
    //property name in the WS.

}