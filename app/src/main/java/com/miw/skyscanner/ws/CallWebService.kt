package com.miw.skyscanner.ws

import com.miw.skyscanner.model.Airport
import com.miw.skyscanner.model.Coordinate
import com.miw.skyscanner.model.Plane
import com.miw.skyscanner.model.PlaneStatus
import com.miw.skyscanner.utils.SSLConnection
import com.miw.skyscanner.utils.WSUtils
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpsTransportSE
import org.w3c.dom.Element
import org.w3c.dom.Node
import javax.xml.parsers.DocumentBuilderFactory

// https://ec2-18-222-182-217.us-east-2.compute.amazonaws.com:8080/SkyScannerWS/SkyScannerWS.asmx?WSDL
class CallWebService {
    private fun callAPI(propertiesMap: Map<String, Any>, methodName: String): String {
        val result: String
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


    // Planes & Airports
    fun callPlanesCloseToAirport(airportCode: String): List<Plane> {
        val planes = mutableListOf<Plane>()
        val response = callAPI(mapOf("airportCode" to airportCode),
            WSUtils.METHOD_PLANES_CLOSE_TO_AIRPORT)

        val xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(response)
        val planeNodes = xmlDoc.
            getElementsByTagName("GetPlanesCloseToAirportResult")

        for (i in 0 until planeNodes.length) {
            val planeNode = planeNodes.item(i)
            if (planeNode.nodeType == Node.ELEMENT_NODE) {
                with(planeNode as Element) {
                    planes.add(convertToPlane(planeNode))
                }
            }
        }
        return planes.toList()
    }


    //Add a new method for each call. It must receive the params and create a map with the
    //property name in the WS.


    private fun convertToPlane(element: Element): Plane {
        val plane = Plane()
        plane.planeStatus = convertToPlaneStatus(
            element.getElementsByTagName("Status").item(0) as Element
        )
        plane.icao24 = element.getElementsByTagName("Icao24").item(0).textContent
        plane.departureAirport = convertToAirport(
            element.getElementsByTagName("DepartureAirport").item(0) as Element
        )
        plane.arrivalAirport = convertToAirport(
            element.getElementsByTagName("ArrivalAirport").item(0) as Element
        )
        plane.departureAirportCode = element.getElementsByTagName("DepartureAirportCode").item(0).textContent
        plane.arrivalAirportCode = element.getElementsByTagName("ArrivalAirportCode").item(0).textContent
        plane.forecast = null // TODO use convertToForecast

        return plane
    }

    private fun convertToPlaneStatus (element: Element): PlaneStatus {
        val status = PlaneStatus()
        status.icao24 = element.getElementsByTagName("Icao24").item(0).textContent
        status.lastUpdate =
            element.getElementsByTagName("LastUpdate").item(0).textContent.toIntOrNull()
        status.location = convertToCoordinate(
            element.getElementsByTagName("Location").item(0) as Element
        )
        status.altitude = element.getElementsByTagName("Altitude").item(0).textContent.toDoubleOrNull()
        status.speed = element.getElementsByTagName("Speed").item(0).textContent.toDoubleOrNull()
        status.onGround = ConversionHelper
            .toBooleanOrNull(element.getElementsByTagName("OnGround").item(0).textContent)
        status.verticalRate = element.getElementsByTagName("VerticalRate").item(0).textContent.toDoubleOrNull()
        status.ascending = ConversionHelper
            .toBooleanOrNull(element.getElementsByTagName("Ascending").item(0).textContent)

        return status
    }

    private fun convertToCoordinate (element: Element): Coordinate {
        val coordinate = Coordinate(0.0,0.0)
        coordinate.latitude = ConversionHelper.toDoubleOrDefault(
            element.getElementsByTagName("Latitude").item(0).textContent, 0.0)
        coordinate.longitude = ConversionHelper.toDoubleOrDefault(
            element.getElementsByTagName("Longitude").item(0).textContent, 0.0)

        return coordinate
    }

    private fun convertToAirport (element: Element): Airport {
        val airport = Airport()
        airport.code = element.getElementsByTagName("Code").item(0).textContent
        airport.name = element.getElementsByTagName("Name").item(0).textContent
        airport.city = element.getElementsByTagName("City").item(0).textContent
        airport.country = element.getElementsByTagName("Country").item(0).textContent
        airport.phone = element.getElementsByTagName("Phone").item(0).textContent
        airport.postalCode = element.getElementsByTagName("PostalCode").item(0).textContent
        airport.location = convertToCoordinate(
            element.getElementsByTagName("Location").item(0) as Element
        )
        airport.forecast = null // TODO use convertToForecast

        return airport
    }


    class ConversionHelper {
        companion object {
            fun toIntOrDefault (input: String, default: Int): Int {
                return when (val conversion: Int? = input.trim().toIntOrNull()) {
                    null -> default
                    else -> conversion
                }
            }
            fun toDoubleOrDefault (input: String, default: Double): Double {
                return when (val conversion: Double? = input.trim().toDoubleOrNull()) {
                    null -> default
                    else -> conversion
                }
            }
            fun toBooleanOrNull (input: String): Boolean? {
                return when (input.trim().toBoolean()){
                    true -> true
                    input.equals("false", ignoreCase = true) -> false
                    else -> null
                }
            }
        }
    }

}