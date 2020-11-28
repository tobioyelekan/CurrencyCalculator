package com.example.currencycalculator.db

import com.example.currencycalculator.data.model.CurrencyRate
import com.example.currencycalculator.data.model.CurrencySymbol

object TestObjectUtil {
    val symbols = listOf(
        CurrencySymbol("NGN", "Nigeria Nigeria"),
        CurrencySymbol("GHS", "Ghana Cedis"),
        CurrencySymbol("USD", "United States Dollar")
    )

    val rates = listOf(
        CurrencyRate("NGN", 455.3443),
        CurrencyRate("GHS", 6.90334),
        CurrencyRate("USD", 1.53434)
    )
}