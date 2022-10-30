package com.example.esrichallange.modules

import android.content.Context
import com.example.blueetoothlibrary.bluetoothLibrary.BluetoothLibrary
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BluetoothModule {

    @Provides
    @Singleton
    fun providesBluetoothLibrary(@ApplicationContext context: Context): BluetoothLibrary {
        return BluetoothLibrary(context)
    }
}