package com.example.currencycalculator.ui.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.currencycalculator.data.helper.Resource
import com.example.currencycalculator.data.source.repo.CurrencyRepository
import com.example.currencycalculator.util.MainCoroutineRule
import com.example.currencycalculator.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class SplashViewModelMockTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantAppExecutors = InstantTaskExecutorRule()

    private lateinit var splashViewModel: SplashViewModel

    private val repository = mock(CurrencyRepository::class.java)

    @Before
    fun setupViewModel() {
        splashViewModel = SplashViewModel(repository)
    }

    @Test
    fun `assert that success is returned when database is empty and call to network is successful`() =
        mainCoroutineRule.runBlockingTest {
            val response = MutableLiveData<Resource<Unit>>()
            response.value = Resource.success(Unit)

            `when`(repository.fetchRateSymbols()).thenReturn(response)
        }
}