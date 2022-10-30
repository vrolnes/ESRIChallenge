package com.example.esrichallange.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.esrichallange.ui.theme.Purple200


@Composable
fun Dropdown(items: List<String>, itemClick: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(1) }
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .defaultMinSize(minWidth = 80.dp, minHeight = 36.dp)
            .background(
                Purple200, shape = RoundedCornerShape(4.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            items[selectedIndex],
            modifier = Modifier
                .wrapContentSize()
                .padding(8.dp)
                .clickable(onClick = { expanded = true })
                .background(
                    Purple200
                )
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .wrapContentSize()
                .background(Purple200)
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    selectedIndex = index
                    expanded = false
                    itemClick.invoke(s)
                }) {
                    Text(text = s)
                }
            }
        }
    }
}