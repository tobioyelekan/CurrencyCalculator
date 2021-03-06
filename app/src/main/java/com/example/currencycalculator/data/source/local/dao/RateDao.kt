package com.example.currencycalculator.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencycalculator.data.model.CurrencyRate

@Dao
interface RateDao {
    @Query("SELECT * FROM currency_rate")
    suspend fun getCurrencyAsync(): List<CurrencyRate>

    @Query("SELECT * FROM currency_rate WHERE currency = :id")
    fun getCurrency(id: String): LiveData<CurrencyRate>

    @Query("SELECT * FROM currency_rate WHERE currency = :id")
    fun getSingleCurrencyRate(id: String): CurrencyRate

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllRates(rates: List<CurrencyRate>)
}