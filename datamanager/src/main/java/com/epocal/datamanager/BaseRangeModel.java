package com.epocal.datamanager;

import com.epocal.common.types.am.AnalyteName;

import java.util.HashMap;
import java.util.Map;

public class BaseRangeModel {
    class DefaultRangeValues {
        Double referenceLow = 0.0d;
        Double referenceHigh = 0.0d;
        Double criticalLow = 0.0d;
        Double criticalHigh = 0.0d;

        public DefaultRangeValues(Double referenceLow, Double referenceHigh, Double criticalLow, Double criticalHigh) {
            this.referenceLow = referenceLow;
            this.referenceHigh = referenceHigh;
            this.criticalLow = criticalLow;
            this.criticalHigh = criticalHigh;
        }
    }
    protected Map<AnalyteName, DefaultRangeValues> defaultRanges = new HashMap<>();

    protected BaseRangeModel() {
        defaultRanges.put(AnalyteName.Na, new DefaultRangeValues(138d, 146d, 84d, 181d));
        defaultRanges.put(AnalyteName.K, new DefaultRangeValues(3.5, 4.5d, 0.5d, 13d));
        defaultRanges.put(AnalyteName.pCO2, new DefaultRangeValues(35d, 48d, 4d, 251d));
        defaultRanges.put(AnalyteName.pO2, new DefaultRangeValues(83d, 108d, 4d, 751d));
        defaultRanges.put(AnalyteName.Ca, new DefaultRangeValues(1.15d, 1.33, 0d, 5d));
        defaultRanges.put(AnalyteName.pH, new DefaultRangeValues(7.35, 7.45, 5.5, 9d));
        defaultRanges.put(AnalyteName.Hct, new DefaultRangeValues(38d, 51d, 9d, 76d));
        defaultRanges.put(AnalyteName.pHT, new DefaultRangeValues(7.35, 7.45, 5.5, 9d));
        defaultRanges.put(AnalyteName.pCO2T, new DefaultRangeValues(35d, 48d, 4d, 251d));
        defaultRanges.put(AnalyteName.pO2T, new DefaultRangeValues(83d, 108d, 4d, 751d));
        defaultRanges.put(AnalyteName.HCO3act, new DefaultRangeValues(21d, 28d, 0d, 86d));
        defaultRanges.put(AnalyteName.BEecf, new DefaultRangeValues(-2d, 3d, -31d, 31d));
        defaultRanges.put(AnalyteName.BEb, new DefaultRangeValues(-2d, 3d, -31d, 31d));
        defaultRanges.put(AnalyteName.O2SAT, new DefaultRangeValues(94d, 98d, -1d, 101d));
        defaultRanges.put(AnalyteName.cTCO2, new DefaultRangeValues(22d, 29d, 4d, 51d));
        defaultRanges.put(AnalyteName.cHgb, new DefaultRangeValues(12d, 17d, 2.3, 26d));
        defaultRanges.put(AnalyteName.Glucose, new DefaultRangeValues(74d, 100d, 19d, 701d));
        defaultRanges.put(AnalyteName.Chloride, new DefaultRangeValues(98d, 107d, 64d, 141d));
        defaultRanges.put(AnalyteName.Lactate, new DefaultRangeValues(0.56, 1.39, 0d, 21d));
        defaultRanges.put(AnalyteName.Creatinine, new DefaultRangeValues(0.51, 1.19, 0d, 16d));
        defaultRanges.put(AnalyteName.AnionGap, new DefaultRangeValues(7d, 16d, -15d, 96d));
        defaultRanges.put(AnalyteName.AnionGapK, new DefaultRangeValues(10d, 20d, -11d, 100d));
        defaultRanges.put(AnalyteName.eGFR, new DefaultRangeValues(1d, 401d, -1d, 401d));
        defaultRanges.put(AnalyteName.eGFRa, new DefaultRangeValues(1d, 401d, -1d, 401d));
        defaultRanges.put(AnalyteName.eGFRj, new DefaultRangeValues(1d, 401d, -1d, 401d));
        defaultRanges.put(AnalyteName.GFRckd, new DefaultRangeValues(1d, 401d, -1d, 401d));
        defaultRanges.put(AnalyteName.GFRckda, new DefaultRangeValues(1d, 401d, -1d, 401d));
        defaultRanges.put(AnalyteName.GFRswz, new DefaultRangeValues(1d, 401d, -1d, 401d));
        defaultRanges.put(AnalyteName.AlveolarO2, new DefaultRangeValues(4d, 801d, 4d, 801d));
        defaultRanges.put(AnalyteName.ArtAlvOxDiff, new DefaultRangeValues(0d, 801d, 0d, 801d));
        defaultRanges.put(AnalyteName.ArtAlvOxRatio, new DefaultRangeValues(-1d, 101d, -1d, 101d));
        defaultRanges.put(AnalyteName.cAlveolarO2, new DefaultRangeValues(4d, 801d, 4d, 801d));
        defaultRanges.put(AnalyteName.cArtAlvOxDiff, new DefaultRangeValues(0d, 801d, 0d, 801d));
        defaultRanges.put(AnalyteName.cArtAlvOxRatio, new DefaultRangeValues(-1d, 101d, -1d, 101d));
        defaultRanges.put(AnalyteName.TCO2, new DefaultRangeValues(22d, 29d, 4d, 51d));
        defaultRanges.put(AnalyteName.Urea, new DefaultRangeValues(2.9d, 9.3d, 0.7d, 43.2d));
        defaultRanges.put(AnalyteName.UreaCreaRatio, new DefaultRangeValues(48.5d, 80.8d, 0.4d, 1615.5d));
        defaultRanges.put(AnalyteName.BUN, new DefaultRangeValues(8d, 26d, 2d, 121d));
        defaultRanges.put(AnalyteName.BUNCreaRatio, new DefaultRangeValues(12d, 20d, 0.1d, 400d));
    }
}
