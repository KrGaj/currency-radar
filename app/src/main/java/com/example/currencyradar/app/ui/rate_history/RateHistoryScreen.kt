package com.example.currencyradar.app.ui.rate_history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.currencyradar.R
import com.example.currencyradar.app.ui.theme.CurrencyRadarTheme
import com.example.currencyradar.app.ui.theme.Typography
import com.example.currencyradar.domain.models.Currency
import kotlinx.coroutines.launch

@Composable
fun RateHistoryScreen(
    rateHistoryViewModel: RateHistoryViewModel,
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by rateHistoryViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
    ) { innerPadding ->
        uiState.error?.let {
            val errorMsg = stringResource(R.string.fetch_error_msg)
            coroutineScope.launch {
                snackbarHostState.showSnackbar(errorMsg)
                rateHistoryViewModel.onErrorMessageShown()
            }
        }

        RateHistoryScreen(
            modifier = Modifier.padding(innerPadding),
            uiState = uiState,
        )
    }
}

@Composable
private fun RateHistoryScreen(
    modifier: Modifier = Modifier,
    uiState: RateHistoryUiState,
) {
    Column(
        modifier = Modifier.fillMaxSize().then(modifier),
    ) {
        CurrencyDetails(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            currency = uiState.currency,
        )
        RateHistoryList(
            rateHistory = uiState.rateHistory,
        )
    }
}

@Composable
private fun CurrencyDetails(
    modifier: Modifier = Modifier,
    currency: Currency,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            style = Typography.titleLarge,
            text = currency.name,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            style = Typography.titleMedium,
            text = currency.code,
        )
    }
}

@Composable
private fun RateHistoryList(
    modifier: Modifier = Modifier,
    rateHistory: List<RateHistoryUiState.DailyRateUiState>,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(items = rateHistory, key = { it.date }) {
            RateHistoryItem(
                modifier = Modifier.padding(8.dp),
                dailyRate = it,
            )
        }
    }
}

@Composable
private fun RateHistoryItem(
    modifier: Modifier = Modifier,
    dailyRate: RateHistoryUiState.DailyRateUiState,
) {
    Row(
        modifier = Modifier.fillMaxWidth().then(modifier),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = dailyRate.date,
        )
        Text(
            text = dailyRate.middleValue,
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewRateHistoryScreen() {
    CurrencyRadarTheme {
        RateHistoryScreen(
            uiState = UI_STATE,
        )
    }
}

private val CURRENCY = Currency(
    name = "Euro",
    code = "EUR",
)

private val RATE_HISTORY = listOf(
    RateHistoryUiState.DailyRateUiState(
        date = "12.12.2025",
        middleValue = "4.2271",
    ),
    RateHistoryUiState.DailyRateUiState(
        date = "11.12.2025",
        middleValue = "4.2284",
    ),
    RateHistoryUiState.DailyRateUiState(
        date = "10.12.2025",
        middleValue = "4.2274",
    ),
)

private val UI_STATE = RateHistoryUiState(
    currency = CURRENCY,
    rateHistory = RATE_HISTORY,
)
