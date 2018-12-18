/*
 * Copyright (c) 2018. Siemens Healthineers. All rights reserved.
 */

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

public class LegacyReqDestroyTest extends LegacyMsgPayload {

    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Command,
            LegacyMessageType.DestroyTestResults.value);

    public LegacyReqDestroyTest(short testId, short returnCode, short returnCodeCategory) {
        mTestId = testId;
        mReturnCode = returnCode;
        mReturnCodeCategory = returnCodeCategory;
    }

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    private short mTestId;
    private short mReturnCode;
    private short mReturnCodeCategory;

    @Override
    public int fillBuffer() {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();


            output.write(toByteArray(mTestId));
            output.write(toByteArray(mReturnCode));
            output.write(toByteArray(mReturnCodeCategory));
            output.flush();
            setRawBuffer(output.toByteArray());
            output.close();
            return getRawBuffer().length;
        }
        catch (IOException e) {
            Log.e(LegacyMessageType.convert(getDescriptor().getType()).name(),e.getLocalizedMessage());
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
