package com.example.currencyradar.app.ui.current_rates

import com.example.currencyradar.app.ui.common.mapper.toCurrencyUiState
import com.example.currencyradar.domain.models.CurrentRate

fun CurrentRate.toCurrentRateUiState() = CurrentRateUiState(
    displayCurrency = currency.toCurrencyUiState(),
    displayMiddleValue = middleValue.stripTrailingZeros().toPlainString(),
)
