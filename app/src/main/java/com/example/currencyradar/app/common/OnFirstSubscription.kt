package com.example.currencyradar.app.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

fun <T> MutableStateFlow<T>.onFirstSubscription(
    scope: CoroutineScope,
    action: () -> Unit,
) {
    scope.launch {
        subscriptionCount.filter { it == 1 }
            .take(1)
            .collect {
                action()
            }
    }
}
