package com.example.currencycalculator.data.source.repo

import androidx.lifecycle.LiveData
import com.example.currencycalculator.data.helper.Resource
import com.example.currencycalculator.data.model.CurrencyRate
import com.example.currencycalculator.data.model.CurrencySymbol

interface CurrencyRepository {

    fun fetchRateSymbols(): LiveData<Resource<Unit>>

    suspend fun callNetwork(): Resource<String>

    fun searchSymbol(search: String): LiveData<List<CurrencySymbol>?>

    suspend fun getCurrencyRates(): List<CurrencyRate>

    suspend fun getCurrencySymbols(): List<CurrencySymbol>

    fun getCurrency(id: String): LiveData<CurrencyRate>

    suspend fun saveRates(rates: List<CurrencyRate>)

    suspend fun saveSymbols(symbols: List<CurrencySymbol>)

}