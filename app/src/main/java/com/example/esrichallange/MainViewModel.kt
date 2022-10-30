package com.example.esrichallange

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blueetoothlibrary.bluetoothLibrary.BluetoothLibrary
import com.example.blueetoothlibrary.constants.ScanRates
import com.example.blueetoothlibrary.models.Device
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val bluetoothLibrary: BluetoothLibrary) : ViewModel() {

    val deviceList = mutableStateListOf<Device>()

    init {
        viewModelScope.launch {
            bluetoothLibrary.deviceFlow.collect { newDevice ->
                if (deviceList.firstOrNull {
                        it.deviceName == newDevice.deviceName
                    } == null) {
                    deviceList.add(newDevice)
                }
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