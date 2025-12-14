package com.example.currencyradar.domain.repository

import com.example.currencyradar.domain.models.CurrencyTableType
import com.example.currencyradar.domain.models.CurrentRate

fun interface CurrentRatesRepository {
    suspend fun getTable(
        type: CurrencyTableType,
    ): List<CurrentRate>
}
