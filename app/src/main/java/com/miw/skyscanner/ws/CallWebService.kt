package com.miw.skyscanner.ws

import com.miw.skyscanner.model.Forecast
import com.miw.skyscanner.utils.SSLConnection
import com.miw.skyscanner.utils.WSUtils
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpsTransportSE
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.lang.Float.parseFloat
import java.lang.Integer.parseInt
import java.lang.Long.parseLong
import javax.xml.parsers.DocumentBuilderFactory

class CallWebService {
    private fun callAPI(propertiesMap: Map<String, Any>, methodName: String): String {
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
        val soapPrimitive = envelope.response
        result = soapPrimitive.toString()
        return result
    }

    fun callLogin(input1: String, input2: String) =
        callAPI(mapOf("username" to input1, "password" to input2), WSUtils.METHOD_LOGIN)

    fun callRegister(username: String, name: String, surname: String, email: String,
                     airportCode: String, password: String) =
        callAPI(mapOf("username" to username, "name" to name, "surname" to surname,
            "mail" to email, "airport" to airportCode, "password" to password), WSUtils.METHOD_REGISTER)

    fun callCurrentWeather(airportCode: String): Forecast {
        val response = callAPI(mapOf("aiportCode" to airportCode),
            WSUtils.METHOD_GET_WEATHER_BY_AIRPORT)
        val xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(response)
        val forecastResponse = xmlDoc.getElementsByTagName("GetWeatherByAirportResult").item(0)

        with (forecastResponse as Element) {
            return convertToForecast(forecastResponse)
        }
    }

    fun callGetForecast(airportCode: String, time: Long): List<Forecast> {
        val forecasts = mutableListOf<Forecast>()
        val response = callAPI(mapOf("aiportCode" to airportCode, "time" to time),
            WSUtils.METHOD_GET_WEATHER_BY_AIRPORT)
        val xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(response)
        val forecastList = xmlDoc.getElementsByTagName("GetWeatherByAirportResult")

        for (i in 0 until forecastList.length) {
            val forecastNode = forecastList.item(i)
            if (forecastNode.nodeType == Node.ELEMENT_NODE) {
                with(forecastNode as Element) {
                    forecasts.add(convertToForecast(forecastNode))
                }
            }
        }
        return forecasts.toList()
    }

    private fun convertToForecast(element: Element): Forecast {
        val forecast = Forecast()
        forecast.time = parseLong(element.getElementsByTagName("Time").item(0).textContent)
        forecast.main = element.getElementsByTagName("Main").item(0).textContent
        forecast.description = element.getElementsByTagName("Description").item(0).textContent
        forecast.temperature = parseFloat(element.getElementsByTagName("Temperature").item(0).textContent)
        forecast.temperatureMax = parseFloat(element.getElementsByTagName("TemperatureMax").item(0).textContent)
        forecast.temperatureMin = parseFloat(element.getElementsByTagName("TemperatureMin").item(0).textContent)
        forecast.pressure = parseInt(element.getElementsByTagName("Pressure").item(0).textContent)
        forecast.humidity = parseInt(element.getElementsByTagName("Humidity").item(0).textContent)
        forecast.windSpeed = parseFloat(element.getElementsByTagName("WindSpeed").item(0).textContent)
        forecast.windDirection = parseFloat(element.getElementsByTagName("WindDirection").item(0).textContent)
        forecast.cloudiness = parseInt(element.getElementsByTagName("Cloudiness").item(0).textContent)
        return forecast
    }

    //Add a new method for each call. It must receive the params and create a map with the
    //property name in the WS.

}