package com.example.currencyradar.app.ui.rate_history

import androidx.compose.runtime.Immutable
import com.example.currencyradar.domain.models.Currency

@Immutable
data class RateHistoryUiState(
    val currency: Currency,
    val rateHistory: List<DailyRateUiState> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null,
) {
    @Immutable
    data class DailyRateUiState(
        val date: String,
        val middleValue: String,
    )
}
