package com.example.currencyradar.domain.repository

import com.example.currencyradar.domain.models.Currency
import com.example.currencyradar.domain.models.DailyRate
import com.example.currencyradar.domain.models.TableType
import kotlinx.datetime.LocalDate

fun interface RateHistoryRepository {
    suspend fun getRateHistory(
        currency: Currency,
        tableType: TableType,
        from: LocalDate,
        to: LocalDate,
    ): Result<List<DailyRate>>
}
