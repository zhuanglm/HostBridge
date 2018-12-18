package com.epocal.reader.type;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.common.EpocVersion;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dning on 6/30/2017.
 */

public class ChannelDataPairCollection {

    private List<ChannelDataPair> mPairList;

    public List<ChannelDataPair> getPairList() {
        return mPairList;
    }

    public void setPairList(List<ChannelDataPair> mPairList) {
        this.mPairList = mPairList;
    }

    public ChannelDataPairCollection()
    {
        mPairList = new ArrayList<ChannelDataPair>();
    }

    public void readBytes(DataStreamReader dsr, int pairLength) throws IOException
    {
        if (pairLength <= 0)
            return;
        mPairList = new ArrayList<ChannelDataPair>(pairLength);
        for (int i = 0; i < pairLength; i++ ) {
            ChannelDataPair cdp = new ChannelDataPair();
            cdp.setChannelType(dsr.readByte());
            cdp.setValue(BigEndianBitConverter.toSingle(dsr, 4, 0));
            mPairList.add(cdp);
        }
    }

    public int getCount()
    {
        if(mPairList != null)
        {
            return mPairList.size();
        }
        return 0;
    }

    public float getValuebyIndex(int index)
    {
        if(mPairList != null)
        {
            return mPairList.get(index).getValue();
        }
        return Float.MIN_VALUE;
    }

    public byte[] toBytes() throws IOException
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        for(ChannelDataPair pair : mPairList)
        {
            output.write(pair.getChannelType());
            output.write(BigEndianBitConverter.getBytes(pair.getValue()));
        }
        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();
        return bytes;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for(ChannelDataPair pair : mPairList)
        {
            sb.append(String.format("DataPair{{ Channel Type: %s, Value: %s }}, ",pair.getChannelType(), pair.getValue()));
        }
        return sb.toString();
    }
}
