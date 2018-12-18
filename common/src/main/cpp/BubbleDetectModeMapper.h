#ifndef EPOC_BUBBLEDETECTMODEMAPPER_H
#define EPOC_BUBBLEDETECTMODEMAPPER_H

#include <limits.h>

#include "BubbleDetectMode.h"

using namespace Epoc::Common::Native::Definitions;

class BubbleDetectModeMapper {
public:
    static int mapValue(int enumIndex) {
        int enumValue = INT_MAX;
        switch (enumIndex) {
            case 0:
                enumValue = (int)BubbleDetectMode::AllAlwaysOff;
                break;
            case 1:
                enumValue = (int)BubbleDetectMode::AllAlwaysOn;
                break;
            case 2:
                enumValue = (int)BubbleDetectMode::BDChannelSamplingOnlyOn;
                break;
            case 3:
                enumValue = (int)BubbleDetectMode::BDStageAllOn;
                break;
            case 4:
                enumValue = (int)BubbleDetectMode::BDStageBDChannelOnOnly;
                break;
        }
        return enumValue;
    }
};


#endif // EPOC_BUBBLEDETECTMODEMAPPER_H