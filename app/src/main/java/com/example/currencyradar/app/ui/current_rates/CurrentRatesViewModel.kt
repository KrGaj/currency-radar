package com.example.currencyradar.app.ui.current_rates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyradar.domain.models.CurrencyTableType
import com.example.currencyradar.domain.repository.CurrentRatesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class CurrentRatesViewModel(
    private val currentRatesRepository: CurrentRatesRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CurrentRatesUiState())
    val uiState
        get() = _uiState.asStateFlow()

    fun getCurrentRates(
        tableType: CurrencyTableType,
    ) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = currentRatesRepository.getCurrentRates(tableType)

            _uiState.update {
                result.fold(
                    onSuccess = { data -> CurrentRatesUiState(currentRates = data) },
                    onFailure = { error -> CurrentRatesUiState(error = error) },
                )
            }
        }
    }
}
