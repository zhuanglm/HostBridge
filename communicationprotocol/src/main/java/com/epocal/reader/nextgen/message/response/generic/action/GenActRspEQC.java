package com.epocal.reader.nextgen.message.response.generic.action;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.enumtype.GenericActionResponse;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.type.EQCActionData;
import com.epocal.reader.type.EQCActionResult;
import com.epocal.reader.type.MessageDescriptor;

import java.io.IOException;

/**
 * Created by dning on 6/30/2017.
 */

public class GenActRspEQC extends MsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Generic, MessageGroup.Action,
            GenericActionResponse.eQC.value);

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

    private int meQCId;
    private EQCActionData mData;
    private EQCActionResult mEQCActionResult;

    public EQCActionResult getmEQCActionResult() {
        return mEQCActionResult;
    }

    public void setmEQCActionResult(EQCActionResult mEQCActionResult) {
        this.mEQCActionResult = mEQCActionResult;
    }

    public int geteQCId() {
        return meQCId;
    }

    public void seteQCId(int meQCId) {
        this.meQCId = meQCId;
    }

    public EQCActionData getData() {
        return mData;
    }

    public void setData(EQCActionData mData) {
        this.mData = mData;
    }

    public GenActRspEQC() {
        mData = new EQCActionData();
        mEQCActionResult = new EQCActionResult();
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
                case Ack:
                case Error:
                    try {
                        mResponseDetails = dsr.readByte();
                    } catch (IOException e) {
                        parseResult = ParseResult.UnknownFailure;
                    }
                    break;
                case ActionStarted:
                    try {
                        meQCId = BigEndianBitConverter.toInt32(dsr, 4, 0);
                    } catch (IOException e) {
                        parseResult = ParseResult.UnknownFailure;
                    }
                    break;
                case ActionCompleted:
                    try {
                        meQCId = BigEndianBitConverter.toInt32(dsr, 4, 0);
                        mEQCActionResult.readBytes(dsr);
                    } catch (IOException e) {
                        parseResult = ParseResult.UnknownFailure;
                    }
                    break;
                case Data:
                    try {
                        mData.readBytes(dsr);
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

        switch (MessageCodeType.convert(getMessageCode())) {
            case Ack:
            case Error:
                sb.append("ResponseDetails: " + mResponseDetails);
                break;
            case ActionStarted:
                sb.append("eQCId: " + meQCId);
                break;
            case ActionCompleted:
                sb.append("eQCId: " + meQCId);
                sb.append("mEQCActionResult" + mEQCActionResult.toString());
                break;
            case Data:
                sb.append("Data: " + mData.toString());
                break;
        }
        return sb.toString();
    }
}
