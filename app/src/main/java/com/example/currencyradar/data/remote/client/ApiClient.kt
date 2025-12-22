package com.example.currencyradar.data.remote.client

import com.example.currencyradar.data.remote.client.resources.Rates
import com.example.currencyradar.data.remote.dto.current_rates.CurrentRatesTableDto
import com.example.currencyradar.data.remote.client.resources.Tables
import com.example.currencyradar.data.remote.dto.rate_history.RateHistoryDto
import com.example.currencyradar.domain.models.Currency
import com.example.currencyradar.domain.models.TableType
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
import kotlinx.datetime.LocalDate
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
        table: TableType,
    ): CurrentRatesTableDto {
        val response = client.get(
            Tables.Table(
                table = table,
            ),
        )

        val tables: List<CurrentRatesTableDto> = response.body()
        return tables.last()
    }

    suspend fun getRateHistory(
        currency: Currency,
        tableType: TableType,
        from: LocalDate,
        to: LocalDate,
    ): RateHistoryDto {
        val ratesTableResource = Rates.Table(
            table = tableType,
        )
        val currencyResource = Rates.Table.Code(
            parent = ratesTableResource,
            code = currency.code,
        )
        val ratesInRangeResource = Rates.Table.Code.DateRange(
            parent = currencyResource,
            startDate = from,
            endDate = to,
        )

        val response = client.get(
            ratesInRangeResource,
        )

        return response.body()
    }
}
