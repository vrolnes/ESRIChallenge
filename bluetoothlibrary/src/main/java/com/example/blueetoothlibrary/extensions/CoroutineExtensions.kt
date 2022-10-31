package com.example.blueetoothlibrary.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

/**
 * Launch periodic async in coroutine scope
 *
 * @param repeatMillis
 * @param action Task to do
 */
fun CoroutineScope.launchPeriodicAsync(repeatMillis: Long, action: () -> Unit) = this.async {
    while (isActive) {
        action()
        delay(repeatMillis)
    }
}