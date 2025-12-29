package com.example.currencyradar.app.ui.current_rates

import androidx.compose.runtime.Immutable
import com.example.currencyradar.app.ui.common.models.CurrencyUiState
import com.example.currencyradar.domain.models.TableType

@Immutable
data class CurrentRatesUiState(
    val currentRates: List<CurrentRateUiState> = emptyList(),
    val isLoading: Boolean = false,
    val tableType: TableType = TableType.A,
    val error: Throwable? = null,
)

@Immutable
data class CurrentRateUiState(
    val displayCurrency: CurrencyUiState,
    val displayMiddleValue: String,
)
