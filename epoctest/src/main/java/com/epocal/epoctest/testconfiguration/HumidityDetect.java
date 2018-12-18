package com.epocal.epoctest.testconfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dning on 9/19/2017.
 */

public class HumidityDetect {
    public int SensorType;
    public double StartTime;
    public double WindowSize;
    public double LowLimit;
    public double HighLimit;
    public List<ExtraParameter> ExtraParameters;

    public HumidityDetect() {
        ExtraParameters = new ArrayList<ExtraParameter>();
    }
}
