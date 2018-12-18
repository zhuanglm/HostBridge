#ifndef EPOC_DELIVERYSYSTEMMAPPER_H
#define EPOC_DELIVERYSYSTEMMAPPER_H

#include <limits.h>

#include "DeliverySystem.h"

using namespace Epoc::Common::Native::Definitions;

class DeliverySystemMapper {
public:
    static int mapValue(int enumIndex) {
        int enumValue = INT_MAX;
        switch (enumIndex) {
            case 0:
                enumValue = (int) deliverySystem::AdultVent;
                break;
            case 1:
                enumValue = (int) deliverySystem::NeoVent;
                break;
            case 2:
                enumValue = (int) deliverySystem::RoomAir;
                break;
            case 3:
                enumValue = (int) deliverySystem::NasalCannula;
                break;
            case 4:
                enumValue = (int) deliverySystem::HFNC;
                break;
            case 5:
                enumValue = (int) deliverySystem::VentiMask;
                break;
            case 6:
                enumValue = (int) deliverySystem::NRB;
                break;
            case 7:
                enumValue = (int) deliverySystem::AquinOx;
                break;
            case 8:
                enumValue = (int) deliverySystem::FaceTent;
                break;
            case 9:
                enumValue = (int) deliverySystem::AeroTx;
                break;
            case 10:
                enumValue = (int) deliverySystem::AeroMask;
                break;
            case 11:
                enumValue = (int) deliverySystem::TCollar;
                break;
            case 12:
                enumValue = (int) deliverySystem::ETTube;
                break;
            case 13:
                enumValue = (int) deliverySystem::Bagging;
                break;
            case 14:
                enumValue = (int) deliverySystem::Vapotherm;
                break;
            case 15:
                enumValue = (int) deliverySystem::OxyHood;
                break;
            case 16:
                enumValue = (int) deliverySystem::HFOV;
                break;
            case 17:
                enumValue = (int) deliverySystem::HFJV;
                break;
            case 18:
                enumValue = (int) deliverySystem::Incubator;
                break;
            case 19:
                enumValue = (int) deliverySystem::OptiFlow;
                break;
            case 20:
                enumValue = (int) deliverySystem::Other;
                break;
            case 21:
                enumValue = (int) deliverySystem::BiPAP;
                break;
            case 22:
                enumValue = (int) deliverySystem::CPAP;
                break;
            case 23:
                enumValue = (int) deliverySystem::OxyMask;
                break;
            case 24:
                enumValue = (int) deliverySystem::PediVent;
                break;
            case 25:
                enumValue = (int) deliverySystem::PRB;
                break;
            case 26:
                enumValue = (int) deliverySystem::TTube;
                break;
        }
        return enumValue;
    }
};


#endif // EPOC_DELIVERYSYSTEMMAPPER_H