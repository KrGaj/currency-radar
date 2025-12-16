package com.example.currencyradar.data.remote.dto.current_rates

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentRatesTableDto(
    @SerialName("table") val tableName: String,
    @SerialName("no") val number: String,
    val effectiveDate: String,
    val rates: List<CurrentRateDto>,
)
