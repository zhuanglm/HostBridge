package com.epocal.network.bluetooth.di.module;


import android.content.Context;

import com.epocal.network.bluetooth.BluetoothConnector;
import com.epocal.network.bluetooth.BluetoothController;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 *
 * Created by dning on 5/8/2017.
 */

@Module
public class BluetoothControllerModule {
    private Context mContext;

    public BluetoothControllerModule(Context context) {
        mContext = context;
    }

    @Provides
    @Singleton
    public BluetoothController provideBluetoothController() {
        return new BluetoothController(mContext);
    }

    @Provides
    public BluetoothConnector provideBluetoothConnector() {
        return new BluetoothConnector();
    }
}
