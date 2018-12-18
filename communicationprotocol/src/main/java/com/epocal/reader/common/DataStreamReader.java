package com.epocal.reader.common;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by dning on 6/8/2017.
 */

public class DataStreamReader
{
    public static byte[] readFixedLength(DataStreamReader dsr, int fixedlength) throws IOException
    {
        if(fixedlength == 0){
            return null;
        }
        byte[] data = dsr.readBytes(fixedlength);
        return data;
    }

    public static byte[] readRemainingData(DataStreamReader dsr) throws IOException
    {
        int len = dsr.available();
        byte[] data = dsr.readBytes(len);
        return data;
    }

    private byte[] mRawData;
    private int mPosition;

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public byte[] getRawData() {
        return mRawData;
    }

    public void setRawData(byte[] mRawData) {
        if(mRawData == null) {
            this.mRawData = new byte[0];
            return;
        }
        if( this.mRawData == null )
        {
            this.mRawData = new byte[mRawData.length];
        }
        for(int i = 0 ; i < mRawData.length; i++ )
        {
            this.mRawData[i] = mRawData[i];
        }
    }

    public DataStreamReader(byte[] buffer)
    {
        setRawData(buffer);
        mPosition = 0;
    }

    public void reset()
    {
        mPosition = 0;
    }

    public byte readByte() throws IOException
    {
        if (mPosition >= mRawData.length)
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        byte bData = mRawData[mPosition];
        mPosition++;
        return bData;
    }

    public byte[] readBytes(int length) throws IOException
    {
        if ((length<= 0) || (mPosition + length > mRawData.length) )
        {
            throw new ArrayIndexOutOfBoundsException();
        }
        byte[] data = new byte[length];
        System.arraycopy(mRawData, mPosition, data, 0, length);
        mPosition += length;
        return data;
    }

    public int available()
    {
        return mRawData.length - mPosition;
    }

    public void dispose()
    {

    }
}
