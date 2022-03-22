package com.cosmos.candelabra

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.cosmos.candelabra.data.repository.PreferencesRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltAndroidApp
class CandelabraApplication : Application() {

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    override fun onCreate() {
        super.onCreate()

        runBlocking {
            val nightMode = preferencesRepository.getNightMode().first()
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }
    }
}
