package com.epocal.epoctest.type;

import android.util.Log;

import com.epocal.common.am.FinalResult;
import com.epocal.common.realmentities.Analyte;
import com.epocal.common.realmentities.AnalyteUnitInfo;
import com.epocal.common.realmentities.TestResult;
import com.epocal.common.realmentities.UnitConversionFactor;
import com.epocal.common.types.ResultStatus;
import com.epocal.common.types.TestErrorCode;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.types.am.ResultsCalcReturnCode;
import com.epocal.common.types.am.Sensors;
import com.epocal.common.types.am.TestMode;
import com.epocal.common.types.am.Units;
import com.epocal.datamanager.AnalyteModel;
import com.epocal.epoctest.analyticalmanager.AnalyticalManager;
import com.epocal.epoctest.testconfiguration.InsanityRange;
import com.epocal.epoctest.testconfiguration.ReportableRange;
import com.epocal.epoctest.testconfiguration.TestConfiguration;
import com.epocal.reader.protocolcommontype.HostErrorCode;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dning on 10/10/2017.
 */

public class TestDataFormatConvert {
    public static AnalyteName SensorToAnalyte(Sensors sensorType) {
        if (sensorType == Sensors.Sodium)
            return AnalyteName.Na;
        else if (sensorType == Sensors.Potassium)
            return AnalyteName.K;
        else if (sensorType == Sensors.Calcium)
            return AnalyteName.Ca;
        else if (sensorType == Sensors.CarbonDioxide)
            return AnalyteName.pCO2;
        else if (sensorType == Sensors.Oxygen)
            return AnalyteName.pO2;
        else if (sensorType == Sensors.pH)
            return AnalyteName.pH;
        else if (sensorType == Sensors.Hematocrit)
            return AnalyteName.Hct;
        else if (sensorType == Sensors.Conductivity)
            return AnalyteName.Hct;
        else if (sensorType == Sensors.Glucose)
            return AnalyteName.Glucose;
        else if (sensorType == Sensors.Lactate)
            return AnalyteName.Lactate;
        else if (sensorType == Sensors.Chloride)
            return AnalyteName.Chloride;
        else if (sensorType == Sensors.Creatinine)
            return AnalyteName.Creatinine;
        else if (sensorType == Sensors.Urea)
            return AnalyteName.BUN;
        else if (sensorType == Sensors.TCO2)
            return AnalyteName.TCO2;
        else
            return AnalyteName.Unknown;
    }

    public static AnalyteName AnalytesUreatoBUNInternalSwitch(AnalyteName analyteName) {
        if (analyteName == AnalyteName.Urea) {
            return AnalyteName.BUN;
        } else if (analyteName == AnalyteName.UreaCreaRatio) {
            return AnalyteName.BUNCreaRatio;
        }
        return analyteName;
    }

    public static double CToF(double c) {
        return (c * 1.8) + 32;
    }

    public static double FToC(double f) {
        return (f - 32) / 1.8;
    }

    public static double upConvertAnalyteDataDefaultUnit(AnalyteName analyte, double reading, int defaultUnit) {
        if (AnalyticalManager.isBadValue(reading))
            return reading;
        Analyte an = new AnalyteModel().getAnalyte(analyte);

        double convFactor = Double.NaN;
        if (defaultUnit == an.getUnitType().value) {
            return reading;
        } else {
           // convFactor = getConversionFactor(an, defaultUnit, an.getUnitType().value);
            convFactor = getConversionFactor(analyte, defaultUnit, an.getUnitType().value);
        }
        if (!Double.isNaN(convFactor)) {
            return reading * convFactor;
        }
        return reading;
    }
    public static double getConversionFactor(AnalyteName analyteName, int fromUnit, int toUnit) {
        double retval = Double.NaN;
        ArrayList<UnitConversionFactor> factors = new AnalyteModel().getUnitConversionFactors(analyteName);
        if (factors != null) {
            for (int i = 0; i < factors.size(); i++) {
                UnitConversionFactor factor = (factors.get(i));
                if (factor.getfromUnitType().value == fromUnit && factor.gettoUnitType().value == toUnit) {
                    retval = factor.getConversionValue();
                    break;
                }
            }
        }
        return retval;
    }
    public static double getConversionFactor(Analyte an, int fromUnit, int toUnit) {
        double retval = Double.NaN;
        List factors = an.getUnitConversionFactors();
        if (factors != null) {
            for (int i = 0; i < factors.size(); i++) {
                UnitConversionFactor factor = ((UnitConversionFactor) factors.get(i));
                if (factor.getfromUnitType().value == fromUnit && factor.gettoUnitType().value == toUnit) {
                    retval = factor.getConversionValue();
                    break;
                }
            }
        }
        return retval;
    }

    public static ResultStatus determineRangeIndicator(ResultsCalcReturnCode returnCode,
                                                       double compareAgainst,
                                                       double referenceLow,
                                                       double referenceHigh,
                                                       double criticalLow,
                                                       double criticalHigh,
                                                       double reportableLow,
                                                       double reportableHigh) {
        boolean turnOffLow = false;
        boolean turnOffHigh = false;

        ResultStatus tempRef = ResultStatus.Normal;

        if ((returnCode == ResultsCalcReturnCode.Success) ||
                (returnCode == ResultsCalcReturnCode.UncorrectedHematocrit) ||
                (returnCode == ResultsCalcReturnCode.UnderReportableRange) ||
                (returnCode == ResultsCalcReturnCode.OverReportableRange)) {
            // first check critical
            if (criticalLow < reportableLow)
                turnOffLow = true;

            if (criticalHigh > reportableHigh)
                turnOffHigh = true;

            if (turnOffHigh && turnOffLow) {
                tempRef = ResultStatus.Normal;
            } else if (turnOffLow) {
                if ((compareAgainst > criticalHigh) && (compareAgainst >= reportableLow)) {
                    tempRef = ResultStatus.CriticalHigh;
                }
            } else if (turnOffHigh) {
                if ((compareAgainst < criticalLow) && (compareAgainst <= reportableHigh)) {
                    tempRef = ResultStatus.CriticalLow;
                }
            } else {
                if (compareAgainst < criticalLow) {
                    tempRef = ResultStatus.CriticalLow;
                } else if (compareAgainst > criticalHigh) {
                    tempRef = ResultStatus.CriticalHigh;
                }
            }

            if (tempRef == ResultStatus.Normal) {
                turnOffHigh = false;
                turnOffLow = false;

                // first check critical
                if (referenceLow < reportableLow)
                    turnOffLow = true;

                if (referenceHigh > reportableHigh)
                    turnOffHigh = true;

                if (turnOffHigh && turnOffLow) {
                    tempRef = ResultStatus.Normal;
                } else if (turnOffLow) {
                    if ((compareAgainst > referenceHigh) && (compareAgainst >= reportableLow)) {
                        tempRef = ResultStatus.ReferenceHigh;
                    }
                } else if (turnOffHigh) {
                    if ((compareAgainst < referenceLow) && (compareAgainst <= reportableHigh)) {
                        tempRef = ResultStatus.ReferenceLow;
                    }
                } else {
                    if (compareAgainst < referenceLow) {
                        tempRef = ResultStatus.ReferenceLow;
                    } else if (compareAgainst > referenceHigh) {
                        tempRef = ResultStatus.ReferenceHigh;
                    }
                }
            }
        }

        return tempRef;
    }

    public static Units getUnit(AnalyteName analyte) {
        Analyte an = new AnalyteModel().getAnalyte(analyte);
        return an.getUnitType();
    }

    public static double getAnalyteToCorrectDecimalPlaces(AnalyteName analyte, double val, Units units) {
        if (Double.isNaN(val)) {
            return Double.NaN;
        } else if (val == Double.NEGATIVE_INFINITY || val == Double.MIN_VALUE) {
            return Double.MIN_VALUE;
        } else if (val == Double.POSITIVE_INFINITY || val == Double.MAX_VALUE) {
            return Double.MAX_VALUE;
        } else {
            return Double.parseDouble(getAnalyteStringToCorrectDecimalPlaces(analyte, val, units));
        }
    }

    public static String getAnalyteStringToCorrectDecimalPlaces(AnalyteName analyte, double val, Units units) {
        try {
            Analyte an = new AnalyteModel().getAnalyte(analyte);

            String formatterStr = "";
            AnalyteUnitInfo m = null;
            for (int i = 0; i < an.getMetaInfo().size(); i++) {
                if (an.getMetaInfo().get(i).getUnitType() == units) {
                    m = an.getMetaInfo().get(i);
                    break;
                }
            }
            if (m != null) {
                formatterStr = m.getPrecision();
            }
            NumberFormat formatter = new DecimalFormat(formatterStr);
            return formatter.format(val);
        }
        catch (Exception e)
        {
            Log.d("CorrectDecimalPlaces",e.getMessage());
        }
        return Double.toString(val);
    }

    public static String convertReadingValue(ResultsCalcReturnCode returnCode, double readingValue, TestResult result) {
        String retString;
        result.setValue(readingValue);

        if ((returnCode.value >= ResultsCalcReturnCode.FailedQCStart.value) &&
                (returnCode.value <= ResultsCalcReturnCode.FailedQCLast.value)) {
            result.setValue(Double.NaN);
            result.setResultStatus(ResultStatus.FailediQC);
            retString = "Failed iQC";
        } else if (returnCode == ResultsCalcReturnCode.OverReportableRange) {
            result.setValue(readingValue);
            //Result.Status = Epoc.Types.ResultStatus.AboveReportableRange;
            retString = "> " + Double.toString(readingValue);
        } else if (returnCode == ResultsCalcReturnCode.UnderReportableRange) {
            result.setValue(readingValue);
            //Result.Status = Epoc.Types.ResultStatus.BelowReportableRange;
            retString = "< " + Double.toString(readingValue);
        } else if ((returnCode == ResultsCalcReturnCode.CannotCalculate) ||
                (returnCode == ResultsCalcReturnCode.UnexplainedFailure)) {
            result.setValue(Double.NaN);
            result.setResultStatus(ResultStatus.CNC);
            retString = "cnc";
        } else if ((returnCode == ResultsCalcReturnCode.SpikeInSample) ||
                (returnCode == ResultsCalcReturnCode.DipInSample) ||
                (returnCode == ResultsCalcReturnCode.SampleWindowNoise) ||
                (returnCode == ResultsCalcReturnCode.UnderInsanityRange) ||
                (returnCode == ResultsCalcReturnCode.OverInsanityRange)) {
            result.setValue(Double.NaN);
            result.setResultStatus(ResultStatus.FailediQC);
            retString = "Failed iQC";
        } else if (returnCode == ResultsCalcReturnCode.ExpiredCard) {
            result.setValue(Double.NaN);
            result.setResultStatus(ResultStatus.Expired);
            retString = "expired";
        } else if (AnalyticalManager.failedIQC(returnCode)) {
            result.setValue(Double.NaN);
            result.setResultStatus(ResultStatus.FailediQC);
            retString = "Failed iQC";
        }
        // if no error codes, return the original value string
        retString = Double.toString(readingValue);
        return retString;
    }

    public static void applyReportableAndInsanityRangesToTestResults(List testResults, TestMode testMode, TestConfiguration testConfiguration) {
        for (int i = 0; i < testResults.size(); i++) {
            FinalResult fr = (FinalResult) testResults.get(i);
            if (fr.returnCode == ResultsCalcReturnCode.Success) {
                InsanityRange insanityRange = testConfiguration.getInsanityRange(fr.analyte);
                ReportableRange reportableRange = testConfiguration.getDefaultReportableRange(fr.analyte);
                if (fr.reading < insanityRange.Low) {
                    fr.returnCode = ResultsCalcReturnCode.UnderInsanityRange;
                } else if (fr.reading > insanityRange.High) {
                    fr.returnCode = ResultsCalcReturnCode.OverInsanityRange;
                } else if (testMode == TestMode.BloodTest) {
                    if (fr.reading < reportableRange.Low) {
                        fr.returnCode = ResultsCalcReturnCode.UnderReportableRange;
                    } else if (fr.reading > reportableRange.High) {
                        fr.returnCode = ResultsCalcReturnCode.OverReportableRange;
                    }
                }
            }
        }
    }

    public static TestErrorCode convertToErrorCode(HostErrorCode hostErrorCode) {
        return TestErrorCode.fromInt(hostErrorCode.value);
    }

}
