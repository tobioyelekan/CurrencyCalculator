package com.example.currencycalculator.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencycalculator.data.model.CurrencyRate

@Dao
interface CurrencyRateDao {
    @Query("SELECT * FROM currency_rate")
    fun getCurrencies(): LiveData<List<CurrencyRate>>

    @Query("SELECT * FROM currency_rate")
    suspend fun getCurrencyAsync(): List<CurrencyRate>

    @Query("SELECT * FROM currency_rate WHERE currency = :id")
    fun getCurrency(id: String): LiveData<CurrencyRate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllRates(rates: List<CurrencyRate>)
}