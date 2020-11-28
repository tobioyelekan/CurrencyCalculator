package com.example.currencycalculator.ui.convert

internal fun convertCurrency(amount: String, rate1: Double, rate2: Double): String {
    if (amount.isEmpty()) return ""
    return String.format("%.2f", (rate2 * amount.toDouble()) / rate1)
}

internal fun getImageUrl(symbol: String): String {
    return if (symbol.length < 2) " "
    else "https://flagcdn.com/28x21/${symbol.substring(0, 2).toLowerCase()}.png"
}