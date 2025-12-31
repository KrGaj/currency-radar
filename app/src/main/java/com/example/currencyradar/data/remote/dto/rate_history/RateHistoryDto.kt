package com.example.currencyradar.data.remote.dto.rate_history

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RateHistoryDto(
    val table: String,
    @SerialName("currency") val currencyName: String,
    @SerialName("code") val currencyCode: String,
    val rates: List<DailyRateDto>,
)
