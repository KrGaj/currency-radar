package com.example.currencyradar.app.ui.rate_history

import com.example.currencyradar.domain.models.DailyRate

data class RateHistoryUiState(
    val rateHistory: List<DailyRate> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null,
)
