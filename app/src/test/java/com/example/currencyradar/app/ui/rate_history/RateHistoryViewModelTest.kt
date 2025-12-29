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
            currencyCode = CURRENCY.code,
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

            val rateHistoryUiStates = RateHistoryTestData.rateHistory.rates
                .map {
                    it.toDailyRateUiState(
                        referentialDailyRate = RateHistoryTestData.rateHistory.rates.last(),
                        thresholdPercent = 0.1.toBigDecimal(),
                    )
                }
            awaitItem().let {
                it.rateHistory?.rates?.containsAll(rateHistoryUiStates) shouldBe true
                it.isLoading shouldBe false
                it.error shouldBe null
            }
        }
    }

    @Test
    fun `State contains rate history`() = runTest {
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
            item.rateHistory shouldBe RateHistoryTestData.rateHistory.toRateHistoryDataUiState(
                referentialRate = RateHistoryTestData.rateHistory.rates.last(),
                thresholdPercent = 0.1.toBigDecimal(),
            )
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
                it.rateHistory shouldBe null
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
                rateHistory = RateHistoryTestData.rateHistory.toRateHistoryDataUiState(
                    referentialRate = RateHistoryTestData.rateHistory.rates.last(),
                    thresholdPercent = 0.1.toBigDecimal(),
                ),
            )
        }
    }

    @Test
    fun `Rate history contains correct information about trends`() = runTest {
        val referentialValue = 5.0.toBigDecimal()
        val thresholdPercent = 0.05.toBigDecimal()
        val minimalValue = referentialValue - 2.toBigDecimal() * thresholdPercent
        val step = ((referentialValue + thresholdPercent * 2.toBigDecimal()) -
                minimalValue) / RateHistoryTestData.rateHistory.rates.size.toBigDecimal()
        val testRates = RateHistoryTestData.rateHistory.rates.mapIndexed { index, rate ->
            rate.copy(middleValue = rate.middleValue - thresholdPercent + index.toBigDecimal() * step)
        }

        val testRateHistory = RateHistoryTestData.rateHistory.copy(rates = testRates)

        coEvery {
            repository.getRateHistory(any(), any(), any(), any())
        } coAnswers {
            delay(1.seconds)
            Result.success(testRateHistory)
        }

        viewModel.uiState.test {
            testScheduler.advanceUntilIdle()

            skipItems(2)
            awaitItem().let {
                it.rateHistory?.rates?.all { rate ->
                    if (rate.displayMiddleValue.toBigDecimal() !in (referentialValue - thresholdPercent)..(referentialValue + thresholdPercent)) {
                        rate.trend == DailyRateUiState.RateTrend.LARGE_DIFFERENCE
                    } else {
                        rate.trend == DailyRateUiState.RateTrend.NEUTRAL
                    }
                }
            }
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