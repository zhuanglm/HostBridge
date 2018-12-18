package com.epocal.reader.type;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.common.FixedArray;
import com.epocal.reader.enumtype.ReaderStateType;
import com.epocal.reader.enumtype.TestStatus;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/19/2017.
 */

public class DeviceStatusData extends DataFragment
{
    public ReaderStateType getReaderState() {
        return mReaderState;
    }

    public void setReaderState(ReaderStateType readerState) {
        mReaderState = readerState;
    }

    public int getEnabledInterfaces() {
        return mEnabledInterfaces;
    }

    public void setEnabledInterfaces(int enabledInterfaces) {
        mEnabledInterfaces = enabledInterfaces;
    }

    public int getEnabledSessions() {
        return mEnabledSessions;
    }

    public void setEnabledSessions(int enabledSessions) {
        mEnabledSessions = enabledSessions;
    }

    public com.epocal.reader.enumtype.TestStatus getTestStatus() {
        return mTestStatus;
    }

    public void setTestStatus(com.epocal.reader.enumtype.TestStatus testStatus) {
        mTestStatus = testStatus;
    }

    public byte getTestState() {
        return mTestState;
    }

    public void setTestState(byte testState) {
        mTestState = testState;
    }

    public FixedArray<Byte> getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(FixedArray<Byte> errorCode) {
        mErrorCode = errorCode;
    }

    public HardwareStatus getHardwareStatus() {
        return mHardwareStatus;
    }

    public void setHardwareStatus(HardwareStatus hardwareStatus) {
        mHardwareStatus = hardwareStatus;
    }

    public ReaderStatus getReaderStatus() {
        return mReaderStatus;
    }

    public void setReaderStatus(ReaderStatus readerStatus) {
        mReaderStatus = readerStatus;
    }

    private ReaderStateType mReaderState;
    private int mEnabledInterfaces;
    private int mEnabledSessions;
    private TestStatus mTestStatus;
    private byte mTestState;
    private FixedArray<Byte> mErrorCode;
    private HardwareStatus mHardwareStatus;
    private ReaderStatus mReaderStatus;

    public DeviceStatusData()
    {
        mErrorCode = new FixedArray(Byte.class, 8);
        mHardwareStatus = new HardwareStatus();
        mReaderStatus = new ReaderStatus();
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(mReaderState.value);
        output.write(BigEndianBitConverter.getBytes(mEnabledInterfaces));
        output.write(BigEndianBitConverter.getBytes(mEnabledSessions));
        output.write(mTestStatus.value);
        output.write(FixedArray.toPrimitive(mErrorCode.getData()));
        output.write(mHardwareStatus.toBytes());
        output.write(mReaderStatus.toBytes());

        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();
        return bytes;
    }

    @Override
    public void readBytes(DataStreamReader dsr) throws IOException
    {
        mReaderState = ReaderStateType.convert(dsr.readByte());
        mEnabledInterfaces = BigEndianBitConverter.toInt32(dsr, 4, 0);
        mEnabledSessions = BigEndianBitConverter.toInt32(dsr, 4, 0);
        mTestStatus = TestStatus.convert(dsr.readByte());
        mTestState = dsr.readByte();
        mErrorCode.setData(Byte.class, FixedArray.toClass(DataStreamReader.readFixedLength(dsr, mErrorCode.FixedLen)));
        mHardwareStatus.readBytes(dsr);
        mReaderStatus.readBytes(dsr);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ReaderState: " + mReaderState.toString());
        sb.append("EnabledInterfaces: " + mEnabledInterfaces);
        sb.append("EnabledSessions: " + mEnabledSessions);
        sb.append("TestStatus: " + mTestStatus.toString());
        sb.append("TestState: " + mTestState);
        sb.append("ErrorCode: " + mErrorCode.toString());
        sb.append("HardwareStatus: " + mHardwareStatus.toString());
        sb.append("mReaderStatus: " + mReaderStatus.toString());
        return sb.toString();
    }
}
