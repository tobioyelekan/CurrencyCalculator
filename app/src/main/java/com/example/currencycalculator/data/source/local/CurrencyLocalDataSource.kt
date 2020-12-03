package com.example.currencycalculator.data.source.local

import androidx.lifecycle.LiveData
import com.example.currencycalculator.data.model.CurrencyRate
import com.example.currencycalculator.data.model.CurrencySymbol

interface CurrencyLocalDataSource {
    suspend fun saveRates(rates: List<CurrencyRate>)

    suspend fun getCurrencyRateAsync(): List<CurrencyRate>

    fun observeRate(id: String): LiveData<CurrencyRate>

    suspend fun saveSymbols(symbols: List<CurrencySymbol>)

    suspend fun getCurrencySymbolAsync(): List<CurrencySymbol>

    fun observeSymbols(search: String): LiveData<List<CurrencySymbol>?>

}