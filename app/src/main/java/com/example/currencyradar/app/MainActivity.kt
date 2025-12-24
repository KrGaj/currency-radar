package com.example.currencyradar.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.currencyradar.app.ui.navigation.AppNavigation
import com.example.currencyradar.app.ui.theme.CurrencyRadarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurrencyRadarTheme {
                AppNavigation()
            }
        }
    }
}
