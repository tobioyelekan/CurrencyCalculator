package com.example.currencycalculator.ui.convert

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.currencycalculator.data.model.CurrencyRate
import com.example.currencycalculator.data.source.repo.CurrencyRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ConvertViewModel @ViewModelInject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    private lateinit var firstCurrencyData: CurrencyRate
    private lateinit var secondCurrencyData: CurrencyRate

    private val selectFirst = MutableLiveData<String>()

    private val selectSecond = MutableLiveData<String>()

    private val _amountText1 = MutableLiveData<String>()
    val amountText1: LiveData<String> = _amountText1

    private val _amountText2 = MutableLiveData<String>()
    val amountText2: LiveData<String> = _amountText2

    init {
        fetchRate()
    }

    private fun fetchRate() {
        viewModelScope.launch {
            val rates = currencyRepository.getCurrencyRates()
            firstCurrencyData = rates[0]
            secondCurrencyData = rates[1]

            selectFirst(rates[0].currency)
            selectSecond(rates[1].currency)
        }
    }

    private fun selectFirst(id: String) {
        selectFirst.value = id
    }

    private fun selectSecond(id: String) {
        selectSecond.value = id
    }

    fun changeCurrency(pos: Int, currency: String, firstAmount: String, secondAmount: String) {
        when (pos) {
            0 -> {
                if (currency != firstCurrencyData.currency) {
                    viewModelScope.launch {
                        selectFirst(currency)
                        if (secondAmount.isNotEmpty()) {
                            delay(1000)
                            convertCurrency(secondAmount, 1)
                        }
                    }
                }
            }
            1 -> {
                if (currency != secondCurrencyData.currency) {
                    viewModelScope.launch {
                        selectSecond(currency)
                        if (firstAmount.isNotEmpty()) {
                            delay(1000)
                            convertCurrency(firstAmount, 0)
                        }
                    }
                }
            }
        }
    }

    fun convertCurrency(amount: String, pos: Int) {
        when (pos) {
            0 -> {
                _amountText2.value = convertCurrency(
                    amount,
                    firstCurrencyData.rate,
                    secondCurrencyData.rate
                )
            }
            1 -> {
                _amountText1.value = convertCurrency(
                    amount,
                    secondCurrencyData.rate,
                    firstCurrencyData.rate
                )
            }
        }
    }

    val firstCurrency = selectFirst.switchMap {
        viewModelScope.launch {
            firstCurrencyData = currencyRepository.getSingleCurrencyRate(it)
        }
        currencyRepository.getCurrency(it)
    }

    val secondCurrency = selectSecond.switchMap {
        viewModelScope.launch {
            firstCurrencyData = currencyRepository.getSingleCurrencyRate(it)
        }
        currencyRepository.getCurrency(it)
    }

}