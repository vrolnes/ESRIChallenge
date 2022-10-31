package com.example.esrichallange

import com.example.blueetoothlibrary.constants.ScanRates
import com.example.blueetoothlibrary.models.Device
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class MainViewModelTest {
    private val FAKE_DEVICE = Device("Test", -1, "1")
    lateinit var mainViewModel : FakeMainViewModel

    @Before
    fun setUp() {
        mainViewModel = FakeMainViewModel()
    }

    @Test
    fun listHasRightDevice() {
        val device = mainViewModel.deviceList.first()
        assertEquals(FAKE_DEVICE, device)
    }

    @Test
    fun listHasNotRightDevice() {
        mainViewModel.deviceList.clear()
        mainViewModel.deviceList.add(Device("test",-2,"2"))
        val device = mainViewModel.deviceList.first()
        assertNotEquals(FAKE_DEVICE, device)
    }

    @Test
    fun buttonTextIsStart() {
        assertEquals("Start", mainViewModel.buttonText.value)
    }

    @Test
    fun buttonTextIsStop() {
        mainViewModel.startOrStopScan()
        assertEquals("Stop",mainViewModel.buttonText.value)
    }

    @Test
    fun scanRateIsMedium() {
        assertEquals(ScanRates.MEDIUM_SCAN_RATE,mainViewModel.fakeScanRate)
    }

    @Test
    fun scanRateIsLow() {
        mainViewModel.setScanRate(ScanRates.LOW_SCAN_RATE)
        assertEquals(ScanRates.LOW_SCAN_RATE,mainViewModel.fakeScanRate)
    }

    @Test
    fun scanRateIsHigh() {
        mainViewModel.setScanRate(ScanRates.HIGH_SCAN_RATE)
        assertEquals(ScanRates.HIGH_SCAN_RATE,mainViewModel.fakeScanRate)
    }
}