package com.example.currencyradar.app.ui.current_rates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyradar.domain.models.TableType
import com.example.currencyradar.domain.repository.CurrentRatesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CurrentRatesViewModel(
    private val currentRatesRepository: CurrentRatesRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CurrentRatesUiState())
    val uiState = _uiState.onStart {
        val tableType = _uiState.value.tableType
        getCurrentRates(tableType)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = CurrentRatesUiState(),
    )

    fun getCurrentRates(
        tableType: TableType,
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                )
            }

            val ratesResult = currentRatesRepository.getCurrentRates(tableType)

            _uiState.update {
                ratesResult.fold(
                    onSuccess = { data -> it.copy(currentRates = data, isLoading = false, tableType = tableType) },
                    onFailure = { error -> it.copy(error = error, isLoading = false) },
                )
            }
        }
    }

    fun onErrorMessageShown() {
        _uiState.update { it.copy(error = null) }
    }
}
