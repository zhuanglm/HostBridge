package com.epocal.reader.legacy.message.response;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.type.MessageDescriptor;

/**
 * Created by rzhuang on Aug 17 2018.
 */

public class LegacyRspHCMCfg extends MsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Debug,
            LegacyMessageType.HCMConfiguration.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public LegacyRspHCMCfg() {
    }

    private byte[] mConfigBuffer;

    @Override
    public int fillBuffer() {
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer) {

        System.arraycopy(buffer, 0, mConfigBuffer, 0, buffer.length);

        return ParseResult.Success;
    }


    @Override
    public String toString() {
        return "AD: ";

//                for (int i = 0; i < debuggingInfo.length; i++)
//                {
//                    if (debuggingInfo[i] < 16)
//                        tempString += " 0" + debuggingInfo[i];
//                    else
//                        tempString += " " + debuggingInfo[i];
//                }

    }

}