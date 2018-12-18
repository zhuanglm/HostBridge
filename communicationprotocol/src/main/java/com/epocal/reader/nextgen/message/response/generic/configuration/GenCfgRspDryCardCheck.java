package com.epocal.reader.nextgen.message.response.generic.configuration;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.enumtype.GenericConfigurationResponse;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.type.DryCardCheckConfiguration;
import com.epocal.reader.type.MessageDescriptor;

import java.io.IOException;

/**
 * Created by dning on 6/28/2017.
 */

public class GenCfgRspDryCardCheck extends MsgPayload
{
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Generic, MessageGroup.Configuration,
            GenericConfigurationResponse.DryCardCheck.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public enum MessageCodeType
    {
        NotDefined ((byte)0),
        Ack ((byte)1),
        Error ((byte)2),
        Configuration ((byte)3);

        public final byte value;
        MessageCodeType(byte value)
        {
            this.value = Byte.valueOf(value);
        }
        public static MessageCodeType convert(byte value) {return MessageCodeType.values()[value];}
    }

    public MessageCodeType mResponse;
    public MessageCodeType getResponse()
    {
        return MessageCodeType.convert(getMessageCode());
    }

    private byte mResponseDetails;

    public byte getResponseDetails() {
        return mResponseDetails;
    }

    public void setResponseDetails(byte mResponseDetails) {
        this.mResponseDetails = mResponseDetails;
    }

    private DryCardCheckConfiguration mConfig;
    public DryCardCheckConfiguration getEQCConfig() {
        return mConfig;
    }
    public void setEQCConfig(DryCardCheckConfiguration mConfig) {
        this.mConfig = mConfig;
    }

    public GenCfgRspDryCardCheck()
    {
        mConfig = new DryCardCheckConfiguration();
    }

    public GenCfgRspDryCardCheck(MessageCodeType messageCode, byte respDetail)
    {
        mResponseDetails = respDetail;
        setMessageCode(messageCode.value);
    }

    @Override
    public int fillBuffer() {
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer)
    {
        setRawBuffer(buffer);

        DataStreamReader dsr = new DataStreamReader(getRawBuffer());
        ParseResult parseResult = ParseResult.Success;

        try {
            getDescriptor().readBytes(dsr);
            setMessageCode(dsr.readByte());
        }catch (IOException e)
        {parseResult = ParseResult.UnknownFailure;}

        if (parseResult == ParseResult.Success) {
            switch (MessageCodeType.convert(getMessageCode())) {
                case Ack:
                case Error:
                    try {
                        mResponseDetails = dsr.readByte();
                    } catch (IOException e) {
                        parseResult = ParseResult.UnknownFailure;
                    }
                    break;
                case Configuration:
                    try {
                        mConfig.readBytes(dsr);
                    } catch (IOException e) {
                        parseResult = ParseResult.UnknownFailure;
                    }
                    break;
                default:
                    parseResult = ParseResult.InvalidMessageCode;
            }
        }
        dsr.dispose();
        return parseResult;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MsgCode: " + MessageCodeType.convert(getMessageCode()).toString());

        switch (MessageCodeType.convert(getMessageCode())) {
            case Ack:
            case Error:
                sb.append("ResponseDetails: " + mResponseDetails);
                break;
            case Configuration:
                mConfig.toString();
                break;
        }
        return sb.toString();
    }

}
