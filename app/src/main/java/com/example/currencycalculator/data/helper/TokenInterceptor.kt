package com.example.currencycalculator.data.helper

import com.example.currencycalculator.BuildConfig
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
            .addQueryParameter("access_key", BuildConfig.API_KEY)
            .build()

        val requestBuilder = request.newBuilder()
            .url(url)
            .build()

        return chain.proceed(requestBuilder)
    }
}
