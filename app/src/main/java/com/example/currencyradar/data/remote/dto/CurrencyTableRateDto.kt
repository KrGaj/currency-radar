package com.example.currencyradar.data.remote.dto

import com.example.currencyradar.data.serialization.BigDecimalSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class CurrencyTableRateDto(
    val currency: String,
    val code: String,
    @Serializable(with = BigDecimalSerializer::class) val mid: BigDecimal,
)
