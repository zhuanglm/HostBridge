#ifndef EPOC_RESULTSCALCRETURNCODEMAPPER_H
#define EPOC_RESULTSCALCRETURNCODEMAPPER_H

#include <limits.h>

#include "ResultsCalcReturnCode.h"

using namespace Epoc::Common::Native::Definitions;

class ResultsCalcReturnCodeMapper {
public:
    static int mapValue(int enumIndex) {
        int enumValue = INT_MAX;
        switch (enumIndex) {
            case 0:
                enumValue = (int)ResultsCalcReturnCode::Success;
                break;
            case 1:
                enumValue = (int)ResultsCalcReturnCode::FailedQCStart;
                break;
            case 2:
                enumValue = (int)ResultsCalcReturnCode::CalMeanQCLow;
                break;
            case 3:
                enumValue = (int)ResultsCalcReturnCode::CalMeanQCHigh;
                break;
            case 4:
                enumValue = (int)ResultsCalcReturnCode::CalDriftQCLow;
                break;
            case 5:
                enumValue = (int)ResultsCalcReturnCode::CalDriftQCHigh;
                break;
            case 6:
                enumValue = (int)ResultsCalcReturnCode::CalSecondQCLow;
                break;
            case 7:
                enumValue = (int)ResultsCalcReturnCode::CalSecondQCHigh;
                break;
            case 8:
                enumValue = (int)ResultsCalcReturnCode::CalNoiseQCHigh;
                break;
            case 9:
                enumValue = (int)ResultsCalcReturnCode::SampleMeanQCLow;
                break;
            case 10:
                enumValue = (int)ResultsCalcReturnCode::SampleMeanQCHigh;
                break;
            case 11:
                enumValue = (int)ResultsCalcReturnCode::SampleDriftQCLow;
                break;
            case 12:
                enumValue = (int)ResultsCalcReturnCode::SampleDriftQCHigh;
                break;
            case 13:
                enumValue = (int)ResultsCalcReturnCode::SampleSecondQCLow;
                break;
            case 14:
                enumValue = (int)ResultsCalcReturnCode::SampleSecondQCHigh;
                break;
            case 15:
                enumValue = (int)ResultsCalcReturnCode::SampleNoiseQCHigh;
                break;
            case 16:
                enumValue = (int)ResultsCalcReturnCode::PostMeanQCLow;
                break;
            case 17:
                enumValue = (int)ResultsCalcReturnCode::PostMeanQCHigh;
                break;
            case 18:
                enumValue = (int)ResultsCalcReturnCode::PostDriftQCLow;
                break;
            case 19:
                enumValue = (int)ResultsCalcReturnCode::PostDriftQCHigh;
                break;
            case 20:
                enumValue = (int)ResultsCalcReturnCode::PostSecondQCLow;
                break;
            case 21:
                enumValue = (int)ResultsCalcReturnCode::PostSecondQCHigh;
                break;
            case 22:
                enumValue = (int)ResultsCalcReturnCode::PostNoiseQCHigh;
                break;
            case 23:
                enumValue = (int)ResultsCalcReturnCode::DeltaDriftLow;
                break;
            case 24:
                enumValue = (int)ResultsCalcReturnCode::DeltraDriftHigh;
                break;
            case 25:
                enumValue = (int)ResultsCalcReturnCode::DeltaMeanPostSample;
                break;
            case 26:
                enumValue = (int)ResultsCalcReturnCode::InterferentDetected;
                break;
            case 27:
                enumValue = (int)ResultsCalcReturnCode::GenericQCLow;
                break;
            case 28:
                enumValue = (int)ResultsCalcReturnCode::GenericQCHigh;
                break;
            case 29:
                enumValue = (int)ResultsCalcReturnCode::EarlyWindowLow;
                break;
            case 30:
                enumValue = (int)ResultsCalcReturnCode::EarlyWindowHigh;
                break;
            case 31:
                enumValue = (int)ResultsCalcReturnCode::PO2Bubble;
                break;
            case 32:
                enumValue = (int)ResultsCalcReturnCode::AdditionalMeanLow;
                break;
            case 33:
                enumValue = (int)ResultsCalcReturnCode::AdditionalMeanHigh;
                break;
            case 34:
                enumValue = (int)ResultsCalcReturnCode::AdditionalDriftLow;
                break;
            case 35:
                enumValue = (int)ResultsCalcReturnCode::AdditionalDriftHigh;
                break;
            case 36:
                enumValue = (int)ResultsCalcReturnCode::AdditionalNoiseHigh;
                break;
            case 37:
                enumValue = (int)ResultsCalcReturnCode::FailedQCLast;
                break;
            case 38:
                enumValue = (int)ResultsCalcReturnCode::ReportableLow;
                break;
            case 39:
                enumValue = (int)ResultsCalcReturnCode::ReportableHigh;
                break;
            case 40:
                enumValue = (int)ResultsCalcReturnCode::CannotCalculate;
                break;
            case 41:
                enumValue = (int)ResultsCalcReturnCode::BubbleAbnormality;
                break;
            case 42:
                enumValue = (int)ResultsCalcReturnCode::UnexplainedFailure;
                break;
            case 43:
                enumValue = (int)ResultsCalcReturnCode::UnderReportableRange;
                break;
            case 44:
                enumValue = (int)ResultsCalcReturnCode::OverReportableRange;
                break;
            case 45:
                enumValue = (int)ResultsCalcReturnCode::UncorrectedHematocrit;
                break;
            case 46:
                enumValue = (int)ResultsCalcReturnCode::RequirementsNotFound;
                break;
            case 47:
                enumValue = (int)ResultsCalcReturnCode::DipInSample;
                break;
            case 48:
                enumValue = (int)ResultsCalcReturnCode::SpikeInSample;
                break;
            case 49:
                enumValue = (int)ResultsCalcReturnCode::SampleWindowNoise;
                break;
            case 50:
                enumValue = (int)ResultsCalcReturnCode::OverInsanityRange;
                break;
            case 51:
                enumValue = (int)ResultsCalcReturnCode::UnderInsanityRange;
                break;
            case 52:
                enumValue = (int)ResultsCalcReturnCode::SWPeakNoiseHigh;
                break;
            case 53:
                enumValue = (int)ResultsCalcReturnCode::SWPeakDriftLow;
                break;
            case 54:
                enumValue = (int)ResultsCalcReturnCode::SWPeakDriftHigh;
                break;
            case 55:
                enumValue = (int)ResultsCalcReturnCode::ExpiredCard;
                break;
            case 56:
                enumValue = (int)ResultsCalcReturnCode::EarlyDipInSample;
                break;
            case 57:
                enumValue = (int)ResultsCalcReturnCode::EarlySpikeInSample;
                break;
            case 58:
                enumValue = (int)ResultsCalcReturnCode::LateDipInSample;
                break;
            case 59:
                enumValue = (int)ResultsCalcReturnCode::LateSpikeInSample;
                break;
            case 60:
                enumValue = (int)ResultsCalcReturnCode::CreaEarlyWindowLow;
                break;
            case 61:
                enumValue = (int)ResultsCalcReturnCode::CreaEarlyWindowHigh;
                break;
            case 62:
                enumValue = (int)ResultsCalcReturnCode::CreaDriftFailure;
                break;
            case 63:
                enumValue = (int)ResultsCalcReturnCode::HematocritShortSample;
                break;
            case 64:
                enumValue = (int)ResultsCalcReturnCode::FailedQCEver;
                break;
            case 65:
                enumValue = (int)ResultsCalcReturnCode::SampleWindowAllPointCheck;
                break;
            case 66:
                enumValue = (int)ResultsCalcReturnCode::Reserved;
                break;
            case 67:
                enumValue = (int)ResultsCalcReturnCode::CalWindowDip;
                break;
            case 68:
                enumValue = (int)ResultsCalcReturnCode::SampleDipEarly;
                break;
            case 69:
                enumValue = (int)ResultsCalcReturnCode::SampleDipLate;
                break;
            case 70:
                enumValue = (int)ResultsCalcReturnCode::CreaSampleBubble;
                break;
            case 71:
                enumValue = (int)ResultsCalcReturnCode::CreaCalDriftTermLowerConcLimit;
                break;
        }
        return enumValue;
    }
};


#endif // EPOC_RESULTSCALCRETURNCODEMAPPER_H