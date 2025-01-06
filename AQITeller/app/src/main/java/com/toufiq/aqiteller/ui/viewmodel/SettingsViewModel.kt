import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.toufiq.aqiteller.util.WorkManagerUtil
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    application: Application,
    private val settingsRepository: SettingsRepository
) : AndroidViewModel(application) {

    private val workManagerUtil = WorkManagerUtil(application)

    val settings: StateFlow<Settings> = settingsRepository.settings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Settings()
        )

    fun updateNotificationInterval(minutes: Int) {
        viewModelScope.launch {
            settingsRepository.updateNotificationInterval(minutes)
            workManagerUtil.updateInterval(minutes)
        }
    }

    fun updateNotifyOnHighAqi(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateNotifyOnHighAqi(enabled)
        }
    }

    fun updateHighAqiThreshold(threshold: Int) {
        viewModelScope.launch {
            settingsRepository.updateHighAqiThreshold(threshold)
        }
    }

    fun updateNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateNotificationsEnabled(enabled)
            workManagerUtil.setupNotifications(enabled)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as Application
                SettingsViewModel(
                    application = application,
                    settingsRepository = SettingsRepository(application)
                )
            }
        }
    }
} 