package com.epocal.reader.legacy.message.response;

import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.LegacyMsgPayload;
import com.epocal.reader.type.MessageDescriptor;

public class LegacyRspAck extends LegacyMsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Command,
            LegacyMessageType.Ack.value);

    public enum AckType {
        Config1_1((byte) 0),
        Config1_2((byte) 1),
        Config1_3((byte) 2),
        Config1_4((byte) 3),
        Config2((byte) 4),
        Config3((byte) 5),
        ResultsDestroyed((byte) 6),
        ReadyforFirmwareUpgrade((byte) 7),
        FirmwareUpgradeComplete((byte) 8),
        ResumeTransmission((byte) 9),
        SendingOldTest((byte) 10),
        FirmwarePacketReceived((byte) 11),
        EnableTestAck((byte) 12),
        DeviceEnable((byte) 13),
        Reserved((byte) 14);

        public final byte value;

        AckType(byte value) {
            this.value = value;
        }

        public static AckType convert(byte val) {
            return AckType.values()[val];
        }
    }

    public AckType getAckType() {
        return mAckType;
    }

    private AckType mAckType;

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    @Override
    public int fillBuffer() {
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer) {
        if (buffer.length > 1) {
            return ParseResult.BufferLengthIncorrect;
        }

        byte val = (byte) (buffer[0] & 0xff);

        if (val > AckType.Reserved.value) {
            return ParseResult.InvalidParameter;
        }

        mAckType = AckType.convert(val);
        return ParseResult.Success;
    }

    @Override
    public String toString() {
        return "Ack: " + mAckType.toString();
    }
}
