package com.epocal.epoctest.testconfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dning on 9/15/2017.
 */

public class SensorDescriptor {

    public byte SensorType;
    public byte SubSensorType;

    public List<Window> Windows;
    public List<QualityControlLimit> QualityControlLimits;
    public List<ExtraParameter> ExtraParameters;

    public double Extrapolation;
    public double SlopeFactor;
    public double CalConcentration;

    public double CalCurveWeight;
    public double SampleCurveWeight;
    public double PostCurvatureWeight;

    public double Offset;

    public double TPlus;
    public double TMinus;

    public int BloodPointsToSkip;
    public int BloodPointsInWindow;
    public double BloodNoiseHigh;

    public int AqPointsToSkip;
    public int AqPointsInWindow;
    public double AqNoiseHigh;

    public int LateBloodPointsToSkip;
    public int LateBloodPointsInWindow;
    public double LateBloodNoiseHigh;

    public int LateAqPointsToSkip;
    public int LateAqPointsInWindow;
    public double LateAqNoiseHigh;

    public double RTPointLimitLow;
    public double RTPointLimitHigh;

    public double D1Low;
    public double D1High;
    public double P1D2Low;
    public double P1D2High;
    public double P2D2Low;
    public double P2D2High;
    public double P3D2Low;
    public double P3D2High;

    public double AParam;
    public double BParam;
    public double CParam;
    public double DParam;
    public double FParam;
    public double GParam;

    public double AmbientTempOffset;
    public double InjectionTimeOffset;
    public double AgeOffset;
    public double PowerOffset;

    public String NeuralNetBlood;
    public String NeuralNetAQ;

    public SensorDescriptor() {
        Windows = new ArrayList<Window>();
        QualityControlLimits = new ArrayList<QualityControlLimit>();
        ExtraParameters = new ArrayList<ExtraParameter>(); //100
    }

    public Window findWindow(WindowPhase phase) {
        for (Window window : Windows) {
            if (window.Phase == phase.value) {
                return window;
            }
        }
        return null;
    }

    public QualityControlLimit findQualityControlLimit(QualityControlLimitPhase phase) {
        for (QualityControlLimit qclimit : QualityControlLimits) {
            if (qclimit.Phase == phase.value) {
                return qclimit;
            }
        }
        return null;
    }

    public ExtraParameter findExtraParameter(int idx) {
        for (int i = 0; i < ExtraParameters.size(); i++) {
            if (ExtraParameters.get(i).extraParameterID == idx) {
                return ExtraParameters.get(i);
            }
        }
        return null;
    }
}
