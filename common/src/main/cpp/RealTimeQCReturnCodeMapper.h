//
// Created by akaiya on 07/09/2017.
//

#ifndef SRC_REALTIMEQCRETURNCODEMAPPER_H
#define SRC_REALTIMEQCRETURNCODEMAPPER_H
#include <limits.h>
#include "RealTimeQCReturnCode.h"

using namespace Epoc::Common::Native::Definitions;

class RealTimeQCReturnCodeMapper {
public:
    static int mapValue(int enumIndex) {
        int enumValue = INT_MAX;
        switch (enumIndex)
        {
            case 0:
                enumValue = (int) RealTimeQCReturnCode::Success;
                break;
            case 1:
                enumValue = (int) RealTimeQCReturnCode::FirstFailed;
                break;
            case 2:
                enumValue = (int) RealTimeQCReturnCode::FailedPointLowQC;
                break;
            case 3:
                enumValue = (int) RealTimeQCReturnCode::FailedPointHighQC;;
                break;
            case 4:
                enumValue = (int) RealTimeQCReturnCode::FailedD1Low;
                break;
            case 5:
                enumValue = (int) RealTimeQCReturnCode::FailedD1High;
                break;
            case 6:
                enumValue = (int) RealTimeQCReturnCode::FailedD2Low;
                break;
            case 7:
                enumValue = (int) RealTimeQCReturnCode::FailedD2High;
                break;
            case 8:
                enumValue = (int) RealTimeQCReturnCode::Failed;
                break;
            case 9:
                enumValue = (int) RealTimeQCReturnCode::LastFailed;
                break;

            case 10:
                enumValue = (int) RealTimeQCReturnCode::NotPerformed;
                break;
            case 11:
                enumValue = (int) RealTimeQCReturnCode::HumidityFailed;
                break;
            case 12:
                enumValue = (int) RealTimeQCReturnCode::CreaEarlyWindowLow;
                break;
            case 13:
                enumValue = (int) RealTimeQCReturnCode::CreaEarlyWindowHigh;
                break;
            case 14:
                enumValue = (int) RealTimeQCReturnCode::AdditionalWindowMeanLow;
                break;
            case 15:
                enumValue = (int) RealTimeQCReturnCode::AdditionalWindowMeanHigh;
                break;
            case 16:
                enumValue = (int) RealTimeQCReturnCode::AdditionalWindowDriftLow;
                break;
            case 17:
                enumValue = (int) RealTimeQCReturnCode::AdditionalWindowDriftHigh;
                break;
            case 18:
                enumValue = (int) RealTimeQCReturnCode::AdditionalWindowNoiseHigh;
                break;
            case 19:
                enumValue = (int) RealTimeQCReturnCode::ReferenceBubble;
                break;

        }
        return enumValue;
    }
};

#endif //SRC_REALTIMEQCRETURNCODEMAPPER_H
