package com.miw.skyscanner.utils

import android.util.Log
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

class SSLConnection {
    private var trustManagers: Array<TrustManager>? = null

    class _FakeX509TrustManager : X509TrustManager {
        @Throws(CertificateException::class)
        override fun checkClientTrusted(
            arg0: Array<X509Certificate?>?,
            arg1: String?
        ) {
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(
            arg0: Array<X509Certificate?>?,
            arg1: String?
        ) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return _AcceptedIssuers
        }

        companion object {
            private val _AcceptedIssuers: Array<X509Certificate> = arrayOf<X509Certificate>()
        }
    }

    fun allowAllSSL() {
        HttpsURLConnection.setDefaultHostnameVerifier(object : HostnameVerifier {
            override fun verify(hostname: String?, session: SSLSession?): Boolean {
                return true
            }
        })
        val context: SSLContext
        if (trustManagers == null) {
            trustManagers = arrayOf(_FakeX509TrustManager())
        }
        try {
            context = SSLContext.getInstance("TLS")
            context.init(null, trustManagers, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(context.socketFactory)
        } catch (e: NoSuchAlgorithmException) {
            Log.e("allowAllSSL", e.toString())
        } catch (e: KeyManagementException) {
            Log.e("allowAllSSL", e.toString())
        }
    }
}