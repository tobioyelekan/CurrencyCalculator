package com.example.currencycalculator.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.currencycalculator.repo.CurrencyConverterRepo
import com.example.currencycalculator.ui.dialog.CurrencyViewModel
import com.example.currencycalculator.util.getOrAwaitValue
import com.example.currencycalculator.util.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
class SelectViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val repository = mock(CurrencyConverterRepo::class.java)
    private lateinit var viewModel: CurrencyViewModel

    @Before
    fun init() {
        viewModel = CurrencyViewModel(repository)
    }

    @Test
    fun `check value of selected currency livedata`() {
        viewModel.selectCurrency("AED")
        val value = viewModel.selectedCurrency.getOrAwaitValue()
        assertThat(value, `is`("AED"))
    }

    @Test
    fun `verify that searchSymbol() was called`() {
        viewModel.searchCurrency("a")
        viewModel.symbols.observeForever(mock())
        verify(repository).searchSymbol("a")
    }
}