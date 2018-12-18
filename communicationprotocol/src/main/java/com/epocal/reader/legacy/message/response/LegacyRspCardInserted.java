package com.epocal.reader.legacy.message.response;

import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.LegacyMsgPayload;
import com.epocal.reader.type.MessageDescriptor;

public class LegacyRspCardInserted extends LegacyMsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Command,
            LegacyMessageType.CardInserted.value);


    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public byte getBarcodeLength() {
        return mBarcodeLength;
    }

    public byte[] getBarcode() {
        return mBarcode;
    }

    public byte[] getmDryCheck() {
        return mDryCheck;
    }

    private byte mBarcodeLength;
    private byte[] mBarcode;
    private byte[] mDryCheck;
    private byte[] mRawBarcode;

    public LegacyRspCardInserted() {
        byte maxBarcodeLength = 16;
        mBarcode = new byte[maxBarcodeLength];
        mRawBarcode = null;
    }

    @Override
    public int fillBuffer() {
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer) {
        byte dryCardCheckLength = 1;
        mBarcodeLength = buffer[0];
        mDryCheck = new byte[dryCardCheckLength];
        int count = buffer.length;

        int i;

        for (i = 0; i < mBarcodeLength; i++) {
            mBarcode[i] = buffer[i + 1];
        }

        for (i = 0; i < dryCardCheckLength; i++) {
            mDryCheck[i] = buffer[mBarcodeLength + 1 + i];
        }

        // see how many bytes are left in the message
        int numBytes = (count - (mBarcodeLength + 1 + dryCardCheckLength));

        // if there was any room left
        if (numBytes > 0) {
            // create an array for the symbols
            mRawBarcode = new byte[numBytes];

            // for each byte
            for (i = 0; i < numBytes; i++) {
                mRawBarcode[i] = buffer[mBarcodeLength + 1 + dryCardCheckLength + i];
            }
        }

        return ParseResult.Success;
    }

    @Override
    public String toString() {
        return null;
    }
}
