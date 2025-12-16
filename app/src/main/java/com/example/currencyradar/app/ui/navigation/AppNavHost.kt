package com.example.currencyradar.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.currencyradar.app.ui.current_rates.CurrentRatesScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.CurrentRates,
    ) {
        composable<Screen.CurrentRates> {
            CurrentRatesScreen()
        }
    }
}
