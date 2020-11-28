package com.example.currencycalculator.ui.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.currencycalculator.repo.CurrencyConverterRepo

class SplashViewModel @ViewModelInject constructor(
    repo: CurrencyConverterRepo
) : ViewModel() {

    val status = repo.fetchRatesSymbols()
}