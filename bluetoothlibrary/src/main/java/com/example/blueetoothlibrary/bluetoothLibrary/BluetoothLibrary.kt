package com.example.blueetoothlibrary.bluetoothLibrary

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.blueetoothlibrary.constants.ScanRates
import com.example.blueetoothlibrary.extensions.launchPeriodicAsync
import com.example.blueetoothlibrary.models.Device
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Bluetooth library for bluetooth operations
 *  Due to Android S changes self check permissions are being made in required points
 * @property context
 */
class BluetoothLibrary(private val context: Context) {

    private var scanRate = ScanRates.MEDIUM_SCAN_RATE.duration
    private var scanning = false
    val deviceFlow = MutableSharedFlow<Device>()
    private var scope = CoroutineScope(Dispatchers.Default)

    private var bluetoothManager: BluetoothManager? = null
    private var bluetoothAdapter: BluetoothAdapter? = null
    private val receiver = object : BroadcastReceiver() {

        /**
         * On bluetooth device receive
         *
         * @param context
         * @param intent
         */
        override fun onReceive(context: Context, intent: Intent) {

            when (intent.action.toString()) {

                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE)

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.BLUETOOTH_CONNECT
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            Log.d("BluetoothLibrary", "Connect Permission not granted")
                        } else {
                            device?.name?.let { name ->
                                scope.launch {
                                    deviceFlow.emit(
                                        Device(
                                            name,
                                            rssi,
                                            System.currentTimeMillis().toString()
                                        )
                                    )
                                }
                            }
                        }
                    } else {
                        device?.name?.let { name ->
                            scope.launch {
                                deviceFlow.emit(
                                    Device(
                                        name,
                                        rssi,
                                        System.currentTimeMillis().toString()
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    init {
        bluetoothManager = context.getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager?.adapter
        if (bluetoothAdapter == null) {
            Log.d("BluetoothLibrary", "Device doesn't support bluetooth")
        }
    }

    /**
     * Start scan
     *
     */
    fun startScan() {
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(receiver, filter)
        if (!scanning) {
            if (!scope.isActive) {
                scope = CoroutineScope(Dispatchers.Default)
            }
            scope.launchPeriodicAsync(scanRate) {
                scanRepeatedly()
            }
        }
    }

    /**
     * Stop scan
     *
     */
    fun stopScan() {
        context.unregisterReceiver(receiver)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("BluetoothLibrary", "Scan Permission not granted")
                return
            } else {
                scanning = false
                bluetoothAdapter?.cancelDiscovery()
                scope.cancel()
            }
        } else {
            scanning = false
            bluetoothAdapter?.cancelDiscovery()
            scope.cancel()
        }
    }

    /**
     * Set scan rate
     *
     * @param scanRate
     */
    fun setScanRate(scanRate: ScanRates) {
        this.scanRate = scanRate.duration
    }

    private fun scanRepeatedly() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_SCAN
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (scanning) {
                    scanning = false
                    bluetoothAdapter?.cancelDiscovery()
                } else {
                    scanning = true
                    bluetoothAdapter?.startDiscovery()
                }
            } else {
                Log.d("BluetoothLibrary", "Scan Permission not granted")
                return
            }
        } else {
            if (scanning) {
                scanning = false
                bluetoothAdapter?.cancelDiscovery()
            } else {
                scanning = true
                bluetoothAdapter?.startDiscovery()
            }
        }
    }
}