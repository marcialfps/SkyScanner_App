package com.miw.skyscanner.ws

import android.util.Log
import com.miw.skyscanner.model.Forecast
import com.miw.skyscanner.utils.SSLConnection
import com.miw.skyscanner.utils.WSUtils
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpsTransportSE
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.lang.Double.parseDouble
import java.lang.Exception
import java.lang.Float.parseFloat
import java.lang.Integer.parseInt
import java.lang.Long.parseLong
import javax.xml.parsers.DocumentBuilderFactory

class CallWebService {
    private fun callAPI(propertiesMap: Map<String, Any>, methodName: String): SoapSerializationEnvelope {
        var result = ""
        val soapAction = WSUtils.SOAP_NAMESPACE + methodName
        val soapObject = SoapObject(WSUtils.SOAP_NAMESPACE, methodName)

        propertiesMap.forEach { k, v -> soapObject.addProperty(k, v) }

        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
        envelope.setOutputSoapObject(soapObject)
        envelope.dotNet = true

        val httpsTransportSE = HttpsTransportSE(WSUtils.SOAP_HOST,
            WSUtils.SOAP_PORT, WSUtils.SOAP_FILE, WSUtils.SOAP_TIMEOUT)

        val sslConnection = SSLConnection()
        sslConnection.allowAllSSL()

        httpsTransportSE.call(soapAction, envelope)
        return envelope
        /*val soapPrimitive = envelope.response
        result = soapPrimitive.toString()
        return result*/
    }

    private fun callAPIString(propertiesMap: Map<String, Any>, methodName: String): String {
        val envelope = callAPI(propertiesMap, methodName)
        val soapPrimitive = envelope.response
        return soapPrimitive.toString()
    }

    fun callLogin(input1: String, input2: String) =
        callAPI(mapOf("username" to input1, "password" to input2), WSUtils.METHOD_LOGIN)

    fun callRegister(username: String, name: String, surname: String, email: String,
                     airportCode: String, password: String) =
        callAPI(mapOf("username" to username, "name" to name, "surname" to surname,
            "mail" to email, "airport" to airportCode, "password" to password), WSUtils.METHOD_REGISTER)

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

    //Add a new method for each call. It must receive the params and create a map with the
    //property name in the WS.

}