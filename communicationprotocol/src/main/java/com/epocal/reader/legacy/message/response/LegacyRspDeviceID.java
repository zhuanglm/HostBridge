package com.epocal.reader.legacy.message.response;

import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.LegacyMsgPayload;
import com.epocal.reader.type.Legacy.LegacyInfo;
import com.epocal.reader.type.MessageDescriptor;

/**
 * Created by rzhuang on July 30 2018.
 */

public class LegacyRspDeviceID extends LegacyMsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Command,
            LegacyMessageType.DeviceIdResponse.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }


    private LegacyInfo mLegacyInfo;

    public LegacyRspDeviceID() {
        mLegacyInfo = new LegacyInfo();
    }

    public LegacyInfo getLegacyInfo() {
        return mLegacyInfo;
    }

    @Override
    public int fillBuffer() {
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer) {
        //setRawBuffer(buffer);
        mLegacyInfo.loadFromBuf(buffer);

        return ParseResult.Success;
    }

    @Override
    public String toString() {
        return mLegacyInfo.toString();
    }
}
