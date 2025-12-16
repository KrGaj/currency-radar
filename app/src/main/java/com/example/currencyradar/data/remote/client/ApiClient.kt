package com.example.currencyradar.data.remote.client

import com.example.currencyradar.data.remote.dto.current_rates.CurrentRatesTableDto
import com.example.currencyradar.data.remote.client.resources.Tables
import com.example.currencyradar.domain.models.CurrencyTableType
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.contentnegotiation.ContentNegotiationConfig
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.plugins.resources.get
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single
import kotlin.time.Duration.Companion.seconds


@Single
class ApiClient(
    engine: HttpClientEngine = OkHttp.create(),
) {
    private val client = HttpClient(engine) {
        install(Resources)
        install(ContentNegotiation, ::configureContentNegotiation)
        install(HttpTimeout) {
            requestTimeoutMillis = 10.seconds.inWholeMilliseconds
        }

        configureDefaultRequest()
        expectSuccess = true
    }

    private fun configureContentNegotiation(
        configure: ContentNegotiationConfig,
    ) = configure.json(
        Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        }
    )

    private fun HttpClientConfig<*>.configureDefaultRequest() = defaultRequest {
        url {
            protocol = URLProtocol.HTTPS
            host = "api.nbp.pl"
            path("/api/exchangerates/")
        }
        contentType(ContentType.Application.Json)
    }

    suspend fun getCurrentRatesTable(
        table: CurrencyTableType,
    ): CurrentRatesTableDto {
        val response = client.get(
            Tables.Table(
                table = table.name,
            ),
        )

        val tables: List<CurrentRatesTableDto> = response.body()
        return tables.last()
    }
}
