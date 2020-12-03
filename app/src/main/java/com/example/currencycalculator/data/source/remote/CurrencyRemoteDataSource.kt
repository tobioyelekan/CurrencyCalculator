package com.example.currencycalculator.data.source.remote

import com.example.currencycalculator.data.helper.Resource
import com.example.currencycalculator.data.model.CurrencyRate
import com.example.currencycalculator.data.model.CurrencySymbol

interface CurrencyRemoteDataSource {

    suspend fun getRates(): Resource<List<CurrencyRate>>

    suspend fun getSymbols(): Resource<List<CurrencySymbol>>
}