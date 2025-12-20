package com.example.currencyradar.app.ui.current_rates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyradar.app.common.onFirstSubscription
import com.example.currencyradar.domain.models.TableType
import com.example.currencyradar.domain.repository.CurrentRatesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import kotlin.properties.Delegates

@KoinViewModel
class CurrentRatesViewModel(
    private val currentRatesRepository: CurrentRatesRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CurrentRatesUiState())

    val uiState
        get() = _uiState.asStateFlow()

    init {
        _uiState.onFirstSubscription(
            scope = viewModelScope,
        ) {
            val tableType = TableType.entries[_uiState.value.selectedTabIndex]
            getCurrentRates(tableType)
        }
    }

    fun getCurrentRates(
        tableType: TableType,
    ) {
        var oldTabIndex by Delegates.notNull<Int>()

        _uiState.update {
            oldTabIndex = it.selectedTabIndex

            CurrentRatesUiState(
                isLoading = true,
                selectedTabIndex = tableType.ordinal,
            )
        }

        viewModelScope.launch {
            val ratesResult = currentRatesRepository.getCurrentRates(tableType)

            _uiState.update {
                ratesResult.fold(
                    onSuccess = { data -> it.copy(currentRates = data, isLoading = false) },
                    onFailure = { error -> it.copy(error = error, isLoading = false, selectedTabIndex = oldTabIndex) },
                )
            }
        }
    }

    fun onErrorMessageShown() {
        _uiState.update { it.copy(error = null) }
    }
}
