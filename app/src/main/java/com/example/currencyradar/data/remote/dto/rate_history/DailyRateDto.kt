package com.example.currencyradar.data.remote.dto.rate_history

import com.example.currencyradar.data.serialization.BigDecimalSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class DailyRateDto(
    @SerialName("no") val rateNumber: String,
    val effectiveDate: LocalDate,
    @Serializable(with = BigDecimalSerializer::class) val mid: BigDecimal,
)
