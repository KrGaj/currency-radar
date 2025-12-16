package com.example.currencyradar.data.mapper

import com.example.currencyradar.data.remote.dto.current_rates.CurrentRatesTableDto
import com.example.currencyradar.domain.models.Currency
import com.example.currencyradar.domain.models.CurrentRate

fun CurrentRatesTableDto.toCurrentRates(): List<CurrentRate> = rates.map {
    val currency = Currency(
        name = it.currency,
        code = it.code,
    )

    CurrentRate(
        currency = currency,
        middleValue = it.mid,
    )
}
