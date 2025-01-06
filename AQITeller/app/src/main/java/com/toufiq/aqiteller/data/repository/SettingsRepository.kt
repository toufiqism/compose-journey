import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {
    private object PreferencesKeys {
        val NOTIFICATION_INTERVAL = intPreferencesKey("notification_interval")
        val NOTIFY_ON_HIGH_AQI = booleanPreferencesKey("notify_on_high_aqi")
        val HIGH_AQI_THRESHOLD = intPreferencesKey("high_aqi_threshold")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
    }

    val settings: Flow<Settings> = context.dataStore.data.map { preferences ->
        Settings(
            notificationInterval = preferences[PreferencesKeys.NOTIFICATION_INTERVAL] ?: 30,
            notifyOnHighAqi = preferences[PreferencesKeys.NOTIFY_ON_HIGH_AQI] ?: true,
            highAqiThreshold = preferences[PreferencesKeys.HIGH_AQI_THRESHOLD] ?: 100,
            notificationsEnabled = preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] ?: true
        )
    }

    suspend fun updateNotificationInterval(minutes: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATION_INTERVAL] = minutes
        }
    }

    suspend fun updateNotifyOnHighAqi(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFY_ON_HIGH_AQI] = enabled
        }
    }

    suspend fun updateHighAqiThreshold(threshold: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.HIGH_AQI_THRESHOLD] = threshold
        }
    }

    suspend fun updateNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS_ENABLED] = enabled
        }
    }
} 