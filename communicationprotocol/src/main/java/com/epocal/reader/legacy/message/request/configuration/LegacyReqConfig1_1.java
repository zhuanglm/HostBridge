package com.epocal.reader.legacy.message.request.configuration;

import android.util.Log;

import com.epocal.common.realmentities.Reader;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.ConfigBlockFlag;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.LegacyMsgPayload;
import com.epocal.reader.type.Legacy.GeneralConfig;
import com.epocal.reader.type.MessageDescriptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by rzhuang on Aug 8 2018.
 */

public class LegacyReqConfig1_1 extends LegacyMsgPayload {

    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Configuration,
            LegacyMessageType.ConfigurationMessage1_1.value);

    public LegacyReqConfig1_1() {
        generalConfig = new GeneralConfig();
    }

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public void setBlockFlag(byte mBlockFlag) {
        this.mBlockFlag = mBlockFlag;
    }

    // which structures are present
    private byte mBlockFlag;

    public void setNumMaintenanceRecords(int numMaintenanceRecords) {
        this.mNumMaintenanceRecords = numMaintenanceRecords;
    }

    private int mNumMaintenanceRecords;
    public GeneralConfig generalConfig;

    @Override
    public int fillBuffer() {

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            output.write(mBlockFlag);

            if ((mBlockFlag & ConfigBlockFlag.General.value) != 0) {
                // put in general configuration
//                output.write(generalConfig.getReadyTimer());
//                output.write(generalConfig.getHandleTurningTimer());
//                output.write(generalConfig.getCardRemovingTimer());
                output.write((byte) 0xff);
                output.write((byte) 0xff);
                output.write((byte) 0xff);

            }

            if ((mBlockFlag & ConfigBlockFlag.Bluetooth.value) != 0) {
                // put in bluetooth configuration
                Reader reader = new Reader();
                output.write(reader.getPin().getBytes());
            }

            if ((mBlockFlag & ConfigBlockFlag.MaintenanceTestRecordNumber.value) != 0) {
                output.write((byte) mNumMaintenanceRecords);
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
