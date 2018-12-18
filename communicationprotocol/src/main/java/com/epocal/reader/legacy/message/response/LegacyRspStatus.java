package com.epocal.reader.legacy.message.response;

import com.epocal.reader.enumtype.AccessType;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MaintenanceFlagType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.enumtype.legacy.ReaderMode;
import com.epocal.reader.legacy.LegacyMsgPayload;
import com.epocal.reader.type.HardwareStatus;
import com.epocal.reader.type.MessageDescriptor;
import com.epocal.reader.type.ReaderStatus;

import java.text.NumberFormat;

/**
 * Created by rzhuang on July 31 2018.
 */

public class LegacyRspStatus extends LegacyMsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Command,
            LegacyMessageType.ReaderStatusResponse.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    private byte mErrorCode;
    private ReaderMode mMode;
    private ReaderStatus mReaderStatus;
    //private boolean mHCMError;

    public HardwareStatus hwStatus;
    public static String InterfaceVersion = "2.11";

    public LegacyRspStatus() {
        hwStatus = new HardwareStatus();
        mReaderStatus = new ReaderStatus();
    }

    public byte getErrorCode() {
        return mErrorCode;
    }

    @Override
    public int fillBuffer() {
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer) {
        int count = buffer.length;
        if (count < 28) {
            return ParseResult.BufferLengthIncorrect;
        }

        mMode = ReaderMode.convert(buffer[0]);
        mErrorCode = buffer[1];
        hwStatus.setBatteryLevel(getFloat(buffer, 2));
        hwStatus.setAmbientTemp1(getFloat(buffer, 6));
        hwStatus.setBGETopHeaterTemp(getRevFloat(buffer, 10));
        hwStatus.setBGEBottomHeaterTemp(getRevFloat(buffer, 14));
        hwStatus.setBarometricPressureSensor1(getFloat(buffer, 18));
        hwStatus.setDamCPUTemperature(getFloat(buffer, 22));
        hwStatus.setInterfaces(buffer[26]);
        byte[] scr = new byte[1];
        scr[0] = buffer[27];
        hwStatus.setSelfCheckResults(scr);
        hwStatus.setBarometricPressureSensor2(getFloat(buffer, 28));

        hwStatus.setBarometricSensor();

        if (count >= 34) {
            mReaderStatus.mAccess = AccessType.convert(buffer[32]);
            mReaderStatus.mMaintenanceFlag = MaintenanceFlagType.convert(buffer[33]);
        }

        return ParseResult.Success;
    }

    public boolean isMotorNotReset() {

        if (InterfaceVersion.equals("2.11")) {
            return ((mErrorCode & ErrorCode120.MotorNotReset.value) != 0);
        } else {
            return ((mErrorCode & ErrorCode121.MotorNotReset.value) != 0);
        }

    }

    public boolean isMotorInUnknownPosition() {

        if (InterfaceVersion.equals("2.11")) {
            return ((mErrorCode & ErrorCode120.MotorInUnknownPosition.value) != 0);
        } else {
            return ((mErrorCode & ErrorCode121.MotorInUnknownPosition.value) != 0);
        }

    }

    public boolean isSelfCheckPassed() {
        if (InterfaceVersion.equals("2.11")) {
            for (byte b : this.hwStatus.getSelfCheckResults())
                if (b != (byte) 0xff)
                    return false;
        }

        return (this.mErrorCode == 0);

    }

    public boolean isHCMError() {
        if (InterfaceVersion.equals("2.11")) {
            return ((mErrorCode & ErrorCode120.HCM.value) != 0);
        } else {
            return ((mErrorCode & ErrorCode121.HCM.value) != 0);
        }
    }

    public enum ErrorCode120 {
        OldTestResultsPresent((byte) 0x1),
        CardInReader((byte) 0x2),
        HCM((byte) 0x4),
        DAMBootupError((byte) 0x8),
        MotorInUnknownPosition((byte) 0x10),
        ExternalFlashAccessError((byte) 0x20),
        MotorNotReset((byte) 0x40),
        SIBFailure((byte) 0x80);

        public final byte value;

        ErrorCode120(byte val) {
            this.value = val;
        }
    }

    private enum ErrorCode121 {
        SIBFailure((byte) 0x1),
        CardInReader((byte) 0x2),
        HCM((byte) 0x4),
        DAMBootupError((byte) 0x8),
        MotorInUnknownPosition((byte) 0x10),
        ExternalFlashAccessError((byte) 0x20),
        MotorNotReset((byte) 0x40),
        Incompatibility((byte) 0x80);

        public final byte value;

        ErrorCode121(byte val) {
            this.value = val;
        }
    }

    public void AdjustErrorCode() {
        if (InterfaceVersion.equals("2.11")) {
            // this should fix the problem of the self test failing but looking like it passed
            // rsrmsg.errorcode is readonly
            if (hwStatus.getSelfCheckResults()[0] == 0) {
                mErrorCode |= ErrorCode120.SIBFailure.value;
            }
        }
    }

    public boolean isCardInReader() {
        if (InterfaceVersion.equals("2.11")) {
            return ((mErrorCode & ErrorCode120.CardInReader.value) != 0);
        } else {
            return ((mErrorCode & ErrorCode121.CardInReader.value) != 0);
        }
    }

    @Override
    public String toString() {
        NumberFormat numFormat = NumberFormat.getNumberInstance();

        return "ReaderStatusResponse: err:" + numFormat.format(mErrorCode) +
                " temp: " + numFormat.format(hwStatus.getAmbientTemp1()) +
                " press: " + numFormat.format(hwStatus.getBarometricPressureSensor()) +
                " press1: " + numFormat.format(hwStatus.getBarometricPressureSensor1())
                + " press2: " + numFormat.format(hwStatus.getBarometricPressureSensor2()) +
                " batt: " + numFormat.format(hwStatus.getBatteryLevel())
                + numFormat.format(hwStatus.getDamCPUTemperature()) +
                " interfaces: " + hwStatus.getInterfaces() +
                " selfcheck0: " + hwStatus.getSelfCheckResults()[0]
                + numFormat.format(hwStatus.getBGETopHeaterTemp()) +
                " bottomheat: " + numFormat.format(hwStatus.getBGEBottomHeaterTemp()) +
                " readerstatus: " + mReaderStatus.toString();
    }

}
