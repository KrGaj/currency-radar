package com.example.currencyradar.data.repository

import com.example.currencyradar.data.remote.client.ApiClient
import com.example.currencyradar.data.remote.dto.CurrencyTableDto
import com.example.currencyradar.data.remote.dto.CurrencyTableRateDto
import com.example.currencyradar.domain.models.Currency
import com.example.currencyradar.domain.models.CurrencyTableType
import com.example.currencyradar.domain.models.CurrentRate
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
        coEvery { apiClient.getCurrentRatesTable(CurrencyTableType.A) } returns tableA

        val result = repository.getCurrentRates(CurrencyTableType.A)
        result shouldBe Result.success(currentRates)
    }

    @Test
    fun `Repository returns failure with exception`() = runTest {
        val expectedException = IllegalStateException()

        coEvery { apiClient.getCurrentRatesTable(any()) } throws expectedException

        val result = repository.getCurrentRates(CurrencyTableType.A)
        result shouldBe Result.failure(expectedException)
    }

    companion object {
        private val rates = listOf(
            CurrencyTableRateDto(
                currency = "dolar kanadyjski",
                code = "CAD",
                mid = 2.6181.toBigDecimal(),
            ),
            CurrencyTableRateDto(
                currency = "euro",
                code = "EUR",
                mid = 4.2271.toBigDecimal(),
            ),
            CurrencyTableRateDto(
                currency = "korona norweska",
                code = "NOK",
                mid = 0.3569.toBigDecimal(),
            ),
        )

        private val tableA = CurrencyTableDto(
            tableName = "A",
            number = "241/A/NBP/2025",
            effectiveDate = "2025-12-12",
            rates = rates,
        )

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
