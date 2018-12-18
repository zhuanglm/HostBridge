package com.epocal.reader.legacy.message.response;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.type.MessageDescriptor;

import java.text.NumberFormat;
import java.util.Arrays;

/**
 * Created by rzhuang on July 30 2018.
 */

public class ArmDebugInfo extends MsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Debug,
            LegacyMessageType.ArmDebugInfo.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public ArmDebugInfo() {
    }

//    public enum ArmDebugCode {
//        PSN ((byte)0x01),
//        MotorEvents ((byte)0x02),
//        MotorSpeed ((byte)0x03),
//        CardInsertion ((byte)0x04),
//        HandleSwitchDetected ((byte)0x05),
//        HeaterDetectionEvent ((byte)0x80);
//
//        public final byte value;
//        ArmDebugCode(byte value) {
//            this.value = value;
//        }
//        //public static ArmDebugCode convert(byte value) {return ArmDebugCode.values()[value];
//
//    }

    private byte mDebugCode;
    private byte[] mDebuggingInfo;


    @Override
    public int fillBuffer() {
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer) {

        // handle however many bytes are incoming.. code is first
        mDebugCode = buffer[0];

        // 1 byte less... for the code
        mDebuggingInfo = Arrays.copyOf(buffer, buffer.length);

        return ParseResult.Success;
    }


    @Override
    public String toString() {
        StringBuilder tempString = new StringBuilder("AD: " + mDebugCode);
        NumberFormat numFormat = NumberFormat.getNumberInstance();

        for (byte di : mDebuggingInfo) {
            tempString.append(" ").append(numFormat.format(di));
        }

        return tempString.toString();
    }

}