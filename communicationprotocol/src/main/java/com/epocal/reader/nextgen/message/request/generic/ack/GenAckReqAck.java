package com.epocal.reader.nextgen.message.request.generic.ack;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.enumtype.GenericAckRequest;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageCode;
import com.epocal.reader.enumtype.MessageDetails;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.MessageType;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.type.MessageDescriptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Created by dning on 7/5/2017.
 */

public class GenAckReqAck extends MsgPayload
{
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Generic, MessageGroup.Ack,
            (byte) GenericAckRequest.Ack.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public enum MessageCodeType
    {
        NotDefined ((byte)0);

        public final byte value;
        MessageCodeType(byte value)
        {
            this.value = Byte.valueOf(value);
        }
        public static MessageCodeType convert(byte value) {return MessageCodeType.values()[value];}
    }

    private MessageGroup mGroup;
    private MessageType mType;
    private MessageCode mCode;
    private MessageDetails mDetails;

    public GenAckReqAck()
    {
        setMessageCode(MessageCodeType.NotDefined.value);
        mGroup = MessageGroup.Ack;
        mType = MessageType.Undefined;
        mCode = MessageCode.NotDefined;
        mDetails = MessageDetails.NotDefined;
    }

    @Override
    public int fillBuffer() {

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            output.write(getDescriptor().toBytes());
            output.write(getMessageCode());
            output.write(mGroup.value);
            output.write(mType.value);
            output.write(mCode.value);
            output.write(mDetails.value);
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
        sb.append("Group: " + mGroup.toString());
        sb.append("Type: " + mType.toString());
        sb.append("Code: " + mCode.toString());
        sb.append("Details: " + mDetails.toString());
        return sb.toString();
    }
}
