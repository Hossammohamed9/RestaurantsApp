package com.example.task.util

import android.annotation.SuppressLint
import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.OkHttpClient
import java.io.InputStream
import java.lang.Exception
import java.lang.RuntimeException
import java.security.cert.CertificateException
import javax.net.ssl.*


@GlideModule
class MyGlideModule : AppGlideModule() {


    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val okHttpClient: OkHttpClient = getunsafeOkHttpClient().build()
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(okHttpClient))
    }

    private fun getunsafeOkHttpClient(): OkHttpClient.Builder{
        try {
            val trustAllCerts = arrayOf<TrustManager>(@SuppressLint("CustomX509TrustManager")
            object : X509TrustManager {
                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                } // Noncompliant (s4830)

                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                } // Noncompliant (s4830)

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                    return arrayOf()
                }
            })

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            val sslSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { hostname, session -> true }
            return builder
        }catch (e: Exception){
            throw RuntimeException(e)
        }
    }
}