package com.example.currencyradar.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.annotation.KoinApplication
import org.koin.ksp.generated.*

@KoinApplication
class CurrencyRadarApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CurrencyRadarApp)
        }
    }
}
