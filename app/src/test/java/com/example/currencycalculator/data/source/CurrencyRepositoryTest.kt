package com.example.currencycalculator.data.source

import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import com.example.currencycalculator.data.helper.Resource
import com.example.currencycalculator.data.model.CurrencyRate
import com.example.currencycalculator.data.model.CurrencySymbol
import com.example.currencycalculator.data.source.local.CurrencyLocalDataSource
import com.example.currencycalculator.data.source.remote.CurrencyRemoteDataSource
import com.example.currencycalculator.data.source.repo.DefaultCurrencyRepository
import com.example.currencycalculator.util.MainCoroutineRule
import com.example.currencycalculator.util.TestObjectUtil
import com.example.currencycalculator.util.argumentCaptor
import com.example.currencycalculator.util.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class CurrencyRepositoryTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var repository: DefaultCurrencyRepository

    private val remoteDataSource = mock(CurrencyRemoteDataSource::class.java)

    private val localDataSource = mock(CurrencyLocalDataSource::class.java)

    @Before
    fun setup() {
        repository = DefaultCurrencyRepository(
            remoteDataSource,
            localDataSource,
            Dispatchers.Main,
            ApplicationProvider.getApplicationContext()
        )
    }

    @Test
    fun `assert that fetchRateSymbols returns success from network call when cache is empty`() =
        mainCoroutineRule.runBlockingTest {

            `when`(remoteDataSource.getRates()).thenReturn(Resource.success(TestObjectUtil.rates))
            `when`(remoteDataSource.getSymbols()).thenReturn(Resource.success(TestObjectUtil.symbols))

            `when`(localDataSource.getCurrencyRateAsync()).thenReturn(emptyList())
            `when`(localDataSource.getCurrencySymbolAsync()).thenReturn(emptyList())

            val response = repository.fetchRateSymbols()

            assertThat(response.getOrAwaitValue(), `is`(Resource.loading()))
            assertThat(response.getOrAwaitValue(), `is`(Resource.success(Unit)))
        }

//    @Test
//    fun `assert that fetchRateSymbols returns success when cache is available`() =
//        mainCoroutineRule.runBlockingTest {
//            `when`(localDataSource.getCurrencyRateAsync()).thenReturn(TestObjectUtil.rates)
//            `when`(localDataSource.getCurrencySymbolAsync()).thenReturn(TestObjectUtil.symbols)
//
//            val response = repository.fetchRateSymbols()
//
//            assertThat(response.getOrAwaitValue(), `is`(Resource.loading()))
//            assertThat(response.getOrAwaitValue(), `is`(Resource.success(Unit)))
//        }

    @Test
    fun `assert that callNetwork succeeds`() = mainCoroutineRule.runBlockingTest {
        `when`(remoteDataSource.getRates()).thenReturn(Resource.success(TestObjectUtil.rates))
        `when`(remoteDataSource.getSymbols()).thenReturn(Resource.success(TestObjectUtil.symbols))

        val response = repository.callNetwork()

        verify(remoteDataSource).getSymbols()
        verify(remoteDataSource).getSymbols()

        assertThat(response, `is`(Resource.success("success")))
    }

    @Test
    fun `assert that callNetwork fails when error returns from network call`() =
        mainCoroutineRule.runBlockingTest {
            `when`(remoteDataSource.getRates()).thenReturn(Resource.error("error occurred"))
            `when`(remoteDataSource.getSymbols()).thenReturn(Resource.error("error occurred"))

            val response = repository.callNetwork()

            verify(remoteDataSource).getSymbols()
            verify(remoteDataSource).getSymbols()

            assertThat(response, `is`(Resource.error("error occurred please try again")))
        }

    @Test
    fun `assert search symbol`() = mainCoroutineRule.runBlockingTest {
        val data = MutableLiveData<List<CurrencySymbol>>()
        data.value = TestObjectUtil.symbols

        `when`(localDataSource.observeSymbols("id")).thenReturn(data)

        val response = repository.searchSymbol("id").getOrAwaitValue()

        verify(localDataSource).observeSymbols("id")
        assertThat(response, `is`(TestObjectUtil.symbols))
    }

    @Test
    fun `assert getCurrencyRates`() = mainCoroutineRule.runBlockingTest {
        `when`(localDataSource.getCurrencyRateAsync()).thenReturn(TestObjectUtil.rates.take(2))
        val response = repository.getCurrencyRates()

        verify(localDataSource).getCurrencyRateAsync()
        assertThat(response, `is`(TestObjectUtil.rates.take(2)))
    }

    @Test
    fun `assert getSingleCurrencyRate`() = mainCoroutineRule.runBlockingTest {
        `when`(localDataSource.getSingleCurrencyRate("id")).thenReturn(TestObjectUtil.rates[0])
        val response = repository.getSingleCurrencyRate("id")

        verify(localDataSource).getSingleCurrencyRate("id")
        assertThat(response, `is`(TestObjectUtil.rates[0]))
    }

    @Test
    fun `assert getCurrencySymbols`() = mainCoroutineRule.runBlockingTest {
        `when`(localDataSource.getCurrencySymbolAsync()).thenReturn(TestObjectUtil.symbols)
        val response = repository.getCurrencySymbols()


        verify(localDataSource).getCurrencySymbolAsync()
        assertThat(response, `is`(TestObjectUtil.symbols))
    }

    @Test
    fun `assert getCurrency`() = mainCoroutineRule.runBlockingTest {
        val data = MutableLiveData<CurrencyRate>()
        data.value = TestObjectUtil.rates[1]

        `when`(localDataSource.observeRate("id")).thenReturn(data)

        val response = repository.getCurrency("id").getOrAwaitValue()

        verify(localDataSource).observeRate("id")
        assertThat(response, `is`(TestObjectUtil.rates[1]))
    }

//    @Test
//    fun `assert saveRates`() = mainCoroutineRule.runBlockingTest {
//        repository.saveRates(TestObjectUtil.rates)
//
//        val argumentCapture = argumentCaptor<List<CurrencyRate>>()
//        verify(localDataSource).saveRates(argumentCapture.capture())
//
//        val list = argumentCapture.value
//        assertThat(list, `is`(TestObjectUtil.rates))
//    }
//
//    @Test
//    fun `assert saveSymbols`() = mainCoroutineRule.runBlockingTest {
//
//        repository.saveSymbols(TestObjectUtil.symbols)
//
//        val argumentCapture = argumentCaptor<List<CurrencySymbol>>()
//        verify(localDataSource).saveSymbols(argumentCapture.capture())
//
//        val list = argumentCapture.value
//        assertThat(list, `is`(TestObjectUtil.symbols))
//    }
}