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
class RateDaoTest : DbSetup() {

    @Test
    fun insertAndReadRates() = runBlocking {
        val rates = TestObjectUtil.rates
        rateDao.saveAllRates(rates)

        val result = rateDao.getCurrencyAsync()
        assertThat(result.size, `is`(3))
        assertThat(result, `is`(rates))
    }

    @Test
    fun insertAndLoadLiveDataRates() = runBlocking {
        val rates = TestObjectUtil.rates
        rateDao.saveAllRates(rates)

        val result = rateDao.getCurrencies().getOrAwaitValue()
        assertThat(result.size, `is`(3))
        assertThat(result, `is`(rates))
    }

    @Test
    fun getCurrency() = runBlocking {
        val rates = TestObjectUtil.rates
        rateDao.saveAllRates(rates)

        val response = rateDao.getCurrency("USD").getOrAwaitValue()
        assertThat(response.currency, `is`("USD"))
        assertThat(response.rate, `is`(1.53434))
    }
}