package com.example.currencycalculator.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.currencycalculator.data.helper.Resource
import com.example.currencycalculator.data.helper.Resource.Status
import com.example.currencycalculator.data.model.CurrencyRate
import com.example.currencycalculator.data.model.CurrencySymbol
import com.example.currencycalculator.data.source.repo.CurrencyRepository
import com.example.currencycalculator.util.TestObjectUtil

class FakeCurrencyRepository : CurrencyRepository {

    private var shouldReturnError = false

    private val symbols = mutableListOf<CurrencySymbol>()
    private val rates = mutableListOf<CurrencyRate>()

    private val observableSymbols = MutableLiveData<List<CurrencySymbol>>()

    private val observableCurrencyRate = MutableLiveData<CurrencyRate>()

    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override fun fetchRateSymbols(): LiveData<Resource<Unit>> {

        return liveData {
            emit(Resource.loading())

            if (symbols.isEmpty() || rates.isEmpty()) { //check if data is available
                val response = callNetwork()
                when (response.status) {
                    Status.SUCCESS -> emit(Resource.success(Unit))
                    Status.ERROR -> emit(Resource.error(response.message!!, null))
                    else -> {
                    }
                }
            } else {
                emit(Resource.success(Unit))
            }
        }
    }


    override suspend fun callNetwork(): Resource<String> {
        return if (shouldReturnError) {
            Resource.error("error occurred", null)
        } else {
            saveRates(TestObjectUtil.rates)
            saveSymbols(TestObjectUtil.symbols)
            Resource.success("success")
        }
    }

    override fun searchSymbol(search: String): LiveData<List<CurrencySymbol>> {
        filterSymbols(search)
        return observableSymbols
    }

    private fun filterSymbols(search: String) {
        val symbols = if (search.isEmpty()) symbols
        else symbols.filter { data ->
            data.currency.contains(
                search,
                ignoreCase = true
            ) || data.name.contains(
                search,
                ignoreCase = true
            )
        }

        observableSymbols.value = symbols
    }

    override suspend fun getCurrencyRates() = rates

    override suspend fun getCurrencySymbols() = symbols

    override fun getCurrency(id: String): LiveData<CurrencyRate> {
        findCurrency(id)
        return observableCurrencyRate
    }

    override suspend fun getSingleCurrencyRate(id: String): CurrencyRate {
        return rates.find { it.currency == id }!!
    }

    private fun findCurrency(id: String) {
        observableCurrencyRate.value = rates.find { it.currency == id }!!
    }

    override suspend fun saveRates(rates: List<CurrencyRate>) {
        this.rates.addAll(rates)
    }

    override suspend fun saveSymbols(symbols: List<CurrencySymbol>) {
        this.symbols.addAll(symbols)
        refreshData()
    }

    private fun refreshData() {
        observableSymbols.value = symbols
    }

}