package com.example.currencycalculator.ui.dialog

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.currencycalculator.data.source.repo.CurrencyRepository

class CurrencyViewModel @ViewModelInject constructor
    (currencyRepository: CurrencyRepository) : ViewModel() {

    private val _selectedCurrency = MutableLiveData<String>()
    val selectedCurrency: LiveData<String> = _selectedCurrency

    private val _searchText = MutableLiveData<String>()

    init {
        searchCurrency("")
    }

    fun searchCurrency(search: String) {
        _searchText.value = search
    }

    val symbols = _searchText.switchMap {
        currencyRepository.searchSymbol(it)
    }

    fun selectCurrency(currency: String) {
        _selectedCurrency.value = currency
    }

}