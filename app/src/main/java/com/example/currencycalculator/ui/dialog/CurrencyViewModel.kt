package com.example.currencycalculator.ui.dialog

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.currencycalculator.repo.CurrencyConverterRepo

class CurrencyViewModel @ViewModelInject constructor(repo: CurrencyConverterRepo) : ViewModel() {

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
        repo.searchSymbol(it)
    }

    fun selectCurrency(currency: String) {
        _selectedCurrency.value = currency
    }

}