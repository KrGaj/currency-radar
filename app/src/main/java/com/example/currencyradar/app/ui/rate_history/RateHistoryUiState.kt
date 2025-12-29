package com.example.currencyradar.app.ui.rate_history

import androidx.compose.runtime.Immutable
import com.example.currencyradar.app.ui.common.models.CurrencyUiState

@Immutable
data class RateHistoryUiState(
    val rateHistory: RateHistoryDataUiState? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null,
)

@Immutable
data class RateHistoryDataUiState(
    val currency: CurrencyUiState,
    val rates: List<DailyRateUiState>,
)

@Immutable
data class DailyRateUiState(
    val displayDate: String,
    val displayMiddleValue: String,
)
