#ifndef EPOC_BLOODSAMPLETYPEMAPPER_H
#define EPOC_BLOODSAMPLETYPEMAPPER_H

#include <limits.h>

#include "BloodSampleType.h"

using namespace Epoc::Common::Native::Definitions;

class BloodSampleTypeMapper {
public:
    static int mapValue(int enumIndex) {
        int enumValue = INT_MAX;
        switch (enumIndex) {
            case 0:
                enumValue = (int)BloodSampleType::Unspecified;
                break;
            case 1:
                enumValue = (int)BloodSampleType::Unknown;
                break;
            case 2:
                enumValue = (int)BloodSampleType::Arterial;
                break;
            case 3:
                enumValue = (int)BloodSampleType::Venous;
                break;
            case 4:
                enumValue = (int)BloodSampleType::MixedVenous;
                break;
            case 5:
                enumValue = (int)BloodSampleType::Capillary;
                break;
            case 6:
                enumValue = (int)BloodSampleType::Cord;
                break;
            case 7:
                enumValue = (int)BloodSampleType::CordArterial;
                break;
            case 8:
                enumValue = (int)BloodSampleType::CordVenous;
                break;
        }
        return enumValue;
    }
};


#endif // EPOC_BLOODSAMPLETYPEMAPPER_H