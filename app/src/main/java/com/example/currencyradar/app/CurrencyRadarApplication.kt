package com.example.currencyradar.app

import android.app.Application
import com.example.currencyradar.app.di.CurrencyRadarApp
import org.koin.android.ext.koin.androidContext
import org.koin.plugin.module.dsl.startKoin

class CurrencyRadarApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin<CurrencyRadarApp> {
            androidContext(this@CurrencyRadarApplication)
        }
    }
}
