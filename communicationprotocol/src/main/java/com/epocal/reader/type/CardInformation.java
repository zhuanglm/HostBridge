package com.epocal.reader.type;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.common.FixedArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 7/4/2017.
 */

public class CardInformation extends DataFragment
{
    private byte mDryCardCheckResults;
    private byte mPrefilledDetection;
    private FixedArray<Byte> mBarcode;

    public byte getDryCardCheckResults() {
        return mDryCardCheckResults;
    }

    public void setDryCardCheckResults(byte mDryCardCheckResults) {
        this.mDryCardCheckResults = mDryCardCheckResults;
    }

    public byte getPrefilledDetection() {
        return mPrefilledDetection;
    }

    public void setPrefilledDetection(byte mPrefilledDetection) {
        this.mPrefilledDetection = mPrefilledDetection;
    }

    public FixedArray<Byte> getBarcode() {
        return mBarcode;
    }

    private void setBarcode(FixedArray<Byte> mBarcode) {
        this.mBarcode = mBarcode;
    }

    public short getBarcodeLength()
    {
        if (mBarcode == null)
            return 0;
        return (short)mBarcode.FixedLen;
    }

    public void setBarcodeLength(short len)
    {
        mBarcode = new FixedArray<Byte>(Byte.class, len);
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(mDryCardCheckResults);
        output.write(mPrefilledDetection);
        output.write(BigEndianBitConverter.getBytes(getBarcodeLength()));
        if(getBarcodeLength() > 0 && mBarcode != null)
            output.write(FixedArray.toPrimitive(mBarcode.getData()));

        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();
        return bytes;
    }

    @Override
    public void readBytes(DataStreamReader dsr) throws IOException
    {
        mDryCardCheckResults = dsr.readByte();
        mPrefilledDetection = dsr.readByte();
        setBarcodeLength(BigEndianBitConverter.toUInt16(dsr, 2, 0));

        if (getBarcodeLength() > 0)
            mBarcode.setData(Byte.class, FixedArray.toClass(DataStreamReader.readFixedLength(dsr, mBarcode.FixedLen)));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DryCardCheckResults: " + mDryCardCheckResults);
        sb.append("PrefilledDetection: " + mPrefilledDetection);
        sb.append("BarcodeLength: " + getBarcodeLength());
        sb.append("Barcode: " + mBarcode.toString());

        return sb.toString();
    }
}
