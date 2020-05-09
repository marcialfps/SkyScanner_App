package com.miw.skyscanner.utils

import android.annotation.SuppressLint
import android.util.Log
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

// accept proprietary SSL certificates
@SuppressLint("TrustAllX509TrustManager", "BadHostnameVerifier")
class SSLConnection {
    private var trustManagers: Array<TrustManager>? = null

    class FakeX509TrustManager : X509TrustManager {
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
            return AcceptedIssuers
        }

        companion object {
            private val AcceptedIssuers: Array<X509Certificate> = arrayOf()
        }
    }

    fun allowAllSSL() {
        HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true }
        val context: SSLContext
        if (trustManagers == null) {
            trustManagers = arrayOf(FakeX509TrustManager())
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