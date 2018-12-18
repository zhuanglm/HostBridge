package com.epocal.reader.nextgen.message.request.generic.command;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.enumtype.GenericCommandRequest;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.TestPerformMode;
import com.epocal.reader.type.MessageDescriptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 7/18/2017.
 */

public class GenCmdReqPerformDryCardCheck extends MsgPayload
{
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Generic, MessageGroup.Command,
            (byte) GenericCommandRequest.PerformDryCard.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public enum MessageCodeType
    {
        NotDefined ((byte)0),
        Start ((byte)1),
        Stop ((byte)2),
        Resume ((byte)3),
        Cancel ((byte)4);

        public final byte value;
        MessageCodeType(byte value)
        {
            this.value = Byte.valueOf(value);
        }
        public static MessageCodeType convert(byte value) {return MessageCodeType.values()[value];}
    }

    private int mDryCardId;
    private TestPerformMode mTestPerformMode;

    public int geDryCardId() {
        return mDryCardId;
    }

    public void setDryCardId(int mDryCardId) {
        this.mDryCardId = mDryCardId;
    }

    public TestPerformMode getTestPerformMode() {
        return mTestPerformMode;
    }

    public void setTestPerformMode(TestPerformMode mTestPerformMode) {
        this.mTestPerformMode = mTestPerformMode;
    }

    public GenCmdReqPerformDryCardCheck()
    {
        setMessageCode(MessageCodeType.NotDefined.value);
        mDryCardId = 0;
        mTestPerformMode = TestPerformMode.Auto;
    }
    public GenCmdReqPerformDryCardCheck(MessageCodeType code)
    {
        setMessageCode(code.value);
    }

    @Override
    public int fillBuffer() {

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            output.write(getDescriptor().toBytes());
            output.write(getMessageCode());
            output.write(BigEndianBitConverter.getBytes(mDryCardId));
            if (getMessageCode() == MessageCodeType.Start.value)
            {
                output.write(mTestPerformMode.value);
            }
            output.flush();
            setRawBuffer(output.toByteArray());
            output.close();
            return getRawBuffer().length;
        }
        catch (IOException e)
        {}
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer) {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MsgCode: " + MessageCodeType.convert(getMessageCode()).toString());
        sb.append("DryCardId: " + mDryCardId);
        if (getMessageCode() == MessageCodeType.Start.value)
        {
            sb.append("TestPerformMode: " + mTestPerformMode);
        }
        return sb.toString();
    }
}
