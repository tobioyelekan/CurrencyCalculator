package com.example.currencycalculator.ui.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.currencycalculator.data.helper.Resource
import com.example.currencycalculator.data.source.FakeCurrencyRepository
import com.example.currencycalculator.util.MainCoroutineRule
import com.example.currencycalculator.util.TestObjectUtil
import com.example.currencycalculator.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SplashViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var splashViewModel: SplashViewModel

    private lateinit var fakeRepository: FakeCurrencyRepository

    @Before
    fun init() {
        fakeRepository = FakeCurrencyRepository()

        splashViewModel = SplashViewModel(fakeRepository)
    }

    @Test
    fun `assert that success is returned when database is empty and call to network is successful`() =
        mainCoroutineRule.runBlockingTest {
            assertThat(splashViewModel.status.getOrAwaitValue(), `is`(Resource.loading()))
            assertThat(splashViewModel.status.getOrAwaitValue(), `is`(Resource.success(Unit)))
        }

    @Test
    fun `assert that success is returned when database is not empty`() {
        mainCoroutineRule.runBlockingTest {

            fakeRepository.saveRates(TestObjectUtil.rates)
            fakeRepository.saveSymbols(TestObjectUtil.symbols)

            assertThat(splashViewModel.status.getOrAwaitValue(), `is`(Resource.loading()))
            assertThat(splashViewModel.status.getOrAwaitValue(), `is`(Resource.success(Unit)))
        }
    }


    @Test
    fun `assert that error is returned when database is empty and network request fails`() {
        mainCoroutineRule.runBlockingTest {
            fakeRepository.setShouldReturnError(true)

            assertThat(splashViewModel.status.getOrAwaitValue(), `is`(Resource.loading()))
            assertThat(
                splashViewModel.status.getOrAwaitValue(),
                `is`(Resource.error("error occurred"))
            )
        }
    }

}