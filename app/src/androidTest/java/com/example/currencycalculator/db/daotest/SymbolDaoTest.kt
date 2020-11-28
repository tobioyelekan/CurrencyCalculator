package com.example.currencycalculator.db.daotest

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.currencycalculator.db.DbSetup
import com.example.currencycalculator.db.TestObjectUtil
import com.example.currencycalculator.getOrAwaitValue
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

        val symbols = TestObjectUtil.symbols
        symbolDao.saveAllSymbols(symbols)

        val loadSymbols = symbolDao.getCurrencySymbolsAsync()
        assertThat(loadSymbols.size, `is`(3))
        assertThat(loadSymbols, `is`(symbols))
    }

    @Test
    fun searchCurrencySymbol() = runBlocking {
        val symbols = TestObjectUtil.symbols
        symbolDao.saveAllSymbols(symbols)

        val searchResult = symbolDao.getCurrencySymbols("NGN").getOrAwaitValue()
        assertThat(searchResult!!.size, `is`(1))
        assertThat(searchResult[0].currency, `is`("NGN"))
        assertThat(searchResult, `is`(searchResult))
    }

    @Test
    fun searchEmptyToReturnAll() = runBlocking {
        val symbols = TestObjectUtil.symbols
        symbolDao.saveAllSymbols(symbols)

        val searchResult = symbolDao.getCurrencySymbols("").getOrAwaitValue()
        assertThat(searchResult!!.size, `is`(3))
        assertThat(searchResult, `is`(searchResult))
    }

    @Test
    fun searchSymbolNotExist() = runBlocking {
        val symbols = TestObjectUtil.symbols
        symbolDao.saveAllSymbols(symbols)

        val searchResult = symbolDao.getCurrencySymbols("lpx").getOrAwaitValue()
        assertThat(searchResult, `is`(emptyList()))
    }
}