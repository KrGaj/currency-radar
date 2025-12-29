package com.example.currencyradar.app.ui.rate_history

import com.example.currencyradar.domain.models.DailyRate
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.char


fun DailyRate.toDailyRateUiState(): DailyRateUiState {
    val formatter = LocalDate.Format {
        day()
        char('.')
        monthNumber()
        char('.')
        year()
    }

    return DailyRateUiState(
        displayDate = formatter.format(date),
        displayMiddleValue = middleValue.toString(),
    )
}
