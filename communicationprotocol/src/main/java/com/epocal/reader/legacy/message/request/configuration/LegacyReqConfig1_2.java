package com.epocal.reader.legacy.message.request.configuration;

import android.util.Log;

import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.LegacyBubbleDetectMode;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.legacy.LegacyMsgPayload;
import com.epocal.reader.protocolcommontype.Sequence;
import com.epocal.reader.type.Legacy.DAM_ADC_Configuration;
import com.epocal.reader.type.Legacy.ExtraBytes;
import com.epocal.reader.type.Legacy.SelfCheckQCLimits;
import com.epocal.reader.type.MessageDescriptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by rzhuang on July 30 2018.
 */

public class LegacyReqConfig1_2 extends LegacyMsgPayload {

    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Configuration,
            LegacyMessageType.ConfigurationMessage1_2.value);

    public LegacyReqConfig1_2() {
        mSequenceLength = 0;
        mTransmitRawData = (byte) 1;
        selfCheckVersion = new SequenceVersion();
        qcLimits = new SelfCheckQCLimits();
        extraInfo = new ArrayList();
        adcConfig = new DAM_ADC_Configuration();
    }

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    private byte mNumChannelKeys;
    private byte mNumLimitKeys;
    private byte mNumBlocks;

    private byte mTransmitRawData;
    private byte mSequenceLength;
    private byte mSelfCheckDuration;
    private float mSampleFrequency;
    private byte mAnalogClock;
    private byte mNumSamplesPerChannel;
    private int mBdFrequency;

    public byte getSelfCheckDuration() {
        return mSelfCheckDuration;
    }

    public void setSelfCheckDuration(byte mSelfCheckDuration) {
        this.mSelfCheckDuration = mSelfCheckDuration;
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

    public void setBdFrequency(int mBdFrequency) {
        this.mBdFrequency = mBdFrequency;
    }

    public byte[] channelLimits;
    public float[] limitKeys;
    public byte[] blockSizes;
    public Sequence[] sampleSequence;
    public SequenceVersion selfCheckVersion;
    public SelfCheckQCLimits qcLimits;
    public LegacyBubbleDetectMode bdMode;
    public DAM_ADC_Configuration adcConfig;
    public ArrayList extraInfo;

    public byte getNumChannelKeys() {
        return mNumChannelKeys;
    }

    public void setNumChannelKeys(byte val) {
        mNumChannelKeys = val;
        channelLimits = new byte[mNumChannelKeys];
    }

    public byte getNumLimitKeys() {
        return mNumLimitKeys;
    }

    public void setNumLimitKeys(byte val) {
        mNumLimitKeys = val;

        limitKeys = new float[mNumLimitKeys];
    }

    public byte getNumBlocks() {
        return mNumBlocks;
    }

    public void setNumBlocks(byte val) {
        mNumBlocks = val;

        blockSizes = new byte[mNumBlocks];
    }

    public void setSequenceLength(byte val) {
        mSequenceLength = val;
        sampleSequence = new Sequence[mSequenceLength];
    }

    @Override
    public int fillBuffer() {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            output.write(selfCheckVersion.major);
            output.write(selfCheckVersion.minor);
            output.write(selfCheckVersion.revision);
            output.write(mTransmitRawData);
            byte[] buffer;

            for (int i = 0; i < mNumLimitKeys; i++) {
                buffer = toByteArray(limitKeys[i]);
                output.write(buffer);
            }

            //wait for implement getSelfCheckData
            output.write(channelLimits);
            output.write(bdMode.value);

            buffer = toByteArray(mBdFrequency);
            output.write(Arrays.copyOfRange(buffer, 1, buffer.length)); //C# moved buffer only keep 3 bytes

            output.write(adcConfig.getFilterOrder());
            output.write(adcConfig.getInputBuffer());
            output.write(adcConfig.getPolarityMode());
            output.write(toByteArray(adcConfig.getVDACOffset()));
            output.write(adcConfig.getPGA());

            output.write(mSelfCheckDuration);
            output.write(toByteArray(mSampleFrequency));

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

            for (int i = 0; i < extraInfo.size(); i++) {
                buffer = fillBufferWithType(((ExtraBytes) extraInfo.get(i)).type,
                        ((ExtraBytes) extraInfo.get(i)).value);
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
