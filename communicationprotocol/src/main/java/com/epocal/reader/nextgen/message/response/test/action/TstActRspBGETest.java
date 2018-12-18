package com.epocal.reader.nextgen.message.response.test.action;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.TestActionResponse;
import com.epocal.reader.type.MessageDescriptor;
import com.epocal.reader.type.TestActionData;

import java.io.IOException;

/**
 * Created by dning on 7/25/2017.
 */

public class TstActRspBGETest extends MsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Test, MessageGroup.Action,
            TestActionResponse.BGETest.value);

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

    private int mTestId;
    private TestActionData mData;

    public int getTestId() {
        return mTestId;
    }

    public void setTestId(int mTestId) {
        this.mTestId = mTestId;
    }

    public TestActionData getData() {
        return mData;
    }

    public void setData(TestActionData mData) {
        this.mData = mData;
    }

    public TstActRspBGETest() {
        setMessageCode(MessageCodeType.NotDefined.value);
        mData = new TestActionData();
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
            if (getMessageCode() == MessageCodeType.ActionStarted.value || getMessageCode() == MessageCodeType.ActionCompleted.value) {
                mTestId = BigEndianBitConverter.toInt32(dsr, 4, 0);
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
        if (getMessageCode() == MessageCodeType.ActionStarted.value || getMessageCode() == MessageCodeType.ActionCompleted.value) {
            sb.append("TestId: " + mTestId);
        }
        else if(getMessageCode() == MessageCodeType.Data.value) {
            sb.append("Data: " + mData.toString());
        }
        return sb.toString();
    }
}

