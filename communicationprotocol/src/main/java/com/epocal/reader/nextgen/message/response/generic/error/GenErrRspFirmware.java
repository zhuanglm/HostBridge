package com.epocal.reader.nextgen.message.response.generic.error;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.common.FixedArray;
import com.epocal.reader.enumtype.GenericErrorResponse;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.type.MessageDescriptor;

import java.io.IOException;

/**
 * Created by dning on 8/15/2017.
 */

public class GenErrRspFirmware extends MsgPayload
{
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Generic, MessageGroup.Error,
            GenericErrorResponse.Firmware.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public enum MessageCodeType
    {
        NotDefined ((byte)0),
        Compatibility ((byte)1),
        CPBootUpFailure ((byte)2),
        CooxBootUpFailure ((byte)3),
        BootUpFailure2D ((byte)4),
        WatchDogReset ((byte)5),
        Exception ((byte)6),
        UnexpectedReset ((byte)7),
        UpgradeRecoveryFailed ((byte)8);

        public final byte value;
        MessageCodeType(byte value)
        {
            this.value = Byte.valueOf(value);
        }
        public static MessageCodeType convert(byte value) {return MessageCodeType.values()[value];}
    }

    public enum ErrorSeverity
    {
        NotDefined ((byte)0),
        High ((byte)1),
        Low ((byte)2);

        public final byte value;
        ErrorSeverity(byte value)
        {
            this.value = Byte.valueOf(value);
        }
        public static ErrorSeverity convert(byte value) {return ErrorSeverity.values()[value];}
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

    private ErrorSeverity mErrorSeverity;

    public FixedArray<Byte> mMessageDetails;

    public GenErrRspFirmware()
    {
        setMessageCode(MessageCodeType.NotDefined.value);
        mMessageDetails = new FixedArray<Byte>(Byte.class, 0);
        mErrorSeverity = ErrorSeverity.NotDefined;
    }

    public GenErrRspFirmware(byte errorCode)
    {
        setMessageCode(errorCode);
        mResponseDetails = errorCode;
        mMessageDetails = new FixedArray<Byte>(Byte.class, 0);
        mErrorSeverity = ErrorSeverity.NotDefined;
    }

    @Override
    public int fillBuffer() {
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer)  {
        setRawBuffer(buffer);

        DataStreamReader dsr = new DataStreamReader(getRawBuffer());
        ParseResult parseResult = ParseResult.Success;

        try {
            getDescriptor().readBytes(dsr);
            setMessageCode(dsr.readByte());
        }catch (IOException e)
        { parseResult = ParseResult.UnknownFailure;}

        if (parseResult == ParseResult.Success) {
            try {
                mErrorSeverity = ErrorSeverity.convert(dsr.readByte());
            }
            catch (IOException e){
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
        sb.append("ErrorSeverity: " + mErrorSeverity.toString());
        return sb.toString();
    }
}
