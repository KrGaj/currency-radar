package com.example.currencyradar.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.currencyradar.app.ui.navigation.AppNavHost
import com.example.currencyradar.app.ui.theme.CurrencyRadarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            CurrencyRadarTheme {
                AppNavHost(
                    navController = navController,
                )
            }
        }
    }
}
