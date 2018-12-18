package com.epocal.reader.nextgen.message.request.generic.session;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.enumtype.GenericSessionRequest;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.type.MessageDescriptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/29/2017.
 */

public class GenSsnReqSessionEqc extends MsgPayload
{
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Generic, MessageGroup.Session,
            (byte) GenericSessionRequest.Eqc.value);

    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public enum MessageCodeType
    {
        NotDefined ((byte)0),
        Enable ((byte)1),
        Disable ((byte)2),
        Resume ((byte)3),
        Cancel ((byte)4);

        public final byte value;
        MessageCodeType(byte value)
        {
            this.value = Byte.valueOf(value);
        }
        public static MessageCodeType convert(byte value) {return MessageCodeType.values()[value];}
    }

    public GenSsnReqSessionEqc()
    {
        setMessageCode(MessageCodeType.NotDefined.value);
    }

    public GenSsnReqSessionEqc(MessageCodeType code)
    {
        setMessageCode(code.value);
    }

    private int mSessionId;
    private long mSessionTime;

    public int getSessionId() {
        return mSessionId;
    }

    public void setSessionId(int mSessionId) {
        this.mSessionId = mSessionId;
    }

    public long getSessionTime() {
        return mSessionTime;
    }

    public void setSessionTime(long mSessionTime) {
        this.mSessionTime = mSessionTime;
    }

    @Override
    public int fillBuffer() {

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            output.write(getDescriptor().toBytes());
            output.write(getMessageCode());

            switch (MessageCodeType.convert(getMessageCode()))
            {
                case Enable:
                    output.write(BigEndianBitConverter.getBytes(mSessionId));
                    output.write(BigEndianBitConverter.getBytes(mSessionTime));
                    break;
                case Disable:
                case Resume:
                    output.write(BigEndianBitConverter.getBytes(mSessionId));
                    break;
                case Cancel:
                    break;
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
        sb.append("mSessionId: " + mSessionId);
        if (MessageCodeType.convert(getMessageCode()) == MessageCodeType.Enable) {
            sb.append("mSessionTime: " + mSessionTime);
        }
        return sb.toString();
    }

}
