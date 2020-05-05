package com.miw.skyscanner.ws

import com.miw.skyscanner.utils.SSLConnection
import com.miw.skyscanner.utils.WSUtils
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpsTransportSE
import java.lang.Exception

class CallWebService {
    fun callLogin(input1: String?, input2: String?) : String {
        var result = ""
        val soapAction = WSUtils.SOAP_NAMESPACE + WSUtils.METHOD_LOGIN
        val soapObject = SoapObject(WSUtils.SOAP_NAMESPACE, WSUtils.METHOD_LOGIN)

        soapObject.addProperty("username", input1)
        soapObject.addProperty("password", input1)

        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
        envelope.setOutputSoapObject(soapObject)
        envelope.dotNet = true

        val httpsTransportSE = HttpsTransportSE(WSUtils.SOAP_HOST,
            WSUtils.SOAP_PORT, WSUtils.SOAP_FILE, WSUtils.SOAP_TIMEOUT)

        val sslConnection = SSLConnection()
        sslConnection.allowAllSSL()

        try {
            httpsTransportSE.call(soapAction, envelope)
            val soapPrimitive = envelope.response
            result = soapPrimitive.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }
}