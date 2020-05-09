package com.miw.skyscanner.ws

import com.miw.skyscanner.utils.SSLConnection
import com.miw.skyscanner.utils.WSUtils
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpsTransportSE

// https://ec2-18-222-182-217.us-east-2.compute.amazonaws.com:8080/SkyScannerWS/SkyScannerWS.asmx?WSDL
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





    //Add a new method for each call. It must receive the params and create a map with the
    //property name in the WS.





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