package com.example.currencycalculator.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.currencycalculator.util.TestObjectUtil
import com.example.currencycalculator.util.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class CurrencyLocalDataSourceTest {

    private lateinit var localDataSource: CurrencyLocalDataSourceImpl
    private lateinit var database: CurrencyDatabase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CurrencyDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        localDataSource =
            CurrencyLocalDataSourceImpl(
                database.currencyRateDao(),
                database.currencySymbolDao(),
                Dispatchers.Main
            )

        runBlocking {
            saveRates()
            saveSymbols()
        }
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun saveRates_and_retrieveRates() = runBlocking {
        val rates = localDataSource.getCurrencyRateAsync()
        assertThat(rates, `is`(TestObjectUtil.rates))
    }

    @Test
    fun saveSymbols_and_retrieve() = runBlocking {
        val symbols = localDataSource.getCurrencySymbolAsync()
        assertThat(symbols, `is`(TestObjectUtil.symbols))
    }

    @Test
    fun getSingleCurrencyRate() = runBlocking {
        val singleRate = localDataSource.getSingleCurrencyRate(TestObjectUtil.rates[1].currency)
        assertThat(singleRate, `is`(TestObjectUtil.rates[1]))
    }

    @Test
    fun find_and_observe_a_currency_rate() = runBlocking {
        val response =
            localDataSource.observeRate(TestObjectUtil.rates[0].currency).getOrAwaitValue()
        assertThat(response, `is`(TestObjectUtil.rates[0]))
    }

    @Test
    fun search_and_observe_symbol_results() = runBlocking {
        val response =
            localDataSource.observeSymbols("e").getOrAwaitValue()
        assertThat(response, `is`(TestObjectUtil.symbols))
    }

    private fun saveRates() = runBlocking {
        localDataSource.saveRates(TestObjectUtil.rates)
    }

    private fun saveSymbols() = runBlocking {
        localDataSource.saveSymbols(TestObjectUtil.symbols)
    }

}