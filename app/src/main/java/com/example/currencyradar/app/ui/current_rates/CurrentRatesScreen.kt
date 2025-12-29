package com.example.currencyradar.app.ui.current_rates

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.currencyradar.R
import com.example.currencyradar.app.ui.common.toCapitalized
import com.example.currencyradar.app.ui.theme.Typography
import com.example.currencyradar.domain.models.Currency
import com.example.currencyradar.domain.models.CurrentRate
import com.example.currencyradar.domain.models.TableType
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CurrentRatesScreen(
    currentRatesViewModel: CurrentRatesViewModel = koinViewModel(),
    onCurrencyListItemClick: (String, TableType) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by currentRatesViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        uiState.error?.let {
            val errorMsg = stringResource(R.string.fetch_error_msg)
            coroutineScope.launch {
                snackbarHostState.showSnackbar(errorMsg)
                currentRatesViewModel.onErrorMessageShown()
            }
        }

        CurrentRatesScreen(
            modifier = Modifier.padding(innerPadding),
            uiState = uiState,
            onTabClick = { currentRatesViewModel.getCurrentRates(it) },
            onCurrencyListItemClick = onCurrencyListItemClick,
        )
    }
}

@Composable
private fun CurrentRatesScreen(
    modifier: Modifier = Modifier,
    uiState: CurrentRatesUiState,
    onTabClick: (TableType) -> Unit,
    onCurrencyListItemClick: (String, TableType) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().then(modifier),
    ) {
        CurrentRatesTabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = uiState.tableType.ordinal,
            onTabClick = onTabClick,
        )

        if (uiState.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
            )
        }

        CurrentRatesList(
            currentRates = uiState.currentRates,
            onItemClick = {
                onCurrencyListItemClick(
                    it.code,
                    uiState.tableType,
                )
            },
        )
    }
}

@Composable
private fun CurrentRatesTabRow(
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    onTabClick: (TableType) -> Unit,
) {
    PrimaryTabRow(
        modifier = modifier,
        selectedTabIndex = selectedTabIndex,
    ) {
        TableType.entries.forEachIndexed { index, tableType ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabClick(tableType) },
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = stringResource(R.string.table_tab_label, tableType.name),
                )
            }
        }
    }
}

@Composable
private fun CurrentRatesList(
    modifier: Modifier = Modifier,
    currentRates: List<CurrentRate>,
    onItemClick: (Currency) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(
            items = currentRates,
            key = { it.currency.code },
        ) {
            CurrentRateItem(
                currentRate = it,
                onClick = onItemClick,
            )
        }
    }
}

@Composable
private fun CurrentRateItem(
    modifier: Modifier = Modifier,
    currentRate: CurrentRate,
    onClick: (Currency) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = { onClick(currentRate.currency) },
            )
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
        ) {
            Text(
                style = Typography.bodyLarge,
                text = currentRate.currency.name
                    .toCapitalized(),
            )

            Spacer(
                modifier = Modifier.size(4.dp),
            )

            Text(
                style = Typography.bodyMedium,
                text = currentRate.currency.code,
            )
        }

        Text(
            modifier = Modifier
                .wrapContentSize()
                .padding(8.dp),
            text = currentRate.middleValue.stripTrailingZeros().toPlainString(),
        )
    }
}


@Preview(locale = "pl", showBackground = true)
@Composable
private fun PreviewCurrentRatesScreen() {
    MaterialTheme {
        CurrentRatesScreen(
            uiState = currentRatesUiState,
            onTabClick = {},
            onCurrencyListItemClick = { _, _ -> },
        )
    }
}

@Preview(locale = "pl", showBackground = true)
@Composable
private fun PreviewCurrentRatesScreenLoading() {
    MaterialTheme {
        CurrentRatesScreen(
            uiState = currentRatesUiState.copy(isLoading = true),
            onTabClick = {},
            onCurrencyListItemClick = { _, _ -> },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCurrentRatesTabRow() {
    MaterialTheme {
        CurrentRatesTabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = 0,
        ) { }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCurrentRateItem() {
    MaterialTheme {
        CurrentRateItem(
            currentRate = currentRates.first(),
            onClick = {},
        )
    }
}


private val currentRates = listOf(
    CurrentRate(
        currency = Currency(
            name = "dolar kanadyjski",
            code = "CAD",
        ),
        middleValue = 2.6181.toBigDecimal(),
    ),
    CurrentRate(
        currency = Currency(
            name = "euro",
            code = "EUR",
        ),
        middleValue = 4.2271.toBigDecimal(),
    ),
    CurrentRate(
        currency = Currency(
            name = "korona norweska",
            code = "NOK",
        ),
        middleValue = 0.3569.toBigDecimal(),
    ),
)

private val currentRatesUiState = CurrentRatesUiState(
    currentRates = currentRates,
)
