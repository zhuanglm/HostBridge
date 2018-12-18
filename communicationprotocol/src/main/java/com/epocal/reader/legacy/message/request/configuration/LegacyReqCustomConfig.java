package com.epocal.reader.legacy.message.request.configuration;

import android.util.Log;

import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.CustomConfigSubcode;
import com.epocal.reader.enumtype.legacy.CustomConfigTask;
import com.epocal.reader.enumtype.legacy.CustomConfigType;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.LegacyMsgPayload;
import com.epocal.reader.type.MessageDescriptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by rzhuang on Sep 18 2018.
 */

public class LegacyReqCustomConfig extends LegacyMsgPayload {

    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Configuration,
            LegacyMessageType.ConfigurationMessage2.value);

    public LegacyReqCustomConfig() {

    }

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    private com.epocal.reader.enumtype.legacy.CustomConfigSubcode mCustomConfigSubcode;
    private com.epocal.reader.enumtype.legacy.CustomConfigTask mCustomConfigTask;
    private com.epocal.reader.enumtype.legacy.CustomConfigType mCustomConfigType;
    private byte mMessageVersion = 0;
    private byte mFormatVersion = 1;
    private short mContentVersion = 0;
    private int mPosition = 0;
    private short mBlockLength = 5;

    private String mHostID = "";
    private Calendar mLastUpdateTime;
    private int mLastUpdateTimeBySeconds;

    private int mTableLength = 0;
    private byte mTableCRC = (byte) 0;

    public byte[] mDataBlock;

    public void setCustomConfigSubcode(CustomConfigSubcode mCustomConfigSubcode) {
        this.mCustomConfigSubcode = mCustomConfigSubcode;
    }

    public void setCustomConfigTask(CustomConfigTask mCustomConfigTask) {
        this.mCustomConfigTask = mCustomConfigTask;
    }

    public void setCustomConfigType(CustomConfigType mCustomConfigType) {
        this.mCustomConfigType = mCustomConfigType;
    }

    public void setFormatVersion(byte mFormatVersion) {
        this.mFormatVersion = mFormatVersion;
    }

    public void setContentVersion(short mContentVersion) {
        this.mContentVersion = mContentVersion;
    }

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public void setHostID(String mHostID) {
        this.mHostID = mHostID;
    }

    public void setLastUpdateTimeBySeconds(int mLastUpdateTimeBySeconds) {
        this.mLastUpdateTimeBySeconds = mLastUpdateTimeBySeconds;
    }

    public void setTableLength(int mTableLength) {
        this.mTableLength = mTableLength;
    }

    public void setTableCRC(byte mTableCRC) {
        this.mTableCRC = mTableCRC;
    }

    @Override
    public int fillBuffer() {

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            output.write(mCustomConfigSubcode.value);
            output.write(mCustomConfigTask.value);
            output.write(mCustomConfigType.value);
            output.write(mMessageVersion);

            if (mCustomConfigSubcode == mCustomConfigSubcode.eCustomConfigGet) {
                if (mCustomConfigType == CustomConfigType.Descriptor) {
                    //return 4;
                } else if (mCustomConfigType == CustomConfigType.Block) {
                    output.write(toByteArray(mPosition));
                    output.write(toByteArray(mBlockLength));
                    //return 10;
                }

                output.flush();
                setRawBuffer(output.toByteArray());
                output.close();
                return getRawBuffer().length;

            } else if (mCustomConfigSubcode == CustomConfigSubcode.eCustomConfigSet) {
                if (mCustomConfigType == CustomConfigType.Block) {
                    output.write(mFormatVersion);
                    output.write(toByteArray(mContentVersion));

                    output.write(toByteArray(mPosition));
                    output.write(toByteArray(mBlockLength));

                    byte[] hostid = mHostID.getBytes();
                    for (int i = 0; i < 20; i++) {
                        if (i < hostid.length) {
                            output.write(hostid[i]);
                        } else {
                            output.write((byte) 0);
                        }
                    }

                    output.write(toByteArray(mLastUpdateTimeBySeconds));
                    output.write(toByteArray(mTableLength));
                    output.write(mTableCRC);

                    if (mDataBlock != null) {
                        output.write(mDataBlock);
                    }

                    byte crc = (byte) 0xaa;

                    for (int i = 0; i < mDataBlock.length; i++)
                        crc ^= mDataBlock[i];

                    output.write(crc);

                    output.flush();
                    setRawBuffer(output.toByteArray());
                    output.close();
                    return getRawBuffer().length;
                }
            }


            output.flush();
            setRawBuffer(output.toByteArray());
            output.close();
            return getRawBuffer().length;
        } catch (IOException e) {
            Log.e(LegacyMessageType.convert(getDescriptor().getType()).name(), e.getLocalizedMessage());
        }
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer) {
        return ParseResult.InvalidCall;
    }

    @Override
    public String toString() {
        return null;
    }


}
