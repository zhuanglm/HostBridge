package com.epocal.readersettings.di;

import com.epocal.network.bluetooth.di.module.BluetoothControllerModule;
import com.epocal.readersettings.ReadersListActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * The DI
 * Created by Zeeshan A Zakaria on 7/11/2017.
 */

@Singleton
@Component(modules = {BluetoothControllerModule.class})
public interface BluetoothComponent {
    void inject(ReadersListActivity activity);
}
