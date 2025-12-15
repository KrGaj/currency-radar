package com.example.currencyradar.app.ui.current_rates

import app.cash.turbine.test
import com.example.currencyradar.domain.models.Currency
import com.example.currencyradar.domain.models.CurrencyTableType
import com.example.currencyradar.domain.models.CurrentRate
import com.example.currencyradar.domain.repository.CurrentRatesRepository
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class CurrentRatesViewModelTest {
    private lateinit var currentRatesRepository: CurrentRatesRepository
    private lateinit var viewModel: CurrentRatesViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())

        currentRatesRepository = mockk()
        viewModel = CurrentRatesViewModel(currentRatesRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `After data fetch, state contains list of current rates`() = runTest {
        coEvery {
            currentRatesRepository.getCurrentRates(any())
        } returns Result.success(currentRates)

        viewModel.getCurrentRates(tableType = CurrencyTableType.B)
        testScheduler.runCurrent()

        viewModel.uiState.test {
            val currentState = awaitItem()

            currentState.currentRates shouldBe currentRates
            currentState.isLoading shouldBe false
            currentState.error shouldBe null
        }
    }

    @Test
    fun `When fetching data, state correctly indicates loading`() = runTest {
        coEvery {
            currentRatesRepository.getCurrentRates(any())
        } returns Result.success(currentRates)

        viewModel.getCurrentRates(tableType = CurrencyTableType.B)
        viewModel.uiState.test {
            awaitItem().isLoading shouldBe true
        }

        testScheduler.runCurrent()

        viewModel.uiState.test {
            awaitItem().isLoading shouldBe false
        }
    }

    @Test
    fun `On fetch failure, state contains thrown exception`() = runTest {
        val expectedException = IllegalStateException()

        coEvery {
            currentRatesRepository.getCurrentRates(any())
        } returns Result.failure(expectedException)

        viewModel.getCurrentRates(tableType = CurrencyTableType.B)
        testScheduler.runCurrent()

        viewModel.uiState.test {
            val currentState = awaitItem()

            currentState.currentRates shouldBe emptyList()
            currentState.isLoading shouldBe false
            currentState.error shouldBe expectedException
        }
    }

    companion object {
        private val currentRates = listOf(
            CurrentRate(
                currency = Currency(
                    name = "dolar kanadyjski",
                    code = "CAD",
                ),
                middleValue = 2.6181.toBigDecimal(),
            ),
            CurrentRate(
                currency = Currency(
                    name = "euro",
                    code = "EUR",
                ),
                middleValue = 4.2271.toBigDecimal(),
            ),
            CurrentRate(
                currency = Currency(
                    name = "korona norweska",
                    code = "NOK",
                ),
                middleValue = 0.3569.toBigDecimal(),
            ),
        )
    }
}