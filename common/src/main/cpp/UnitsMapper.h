#ifndef EPOC_UNITSMAPPER_H
#define EPOC_UNITSMAPPER_H

#include <limits.h>

#include "Units.h"

using namespace Epoc::Common::Native::Definitions;

class UnitsMapper {
public:
    static int mapValue(int enumIndex) {
        int enumValue = INT_MAX;
        switch (enumIndex) {
            case 0:
                enumValue = (int)Units::None;
                break;
            case 1:
                enumValue = (int)Units::Percent;
                break;
            case 2:
                enumValue = (int)Units::mmolL;
                break;
            case 3:
                enumValue = (int)Units::mmhg;
                break;
            case 4:
                enumValue = (int)Units::mldl;
                break;
            case 5:
                enumValue = (int)Units::degreesC;
                break;
            case 6:
                enumValue = (int)Units::degreesF;
                break;
            case 7:
                enumValue = (int)Units::degreesK;
                break;
            case 8:
                enumValue = (int)Units::gldl;
                break;
            case 9:
                enumValue = (int)Units::pH;
                break;
            case 10:
                enumValue = (int)Units::kPa;
                break;
            case 11:
                enumValue = (int)Units::mEqL;
                break;
            case 12:
                enumValue = (int)Units::mgdl;
                break;
            case 13:
                enumValue = (int)Units::LL;
                break;
            case 14:
                enumValue = (int)Units::gL;
                break;
            case 15:
                enumValue = (int)Units::umolL;
                break;
            case 16:
                enumValue = (int)Units::mlmin173m2;
                break;
            case 17:
                enumValue = (int)Units::cm;
                break;
            case 18:
                enumValue = (int)Units::fraction;
                break;
            case 19:
                enumValue = (int)Units::mgmg;
                break;
            case 20:
                enumValue = (int)Units::mmolmmol;
                break;
            case 21:
                enumValue = (int)Units::inches;
                break;
#ifdef NEXT_GEN
            case 22:
                enumValue = (int)Units::LitersPerMinute;
                break;
            case 23:
                enumValue = (int)Units::MillimolePerMillimole;
                break;
            case 24:
                enumValue = (int)Units::MilligramPerMilligram;
                break;
            case 25:
                enumValue = (int)Units::MilliLiterPerLiter;
                break;
#endif
        }
        return enumValue;
    }
};


#endif // EPOC_UNITSMAPPER_H