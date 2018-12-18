package com.epocal.reader.di.module;


import com.epocal.reader.enumtype.ParsingMode;
import com.epocal.reader.nextgen.parser.NextgenMessageParser;
import com.epocal.reader.parser.MessageParser;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dning on 6/12/2017.
 */
@Module
public class MessageParserModule {

    private ParsingMode mParsingMode;

    public MessageParserModule(ParsingMode mode)
    {
        mParsingMode = mode;
    }
    @Provides
    MessageParser provideMessageParser(){ return new MessageParser(mParsingMode);}
}
