#ifndef EPOC_ANALYTESMAPPER_H
#define EPOC_ANALYTESMAPPER_H

#include <limits.h>

#include "Analytes.h"

using namespace Epoc::Common::Native::Definitions;

class AnalytesMapper {
public:
    static int mapValue(int enumIndex) {
        int enumValue = INT_MAX;
        switch (enumIndex) {
            case 0:
                enumValue = (int)Analytes::None;
                break;
            case 1:
                enumValue = (int)Analytes::Sodium;
                break;
            case 2:
                enumValue = (int)Analytes::Potassium;
                break;
            case 3:
                enumValue = (int)Analytes::CarbonDioxide;
                break;
            case 4:
                enumValue = (int)Analytes::Oxygen;
                break;
            case 5:
                enumValue = (int)Analytes::Calcium;
                break;
            case 6:
                enumValue = (int)Analytes::pH;
                break;
            case 7:
                enumValue = (int)Analytes::Hematocrit;
                break;
            case 8:
                enumValue = (int)Analytes::CorrectedpH;
                break;
            case 9:
                enumValue = (int)Analytes::correctedCO2;
                break;
            case 10:
                enumValue = (int)Analytes::correctedPO2;
                break;
            case 11:
                enumValue = (int)Analytes::ActualBicarbonate;
                break;
            case 12:
                enumValue = (int)Analytes::StandardBicarbonate;
                break;
            case 13:
                enumValue = (int)Analytes::CalciumIon;
                break;
            case 14:
                enumValue = (int)Analytes::BaseExcessECF;
                break;
            case 15:
                enumValue = (int)Analytes::BaseExcessBlood;
                break;
            case 16:
                enumValue = (int)Analytes::OxygenConcentration;
                break;
            case 17:
                enumValue = (int)Analytes::OxygenSaturation;
                break;
            case 18:
                enumValue = (int)Analytes::TotalCO2;
                break;
            case 19:
                enumValue = (int)Analytes::AlveolarO2;
                break;
            case 20:
                enumValue = (int)Analytes::ArtAlvOxDiff;
                break;
            case 21:
                enumValue = (int)Analytes::ArtAlvOxRatio;
                break;
            case 22:
                enumValue = (int)Analytes::Hemoglobin;
                break;
            case 23:
                enumValue = (int)Analytes::UncorrectedHematocrit;
                break;
            case 24:
                enumValue = (int)Analytes::Glucose;
                break;
            case 25:
                enumValue = (int)Analytes::Chloride;
                break;
            case 26:
                enumValue = (int)Analytes::Lactate;
                break;
            case 27:
                enumValue = (int)Analytes::Urea;
                break;
            case 28:
                enumValue = (int)Analytes::Creatinine;
                break;
            case 29:
                enumValue = (int)Analytes::AnionGap;
                break;
            case 30:
                enumValue = (int)Analytes::AnionGapK;
                break;
            case 31:
                enumValue = (int)Analytes::Creatine;
                break;
            case 32:
                enumValue = (int)Analytes::eGFR;
                break;
            case 33:
                enumValue = (int)Analytes::eGFRa;
                break;
            case 34:
                enumValue = (int)Analytes::eGFRj;
                break;
            case 35:
                enumValue = (int)Analytes::correctedOxygenSaturation;
                break;
            case 36:
                enumValue = (int)Analytes::correctedAlveolarO2;
                break;
            case 37:
                enumValue = (int)Analytes::correctedArtAlvOxDiff;
                break;
            case 38:
                enumValue = (int)Analytes::correctedArtAlvOxRatio;
                break;
            case 39:
                enumValue = (int)Analytes::MeasuredTCO2;
                break;
            case 40:
                enumValue = (int)Analytes::UreaCreaRatio;
                break;
            case 41:
                enumValue = (int)Analytes::BUN;
                break;
            case 42:
                enumValue = (int)Analytes::BUNCreaRatio;
                break;
            case 43:
                enumValue = (int)Analytes::GFRckd;
                break;
            case 44:
                enumValue = (int)Analytes::GFRckda;
                break;
            case 45:
                enumValue = (int)Analytes::GFRswz;
                break;
#if NEXT_GEN
            case 46:
                enumValue = (int)Analytes::FO2Hb;
                break;
            case 47:
                enumValue = (int)Analytes::FHHb;
                break;
            case 48:
                enumValue = (int)Analytes::FMetHb;
                break;
            case 49:
                enumValue = (int)Analytes::FCOHb;
                break;
            case 50:
                enumValue = (int)Analytes::Hgb;
                break;
            case 51:
                enumValue = (int)Analytes::SO2;
                break;
            case 52:
                enumValue = (int)Analytes::ctO2;
                break;
            case 53:
                enumValue = (int)Analytes::BO2;
                break;
#endif
        }
        return enumValue;
    }
};


#endif // EPOC_ANALYTESMAPPER_H