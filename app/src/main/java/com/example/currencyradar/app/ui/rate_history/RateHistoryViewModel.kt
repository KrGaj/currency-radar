package com.example.currencyradar.app.ui.rate_history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyradar.domain.models.Currency
import com.example.currencyradar.domain.models.DailyRate
import com.example.currencyradar.domain.models.TableType
import com.example.currencyradar.domain.repository.RateHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam
import kotlin.time.Clock

@KoinViewModel
class RateHistoryViewModel(
    private val repository: RateHistoryRepository,
    @InjectedParam private val currency: Currency,
    @InjectedParam private val table: TableType,
    private val clock: Clock = Clock.System,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        RateHistoryUiState(
            currency = currency,
        )
    )

    val uiState = _uiState.onStart {
        getRateHistory()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = RateHistoryUiState(
            currency = currency,
        ),
    )

    fun getRateHistory(
        latestDaysRange: DatePeriod = DatePeriod(days = 14),
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val dateTo = clock.todayIn(TimeZone.currentSystemDefault())
            val dateFrom = dateTo - latestDaysRange

            val result = repository.getRateHistory(
                currency = currency,
                tableType = table,
                from = dateFrom,
                to = dateTo,
            )

            _uiState.update { state ->
                produceStateFromResult(state, result)
            }
        }
    }

    private fun produceStateFromResult(
        currentState: RateHistoryUiState,
        result: Result<List<DailyRate>>,
    ) = result.fold(
        onSuccess = { rates ->
            val ratesSorted = rates.sortedByDescending { it.date }
            val rateUiStates = ratesSorted.map { it.toDailyRateUiState() }

            currentState.copy(
                rateHistory = rateUiStates,
                isLoading = false,
                error = null,
            )
        },
        onFailure = {
            currentState.copy(
                error = it,
                isLoading = false,
            )
        },
    )
}
