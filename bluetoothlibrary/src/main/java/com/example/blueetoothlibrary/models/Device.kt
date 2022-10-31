package com.example.blueetoothlibrary.models

/**
 * Device model
 *
 * @property deviceName
 * @property rssi
 * @property timestamp
 */
data class Device(
    var deviceName: String,
    var rssi: Short,
    var timestamp: String
)
