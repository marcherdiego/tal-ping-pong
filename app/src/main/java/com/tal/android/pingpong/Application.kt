package com.tal.android.pingpong

import android.app.Application
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.tal.android.pingpong.networking.ServiceGenerator
import com.tal.android.pingpong.utils.SharedPreferencesUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        val sharedPreferences = SharedPreferencesUtils(this)
        ServiceGenerator.baseUrl = "https://api.tal-staging.com/"
        ServiceGenerator.httpClient = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                var request = chain.request()
                val requestBuilder = request.newBuilder()
                    .addHeader(ACCEPT_HEADER, APPLICATION_JSON_HEADER)
                sharedPreferences.getUserToken()?.let {
                    requestBuilder.addHeader(AUTHORIZATION, "Bearer $it")
                } ?: run {
                    requestBuilder.removeHeader(AUTHORIZATION)
                }
                request = requestBuilder.url(request.url()).build()
                chain.proceed(request)
            }
            .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    companion object {
        private const val ACCEPT_HEADER = "Accept"
        private const val APPLICATION_JSON_HEADER = "application/json, application/vnd.tal-retail.v2"
        private const val AUTHORIZATION = "Authorization"
    }
}