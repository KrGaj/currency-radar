package com.example.currencyradar.data.remote.client

import com.example.currencyradar.domain.models.CurrencyTableType
import com.example.currencyradar.test_data.CurrentRatesTestData
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
    }

    @Test
    fun `Client returns correct table`() = runTest {
        val response = apiClient.getCurrentRatesTable(CurrencyTableType.A)
        response shouldBe CurrentRatesTestData.tableA
    }
}
