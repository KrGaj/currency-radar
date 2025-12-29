package com.example.currencyradar.data.repository

import com.example.currencyradar.data.mapper.toRateHistory
import com.example.currencyradar.data.remote.client.ApiClient
import com.example.currencyradar.domain.models.RateHistory
import com.example.currencyradar.domain.models.TableType
import com.example.currencyradar.domain.repository.RateHistoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import org.koin.core.annotation.Factory

@Factory(binds = [RateHistoryRepository::class])
class RateHistoryRepositoryDefault(
    private val apiClient: ApiClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : RateHistoryRepository {
    override suspend fun getRateHistory(
        currencyCode: String,
        tableType: TableType,
        from: LocalDate,
        to: LocalDate,
    ): Result<RateHistory> = withContext(dispatcher) {
        Result.runCatching {
            val response = apiClient.getRateHistory(
                currencyCode = currencyCode,
                tableType = tableType,
                from = from,
                to = to,
            )

            return@runCatching response.toRateHistory()
        }
    }
}
