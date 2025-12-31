package com.example.currencyradar.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.currencyradar.app.ui.current_rates.CurrentRatesScreen
import com.example.currencyradar.app.ui.rate_history.RateHistoryScreen
import com.example.currencyradar.app.ui.rate_history.RateHistoryViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parameterSetOf

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(Screen.CurrentRates)

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<Screen.CurrentRates> {
                CurrentRatesScreen(
                    onCurrencyListItemClick = { currencyCode, tableType ->
                        backStack.add(
                            Screen.RateHistory(
                                currencyCode = currencyCode,
                                tableType = tableType,
                            )
                        )
                    },
                )
            }

            entry<Screen.RateHistory> {
                val viewModel: RateHistoryViewModel = koinViewModel {
                    parameterSetOf(it.currencyCode, it.tableType)
                }

                RateHistoryScreen(
                    rateHistoryViewModel = viewModel,
                )
            }
        }
    )
}
