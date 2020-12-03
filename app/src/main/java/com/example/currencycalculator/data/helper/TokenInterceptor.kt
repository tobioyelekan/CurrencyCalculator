package com.example.currencycalculator.data.source.remote

import okhttp3.Interceptor
import okhttp3.Interceptor.*
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor(): Interceptor {
    override fun intercept(chain: Chain): Response {
        val request = chain.request()
        val httpUrl = request.url

        val url = httpUrl.newBuilder()
            .addQueryParameter("access_key", "98efa8d515e0874928adcb9c8c8255a0")
            .build()

        val requestBuilder = request.newBuilder()
            .url(url)
            .build()

        return chain.proceed(requestBuilder)
    }
}
