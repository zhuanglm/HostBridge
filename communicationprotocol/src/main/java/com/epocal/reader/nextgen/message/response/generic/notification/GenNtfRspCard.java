package com.epocal.reader.nextgen.message.response.generic.notification;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.enumtype.GenericNotificationResponse;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.SwitchLevel;
import com.epocal.reader.type.CardInformation;
import com.epocal.reader.type.MessageDescriptor;

import java.io.IOException;

/**
 * Created by dning on 7/4/2017.
 */

public class GenNtfRspCard extends MsgPayload
{
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Generic, MessageGroup.Notification,
            GenericNotificationResponse.Card.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public enum MessageCodeType
    {
        NotDefined ((byte)0),
        Inserted ((byte)1),
        Detected ((byte)2),
        Removed ((byte)3),
        CardInformation ((byte)4),
        Engaged ((byte)5);

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

    private CardInformation mCardInformation;
    private SwitchLevel mFrontSwitchLevel;
    private SwitchLevel mRearSwitchLevel;

    public CardInformation getCardInformation() {
        return mCardInformation;
    }

    public void setCardInformation(CardInformation mCardInformation) {
        this.mCardInformation = mCardInformation;
    }

    public SwitchLevel getFrontSwitchLevel() {
        return mFrontSwitchLevel;
    }

    public void setFrontSwitchLevel(SwitchLevel mFrontSwitchLevel) {
        this.mFrontSwitchLevel = mFrontSwitchLevel;
    }

    public SwitchLevel getRearSwitchLevel() {
        return mRearSwitchLevel;
    }

    public void setRearSwitchLevel(SwitchLevel mRearSwitchLevel) {
        this.mRearSwitchLevel = mRearSwitchLevel;
    }

    public GenNtfRspCard()
    {
        mCardInformation = new CardInformation();
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
                case Inserted:
                case Detected:
                case Removed:
                    try {
                        mFrontSwitchLevel = SwitchLevel.convert(dsr.readByte());
                        mRearSwitchLevel = SwitchLevel.convert(dsr.readByte());
                    } catch (IOException e) {
                        parseResult = ParseResult.UnknownFailure;
                    }
                    break;
                case CardInformation:
                    try {
                        mCardInformation.readBytes(dsr);
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
            case Inserted:
            case Detected:
            case Removed:
                sb.append("FrontSwitchLevel: " + mFrontSwitchLevel.toString());
                sb.append("RearSwitchLevel: " + mRearSwitchLevel.toString());
                break;
            case CardInformation:
                sb.append("CardInformation: " + mCardInformation.toString());
                break;
        }
        return sb.toString();
    }
}
