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
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.blueetoothlibrary.constants.ScanRates
import com.example.blueetoothlibrary.models.Device

class BluetoothLibrary(private val context: Context, private val deviceList: ArrayList<Device>) {

    private var scanRate = ScanRates.MEDIUM_SCAN_RATE.duration
    private var scanning = false
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = { scanRepeatedly() }

    private var bluetoothManager: BluetoothManager? = null
    private var bluetoothAdapter: BluetoothAdapter? = null
    private val receiver = object : BroadcastReceiver() {

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
                                deviceList.add(
                                    Device(
                                        name,
                                        rssi,
                                        System.currentTimeMillis().toString()
                                    )
                                )
                            }
                        }
                    } else {
                        device?.name?.let { name ->
                            deviceList.add(
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

    init {
        bluetoothManager = context.getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager?.adapter
        if (bluetoothAdapter == null) {
            Log.d("BlueToothLibrary", "Device doesn't support bluetooth")
        }
    }

    fun startScan() {
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(receiver, filter)
        if (!scanning) {
            Handler(Looper.getMainLooper()).post(runnable)
        }
    }

    fun stopScan() {
        context.unregisterReceiver(receiver)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            scanning = false
            bluetoothAdapter?.cancelDiscovery()
            handler.removeCallbacks(runnable)
        } else {
            Log.d("BluetoothLibrary", "Scan Permission not granted")
            return
        }
    }

    fun setScanRate(scanRate: ScanRates) {
        this.scanRate = scanRate.duration
    }

    private fun scanRepeatedly() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (scanning) {
                scanning = false
                bluetoothAdapter?.cancelDiscovery()
            } else {
                scanning = true
                bluetoothAdapter?.startDiscovery()
            }
            Handler(Looper.getMainLooper()).postDelayed(runnable, scanRate)
        } else {
            Log.d("BluetoothLibrary", "Scan Permission not granted")
            return
        }
    }
}