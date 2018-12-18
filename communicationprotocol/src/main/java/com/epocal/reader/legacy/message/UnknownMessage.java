package com.epocal.reader.legacy.message;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.type.MessageDescriptor;

public class UnknownMessage extends MsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Debug,
            LegacyMessageType.None.value);

    public byte supposedMessageType;

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public UnknownMessage() {}

    @Override
    public int fillBuffer()
    {
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer) {

        return ParseResult.UnknownInnerCode;
    }


    @Override
    public String toString()
    {
        String tempString = "Unknown type: "+supposedMessageType;

        return tempString;
    }
}
