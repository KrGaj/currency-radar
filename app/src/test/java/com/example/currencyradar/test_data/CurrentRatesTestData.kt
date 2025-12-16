package com.example.currencyradar.test_data

import com.example.currencyradar.data.remote.dto.current_rates.CurrentRatesTableDto
import com.example.currencyradar.data.remote.dto.current_rates.CurrentRateDto
import com.example.currencyradar.domain.models.Currency
import com.example.currencyradar.domain.models.CurrentRate

object CurrentRatesTestData {
    val rates = listOf(
        CurrentRateDto(
            currency = "dolar kanadyjski",
            code = "CAD",
            mid = 2.6181.toBigDecimal(),
        ),
        CurrentRateDto(
            currency = "euro",
            code = "EUR",
            mid = 4.2271.toBigDecimal(),
        ),
        CurrentRateDto(
            currency = "korona norweska",
            code = "NOK",
            mid = 0.3569.toBigDecimal(),
        ),
    )

    val tableA = CurrentRatesTableDto(
        tableName = "A",
        number = "241/A/NBP/2025",
        effectiveDate = "2025-12-12",
        rates = rates,
    )

    val tableB = CurrentRatesTableDto(
        tableName = "B",
        number = "241/A/NBP/2025",
        effectiveDate = "2025-12-12",
        rates = rates.dropLast(1),
    )

    val currentRates = listOf(
        CurrentRate(
            currency = Currency(
                name = "dolar kanadyjski",
                code = "CAD",
            ),
            middleValue = 2.6181.toBigDecimal(),
        ),
        CurrentRate(
            currency = Currency(
                name = "euro",
                code = "EUR",
            ),
            middleValue = 4.2271.toBigDecimal(),
        ),
        CurrentRate(
            currency = Currency(
                name = "korona norweska",
                code = "NOK",
            ),
            middleValue = 0.3569.toBigDecimal(),
        ),
    )
}
