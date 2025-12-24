package com.example.currencyradar.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.currencyradar.app.ui.current_rates.CurrentRatesScreen

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(Screen.CurrentRates)

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<Screen.CurrentRates> {
                CurrentRatesScreen(
                    onCurrencyListItemClick = {},
                )
            }
        }
    )
}
