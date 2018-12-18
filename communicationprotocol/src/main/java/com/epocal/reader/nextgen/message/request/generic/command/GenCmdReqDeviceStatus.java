package com.epocal.reader.nextgen.message.request.generic.command;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.enumtype.GenericCommandRequest;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.type.MessageDescriptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/19/2017.
 */

public class GenCmdReqDeviceStatus extends MsgPayload {

    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Generic, MessageGroup.Command,
            (byte) GenericCommandRequest.DeviceStatusRequest.value);

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

    public GenCmdReqDeviceStatus()
    {
        setMessageCode(MessageCodeType.NotDefined.value);
    }

    public GenCmdReqDeviceStatus(MessageCodeType code)
    {
        setMessageCode(code.value);
    }

    @Override
    public int fillBuffer() {

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            output.write(getDescriptor().toBytes());
            output.write(getMessageCode());
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
        return sb.toString();
    }
}
