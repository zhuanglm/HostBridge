package com.epocal.epoctest.testconfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dning on 9/19/2017.
 */

public class SelfCheckConfig {

    public VersionObject SelfCheckVersion;

    public double QCLimitPotentiometricLow;
    public double QCLimitPotentiometricHigh;

    public double QCLimitAmperometricLow;
    public double QCLimitAmperometricHigh;

    public double QCLimitConductivityLow;
    public double QCLimitConductivityHigh;

    public double QCLimitGroundLow;
    public double QCLimitGroundHigh;

    public ADCSetting ADCConfigSetting;

    public double Duration;
    public SamplingInfo SamplingInfoSetting;

    public BubbleDetect BubbleDetectSetting;

    public int SequenceLength;

    public List<SequenceBlock> SequenceBlocks;
    public List<ExtraByte> Extrabytelist;

    public SelfCheckConfig()
    {
        SelfCheckVersion = new VersionObject();
        ADCConfigSetting = new ADCSetting();
        SamplingInfoSetting = new SamplingInfo();
        BubbleDetectSetting = new BubbleDetect();
        SequenceBlocks = new ArrayList<SequenceBlock>();
        Extrabytelist = new ArrayList<ExtraByte>();
    }
}
