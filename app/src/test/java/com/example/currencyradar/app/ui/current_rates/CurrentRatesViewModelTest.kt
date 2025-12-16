package com.example.currencyradar.app.ui.current_rates

import app.cash.turbine.test
import com.example.currencyradar.domain.models.CurrencyTableType
import com.example.currencyradar.domain.repository.CurrentRatesRepository
import com.example.currencyradar.test_data.CurrentRatesTestData
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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
    fun `Data are not fetched until VM state's first subscription`() = runTest {
        coEvery {
            currentRatesRepository.getCurrentRates(any())
        } returns Result.success(CurrentRatesTestData.currentRates)

        viewModel = CurrentRatesViewModel(currentRatesRepository)
        testScheduler.runCurrent()
        coVerify(exactly = 0) { currentRatesRepository.getCurrentRates(any()) }

        viewModel.uiState.test {
            awaitItem() shouldBe CurrentRatesUiState()
        }

        testScheduler.runCurrent()
        coVerify(exactly = 1) { currentRatesRepository.getCurrentRates(any()) }
    }

    @Test
    fun `Data are automatically fetched only on VM state's first subscription`() = runTest {
        coEvery {
            currentRatesRepository.getCurrentRates(any())
        } returns Result.success(CurrentRatesTestData.currentRates)

        viewModel = CurrentRatesViewModel(currentRatesRepository)
        testScheduler.runCurrent()
        coVerify(exactly = 0) { currentRatesRepository.getCurrentRates(any()) }

        viewModel.uiState.test {
            awaitItem() shouldBe CurrentRatesUiState()
        }

        val job = launch {
            viewModel.uiState.collect()
        }

        testScheduler.runCurrent()
        job.cancel()

        testScheduler.runCurrent()
        coVerify(exactly = 1) { currentRatesRepository.getCurrentRates(any()) }
    }

    @Test
    fun `After data fetch, state contains list of current rates`() = runTest {
        val selectedTable = CurrencyTableType.B

        coEvery {
            currentRatesRepository.getCurrentRates(any())
        } returns Result.success(CurrentRatesTestData.currentRates)

        viewModel.getCurrentRates(tableType = selectedTable)
        testScheduler.runCurrent()

        viewModel.uiState.test {
            val currentState = awaitItem()

            currentState.currentRates shouldBe CurrentRatesTestData.currentRates
            currentState.selectedTabIndex shouldBe selectedTable.ordinal
            currentState.isLoading shouldBe false
            currentState.error shouldBe null
        }
    }

    @Test
    fun `When fetching data, state correctly indicates loading`() = runTest {
        coEvery {
            currentRatesRepository.getCurrentRates(any())
        } returns Result.success(CurrentRatesTestData.currentRates)

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
            currentState.selectedTabIndex shouldBe CurrencyTableType.A.ordinal
            currentState.isLoading shouldBe false
            currentState.error shouldBe expectedException
        }
    }

    @Test
    fun `When error occurs, tab index is set to its previous value`() = runTest {
        coEvery {
            currentRatesRepository.getCurrentRates(any())
        } returns Result.success(CurrentRatesTestData.currentRates)

        viewModel.getCurrentRates(tableType = CurrencyTableType.B)
        testScheduler.runCurrent()

        coEvery {
            currentRatesRepository.getCurrentRates(any())
        } returns Result.failure(IllegalStateException())

        viewModel.getCurrentRates(tableType = CurrencyTableType.A)
        testScheduler.runCurrent()

        viewModel.uiState.test {
            awaitItem().selectedTabIndex shouldBe CurrencyTableType.B.ordinal
        }
    }
}
