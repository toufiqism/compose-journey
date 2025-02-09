package com.sol.bluetoothlifecycleapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sol.bluetoothlifecycleapp.R
import com.sol.bluetoothlifecycleapp.data.model.BluetoothDeviceModel

class BluetoothDeviceAdapter(
    private val onDeviceClick: (BluetoothDeviceModel) -> Unit
) : RecyclerView.Adapter<BluetoothDeviceAdapter.DeviceViewHolder>() {

    private var devices = listOf<BluetoothDeviceModel>()

    fun updateDevices(newDevices: List<BluetoothDeviceModel>) {
        devices = newDevices
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return DeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = devices[position]
        holder.bind(device)
        holder.itemView.setOnClickListener { onDeviceClick(device) }
    }

    override fun getItemCount(): Int = devices.size

    class DeviceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val text1: TextView = view.findViewById(android.R.id.text1)
        private val text2: TextView = view.findViewById(android.R.id.text2)

        fun bind(device: BluetoothDeviceModel) {
            text1.text = device.name ?: "Unknown Device"
            text2.text = device.address
        }
    }
} 