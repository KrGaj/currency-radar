package com.example.currencyradar.data.repository

import com.example.currencyradar.data.remote.client.ApiClient
import com.example.currencyradar.domain.models.CurrencyTableType
import com.example.currencyradar.test_data.CurrentRatesTestData
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CurrentRatesRepositoryDefaultTest {
    private lateinit var apiClient: ApiClient
    private lateinit var repository: CurrentRatesRepositoryDefault

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        apiClient = mockk()
        repository = CurrentRatesRepositoryDefault(
            apiClient = apiClient,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    @Test
    fun `Repository returns success with list of recent rates`() = runTest {
        coEvery { apiClient.getCurrentRatesTable(CurrencyTableType.A) } returns CurrentRatesTestData.tableA

        val result = repository.getCurrentRates(CurrencyTableType.A)
        result shouldBe Result.success(CurrentRatesTestData.currentRates)
    }

    @Test
    fun `Repository returns failure with exception`() = runTest {
        val expectedException = IllegalStateException()

        coEvery { apiClient.getCurrentRatesTable(any()) } throws expectedException

        val result = repository.getCurrentRates(CurrencyTableType.A)
        result shouldBe Result.failure(expectedException)
    }
}
