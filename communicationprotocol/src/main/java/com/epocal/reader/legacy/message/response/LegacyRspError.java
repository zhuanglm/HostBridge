package com.epocal.reader.legacy.message.response;

import android.util.Log;

import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.LegacyMsgPayload;
import com.epocal.reader.type.MessageDescriptor;

/**
 * Created by rzhuang on Aug 3 2018.
 */

public class LegacyRspError extends LegacyMsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Command,
            LegacyMessageType.Error.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public enum ErrorType
    {
        InvalidRequest ((byte)0),
        Config1_1Declined ((byte)1),
        Config1_2Declined ((byte)2),
        Config1_3Declined ((byte)3),
        Config1_4Declined ((byte)4),
        InvalidTestId ((byte)5),
        Config2Declined ((byte)6),
        NoTestinReader ((byte)7),
        PrepareforFirmwareFailed ((byte)8),
        FirmwareUpgradeFailed ((byte)9),
        BatteryNearLife ((byte)10),
        HardwareFailure ((byte)11),
        InsertionSwitchBounce ((byte)12),
        DetectionSwitchBounce ((byte)13),
        UnableToProcessRequest ((byte)14),
        SelfCheckInProgress ((byte)15),
        MotorFailure ((byte)16),
        CardInsertedWhileMotorMoving ((byte)17),
        Undefined((byte)255);

        public final byte value;
        ErrorType(byte value) {
            this.value = value;
        }

        public static ErrorType convert(byte val) {
            return ErrorType.values()[val];
        }
    }

    public LegacyRspError() {

    }

    private ErrorType mErrorcode;

    public ErrorType getErrorCode() {
        return mErrorcode;
    }

    @Override
    public int fillBuffer() {
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer) {

        // only 1 parameter
        if (buffer.length > 1)
        {
            return ParseResult.BufferLengthIncorrect;
        }

        int err = buffer[0] & 0x0ff;
        if ( err >= 0 && err < 18)
            mErrorcode = ErrorType.convert((byte)err);
        else {
            mErrorcode = ErrorType.Undefined;
        }

        return ParseResult.Success;
    }

    @Override
    public String toString() {
        return null;
    }
}
