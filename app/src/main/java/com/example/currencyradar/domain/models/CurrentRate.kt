package com.example.currencyradar.domain.models

data class CurrentRate(
    val currency: Currency,
    val middleValue: Double,
)
