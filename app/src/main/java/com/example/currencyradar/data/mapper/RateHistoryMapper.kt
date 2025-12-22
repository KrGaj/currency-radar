package com.example.currencyradar.data.mapper

import com.example.currencyradar.data.remote.dto.rate_history.RateHistoryDto
import com.example.currencyradar.domain.models.DailyRate

fun RateHistoryDto.toDailyRates() = rates.map {
    DailyRate(
        date = it.effectiveDate,
        middleValue = it.mid,
    )
}
