package com.example.currencyradar.data.repository

import com.example.currencyradar.domain.models.CurrencyTableType
import com.example.currencyradar.domain.models.CurrentRate
import com.example.currencyradar.domain.repository.CurrentRatesRepository
import io.ktor.client.HttpClient
import org.koin.core.annotation.Factory

@Factory(binds = [CurrentRatesRepository::class])
class CurrentRatesRepositoryDefault(
    private val httpClient: HttpClient,
) : CurrentRatesRepository {
    override suspend fun getTable(
        type: CurrencyTableType,
    ): List<CurrentRate> {
        TODO("Not yet implemented")
    }
}
