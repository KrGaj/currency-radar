package com.example.currencyradar.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyTableDto(
    @SerialName("table") val tableName: String,
    @SerialName("no") val number: String,
    val effectiveDate: String,
    val rates: List<CurrencyTableRateDto>,
)
