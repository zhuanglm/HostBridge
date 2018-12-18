package com.epocal.reader.legacy.message.response;

import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.CustomConfigSubcode;
import com.epocal.reader.enumtype.legacy.CustomConfigTask;
import com.epocal.reader.enumtype.legacy.CustomConfigType;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.LegacyMsgPayload;
import com.epocal.reader.type.MessageDescriptor;

/**
 * Created by rzhuang on Aug 3 2018.
 */

public class LegacyRspCustomConfig extends LegacyMsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Configuration,
            LegacyMessageType.CustomConfigurationResponse.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public LegacyRspCustomConfig() {

    }

    private com.epocal.reader.enumtype.legacy.CustomConfigSubcode mCustomConfigSubcode;
    private com.epocal.reader.enumtype.legacy.CustomConfigTask mCustomConfigTask;
    private com.epocal.reader.enumtype.legacy.CustomConfigType mCustomConfigType;
    private byte mMessageVersion = 0;

    private int mPosition = 0;
    private short mBlockLength = 0;

    private byte[] mDataBlock;

    @Override
    public int fillBuffer() {
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer) {

        mCustomConfigSubcode = CustomConfigSubcode.convert(buffer[0]);
        mCustomConfigTask = CustomConfigTask.convert(buffer[1]);
        mCustomConfigType = CustomConfigType.convert(buffer[2]);
        mMessageVersion = buffer[3];

        mPosition = getInt(buffer, 4);
        mBlockLength = (short) (buffer[9] & 0x0FF);

        if (mCustomConfigSubcode == CustomConfigSubcode.eCustomConfigGet) {
            System.arraycopy(buffer, 0, mDataBlock, 0, mBlockLength);
        }

        return ParseResult.Success;
    }

    @Override
    public String toString() {
        return null;
    }
}
