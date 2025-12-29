package com.example.currencyradar.domain.models

data class RateHistory(
    val currency: Currency,
    val rates: List<DailyRate>,
)
