package com.example.currencycalculator.data.source.remote

import retrofit2.Response
import retrofit2.http.GET

interface CurrencyConverterService {
    @GET("latest")
    suspend fun getCurrencyRates(): Response<String>

    @GET("symbols")
    suspend fun getCurrencySymbols(): Response<String>
}