package com.example.esrichallange

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.blueetoothlibrary.constants.ScanRates
import com.example.blueetoothlibrary.models.Device

/**
 * Fake main view model
 *
 */
class FakeMainViewModel : ViewModel() {
    private val FAKE_DEVICE = Device("Test", -1, "1")
    val deviceList = mutableStateListOf(FAKE_DEVICE)
    val buttonText = mutableStateOf("Start")
    var isScanning = false
    var fakeScanRate = ScanRates.MEDIUM_SCAN_RATE

    /**
     * Start or stop scan
     *
     */
    fun startOrStopScan() {
        if (!isScanning) {
            isScanning = true
            buttonText.value = "Stop"

        } else {
            isScanning = false
            buttonText.value = "Start"

            deviceList.clear()
        }
    }

    /**
     * Set scan rate
     *
     * @param scanRate
     */
    fun setScanRate(scanRate: ScanRates) {
        fakeScanRate = scanRate
    }
}