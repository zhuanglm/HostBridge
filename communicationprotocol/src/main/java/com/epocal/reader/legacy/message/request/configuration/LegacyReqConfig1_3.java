package com.epocal.reader.legacy.message.request.configuration;

import android.util.Log;

import com.epocal.common.types.am.BubbleDetectMode;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.DryCardTransmissionMode;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.LegacyMsgPayload;
import com.epocal.reader.protocolcommontype.Sequence;
import com.epocal.reader.type.Legacy.DAM_ADC_Configuration;
import com.epocal.reader.type.Legacy.DryCardQCLimits;
import com.epocal.reader.type.Legacy.ExtraBytes;
import com.epocal.reader.type.MessageDescriptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by rzhuang on Aug 9 2018.
 */

public class LegacyReqConfig1_3 extends LegacyMsgPayload {

    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Configuration,
            LegacyMessageType.ConfigurationMessage1_3.value);

    public LegacyReqConfig1_3() {
        extraInfo = new ArrayList<>();
        qcLimits = new DryCardQCLimits();
        adcConfig = new DAM_ADC_Configuration();
        dryCardCheckVersion = new SequenceVersion();
    }

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public SequenceVersion dryCardCheckVersion;
    public DryCardTransmissionMode transMode;
    public DAM_ADC_Configuration adcConfig;
    public DryCardQCLimits qcLimits;
    public BubbleDetectMode bdMode;
    public byte[] blockSizes;
    public Sequence[] sampleSequence;

    private ArrayList<ExtraBytes> extraInfo;
    private boolean mContinueOnWetCard;
    private int mBdFrequency;
    private float mDryCardAirThreshold;
    private float mDryCardFluidThreshold;
    private byte mDryCardCheckDuration;
    private float mSampleFrequency;
    private byte mAnalogClock;
    private byte mNumSamplesPerChannel;

    public byte getNumBlocks() {
        return mNumBlocks;
    }

    private byte mNumBlocks;
    private byte mSequenceLength;

    public void setContinueOnWetCard(boolean mContinueOnWetCard) {
        this.mContinueOnWetCard = mContinueOnWetCard;
    }

    public void setBdFrequency(int mBdFrequency) {
        this.mBdFrequency = mBdFrequency;
    }

    public void setDryCardAirThreshold(float mDryCardAirThreshold) {
        this.mDryCardAirThreshold = mDryCardAirThreshold;
    }

    public void setDryCardFluidThreshold(float mDryCardFluidThreshold) {
        this.mDryCardFluidThreshold = mDryCardFluidThreshold;
    }

    public void setDryCardCheckDuration(byte mDryCardCheckDuration) {
        this.mDryCardCheckDuration = mDryCardCheckDuration;
    }

    public void setSampleFrequency(float mSampleFrequency) {
        this.mSampleFrequency = mSampleFrequency;
    }

    public void setAnalogClock(byte mAnalogClock) {
        this.mAnalogClock = mAnalogClock;
    }

    public void setNumSamplesPerChannel(byte mNumSamplesPerChannel) {
        this.mNumSamplesPerChannel = mNumSamplesPerChannel;
    }

    public void setNumBlocks(byte mNumBlocks) {
        this.mNumBlocks = mNumBlocks;
    }

    public void addExtraInfo(String name, String type, String value) {
        extraInfo.add(new ExtraBytes(name, type, value));
    }

    public void setSequenceLength(byte val) {
        mSequenceLength = val;
        sampleSequence = new Sequence[val];
    }

    @Override
    public int fillBuffer() {

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            output.write(dryCardCheckVersion.major);
            output.write(dryCardCheckVersion.minor);
            output.write(dryCardCheckVersion.revision);
            output.write(transMode.value);
            output.write(mContinueOnWetCard ? (byte) 1 : (byte) 0);

            output.write(toByteArray(qcLimits.getAmperometricLow()));
            output.write(toByteArray(qcLimits.getThirtyKLow()));
            output.write(toByteArray(qcLimits.getAmperometricHigh()));
            output.write(toByteArray(qcLimits.getThirtyKHigh()));

            output.write(bdMode.value);
            // bdfrequence is only 3 bytes. so shift them back
            output.write(int24tobytes(mBdFrequency));

            output.write(toByteArray(mDryCardAirThreshold));
            output.write(toByteArray(mDryCardFluidThreshold));

            // adc config
            output.write(adcConfig.getFilterOrder());
            output.write(adcConfig.getInputBuffer());
            output.write(adcConfig.getPolarityMode());
            output.write(toByteArray(adcConfig.getVDACOffset()));
            output.write(adcConfig.getPGA());

            // duration
            output.write(mDryCardCheckDuration);

            // sequence information
            output.write(toByteArray(mSampleFrequency));
            output.write(mAnalogClock);
            output.write(mNumSamplesPerChannel);

            output.write(mNumBlocks);

            for (int i = 0; i < mNumBlocks; i++) {
                output.write(blockSizes[i]);
            }

            for (int i = 0; i < mSequenceLength; i++) {
                output.write(sampleSequence[i].ChannelType);
                output.write(sampleSequence[i].NumSamples);
                output.write(sampleSequence[i].Inputs);
                output.write(sampleSequence[i].MUXControl);
                output.write(sampleSequence[i].Inputs2);
                output.write(sampleSequence[i].ADCMUX);
                output.write(toByteArray(sampleSequence[i].VAPP1));
                output.write(toByteArray(sampleSequence[i].VAPP2));
                output.write(toByteArray(sampleSequence[i].VAPP3));
                output.write(toByteArray(sampleSequence[i].VAPP4));
            }

            byte[] buffer;

            for (int i = 0; i < extraInfo.size(); i++) {
                buffer = fillBufferWithType((extraInfo.get(i)).type, (extraInfo.get(i)).value);
                output.write(buffer);
            }

            output.flush();
            setRawBuffer(output.toByteArray());
            appendCRC();
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
