#ifndef EPOC_CHANNELTYPEMAPPER_H
#define EPOC_CHANNELTYPEMAPPER_H

#include <limits.h>

#include "ChannelType.h"

using namespace Epoc::Common::Native::Definitions;

class ChannelTypeMapper {
public:
    static int mapValue(int enumIndex) {
        int enumValue = INT_MAX;
        switch (enumIndex) {
            case 0:
                enumValue = (int)ChannelType::P1;
                break;
            case 1:
                enumValue = (int)ChannelType::P2;
                break;
            case 2:
                enumValue = (int)ChannelType::P3;
                break;
            case 3:
                enumValue = (int)ChannelType::P4;
                break;
            case 4:
                enumValue = (int)ChannelType::P5;
                break;
            case 5:
                enumValue = (int)ChannelType::P6;
                break;
            case 6:
                enumValue = (int)ChannelType::P7;
                break;
            case 7:
                enumValue = (int)ChannelType::A1;
                break;
            case 8:
                enumValue = (int)ChannelType::A2;
                break;
            case 9:
                enumValue = (int)ChannelType::A3;
                break;
            case 10:
                enumValue = (int)ChannelType::A4;
                break;
            case 11:
                enumValue = (int)ChannelType::Reference;
                break;
            case 12:
                enumValue = (int)ChannelType::Ground;
                break;
            case 13:
                enumValue = (int)ChannelType::TopHeater;
                break;
            case 14:
                enumValue = (int)ChannelType::BottomHeater;
                break;
            case 15:
                enumValue = (int)ChannelType::CPUTemperature;
                break;
            case 16:
                enumValue = (int)ChannelType::Conductivity;
                break;
            case 17:
                enumValue = (int)ChannelType::FiveK;
                break;
            case 18:
                enumValue = (int)ChannelType::NineK;
                break;
            case 19:
                enumValue = (int)ChannelType::SixteenK;
                break;
            case 20:
                enumValue = (int)ChannelType::ThirtyK;
                break;
            case 21:
                enumValue = (int)ChannelType::Zinf;
                break;
            case 22:
                enumValue = (int)ChannelType::P1HiZ;
                break;
            case 23:
                enumValue = (int)ChannelType::P2HiZ;
                break;
            case 24:
                enumValue = (int)ChannelType::P3HiZ;
                break;
            case 25:
                enumValue = (int)ChannelType::P4HiZ;
                break;
            case 26:
                enumValue = (int)ChannelType::P5HiZ;
                break;
            case 27:
                enumValue = (int)ChannelType::P6HiZ;
                break;
            case 28:
                enumValue = (int)ChannelType::P7HiZ;
                break;
            case 29:
                enumValue = (int)ChannelType::P1LowZ;
                break;
            case 30:
                enumValue = (int)ChannelType::P2LowZ;
                break;
            case 31:
                enumValue = (int)ChannelType::P3LowZ;
                break;
            case 32:
                enumValue = (int)ChannelType::P4LowZ;
                break;
            case 33:
                enumValue = (int)ChannelType::P5LowZ;
                break;
            case 34:
                enumValue = (int)ChannelType::P6LowZ;
                break;
            case 35:
                enumValue = (int)ChannelType::P7LowZ;
                break;
            case 36:
                enumValue = (int)ChannelType::A1ST0;
                break;
            case 37:
                enumValue = (int)ChannelType::A2ST0;
                break;
            case 38:
                enumValue = (int)ChannelType::A3ST0;
                break;
            case 39:
                enumValue = (int)ChannelType::A4ST0;
                break;
            case 40:
                enumValue = (int)ChannelType::A1STiP;
                break;
            case 41:
                enumValue = (int)ChannelType::A1StiM;
                break;
            case 42:
                enumValue = (int)ChannelType::A2Sti;
                break;
            case 43:
                enumValue = (int)ChannelType::A3Sti;
                break;
            case 44:
                enumValue = (int)ChannelType::A4Sti;
                break;
            case 45:
                enumValue = (int)ChannelType::Vapp1;
                break;
            case 46:
                enumValue = (int)ChannelType::Vapp2;
                break;
            case 47:
                enumValue = (int)ChannelType::Vapp3;
                break;
            case 48:
                enumValue = (int)ChannelType::Vapp4;
                break;
            case 49:
                enumValue = (int)ChannelType::Ref18;
                break;
            case 50:
                enumValue = (int)ChannelType::RefOut;
                break;
            case 51:
                enumValue = (int)ChannelType::GroundCE;
                break;
            case 52:
                enumValue = (int)ChannelType::A2_STIP;
                break;
            case 53:
                enumValue = (int)ChannelType::A3_STIP;
                break;
            case 54:
                enumValue = (int)ChannelType::A4_STIP;
                break;
            case 55:
                enumValue = (int)ChannelType::A2_STIM;
                break;
            case 56:
                enumValue = (int)ChannelType::A3_STIM;
                break;
            case 57:
                enumValue = (int)ChannelType::A4_STIM;
                break;
            case 58:
                enumValue = (int)ChannelType::None;
                break;
            case 59:
                enumValue = (int)ChannelType::CH_2_CONDUCTIVITY;
                break;
            case 60:
                enumValue = (int)ChannelType::CH_2_5K;
                break;
            case 61:
                enumValue = (int)ChannelType::CH_2_9K;
                break;
            case 62:
                enumValue = (int)ChannelType::CH_2_16K;
                break;
            case 63:
                enumValue = (int)ChannelType::CH_2_30K;
                break;
            case 64:
                enumValue = (int)ChannelType::CH_2_Zinf;
                break;
            case 65:
                enumValue = (int)ChannelType::CH_2_A1;
                break;
            case 66:
                enumValue = (int)ChannelType::CH_2_A2;
                break;
            case 67:
                enumValue = (int)ChannelType::CH_2_A3;
                break;
            case 68:
                enumValue = (int)ChannelType::CH_2_A4;
                break;
            case 69:
                enumValue = (int)ChannelType::Ref_25V;
                break;
            case 70:
                enumValue = (int)ChannelType::R_OUTLoZ_2_5;
                break;
            case 71:
                enumValue = (int)ChannelType::GNDCELoZ_2_5;
                break;
            case 72:
                enumValue = (int)ChannelType::A1_Hi_Gain_Leak;
                break;
            case 73:
                enumValue = (int)ChannelType::A2_Hi_Gain_Leak;
                break;
            case 74:
                enumValue = (int)ChannelType::A3_Hi_Gain_Leak;
                break;
            case 75:
                enumValue = (int)ChannelType::A4_Hi_Gain_Leak;
                break;
            case 76:
                enumValue = (int)ChannelType::A1_Hi_Gain_Offset;
                break;
            case 77:
                enumValue = (int)ChannelType::A2_Hi_Gain_Offset;
                break;
            case 78:
                enumValue = (int)ChannelType::A3_Hi_Gain_Offset;
                break;
            case 79:
                enumValue = (int)ChannelType::A4_Hi_Gain_Offset;
                break;
            case 80:
                enumValue = (int)ChannelType::CONDUCTIVITY_SETTLING;
                break;
            case 81:
                enumValue = (int)ChannelType::CONDUCTIVITY_THROUGH_A4;
                break;
            case 82:
                enumValue = (int)ChannelType::GCE_VS_REF_OUT;
                break;
            case 83:
                enumValue = (int)ChannelType::RESERVED_CHAN_4;
                break;
            case 84:
                enumValue = (int)ChannelType::Reserved84;
                break;
            case 85:
                enumValue = (int)ChannelType::Reserved85;
                break;
            case 86:
                enumValue = (int)ChannelType::Reserved86;
                break;
            case 87:
                enumValue = (int)ChannelType::Reserved87;
                break;
            case 88:
                enumValue = (int)ChannelType::Reserved88;
                break;
            case 89:
                enumValue = (int)ChannelType::Reserved89;
                break;
            case 90:
                enumValue = (int)ChannelType::Reserved90;
                break;
            case 91:
                enumValue = (int)ChannelType::Reserved91;
                break;
            case 92:
                enumValue = (int)ChannelType::Reserved92;
                break;
            case 93:
                enumValue = (int)ChannelType::Reserved93;
                break;
            case 94:
                enumValue = (int)ChannelType::Reserved94;
                break;
            case 95:
                enumValue = (int)ChannelType::Reserved95;
                break;
            case 96:
                enumValue = (int)ChannelType::Reserved96;
                break;
            case 97:
                enumValue = (int)ChannelType::Reserved97;
                break;
            case 98:
                enumValue = (int)ChannelType::Reserved98;
                break;
            case 99:
                enumValue = (int)ChannelType::Reserved99;
                break;
        }
        return enumValue;
    }
};


#endif // EPOC_CHANNELTYPEMAPPER_H