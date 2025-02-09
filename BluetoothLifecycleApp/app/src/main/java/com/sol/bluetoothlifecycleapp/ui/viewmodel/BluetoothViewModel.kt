package com.sol.bluetoothlifecycleapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

import com.sol.bluetoothlifecycleapp.data.model.BluetoothDeviceModel
import com.sol.bluetoothlifecycleapp.data.model.BluetoothState
import com.sol.bluetoothlifecycleapp.data.repository.BluetoothRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BluetoothViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BluetoothRepository = BluetoothRepository(application)

    val scannedDevices: StateFlow<List<BluetoothDeviceModel>> = repository.scannedDevices
    val state: StateFlow<BluetoothState> = repository.state

    fun startScan() {
        viewModelScope.launch {
            repository.startScan()
        }
    }

    fun stopScan() {
        repository.stopScan()
    }

    fun connectToDevice(address: String) {
        viewModelScope.launch {
            repository.connectToDevice(address)
        }
    }

    fun disconnect() {
        repository.disconnect()
    }

    override fun onCleared() {
        super.onCleared()
        repository.stopScan()
        repository.disconnect()
        repository.cleanup()
    }
} 