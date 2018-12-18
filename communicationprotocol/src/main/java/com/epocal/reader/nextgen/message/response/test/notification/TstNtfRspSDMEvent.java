package com.epocal.reader.nextgen.message.response.test.notification;

import com.epocal.reader.MsgPayload;
import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.SDMStateType;
import com.epocal.reader.enumtype.TestNotificationResponse;
import com.epocal.reader.type.MessageDescriptor;
import com.epocal.util.UnsignedType;

import java.io.IOException;

/**
 * Created by dning on 8/15/2017.
 */

public class TstNtfRspSDMEvent extends MsgPayload {
    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.NextGen, MessageClass.Test, MessageGroup.Notification,
            TestNotificationResponse.SDMEvent.value);

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public enum MessageCodeType {
        NotDefined((byte) 0),

        ValvesOff((byte) 1),
        PrepareTankForBGE((byte) 2),
        TankChargeComplete((byte) 3),
        TankVentComplete((byte) 4),
        CalFluidDeliveryStart((byte) 5),
        CalFluidDeliveryEnd((byte) 6),
        OpticalGuardEvent((byte) 7),
        BgeSampleDeliveryStart((byte) 8),
        BgeSampleDeliveryEnd((byte) 9),
        PrepareTankForCoox((byte) 10),
        CooxSampleDeliveryStart((byte) 11),         // UNUSED BY BRIDGE (NextGen only)
        CooxCapacitorCycle((byte) 12),              // UNUSED BY BRIDGE (NextGen only)
        CooxVentCycle((byte) 13),                   // UNUSED BY BRIDGE (NextGen only)
        CooxSampleParked((byte) 14),                // UNUSED BY BRIDGE (NextGen only)
        CooxSampleDeliveryResumed((byte) 15),       // UNUSED BY BRIDGE (NextGen only)
        CooxSampleAtCuvette((byte) 16),             // UNUSED BY BRIDGE (NextGen only)
        CooxSampleDeliveryEnd((byte) 17);           // UNUSED BY BRIDGE (NextGen only)

        public final byte value;

        MessageCodeType(byte value) {
            this.value = Byte.valueOf(value);
        }

        public static MessageCodeType convert(byte value) {
            return MessageCodeType.values()[value];
        }
    }

    public MessageCodeType mResponse;

    public MessageCodeType getResponse() {
        return MessageCodeType.convert(getMessageCode());
    }

    private byte mResponseDetails;

    public byte getResponseDetails() {
        return mResponseDetails;
    }

    public void setResponseDetails(byte mResponseDetails) {
        this.mResponseDetails = mResponseDetails;
    }

    private UnsignedType.UInt32 mTimeStamp;
    private float mDistanceIntegral;
    private float mPressureDecaySlope;
    private SDMStateType mSDMStateType;
    private boolean mPumpOn;
    private boolean mValve1On;
    private boolean mValve2On;
    private boolean mValve3On;
    private boolean mValve4On;
    private boolean mValve5On;
    private boolean mValve6On;

    public UnsignedType.UInt32 getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(UnsignedType.UInt32 mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }

    public SDMStateType getSDMStateType() {
        return mSDMStateType;
    }

    public void setSDMStateType(SDMStateType mSDMStateType) {
        this.mSDMStateType = mSDMStateType;
    }

    public float getDistanceIntegral() {
        return mDistanceIntegral;
    }

    public void setDistanceIntegral(float distanceIntegral) {
        mDistanceIntegral = distanceIntegral;
    }

    public float getPressureDecaySlope() {
        return mPressureDecaySlope;
    }

    public void setPressureDecaySlope(float mPressureDecaySlope) {
        this.mPressureDecaySlope = mPressureDecaySlope;
    }

    public boolean isPumpOn() {
        return mPumpOn;
    }

    public void setPumpOn(boolean pumpOn) {
        mPumpOn = pumpOn;
    }

    public boolean isValve1On() {
        return mValve1On;
    }

    public void setValve1On(boolean valve1On) {
        mValve1On = valve1On;
    }

    public boolean isValve2On() {
        return mValve2On;
    }

    public void setValve2On(boolean valve2On) {
        mValve2On = valve2On;
    }

    public boolean isValve3On() {
        return mValve3On;
    }

    public void setValve3On(boolean valve3On) {
        mValve3On = valve3On;
    }

    public boolean isValve4On() {
        return mValve4On;
    }

    public void setValve4On(boolean valve4On) {
        mValve4On = valve4On;
    }

    public boolean isValve5On() {
        return mValve5On;
    }

    public void setValve5On(boolean valve5On) {
        mValve5On = valve5On;
    }

    public boolean isValve6On() {
        return mValve6On;
    }

    public void setValve6On(boolean valve6On) {
        mValve6On = valve6On;
    }

    public TstNtfRspSDMEvent() {
        setMessageCode(MessageCodeType.NotDefined.value);
        mTimeStamp = new UnsignedType.UInt32();
    }

    @Override
    public int fillBuffer() {
        return 0;
    }

    @Override
    public ParseResult parseBuffer(byte[] buffer) {
        setRawBuffer(buffer);

        DataStreamReader dsr = new DataStreamReader(getRawBuffer());
        ParseResult parseResult = ParseResult.Success;

        try {
            getDescriptor().readBytes(dsr);
            setMessageCode(dsr.readByte());
            mTimeStamp.setValue(BigEndianBitConverter.toInt32(dsr, 4, 0));
            mDistanceIntegral = BigEndianBitConverter.toSingle(dsr, 4, 0);
            mPressureDecaySlope = BigEndianBitConverter.toSingle(dsr, 4, 0);
            mSDMStateType = SDMStateType.convert(dsr.readByte());
            mPumpOn = BigEndianBitConverter.toBoolean(dsr.readByte());
            mValve1On = BigEndianBitConverter.toBoolean(dsr.readByte());
            mValve2On = BigEndianBitConverter.toBoolean(dsr.readByte());
            mValve3On = BigEndianBitConverter.toBoolean(dsr.readByte());
            mValve4On = BigEndianBitConverter.toBoolean(dsr.readByte());
            mValve5On = BigEndianBitConverter.toBoolean(dsr.readByte());
            mValve6On = BigEndianBitConverter.toBoolean(dsr.readByte());
        } catch (IOException e) {
            parseResult = ParseResult.UnknownFailure;
        }
        dsr.dispose();
        return parseResult;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MsgCode: " + MessageCodeType.convert(getMessageCode()).toString());
        sb.append("TimeStamp: " + mTimeStamp.getValue());
        sb.append("DistanceIntegral: " + mDistanceIntegral);
        sb.append("PressureDecaySlope: " + mPressureDecaySlope);
        sb.append("SDMStateType: " + mSDMStateType.toString());
        sb.append("PumpOn: " + mPumpOn);
        sb.append("Valve1On: " + mValve1On);
        sb.append("Valve2On: " + mValve2On);
        sb.append("Valve3On: " + mValve3On);
        sb.append("Valve4On: " + mValve4On);
        sb.append("Valve5On: " + mValve5On);
        sb.append("Valve6On: " + mValve6On);
        return sb.toString();
    }
}
