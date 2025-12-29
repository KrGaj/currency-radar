package com.example.currencyradar.app.ui.rate_history

import com.example.currencyradar.domain.models.DailyRate
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.char
import java.math.BigDecimal


fun DailyRate.toDailyRateUiState(
    referentialDailyRate: DailyRate,
    thresholdPercent: BigDecimal,
): DailyRateUiState {
    val formatter = LocalDate.Format {
        day()
        char('.')
        monthNumber()
        char('.')
        year()
    }

    val trend = calculateTrend(
        referentialRateValue = referentialDailyRate.middleValue,
        dailyRateValue = this.middleValue,
        thresholdPercent = thresholdPercent,
    )

    return DailyRateUiState(
        displayDate = formatter.format(date),
        displayMiddleValue = middleValue.toString(),
        trend = trend,
    )
}

private fun calculateTrend(
    referentialRateValue: BigDecimal,
    dailyRateValue: BigDecimal,
    thresholdPercent: BigDecimal,
): DailyRateUiState.RateTrend {
    val threshold = referentialRateValue * thresholdPercent

    return when {
        dailyRateValue > referentialRateValue + threshold
                || dailyRateValue < referentialRateValue - threshold -> DailyRateUiState.RateTrend.LARGE_DIFFERENCE
        else -> DailyRateUiState.RateTrend.NEUTRAL
    }
}
