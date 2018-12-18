package com.epocal.reader.nextgen.message.request.generic.command;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.enumtype.GenericCommandRequest;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.type.HostIdInfo;
import com.epocal.reader.type.MessageDescriptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/13/2017.
 */

public class GenCmdReqHostId extends MsgPayload {

    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Generic, MessageGroup.Command,
            (byte) GenericCommandRequest.HostIdInfo.value);

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

    public HostIdInfo getHostIdInfo() {
        return mHostIdInfo;
    }

    public void setHostIdInfo(HostIdInfo hostIdInfo) {
        this.mHostIdInfo = hostIdInfo;
    }

    private HostIdInfo mHostIdInfo;

    public GenCmdReqHostId()
    {
        setMessageCode(MessageCodeType.NotDefined.value);
        this.mHostIdInfo = new HostIdInfo();
    }

    public GenCmdReqHostId(MessageCodeType code)
    {
        setMessageCode(code.value);
        this.mHostIdInfo = new HostIdInfo();
    }

    @Override
    public int fillBuffer() {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            output.write(getDescriptor().toBytes());
            output.write(getMessageCode());
            output.write(mHostIdInfo.toBytes());
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
        sb.append("HostIdInfo: " + mHostIdInfo.toString());
        return sb.toString();
    }
}
