package com.example.currencyradar.data.remote.dto.current_rates

import com.example.currencyradar.data.serialization.BigDecimalSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class CurrentRateDto(
    val currency: String,
    val code: String,
    @Serializable(with = BigDecimalSerializer::class) val mid: BigDecimal,
)
