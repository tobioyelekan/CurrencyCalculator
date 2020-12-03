package com.example.currencycalculator.data.source.local

import androidx.lifecycle.LiveData
import com.example.currencycalculator.data.model.CurrencyRate
import com.example.currencycalculator.data.model.CurrencySymbol
import com.example.currencycalculator.data.source.local.dao.RateDao
import com.example.currencycalculator.data.source.local.dao.SymbolDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrencyLocalDataSourceImpl @Inject constructor(
    private val rateDao: RateDao,
    private val symbolDao: SymbolDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CurrencyLocalDataSource {

    override suspend fun saveRates(rates: List<CurrencyRate>) = withContext(ioDispatcher) {
        rateDao.saveAllRates(rates)
    }

    override suspend fun getCurrencyRateAsync() = withContext(ioDispatcher) {
        return@withContext rateDao.getCurrencyAsync()
    }

    override fun observeRate(id: String): LiveData<CurrencyRate> {
        return rateDao.getCurrency(id)
    }

    override suspend fun saveSymbols(symbols: List<CurrencySymbol>) = withContext(ioDispatcher) {
        symbolDao.saveAllSymbols(symbols)
    }

    override suspend fun getCurrencySymbolAsync() = withContext(ioDispatcher) {
        return@withContext symbolDao.getCurrencySymbolsAsync()
    }

    override fun observeSymbols(search: String): LiveData<List<CurrencySymbol>?> {
        return symbolDao.getCurrencySymbols(search)
    }

}