package com.epocal.epoctest.testconfiguration;

import com.epocal.reader.common.HeaterSetting;

/**
 * Created by dning on 9/14/2017.
 */

public class ReaderConfig {

    public int TransmissionMode;
    public BubbleDetect BubbleDetectSetting;
    public double CalibrationInitExpiryTimer;

    public double AirInitThreshold;
    public double AirAfterFluidThreshold;
    public double FluidInitThreshold;
    public double FluidAfterFluidThreshold;
    public ADCSetting ADCConfigSetting;

    public HeaterSetting TopHeaterSetting;
    public HeaterSetting BottomHeaterSetting;

    public SamplingInfo SamplingInfo;

    public ReaderTimer ReaderTimerSetting;

    public ReaderConfig()
    {
        BubbleDetectSetting = new BubbleDetect();
        ADCConfigSetting = new ADCSetting();
        TopHeaterSetting = new HeaterSetting();
        BottomHeaterSetting = new HeaterSetting();

        SamplingInfo = new SamplingInfo();

        ReaderTimerSetting = new ReaderTimer();
    }
}
