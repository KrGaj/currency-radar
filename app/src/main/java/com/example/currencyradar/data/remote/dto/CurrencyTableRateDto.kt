package com.example.currencyradar.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyTableRateDto(
    val currency: String,
    val code: String,
    val mid: Double,
)
