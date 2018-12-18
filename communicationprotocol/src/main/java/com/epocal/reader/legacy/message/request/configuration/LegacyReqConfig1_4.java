package com.epocal.reader.legacy.message.request.configuration;

import android.util.Log;

import com.epocal.reader.common.HeaterSetting;
import com.epocal.reader.enumtype.InterfaceType;
import com.epocal.reader.enumtype.MessageClass;
import com.epocal.reader.enumtype.MessageGroup;
import com.epocal.reader.enumtype.ParseResult;
import com.epocal.reader.enumtype.legacy.LegacyMessageType;
import com.epocal.reader.enumtype.legacy.TransmissionMode;
import com.epocal.reader.legacy.LegacyMsgPayload;
import com.epocal.reader.protocolcommontype.Sequence;
import com.epocal.reader.type.Legacy.BDConfig;
import com.epocal.reader.type.Legacy.DAM_ADC_Configuration;
import com.epocal.reader.type.Legacy.ExtraBytes;
import com.epocal.reader.type.Legacy.StagesTimers;
import com.epocal.reader.type.MessageDescriptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by rzhuang on Aug 23 2018.
 */

public class LegacyReqConfig1_4 extends LegacyMsgPayload {

    public static final MessageDescriptor MESSAGE_DESCRIPTOR = new MessageDescriptor(
            InterfaceType.Legacy, MessageClass.Generic, MessageGroup.Configuration,
            LegacyMessageType.ConfigurationMessage1_4.value);

    public LegacyReqConfig1_4() {
        mExtraInfo = new ArrayList<>();
        bdConfig = new BDConfig();
        adcConfig = new DAM_ADC_Configuration();
        stagesTimers = new StagesTimers();
        testSequenceVersion = new SequenceVersion();
        mTransMode = TransmissionMode.AllSampledData;
        topHeating = new HeaterSetting();
        bottomHeating = new HeaterSetting();
    }

    @Override
    public MessageDescriptor getDescriptor() {
        return MESSAGE_DESCRIPTOR;
    }

    public SequenceVersion testSequenceVersion;
    public BDConfig bdConfig;
    public DAM_ADC_Configuration adcConfig;
    public StagesTimers stagesTimers;
    public HeaterSetting topHeating;
    public HeaterSetting bottomHeating;
    public Sequence[] sampleSequence;
    public byte[] blockSizes;

    private ArrayList<ExtraBytes> mExtraInfo;
    private TransmissionMode mTransMode;
    private float mSampleFrequency;
    private byte mAnalogClock;
    private byte mNumSamplesPerChannel;
    private byte mNumBlocks;

    private byte mSequenceLength;

    public float getSampleFrequency() {
        return mSampleFrequency;
    }

    public byte getNumBlocks() {
        return mNumBlocks;
    }

    public void setNumBlocks(byte mNumBlocks) {
        this.mNumBlocks = mNumBlocks;
    }

    public void setTransMode(TransmissionMode mTransMode) {
        this.mTransMode = mTransMode;
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

    public void addExtraInfo(String name, String type, String value) {
        mExtraInfo.add(new ExtraBytes(name, type, value));
    }

    public void setSequenceLength(byte val) {
        mSequenceLength = val;
        sampleSequence = new Sequence[val];
    }

    @Override
    public int fillBuffer() {

        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            output.write(testSequenceVersion.major);
            output.write(testSequenceVersion.minor);
            output.write(testSequenceVersion.revision);
            output.write(mTransMode.value);
            //Host3, transMode value = 0 , here is 2 output.write((byte) 0);
            output.write(bdConfig.BDMode.value);

            // put the uint32 frequence into the buffer at count+5.. then overrwrite the
            // most significant byte.
            output.write(int24tobytes(bdConfig.getBDFrequency()));
            output.write(toByteArray(bdConfig.getAirInitThreshold()));
            output.write(toByteArray(bdConfig.getFluidInitThreshold()));
            output.write(toByteArray(bdConfig.getAirAfterFluidThreshold()));
            output.write(toByteArray(bdConfig.getFluidAfterFluidThreshold()));

            output.write(adcConfig.getFilterOrder());
            output.write(adcConfig.getInputBuffer());
            output.write(adcConfig.getPolarityMode());
            output.write(toByteArray(adcConfig.getVDACOffset()));
            output.write(adcConfig.getPGA());

            output.write(toByteArray(stagesTimers.getCalibrationExpiryTimer()));
            output.write(toByteArray(stagesTimers.getSampleIntroductionTimer()));
            output.write(toByteArray(stagesTimers.getSampleCollectionTimer()));

            output.write(toByteArray((float) topHeating.SetPoint));
            output.write(toByteArray((float) topHeating.KP));
            output.write(toByteArray((float) topHeating.KD));
            output.write(toByteArray((float) topHeating.KI));

            output.write(toByteArray((float) bottomHeating.SetPoint));
            output.write(toByteArray((float) bottomHeating.KP));
            output.write(toByteArray((float) bottomHeating.KD));
            output.write(toByteArray((float) bottomHeating.KI));

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

            for (int i = 0; i < mExtraInfo.size(); i++) {
                buffer = fillBufferWithType((mExtraInfo.get(i)).type,(mExtraInfo.get(i)).value);
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
