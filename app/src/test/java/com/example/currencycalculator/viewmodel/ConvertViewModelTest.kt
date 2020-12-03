package com.example.currencycalculator.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.currencycalculator.data.source.repo.CurrencyConverterRepo
import com.example.currencycalculator.ui.convert.ConvertViewModel
import com.example.currencycalculator.util.TestCoroutineRule
import com.example.currencycalculator.util.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
class ConvertViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    private val repository = mock(CurrencyConverterRepo::class.java)
    private lateinit var viewModel: ConvertViewModel

    @Before
    fun init() {
        viewModel = ConvertViewModel(repository)
    }

    @Test
    fun `verify getCurrency called when select first is fired`() {
        viewModel.selectFirst("NGN")
        viewModel.firstCurrency.observeForever(mock())
        verify(repository).getCurrency("NGN")
    }

    @Test
    fun `verify getCurrency called when select second is fired`() {
        viewModel.selectSecond("AED")
        viewModel.secondCurrency.observeForever(mock())
        verify(repository).getCurrency("AED")
    }
}