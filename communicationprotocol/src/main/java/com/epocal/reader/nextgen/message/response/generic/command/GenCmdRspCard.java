package com.epocal.reader.nextgen.message.response.generic.command;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.enumtype.GenericCommandResponse;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.type.CardInformation;
import com.epocal.reader.type.MessageDescriptor;

import java.io.IOException;

/**
 * Created by dning on 7/21/2017.
 */

public class GenCmdRspCard extends MsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Generic, MessageGroup.Command,
            GenericCommandResponse.CardResponse.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public enum MessageCodeType {
        NotDefined((byte) 0),
        CardInformation((byte) 1),
        RemoveCard((byte) 2),
        AcceptCard((byte) 3),
        InvalidCard((byte)4);

        public final byte value;

        MessageCodeType(byte value) {
            this.value = Byte.valueOf(value);
        }

        public static MessageCodeType convert(byte value) {
            return MessageCodeType.values()[value];
        }
    }

    public enum DataResponseCode {
        NotDefined((byte) 0),
        ACK((byte) 1),
        Err((byte) 2);

        public final byte value;

        DataResponseCode(byte value) {
            this.value = Byte.valueOf(value);
        }

        public static DataResponseCode convert(byte value) {
            return DataResponseCode.values()[value];
        }
    }

    public MessageCodeType mResponse;
    public MessageCodeType getResponse() {
        return MessageCodeType.convert(getMessageCode());
    }

    private byte mResponseDetails;

    public byte getResponseDetails() {
        return mResponseDetails;
    }
    public void setResponseDetails(byte mResponseDetails) {
        this.mResponseDetails = mResponseDetails;
    }

    private CardInformation mCardInformation;
    public CardInformation getCardInformation() {return mCardInformation;}
    public void setCardInformation(CardInformation mCardInformation) {this.mCardInformation = mCardInformation;}

    public GenCmdRspCard() {
        setMessageCode(MessageCodeType.NotDefined.value);
    }

    public GenCmdRspCard(byte errorCode) {
        setMessageCode(MessageCodeType.NotDefined.value);
        mResponseDetails = errorCode;
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
            mResponseDetails = dsr.readByte();
        }catch (IOException e)
        {parseResult = ParseResult.UnknownFailure;}

        dsr.dispose();
        return parseResult;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MsgCode: " + MessageCodeType.convert(getMessageCode()).toString());
        sb.append("ResponseDetails: " + mResponseDetails);
        return sb.toString();
    }
}
