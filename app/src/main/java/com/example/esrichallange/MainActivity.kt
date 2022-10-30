package com.example.esrichallange

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.example.blueetoothlibrary.constants.ScanRates
import com.example.esrichallange.screens.MainScreen
import com.example.esrichallange.ui.theme.ESRIChallangeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestBluetoothAndLocation()
        setContent {
            ESRIChallangeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen(
                        modifier = Modifier.fillMaxSize(),
                        startButtonClick = { mainViewModel.startScan() },
                        stopButtonClick = { mainViewModel.stopScan() },
                        scanRateClicked = { scanRate ->
                            ScanRates.values().find { it.text == scanRate }
                                ?.let { mainViewModel.setScanRate(it) }
                        },
                        deviceList = mainViewModel.deviceList
                    )
                }
            }
        }
    }

    private fun requestBluetoothAndLocation() {
        val requestBluetooth =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode != RESULT_OK) {
                    Toast.makeText(this, "Bluetooth required!", Toast.LENGTH_LONG).show()
                    this.finishAndRemoveTask()
                }
            }
        val requestMultiplePermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                permissions.entries.forEach {
                    val isGranted = it.value
                    if (!isGranted) {
                        Toast.makeText(this, "Permissions required!", Toast.LENGTH_LONG).show()
                        this.finishAndRemoveTask()
                    }
                }
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            )
        } else {
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestBluetooth.launch(enableBtIntent)
        }
    }
}