package com.example.currencycalculator.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_rate")
data class CurrencyRate(
    @PrimaryKey
    val currency: String,
    val rate: Double
)

@Entity(tableName = "currency_symbols")
data class CurrencySymbol(
    @PrimaryKey
    val currency: String,
    val name: String
)