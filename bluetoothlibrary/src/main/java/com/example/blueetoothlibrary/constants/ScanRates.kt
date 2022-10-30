package com.example.blueetoothlibrary.constants

enum class ScanRates(val text: String, val duration: Long) {
    LOW_SCAN_RATE("LOW", 10000L),
    MEDIUM_SCAN_RATE("MEDIUM", 5000L),
    HIGH_SCAN_RATE("HIGH", 3000L),
}