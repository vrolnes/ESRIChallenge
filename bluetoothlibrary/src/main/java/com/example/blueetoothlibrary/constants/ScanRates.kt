package com.example.blueetoothlibrary.constants

/**
 * Scan rates
 *
 * @property text Scan rate name
 * @property duration Scan rate duration
 */
enum class ScanRates(val text: String, val duration: Long) {
    /**
     * Low Scan Rate
     *
     */
    LOW_SCAN_RATE("LOW", 10000L),

    /**
     * Medium Scan Rate
     *
     */
    MEDIUM_SCAN_RATE("MEDIUM", 5000L),

    /**
     * High Scan Rate
     *
     */
    HIGH_SCAN_RATE("HIGH", 3000L),
}