package com.example.currencyradar.test_data

import com.example.currencyradar.data.mapper.toDailyRates
import com.example.currencyradar.data.remote.dto.rate_history.DailyRateDto
import com.example.currencyradar.data.remote.dto.rate_history.RateHistoryDto
import com.example.currencyradar.domain.models.Currency
import kotlinx.datetime.LocalDate

object RateHistoryTestData {
    val rateHistoryDtoEntries = listOf(
        DailyRateDto(
            rateNumber = "237/A/NBP/2025",
            effectiveDate = LocalDate(
                year = 2025,
                month = 12,
                day = 8,
            ),
            mid = 4.2289.toBigDecimal(),
        ),
        DailyRateDto(
            rateNumber = "238/A/NBP/2025",
            effectiveDate = LocalDate(
                year = 2025,
                month = 12,
                day = 9,
            ),
            mid = 4.2340.toBigDecimal(),
        ),
        DailyRateDto(
            rateNumber = "239/A/NBP/2025",
            effectiveDate = LocalDate(
                year = 2025,
                month = 12,
                day = 10,
            ),
            mid = 4.2274.toBigDecimal(),
        ),
        DailyRateDto(
            rateNumber = "240/A/NBP/2025",
            effectiveDate = LocalDate(
                year = 2025,
                month = 12,
                day = 11,
            ),
            mid = 4.2284.toBigDecimal(),
        ),
        DailyRateDto(
            rateNumber = "241/A/NBP/2025",
            effectiveDate = LocalDate(
                year = 2025,
                month = 12,
                day = 12,
            ),
            mid = 4.2271.toBigDecimal(),
        ),
    )

    val rateHistoryDto = RateHistoryDto(
        table = "A",
        currencyName = "euro",
        currencyCode = "EUR",
        rates = rateHistoryDtoEntries,
    )

    val rateHistory = rateHistoryDto.toDailyRates()

    val currency = Currency(
        rateHistoryDto.currencyName,
        rateHistoryDto.currencyCode,
    )
}
