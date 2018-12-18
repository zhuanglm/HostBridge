package com.epocal.common.epocobjects;


import com.epocal.common.types.am.AnalyteName;

import java.util.EnumSet;

/**
 * List of all the analytes by their grouping
 *
 * Created by Bela Mate on 6/16/2017.
 */

public class AnalyteGroups {
    // a.k.a Gases+
    public static EnumSet<AnalyteName> GASES = EnumSet.of(
            AnalyteName.pH,
            AnalyteName.pCO2,
            AnalyteName.pO2,
            AnalyteName.pHT,
            AnalyteName.pCO2T,
            AnalyteName.pO2T,
            AnalyteName.HCO3act,
            AnalyteName.BEb,
            AnalyteName.BEecf,
            AnalyteName.O2SAT,
            AnalyteName.AlveolarO2,     // A
            AnalyteName.ArtAlvOxDiff,   // A-a
            AnalyteName.ArtAlvOxRatio,  // a/A
            AnalyteName.cAlveolarO2,    // A(T)
            AnalyteName.cArtAlvOxDiff,  // A-a(T)
            AnalyteName.cArtAlvOxRatio  // a/A(T)
    );
    // a.k.a Meta+
    public static EnumSet<AnalyteName> METABOLITES = EnumSet.of(
            AnalyteName.Glucose,
            AnalyteName.Lactate,
            AnalyteName.Creatinine,
            AnalyteName.eGFR,
            AnalyteName.eGFRa,
            AnalyteName.eGFRj,
            AnalyteName.GFRckd,
            AnalyteName.GFRckda,
            AnalyteName.GFRswz,
            AnalyteName.BUN,
            AnalyteName.BUNCreaRatio,
            AnalyteName.Urea,
            AnalyteName.UreaCreaRatio
    );
    // a.k.a Chem+
    public static EnumSet<AnalyteName> CHEMICALS = EnumSet.of(
            AnalyteName.Na,
            AnalyteName.K,
            AnalyteName.Ca,
            AnalyteName.Chloride,
            AnalyteName.TCO2,
            AnalyteName.cTCO2,
            AnalyteName.AnionGap,
            AnalyteName.AnionGapK,
            AnalyteName.Hct,
            AnalyteName.cHgb
            // TODO: Missing below:
            // AnalyteName.UncorrectedHematocrit
    );
}
