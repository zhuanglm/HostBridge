#ifndef EPOC_SENSORSMAPPER_H
#define EPOC_SENSORSMAPPER_H

#include <limits.h>
#include "Sensors.h"

using namespace Epoc::Common::Native::Definitions;

class SensorsMapper {
public:
    static int mapValue(int enumIndex) {
        int enumValue = INT_MAX;
        switch (enumIndex) {
            case 0:
                enumValue = (int) Sensors::None;
                break;
            case 1:
                enumValue = (int) Sensors::Conductivity;
                break;
            case 2:
                enumValue = (int) Sensors::Ground;
                break;
            case 3:
                enumValue = (int) Sensors::Sodium;
                break;
            case 4:
                enumValue = (int) Sensors::Potassium;
                break;
            case 5:
                enumValue = (int) Sensors::CarbonDioxide;
                break;
            case 6:
                enumValue = (int) Sensors::Oxygen;
                break;
            case 7:
                enumValue = (int) Sensors::Calcium;
                break;
            case 8:
                enumValue = (int) Sensors::pH;
                break;
            case 9:
                enumValue = (int) Sensors::Hematocrit;
                break;
            case 10:
                enumValue = (int) Sensors::HeaterTop;
                break;
            case 11:
                enumValue = (int) Sensors::HeaterBottom;
                break;
            case 12:
                enumValue = (int) Sensors::CPUTemperature;
                break;
            case 13:
                enumValue = (int) Sensors::Reference;
                break;
            case 14:
                enumValue = (int) Sensors::FiveK;
                break;
            case 15:
                enumValue = (int) Sensors::NineK;
                break;
            case 16:
                enumValue = (int) Sensors::SixteenK;
                break;
            case 17:
                enumValue = (int) Sensors::ThirtyK;
                break;
            case 18:
                enumValue = (int) Sensors::ConductivityMv;
                break;
            case 19:
                enumValue = (int) Sensors::TopHeaterPid;
                break;
            case 20:
                enumValue = (int) Sensors::Chloride;
                break;
            case 21:
                enumValue = (int) Sensors::Urea;
                break;
            case 22:
                enumValue = (int) Sensors::Glucose;
                break;
            case 23:
                enumValue = (int) Sensors::Creatinine;
                break;
            case 24:
                enumValue = (int) Sensors::NotUsed;
                break;
            case 25:
                enumValue = (int) Sensors::Lactate;
                break;
            case 26:
                enumValue = (int) Sensors::Gold;
                break;
            case 27:
                enumValue = (int) Sensors::Creatine;
                break;
            case 28:
                enumValue = (int) Sensors::TCO2;
                break;
        }
        return enumValue;
    }
};

#endif // EPOC_SENSORSMAPPER_H