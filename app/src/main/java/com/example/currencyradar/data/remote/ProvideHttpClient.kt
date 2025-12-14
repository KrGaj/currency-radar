package com.example.currencyradar.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.contentnegotiation.ContentNegotiationConfig
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single
import kotlin.time.Duration.Companion.seconds

@Single
fun provideHttpClient() = HttpClient(OkHttp) {
    install(Resources)
    install(ContentNegotiation, ::configureContentNegotiation)
    install(HttpTimeout) {
        requestTimeoutMillis = 10.seconds.inWholeMilliseconds
    }

    configureDefaultRequest()
}

private fun HttpClientConfig<*>.configureDefaultRequest() = defaultRequest {
    url {
        protocol = URLProtocol.HTTPS
        host = "api.nbp.pl"
        path("/api")
    }
    contentType(ContentType.Application.Json)
}

private fun configureContentNegotiation(
    configure: ContentNegotiationConfig,
) = configure.json(
    Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }
)