package com.epocal.reader.legacy.message.request.command;

import android.util.Log;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.type.MessageDescriptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by rzhuang on Aug 10 2018.
 */

public class LegacyReqInvalidCard extends MsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Debug,
            LegacyMessageType.InvalidCard.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public LegacyReqInvalidCard() {
    }

    @Override
    public int fillBuffer() {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            output.write(0);
            output.flush();
            setRawBuffer(output.toByteArray());
            output.close();
            return getRawBuffer().length;
        } catch (IOException e) {
            Log.e(LegacyMessageType.convert(getDescriptor().getType()).name(), e.getLocalizedMessage());
        }
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer) {

        return ParseResult.InvalidCall;
    }


    @Override
    public String toString() {
        return null;
    }

}