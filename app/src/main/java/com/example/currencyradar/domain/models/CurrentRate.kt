package com.example.currencyradar.domain.models

import java.math.BigDecimal

data class CurrentRate(
    val currency: Currency,
    val middleValue: BigDecimal,
)
