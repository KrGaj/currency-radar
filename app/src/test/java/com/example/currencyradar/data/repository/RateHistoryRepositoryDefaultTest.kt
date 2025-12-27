package com.example.currencyradar.data.repository

import com.example.currencyradar.data.remote.client.ApiClient
import com.example.currencyradar.domain.models.TableType
import com.example.currencyradar.test_data.RateHistoryTestData
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RateHistoryRepositoryDefaultTest {
    private lateinit var apiClient: ApiClient
    private lateinit var repository: RateHistoryRepositoryDefault

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        apiClient = mockk()
        repository = RateHistoryRepositoryDefault(
            apiClient = apiClient,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `Repository returns result wrapping single currency rate history`() = runTest {
        val expectedResult = Result.success(RateHistoryTestData.rateHistory)

        val tableType = TableType.valueOf(RateHistoryTestData.rateHistoryDto.table)
        val dataFrom = RateHistoryTestData.rateHistoryDto.rates.first().effectiveDate
        val dataTo = RateHistoryTestData.rateHistoryDto.rates.last().effectiveDate

        coEvery {
            apiClient.getRateHistory(any(), any(), any(), any())
        } returns RateHistoryTestData.rateHistoryDto

        val result = repository.getRateHistory(RateHistoryTestData.currency, tableType, dataFrom, dataTo)

        result shouldBe expectedResult
    }

    @Test
    fun `Repository returns result wrapping an exception`() = runTest {
        val expectedException = IllegalStateException()

        coEvery {
            apiClient.getRateHistory(any(), any(), any(), any())
        } throws expectedException

        val result = repository.getRateHistory(mockk(), mockk(), mockk(), mockk())
        result shouldBe Result.failure(expectedException)
    }
}
