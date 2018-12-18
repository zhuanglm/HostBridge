package com.epocal.reader.type;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.enumtype.BDMode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by dning on 6/26/2017.
 */

public class BDConfiguration extends DataFragment
{
    private com.epocal.reader.enumtype.BDMode mBDMode;
    private int mBDFrequency; //3 bytes!

    public BDMode getBDMode() {
        return mBDMode;
    }

    public void setBDMode(BDMode mBDMode) {
        this.mBDMode = mBDMode;
    }

    public int getBDFrequency() {
        return mBDFrequency;
    }

    public void setBDFrequency(int mBDFrequency) {
        this.mBDFrequency = mBDFrequency;
    }

    public BDConfiguration() {}

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(mBDMode.value);
        output.write(Arrays.copyOfRange(BigEndianBitConverter.getBytes(mBDFrequency),0, 3));

        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();
        return bytes;
    }

    @Override
    public void readBytes(DataStreamReader dsr) throws IOException
    {
        mBDMode = BDMode.convert(dsr.readByte());
        mBDFrequency = BigEndianBitConverter.toInt32(dsr, 3, 0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BDMode: " + mBDMode.toString());
        sb.append("BDFrequency: " + mBDFrequency);
        return sb.toString();
    }
}
