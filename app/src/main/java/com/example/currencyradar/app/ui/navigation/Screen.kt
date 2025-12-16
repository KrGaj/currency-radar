package com.example.currencyradar.app.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    object CurrentRates : Screen()
}
