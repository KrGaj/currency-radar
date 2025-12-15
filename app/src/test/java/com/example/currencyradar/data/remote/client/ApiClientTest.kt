package com.example.currencyradar.data.remote.client

import com.example.currencyradar.data.remote.dto.CurrencyTableDto
import com.example.currencyradar.data.remote.dto.CurrencyTableRateDto
import com.example.currencyradar.domain.models.CurrencyTableType
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test

class ApiClientTest {
    private lateinit var mockEngine: MockEngine
    private lateinit var apiClient: ApiClient

    @Before
    fun setUp() {
        mockEngine = MockEngine { request ->
            val urlSegments = request.url.segments

            val content = if (urlSegments.contains("A")) {
                Json.encodeToString(listOf(tableA))
            } else {
                Json.encodeToString(listOf(tableB))
            }

            respond(
                content = content,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }

        apiClient = ApiClient(mockEngine)
    }

    @Test
    fun `Client returns correct table`() = runTest {
        val response = apiClient.getCurrentRatesTable(CurrencyTableType.A)
        response shouldBe tableA
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

        private val tableB = CurrencyTableDto(
            tableName = "B",
            number = "241/A/NBP/2025",
            effectiveDate = "2025-12-12",
            rates = rates.dropLast(1),
        )
    }
}
