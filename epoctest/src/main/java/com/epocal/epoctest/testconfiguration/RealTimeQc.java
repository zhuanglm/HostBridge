package com.epocal.epoctest.testconfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dning on 9/22/2017.
 */

public class RealTimeQc {
    public int RealTimeQCKey;
    public boolean Enabled;
    public int StartTime;
    public int Interval;
    public int RealTimeQCType;
    public int NumPoints;
    public List<ExtraParameter> ExtraParameters;
    public int HumidityDetectUntil;

    public RealTimeQc() {
        ExtraParameters = new ArrayList<ExtraParameter>();
    }
}
