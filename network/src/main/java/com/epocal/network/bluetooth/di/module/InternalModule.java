package com.epocal.network.bluetooth.di.module;

import com.epocal.network.bluetooth.BluetoothBindServiceConnection;
import com.epocal.network.bluetooth.BluetoothBroadcastReceiver;
import com.epocal.network.bluetooth.BluetoothConnection;
import com.epocal.network.bluetooth.BluetoothDeviceFilter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dning on 4/27/2017.
 */

@Module
public class InternalModule {
    public InternalModule() {
    }

    @Provides
    public BluetoothBindServiceConnection provideBluetoothBindServiceConnection() {
        return new BluetoothBindServiceConnection();
    }

    @Provides
    public BluetoothBroadcastReceiver provideBluetoothBroadcastReceiver() {
        return new BluetoothBroadcastReceiver();
    }

    @Provides
    public BluetoothDeviceFilter provideBluetoothDeviceFilter() {
        return new BluetoothDeviceFilter();
    }

    @Provides
    public BluetoothConnection provideBluetoothConnection() {
        return new BluetoothConnection();
    }
}
