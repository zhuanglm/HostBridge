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
 * Created by rzhuang on July 31 2018.
 */

public class LegacyReqEnableReader extends LegacyMsgPayload {

    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Command,
            LegacyMessageType.EnableReader.value);

    public LegacyReqEnableReader() {

    }

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public enum ReaderHardwareEnabledFlag {
        enabled ((byte)0),
        disabled ((byte)1);

        private final byte value;
        ReaderHardwareEnabledFlag(byte value) {
            this.value = value;
        }
    }
    public enum ReaderMaintenanceFlag {
        enabled ((byte)0) ,
        lockedByHost ((byte) 1),
        lockedByReader ((byte) 2),
        lockedByBoth ((byte) 3);

        private final byte value;
        ReaderMaintenanceFlag(byte value) {
            this.value = value;
        }
    }

    public void setReaderHardwareDisabled(ReaderHardwareEnabledFlag mReaderHardwareDisabled) {
        this.mReaderHardwareDisabled = mReaderHardwareDisabled;
    }

    private ReaderHardwareEnabledFlag mReaderHardwareDisabled;

    public void setReaderMaintenanceDisabled(ReaderMaintenanceFlag mReaderMaintenanceDisabled) {
        this.mReaderMaintenanceDisabled = mReaderMaintenanceDisabled;
    }

    private ReaderMaintenanceFlag mReaderMaintenanceDisabled;

    @Override
    public int fillBuffer() {
        // at count put the self check in
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            output.write(mReaderHardwareDisabled.value);
            output.write(mReaderMaintenanceDisabled.value);

            output.flush();
            setRawBuffer(output.toByteArray());
            output.close();
            return getRawBuffer().length;
        }
        catch (IOException e) {
            Log.e(LegacyMessageType.convert(getDescriptor().getType()).name(),e.getLocalizedMessage());
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
