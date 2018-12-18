package com.epocal.reader.type;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.common.EpocVersion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/26/2017.
 */

public class ConfigurationHeader extends DataFragment
{
    private final int mHeaderLength = 6;
    private byte mConfigCRC;
    private short mConfigLength;
    private EpocVersion mConfigVersion;
    private byte[] mData;

    public int getHeaderLength() {
        return mHeaderLength;
    }

    public byte getConfigCRC() {
        return mConfigCRC;
    }

    public void setConfigCRC(byte mConfigCRC) {
        this.mConfigCRC = mConfigCRC;
    }

    public short getConfigLength() {
        return mConfigLength;
    }

    public void setConfigLength(short mConfigLength) {
        this.mConfigLength = mConfigLength;
    }

    public EpocVersion getConfigVersion() {
        return mConfigVersion;
    }

    public void setConfigVersion(EpocVersion mConfigVersion) {
        this.mConfigVersion = mConfigVersion;
    }

    public byte[] getData() {
        return mData;
    }

    public void setData(byte[] mData) {
        this.mData = mData;
    }

    public ConfigurationHeader()
    {
        mConfigVersion = new EpocVersion(1, 0, 0, 0);
        mConfigLength = mHeaderLength;
        mConfigCRC = (byte)0x90;
    }

    private void CalculateCRC(byte[] buff)
    {
        //TODO: Undo CRC hardcoding once implemented in the Reader
        //Crc = CrcSoftware.CalculateCrcUShort(buff);
    }


    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        if (mData != null)
        {
            CalculateCRC(mData);
            mConfigLength = (short)mData.length;
        }

        output.write(mConfigCRC);
        output.write(BigEndianBitConverter.getBytes(mConfigLength));
        output.write(mConfigVersion.toBytes(3));

        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();
        return bytes;
    }

    @Override
    public void readBytes(DataStreamReader dsr) throws IOException {
        mConfigCRC = dsr.readByte();
        mConfigLength = BigEndianBitConverter.toInt16(dsr, 2, 0);
        mConfigVersion = EpocVersion.FromBytes(DataStreamReader.readFixedLength(dsr, 3));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Config CRC: " + mConfigCRC);
        sb.append("Config Length: " + mConfigLength);
        sb.append("Config Version: " + mConfigVersion.toString());
        return sb.toString();
    }
}
