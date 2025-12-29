package com.example.currencyradar.app.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.example.currencyradar.domain.models.Currency
import com.example.currencyradar.domain.models.TableType
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen : NavKey {
    @Serializable
    object CurrentRates : Screen()

    @Serializable
    data class RateHistory(
        val currency: Currency,
        val tableType: TableType,
    ) : Screen()
}
