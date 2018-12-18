package com.epocal.reader;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParsingMode;
import com.epocal.reader.enumtype.ProtoGeneration;
import com.epocal.reader.type.MessageDescriptor;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by dning on 6/9/2017.
 */

public class PreliminaryMessage {

    public byte getGeneration() {
        return mGeneration;
    }

    private void setGeneration(byte mGeneration) {
        this.mGeneration = mGeneration;
    }

    public byte getHeaderLength() {
        return mHeaderLength;
    }

    private void setHeaderLength(byte mHeaderLength) {
        this.mHeaderLength = mHeaderLength;
    }

    public byte getInterfaceType() {
        return mInterfaceType;
    }

    private void setInterfaceType(byte mInterfaceType) {
        this.mInterfaceType = mInterfaceType;
    }

    public byte getVersionMajor() {
        return mVersionMajor;
    }

    private void setVersionMajor(byte mVersionMajor) {
        this.mVersionMajor = mVersionMajor;
    }

    public byte getVersionMinor() {
        return mVersionMinor;
    }

    private void setVersionMinor(byte mVersionMinor) {
        this.mVersionMinor = mVersionMinor;
    }

    public byte getSourceDevice() {
        return mSourceDevice;
    }

    private void setSourceDevice(byte mSourceDevice) {
        this.mSourceDevice = mSourceDevice;
    }

    public byte getDestinationDevice() {
        return mDestinationDevice;
    }

    private void setDestinationDevice(byte mDestinationDevice) {
        this.mDestinationDevice = mDestinationDevice;
    }

    public short getPacketNumber() {
        return mPacketNumber;
    }

    private void setPacketNumber(short mPacketNumber) {
        this.mPacketNumber = mPacketNumber;
    }

    public short getPayloadLength() {
        return mPayloadLength;
    }

    private void setPayloadLength(short mPayloadLength) {
        this.mPayloadLength = mPayloadLength;
    }

    public byte[] getPayload() {
        return mPayload;
    }

    private void setPayload(byte[] mPayload) {
        this.mPayload = mPayload;
    }

    public short getHeaderCRC() {
        return mHeaderCRC;
    }

    private void setHeaderCRC(short mHeaderCRC) {
        this.mHeaderCRC = mHeaderCRC;
    }

    public short getPayloadCRC() {
        return mPayloadCRC;
    }

    private void setPayloadCRC(short mPayloadCRC) {
        this.mPayloadCRC = mPayloadCRC;
    }

    public MessageDescriptor getMessageDescriptor() {
        return mMessageDescriptor;
    }

    private void setMessageDescriptor(MessageDescriptor mMessageDescriptor) {
        this.mMessageDescriptor = mMessageDescriptor;
    }

    public ParsingMode getParsingMode() {
        return mParsingMode;
    }

    public void setParsingMode(ParsingMode mParsingMode) {
        this.mParsingMode = mParsingMode;
    }

    private ParsingMode mParsingMode;
    private byte[] mPreamble;
    private byte mGeneration;
    private byte mHeaderLength;
    private byte mInterfaceType;
    private byte mVersionMajor;
    private byte mVersionMinor;
    private byte mSourceDevice;
    private byte mDestinationDevice;
    private short mPacketNumber;
    private short mPayloadLength;
    private byte[] mPayload;
    private short mHeaderCRC;
    private short mPayloadCRC;
    private byte[] mEndOfPacket;

    private MessageDescriptor mMessageDescriptor; //{ get; private set; }


    //added by rzhuang at July 25 2018 for Legacy reader
    public PreliminaryMessage(byte[] data, ProtoGeneration proto) throws IOException
    {
        switch (proto) {
            case Legacy:
                mHeaderLength = ProtocolChecker.LegacyHeaderLength;
                mPreamble = Arrays.copyOfRange(data, 0, 4);
                mPacketNumber = BigEndianBitConverter.byte2short(Arrays.copyOfRange(data, 5, 7));

                mPayloadLength = BigEndianBitConverter.byte2short(Arrays.copyOfRange(data,
                        ProtocolChecker.LegacyMsgLengthOffset,
                        ProtocolChecker.LegacyMsgLengthOffset+ProtocolChecker.MsgLengthBytesNum));
                mPayload = Arrays.copyOfRange(data, mHeaderLength-1, mHeaderLength + mPayloadLength-1);
                mEndOfPacket = Arrays.copyOfRange(data, data.length - 1, data.length);

                mMessageDescriptor = new MessageDescriptor(InterfaceType.Legacy,
                        MessageClass.NotDefined, MessageGroup.NotDefined, (byte)0);
                mMessageDescriptor.setType(data[4]);
                break;
        }
    }

    public PreliminaryMessage(byte[] data) throws IOException
    {
        mPreamble = Arrays.copyOfRange(data, 0, 3);
        mGeneration = data[3];
        mHeaderLength = data[4];
        mInterfaceType = data[5];
        mVersionMajor = data[6];
        mVersionMinor = data[7];
        mSourceDevice = data[8];
        mDestinationDevice = data[9];
        mPacketNumber = (short)BigEndianBitConverter.toInt16(Arrays.copyOfRange(data, 10, 12), 0);
        mPayloadLength = (short)(BigEndianBitConverter.toInt16(Arrays.copyOfRange(data, 12, 14), 0)-2);
        mHeaderCRC = (short)BigEndianBitConverter.toInt16(Arrays.copyOfRange(data, 14, 16), 0);
        mPayload = Arrays.copyOfRange(data, 16, 16 + mPayloadLength);
        mPayloadCRC = (short)BigEndianBitConverter.toInt16(Arrays.copyOfRange(data, 16 + mPayloadLength, 16 + mPayloadLength + 2), 0);
        mEndOfPacket = Arrays.copyOfRange(data, data.length - 1, data.length);

        mMessageDescriptor = new MessageDescriptor(InterfaceType.NotDefined, MessageClass.NotDefined, MessageGroup.NotDefined, (byte)0);
        mMessageDescriptor.setMsgClass(MessageClass.convert(mPayload[0]));
        mMessageDescriptor.setMsgGroup(MessageGroup.convert(mPayload[1]));
        mMessageDescriptor.setType(mPayload[2]);
        mMessageDescriptor.setMsgInterface(InterfaceType.convert(mInterfaceType));
    }

    public byte[] getArray() {return mPreamble;}
    public MessageDescriptor descriptor() {return mMessageDescriptor;}
}