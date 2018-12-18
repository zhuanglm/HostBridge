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

import static com.epocal.reader.common.EpocTime.timePOSIXMilliseconds2009;

/**
 * Created by rzhuang on July/19/2018.
 */

public class LegacyReqDeviceId extends LegacyMsgPayload {
    private String mDeviceSN;
    private byte mMajorVer;
    private byte mMinorVer;
    private byte mRevisionVer;
    private byte mMajorInterfaceVer;
    private byte mMinorInterfaceVer;
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Command,
            LegacyMessageType.GetDeviceId.value);

    public LegacyReqDeviceId(String deviceSerialNumber, byte majorVersion, byte minorVersion,
                             byte revisionVersion, byte majorInterfaceVer, byte minorInterfaceVer) {
        mDeviceSN = deviceSerialNumber;
        mMajorVer = majorVersion;
        mMinorVer = minorVersion;
        mRevisionVer = revisionVersion;
        mMajorInterfaceVer = majorInterfaceVer;
        mMinorInterfaceVer = minorInterfaceVer;

    }

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    @Override
    public int fillBuffer() {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            // host type 1 bytes
            output.write(0);
            // host id 3 bytes
            output.write(int24tobytes((long) Double.parseDouble(mDeviceSN)));    //some string has "."

            output.write(mMajorVer);
            output.write(mMinorVer);
            output.write(mRevisionVer);

            output.write(mMajorInterfaceVer);
            output.write(mMinorInterfaceVer);

            // time. 3 bytes
            // number of minutes since sep 1st 2009
            output.write(int24tobytes(timePOSIXMilliseconds2009()));

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
        return null;
    }

    @Override
    public String toString() {
        return null;
    }
}
