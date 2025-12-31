package com.example.currencyradar.data.mapper

import com.example.currencyradar.data.remote.dto.rate_history.RateHistoryDto
import com.example.currencyradar.domain.models.Currency
import com.example.currencyradar.domain.models.RateHistory

fun RateHistoryDto.toRateHistory(): RateHistory {
    val currency = Currency(
        name = currencyName,
        code = currencyCode,
    )

    val rates = rates.map { it.toDailyRate() }

    return RateHistory(
        currency = currency,
        rates = rates,
    )
}
