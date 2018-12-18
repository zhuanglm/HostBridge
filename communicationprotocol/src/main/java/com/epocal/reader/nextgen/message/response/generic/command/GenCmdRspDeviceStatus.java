package com.epocal.reader.nextgen.message.response.generic.command;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.enumtype.GenericCommandResponse;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.type.DeviceStatusData;
import com.epocal.reader.type.MessageDescriptor;

import java.io.IOException;

/**
 * Created by dning on 6/19/2017.
 */

public class GenCmdRspDeviceStatus extends MsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Generic, MessageGroup.Command,
            GenericCommandResponse.DeviceStatusResponse.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public enum MessageCodeType {
        NotDefined((byte) 0),
        Data((byte) 1),
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

    private DeviceStatusData mDeviceStatusData;

    public DeviceStatusData getDeviceStatusData() {
        return mDeviceStatusData;
    }

    public void setDeviceStatusData(DeviceStatusData mDeviceStatusData) {
        this.mDeviceStatusData = mDeviceStatusData;
    }

    public GenCmdRspDeviceStatus() {
        mDeviceStatusData = new DeviceStatusData();
    }

    public GenCmdRspDeviceStatus(byte errorCode) {
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
        } catch (IOException e) {
            parseResult = ParseResult.UnknownFailure;
        }

        if (parseResult == ParseResult.Success) {
            switch (MessageCodeType.convert(getMessageCode())) {
                case Data:
                    if (mDeviceStatusData == null) {
                        mDeviceStatusData = new DeviceStatusData();
                    }
                    try {
                        mDeviceStatusData.readBytes(dsr);
                    } catch (IOException e) {
                        parseResult = ParseResult.UnknownFailure;
                    }
                    break;
                case Error:
                    try {
                        mResponseDetails = dsr.readByte();
                    } catch (IOException e) {
                        parseResult = ParseResult.UnknownFailure;
                    }
                    break;
                default:
                    return parseResult = ParseResult.InvalidMessageCode;
            }
        }
        dsr.dispose();
        return parseResult;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MsgCode: " + MessageCodeType.convert(getMessageCode()).toString());

        switch (MessageCodeType.convert(getMessageCode())) {
            case Data:
                sb.append("DeviceStatusData: " + mDeviceStatusData.toString());
                break;
            case Error:
                sb.append("ResponseDetails: " + mResponseDetails);
                break;
        }
        return sb.toString();
    }
}
