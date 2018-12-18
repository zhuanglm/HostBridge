package com.epocal.reader.nextgen.message.response.generic.error;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.common.FixedArray;
import com.epocal.reader.enumtype.GenericErrorResponse;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.type.MessageDescriptor;
import com.epocal.util.UnsignedType;

import java.io.IOException;

/**
 * Created by dning on 8/15/2017.
 */

public class GenErrRspCommunicationInterface extends MsgPayload
{
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Generic, MessageGroup.Error,
            GenericErrorResponse.CommunicationInterface.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public enum MessageCodeType
    {
        NotDefined ((byte)0),
        InterfaceNotSupported ((byte)1),
        UnknownMessage ((byte)2),
        MessageNotSupported ((byte)3),
        InvalidRequest ((byte)4),
        CorruptedMessage ((byte)5),
        DataSkipped ((byte)6);

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

    public enum CorruptedMessageDetails
    {
        NotDefined ((byte)0),
        HeaderCRCMismatch ((byte)1),
        PayloadCRCMismatch ((byte)2),
        WrongMsgLength ((byte)3),
        NoEndOfPacket ((byte)4);

        public final byte value;
        CorruptedMessageDetails(byte value)
        {
            this.value = Byte.valueOf(value);
        }
        public static CorruptedMessageDetails convert(byte value) {return CorruptedMessageDetails.values()[value];}
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

    // Received Message Descriptor
    private byte mReceivedMessageClass;
    private byte mReceivedMessageGroup;
    private byte mReceivedMessageType;
    private byte mReceivedMessageCode;

    public FixedArray<Byte> mMessageDetails;
    public UnsignedType.UInt16 mNumberOfDroppedBytes;
    public CorruptedMessageDetails mCorruptedMsgDetails;

    public GenErrRspCommunicationInterface()
    {
        setMessageCode(MessageCodeType.NotDefined.value);
        mMessageDetails = new FixedArray<Byte>(Byte.class, 0);
        mErrorSeverity = ErrorSeverity.NotDefined;
        mReceivedMessageClass = 0;
        mReceivedMessageGroup = 0;
        mReceivedMessageType = 0;
        mReceivedMessageCode = 0;
        mCorruptedMsgDetails = CorruptedMessageDetails.NotDefined;
        mNumberOfDroppedBytes = new UnsignedType.UInt16();
        mNumberOfDroppedBytes.setValue((short)0);
    }

    public GenErrRspCommunicationInterface(byte errorCode)
    {
        setMessageCode(errorCode);
        mResponseDetails = errorCode;
        mMessageDetails = new FixedArray<Byte>(Byte.class, 0);
        mErrorSeverity = ErrorSeverity.NotDefined;
        mReceivedMessageClass = 0;
        mReceivedMessageGroup = 0;
        mReceivedMessageType = 0;
        mReceivedMessageCode = 0;
        mCorruptedMsgDetails = CorruptedMessageDetails.NotDefined;
        mNumberOfDroppedBytes = new UnsignedType.UInt16();
        mNumberOfDroppedBytes.setValue((short)0);
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
                mReceivedMessageClass = dsr.readByte();
                mReceivedMessageGroup = dsr.readByte();
                mReceivedMessageType = dsr.readByte();
                mReceivedMessageCode = dsr.readByte();

                switch (MessageCodeType.convert(getMessageCode())) {
                    case CorruptedMessage:
                        try {
                            mCorruptedMsgDetails = CorruptedMessageDetails.convert(dsr.readByte());
                        } catch (IOException e) {
                            parseResult = ParseResult.UnknownFailure;
                        }
                        break;
                    case DataSkipped:
                        try {
                            mNumberOfDroppedBytes.setValue(BigEndianBitConverter.toInt16(dsr, 2, 0));
                            mMessageDetails.setData(Byte.class, FixedArray.toClass(dsr.readFixedLength(dsr, mMessageDetails.FixedLen)));
                        } catch (IOException e) {
                            parseResult = ParseResult.UnknownFailure;
                        }
                        break;
                    default:
                        parseResult = ParseResult.InvalidMessageCode;
                }
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
        sb.append("ReceivedMessageClass: " + mReceivedMessageClass);
        sb.append("ReceivedMessageGroup: " + mReceivedMessageGroup);
        sb.append("ReceivedMessageType: " + mReceivedMessageType);
        sb.append("ReceivedMessageCode: " + mReceivedMessageCode);


        switch (MessageCodeType.convert(getMessageCode())) {
            case CorruptedMessage:
                sb.append("CorruptedMsgDetails: " + mCorruptedMsgDetails.toString());
                break;
            case DataSkipped:
                sb.append("NumberOfDroppedBytes: " + mNumberOfDroppedBytes);
                break;
        }
        return sb.toString();
    }
}

