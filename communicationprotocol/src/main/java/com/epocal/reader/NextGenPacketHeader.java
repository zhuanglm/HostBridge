package com.epocal.reader;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.EpocVersion;
import com.epocal.reader.enumtype.DestinationDevice;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.SourceDevice;
import com.epocal.reader.parser.IPacketHeader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/13/2017.
 */

public class NextGenPacketHeader implements IPacketHeader {
    @Override
    public byte[] getPreamble() {
        return mPreamble;
    }

    @Override
    public void setPreamble(byte[] preamble) {
        mPreamble = new byte[preamble.length];
        System.arraycopy(preamble, 0, mPreamble, 0, preamble.length);
    }

    @Override
    public byte getProtoGeneration() {
        return mProtoGeneration;
    }

    @Override
    public void setProtoGeneration(byte protoGeneration) {
        mProtoGeneration = protoGeneration;
    }

    @Override
    public byte getHeaderLength() {
        return mHeaderLength;
    }

    @Override
    public void setHeaderLength(byte headerLength) {
        mHeaderLength = headerLength;
    }

    @Override
    public byte getInterfaceType() {
        return mInterfaceType;
    }

    @Override
    public void setInterfaceType(byte interfaceType) {
        mInterfaceType = interfaceType;
    }

    @Override
    public EpocVersion getInterfaceVersion() {
        return mInterfaceVersion;
    }

    @Override
    public void setInterfaceVersion(EpocVersion interfaceVersion) {
        mInterfaceVersion = interfaceVersion;
    }

    @Override
    public byte getSourceDevice() {
        return mSourceDevice;
    }

    @Override
    public void setSourceDevice(byte sourceDevice) {
        mSourceDevice = sourceDevice;
    }

    @Override
    public byte getDestinationDevice() {
        return mDestinationDevice;
    }

    @Override
    public void setDestinationDevice(byte destinationDevice) {
        mDestinationDevice = destinationDevice;
    }

    @Override
    public short getPacketNumber() {
        return mPacketNumber;
    }

    @Override
    public void setPacketNumber(short packetNumber) {
        mPacketNumber = packetNumber;
    }

    @Override
    public short getPayloadLength() {
        return mPayloadLength;
    }

    @Override
    public void setPayloadLength(short payloadLength) {
        mPayloadLength = payloadLength;
    }

    @Override
    public short getCRC() {
        return mCRC;
    }

    @Override
    public void setCRC(short crc) {
        mCRC = crc;
    }

    @Override
    public byte[] createHeader() {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            output.write(mPreamble);
            output.write(mProtoGeneration);
            output.write(mHeaderLength);
            output.write(mInterfaceType);
            output.write(mInterfaceVersion.toBytes(2));
            output.write(mSourceDevice);
            output.write(mDestinationDevice);
            output.write(BigEndianBitConverter.getBytes(mPacketNumber));
            output.write(BigEndianBitConverter.getBytes(mPayloadLength));
            output.write(BigEndianBitConverter.getBytes(calculateCRC(output.toByteArray())));
            output.flush();
            output.close();
            byte[] srcbyte = output.toByteArray();
            byte[] dstbyte = new byte[srcbyte.length];
            System.arraycopy(srcbyte, 0, dstbyte, 0, dstbyte.length);
            return dstbyte;
        }
        catch (IOException e)
        {}
        return null;
    }

    static int counter = 0;
    private byte[] mPreamble;
    private byte mProtoGeneration;
    private byte mHeaderLength;
    private byte mInterfaceType;
    private EpocVersion mInterfaceVersion;
    private byte mSourceDevice;
    private byte mDestinationDevice;
    private short mPacketNumber;
    private short mPayloadLength;
    private short mCRC;

    public NextGenPacketHeader()
    {
        mPreamble = new byte[] { (byte)0xC0, (byte)0xDE, (byte)0xC0 };
        mProtoGeneration = 0x01;
        mHeaderLength = 16;

        mInterfaceType = InterfaceType.NextGen.value;
        mInterfaceVersion = new EpocVersion(1, 0, null, null);
        mSourceDevice = SourceDevice.EpocHost.value;
        mDestinationDevice = DestinationDevice.EpocReader.value;
        mPacketNumber = (short)counter++;
        mPayloadLength = 0;
        mCRC = 0;
    }

    private short calculateCRC(byte[] data)
    {
        //TODO: CRC hardcoded.
        try {
            return BigEndianBitConverter.toInt16(new byte[]{0x34, 0x12}, 0);
        }
        catch (IOException e)
        {}
        return 0;
    }
}
