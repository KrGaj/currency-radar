package com.example.currencyradar.data.mapper

import com.example.currencyradar.data.remote.dto.rate_history.DailyRateDto
import com.example.currencyradar.domain.models.DailyRate

fun DailyRateDto.toDailyRate() = DailyRate(
    date = effectiveDate,
    middleValue = mid,
)
