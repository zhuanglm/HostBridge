package com.epocal.network.bluetooth.di.component;

import android.bluetooth.BluetoothAdapter;

import com.epocal.network.bluetooth.BluetoothBindServiceConnection;
import com.epocal.network.bluetooth.BluetoothBroadcastReceiver;
import com.epocal.network.bluetooth.BluetoothConnection;
import com.epocal.network.bluetooth.BluetoothConnector;
import com.epocal.network.bluetooth.BluetoothController;
import com.epocal.network.bluetooth.BluetoothDeviceFilter;
import com.epocal.network.bluetooth.BluetoothDiscovery;
import com.epocal.network.bluetooth.di.module.BluetoothModule;
import com.epocal.network.bluetooth.di.module.InternalModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by dning on 4/27/2017.
 */

@Singleton
@Component(modules = {BluetoothModule.class, InternalModule.class})
public interface BluetoothComponent {
    void inject(BluetoothController injected);

    void inject(BluetoothDiscovery injected);

    void inject(BluetoothConnector injected);

    BluetoothAdapter provideBluetoothAdapter();

    BluetoothBindServiceConnection provideBluetoothBindServiceConnection();

    BluetoothBroadcastReceiver provideBluetoothBroadcastReceiver();

    BluetoothDeviceFilter provideBluetoothDeviceFilter();

    BluetoothConnection provideBluetoothConnection();
}
