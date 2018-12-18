#ifndef EPOC_DRAWSITESMAPPER_H
#define EPOC_DRAWSITESMAPPER_H

#include <limits.h>

#include "DrawSites.h"

using namespace Epoc::Common::Native::Definitions;

class DrawSitesMapper {
public:
    static int mapValue(int enumIndex) {
        int enumValue = INT_MAX;
        switch (enumIndex) {
            case 0:
                enumValue = (int)drawSites::RRadial;
                break;
            case 1:
                enumValue = (int)drawSites::LRadial;
                break;
            case 2:
                enumValue = (int)drawSites::RBrach;
                break;
            case 3:
                enumValue = (int)drawSites::LBrach;
                break;
            case 4:
                enumValue = (int)drawSites::ArtLine;
                break;
            case 5:
                enumValue = (int)drawSites::RFem;
                break;
            case 6:
                enumValue = (int)drawSites::LFem;
                break;
            case 7:
                enumValue = (int)drawSites::CentLine;
                break;
            case 8:
                enumValue = (int)drawSites::SwanGanz;
                break;
            case 9:
                enumValue = (int)drawSites::RHeel;
                break;
            case 10:
                enumValue = (int)drawSites::LHeel;
                break;
            case 11:
                enumValue = (int)drawSites::UAC;
                break;
            case 12:
                enumValue = (int)drawSites::UVC;
                break;
            case 13:
                enumValue = (int)drawSites::RA;
                break;
            case 14:
                enumValue = (int)drawSites::RV;
                break;
            case 15:
                enumValue = (int)drawSites::PA;
                break;
            case 16:
                enumValue = (int)drawSites::RFinger;
                break;
            case 17:
                enumValue = (int)drawSites::LFinger;
                break;
            case 18:
                enumValue = (int)drawSites::RToe;
                break;
            case 19:
                enumValue = (int)drawSites::LToe;
                break;
            case 20:
                enumValue = (int)drawSites::PICC;
                break;
            case 21:
                enumValue = (int)drawSites::Other;
                break;
        }
        return enumValue;
    }
};


#endif // EPOC_DRAWSITESMAPPER_H