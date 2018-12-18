package com.epocal.reader;

import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.type.MessageDescriptor;

import java.io.IOException;

/**
 * Created by dning on 6/7/2017.
 */

public abstract class MsgPayload implements IMsgPayload
{
    public abstract MessageDescriptor getDescriptor();

    public byte getMessageCode()
    {
        return mMessageCode;
    }

    public void setMessageCode(byte messagecode)
    {
        mMessageCode = messagecode;
    }

    public byte[] getRawBuffer()
    {
        return mRawbuffer;
    }

    public void setRawBuffer(byte[] rawbuffer)
    {
        if(mRawbuffer == null)
        {
            mRawbuffer = new byte[rawbuffer.length];
        }
        System.arraycopy(rawbuffer, 0, mRawbuffer, 0, Math.min(rawbuffer.length, mRawbuffer.length));
    }

    public abstract int fillBuffer();

    public abstract ParseResult parseBuffer(byte[] buffer);

    private byte mMessageCode;

    private byte[] mRawbuffer;

    public abstract String toString();

}
