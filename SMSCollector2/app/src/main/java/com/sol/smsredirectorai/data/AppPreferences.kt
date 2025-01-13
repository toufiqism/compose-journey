package com.sol.smsredirectorai.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Define the DataStore at the top level
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AppPreferences(private val context: Context) {
    companion object {
        private val SIM1_NUMBER = stringPreferencesKey("sim1_number")
        private val SIM1_NAME = stringPreferencesKey("sim1_name")
        private val SIM2_NUMBER = stringPreferencesKey("sim2_number")
        private val SIM2_NAME = stringPreferencesKey("sim2_name")
        private val API_URL = stringPreferencesKey("api_url")
    }

    val sim1Info: Flow<SimInfo> = context.dataStore.data.map { preferences ->
        SimInfo(
            number = preferences[SIM1_NUMBER] ?: "",
            name = preferences[SIM1_NAME] ?: ""
        )
    }

    val sim2Info: Flow<SimInfo> = context.dataStore.data.map { preferences ->
        SimInfo(
            number = preferences[SIM2_NUMBER] ?: "",
            name = preferences[SIM2_NAME] ?: ""
        )
    }

    val apiUrl: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[API_URL] ?: ""
    }

    suspend fun saveSim1Info(info: SimInfo) {
        context.dataStore.edit { preferences ->
            preferences[SIM1_NUMBER] = info.number
            preferences[SIM1_NAME] = info.name
        }
    }

    suspend fun saveSim2Info(info: SimInfo) {
        context.dataStore.edit { preferences ->
            preferences[SIM2_NUMBER] = info.number
            preferences[SIM2_NAME] = info.name
        }
    }

    suspend fun saveApiUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[API_URL] = url
        }
    }
}

data class SimInfo(
    val number: String,
    val name: String
) 