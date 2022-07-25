package com.ricardaparicio.cryptodemo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.requestly.android.core.Requestly

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Requestly.Builder(this, "bk9fvxFXM5HNwaMc6gkZ")
            .setNetworkLoggerUIState(true)
            .build()
    }
}