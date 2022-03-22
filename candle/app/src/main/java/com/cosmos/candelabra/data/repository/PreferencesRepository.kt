package com.cosmos.candelabra.data.repository

import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.cosmos.candelabra.data.model.preferences.UiPreferences
import com.cosmos.candelabra.util.extension.getValue
import com.cosmos.candelabra.util.extension.setValue
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepository @Inject constructor(
    private val preferencesDatastore: DataStore<Preferences>
) {

    //region Ui

    suspend fun setNightMode(nightMode: Int) {
        preferencesDatastore.setValue(UiPreferences.PreferencesKeys.NIGHT_MODE, nightMode)
    }

    fun getNightMode(defaultValue: Int = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM): Flow<Int> {
        return preferencesDatastore.getValue(UiPreferences.PreferencesKeys.NIGHT_MODE, defaultValue)
    }

    //endregion
}
