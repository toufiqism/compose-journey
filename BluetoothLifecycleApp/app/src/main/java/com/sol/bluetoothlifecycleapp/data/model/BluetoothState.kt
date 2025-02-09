package com.sol.bluetoothlifecycleapp.data.model

sealed class BluetoothState {
    object Idle : BluetoothState()
    object Scanning : BluetoothState()
    object Connecting : BluetoothState()
    data class Connected(val deviceName: String) : BluetoothState()
    data class Error(val message: String) : BluetoothState()
} 