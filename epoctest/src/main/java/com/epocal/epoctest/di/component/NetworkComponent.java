package com.epocal.epoctest.di.component;

import com.epocal.network.bluetooth.BluetoothConnector;
import com.epocal.network.bluetooth.BluetoothController;
import com.epocal.epoctest.testprocess.TestCommunication;
import com.epocal.epoctest.di.module.NetworkModule;
import com.epocal.reader.parser.MessageParser;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by dning on 5/25/2017.
 */

@Singleton
@Component(modules = {NetworkModule.class})
public interface NetworkComponent
{
    void inject(TestCommunication injected);

    BluetoothController provideBluetoothController();
    BluetoothConnector provideBluetoothConnector();
    MessageParser provideMessageParser();
}
