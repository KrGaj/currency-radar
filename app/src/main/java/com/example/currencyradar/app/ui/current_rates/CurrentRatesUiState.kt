package com.example.currencyradar.app.ui.current_rates

import com.example.currencyradar.domain.models.CurrencyTableType
import com.example.currencyradar.domain.models.CurrentRate

data class CurrentRatesUiState(
    val currentRates: List<CurrentRate> = emptyList(),
    val isLoading: Boolean = false,
    val selectedTabIndex: Int = CurrencyTableType.A.ordinal,
    val error: Throwable? = null,
)
