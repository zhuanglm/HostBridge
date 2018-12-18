package com.epocal.reader.legacy.message.request.command;

import android.util.Log;

import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.LegacyMsgPayload;
import com.epocal.reader.type.MessageDescriptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by rzhuang on July 30 2018.
 */

public class LegacyReqStatistics extends LegacyMsgPayload {

    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Command,
            LegacyMessageType.GetStatistics.value);

    public LegacyReqStatistics() {
        mStatisticsControl = StatisticsControl.send;
    }

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public enum StatisticsType {
        general((byte) 0),
        performance((byte) 1),
        maintenance((byte) 2);

        public final byte value;

        StatisticsType(byte value) {
            this.value = value;
        }

        public static StatisticsType convert(byte value) {
            return StatisticsType.values()[value];
        }
    }

    public enum StatisticsControl {
        send((byte) 0),
        sendAndReset((byte) 1);

        public final byte value;

        StatisticsControl(byte value) {
            this.value = value;
        }
    }

    public void setStatisticsFlag(StatisticsType mStatisticsFlag) {
        this.mStatisticsFlag = mStatisticsFlag;
    }

    private StatisticsType mStatisticsFlag;
    private StatisticsControl mStatisticsControl;

    @Override
    public int fillBuffer() {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            output.write(mStatisticsFlag.value);
            output.write(mStatisticsControl.value);

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
