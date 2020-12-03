package com.example.currencycalculator.ui.convert

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.currencycalculator.data.source.repo.CurrencyRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ConvertViewModel @ViewModelInject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

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
            selectFirst(rates[0].currency)
            selectSecond(rates[1].currency)
        }
    }

    fun selectFirst(id: String) {
        selectFirst.value = id
    }

    fun selectSecond(id: String) {
        selectSecond.value = id
    }

    fun changeCurrency(pos: Int, currency: String, firstAmount: String, secondAmount: String) {
        when (pos) {
            0 -> {
                if (currency != firstCurrency.value?.currency) {
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
                if (currency != secondCurrency.value?.currency) {
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
                    firstCurrency.value!!.rate,
                    secondCurrency.value!!.rate
                )
            }
            1 -> {
                _amountText1.value = convertCurrency(
                    amount,
                    secondCurrency.value!!.rate,
                    firstCurrency.value!!.rate
                )
            }
        }
    }

    val firstCurrency = selectFirst.switchMap {
        currencyRepository.getCurrency(it)
    }

    val secondCurrency = selectSecond.switchMap {
        currencyRepository.getCurrency(it)
    }

}