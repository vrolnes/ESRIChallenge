package com.example.esrichallange.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.blueetoothlibrary.models.Device
import com.example.esrichallange.ui.components.Dropdown

@Composable
fun MainScreen(
    modifier: Modifier,
    startButtonClick: () -> Unit,
    stopButtonClick: () -> Unit,
    scanRateClicked: (String) -> Unit,
    deviceList: MutableList<Device>?
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.Top) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { startButtonClick.invoke() }) {
                Text(text = "Start")
            }
            Button(onClick = { stopButtonClick.invoke() }) {
                Text(text = "Stop")
            }
            Dropdown(items = listOf("LOW", "MEDIUM", "HIGH"), itemClick = scanRateClicked)
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            deviceList?.let {
                items(it) { device ->
                    DeviceCard(device = device)
                }
            }
        }
    }
}

@Composable
fun DeviceCard(device: Device) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        backgroundColor = Color.Gray,
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Device Name: ")
                Text(text = device.deviceName)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Device RSSI: ")
                Text(text = device.rssi.toString())
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Detection TimeStamp: ")
                Text(text = device.timestamp)
            }
        }
    }
}
