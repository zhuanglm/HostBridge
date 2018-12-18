package com.epocal.reader.legacy.message.response;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.type.MessageDescriptor;

/**
 * Created by rzhuang on Aug 29 2018.
 */

public class LegacyRspConfiguration3 extends MsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Debug,
            LegacyMessageType.ConfigurationResponse3.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    //private byte[] configBuffer;

    public LegacyRspConfiguration3() {
    }

    @Override
    public int fillBuffer() {
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer) {
        //System.arraycopy(buffer, 0, configBuffer, 0, buffer.length);

        return ParseResult.Success;
    }


    @Override
    public String toString() {
        return "AD: ";//+ debugCode;
    }

}