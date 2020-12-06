package com.example.currencycalculator.data.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencycalculator.data.model.CurrencySymbol

@Dao
interface SymbolDao {
    @Query("SELECT * FROM currency_symbols")
    suspend fun getCurrencySymbolsAsync(): List<CurrencySymbol>

    @Query("SELECT * FROM currency_symbols WHERE name LIKE '%' || :searchText || '%' OR currency LIKE '%' || :searchText || '%'")
    fun getCurrencySymbols(searchText: String): LiveData<List<CurrencySymbol>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllSymbols(symbols: List<CurrencySymbol>)
}