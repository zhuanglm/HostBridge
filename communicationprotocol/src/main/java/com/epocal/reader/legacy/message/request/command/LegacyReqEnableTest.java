package com.epocal.reader.legacy.message.request.command;

import android.util.Log;

import com.epocal.reader.common.EpocTime;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.LegacyMsgPayload;
import com.epocal.reader.type.MessageDescriptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by rzhuang on Aug 22 2018.
 */

public class LegacyReqEnableTest extends LegacyMsgPayload {

    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Command,
            LegacyMessageType.EnableTest.value);

    public LegacyReqEnableTest() {

    }

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public void setTestId(short mTestId) {
        this.mTestId = mTestId;
    }

    private short mTestId;

    public void setTestTime(Date mTestTime) {
        this.mTestTime = mTestTime;
    }

    private Date mTestTime;

    public void setHostId(String mHostId) {
        this.mHostId = mHostId;
    }

    private String mHostId;

    public void setOperatorId(String mOperatorId) {
        this.mOperatorId = mOperatorId;
    }

    private String mOperatorId;

    public void setPatientId(String mPatientId) {
        this.mPatientId = mPatientId;
    }

    private String mPatientId;

    @Override
    public int fillBuffer() {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            output.write(toByteArray(mTestId));

            // number of minutes since sep 1st 2009
            output.write(int24tobytes(EpocTime.timePOSIXMinutes2009(mTestTime)));

            // parse the hostid (0574eb) into a uint32, but since we only use 3 bytes of it, we use int24tobytes
            output.write(int24tobytes(Long.parseLong(mHostId)));

            output.write(stringToAscii(mOperatorId).getBytes(),0,Math.min(20, mOperatorId.length()));
            output.write(stringToAscii(mPatientId).getBytes(),0,Math.min(20, mPatientId.length()));

            output.flush();
            setRawBuffer(output.toByteArray());
            output.close();
            return getRawBuffer().length;
            //return 48;
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
