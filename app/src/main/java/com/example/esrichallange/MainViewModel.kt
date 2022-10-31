package com.example.esrichallange

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blueetoothlibrary.bluetoothLibrary.BluetoothLibrary
import com.example.blueetoothlibrary.constants.ScanRates
import com.example.blueetoothlibrary.models.Device
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Main view model
 *
 * @property bluetoothLibrary
 * @constructor Create main view model with library flow collecting
 */
@HiltViewModel
class MainViewModel @Inject constructor(private val bluetoothLibrary: BluetoothLibrary) :
    ViewModel() {

    val deviceList = mutableStateListOf<Device>()
    val buttonText = mutableStateOf("Start")
    private var isScanning = false


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

    /**
     * Start or stop scan
     *
     */
    fun startOrStopScan() {
        if (!isScanning) {
            isScanning = true
            buttonText.value = "Stop"
            bluetoothLibrary.startScan()
        } else {
            isScanning = false
            buttonText.value = "Start"
            bluetoothLibrary.stopScan()
            deviceList.clear()
        }
    }

    /**
     * Set scan rate
     *
     * @param scanRate
     */
    fun setScanRate(scanRate: ScanRates) {
        bluetoothLibrary.setScanRate(scanRate)
    }

}