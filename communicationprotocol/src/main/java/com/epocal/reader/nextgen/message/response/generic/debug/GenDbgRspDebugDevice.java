package com.epocal.reader.nextgen.message.response.generic.debug;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.enumtype.GenericDebugResponse;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.type.CalDeliverySlowDownCounts;
import com.epocal.reader.type.DebugMotorEvents;
import com.epocal.reader.type.MessageDescriptor;

import java.io.IOException;

/**
 * Created by dning on 8/15/2017.
 */

public class GenDbgRspDebugDevice extends MsgPayload
{
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Generic, MessageGroup.Debug,
            GenericDebugResponse.DebugDevice.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public enum MessageCodeType {
        NotDefined((byte) 0),
        Motor((byte) 1);

        public final byte value;

        MessageCodeType(byte value) {
            this.value = Byte.valueOf(value);
        }

        public static MessageCodeType convert(byte value) {
            return MessageCodeType.values()[value];
        }
    }

    public enum MessageDetail {
        NotDefined((byte) 0),
        MotorEvents((byte) 1),
        SlowDownCounts((byte) 2);

        public final byte value;

        MessageDetail(byte value) {
            this.value = Byte.valueOf(value);
        }

        public static MessageDetail convert(byte value) {
            return MessageDetail.values()[value];
        }
    }

    public MessageCodeType mResponse;

    public MessageCodeType getResponse() {
        return MessageCodeType.convert(getMessageCode());
    }


    private MessageDetail mResponseDetails;

    public MessageDetail getResponseDetails() {
        return mResponseDetails;
    }

    public void setResponseDetails(MessageDetail mResponseDetails) {
        this.mResponseDetails = mResponseDetails;
    }

    private DebugMotorEvents mDebugMotorEvents;
    private CalDeliverySlowDownCounts mCalDeliverySlowDownCounts;

    public GenDbgRspDebugDevice() {
        mDebugMotorEvents = new DebugMotorEvents();
        mCalDeliverySlowDownCounts = new CalDeliverySlowDownCounts();
    }

    public GenDbgRspDebugDevice(MessageCodeType messageCode) {
        setMessageCode(messageCode.value);
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
            mResponseDetails = MessageDetail.convert(dsr.readByte());
        } catch (IOException e) {
            parseResult = ParseResult.UnknownFailure;
        }

        if (parseResult == ParseResult.Success) {

            switch (mResponseDetails) {
                case MotorEvents:
                    try {
                        mDebugMotorEvents = new DebugMotorEvents();
                        mDebugMotorEvents.readBytes(dsr);
                    } catch (IOException e) {
                        parseResult = ParseResult.UnknownFailure;
                    }
                    break;
                case SlowDownCounts:
                    try {
                        mCalDeliverySlowDownCounts = new CalDeliverySlowDownCounts();
                        mCalDeliverySlowDownCounts.readBytes(dsr);
                    } catch (IOException e) {
                        parseResult = ParseResult.UnknownFailure;
                    }
                    break;
                default:
                    parseResult = ParseResult.InvalidMessageCode;
            }
        }
        dsr.dispose();
        return parseResult;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MsgCode: " + MessageCodeType.convert(getMessageCode()).toString());
        sb.append("ResponseDetails: " + mResponseDetails.toString());
        switch (mResponseDetails) {
            case MotorEvents:
                sb.append("DebugMotorEvents: " + mDebugMotorEvents.toString());
                break;
            case SlowDownCounts:
                sb.append("CalDeliverySlowDownCounts: " + mCalDeliverySlowDownCounts.toString());
                break;
        }
        return sb.toString();
    }
}

