package com.example.currencycalculator.ui.convert

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.currencycalculator.data.source.FakeCurrencyRepository
import com.example.currencycalculator.util.MainCoroutineRule
import com.example.currencycalculator.util.TestObjectUtil
import com.example.currencycalculator.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat


@ExperimentalCoroutinesApi
class ConvertViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutine = MainCoroutineRule()

    private lateinit var convertViewModel: ConvertViewModel

    private val repository = FakeCurrencyRepository()

    @Before
    fun init() {
        runBlocking {
            repository.saveRates(TestObjectUtil.rates)
        }
        convertViewModel = ConvertViewModel(repository)
    }

    @Test
    fun `assert that launch of convert screen has both currency set`() {
        assertThat(convertViewModel.firstCurrency.getOrAwaitValue(), `is`(TestObjectUtil.rates[0]))
        assertThat(convertViewModel.secondCurrency.getOrAwaitValue(), `is`(TestObjectUtil.rates[1]))
    }

    @Test
    fun `assert that changing the first currency changes to the new currency`() =
        mainCoroutine.runBlockingTest {
            convertViewModel.changeCurrency(0, TestObjectUtil.rates.last().currency, "", "")
            val firstCurrency = convertViewModel.firstCurrency.getOrAwaitValue()

            assertThat(firstCurrency, `is`(TestObjectUtil.rates.last()))
            assertThat(firstCurrency.currency, `is`(TestObjectUtil.rates.last().currency))
            assertThat(firstCurrency.rate, `is`(TestObjectUtil.rates.last().rate))
        }

    @Test
    fun `assert that changing the second currency changes to the new currency`() =
        mainCoroutine.runBlockingTest {
            convertViewModel.changeCurrency(1, TestObjectUtil.rates[2].currency, "", "")
            val secondCurrency = convertViewModel.secondCurrency.getOrAwaitValue()

            assertThat(secondCurrency, `is`(TestObjectUtil.rates[2]))
            assertThat(secondCurrency.currency, `is`(TestObjectUtil.rates[2].currency))
            assertThat(secondCurrency.rate, `is`(TestObjectUtil.rates[2].rate))
        }

    @Test
    fun `assert that typing on first edit text converts currency and sets result to the second edit text`() {
        //note default currencies selected are NGN(rate = 455.3443) and GHS(rate = 6.90334)

        convertViewModel.convertCurrency("1", 0)
        val amount =
            convertCurrency("1", TestObjectUtil.rates[0].rate, TestObjectUtil.rates[1].rate)

        assertThat(convertViewModel.amountText2.getOrAwaitValue(), `is`(amount))
    }

    @Test
    fun `assert that typing on second edit text converts currency and sets result to the first edit text`() {
        //note default currencies selected are NGN(rate = 455.3443) and GHS(rate = 6.90334)

        convertViewModel.convertCurrency("1", 1)

        val amount =
            convertCurrency("1", TestObjectUtil.rates[1].rate, TestObjectUtil.rates[0].rate)

        assertThat(convertViewModel.amountText1.getOrAwaitValue(), `is`(amount))
    }

    @Test
    fun `assert that changing of first currency should convert `() {
        convertViewModel
    }

}