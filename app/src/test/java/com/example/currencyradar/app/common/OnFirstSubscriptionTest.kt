package com.example.currencyradar.app.common

import app.cash.turbine.test
import com.example.currencyradar.app.ui.current_rates.CurrentRatesUiState
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class OnFirstSubscriptionTest {
    lateinit var state: MutableStateFlow<CurrentRatesUiState>
    lateinit var action: () -> Unit

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        state = MutableStateFlow(CurrentRatesUiState())
        action = spyk()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Action is performed on MutableStateFlow's first subscription`() = runTest {
        every { action() } answers {
            state.update { it.copy(selectedTabIndex = it.selectedTabIndex + 1) }
        }

        state.onFirstSubscription(
            scope = backgroundScope,
            action = action,
        )
        testScheduler.runCurrent()

        verify(exactly = 0) { action() }

        state.test { awaitItem().selectedTabIndex shouldBe 0 }
        testScheduler.runCurrent()

        verify(exactly = 1) { action() }
        state.test { awaitItem().selectedTabIndex shouldBe 1 }
    }

    @Test
    fun `Action is performed only on MutableStateFlow's first subscription`() = runTest {
        every { action() } answers {
            state.update { it.copy(selectedTabIndex = it.selectedTabIndex + 1) }
        }

        state.onFirstSubscription(
            scope = backgroundScope,
            action = action,
        )
        testScheduler.runCurrent()

        state.test { awaitItem().selectedTabIndex shouldBe 0 }
        testScheduler.runCurrent()

        val job1 = launch {
            state.collect {  }
        }
        testScheduler.runCurrent()

        val job2 = launch {
            state.collect {  }
        }
        testScheduler.runCurrent()

        job1.cancel()
        job2.cancel()

        verify(exactly = 1) { action() }
        state.test { awaitItem().selectedTabIndex shouldBe 1 }
    }
}
