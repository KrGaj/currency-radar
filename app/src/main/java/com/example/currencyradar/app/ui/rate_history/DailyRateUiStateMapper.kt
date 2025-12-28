package com.example.currencyradar.app.ui.rate_history

import com.example.currencyradar.domain.models.DailyRate
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.char


fun DailyRate.toDailyRateUiState(): RateHistoryUiState.DailyRateUiState {
    val formatter = LocalDate.Format {
        day()
        char('.')
        monthNumber()
        char('.')
        year()
    }

    return RateHistoryUiState.DailyRateUiState(
        date = formatter.format(date),
        middleValue = middleValue.toString(),
    )
}
