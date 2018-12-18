package com.epocal.reader.nextgen.message.response.generic.command;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.enumtype.GenericCommandResponse;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.type.MessageDescriptor;

import java.io.IOException;

/**
 * Created by dning on 6/29/2017.
 */

public class GenCmdRspPerformEQC extends MsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Generic, MessageGroup.Command,
            GenericCommandResponse.PerformEqc.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public enum MessageCodeType {
        NotDefined((byte) 0),
        Ack((byte) 1),
        Error((byte) 2);

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

    private int mTimestamp;
    public int getTimestamp() {
        return mTimestamp;
    }
    public void setTimestamp(int mTimestamp) {
        this.mTimestamp = mTimestamp;
    }

    public GenCmdRspPerformEQC() {
        setMessageCode(MessageCodeType.NotDefined.value);
    }

    public GenCmdRspPerformEQC(byte errorCode) {
        setMessageCode(MessageCodeType.Error.value);
        mResponseDetails = errorCode;
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
            mResponseDetails = dsr.readByte();
            if (MessageCodeType.convert(getMessageCode()) == MessageCodeType.Ack) {
                mTimestamp = BigEndianBitConverter.toInt32(dsr, 4, 0);
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
        sb.append("ResponseDetails: " + mResponseDetails);
        if (MessageCodeType.convert(getMessageCode()) == MessageCodeType.Ack) {
            sb.append("Timestamp: " + mTimestamp);
        }
        return sb.toString();
    }
}
