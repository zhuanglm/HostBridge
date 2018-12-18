package com.epocal.reader.nextgen.message.request.generic.configuration;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.enumtype.ConfigGroupFlag;
import com.epocal.reader.enumtype.GenericConfigurationRequest;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.type.BTDescriptor;
import com.epocal.reader.type.GeneralConfiguration;
import com.epocal.reader.type.MaintenanceTestRecordNumber;
import com.epocal.reader.type.MessageDescriptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/26/2017.
 */

public class GenCfgReqGeneral extends MsgPayload
{
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Generic, MessageGroup.Configuration,
            (byte) GenericConfigurationRequest.General.value);

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

    private ConfigGroupFlag mGroupFlag;
    private GeneralConfiguration mGeneralConfig;
    private BTDescriptor mBTDescriptor;
    private MaintenanceTestRecordNumber mTestRecordNumber;

    public ConfigGroupFlag getGroupFlag() {
        return mGroupFlag;
    }

    public void setGroupFlag(ConfigGroupFlag mGroupFlag) {
        this.mGroupFlag = mGroupFlag;
    }

    public GeneralConfiguration getGeneralConfig() {
        return mGeneralConfig;
    }

    public void setGeneralConfig(GeneralConfiguration mGeneralConfig) {
        this.mGeneralConfig = mGeneralConfig;
    }

    public BTDescriptor getBTDescriptor() {
        return mBTDescriptor;
    }

    public void setBTDescriptor(BTDescriptor mBTDescriptor) {
        this.mBTDescriptor = mBTDescriptor;
    }

    public MaintenanceTestRecordNumber getTestRecordNumber() {
        return mTestRecordNumber;
    }

    public void setTestRecordNumber(MaintenanceTestRecordNumber mTestRecordNumber) {
        this.mTestRecordNumber = mTestRecordNumber;
    }

    public GenCfgReqGeneral()
    {
        setMessageCode(MessageCodeType.NotDefined.value);
    }

    public GenCfgReqGeneral(MessageCodeType code)
    {
        setMessageCode(code.value);
    }


    @Override
    public int fillBuffer() {

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            output.write(getDescriptor().toBytes());
            output.write(getMessageCode());

            output.write(mGroupFlag.value);
            if (MessageCodeType.convert(getMessageCode()) == MessageCodeType.Set)
            {
                if (mGroupFlag.hasFlag(ConfigGroupFlag.General))
                    output.write(mGeneralConfig.toBytes());
                if (mGroupFlag.hasFlag(ConfigGroupFlag.BtDescriptor))
                    output.write(mBTDescriptor.toBytes());
                if (mGroupFlag.hasFlag(ConfigGroupFlag.MaintenanceTestRecord))
                    output.write(mTestRecordNumber.toBytes());
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
        sb.append("GroupFlag: " + mGroupFlag);
        return sb.toString();
    }
}
