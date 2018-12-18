package com.epocal.reader.type;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.common.EpocTime;
import com.epocal.reader.common.EpocVersion;
import com.epocal.reader.common.FixedArray;
import com.epocal.reader.enumtype.TestMode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 7/17/2017.
 */

public class TestInfo extends DataFragment
{
    private int mTestId;
    private TestMode mTestMode;
    private FixedArray<Byte> mTestTime;
    private FixedArray<Byte> mHostId;
    private FixedArray<Byte> mOperatorId;
    private EpocVersion mTestConfigVersion;

    public int getTestId() {
        return mTestId;
    }

    public void setTestId(int mTestId) {
        this.mTestId = mTestId;
    }

    public TestMode getTestMode() {
        return mTestMode;
    }

    public void setTestMode(TestMode mTestMode) {
        this.mTestMode = mTestMode;
    }

    public FixedArray<Byte> getTestTime() {
        return mTestTime;
    }

    public void setTestTime(FixedArray<Byte> mTestTime) {
        this.mTestTime = mTestTime;
    }

    public FixedArray<Byte> getHostId() {
        return mHostId;
    }

    public void setHostId(FixedArray<Byte> mHostId) {
        this.mHostId = mHostId;
    }

    public FixedArray<Byte> getOperatorId() {
        return mOperatorId;
    }

    public void setOperatorId(FixedArray<Byte> mOperatorId) {
        this.mOperatorId = mOperatorId;
    }

    public EpocVersion getTestConfigVersion() {
        return mTestConfigVersion;
    }

    public void setTestConfigVersion(EpocVersion mTestConfigVersion) {
        this.mTestConfigVersion = mTestConfigVersion;
    }

    public TestInfo()
    {
        mTestId = 1;
        mTestMode = TestMode.Patient;
        mTestTime = new FixedArray<Byte>(Byte.class, 3);
        mHostId = new FixedArray<Byte>(Byte.class, 4);
        mOperatorId = new FixedArray<Byte>(Byte.class, 20);
        mTestConfigVersion = new EpocVersion(1, 0, 0);

        mTestTime.setData(Byte.class, new Byte[]{(byte)0, (byte)0, (byte)0});
        mHostId.setData(Byte.class, new Byte[]{(byte)0, (byte)0, (byte)0, (byte)0});
        mOperatorId.setData(Byte.class, new Byte[]{(byte)0, (byte)0, (byte)0, (byte)0, (byte)0
        ,(byte)0, (byte)0, (byte)0, (byte)0, (byte)0,
                (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0});
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(BigEndianBitConverter.getBytes(mTestId));
        output.write(mTestMode.value);
        output.write(FixedArray.toPrimitive(mTestTime.getData()));
        output.write(FixedArray.toPrimitive(mHostId.getData()));
        output.write(FixedArray.toPrimitive(mOperatorId.getData()));
        output.write(mTestConfigVersion.toBytes(3));

        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();
        return bytes;
    }

    @Override
    public void readBytes(DataStreamReader dsr) throws IOException {
        mTestId = BigEndianBitConverter.toInt32(dsr, 4, 0);
        mTestMode = TestMode.convert(dsr.readByte());
        mTestTime.setData(Byte.class, FixedArray.toClass(DataStreamReader.readFixedLength(dsr, mTestTime.FixedLen)));
        mHostId.setData(Byte.class, FixedArray.toClass(DataStreamReader.readFixedLength(dsr, mHostId.FixedLen)));
        mOperatorId.setData(Byte.class, FixedArray.toClass(DataStreamReader.readFixedLength(dsr, mOperatorId.FixedLen)));
        mTestConfigVersion = EpocVersion.FromBytes(DataStreamReader.readFixedLength(dsr, 3));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TestId: " + mTestId);
        sb.append("TestMode: " + mTestMode);
        sb.append("TestTime: " + mTestTime.toString());
        sb.append("HostId: " + mHostId.toString());
        sb.append("OperatorId: " + mOperatorId.toString());
        sb.append("TestConfigVersion: " + mTestConfigVersion.toString());

        return sb.toString();
    }
}
