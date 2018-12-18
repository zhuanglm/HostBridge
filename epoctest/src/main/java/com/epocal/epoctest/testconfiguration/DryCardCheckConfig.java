package com.epocal.epoctest.testconfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dning on 9/19/2017.
 */

public class DryCardCheckConfig {

    public int ReaderType;
    public int TransmissionMode;

    public double QCLimitAmperometricLow;
    public double QCLimitAmperometricHigh;
    public double QCLimit30KLow;
    public double QСLimit30KHigh;

    public BubbleDetect BubbleDetectSetting;
    public double DryCardAirThreshold;
    public double DryCardFluidThreshold;

    public ADCSetting ADCConfigSetting;
    public double Duration;
    public SamplingInfo SamplingInfoSetting;

    public int SequenceLength;
    public VersionObject DryCheckSequenceVersion;

    public List<SequenceBlock> SequenceBlocks;
    public List<ExtraByte> Extrabytelist;

    public DryCardCheckConfig() {
        BubbleDetectSetting = new BubbleDetect();
        ADCConfigSetting = new ADCSetting();
        SamplingInfoSetting = new SamplingInfo();
        DryCheckSequenceVersion = new VersionObject();
        Extrabytelist = new ArrayList<ExtraByte>();
        SequenceBlocks = new ArrayList<SequenceBlock>();
    }
}
