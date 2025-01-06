import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toufiq.aqiteller.data.repository.SettingsRepository
import com.toufiq.aqiteller.util.LocationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AqiViewModel(
    private val repository: AqiRepository,
    private val locationHelper: LocationHelper,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _aqiState = MutableStateFlow<AqiUiState>(AqiUiState.Initial)
    val aqiState: StateFlow<AqiUiState> = _aqiState

    private var updateJob: Job? = null
    private var currentLocation: Location? = null

    init {
        startPeriodicUpdates()
    }

    private fun startPeriodicUpdates() {
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                while (true) {
                    fetchCurrentLocationAndAqi()
                    delay(settings.notificationInterval * 60 * 1000L)
                }
            }
        }
    }

    private suspend fun fetchCurrentLocationAndAqi() {
        _aqiState.value = AqiUiState.Loading
        try {
            currentLocation = locationHelper.getCurrentLocation()
            if (currentLocation != null) {
                repository.getAirQuality(
                    currentLocation!!.latitude,
                    currentLocation!!.longitude
                ).onSuccess { response ->
                    _aqiState.value = AqiUiState.Success(response)
                }.onFailure { error ->
                    _aqiState.value = AqiUiState.Error(error.message ?: "Unknown error occurred")
                }
            } else {
                _aqiState.value = AqiUiState.Error("Could not get location")
            }
        } catch (e: Exception) {
            _aqiState.value = AqiUiState.Error(e.message ?: "Unknown error occurred")
        }
    }

    override fun onCleared() {
        super.onCleared()
        updateJob?.cancel()
    }
}

sealed class AqiUiState {
    object Initial : AqiUiState()
    object Loading : AqiUiState()
    data class Success(val data: AqiResponse) : AqiUiState()
    data class Error(val message: String) : AqiUiState()
} 