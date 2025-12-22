package com.example.currencyradar.data.remote.client

import com.example.currencyradar.domain.models.Currency
import com.example.currencyradar.domain.models.TableType
import com.example.currencyradar.test_data.CurrentRatesTestData
import com.example.currencyradar.test_data.RateHistoryTestData
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test

class ApiClientTest {
    private lateinit var mockEngine: MockEngine
    private lateinit var apiClient: ApiClient

    @Test
    fun `Client returns correct table`() = runTest {
        mockEngine = MockEngine { request ->
            val urlSegments = request.url.segments

            val content = if (urlSegments.contains("A")) {
                Json.encodeToString(listOf(CurrentRatesTestData.tableA))
            } else {
                Json.encodeToString(listOf(CurrentRatesTestData.tableB))
            }

            respond(
                content = content,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }

        apiClient = ApiClient(mockEngine)

        val response = apiClient.getCurrentRatesTable(TableType.A)
        response shouldBe CurrentRatesTestData.tableA
    }

    @Test
    fun `Client returns correct rate history`() = runTest {
        val testData = RateHistoryTestData.rateHistoryDto
        val currency = Currency(
            name = testData.currencyName,
            code = testData.currencyCode,
        )

        mockEngine = MockEngine {
            val content = Json.encodeToString(testData)

            respond(
                content = content,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }

        apiClient = ApiClient(mockEngine)

        val response = apiClient.getRateHistory(
            currency = currency,
            tableType = TableType.valueOf(testData.table),
            from = testData.rates.first().effectiveDate,
            to = testData.rates.last().effectiveDate,
        )

        response shouldBe testData
    }
}
