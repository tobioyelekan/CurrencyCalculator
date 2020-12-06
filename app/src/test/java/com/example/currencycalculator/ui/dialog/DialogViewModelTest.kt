package com.example.currencycalculator.ui.dialog

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.currencycalculator.data.source.FakeCurrencyRepository
import com.example.currencycalculator.util.TestObjectUtil
import com.example.currencycalculator.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat

//            CurrencySymbol("NGN", "Nigeria Nigeria")
//            CurrencySymbol("GHS", "Ghana Cedis")
//            CurrencySymbol("USD", "United States Dollar")

@ExperimentalCoroutinesApi
class DialogViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var dialogViewModel: CurrencyViewModel

    private val repository = FakeCurrencyRepository()

    @Before
    fun init() {
        runBlocking {
            repository.saveSymbols(TestObjectUtil.symbols)
        }
        dialogViewModel = CurrencyViewModel(repository)
    }

    @Test
    fun `assert that launch of dialog shows all the currencies`() {
        val symbols = dialogViewModel.symbols.getOrAwaitValue()
        assertThat(symbols, `is`(notNullValue()))
        assertThat(symbols, `is`(TestObjectUtil.symbols))
    }

    @Test
    fun `assert that search currency produces expected result`() {
        dialogViewModel.searchCurrency("ng")
        val response = dialogViewModel.symbols.getOrAwaitValue()
        assertThat(response, `is`(notNullValue()))
        assertThat(response.size, `is`(1))
        assertThat(response[0].currency, `is`(TestObjectUtil.symbols[0].currency))
        assertThat(response[0].name, `is`(TestObjectUtil.symbols[0].name))
    }

    @Test
    fun `assert that non-existence of currency returns an empty list`() {
        dialogViewModel.searchCurrency("xd")
        val response = dialogViewModel.symbols.getOrAwaitValue()
        assertThat(response, `is`(emptyList()))
    }

    @Test
    fun `assert that a key returns more than one result`() {
        dialogViewModel.searchCurrency("s")
        val response = dialogViewModel.symbols.getOrAwaitValue()
        assertThat(response, `is`(notNullValue()))
        assertThat(response.size, `is`(2))

        assertThat(response[0].currency, `is`(TestObjectUtil.symbols[1].currency))
        assertThat(response[0].name, `is`(TestObjectUtil.symbols[1].name))

        assertThat(response[1].currency, `is`(TestObjectUtil.symbols[2].currency))
        assertThat(response[1].name, `is`(TestObjectUtil.symbols[2].name))
    }

}