package com.epocal.reader.nextgen.message.request.generic.configuration;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.enumtype.GenericConfigurationRequest;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.type.DryCardCheckConfiguration;
import com.epocal.reader.type.MessageDescriptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/27/2017.
 */

public class GenCfgReqDryCardCheck extends MsgPayload
{
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Generic, MessageGroup.Configuration,
            (byte) GenericConfigurationRequest.DryCardCheck.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public enum MessageCodeType
    {
        NotDefined ((byte)0),
        Get ((byte)1),
        Set ((byte)2);

        public final byte value;
        MessageCodeType(byte value)
        {
            this.value = Byte.valueOf(value);
        }
        public static MessageCodeType convert(byte value) {return MessageCodeType.values()[value];}
    }

    private DryCardCheckConfiguration mDryCardConfig;

    public DryCardCheckConfiguration getDryCardConfig() {
        return mDryCardConfig;
    }

    public void setDryCardConfig(DryCardCheckConfiguration mDryCardConfig) {
        this.mDryCardConfig = mDryCardConfig;
    }

    public GenCfgReqDryCardCheck()
    {
        mDryCardConfig = new DryCardCheckConfiguration();
    }

    public GenCfgReqDryCardCheck(MessageCodeType code, DryCardCheckConfiguration config)
    {
        setMessageCode(code.value);
        mDryCardConfig = config;
    }

    @Override
    public int fillBuffer() {

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            output.write(getDescriptor().toBytes());
            output.write(getMessageCode());
            output.write(mDryCardConfig.toBytes());

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
        sb.append("DryCardConfig: " + mDryCardConfig.toString());
        return sb.toString();
    }
}
