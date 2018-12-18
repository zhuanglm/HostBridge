package com.epocal.reader.nextgen.message.response.generic.configuration;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.enumtype.ConfigGroupFlag;
import com.epocal.reader.enumtype.GenericConfigurationResponse;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.type.BTDescriptor;
import com.epocal.reader.type.GeneralConfiguration;
import com.epocal.reader.type.MaintenanceTestRecordNumber;
import com.epocal.reader.type.MessageDescriptor;
import com.epocal.util.ByteUtil;

import java.io.IOException;

/**
 * Created by dning on 6/27/2017.
 */

public class GenCfgRspGeneral extends MsgPayload
{
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Generic, MessageGroup.Configuration,
            GenericConfigurationResponse.General.value);

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

    private ConfigGroupFlag mGroupFlag;
    private GeneralConfiguration mConfig;
    private BTDescriptor mBTDescriptor;
    private MaintenanceTestRecordNumber mTestRecordNumber;

    public GenCfgRspGeneral()
    {
        mGroupFlag = ConfigGroupFlag.General;
        mConfig = new GeneralConfiguration();
        mBTDescriptor = new BTDescriptor();
        mTestRecordNumber = new MaintenanceTestRecordNumber();
    }

    public GenCfgRspGeneral(MessageCodeType respCode, byte respDetail)
    {
        setMessageCode(respCode.value);
        mResponseDetails = respDetail;
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
                        mGroupFlag = ConfigGroupFlag.convert(ByteUtil.toUnsignedInt(dsr.readByte()));
                        if (mGroupFlag.hasFlag(ConfigGroupFlag.General)) {
                            mConfig.readBytes(dsr);
                        }
                        if (mGroupFlag.hasFlag(ConfigGroupFlag.BtDescriptor)) {
                            mBTDescriptor.readBytes(dsr);
                        }
                        if (mGroupFlag.hasFlag(ConfigGroupFlag.MaintenanceTestRecord)) {
                            mTestRecordNumber.readBytes(dsr);
                        }
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
                if (mGroupFlag.hasFlag(ConfigGroupFlag.General))
                {
                    mConfig.toString();
                }
                if (mGroupFlag.hasFlag(ConfigGroupFlag.BtDescriptor))
                {
                    mBTDescriptor.toString();
                }
                if (mGroupFlag.hasFlag(ConfigGroupFlag.MaintenanceTestRecord))
                {
                    mTestRecordNumber.toString();
                }
                break;
        }
        return sb.toString();
    }
}
