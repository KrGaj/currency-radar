package com.example.currencyradar.domain.models

import kotlinx.datetime.LocalDate
import java.math.BigDecimal

data class DailyRate(
    val date: LocalDate,
    val middleValue: BigDecimal,
)
