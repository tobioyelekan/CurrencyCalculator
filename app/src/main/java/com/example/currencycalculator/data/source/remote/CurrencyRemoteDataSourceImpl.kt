package com.example.currencycalculator.data.source.remote

import com.example.currencycalculator.data.helper.Resource
import com.example.currencycalculator.data.helper.toCurrencyRateList
import com.example.currencycalculator.data.helper.toCurrencySymbolList
import com.example.currencycalculator.data.model.CurrencyRate
import com.example.currencycalculator.data.model.CurrencySymbol
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrencyRemoteDataSourceImpl @Inject constructor(
    private val currencyService: CurrencyConverterService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CurrencyRemoteDataSource {

    override suspend fun getRates(): Resource<List<CurrencyRate>> =
        withContext(ioDispatcher) {
            return@withContext try {
                val response = currencyService.getCurrencyRates()
                if (response.isSuccessful) {
                    Resource.success(toCurrencyRateList(response.body()!!))
                } else {
                    Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Resource.error(e.message!!, null)
            } catch (IO: Exception) {
                Resource.error("No internet access", null)
            }
        }


    override suspend fun getSymbols(): Resource<List<CurrencySymbol>> =
        withContext(ioDispatcher) {
            return@withContext try {
                val response = currencyService.getCurrencySymbols()
                if (response.isSuccessful) {
                    Resource.success(toCurrencySymbolList(response.body()!!))
                } else {
                    Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Resource.error(e.message!!, null)
            } catch (IO: Exception) {
                Resource.error("No internet access", null)
            }
        }


}