package com.epocal.epoctest.di.module;

import android.content.Context;

import com.epocal.common.globaldi.GlobalAppModule;
import com.epocal.network.bluetooth.BluetoothConnector;
import com.epocal.network.bluetooth.BluetoothController;
import com.epocal.reader.di.module.MessageParserModule;
import com.epocal.reader.enumtype.ParsingMode;
import com.epocal.reader.parser.MessageParser;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dning on 5/25/2017.
 */

@Module(includes = {GlobalAppModule.class} )
public class NetworkModule
{
    private ParsingMode mParsingMode;
    public NetworkModule(ParsingMode parsingMode)
    {
        mParsingMode = parsingMode;
    }
    @Provides
    @Singleton
    public BluetoothController provideBluetoothController(Context context) {return new BluetoothController(context);}

    @Provides
    public BluetoothConnector provideBluetoothConnector()
    {
        return new BluetoothConnector();
    }

    @Provides
    public MessageParser provideMessageParser()
    {
        return new MessageParser(mParsingMode);
    }
}
