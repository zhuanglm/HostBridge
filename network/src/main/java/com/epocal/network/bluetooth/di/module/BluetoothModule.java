package com.epocal.network.bluetooth.di.module;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import com.epocal.common.globaldi.GlobalAppModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dning on 4/27/2017.
 */

@Module(includes = GlobalAppModule.class)
public class BluetoothModule {
    @Provides
    @Singleton
    public BluetoothAdapter provideBluetoothAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }
}