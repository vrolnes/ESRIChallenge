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

class BluetoothLibrary(private val context: Context) {

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
                        }
                        else {
                            val deviceName = device?.name
                            //TODO: Timestamp?
                            val deviceRssi = rssi
                            Log.d("BluetoothLibrary", "$deviceName, $deviceRssi")
                        }
                    }else{
                        val deviceName = device?.name
                        val deviceRssi = rssi
                        Log.d("BluetoothLibrary", "$deviceName, $deviceRssi")
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

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            bluetoothAdapter?.startDiscovery()
        }
        else{
            Log.d("BluetoothLibrary", "Scan Permission not granted")
            return
        }

    }

    fun stopScan() {
        context.unregisterReceiver(receiver)
    }
}