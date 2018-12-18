package com.epocal.reader.nextgen.message.request.generic.configuration;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.enumtype.GenericConfigurationRequest;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.type.EQCConfiguration;
import com.epocal.reader.type.MessageDescriptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/26/2017.
 */

public class GenCfgReqSIBeQC extends MsgPayload
{
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Generic, MessageGroup.Configuration,
            (byte) GenericConfigurationRequest.SIBeQC.value);

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

    private EQCConfiguration mEQCConfig;

    public EQCConfiguration getEQCConfig() {
        return mEQCConfig;
    }

    public void setEQCConfig(EQCConfiguration mEQCConfig) {
        this.mEQCConfig = mEQCConfig;
    }

    public GenCfgReqSIBeQC()
    {
        mEQCConfig = new EQCConfiguration();
        setMessageCode(MessageCodeType.NotDefined.value);
    }

    public GenCfgReqSIBeQC(MessageCodeType code, EQCConfiguration config)
    {
        mEQCConfig = config;
        setMessageCode(code.value);
    }

    @Override
    public int fillBuffer() {

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            output.write(getDescriptor().toBytes());
            output.write(getMessageCode());
            output.write(mEQCConfig.toBytes());

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
        sb.append("EQCConfig: " + mEQCConfig.toString());
        return sb.toString();
    }
}
