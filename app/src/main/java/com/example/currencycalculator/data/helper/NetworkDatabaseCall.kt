package com.example.currencycalculator.data.helper

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.currencycalculator.data.model.CurrencyRate
import com.example.currencycalculator.data.model.CurrencySymbol
import kotlinx.coroutines.Dispatchers
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.lang.Exception

fun <T, A> resultLiveData(
    databaseCall: suspend () -> LiveData<T>,
    networkCall: suspend () -> Response<A>,
    saveResourceCall: suspend (A) -> Unit
) =
    liveData(Dispatchers.IO) {
        val source: LiveData<Resource<T>> = databaseCall.invoke().map { Resource.success(it) }
        val disposable = emitSource(source.map { Resource.loading(it.data) })
        try {
            val response = networkCall.invoke()
            if (response.isSuccessful) {
                disposable.dispose()
                saveResourceCall.invoke(response.body()!!)
                emitSource(databaseCall.invoke().map { Resource.success(it) })
            } else {
                Log.d("hey", "ERROR call unsuccessful ${response.errorBody()!!.string()}")
                emit(Resource.error(parseError(response.errorBody()!!.string())))
                emitSource(source)
            }
        } catch (e: IOException) {
            Log.d("hey", "ERROR ${e.message}")
            emit(Resource.error<T>("No Internet access"))
            emitSource(source)
        } catch (e: Exception) {
            Log.d("hey", "ERROR ${e.message}")
            emit(Resource.error<T>(e.message!!))
        }
    }

fun toCurrencyRateList(data: String): List<CurrencyRate> {
    val list = mutableListOf<CurrencyRate>()

    try {
        val jsonObject = JSONObject(data)
        val objectRates = jsonObject.getJSONObject("rates")
        val keys = objectRates.keys()
        keys.forEach {
            list.add(CurrencyRate(it, objectRates.getDouble(it)))
        }
    } catch (e: JSONException) {
        Timber.d("PARSE_ERROR ${e.message}")
    }

    return list
}

fun toCurrencySymbolList(data: String): List<CurrencySymbol> {
    val list = mutableListOf<CurrencySymbol>()

    try {
        val jsonObject = JSONObject(data)
        val objectRates = jsonObject.getJSONObject("symbols")
        val keys = objectRates.keys()
        keys.forEach {
            list.add(CurrencySymbol(it, objectRates.getString(it)))
        }
    } catch (e: JSONException) {
        Timber.d("PARSE_ERROR ${e.message}")
    }

    return list
}

fun <T> result(
    networkCall: suspend () -> Response<T>
): LiveData<Resource<T>> =
    liveData(Dispatchers.IO) {
        emit(Resource.loading<T>())

        try {
            val response = networkCall.invoke()
            if (response.isSuccessful) {
                emit(Resource.success<T>(response.body()!!))
            } else {
                emit(Resource.error<T>(parseError(response.errorBody()!!.string())))
            }
        } catch (e: IOException) {
            emit(Resource.error<T>("no internet access"))
        } catch (e: Exception) {
            emit(Resource.error<T>(e.message!!))
        }
    }

fun parseError(error: String): String {
    return try {
        val json = JSONObject(error)
        if (json.has("message")) {
            json.get("message").toString()
        } else {
            "Something went wrong, try again"
        }
    } catch (e: Exception) {
        "Something went wrong"
    }
}
