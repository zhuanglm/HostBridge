package com.epocal.reader.nextgen.message.response.test.action;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.TestActionResponse;
import com.epocal.reader.type.DPMActionData;
import com.epocal.reader.type.MessageDescriptor;

import java.io.IOException;

/**
 * Created by dning on 7/18/2017.
 */

public class TstActRspDevicePeripheralMonitor extends MsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Test, MessageGroup.Action,
            TestActionResponse.DevicePeripheralMonitor.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public enum MessageCodeType {
        NotDefined((byte) 0),
        Ack((byte) 1),
        Error((byte) 2),
        ActionStarted((byte) 3),
        ActionCompleted((byte) 4),
        Data((byte) 5);

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

    private int mPeripheralMonitorId;
    private DPMActionData mData;

    public int getPeripheralMonitorId() {
        return mPeripheralMonitorId;
    }

    public void setPeripheralMonitorId(int mPeripheralMonitorId) {
        this.mPeripheralMonitorId = mPeripheralMonitorId;
    }

    public DPMActionData getData() {
        return mData;
    }

    public void setData(DPMActionData mData) {
        this.mData = mData;
    }

    public TstActRspDevicePeripheralMonitor() {
        setMessageCode(MessageCodeType.NotDefined.value);
        mData = new DPMActionData();
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
            if(getMessageCode() == MessageCodeType.ActionStarted.value || getMessageCode() == MessageCodeType.ActionCompleted.value) {
                mPeripheralMonitorId = BigEndianBitConverter.toInt32(dsr, 4, 0);
            }
            else if(getMessageCode() == MessageCodeType.Data.value) {
                mData.readBytes(dsr);
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
        if(getMessageCode() == MessageCodeType.ActionStarted.value || getMessageCode() == MessageCodeType.ActionCompleted.value) {
            sb.append("PeripheralMonitorId: " + mPeripheralMonitorId);
        }
        else if(getMessageCode() == MessageCodeType.Data.value) {
            sb.append("Data: " + mData.toString());
        }
        return sb.toString();
    }
}
