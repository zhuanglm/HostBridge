package com.epocal.reader.nextgen.message.response.generic.session;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.enumtype.GenericSessionResponse;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.type.MessageDescriptor;

import java.io.IOException;

/**
 * Created by dning on 6/26/2017.
 */

public class GenSsnRspSessionReaderSettings extends MsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Generic, MessageGroup.Session,
            (byte) GenericSessionResponse.ReaderSettings.value);

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

    private int mSessionId;

    public int getSessionId() {
        return mSessionId;
    }

    public void setSessionId(int mSessionId) {
        this.mSessionId = mSessionId;
    }

    public GenSsnRspSessionReaderSettings() {
        setMessageCode(MessageCodeType.NotDefined.value);
    }

    public GenSsnRspSessionReaderSettings(byte errorCode) {
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
                case Ack:
                    try {
                        mSessionId = BigEndianBitConverter.toInt32(dsr, 4, 0);
                        mResponseDetails = dsr.readByte();
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
                sb.append("SessionId: " + mSessionId);
                sb.append("ResponseDetails: " + mResponseDetails);
                break;
            case Error:
                sb.append("ResponseDetails: " + mResponseDetails);
                break;
        }
        return sb.toString();
    }
}
