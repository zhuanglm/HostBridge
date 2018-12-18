package com.epocal.common_ui.testresults;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;

import com.epocal.common.realmentities.TestResult;
import com.epocal.common.types.ResultStatus;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.types.am.Units;
import com.epocal.datamanager.AnalyteModel;
import com.epocal.util.DecimalConversionUtil;

/**
 * The model class for the result object
 * <p>
 * Created by Zeeshan A Zakaria on 10/4/2017.
 */

public class ResultModel {

    private ResultStatus mResultStatus;
    private AnalyteName mAnalyteName;
    private Units mAnalyteUnit;
    private Double mAnalyteValue;
    private Double mValueReportableLow;
    private Double mValueReportableHigh;
    private Double mValueReferenceLow;
    private Double mValueReferenceHigh;
    private Double mValueCriticalLow;
    private Double mValueCriticalHigh;
    private String mDisplayprecision;

    public ResultModel(TestResult testResult) {
        mResultStatus = testResult.getResultStatus();
        mAnalyteValue = testResult.getValue();
        mAnalyteName = testResult.getAnalyteName();
        mAnalyteUnit = testResult.getUnitType();
        mValueReportableLow = testResult.getReportableLow();
        mValueReportableHigh = testResult.getReportableHigh();
        mValueReferenceLow = testResult.getReferenceLow();
        mValueReferenceHigh = testResult.getReferenceHigh();
        mValueCriticalLow = testResult.getCriticalLow();
        mValueCriticalHigh = testResult.getCriticalHigh();
        mDisplayprecision = getDisplayPrecision(testResult.getAnalyteName());
    }

    private Double getDisplayRange() {
        return (mValueReportableHigh - mValueReportableLow) / mAnalyteValue;
    }

    ResultStatus getResultStatus() {
        return mResultStatus;
    }

    public String getAnalyteName() {
        return mAnalyteName.name();
    }

    public String getUnit() {
        switch (mAnalyteUnit) {
            case pH:
            case None:
                return "";
            case mmolL:
                return "mmoI/L";
           case Percent:
                return "%";
            case mmhg:
                return "mmHg";
            case mldl:
                return "ml/dL";
            case degreesC:
               return "C";
            case degreesF:
                return "F";
            case degreesK:
                return "K";
            case gldl:
                return "g/dL";
            case mgdl:
                return "mg/dL";
            case mlmin173m2:
                return "mL/m/1.73m2";
            case kPa:
                return "kPa";
            case mEqL:
                return "mEq/L";
            case LL:
                return "L/L";
            case gL:
                return "g/L";
            case fraction:
                return "Fraction";
            case umolL:
                return "µmol/L";
            case cm:
                return "cm";
            case MillimolePerMillimole:
                return "mmol/mmol";
            case MilligramPerMilligram:
                return "mg/mg";
            case inches:
                return "in";
            case ENUM_UNINITIALIZED:
            default:
                return "";
        }
    }
    boolean valuesOutOfBoundary(){
        boolean retval = false;
        if (mValueReferenceHigh > mValueReportableHigh || mValueReferenceLow< mValueReportableLow){
            retval = true;
        }
        return retval;
    }
    Double getAnalyteVal() {
        return mAnalyteValue;
    }

    Double getReportableLowV() {
        return mValueReportableLow;
    }

    Double getReportableHighV() {
        return mValueReportableHigh;
    }
    private String getDisplayPrecision(AnalyteName analyteName){
        AnalyteModel am = new AnalyteModel();
        return am.getDisplayPrecision(am.getAnalyte(mAnalyteName));
    }
    String getAnalyteValue() {

        return (mAnalyteValue != null ? DecimalConversionUtil.DisplayDouble(mAnalyteValue, mDisplayprecision) : "");
    }

    public String getReferenceLow() {
        return (mValueReferenceLow != null ? DecimalConversionUtil.DisplayDouble(mValueReferenceLow, mDisplayprecision) : "");
    }

    public String getReferenceHigh() {
        return (mValueReferenceHigh != null ? DecimalConversionUtil.DisplayDouble(mValueReferenceHigh, mDisplayprecision) : "");
    }

    public String getReportableLow() {
        return (mValueReportableLow != null ? DecimalConversionUtil.DisplayDouble(mValueReportableLow, mDisplayprecision) : "");
    }

    public String getReportableHigh() {
        return (mValueReportableHigh != null ? DecimalConversionUtil.DisplayDouble(mValueReportableHigh, mDisplayprecision) : "");
    }

    public String getCriticalLow() {
        return (mValueCriticalLow != null ? DecimalConversionUtil.DisplayDouble(mValueCriticalLow, mDisplayprecision) : "");
    }

    public String getCriticalHigh() {
        return (mValueCriticalHigh != null ? DecimalConversionUtil.DisplayDouble(mValueCriticalHigh, mDisplayprecision) : "");
    }

    /**
     * This method places the result value in the appropriate segment proportional to the representation of reportable
     * and reference values
     * @return the resulting width
     */
    int getBarWidthReportableAndReferenceProportionality(Float pxDensity) {

        // The x0 and x1 are pixel locations on the device where the reference low and reference
        // high values are displayed. These are specific to the current TC51 device.
        int x0 = 45;
        int x1 = 135;

        double width = -1d;
        if (mAnalyteValue == null || mAnalyteValue < mValueReportableLow) {
            width = 0d;
        } else if (mAnalyteValue < mValueReferenceLow) {
            width = ((mAnalyteValue - mValueReportableLow) * x0) / (mValueReferenceLow - mValueReportableLow);
        } else if (mAnalyteValue.equals(mValueReferenceLow)) {
            width = (double) x0;
        } else if (mAnalyteValue > mValueReferenceLow && mAnalyteValue < mValueReferenceHigh) {
            double correction = ((mAnalyteValue - mValueReferenceLow) * (x1 - x0)) / (mValueReferenceHigh - mValueReferenceLow);
            width = (x0 + correction);
        } else if (mAnalyteValue.equals(mValueReportableHigh)) {
            width = (double) x1;
        } else if (mAnalyteValue > mValueReferenceHigh && mAnalyteValue < mValueReportableHigh) {
            double correction = ((mAnalyteValue - mValueReferenceHigh) * x0) / (mValueReportableHigh - mValueReferenceHigh);
            width = (x1 + correction);
        } else if (mAnalyteValue >= mValueReportableHigh) {
            width = (double) (x0 + x1);
        }

        return (int) (width * pxDensity);
    }

    /**
     * This method places the result value proportional to the linear representation of reference values
     *
     * @return the resulting width
     */
    int getBarWidthReferenceProportionality(Float pxDensity) {

        // The x0 and x1 are pixel locations on the device where the reference low and reference
        // high values are displayed. These are specific to the current TC51 device.
        int x0 = 45;
        int x1 = 135;

        double width = -1d;
        double extL = (3 * mValueReferenceLow - mValueReferenceHigh)/2;
        double extH = (3 * mValueReferenceHigh - mValueReferenceLow)/2;
        if (mAnalyteValue == null || mAnalyteValue < mValueReportableLow || mAnalyteValue < extL) {
            width = 0d;
        } else if (mAnalyteValue < mValueReferenceLow) {
            width = ((mAnalyteValue - extL) * x0) / (mValueReferenceLow - extL);
        } else if (mAnalyteValue.equals(mValueReferenceLow)) {
            width = (double) x0;
        } else if (mAnalyteValue > mValueReferenceLow && mAnalyteValue < mValueReferenceHigh) {
            double correction = ((mAnalyteValue - mValueReferenceLow) * (x1 - x0)) / (mValueReferenceHigh - mValueReferenceLow);
            width = (x0 + correction);
        } else if (mAnalyteValue.equals(mValueReportableHigh)) {
            width = (double) x1;
        } else if (mAnalyteValue > mValueReferenceHigh && mAnalyteValue < extH) {
            double correction = ((mAnalyteValue - mValueReferenceHigh) * x0) / (extH - mValueReferenceHigh);
            width = (x1 + correction);
        } else if (mAnalyteValue >= mValueReportableHigh || mAnalyteValue >= extH) {
            width = (double) (x0 + x1);
        }
        if (width< 0d){
            width = 0d;
        }
        if (width > 180d){
            width = 180d;
        }
        return (int) (width * pxDensity);
    }
    /**
     * This method places the result value on the bar using the step of ref.segment all over.
     *
     * @return the resulting width
     */
        int getBarWidthSAM(Float pxDensity) {

        // The x0 and x1 are pixel locations on the device where the reference low and reference
        // high values are displayed. These are specific to the current TC51 device.
        int x0 = 45;
        int x1 = 135;

        double width;
        double extL = (3 * mValueReferenceLow - mValueReferenceHigh)/2;
        double extH = (3 * mValueReferenceHigh - mValueReferenceLow)/2;
        if (mAnalyteValue == null || mAnalyteValue < mValueReportableLow || mAnalyteValue <= extL) {
            width = 0d;
        } else if (mAnalyteValue >= mValueReportableHigh || mAnalyteValue >= extH) {
            width = (double) (x0 + x1);
        } else {
            width = 45 *(2 * mAnalyteValue - 3 * mValueReferenceLow + mValueReferenceHigh) / (mValueReferenceHigh - mValueReferenceLow);
        }
        // final sanity
        if (width< 0d){
            width = 0d;
        }
        if (width > 180d){
            width = 180d;
        }
        return (int) (width * pxDensity);
    }
    /**
     * This method returns the width of the bar
     *
     * @param pxDensity the pixel density for correct scaling
     * @return the width of the bar
     */
    int getBarWidth(Float pxDensity) {
        Double displayValue = getDisplayRange();
        return (int) (displayValue * pxDensity);
    }

    /**
     * This method returns the height of the bar, which is set to a static value.
     *
     * @param pxDensity the pixel density for correct scaling
     * @return the height of the bar
     */
    int getBarHeight(Float pxDensity) {
        int height = 15;     // This is a fixed value
        return (int) (height * pxDensity);
    }

    /**
     * Mapping of the Analyte names to their respective short forms for the display
     *
     * @return the short form
     */
    public SpannableString getAnalyteAbbr() {

        SpannableString s = new SpannableString(mAnalyteName.name());

// todo handle all cases
        switch (mAnalyteName) {
            case pCO2:
                s = new SpannableString("pCO2");
                s.setSpan(new StyleSpan(Typeface.ITALIC), 0, 1, 0);
                s.setSpan(new SubscriptSpan(),3,4,0);
                s.setSpan(new RelativeSizeSpan(0.75f), 3,4, 0);
                break;
            case Glucose:
                s = new SpannableString("Glu");
                break;
            case Lactate:
                s = new SpannableString("Lac");
                break;
            case Creatinine:
                s = new SpannableString("Crea");
                break;
            case Chloride:
                s = new SpannableString("Cl-");
                break;
            case Unknown:
                break;
            case Na:
                s = new SpannableString("Na+");
                break;
            case K:
                s = new SpannableString("K+");
                break;
            case pH:
                break;
            case Hct:
                s = new SpannableString("Hct/cHct");
                break;
            case Ca:
                s = new SpannableString("Ca++");
                break;
            case TCO2:
                s = new SpannableString("TCO2");
                break;
            case HCO3Std:
                break;
            case CaIon:
                break;
            case BEecf:
                s = new SpannableString("BE(ecf)");
                break;
            case BEb:
                s = new SpannableString("BE(b)");
                break;
            case pHT:
                s = new SpannableString("pH(T)");
                break;
            case pCO2T:
                s = new SpannableString("pCO2(T)");
                s.setSpan(new StyleSpan(Typeface.ITALIC), 0, 1, 0);
                s.setSpan(new SubscriptSpan(), 3, 4, 0);
                s.setSpan(new RelativeSizeSpan(0.75f), 3,4, 0);
                break;
            case pO2:
                s = new SpannableString("pO2");
                s.setSpan(new StyleSpan(Typeface.ITALIC), 0, 1, 0);
                s.setSpan(new SubscriptSpan(), 2, 3, 0);
                s.setSpan(new RelativeSizeSpan(0.75f), 2,3, 0);
                break;
            case pO2T:
                s = new SpannableString("pO2(T)");
                s.setSpan(new StyleSpan(Typeface.ITALIC), 0, 1, 0);
                s.setSpan(new SubscriptSpan(), 2, 3, 0);
                s.setSpan(new RelativeSizeSpan(0.75f), 2, 3, 0);
                break;
            case AnionGap:
                s = new SpannableString("AGap");
                break;
            case AnionGapK:
                s = new SpannableString("AGapK");
                break;
            case eGFRa:
                s = new SpannableString("GFRmdr-a");
                break;
            case BUNCreaRatio:
                s = new SpannableString("BUN/Crea");
                break;
            case OxygenConcentration:
                break;
            case O2SAT:
                s = new SpannableString("cSO2");
                s.setSpan(new SubscriptSpan(), 3, 4, 0);
                s.setSpan(new RelativeSizeSpan(0.75f), 3,4, 0);
                break;
            case cTCO2:
                s = new SpannableString("cTCO2");
                s.setSpan(new SubscriptSpan(), 4, 5, 0);
                s.setSpan(new RelativeSizeSpan(0.75f), 4,5, 0);
                break;
            case AlveolarO2:
                s = new SpannableString("A");
                break;
            case ArtAlvOxDiff:
                s = new SpannableString("A-a");
                break;
            case ArtAlvOxRatio:
                s = new SpannableString("a/A");
                break;
            case cAlveolarO2:
                s = new SpannableString("A(T)");
                break;
            case cArtAlvOxDiff:
                s = new SpannableString("A-a(T)");
                break;
            case cArtAlvOxRatio:
                s = new SpannableString("a/A(T)");
                break;
            case HCO3act:
                s = new SpannableString("cHCO3-");
                s.setSpan(new SubscriptSpan(), 4, 5, 0);
                s.setSpan(new RelativeSizeSpan(0.75f), 4, 5, 0);
                break;
            case cHgb:
                break;
            case UncorrectedHematocrit:
                break;
            case Urea:
                break;
            case Creatine:
                break;
            case eGFR:
                s = new SpannableString("GFRmdr");
                break;
            case eGFRj:
                s = new SpannableString("eGFR");
                break;
            case GFRckd:
                s = new SpannableString("GFRckd");
                break;
            case GFRckda:
                s = new SpannableString("GFRckd-a");
                break;
            case GFRswz:
                s = new SpannableString("GFRswz");
                break;
            case cO2SAT:
                break;
            case UreaCreaRatio:
                break;
            case BUN:
                break;
            default:
            case ENUM_UNINITIALIZED:
                break;
        }

        return s;
    }
}