package com.epocal.common.am;

import com.epocal.common.types.am.*;

/**
 * This class is a mirror of struct FinalResult defined in globals.h
 * to be compatible with Analytical Manager C++.
 * DO NOT EDIT THIS FILE without consulting with AM-C++ team first!
 */
public class FinalResult {
    public ChannelType channelType;
    public AnalyteName analyte;
    public AnalyteName correctedWhat;
    public double reading;
    public ResultsCalcReturnCode returnCode;
    public boolean requirementsFailedIQC = false;

    public FinalResult() {
        this.channelType = ChannelType.ENUM_UNINITIALIZED;
        this.analyte = AnalyteName.ENUM_UNINITIALIZED;
        this.correctedWhat = AnalyteName.ENUM_UNINITIALIZED;
        this.returnCode = ResultsCalcReturnCode.ENUM_UNINITIALIZED;
    }

    @Override
    public String toString() {
        return "FinalResult{" +
                "channelType=" + channelType +
                ", analyte=" + analyte +
                ", correctedWhat=" + correctedWhat +
                ", reading=" + reading +
                ", returnCode=" + returnCode +
                ", requirementsFailedIQC=" + requirementsFailedIQC +
                '}';
    }
}
