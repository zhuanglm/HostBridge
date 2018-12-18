package com.epocal.reader.legacy.message.response;

import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.LegacyMsgPayload;
import com.epocal.reader.type.MessageDescriptor;

import java.text.NumberFormat;
import java.util.Arrays;

/**
 * Created by rzhuang on Aug 3 2018.
 */

public class LegacyRspAlive extends LegacyMsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Command,
            LegacyMessageType.Alive.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    private byte[] mDebuggingInformation;

    public LegacyRspAlive() {
    }

    @Override
    public int fillBuffer() {
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer) {

        if (buffer.length != 10) {
            return ParseResult.BufferLengthIncorrect;
        }

        mDebuggingInformation = Arrays.copyOf(buffer, buffer.length);

        return ParseResult.Success;
    }

    @Override
    public String toString() {

        StringBuilder tempString = new StringBuilder("AL:");
        NumberFormat numFormat = NumberFormat.getNumberInstance();

        for (byte di : mDebuggingInformation) {
            tempString.append(" ").append(numFormat.format(di));
        }

        return tempString.toString();
    }
}
