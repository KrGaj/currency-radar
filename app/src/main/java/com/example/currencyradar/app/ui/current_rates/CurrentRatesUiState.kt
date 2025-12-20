package com.example.currencyradar.app.ui.current_rates

import com.example.currencyradar.domain.models.TableType
import com.example.currencyradar.domain.models.CurrentRate

data class CurrentRatesUiState(
    val currentRates: List<CurrentRate> = emptyList(),
    val isLoading: Boolean = false,
    val selectedTabIndex: Int = TableType.A.ordinal,
    val error: Throwable? = null,
)
