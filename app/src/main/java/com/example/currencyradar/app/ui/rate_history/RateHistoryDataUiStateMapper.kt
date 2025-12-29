package com.example.currencyradar.app.ui.rate_history

import com.example.currencyradar.app.ui.common.mapper.toCurrencyUiState
import com.example.currencyradar.domain.models.DailyRate
import com.example.currencyradar.domain.models.RateHistory
import java.math.BigDecimal

fun RateHistory.toRateHistoryDataUiState(
    referentialRate: DailyRate,
    thresholdPercent: BigDecimal,
): RateHistoryDataUiState {
    val dailyRateUiStates = rates
        .takeIf { it.isNotEmpty() }
        ?.sortedByDescending { it.date }
        ?.map {
            it.toDailyRateUiState(
                referentialDailyRate = referentialRate,
                thresholdPercent = thresholdPercent,
            )
        } ?: emptyList()

    return RateHistoryDataUiState(
        currency = currency.toCurrencyUiState(),
        rates = dailyRateUiStates,
    )
}
