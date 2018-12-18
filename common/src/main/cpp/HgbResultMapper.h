#ifndef EPOC_HGBRESULTMAPPER_H
#define EPOC_HGBRESULTMAPPER_H

#include <limits.h>
#include "HgbResult.h"

using namespace Epoc::Common::Native::Definitions;

class HgbResultMapper {
public:
    static int mapValue(int enumIndex) {
        int enumValue = INT_MAX;
        switch (enumIndex) {
            case 0:
                enumValue = (int)HgbResult::O2Hb;
                break;
            case 1:
                enumValue = (int)HgbResult::COHb;
                break;
            case 2:
                enumValue = (int)HgbResult::HHb;
                break;
            case 3:
                enumValue = (int)HgbResult::MetHb;
                break;
            case 4:
                enumValue = (int)HgbResult::tHb;
                break;
        }
        return enumValue;
    }
};

#endif // EPOC_HGBRESULTMAPPER_H