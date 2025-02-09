package com.sol.bluetoothlifecycleapp.data.model

data class BluetoothDeviceModel(
    val name: String?,
    val address: String,
    val isConnected: Boolean = false
) 