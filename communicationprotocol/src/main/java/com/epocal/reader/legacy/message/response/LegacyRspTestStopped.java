package com.epocal.reader.legacy.message.response;

import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.enumtype.legacy.TestStoppedReasons;
import com.epocal.reader.legacy.LegacyMsgPayload;
import com.epocal.reader.type.MessageDescriptor;

/**
 * Created by rzhuang on Aug 3 2018.
 */

public class LegacyRspTestStopped extends LegacyMsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Command,
            LegacyMessageType.TestStopped.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public LegacyRspTestStopped() {

    }

    public TestStoppedReasons getReason() {
        return mReason;
    }

    private TestStoppedReasons mReason;

    @Override
    public int fillBuffer() {
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer) {

        if (buffer.length != 1) {
            return ParseResult.BufferLengthIncorrect;
        }

        mReason = TestStoppedReasons.convert(buffer[0]);
        return ParseResult.Success;
    }

    @Override
    public String toString() {
        return "TestStopped Reason: " + mReason.toString();
    }
}
