package com.example.currencyradar.app.ui.rate_history

import com.example.currencyradar.app.ui.common.mapper.toCurrencyUiState
import com.example.currencyradar.domain.models.RateHistory

fun RateHistory.toRateHistoryDataUiState() = RateHistoryDataUiState(
    currency = currency.toCurrencyUiState(),
    rates = rates
        .sortedByDescending { it.date }
        .map { it.toDailyRateUiState() },
)
