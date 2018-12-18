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
import com.epocal.reader.type.TestInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 7/17/2017.
 */

public class GenCmdReqPerformTest extends MsgPayload
{
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Generic, MessageGroup.Command,
            (byte) GenericCommandRequest.PerformTest.value);

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
        Cancel ((byte)4),
        Enabled ((byte)5),
        Disabled ((byte)6);

        public final byte value;
        MessageCodeType(byte value)
        {
            this.value = Byte.valueOf(value);
        }
        public static MessageCodeType convert(byte value) {return MessageCodeType.values()[value];}
    }

    private int mTestId = 1;
    private TestPerformMode mTestPerformMode;
    private TestInfo mTestInfo;

    public int getTestId() {
        return mTestId;
    }

    public void setTestId(int mTestId) {
        this.mTestId = mTestId;
    }

    public TestPerformMode getTestPerformMode() {
        return mTestPerformMode;
    }

    public void setTestPerformMode(TestPerformMode mTestPerformMode) {
        this.mTestPerformMode = mTestPerformMode;
    }

    public TestInfo getTestInfo() {
        return mTestInfo;
    }

    public void setTestInfo(TestInfo mTestInfo) {
        this.mTestInfo = mTestInfo;
    }

    public GenCmdReqPerformTest()
    {
        setMessageCode(MessageCodeType.NotDefined.value);
        mTestId = 0;
        mTestPerformMode = TestPerformMode.Auto;
        mTestInfo = new TestInfo();
    }
    public GenCmdReqPerformTest(MessageCodeType code)
    {
        setMessageCode(code.value);
        mTestId = 0;
        mTestPerformMode = TestPerformMode.Auto;
        mTestInfo = new TestInfo();
    }

    @Override
    public int fillBuffer() {

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            output.write(getDescriptor().toBytes());
            output.write(getMessageCode());
            output.write(BigEndianBitConverter.getBytes(mTestId));
            if (getMessageCode() == MessageCodeType.Start.value)
            {
                output.write(mTestPerformMode.value);
            }
            else if (getMessageCode() == MessageCodeType.Enabled.value)
            {
                output.write(mTestInfo.toBytes());
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
        sb.append("TestId: " + mTestId);
        if (getMessageCode() == MessageCodeType.Start.value)
        {
            sb.append("TestPerformMode: " + mTestPerformMode);
        }
        else if (getMessageCode() == MessageCodeType.Enabled.value)
        {
            sb.append("TestInfo: " + mTestInfo.toString());
        }
        return sb.toString();
    }
}
