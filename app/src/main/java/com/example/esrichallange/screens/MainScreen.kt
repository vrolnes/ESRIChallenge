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
import com.example.esrichallange.ui.components.DeviceCard
import com.example.esrichallange.ui.components.Dropdown

/**
 * Main screen
 *
 * @param modifier
 * @param startStopButtonClick start stop button clicked
 * @param scanRateClicked scan rate item clicked
 * @param deviceList list of devices
 * @param buttonText start stop button text
 * @receiver
 * @receiver
 */
@Composable
fun MainScreen(
    modifier: Modifier,
    startStopButtonClick: () -> Unit,
    scanRateClicked: (String) -> Unit,
    deviceList: MutableList<Device>?,
    buttonText: String
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.Top) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { startStopButtonClick.invoke() }) {
                Text(text = buttonText)
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
