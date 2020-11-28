package com.example.currencycalculator.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.currencycalculator.data.local.dao.CurrencyRateDao
import com.example.currencycalculator.data.local.dao.CurrencySymbolDao
import com.example.currencycalculator.data.remote.CurrencyConverterService
import com.example.currencycalculator.repo.CurrencyConverterRepo
import com.example.currencycalculator.util.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Response

@ExperimentalCoroutinesApi
class ConverterRepoTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    private val converterService = mock(CurrencyConverterService::class.java)
    private val rateDao = mock(CurrencyRateDao::class.java)
    private val symbolDao = mock(CurrencySymbolDao::class.java)
    private val context = mock(Context::class.java)

    private lateinit var repository: CurrencyConverterRepo

    @Before
    fun init() {
        repository =
            CurrencyConverterRepo(converterService, rateDao, symbolDao, context)
    }

    @Test
    fun `fetch rate symbol`() {
        runBlocking {
            `when`(converterService.getCurrencySymbols()).thenReturn(Response.success("response"))
            `when`(converterService.getCurrencyRates()).thenReturn(Response.success("response"))

            val result = repository.callNetwork()

            verify(converterService).getCurrencySymbols()
            verify(converterService).getCurrencyRates()
            assertThat(result, `is`("success"))
        }
    }


}