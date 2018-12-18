package com.epocal.reader.type;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/27/2017.
 */

public class BDThresholds extends DataFragment
{
    private float mBD_AIR_THRESHOLD;
    private float mBD_FLUID_THRESHOLD;

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(BigEndianBitConverter.getBytes(mBD_AIR_THRESHOLD));
        output.write(BigEndianBitConverter.getBytes(mBD_FLUID_THRESHOLD));

        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();
        return bytes;
    }

    @Override
    public void readBytes(DataStreamReader dsr) throws IOException {
        mBD_AIR_THRESHOLD = BigEndianBitConverter.toSingle(dsr, 4, 0);
        mBD_FLUID_THRESHOLD = BigEndianBitConverter.toSingle(dsr, 4, 0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BD_AIR_THRESHOLD: " + mBD_AIR_THRESHOLD);
        sb.append("BD_FLUID_THRESHOLD: " + mBD_FLUID_THRESHOLD);
        return sb.toString();
    }
}
