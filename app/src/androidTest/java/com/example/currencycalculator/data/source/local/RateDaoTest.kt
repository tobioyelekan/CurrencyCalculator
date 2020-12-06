package com.example.currencycalculator.data.source.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.currencycalculator.util.TestObjectUtil
import com.example.currencycalculator.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class RateDaoTest : DbSetup() {

    @Test
    fun insertAndReadRates() = runBlocking {
        saveRates()

        val result = rateDao.getCurrencyAsync()
        assertThat(result.size, `is`(TestObjectUtil.rates.size))
        assertThat(result, `is`(TestObjectUtil.rates))
    }

    @Test
    fun getCurrency() = runBlocking {
        saveRates()

        val response = rateDao.getCurrency("USD").getOrAwaitValue()
        assertThat(response.currency, `is`("USD"))
        assertThat(response.rate, `is`(1.53434))
    }

    @Test
    fun getSingleCurrencyRate() = runBlocking {
        saveRates()

        val response = rateDao.getSingleCurrencyRate(TestObjectUtil.rates[0].currency)
        assertThat(response, `is`(TestObjectUtil.rates[0]))
    }

    private fun saveRates() = runBlocking {
        val rates = TestObjectUtil.rates
        rateDao.saveAllRates(rates)
    }
}