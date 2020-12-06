package com.example.currencycalculator.ui.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.currencycalculator.data.source.repo.CurrencyRepository

class SplashViewModel @ViewModelInject constructor(
    currencyRepository: CurrencyRepository
) : ViewModel() {

    val status = currencyRepository.fetchRateSymbols()
}