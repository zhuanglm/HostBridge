#ifndef EPOC_RESPIRATORYMODEMAPPER_H
#define EPOC_RESPIRATORYMODEMAPPER_H

#include <limits.h>

#include "RespiratoryMode.h"

using namespace Epoc::Common::Native::Definitions;

class RespiratoryModeMapper {
public:
    static int mapValue(int enumIndex) {
        int enumValue = INT_MAX;
        switch (enumIndex) {
            case 0:
                enumValue = (int)respiratoryMode::SIMV;
                break;
            case 1:
                enumValue = (int)respiratoryMode::AC;
                break;
            case 2:
                enumValue = (int)respiratoryMode::PC;
                break;
            case 3:
                enumValue = (int)respiratoryMode::PS;
                break;
            case 4:
                enumValue = (int)respiratoryMode::VC;
                break;
            case 5:
                enumValue = (int)respiratoryMode::BiLevel;
                break;
            case 6:
                enumValue = (int)respiratoryMode::CPAPPS;
                break;
            case 7:
                enumValue = (int)respiratoryMode::SIMVPC;
                break;
            case 8:
                enumValue = (int)respiratoryMode::PAV;
                break;
            case 9:
                enumValue = (int)respiratoryMode::PRVC;
                break;
            case 10:
                enumValue = (int)respiratoryMode::TC;
                break;
            case 11:
                enumValue = (int)respiratoryMode::Other;
                break;
            case 12:
                enumValue = (int)respiratoryMode::BiVent;
                break;
            case 13:
                enumValue = (int)respiratoryMode::NCPAP;
                break;
            case 14:
                enumValue = (int)respiratoryMode::NIV;
                break;
            case 15:
                enumValue = (int)respiratoryMode::PCPS;
                break;
            case 16:
                enumValue = (int)respiratoryMode::PRVCPS;
                break;
            case 17:
                enumValue = (int)respiratoryMode::SIMVPS;
                break;
            case 18:
                enumValue = (int)respiratoryMode::SIMVPCPS;
                break;
            case 19:
                enumValue = (int)respiratoryMode::SIMVPRVCPS;
                break;
            case 20:
                enumValue = (int)respiratoryMode::SIMVVCPS;
                break;
            case 21:
                enumValue = (int)respiratoryMode::VS;
                break;
        }
        return enumValue;
    }
};


#endif // EPOC_RESPIRATORYMODEMAPPER_H