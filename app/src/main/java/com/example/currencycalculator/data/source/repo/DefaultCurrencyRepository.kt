package com.example.currencycalculator.data.source.repo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.currencycalculator.data.helper.Resource
import com.example.currencycalculator.data.helper.Resource.Status
import com.example.currencycalculator.data.model.CurrencyRate
import com.example.currencycalculator.data.model.CurrencySymbol
import com.example.currencycalculator.data.source.local.CurrencyLocalDataSource
import com.example.currencycalculator.data.source.remote.CurrencyRemoteDataSource
import com.example.currencycalculator.util.WorkerClass
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

class DefaultCurrencyRepository @Inject constructor(
    private val remoteDataSource: CurrencyRemoteDataSource,
    private val localDataSource: CurrencyLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val context: Context
) : CurrencyRepository {

    override fun fetchRateSymbols(): LiveData<Resource<Unit>> =
        liveData(ioDispatcher) {
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
                val response = callNetwork()
                when (response.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        emit(Resource.success(Unit))
                    }
                    Status.ERROR -> {
                        emit(Resource.error(response.message!!, null))
                    }
                }
            }
        }

    override suspend fun callNetwork() = coroutineScope {

        val task1 = async { remoteDataSource.getRates() }
        val task2 = async { remoteDataSource.getSymbols() }

        val response1 = task1.await()
        val response2 = task2.await()

        if (response1.status == Status.SUCCESS && response2.status == Status.SUCCESS) {
            saveRates(response1.data!!)
            saveSymbols(response2.data!!)

            Resource.success("success")
        } else {
            Timber.d("Error Occurred ${response1.message} ${response2.message}")

            Resource.error("error occurred please try again")
        }
    }

    private suspend fun isDataAvailable(): Boolean {
        return withContext(ioDispatcher) {
            val task1 = async { getCurrencyRates() }
            val task2 = async { getCurrencySymbols() }

            val rates = task1.await()
            val symbols = task2.await()

            rates.isNotEmpty() && symbols.isNotEmpty()
        }
    }

    override fun searchSymbol(search: String): LiveData<List<CurrencySymbol>> {
        return localDataSource.observeSymbols(search)
    }

    override suspend fun getCurrencyRates(): List<CurrencyRate> {
        return localDataSource.getCurrencyRateAsync()
    }

    override suspend fun getSingleCurrencyRate(id: String): CurrencyRate {
        return localDataSource.getSingleCurrencyRate(id)
    }

    override suspend fun getCurrencySymbols(): List<CurrencySymbol> {
        return localDataSource.getCurrencySymbolAsync()
    }

    override fun getCurrency(id: String): LiveData<CurrencyRate> {
        return localDataSource.observeRate(id)
    }

    override suspend fun saveRates(rates: List<CurrencyRate>) {
        localDataSource.saveRates(rates)
    }

    override suspend fun saveSymbols(symbols: List<CurrencySymbol>) {
        localDataSource.saveSymbols(symbols)
    }

}