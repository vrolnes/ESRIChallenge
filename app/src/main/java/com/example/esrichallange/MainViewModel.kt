package com.example.esrichallange

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blueetoothlibrary.bluetoothLibrary.BluetoothLibrary
import com.example.blueetoothlibrary.constants.ScanRates
import com.example.blueetoothlibrary.models.Device
import kotlinx.coroutines.launch

class MainViewModel(activity: Context) : ViewModel() {

    val deviceList = mutableStateListOf<Device>()
    private val bluetoothLibrary = BluetoothLibrary(context = activity)

    init {
        viewModelScope.launch {
            bluetoothLibrary.deviceFlow.collect {
                deviceList.add(it)
            }
        }
    }

    fun startScan() {
        bluetoothLibrary.startScan()
    }

    fun stopScan() {
        bluetoothLibrary.stopScan()
        deviceList.clear()
    }

    fun setScanRate(scanRate: ScanRates) {
        bluetoothLibrary.setScanRate(scanRate)
    }

}