package com.epocal.reader.type;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;;
import com.epocal.reader.enumtype.FluidicsMotorState;
import com.epocal.reader.enumtype.MotorID;
import com.epocal.reader.enumtype.MotorStatus;
import com.epocal.reader.enumtype.SensorContactsMotorState;
import com.epocal.reader.enumtype.SwitchLevel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 8/15/2017.
 */

public class DebugMotorEvents extends DataFragment
{
    private MotorID mMotorID;
    private MotorStatus mMotorStatus;
    private SensorContactsMotorState mSensorContactsMotorState;
    private FluidicsMotorState mFluidicsMotorState;
    private SwitchLevel mHomeSwitchLevel;
    private int mTimeStamp;
    private int mMoveCounts;
    private int mTotalCounts;

    public MotorID getMotorID() {
        return mMotorID;
    }

    public void setMotorID(MotorID mMotorID) {
        this.mMotorID = mMotorID;
    }

    public MotorStatus getMotorStatus() {
        return mMotorStatus;
    }

    public void setMotorStatus(MotorStatus mMotorStatus) {
        this.mMotorStatus = mMotorStatus;
    }

    public SensorContactsMotorState getSensorContactsMotorState() {
        return mSensorContactsMotorState;
    }

    public void setSensorContactsMotorState(SensorContactsMotorState mSensorContactsMotorState) {
        this.mSensorContactsMotorState = mSensorContactsMotorState;
    }

    public FluidicsMotorState getFluidicsMotorState() {
        return mFluidicsMotorState;
    }

    public void setFluidicsMotorState(FluidicsMotorState mFluidicsMotorState) {
        this.mFluidicsMotorState = mFluidicsMotorState;
    }

    public SwitchLevel getHomeSwitchLevel() {
        return mHomeSwitchLevel;
    }

    public void setHomeSwitchLevel(SwitchLevel mHomeSwitchLevel) {
        this.mHomeSwitchLevel = mHomeSwitchLevel;
    }

    public int getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(int mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }

    public int getMoveCounts() {
        return mMoveCounts;
    }

    public void setMoveCounts(int mMoveCounts) {
        this.mMoveCounts = mMoveCounts;
    }

    public int getTotalCounts() {
        return mTotalCounts;
    }

    public void setTotalCounts(int mTotalCounts) {
        this.mTotalCounts = mTotalCounts;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(mMotorID.value);
        output.write(mMotorStatus.value);

        if(mMotorID == MotorID.SensorContacts) {
            output.write(mSensorContactsMotorState.value);
        }
        else
        {
            output.write(mFluidicsMotorState.value);
        }
        output.write(mHomeSwitchLevel.value);

        output.write(BigEndianBitConverter.getBytes(mTimeStamp));
        output.write(BigEndianBitConverter.getBytes(mMoveCounts));
        output.write(BigEndianBitConverter.getBytes(mTotalCounts));

        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();
        return bytes;
    }

    @Override
    public void readBytes(DataStreamReader dsr) throws IOException {
        mMotorID = MotorID.convert(dsr.readByte());
        mMotorStatus = MotorStatus.convert(dsr.readByte());
        if(mMotorID == MotorID.SensorContacts) {
            mSensorContactsMotorState = SensorContactsMotorState.convert(dsr.readByte());
        }
        else
        {
            mFluidicsMotorState = FluidicsMotorState.convert(dsr.readByte());
        }
        mHomeSwitchLevel = SwitchLevel.convert(dsr.readByte());

        mTimeStamp = BigEndianBitConverter.toInt32(dsr, 4, 0);
        mMoveCounts = BigEndianBitConverter.toInt32(dsr, 4, 0);
        mTotalCounts = BigEndianBitConverter.toInt32(dsr, 4, 0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MotorID: " + mMotorID.toString());
        sb.append("MotorStatus: " + mMotorStatus.toString());
        if(mMotorID == MotorID.SensorContacts) {
            sb.append("SensorContactsMotorState: " + mSensorContactsMotorState.toString());
        }
        else
        {
            sb.append("FluidicsMotorState: " + mFluidicsMotorState.toString());
        }
        sb.append("HomeSwitchLevel: " + mHomeSwitchLevel.toString());

        sb.append("TimeStamp: " + mTimeStamp);
        sb.append("MoveCounts: " + mMoveCounts);
        sb.append("TotalCounts: " + mTotalCounts);
        return sb.toString();
    }
}
