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
 * Created by dning on 6/29/2017.
 */

public class GenCmdReqPerformEQC extends MsgPayload
{
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Generic, MessageGroup.Command,
            (byte) GenericCommandRequest.PerformEqc.value);

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

    private int mEQCId;
    private TestPerformMode mTestPerformMode;

    public int getEQCId() {
        return mEQCId;
    }

    public void setEQCId(int mEQCId) {
        this.mEQCId = mEQCId;
    }

    public TestPerformMode getTestPerformMode() {
        return mTestPerformMode;
    }

    public void setTestPerformMode(TestPerformMode mTestPerformMode) {
        this.mTestPerformMode = mTestPerformMode;
    }

    public GenCmdReqPerformEQC()
    {
        setMessageCode(MessageCodeType.NotDefined.value);
        mEQCId = 0;
        mTestPerformMode = TestPerformMode.Auto;
    }
    public GenCmdReqPerformEQC(MessageCodeType code)
    {
        setMessageCode(code.value);
    }

    @Override
    public int fillBuffer() {

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            output.write(getDescriptor().toBytes());
            output.write(getMessageCode());
            output.write(BigEndianBitConverter.getBytes(mEQCId));
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
        sb.append("EQCId: " + mEQCId);
        if (getMessageCode() == MessageCodeType.Start.value) {
            sb.append("TestPerformMode: " + mTestPerformMode.toString());
        }
        return sb.toString();
    }
}
