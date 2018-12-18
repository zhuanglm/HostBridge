package com.epocal.reader.type;

import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.common.FixedArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 8/16/2017.
 */

public class EQCActionResult extends DataFragment
{
    private byte mQCResult;
    private FixedArray<Byte> mQCFlag;

    public byte getQCResult() {
        return mQCResult;
    }

    public void setQCResult(byte mQCResult) {
        this.mQCResult = mQCResult;
    }

    public FixedArray<Byte> getQCFlag() {
        return mQCFlag;
    }

    public void setQCFlag(FixedArray<Byte> mQCFlag) {
        this.mQCFlag = mQCFlag;
    }

    public EQCActionResult()
    {
        mQCFlag = new FixedArray<Byte>(Byte.class, 30);
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(mQCResult);
        output.write(FixedArray.toPrimitive(mQCFlag.getData()));

        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();
        return bytes;
    }

    @Override
    public void readBytes(DataStreamReader dsr) throws IOException {
        mQCResult = dsr.readByte();
        mQCFlag.setData(Byte.class, FixedArray.toClass(DataStreamReader.readFixedLength(dsr, mQCFlag.FixedLen)));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("QCResult: " + mQCResult);
        sb.append("QCFlag: " + mQCFlag.toString());
        return sb.toString();
    }
}
