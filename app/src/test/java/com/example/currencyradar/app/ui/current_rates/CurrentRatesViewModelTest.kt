package com.example.currencyradar.app.ui.current_rates

import app.cash.turbine.test
import com.example.currencyradar.domain.models.TableType
import com.example.currencyradar.domain.repository.CurrentRatesRepository
import com.example.currencyradar.test_data.CurrentRatesTestData
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

class CurrentRatesViewModelTest {
    private lateinit var currentRatesRepository: CurrentRatesRepository
    private lateinit var viewModel: CurrentRatesViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())

        currentRatesRepository = mockk()

        coEvery {
            currentRatesRepository.getCurrentRates(any())
        } coAnswers {
            delay(1.seconds)
            Result.success(CurrentRatesTestData.currentRates)
        }

        viewModel = CurrentRatesViewModel(currentRatesRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `After state subscription, data are fetched correctly`() = runTest {
        viewModel.uiState.test {
            testScheduler.advanceUntilIdle()

            skipItems(2)
            awaitItem() shouldBe CurrentRatesUiState(
                currentRates = CurrentRatesTestData.currentRates,
            )
        }
    }

    @Test
    fun `After data fetch, state contains list of current rates`() = runTest {
        val selectedTable = TableType.B

        coEvery {
            currentRatesRepository.getCurrentRates(any())
        } coAnswers {
            delay(1.seconds)
            Result.failure(Exception())
        }

        viewModel.uiState.test {
            testScheduler.advanceUntilIdle()

            viewModel.onErrorMessageShown()

            coEvery {
                currentRatesRepository.getCurrentRates(any())
            } coAnswers {
                delay(1.seconds)
                Result.success(CurrentRatesTestData.currentRates)
            }

            viewModel.getCurrentRates(tableType = selectedTable)
            testScheduler.advanceUntilIdle()

            // skip states from first fetch and loading from the second one
            skipItems(5)
            awaitItem() shouldBe CurrentRatesUiState(
                currentRates = CurrentRatesTestData.currentRates,
                tableType = selectedTable,
            )
        }
    }

    @Test
    fun `When fetching data, state correctly indicates loading`() = runTest {
        viewModel.uiState.test {
            testScheduler.runCurrent()
            val initialState = awaitItem()

            initialState shouldBe CurrentRatesUiState()

            testScheduler.runCurrent()
            val loadingState = awaitItem()

            loadingState shouldBe CurrentRatesUiState(isLoading = true)

            testScheduler.runCurrent()
            val finalState = awaitItem()

            finalState shouldBe CurrentRatesUiState(
                currentRates = CurrentRatesTestData.currentRates,
            )
        }
    }

    @Test
    fun `On fetch failure, state contains thrown exception`() = runTest {
        coEvery {
            currentRatesRepository.getCurrentRates(any())
        } coAnswers {
            delay(1.seconds)
            Result.failure(IllegalStateException())
        }

        viewModel.uiState.test {
            testScheduler.advanceUntilIdle()

            viewModel.getCurrentRates(tableType = TableType.B)
            testScheduler.advanceUntilIdle()

            skipItems(4)
            awaitItem().let {
                it.currentRates shouldBe emptyList()
                it.tableType shouldBe TableType.A
                it.isLoading shouldBe false
                it.error shouldBe IllegalStateException()
            }
        }
    }

    @Test
    fun `When error occurs, tab index is set to its previous value`() = runTest {
        viewModel.uiState.test {
            testScheduler.advanceUntilIdle()

            viewModel.getCurrentRates(tableType = TableType.B)
            testScheduler.advanceUntilIdle()

            coEvery {
                currentRatesRepository.getCurrentRates(any())
            } coAnswers {
                delay(1.seconds)
                Result.failure(IllegalStateException())
            }

            viewModel.getCurrentRates(tableType = TableType.A)
            testScheduler.advanceUntilIdle()

            skipItems(6)
            awaitItem().tableType shouldBe TableType.B
        }
    }
}
