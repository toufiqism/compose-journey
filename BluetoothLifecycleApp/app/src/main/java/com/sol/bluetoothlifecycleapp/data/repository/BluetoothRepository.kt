package com.sol.bluetoothlifecycleapp.data.repository

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.sol.bluetoothlifecycleapp.data.model.BluetoothDeviceModel
import com.sol.bluetoothlifecycleapp.data.model.BluetoothState
import com.sol.bluetoothlifecycleapp.service.BluetoothService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BluetoothRepository(private val context: Context) {
    private val bluetoothManager: BluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

    private val _scannedDevices = MutableStateFlow<List<BluetoothDeviceModel>>(emptyList())
    val scannedDevices: StateFlow<List<BluetoothDeviceModel>> = _scannedDevices.asStateFlow()

    private val _state = MutableStateFlow<BluetoothState>(BluetoothState.Idle)
    val state: StateFlow<BluetoothState> = _state.asStateFlow()

    private var bluetoothService: BluetoothService? = null
    
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            bluetoothService = (service as BluetoothService.BluetoothBinder).getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bluetoothService = null
        }
    }

    init {
        bindBluetoothService()
    }

    private fun bindBluetoothService() {
        Intent(context, BluetoothService::class.java).also { intent ->
            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            context.startService(intent)
        }
    }

    fun startScan() {
        if (bluetoothAdapter == null) {
            _state.value = BluetoothState.Error("Bluetooth not supported")
            return
        }

        if (!bluetoothAdapter.isEnabled) {
            _state.value = BluetoothState.Error("Bluetooth is disabled")
            return
        }

        try {
            _state.value = BluetoothState.Scanning
            val pairedDevices = bluetoothAdapter.bondedDevices
            val devices = pairedDevices.map { device ->
                BluetoothDeviceModel(
                    name = device.name,
                    address = device.address,
                    isConnected = device.bondState == BluetoothDevice.BOND_BONDED
                )
            }
            _scannedDevices.value = devices
            bluetoothAdapter.startDiscovery()
        } catch (e: Exception) {
            _state.value = BluetoothState.Error("Failed to start scan: ${e.message}")
        }
    }

    fun stopScan() {
        try {
            bluetoothAdapter?.cancelDiscovery()
            _state.value = BluetoothState.Idle
        } catch (e: Exception) {
            _state.value = BluetoothState.Error("Failed to stop scan: ${e.message}")
        }
    }

    fun connectToDevice(address: String) {
        bluetoothAdapter?.let { adapter ->
            try {
                _state.value = BluetoothState.Connecting
                val device = adapter.getRemoteDevice(address)
                bluetoothService?.connectToDevice(device)
            } catch (e: Exception) {
                _state.value = BluetoothState.Error("Failed to connect: ${e.message}")
            }
        }
    }

    fun disconnect() {
        try {
            bluetoothService?.disconnect()
            _state.value = BluetoothState.Idle
        } catch (e: Exception) {
            _state.value = BluetoothState.Error("Failed to disconnect: ${e.message}")
        }
    }

    fun cleanup() {
        context.unbindService(serviceConnection)
    }
} 