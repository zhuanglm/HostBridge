package com.epocal.reader.type;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.enumtype.ClockSourceType;
import com.epocal.reader.enumtype.GainType;
import com.epocal.reader.enumtype.ModeSelectType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/26/2017.
 */

public class DAMADC extends DataFragment
{
    private ModeSelectType mModeSelect;
    private ClockSourceType mClockSource;
    private byte mChannel;
    private int mBitFlags; //BitFlagsType
    private GainType mGain;
    private int mFilter;
    private int mSettlingTime;

    public ModeSelectType getModeSelect() {
        return mModeSelect;
    }

    public void setModeSelect(ModeSelectType mModeSelect) {
        this.mModeSelect = mModeSelect;
    }

    public ClockSourceType getClockSource() {
        return mClockSource;
    }

    public void setClockSource(ClockSourceType mClockSource) {
        this.mClockSource = mClockSource;
    }

    public byte getChannel() {
        return mChannel;
    }

    public void setChannel(byte mChannel) {
        this.mChannel = mChannel;
    }

    public int getBitFlags() {
        return mBitFlags;
    }

    public void setBitFlags(int mBitFlags) {
        this.mBitFlags = mBitFlags;
    }

    public GainType getGain() {
        return mGain;
    }

    public void setGain(GainType mGain) {
        this.mGain = mGain;
    }

    public int getFilter() {
        return mFilter;
    }

    public void setFilter(int mFilter) {
        this.mFilter = mFilter;
    }

    public int getSettlingTime() {
        return mSettlingTime;
    }

    public void setSettlingTime(int mSettlingTime) {
        this.mSettlingTime = mSettlingTime;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(mModeSelect.value);
        output.write(mClockSource.value);
        output.write(mChannel);
        output.write(BigEndianBitConverter.getBytes(mBitFlags));
        output.write(mGain.value);
        output.write(BigEndianBitConverter.getBytes(mFilter));
        output.write(BigEndianBitConverter.getBytes(mSettlingTime));

        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();
        return bytes;
    }

    @Override
    public void readBytes(DataStreamReader dsr) throws IOException {
        mModeSelect = ModeSelectType.convert(dsr.readByte());
        mClockSource = ClockSourceType.convert(dsr.readByte());
        mChannel = dsr.readByte();
        mBitFlags = BigEndianBitConverter.toInt32(dsr, 4, 0);
        mGain = GainType.convert(dsr.readByte());
        mFilter = BigEndianBitConverter.toInt32(dsr, 4, 0);
        mSettlingTime = BigEndianBitConverter.toInt32(dsr, 4, 0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ModeSelect: " + mModeSelect.toString());
        sb.append("ClockSource: " + mClockSource.toString());
        sb.append("Channel: " + mChannel);
        sb.append("BitFlags: " + mBitFlags);
        sb.append("Gain: " + mGain.toString());
        sb.append("Filter: " + mFilter);
        sb.append("SettlingTime: " + mSettlingTime);
        return sb.toString();
    }
}
