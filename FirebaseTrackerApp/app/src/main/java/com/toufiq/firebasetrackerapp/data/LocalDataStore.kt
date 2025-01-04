package com.toufiq.firebasetrackerapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import java.util.UUID

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "pending_data")

class LocalDataStore(private val context: Context) {
    private val gson = Gson()

    suspend fun saveData(deviceData: DeviceData) {
        val key = stringPreferencesKey(UUID.randomUUID().toString())
        context.dataStore.edit { preferences ->
            preferences[key] = gson.toJson(deviceData)
        }
    }

    suspend fun getAllPendingData(): List<Pair<String, DeviceData>> {
        val preferences = context.dataStore.data.first()
        return preferences.asMap().map { (key, value) ->
            key.name to gson.fromJson(value as String, DeviceData::class.java)
        }
    }

    suspend fun removeData(key: String) {
        context.dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(key))
        }
    }
} 