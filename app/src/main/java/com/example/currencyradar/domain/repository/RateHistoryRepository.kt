package com.example.currencyradar.domain.repository

import com.example.currencyradar.domain.models.RateHistory
import com.example.currencyradar.domain.models.TableType
import kotlinx.datetime.LocalDate

fun interface RateHistoryRepository {
    suspend fun getRateHistory(
        currencyCode: String,
        tableType: TableType,
        from: LocalDate,
        to: LocalDate,
    ): Result<RateHistory>
}
