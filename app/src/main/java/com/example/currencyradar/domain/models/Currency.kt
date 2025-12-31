package com.example.currencyradar.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Currency(
    val name: String,
    val code: String,
)
