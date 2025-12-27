package com.example.currencyradar.app.ui.rate_history

import app.cash.turbine.test
import com.example.currencyradar.domain.models.Currency
import com.example.currencyradar.domain.models.TableType
import com.example.currencyradar.domain.repository.RateHistoryRepository
import com.example.currencyradar.test_data.RateHistoryTestData
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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

class RateHistoryViewModelTest {
    private lateinit var repository: RateHistoryRepository
    private lateinit var viewModel: RateHistoryViewModel


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())

        repository = mockk()
        viewModel = RateHistoryViewModel(
            repository = repository,
            currency = CURRENCY,
            table = TABLE_TYPE,
            clock = CLOCK_FAKE,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `After data fetch, state contains list of currency rates from latest days`() = runTest {
        coEvery {
            repository.getRateHistory(any(), any(), any(), any())
        } coAnswers {
            delay(1.seconds)
            Result.success(RateHistoryTestData.rateHistory)
        }

        viewModel.uiState.test {
            testScheduler.advanceUntilIdle()

            skipItems(2)
            awaitItem().let {
                it.rateHistory.containsAll(RateHistoryTestData.rateHistory) shouldBe true
                it.isLoading shouldBe false
                it.error shouldBe null
            }
        }
    }

    @Test
    fun `State contains rate history sorted by date in descending order`() = runTest {
        coEvery {
            repository.getRateHistory(any(), any(), any(), any())
        } coAnswers {
            delay(1.seconds)
            Result.success(RateHistoryTestData.rateHistory)
        }

        viewModel.uiState.test {
            testScheduler.advanceUntilIdle()

            skipItems(2)
            val item = awaitItem()
            item.rateHistory shouldBe RateHistoryTestData.rateHistory.sortedByDescending { it.date }
        }
    }

    @Test
    fun `After data fetch failure, state contains error`() = runTest {
        coEvery {
            repository.getRateHistory(any(), any(), any(), any())
        } coAnswers {
            delay(1.seconds)
            Result.failure(IllegalStateException())
        }

        viewModel.uiState.test {
            testScheduler.advanceUntilIdle()

            skipItems(2)
            awaitItem().let {
                it.rateHistory shouldBe emptyList()
                it.isLoading shouldBe false
                it.error shouldBe IllegalStateException()
            }
        }
    }

    @Test
    fun `Successful data fetch removes error correctly`() = runTest {
        coEvery {
            repository.getRateHistory(any(), any(), any(), any())
        } coAnswers {
            delay(1.seconds)
            Result.failure(IllegalStateException())
        }

        viewModel.uiState.test {
            testScheduler.advanceUntilIdle()

            coEvery {
                repository.getRateHistory(any(), any(), any(), any())
            } coAnswers {
                delay(1.seconds)
                Result.success(RateHistoryTestData.rateHistory)
            }

            viewModel.getRateHistory()
            testScheduler.advanceUntilIdle()

            skipItems(4)
            awaitItem() shouldBe RateHistoryUiState(
                rateHistory = RateHistoryTestData.rateHistory.sortedByDescending { it.date },
            )
        }
    }

    companion object {
        private val LATEST_DATE = RateHistoryTestData.rateHistoryDtoEntries
            .maxOf { it.effectiveDate }

        private val CURRENCY = RateHistoryTestData.rateHistoryDto.let {
            Currency(
                name = it.currencyName,
                code = it.currencyCode,
            )
        }

        private val TABLE_TYPE = TableType.valueOf(RateHistoryTestData.rateHistoryDto.table)

        private val CLOCK_FAKE = object : Clock {
            override fun now() =
                    LATEST_DATE.atTime(
                        hour = 0,
                        minute =  0
                    ).toInstant(
                        timeZone = TimeZone.currentSystemDefault(),
                    )
        }
    }
}