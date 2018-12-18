package com.epocal.reader.legacy.message.response;

import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.DAMStages;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.LegacyMsgPayload;
import com.epocal.reader.type.MessageDescriptor;

/**
 * Created by rzhuang on Aug 27 2018.
 */

public class LegacyRspStatusUpdate extends LegacyMsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Command,
            LegacyMessageType.StatusUpdate.value);
    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    static int GapSize = 16;
    private byte[] gap;
    private DAMStages mDamMode;
    private float mRemainingTime;

    public LegacyRspStatusUpdate() {
        gap = new byte[GapSize];
    }

    public float getRemainingTime() {
        return mRemainingTime;
    }

    public DAMStages getDamMode() {
        return mDamMode;
    }

    @Override
    public int fillBuffer() {
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer) {
        int count = buffer.length;
        // only 1 parameter
        if (count != GapSize + 5)
        {
            return ParseResult.BufferLengthIncorrect;
        }

        mDamMode = DAMStages.convert(buffer[0]);
        mRemainingTime = getRevFloat(buffer, 1);

        System.arraycopy(buffer,2,gap,0,GapSize);

        return ParseResult.Success;
    }

    @Override
    public String toString() {
        return null;
    }


}
