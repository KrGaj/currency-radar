package com.example.currencyradar.data.repository

import com.example.currencyradar.data.mapper.toCurrentRates
import com.example.currencyradar.data.remote.client.ApiClient
import com.example.currencyradar.domain.models.CurrencyTableType
import com.example.currencyradar.domain.models.CurrentRate
import com.example.currencyradar.domain.repository.CurrentRatesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory(binds = [CurrentRatesRepository::class])
class CurrentRatesRepositoryDefault(
    private val apiClient: ApiClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CurrentRatesRepository {
    override suspend fun getTable(
        type: CurrencyTableType,
    ): Result<List<CurrentRate>> = withContext(dispatcher) {
        Result.runCatching {
            val response = apiClient.getCurrentRatesTable(type)
            return@runCatching response.toCurrentRates()
        }
    }
}
