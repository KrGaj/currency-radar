package com.example.currencyradar.domain.repository

import com.example.currencyradar.domain.models.TableType
import com.example.currencyradar.domain.models.CurrentRate

fun interface CurrentRatesRepository {
    suspend fun getCurrentRates(
        tableType: TableType,
    ): Result<List<CurrentRate>>
}
