package com.epocal.reader.legacy.message.request.command;

import android.util.Log;

import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.LegacyMsgPayload;
import com.epocal.reader.type.MessageDescriptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by rzhuang on July 30 2018.
 */

public class LegacyReqReaderStatus extends LegacyMsgPayload {

    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Command,
            LegacyMessageType.GetReaderStatus.value);

    public LegacyReqReaderStatus() {
        mGetTemperatures = false;
        mPerformSelfCheck = false;
    }

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    private boolean mGetTemperatures;
    private boolean mPerformSelfCheck;

    public void setPerformSelfCheck(boolean val) {
        mPerformSelfCheck = val;
    }

    public void setTemperatures(boolean val) {
        mGetTemperatures = val;
    }

    @Override
    public int fillBuffer() {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte val = mGetTemperatures ? (byte) 0x01 : (byte) 0x00;
            output.write(val);
            val = mPerformSelfCheck ? (byte) 0x01 : (byte) 0x00;
            output.write(val);

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
