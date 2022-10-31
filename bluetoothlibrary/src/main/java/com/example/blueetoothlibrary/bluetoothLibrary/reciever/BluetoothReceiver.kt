package com.example.blueetoothlibrary.bluetoothLibrary.reciever

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.blueetoothlibrary.constants.PermissionConstants
import com.example.blueetoothlibrary.models.Device

/**
 * Bluetooth receiver
 *
 */
abstract class BluetoothReceiver : BroadcastReceiver() {
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
                            PermissionConstants.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        Log.d("BluetoothLibrary", "Connect Permission not granted")
                    } else {
                        device?.name?.let { name ->
                            getFoundDevice(
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
                        getFoundDevice(
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

    /**
     * Get found device
     *
     * @param device
     */
    abstract fun getFoundDevice(device: Device)
}