package com.example.currencycalculator.data.source.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.currencycalculator.util.TestObjectUtil
import com.example.currencycalculator.util.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class SymbolDaoTest : DbSetup() {

    @Test
    fun insertAndReadSymbols() = runBlocking {

        saveAllSymbols()

        val loadSymbols = symbolDao.getCurrencySymbolsAsync()
        assertThat(loadSymbols.size, `is`(TestObjectUtil.symbols.size))
        assertThat(loadSymbols, `is`(TestObjectUtil.symbols))
    }

    @Test
    fun searchCurrencySymbol() = runBlocking {
        saveAllSymbols()

        val searchResult = symbolDao.getCurrencySymbols("NGN").getOrAwaitValue()
        assertThat(searchResult.size, `is`(1))
        assertThat(searchResult[0].currency, `is`("NGN"))
        assertThat(searchResult, `is`(searchResult))
    }

    @Test
    fun searchEmptyToReturnAll() = runBlocking {
        saveAllSymbols()

        val searchResult = symbolDao.getCurrencySymbols("").getOrAwaitValue()
        assertThat(searchResult.size, `is`(TestObjectUtil.symbols.size))
        assertThat(searchResult, `is`(searchResult))
    }

    @Test
    fun searchSymbolNotExist() = runBlocking {
        saveAllSymbols()

        val searchResult = symbolDao.getCurrencySymbols("lpx").getOrAwaitValue()
        assertThat(searchResult, `is`(emptyList()))
    }

    private fun saveAllSymbols() = runBlocking {
        val symbols = TestObjectUtil.symbols
        symbolDao.saveAllSymbols(symbols)
    }
}