package com.example.currencyradar.app.ui.common.mapper

import com.example.currencyradar.app.ui.common.models.CurrencyUiState
import com.example.currencyradar.app.ui.common.toCapitalized
import com.example.currencyradar.domain.models.Currency

fun Currency.toCurrencyUiState() = CurrencyUiState(
    displayName = name.toCapitalized(),
    displayCode = code.uppercase(),
)
