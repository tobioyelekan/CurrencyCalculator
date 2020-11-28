package com.example.currencycalculator.repo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.currencycalculator.data.helper.Resource
import com.example.currencycalculator.data.helper.toCurrencyRateList
import com.example.currencycalculator.data.helper.toCurrencySymbolList
import com.example.currencycalculator.data.local.dao.CurrencyRateDao
import com.example.currencycalculator.data.local.dao.CurrencySymbolDao
import com.example.currencycalculator.data.model.CurrencyRate
import com.example.currencycalculator.data.model.CurrencySymbol
import com.example.currencycalculator.data.remote.CurrencyConverterService
import com.example.currencycalculator.util.WorkerClass
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyConverterRepo @Inject constructor(
    private val currencyConverterService: CurrencyConverterService,
    private val rateDao: CurrencyRateDao,
    private val symbolDao: CurrencySymbolDao,
    @ApplicationContext private val context: Context
) {

    private suspend fun getRates() = currencyConverterService.getCurrencyRates()

    private suspend fun getSymbols() = currencyConverterService.getCurrencySymbols()

    val currencyRates = rateDao.getCurrencies()

    fun searchSymbol(search: String) = symbolDao.getCurrencySymbols(search)

    suspend fun getCurrencyRate() = rateDao.getCurrencyAsync()

    private suspend fun getCurrencySymbol() = symbolDao.getCurrencySymbolsAsync()

    fun getCurrency(id: String) = rateDao.getCurrency(id)

    private suspend fun saveRates(rates: List<CurrencyRate>) {
        rateDao.saveAllRates(rates)
    }

    private suspend fun saveSymbols(symbols: List<CurrencySymbol>) {
        symbolDao.saveAllSymbols(symbols)
    }

    /**
     * If the data is available, we handover the network task to
     * a workmanager class to ensure it finishes without canceling
     *
     * If the cache is empty then we wait for the data to load
     * and store them
     *
     * This implementation is to ensure that at least we have
     * cached data i.e the list of currencies and their rates
     * which is stored with room (currency_rate and symbol_table)
     * and at every point in time the app is opened we refresh
     * the cache by requesting for new data from the API
     */
    fun fetchRatesSymbols(): LiveData<Resource<Unit>> =
        liveData(Dispatchers.IO) {
            emit(Resource.loading())

            if (isDataAvailable()) {
                emit(Resource.success(Unit))

                val constraint = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

                val request = OneTimeWorkRequestBuilder<WorkerClass>()
                    .setConstraints(constraint)
                    .build()
                WorkManager.getInstance(context).enqueue(request)

            } else {
                try {
                    when (val response = callNetwork()) {
                        "success" -> emit(Resource.success(Unit))
                        else -> emit(Resource.error(response, null))
                    }
                } catch (e: IOException) {
                    emit(Resource.error("No Internet access", null))
                } catch (e: Exception) {
                    emit(Resource.error(e.message!!, null))
                }
            }
        }

    suspend fun callNetwork(): String {
        return withContext(Dispatchers.IO) {

            val task1 = async { getRates() }
            val task2 = async { getSymbols() }

            val response1 = task1.await()
            val response2 = task2.await()

            if (response1.isSuccessful && response2.isSuccessful) {
                val rateList = toCurrencyRateList(response1.body()!!)
                val symbolList = toCurrencySymbolList(response2.body()!!)

                saveRates(rateList)
                saveSymbols(symbolList)

                "success"
            } else {
                Timber.d("Error Occurred ${response1.errorBody()} ${response2.errorBody()}")

                "error occurred please try again"
            }
        }
    }

    private suspend fun isDataAvailable(): Boolean {
        return withContext(Dispatchers.IO) {
            val task1 = async { getCurrencyRate() }
            val task2 = async { getCurrencySymbol() }

            val rates = task1.await()
            val symbols = task2.await()

            rates.isNotEmpty() && symbols.isNotEmpty()
        }
    }
}