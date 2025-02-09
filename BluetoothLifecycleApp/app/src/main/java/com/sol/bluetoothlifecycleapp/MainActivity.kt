package com.sol.bluetoothlifecycleapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sol.bluetoothlifecycleapp.data.model.BluetoothState
import com.sol.bluetoothlifecycleapp.databinding.ActivityMainBinding
import com.sol.bluetoothlifecycleapp.ui.adapter.BluetoothDeviceAdapter
import com.sol.bluetoothlifecycleapp.ui.viewmodel.BluetoothViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: BluetoothViewModel by viewModels()
    private lateinit var deviceAdapter: BluetoothDeviceAdapter

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (allGranted) {
            startBluetoothScan()
        } else {
            Toast.makeText(this, "Permissions required to scan for devices", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        deviceAdapter = BluetoothDeviceAdapter { device ->
            viewModel.connectToDevice(device.address)
        }
        binding.devicesList.apply {
            adapter = deviceAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.scannedDevices.collect { devices ->
                deviceAdapter.updateDevices(devices)
            }
        }

        lifecycleScope.launch {
            viewModel.state.collect { state ->
                updateUI(state)
            }
        }
    }

    private fun updateUI(state: BluetoothState) {
        binding.progressBar.visibility = when (state) {
            is BluetoothState.Scanning, is BluetoothState.Connecting -> View.VISIBLE
            else -> View.GONE
        }

        binding.statusText.text = when (state) {
            is BluetoothState.Idle -> "Disconnected"
            is BluetoothState.Scanning -> "Scanning..."
            is BluetoothState.Connecting -> "Connecting..."
            is BluetoothState.Connected -> "Connected to ${state.deviceName}"
            is BluetoothState.Error -> "Error: ${state.message}"
        }

        if (state is BluetoothState.Error) {
            Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun setupClickListeners() {
        binding.scanButton.setOnClickListener {
            checkPermissionsAndStartScan()
        }
    }

    private fun checkPermissionsAndStartScan() {
        val permissions = arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (permissions.all { permission ->
                ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
            }) {
            startBluetoothScan()
        } else {
            permissionLauncher.launch(permissions)
        }
    }

    private fun startBluetoothScan() {
        viewModel.startScan()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopScan()
    }
}