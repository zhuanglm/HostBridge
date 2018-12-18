package com.epocal.reader.nextgen.message.response.test.notification;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.enumtype.DAMStageType;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.TestNotificationResponse;
import com.epocal.reader.type.MessageDescriptor;
import com.epocal.util.UnsignedType;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dning on 7/12/2017.
 */

public class TstNtfRspTimingUpdate extends MsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Test, MessageGroup.Notification,
            TestNotificationResponse.TimingUpdate.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public enum MessageCodeType {
        NotDefined((byte) 0),
        BGEDAMStageDuration((byte) 1);

        public final byte value;

        MessageCodeType(byte value) {
            this.value = Byte.valueOf(value);
        }

        public static MessageCodeType convert(byte value) {
            return MessageCodeType.values()[value];
        }
    }

    public MessageCodeType mResponse;

    public MessageCodeType getResponse() {
        return MessageCodeType.convert(getMessageCode());
    }

    private byte mResponseDetails;

    public byte getResponseDetails() {
        return mResponseDetails;
    }

    public void setResponseDetails(byte mResponseDetails) {
        this.mResponseDetails = mResponseDetails;
    }

    private DAMStageType mDAMStageType;
    private UnsignedType.UInt32 mTimeStamp;
    private float mRemainingTime;

    public DAMStageType getDAMStageType() {
        return mDAMStageType;
    }

    public void setDAMStageType(DAMStageType mDAMStageType) {
        this.mDAMStageType = mDAMStageType;
    }

    public UnsignedType.UInt32 getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(UnsignedType.UInt32 mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }

    public float getRemainingTime() {
        return mRemainingTime;
    }

    public void setRemainingTime(float mRemainingTime) {
        this.mRemainingTime = mRemainingTime;
    }

    public TstNtfRspTimingUpdate() {
        setMessageCode(MessageCodeType.NotDefined.value);
        mTimeStamp = new UnsignedType.UInt32();
    }

    @Override
    public int fillBuffer() {
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer) {
        setRawBuffer(buffer);

        DataStreamReader dsr = new DataStreamReader(getRawBuffer());
        ParseResult parseResult = ParseResult.Success;

        try {
            getDescriptor().readBytes(dsr);
            setMessageCode(dsr.readByte());
            if(getMessageCode() == MessageCodeType.BGEDAMStageDuration.value) {
                mDAMStageType = DAMStageType.convert(dsr.readByte());
                mTimeStamp.setValue(BigEndianBitConverter.toInt32(dsr, 4, 0));
                mRemainingTime = BigEndianBitConverter.toSingle(dsr, 4, 0);
            }
        } catch (IOException e) {
            parseResult = ParseResult.UnknownFailure;
        }
        dsr.dispose();
        return parseResult;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MsgCode: " + MessageCodeType.convert(getMessageCode()).toString());
        sb.append("DAMStageType: " + mDAMStageType.toString());
        sb.append("TimeStamp: " + mTimeStamp.getValue());
        sb.append("RemainingTime: " + mRemainingTime);
        return sb.toString();
    }
}
