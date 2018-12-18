package com.epocal.reader.legacy.message.response;

import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.LegacyMsgPayload;
import com.epocal.reader.type.MessageDescriptor;

public class LegacyRspCardRemoved extends LegacyMsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Command,
            LegacyMessageType.CardRemoved.value);


    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }


    public LegacyRspCardRemoved() {

    }

    @Override
    public int fillBuffer() {
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer) {
        return ParseResult.Success;
    }

    @Override
    public String toString() {
        return null;
    }
}
