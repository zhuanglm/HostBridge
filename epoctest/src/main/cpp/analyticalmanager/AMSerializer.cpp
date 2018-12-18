//
// Created by Akaiya on 31/08/2017.
//

#include <sstream>

#include "AMSerializer.h"
#include "AMSerializerHelper.h"

///////////////////////////////////////////////////////////////////////////////////////////////////////
// Utility methods to convert between Transfer Objects and C++ AM types

void AMSerializer::convert(const HumidityStruct& humidityStruct, to::HumidityStruct& humidityStructTO)
{
    humidityStructTO.set_sensortype((int)humidityStruct.sensorType);
    humidityStructTO.set_windowstart(humidityStruct.windowStart);
    humidityStructTO.set_windowsize(humidityStruct.windowSize);
    humidityStructTO.set_low(humidityStruct.low);
    humidityStructTO.set_high(humidityStruct.high);
    humidityStructTO.set_extra1(humidityStruct.extra1);
    humidityStructTO.set_extra2(humidityStruct.extra2);
    humidityStructTO.set_extra3(humidityStruct.extra3);
    humidityStructTO.set_extra4(humidityStruct.extra4);
    humidityStructTO.set_extra5(humidityStruct.extra5);
    humidityStructTO.set_extra6(humidityStruct.extra6);
    humidityStructTO.set_extra7(humidityStruct.extra7);
    humidityStructTO.set_extra8(humidityStruct.extra8);
    humidityStructTO.set_extra9(humidityStruct.extra9);
    humidityStructTO.set_extra10(humidityStruct.extra10);
}
void AMSerializer::convert(const to::HumidityStruct& humidityStructTO, HumidityStruct& humidityStruct)
{
    humidityStruct.sensorType = (Sensors)humidityStructTO.sensortype();
    humidityStruct.windowStart = humidityStructTO.windowstart();
    humidityStruct.windowSize = humidityStructTO.windowsize();
    humidityStruct.low = humidityStructTO.low();
    humidityStruct.high = humidityStructTO.high();
    humidityStruct.extra1 = humidityStructTO.extra1();
    humidityStruct.extra2 = humidityStructTO.extra2();
    humidityStruct.extra3 = humidityStructTO.extra3();
    humidityStruct.extra4 = humidityStructTO.extra4();
    humidityStruct.extra5 = humidityStructTO.extra5();
    humidityStruct.extra6 = humidityStructTO.extra6();
    humidityStruct.extra7 = humidityStructTO.extra7();
    humidityStruct.extra8 = humidityStructTO.extra8();
    humidityStruct.extra9 = humidityStructTO.extra9();
    humidityStruct.extra10 = humidityStructTO.extra10();
}
void AMSerializer::convert(const FinalResult& finalResult, to::FinalResult& finalResultTO)
{
    finalResultTO.set_channeltype((int)finalResult.channelType);
    finalResultTO.set_analyte((int)finalResult.analyte);
    finalResultTO.set_correctedwhat((int)finalResult.correctedWhat);
    finalResultTO.set_reading(finalResult.reading);
    finalResultTO.set_returncode((int)finalResult.returnCode);
    finalResultTO.set_requirementsfailediqc(finalResult.requirementsFailedIQC);
}
void AMSerializer::convert(const to::FinalResult& finalResultTO, FinalResult& finalResult)
{
    finalResult.channelType = (ChannelType)finalResultTO.channeltype();
    finalResult.analyte = (Analytes)finalResultTO.analyte();
    finalResult.correctedWhat = (Analytes)finalResultTO.correctedwhat();
    finalResult.reading = finalResultTO.reading();
    finalResult.returnCode = (ResultsCalcReturnCode)finalResultTO.returncode();
    finalResult.requirementsFailedIQC = finalResultTO.requirementsfailediqc();
}

void  AMSerializer::convert(const SensorInfo& sensorInfo, to::SensorInfo& sensorInfoTO)
{
    sensorInfoTO.set_channeltype((int)sensorInfo.channelType);
    sensorInfoTO.set_sensortype((int)sensorInfo.sensorType);
    sensorInfoTO.set_sensordescriptornumber(sensorInfo.sensorDescriptorNumber);
    sensorInfoTO.set_caldelimit(sensorInfo.calDelimit);
    sensorInfoTO.set_sampledelimit(sensorInfo.sampleDelimit);
    sensorInfoTO.set_postdelimit(sensorInfo.postDelimit);
    sensorInfoTO.set_extrapolation(sensorInfo.extrapolation);
    sensorInfoTO.set_calwindowsize(sensorInfo.calWindowSize);
    sensorInfoTO.set_samplewindowsize(sensorInfo.sampleWindowSize);
    sensorInfoTO.set_postwindowsize(sensorInfo.postWindowSize);
    sensorInfoTO.set_calcurveweight(sensorInfo.calCurveWeight);
    sensorInfoTO.set_samplecurveweight(sensorInfo.sampleCurveWeight);
    sensorInfoTO.set_calconcentration(sensorInfo.calConcentration);
    sensorInfoTO.set_offset(sensorInfo.offset);
    sensorInfoTO.set_slopefactor(sensorInfo.slopeFactor);
    sensorInfoTO.set_calmeanlowqc(sensorInfo.CalMeanLowQC);
    sensorInfoTO.set_calmeanhighqc(sensorInfo.CalMeanHighQC);
    sensorInfoTO.set_caldriftlowqc(sensorInfo.CalDriftLowQC);
    sensorInfoTO.set_caldrifthighqc(sensorInfo.CalDriftHighQC);
    sensorInfoTO.set_calsecondlowqc(sensorInfo.CalSecondLowQC);
    sensorInfoTO.set_calsecondhighqc(sensorInfo.CalSecondHighQC);
    sensorInfoTO.set_calnoisehighqc(sensorInfo.CalNoiseHighQC);
    sensorInfoTO.set_samplemeanlowqc(sensorInfo.SampleMeanLowQC);
    sensorInfoTO.set_samplemeanhighqc(sensorInfo.SampleMeanHighQC);
    sensorInfoTO.set_sampledriftlowqc(sensorInfo.SampleDriftLowQC);
    sensorInfoTO.set_sampledrifthighqc(sensorInfo.SampleDriftHighQC);
    sensorInfoTO.set_samplesecondlowqc(sensorInfo.SampleSecondLowQC);
    sensorInfoTO.set_samplesecondhighqc(sensorInfo.SampleSecondHighQC);
    sensorInfoTO.set_samplenoisehighqc(sensorInfo.SampleNoiseHighQC);
    sensorInfoTO.set_postmeanlowqc(sensorInfo.PostMeanLowQC);
    sensorInfoTO.set_postmeanhighqc(sensorInfo.PostMeanHighQC);
    sensorInfoTO.set_postdriftlowqc(sensorInfo.PostDriftLowQC);
    sensorInfoTO.set_postdrifthighqc(sensorInfo.PostDriftHighQC);
    sensorInfoTO.set_postsecondlowqc(sensorInfo.PostSecondLowQC);
    sensorInfoTO.set_postsecondhighqc(sensorInfo.PostSecondHighQC);
    sensorInfoTO.set_postnoisehighqc(sensorInfo.PostNoiseHighQC);
    sensorInfoTO.set_deltadriftlowqc(sensorInfo.DeltaDriftLowQC);
    sensorInfoTO.set_deltadrifthighqc(sensorInfo.DeltaDriftHighQC);
    sensorInfoTO.set_param1(sensorInfo.param1);
    sensorInfoTO.set_param2(sensorInfo.param2);
    sensorInfoTO.set_param3(sensorInfo.param3);
    sensorInfoTO.set_param4(sensorInfo.param4);
    sensorInfoTO.set_param5(sensorInfo.param5);
    sensorInfoTO.set_param6(sensorInfo.param6);
    sensorInfoTO.set_param7(sensorInfo.param7);
    sensorInfoTO.set_param8(sensorInfo.param8);
    sensorInfoTO.set_param9(sensorInfo.param9);
    sensorInfoTO.set_param10(sensorInfo.param10);
    sensorInfoTO.set_param11(sensorInfo.param11);
    sensorInfoTO.set_param12(sensorInfo.param12);
    sensorInfoTO.set_param13(sensorInfo.param13);
    sensorInfoTO.set_param14(sensorInfo.param14);
    sensorInfoTO.set_param15(sensorInfo.param15);
    sensorInfoTO.set_param16(sensorInfo.param16);
    sensorInfoTO.set_param17(sensorInfo.param17);
    sensorInfoTO.set_param18(sensorInfo.param18);
    sensorInfoTO.set_param19(sensorInfo.param19);
    sensorInfoTO.set_param20(sensorInfo.param20);
    sensorInfoTO.set_param21(sensorInfo.param21);
    sensorInfoTO.set_param22(sensorInfo.param22);
    sensorInfoTO.set_param23(sensorInfo.param23);
    sensorInfoTO.set_param24(sensorInfo.param24);
    sensorInfoTO.set_param25(sensorInfo.param25);
    sensorInfoTO.set_param26(sensorInfo.param26);
    sensorInfoTO.set_param27(sensorInfo.param27);
    sensorInfoTO.set_param28(sensorInfo.param28);
    sensorInfoTO.set_param29(sensorInfo.param29);
    sensorInfoTO.set_param30(sensorInfo.param30);
    sensorInfoTO.set_param31(sensorInfo.param31);
    sensorInfoTO.set_param32(sensorInfo.param32);
    sensorInfoTO.set_param33(sensorInfo.param33);
    sensorInfoTO.set_param34(sensorInfo.param34);
    sensorInfoTO.set_param35(sensorInfo.param35);
    sensorInfoTO.set_param36(sensorInfo.param36);
    sensorInfoTO.set_param37(sensorInfo.param37);
    sensorInfoTO.set_param38(sensorInfo.param38);
    sensorInfoTO.set_param39(sensorInfo.param39);
    sensorInfoTO.set_param40(sensorInfo.param40);
    sensorInfoTO.set_param41(sensorInfo.param41);
    sensorInfoTO.set_param42(sensorInfo.param42);
    sensorInfoTO.set_param43(sensorInfo.param43);
    sensorInfoTO.set_param44(sensorInfo.param44);
    sensorInfoTO.set_param45(sensorInfo.param45);
    sensorInfoTO.set_param46(sensorInfo.param46);
    sensorInfoTO.set_param47(sensorInfo.param47);
    sensorInfoTO.set_param48(sensorInfo.param48);
    sensorInfoTO.set_param49(sensorInfo.param49);
    sensorInfoTO.set_param50(sensorInfo.param50);
    sensorInfoTO.set_param51(sensorInfo.param51);
    sensorInfoTO.set_param52(sensorInfo.param52);
    sensorInfoTO.set_param53(sensorInfo.param53);
    sensorInfoTO.set_param54(sensorInfo.param54);
    sensorInfoTO.set_param55(sensorInfo.param55);
    sensorInfoTO.set_param56(sensorInfo.param56);
    sensorInfoTO.set_param57(sensorInfo.param57);
    sensorInfoTO.set_param58(sensorInfo.param58);
    sensorInfoTO.set_param59(sensorInfo.param59);
    sensorInfoTO.set_param60(sensorInfo.param60);
    sensorInfoTO.set_param61(sensorInfo.param61);
    sensorInfoTO.set_param62(sensorInfo.param62);
    sensorInfoTO.set_param63(sensorInfo.param63);
    sensorInfoTO.set_param64(sensorInfo.param64);
    sensorInfoTO.set_param65(sensorInfo.param65);
    sensorInfoTO.set_param66(sensorInfo.param66);
    sensorInfoTO.set_param67(sensorInfo.param67);
    sensorInfoTO.set_param68(sensorInfo.param68);
    sensorInfoTO.set_param69(sensorInfo.param69);
    sensorInfoTO.set_param70(sensorInfo.param70);
    sensorInfoTO.set_param71(sensorInfo.param71);
    sensorInfoTO.set_param72(sensorInfo.param72);
    sensorInfoTO.set_param73(sensorInfo.param73);
    sensorInfoTO.set_param74(sensorInfo.param74);
    sensorInfoTO.set_param75(sensorInfo.param75);
    sensorInfoTO.set_param76(sensorInfo.param76);
    sensorInfoTO.set_param77(sensorInfo.param77);
    sensorInfoTO.set_param78(sensorInfo.param78);
    sensorInfoTO.set_param79(sensorInfo.param79);
    sensorInfoTO.set_param80(sensorInfo.param80);
    sensorInfoTO.set_param81(sensorInfo.param81);
    sensorInfoTO.set_param82(sensorInfo.param82);
    sensorInfoTO.set_param83(sensorInfo.param83);
    sensorInfoTO.set_param84(sensorInfo.param84);
    sensorInfoTO.set_param85(sensorInfo.param85);
    sensorInfoTO.set_param86(sensorInfo.param86);
    sensorInfoTO.set_param87(sensorInfo.param87);
    sensorInfoTO.set_param88(sensorInfo.param88);
    sensorInfoTO.set_param89(sensorInfo.param89);
    sensorInfoTO.set_param90(sensorInfo.param90);
    sensorInfoTO.set_param91(sensorInfo.param91);
    sensorInfoTO.set_param92(sensorInfo.param92);
    sensorInfoTO.set_param93(sensorInfo.param93);
    sensorInfoTO.set_param94(sensorInfo.param94);
    sensorInfoTO.set_param95(sensorInfo.param95);
    sensorInfoTO.set_param96(sensorInfo.param96);
    sensorInfoTO.set_param97(sensorInfo.param97);
    sensorInfoTO.set_param98(sensorInfo.param98);
    sensorInfoTO.set_param99(sensorInfo.param99);
    sensorInfoTO.set_param100(sensorInfo.param100);
    sensorInfoTO.set_readermeanlow(sensorInfo.readerMeanLow);
    sensorInfoTO.set_readermeanhigh(sensorInfo.readerMeanHigh);
    sensorInfoTO.set_readerdriftlow(sensorInfo.readerDriftLow);
    sensorInfoTO.set_readerdrifthigh(sensorInfo.readerDriftHigh);
    sensorInfoTO.set_readernoiselow(sensorInfo.readerNoiseLow);
    sensorInfoTO.set_readernoisehigh(sensorInfo.readerNoiseHigh);
    sensorInfoTO.set_tminus(sensorInfo.tMinus);
    sensorInfoTO.set_tplus(sensorInfo.tPlus);
    sensorInfoTO.set_postcurvatureweight(sensorInfo.postCurvatureWeight);
    sensorInfoTO.set_bloodpointstoskip(sensorInfo.bloodPointsToSkip);
    sensorInfoTO.set_bloodpointsinwindow(sensorInfo.bloodPointsInWindow);
    sensorInfoTO.set_bloodnoisehigh(sensorInfo.bloodNoiseHigh);
    sensorInfoTO.set_aqpointstoskip(sensorInfo.aqPointsToSkip);
    sensorInfoTO.set_aqpointsinwindow(sensorInfo.aqPointsInWindow);
    sensorInfoTO.set_aqnoisehigh(sensorInfo.aqNoiseHigh);
    sensorInfoTO.set_latebloodpointstoskip(sensorInfo.lateBloodPointsToSkip);
    sensorInfoTO.set_latebloodpointsinwindow(sensorInfo.lateBloodPointsInWindow);
    sensorInfoTO.set_latebloodnoisehigh(sensorInfo.lateBloodNoiseHigh);
    sensorInfoTO.set_lateaqpointstoskip(sensorInfo.lateAqPointsToSkip);
    sensorInfoTO.set_lateaqpointsinwindow(sensorInfo.lateAqPointsInWindow);
    sensorInfoTO.set_lateaqnoisehigh(sensorInfo.lateAqNoiseHigh);
    sensorInfoTO.set_rtpointlimitlow(sensorInfo.rtPointLimitLow);
    sensorInfoTO.set_rtpointlimithigh(sensorInfo.rtPointLimitHigh);
    sensorInfoTO.set_d1low(sensorInfo.d1Low);
    sensorInfoTO.set_d1high(sensorInfo.d1High);
    sensorInfoTO.set_p1d2low(sensorInfo.p1d2Low);
    sensorInfoTO.set_p1d2high(sensorInfo.p1d2High);
    sensorInfoTO.set_p2d2low(sensorInfo.p2d2Low);
    sensorInfoTO.set_p2d2high(sensorInfo.p2d2High);
    sensorInfoTO.set_p3d2low(sensorInfo.p3d2Low);
    sensorInfoTO.set_p3d2high(sensorInfo.p3d2High);
    sensorInfoTO.set_a(sensorInfo.A);
    sensorInfoTO.set_b(sensorInfo.B);
    sensorInfoTO.set_c(sensorInfo.C);
    sensorInfoTO.set_d(sensorInfo.D);
    sensorInfoTO.set_f(sensorInfo.F);
    sensorInfoTO.set_g(sensorInfo.G);
    sensorInfoTO.set_tamboffset(sensorInfo.TAmbOffset);
    sensorInfoTO.set_injectiontimeoffset(sensorInfo.InjectionTimeOffset);
    sensorInfoTO.set_ageoffset(sensorInfo.AgeOffset);
    sensorInfoTO.set_poweroffset(sensorInfo.PowerOffset);
    sensorInfoTO.set_neuralnetblood(sensorInfo.NeuralNetBlood);
    sensorInfoTO.set_neuralnetaq(sensorInfo.NeuralNetAQ);
}

void  AMSerializer::convert(const to::SensorInfo& sensorInfoTO, SensorInfo& sensorInfo)
{
    sensorInfo.channelType = (ChannelType)sensorInfoTO.channeltype();
    sensorInfo.sensorType = (Sensors)sensorInfoTO.sensortype();
    sensorInfo.sensorDescriptorNumber = (uint8_t)sensorInfoTO.sensordescriptornumber();
    sensorInfo.calDelimit = sensorInfoTO.caldelimit();
    sensorInfo.sampleDelimit = sensorInfoTO.sampledelimit();
    sensorInfo.postDelimit = sensorInfoTO.postdelimit();
    sensorInfo.extrapolation = sensorInfoTO.extrapolation();
    sensorInfo.calWindowSize = sensorInfoTO.calwindowsize();
    sensorInfo.sampleWindowSize = sensorInfoTO.samplewindowsize();
    sensorInfo.postWindowSize = sensorInfoTO.postwindowsize();
    sensorInfo.calCurveWeight = sensorInfoTO.calcurveweight();
    sensorInfo.sampleCurveWeight = sensorInfoTO.samplecurveweight();
    sensorInfo.calConcentration = sensorInfoTO.calconcentration();
    sensorInfo.offset = sensorInfoTO.offset();
    sensorInfo.slopeFactor = sensorInfoTO.slopefactor();
    sensorInfo.CalMeanLowQC = sensorInfoTO.calmeanlowqc();
    sensorInfo.CalMeanHighQC = sensorInfoTO.calmeanhighqc();
    sensorInfo.CalDriftLowQC = sensorInfoTO.caldriftlowqc();
    sensorInfo.CalDriftHighQC = sensorInfoTO.caldrifthighqc();
    sensorInfo.CalSecondLowQC = sensorInfoTO.calsecondlowqc();
    sensorInfo.CalSecondHighQC = sensorInfoTO.calsecondhighqc();
    sensorInfo.CalNoiseHighQC = sensorInfoTO.calnoisehighqc();
    sensorInfo.SampleMeanLowQC = sensorInfoTO.samplemeanlowqc();
    sensorInfo.SampleMeanHighQC = sensorInfoTO.samplemeanhighqc();
    sensorInfo.SampleDriftLowQC = sensorInfoTO.sampledriftlowqc();
    sensorInfo.SampleDriftHighQC = sensorInfoTO.sampledrifthighqc();
    sensorInfo.SampleSecondLowQC = sensorInfoTO.samplesecondlowqc();
    sensorInfo.SampleSecondHighQC = sensorInfoTO.samplesecondhighqc();
    sensorInfo.SampleNoiseHighQC = sensorInfoTO.samplenoisehighqc();
    sensorInfo.PostMeanLowQC = sensorInfoTO.postmeanlowqc();
    sensorInfo.PostMeanHighQC = sensorInfoTO.postmeanhighqc();
    sensorInfo.PostDriftLowQC = sensorInfoTO.postdriftlowqc();
    sensorInfo.PostDriftHighQC = sensorInfoTO.postdrifthighqc();
    sensorInfo.PostSecondLowQC = sensorInfoTO.postsecondlowqc();
    sensorInfo.PostSecondHighQC = sensorInfoTO.postsecondhighqc();
    sensorInfo.PostNoiseHighQC = sensorInfoTO.postnoisehighqc();
    sensorInfo.DeltaDriftLowQC = sensorInfoTO.deltadriftlowqc();
    sensorInfo.DeltaDriftHighQC = sensorInfoTO.deltadrifthighqc();
    sensorInfo.param1 = sensorInfoTO.param1();
    sensorInfo.param2 = sensorInfoTO.param2();
    sensorInfo.param3 = sensorInfoTO.param3();
    sensorInfo.param4 = sensorInfoTO.param4();
    sensorInfo.param5 = sensorInfoTO.param5();
    sensorInfo.param6 = sensorInfoTO.param6();
    sensorInfo.param7 = sensorInfoTO.param7();
    sensorInfo.param8 = sensorInfoTO.param8();
    sensorInfo.param9 = sensorInfoTO.param9();
    sensorInfo.param10 = sensorInfoTO.param10();
    sensorInfo.param11 = sensorInfoTO.param11();
    sensorInfo.param12 = sensorInfoTO.param12();
    sensorInfo.param13 = sensorInfoTO.param13();
    sensorInfo.param14 = sensorInfoTO.param14();
    sensorInfo.param15 = sensorInfoTO.param15();
    sensorInfo.param16 = sensorInfoTO.param16();
    sensorInfo.param17 = sensorInfoTO.param17();
    sensorInfo.param18 = sensorInfoTO.param18();
    sensorInfo.param19 = sensorInfoTO.param19();
    sensorInfo.param20 = sensorInfoTO.param20();
    sensorInfo.param21 = sensorInfoTO.param21();
    sensorInfo.param22 = sensorInfoTO.param22();
    sensorInfo.param23 = sensorInfoTO.param23();
    sensorInfo.param24 = sensorInfoTO.param24();
    sensorInfo.param25 = sensorInfoTO.param25();
    sensorInfo.param26 = sensorInfoTO.param26();
    sensorInfo.param27 = sensorInfoTO.param27();
    sensorInfo.param28 = sensorInfoTO.param28();
    sensorInfo.param29 = sensorInfoTO.param29();
    sensorInfo.param30 = sensorInfoTO.param30();
    sensorInfo.param31 = sensorInfoTO.param31();
    sensorInfo.param32 = sensorInfoTO.param32();
    sensorInfo.param33 = sensorInfoTO.param33();
    sensorInfo.param34 = sensorInfoTO.param34();
    sensorInfo.param35 = sensorInfoTO.param35();
    sensorInfo.param36 = sensorInfoTO.param36();
    sensorInfo.param37 = sensorInfoTO.param37();
    sensorInfo.param38 = sensorInfoTO.param38();
    sensorInfo.param39 = sensorInfoTO.param39();
    sensorInfo.param40 = sensorInfoTO.param40();
    sensorInfo.param41 = sensorInfoTO.param41();
    sensorInfo.param42 = sensorInfoTO.param42();
    sensorInfo.param43 = sensorInfoTO.param43();
    sensorInfo.param44 = sensorInfoTO.param44();
    sensorInfo.param45 = sensorInfoTO.param45();
    sensorInfo.param46 = sensorInfoTO.param46();
    sensorInfo.param47 = sensorInfoTO.param47();
    sensorInfo.param48 = sensorInfoTO.param48();
    sensorInfo.param49 = sensorInfoTO.param49();
    sensorInfo.param50 = sensorInfoTO.param50();
    sensorInfo.param51 = sensorInfoTO.param51();
    sensorInfo.param52 = sensorInfoTO.param52();
    sensorInfo.param53 = sensorInfoTO.param53();
    sensorInfo.param54 = sensorInfoTO.param54();
    sensorInfo.param55 = sensorInfoTO.param55();
    sensorInfo.param56 = sensorInfoTO.param56();
    sensorInfo.param57 = sensorInfoTO.param57();
    sensorInfo.param58 = sensorInfoTO.param58();
    sensorInfo.param59 = sensorInfoTO.param59();
    sensorInfo.param60 = sensorInfoTO.param60();
    sensorInfo.param61 = sensorInfoTO.param61();
    sensorInfo.param62 = sensorInfoTO.param62();
    sensorInfo.param63 = sensorInfoTO.param63();
    sensorInfo.param64 = sensorInfoTO.param64();
    sensorInfo.param65 = sensorInfoTO.param65();
    sensorInfo.param66 = sensorInfoTO.param66();
    sensorInfo.param67 = sensorInfoTO.param67();
    sensorInfo.param68 = sensorInfoTO.param68();
    sensorInfo.param69 = sensorInfoTO.param69();
    sensorInfo.param70 = sensorInfoTO.param70();
    sensorInfo.param71 = sensorInfoTO.param71();
    sensorInfo.param72 = sensorInfoTO.param72();
    sensorInfo.param73 = sensorInfoTO.param73();
    sensorInfo.param74 = sensorInfoTO.param74();
    sensorInfo.param75 = sensorInfoTO.param75();
    sensorInfo.param76 = sensorInfoTO.param76();
    sensorInfo.param77 = sensorInfoTO.param77();
    sensorInfo.param78 = sensorInfoTO.param78();
    sensorInfo.param79 = sensorInfoTO.param79();
    sensorInfo.param80 = sensorInfoTO.param80();
    sensorInfo.param81 = sensorInfoTO.param81();
    sensorInfo.param82 = sensorInfoTO.param82();
    sensorInfo.param83 = sensorInfoTO.param83();
    sensorInfo.param84 = sensorInfoTO.param84();
    sensorInfo.param85 = sensorInfoTO.param85();
    sensorInfo.param86 = sensorInfoTO.param86();
    sensorInfo.param87 = sensorInfoTO.param87();
    sensorInfo.param88 = sensorInfoTO.param88();
    sensorInfo.param89 = sensorInfoTO.param89();
    sensorInfo.param90 = sensorInfoTO.param90();
    sensorInfo.param91 = sensorInfoTO.param91();
    sensorInfo.param92 = sensorInfoTO.param92();
    sensorInfo.param93 = sensorInfoTO.param93();
    sensorInfo.param94 = sensorInfoTO.param94();
    sensorInfo.param95 = sensorInfoTO.param95();
    sensorInfo.param96 = sensorInfoTO.param96();
    sensorInfo.param97 = sensorInfoTO.param97();
    sensorInfo.param98 = sensorInfoTO.param98();
    sensorInfo.param99 = sensorInfoTO.param99();
    sensorInfo.param100 = sensorInfoTO.param100();
    sensorInfo.readerMeanLow = sensorInfoTO.readermeanlow();
    sensorInfo.readerMeanHigh = sensorInfoTO.readermeanhigh();
    sensorInfo.readerDriftLow = sensorInfoTO.readerdriftlow();
    sensorInfo.readerDriftHigh = sensorInfoTO.readerdrifthigh();
    sensorInfo.readerNoiseLow = sensorInfoTO.readernoiselow();
    sensorInfo.readerNoiseHigh = sensorInfoTO.readernoisehigh();
    sensorInfo.tMinus = sensorInfoTO.tminus();
    sensorInfo.tPlus = sensorInfoTO.tplus();
    sensorInfo.postCurvatureWeight = sensorInfoTO.postcurvatureweight();
    sensorInfo.bloodPointsToSkip = sensorInfoTO.bloodpointstoskip();
    sensorInfo.bloodPointsInWindow = sensorInfoTO.bloodpointsinwindow();
    sensorInfo.bloodNoiseHigh = sensorInfoTO.bloodnoisehigh();
    sensorInfo.aqPointsToSkip = sensorInfoTO.aqpointstoskip();
    sensorInfo.aqPointsInWindow = sensorInfoTO.aqpointsinwindow();
    sensorInfo.aqNoiseHigh = sensorInfoTO.aqnoisehigh();
    sensorInfo.lateBloodPointsToSkip = sensorInfoTO.latebloodpointstoskip();
    sensorInfo.lateBloodPointsInWindow = sensorInfoTO.latebloodpointsinwindow();
    sensorInfo.lateBloodNoiseHigh = sensorInfoTO.latebloodnoisehigh();
    sensorInfo.lateAqPointsToSkip = sensorInfoTO.lateaqpointstoskip();
    sensorInfo.lateAqPointsInWindow = sensorInfoTO.lateaqpointsinwindow();
    sensorInfo.lateAqNoiseHigh = sensorInfoTO.lateaqnoisehigh();
    sensorInfo.rtPointLimitLow = sensorInfoTO.rtpointlimitlow();
    sensorInfo.rtPointLimitHigh = sensorInfoTO.rtpointlimithigh();
    sensorInfo.d1Low = sensorInfoTO.d1low();
    sensorInfo.d1High = sensorInfoTO.d1high();
    sensorInfo.p1d2Low = sensorInfoTO.p1d2low();
    sensorInfo.p1d2High = sensorInfoTO.p1d2high();
    sensorInfo.p2d2Low = sensorInfoTO.p2d2low();
    sensorInfo.p2d2High = sensorInfoTO.p2d2high();
    sensorInfo.p3d2Low = sensorInfoTO.p3d2low();
    sensorInfo.p3d2High = sensorInfoTO.p3d2high();
    sensorInfo.A = sensorInfoTO.a();
    sensorInfo.B = sensorInfoTO.b();
    sensorInfo.C = sensorInfoTO.c();
    sensorInfo.D = sensorInfoTO.d();
    sensorInfo.F = sensorInfoTO.f();
    sensorInfo.G = sensorInfoTO.g();
    sensorInfo.TAmbOffset = sensorInfoTO.tamboffset();
    sensorInfo.InjectionTimeOffset = sensorInfoTO.injectiontimeoffset();
    sensorInfo.AgeOffset = sensorInfoTO.ageoffset();
    sensorInfo.PowerOffset = sensorInfoTO.poweroffset();
    sensorInfo.NeuralNetBlood = sensorInfoTO.neuralnetblood();
    sensorInfo.NeuralNetAQ = sensorInfoTO.neuralnetaq();
}

void AMSerializer::convert(const Levels& levels, to::Levels& levelsTO)
{
    levelsTO.set_calmean(levels.calMean);
    levelsTO.set_calslope(levels.calSlope);
    levelsTO.set_calnoise(levels.calNoise);
    levelsTO.set_calsecond(levels.calSecond);
    levelsTO.set_calex(levels.calEx);
    levelsTO.set_samplemean(levels.sampleMean);
    levelsTO.set_sampleslope(levels.sampleSlope);
    levelsTO.set_samplenoise(levels.sampleNoise);
    levelsTO.set_samplesecond(levels.sampleSecond);
    levelsTO.set_sampleex(levels.sampleEx);
    levelsTO.set_postmean(levels.postMean);
    levelsTO.set_postslope(levels.postSlope);
    levelsTO.set_postnoise(levels.postNoise);
    levelsTO.set_postsecond(levels.postSecond);
    levelsTO.set_postex(levels.postEx);
    levelsTO.set_additionalmean(levels.additionalMean);
    levelsTO.set_additionalslope(levels.additionalSlope);
    levelsTO.set_additionalnoise(levels.additionalNoise);
    levelsTO.set_additionalsecond(levels.additionalSecond);
    levelsTO.set_additionalex(levels.additionalEx);
    levelsTO.set_additionalfirst(levels.additionalFirst);
    levelsTO.set_additionallast(levels.additionalLast);
    levelsTO.set_peakmean(levels.peakMean);
    levelsTO.set_peakslope(levels.peakSlope);
    levelsTO.set_peaknoise(levels.peakNoise);
    levelsTO.set_peaksecond(levels.peakSecond);
    levelsTO.set_peakex(levels.peakEx);
    levelsTO.set_response(levels.response);
    levelsTO.set_uncorrectedresponse(levels.uncorrectedResponse);
    levelsTO.set_callast(levels.calLast);
    levelsTO.set_calfirst(levels.calFirst);
    levelsTO.set_samplefirst(levels.sampleFirst);
    levelsTO.set_samplelast(levels.sampleLast);
    levelsTO.set_postfirst(levels.postFirst);
    levelsTO.set_postlast(levels.postLast);
    levelsTO.set_peakfirst(levels.peakFirst);
    levelsTO.set_peaklast(levels.peakLast);
    levelsTO.set_calwindowmovedback(levels.calWindowMovedBack);
    levelsTO.set_output1(levels.output1);
    levelsTO.set_output2(levels.output2);
    levelsTO.set_output3(levels.output3);
    levelsTO.set_output4(levels.output4);
    levelsTO.set_output5(levels.output5);
    levelsTO.set_output6(levels.output6);
    levelsTO.set_output7(levels.output7);
    levelsTO.set_output8(levels.output8);
    levelsTO.set_output9(levels.output9);
    levelsTO.set_output10(levels.output10);
    levelsTO.set_output11(levels.output11);
    levelsTO.set_output12(levels.output12);
    levelsTO.set_output13(levels.output13);
    levelsTO.set_output14(levels.output14);
    levelsTO.set_output15(levels.output15);
    levelsTO.set_output16(levels.output16);
    levelsTO.set_output17(levels.output17);
    levelsTO.set_output18(levels.output18);
    levelsTO.set_output19(levels.output19);
    levelsTO.set_output20(levels.output20);
}

void AMSerializer::convert(const to::Levels& levelsTO, Levels& levels)
{
    levels.calMean = levelsTO.calmean();
    levels.calSlope = levelsTO.calslope();
    levels.calNoise = levelsTO.calnoise();
    levels.calSecond = levelsTO.calsecond();
    levels.calEx = levelsTO.calex();
    levels.sampleMean = levelsTO.samplemean();
    levels.sampleSlope = levelsTO.sampleslope();
    levels.sampleNoise = levelsTO.samplenoise();
    levels.sampleSecond = levelsTO.samplesecond();
    levels.sampleEx = levelsTO.sampleex();
    levels.postMean = levelsTO.postmean();
    levels.postSlope = levelsTO.postslope();
    levels.postNoise = levelsTO.postnoise();
    levels.postSecond = levelsTO.postsecond();
    levels.postEx = levelsTO.postex();
    levels.additionalMean = levelsTO.additionalmean();
    levels.additionalSlope = levelsTO.additionalslope();
    levels.additionalNoise = levelsTO.additionalnoise();
    levels.additionalSecond = levelsTO.additionalsecond();
    levels.additionalEx = levelsTO.additionalex();
    levels.additionalFirst = levelsTO.additionalfirst();
    levels.additionalLast = levelsTO.additionallast();
    levels.peakMean = levelsTO.peakmean();
    levels.peakSlope = levelsTO.peakslope();
    levels.peakNoise = levelsTO.peaknoise();
    levels.peakSecond = levelsTO.peaksecond();
    levels.peakEx = levelsTO.peakex();
    levels.response = levelsTO.response();
    levels.uncorrectedResponse = levelsTO.uncorrectedresponse();
    levels.calLast = levelsTO.callast();
    levels.calFirst = levelsTO.calfirst();
    levels.sampleFirst = levelsTO.samplefirst();
    levels.sampleLast = levelsTO.samplelast();
    levels.postFirst = levelsTO.postfirst();
    levels.postLast = levelsTO.postlast();
    levels.peakFirst = levelsTO.peakfirst();
    levels.peakLast = levelsTO.peaklast();
    levels.calWindowMovedBack = levelsTO.calwindowmovedback();
    levels.output1 = levelsTO.output1();
    levels.output2 = levelsTO.output2();
    levels.output3 = levelsTO.output3();
    levels.output4 = levelsTO.output4();
    levels.output5 = levelsTO.output5();
    levels.output6 = levelsTO.output6();
    levels.output7 = levelsTO.output7();
    levels.output8 = levelsTO.output8();
    levels.output9 = levelsTO.output9();
    levels.output10 = levelsTO.output10();
    levels.output11 = levelsTO.output11();
    levels.output12 = levelsTO.output12();
    levels.output13 = levelsTO.output13();
    levels.output14 = levelsTO.output14();
    levels.output15 = levelsTO.output15();
    levels.output16 = levelsTO.output16();
    levels.output17 = levelsTO.output17();
    levels.output18 = levelsTO.output18();
    levels.output19 = levelsTO.output19();
    levels.output20 = levelsTO.output20();
}

void AMSerializer::convert(const Reading& reading, to::Reading& readingTO)
{
    readingTO.set_time(reading.Time);
    readingTO.set_value(reading.Value);
}

// NOTICE:  The .proto for struct Reading represents the Time/Value as 'float'
//          rather than 'double'.  Time and Value are now 'double' in the
//          C++ AnalyticalManager to increase calculation precision, as
//          per discussions with Taha.
//          
//          The C++ AMSerializer.convert() will handle casting the input data
//          from the serialized buffer from float to double for these values.
void AMSerializer::convert(const to::Reading& readingTO, Reading& reading)
{
    reading.Time = readingTO.time();
    reading.Value = readingTO.value();
}

void AMSerializer::convert(const RealTimeQC& realTimeQC, to::RealTimeQC& realTimeQCTO)
{
    realTimeQCTO.set_enabled(realTimeQC.enabled);
    realTimeQCTO.set_starttime(realTimeQC.startTime);
    realTimeQCTO.set_intervaltime(realTimeQC.intervalTime);
    realTimeQCTO.set_type((int)realTimeQC.type);
    realTimeQCTO.set_numpoints(realTimeQC.numPoints);
    realTimeQCTO.set_extra1(realTimeQC.extra1);
    realTimeQCTO.set_extra2(realTimeQC.extra2);
    realTimeQCTO.set_extra3(realTimeQC.extra3);
    realTimeQCTO.set_extra4(realTimeQC.extra4);
    realTimeQCTO.set_extra5(realTimeQC.extra5);
    realTimeQCTO.set_extra6(realTimeQC.extra6);
    realTimeQCTO.set_continueiffailed(realTimeQC.continueIfFailed);
    realTimeQCTO.set_humidityuntil(realTimeQC.humidityUntil);

    for (int i = 0; i < realTimeQC.humidityConfig.size(); i++)
    {
        to::HumidityStruct* humidityConfigTO = realTimeQCTO.add_humidityconfig();
        convert(realTimeQC.humidityConfig[i], *humidityConfigTO);
    }
}

void AMSerializer::convert(const to::RealTimeQC& realTimeQCTO, RealTimeQC& realTimeQC)
{
    realTimeQC.enabled = realTimeQCTO.enabled();
    realTimeQC.startTime = realTimeQCTO.starttime();
    realTimeQC.intervalTime = realTimeQCTO.intervaltime();
    realTimeQC.type = (RealTimeQCType)realTimeQCTO.type();
    realTimeQC.numPoints = realTimeQCTO.numpoints();
    realTimeQC.extra1 = realTimeQCTO.extra1();
    realTimeQC.extra2 = realTimeQCTO.extra2();
    realTimeQC.extra3 = realTimeQCTO.extra3();
    realTimeQC.extra4 = realTimeQCTO.extra4();
    realTimeQC.extra5 = realTimeQCTO.extra5();
    realTimeQC.extra6 = realTimeQCTO.extra6();
    realTimeQC.continueIfFailed = realTimeQCTO.continueiffailed();
    realTimeQC.humidityUntil = realTimeQCTO.humidityuntil();

    realTimeQC.humidityConfig.reserve(realTimeQCTO.humidityconfig_size());

    for (int i = 0; i < realTimeQCTO.humidityconfig_size(); i++)
    {
        HumidityStruct humidityConfig;
        convert(realTimeQCTO.humidityconfig(i), humidityConfig);
        realTimeQC.humidityConfig.push_back(humidityConfig); 
    }
}

void AMSerializer::convert(const SensorReadings& sensorReadings, to::SensorReadings& sensorReadingsTO)
{
    sensorReadingsTO.set_requirementsfailedqc(sensorReadings.requirementsFailedQC);
    sensorReadingsTO.set_returncode((int)sensorReadings.returnCode);
    sensorReadingsTO.set_sensortype((int)sensorReadings.sensorType);
    sensorReadingsTO.set_sensordescriptornumber(sensorReadings.sensorDescriptorNumber);
    sensorReadingsTO.set_channeltype((int)sensorReadings.channelType);
    sensorReadingsTO.set_analyte((int)sensorReadings.analyte);
    sensorReadingsTO.set_analytestring(sensorReadings.analyteString);

    //sensorReadingsTO.set_repeated reading readings(sensorReadings.repeated Reading readings);
    for (int i = 0; i < sensorReadings.readings->size(); i++)
    {
        to::Reading* readingTO = sensorReadingsTO.add_readings();
        convert((*(sensorReadings.readings))[i], *readingTO);
    }

    sensorReadingsTO.set_readingpointer(sensorReadings.readingPointer);
    sensorReadingsTO.set_numthistypereading(sensorReadings.numThisTypeReading);
    sensorReadingsTO.set_result(sensorReadings.result);
    sensorReadingsTO.set_multiplicationfactor(sensorReadings.multiplicationFactor);

    //sensorReadingsTO.set_sensorinfo sensordescriptor(sensorReadings.SensorInfo sensorDescriptor);
    if (sensorReadings.sensorDescriptor != nullptr) {
        convert(*(sensorReadings.sensorDescriptor), *(sensorReadingsTO.mutable_sensordescriptor()));
    }
    
    sensorReadingsTO.set_realtimeqcpassed((int)sensorReadings.realTimeQCPassed);
    sensorReadingsTO.set_realtimeqcfailedever((int)sensorReadings.realTimeQCFailedEver);
    sensorReadingsTO.set_checkrealtimeqc(sensorReadings.checkRealtimeQC);
    sensorReadingsTO.set_reportablelow(sensorReadings.reportableLow);
    sensorReadingsTO.set_reportablehigh(sensorReadings.reportableHigh);

    //sensorReadingsTO.set_levels levels(sensorReadings.Levels levels);
    if (sensorReadings.levels != nullptr) {
        convert(*(sensorReadings.levels), *(sensorReadingsTO.mutable_levels()));
    }

    sensorReadingsTO.set_humiditypassed((int)sensorReadings.humidityPassed);
    sensorReadingsTO.set_insanitylow(sensorReadings.insanityLow);
    sensorReadingsTO.set_insanityhigh(sensorReadings.insanityHigh);
    sensorReadingsTO.set_insanityqalow(sensorReadings.insanityQALow);
    sensorReadingsTO.set_insanityqahigh(sensorReadings.insanityQAHigh);
    sensorReadingsTO.set_realtimeqcfailuretotal(sensorReadings.RealTimeQCFailureTotal);
    sensorReadingsTO.set_humidityqcfailuretotal(sensorReadings.HumidityQCFailureTotal);
    sensorReadingsTO.set_airqcfailuretotal(sensorReadings.AirQCFailureTotal);
    sensorReadingsTO.set_realtimeqcfailureoccurancestring(sensorReadings.RealTimeQCFailureOccuranceString);
    sensorReadingsTO.set_humidityqcfailureoccurancestring(sensorReadings.HumidityQCFailureOccuranceString);
    sensorReadingsTO.set_airqcfailureoccurancestring(sensorReadings.AirQCFailureOccuranceString);
    sensorReadingsTO.set_extrastring(sensorReadings.extraString);
    sensorReadingsTO.set_resultstring(sensorReadings.resultString);
}

void  AMSerializer::convert(const to::SensorReadings& sensorReadingsTO, SensorReadings& sensorReadings)
{
    // input check: sensorReadings has 3 pointers. check if they are initialized
    if (sensorReadings.readings == nullptr) {
        sensorReadings.readings = std::make_shared<std::vector<Reading>>();
    }
    if (sensorReadings.levels == nullptr) {
        sensorReadings.levels = std::make_shared<Levels>();
    }
    if (sensorReadings.sensorDescriptor == nullptr) {
        sensorReadings.sensorDescriptor = std::make_shared<SensorInfo>();
    }

    sensorReadings.requirementsFailedQC = sensorReadingsTO.requirementsfailedqc();
    sensorReadings.returnCode = (ResultsCalcReturnCode)sensorReadingsTO.returncode();
    sensorReadings.sensorType = (Sensors)sensorReadingsTO.sensortype();
    sensorReadings.sensorDescriptorNumber = (uint8_t)sensorReadingsTO.sensordescriptornumber();
    sensorReadings.channelType = (ChannelType)sensorReadingsTO.channeltype();
    sensorReadings.analyte = (Analytes)sensorReadingsTO.analyte();
    sensorReadings.analyteString = sensorReadingsTO.analytestring();

    sensorReadings.readings->reserve(sensorReadingsTO.readings_size());

    //sensorReadings.repeated Reading readings = sensorReadingsTO.repeated reading readings();
    for (int i = 0; i < sensorReadingsTO.readings_size(); i++)
    {
        Reading reading;
        convert(sensorReadingsTO.readings(i), reading);
        sensorReadings.readings->push_back(reading); // object is copied over
    }

    sensorReadings.readingPointer = sensorReadingsTO.readingpointer();
    sensorReadings.numThisTypeReading = sensorReadingsTO.numthistypereading();
    sensorReadings.result = sensorReadingsTO.result();
    sensorReadings.multiplicationFactor = sensorReadingsTO.multiplicationfactor();

    //sensorReadings.SensorInfo sensorDescriptor = sensorReadingsTO.sensorinfo sensordescriptor();
    if (sensorReadingsTO.has_sensordescriptor()) {
        convert(sensorReadingsTO.sensordescriptor(), *(sensorReadings.sensorDescriptor));
    } else {
        sensorReadings.sensorDescriptor = nullptr;
    }

    sensorReadings.realTimeQCPassed = (RealTimeQCReturnCode)sensorReadingsTO.realtimeqcpassed();
    sensorReadings.realTimeQCFailedEver = (RealTimeQCReturnCode)sensorReadingsTO.realtimeqcfailedever();
    sensorReadings.checkRealtimeQC = sensorReadingsTO.checkrealtimeqc();
    sensorReadings.reportableLow = sensorReadingsTO.reportablelow();
    sensorReadings.reportableHigh = sensorReadingsTO.reportablehigh();

    //sensorReadings.Levels levels = sensorReadingsTO.levels levels();
    if (sensorReadingsTO.has_levels()) {
        convert(sensorReadingsTO.levels(), *(sensorReadings.levels));
    } else {
        sensorReadings.levels = nullptr;
    }

    sensorReadings.humidityPassed = (HumidityReturnCode)sensorReadingsTO.humiditypassed();
    sensorReadings.insanityLow = sensorReadingsTO.insanitylow();
    sensorReadings.insanityHigh = sensorReadingsTO.insanityhigh();
    sensorReadings.insanityQALow = sensorReadingsTO.insanityqalow();
    sensorReadings.insanityQAHigh = sensorReadingsTO.insanityqahigh();
    sensorReadings.RealTimeQCFailureTotal = sensorReadingsTO.realtimeqcfailuretotal();
    sensorReadings.HumidityQCFailureTotal = sensorReadingsTO.humidityqcfailuretotal();
    sensorReadings.AirQCFailureTotal = sensorReadingsTO.airqcfailuretotal();
    sensorReadings.RealTimeQCFailureOccuranceString = sensorReadingsTO.realtimeqcfailureoccurancestring();
    sensorReadings.HumidityQCFailureOccuranceString = sensorReadingsTO.humidityqcfailureoccurancestring();
    sensorReadings.AirQCFailureOccuranceString = sensorReadingsTO.airqcfailureoccurancestring();
    sensorReadings.extraString = sensorReadingsTO.extrastring();
    sensorReadings.resultString = sensorReadingsTO.resultstring();
}

bool AMSerializer::convert(const AnalyticalManager::BGEParameters& bgeParameters, to::BGEParameters& bgeParametersTO)
{
    double paramDouble;
    float paramFloat;
    bool   paramBool;
    int    paramInt;
    int    count = 0;

    bgeParametersTO.set_count(bgeParameters.Count());
    if (bgeParameters.Count() == 0) return true; // nothing to copy

    if (bgeParameters.GetParam(0, &paramDouble))
    {
        bgeParametersTO.set_parameter0(paramDouble);
        count++;
    }

    if (bgeParameters.GetParam(1, &paramDouble))
    {
        bgeParametersTO.set_parameter1(paramDouble);
        count++;
    }

    if (bgeParameters.GetParam(2, &paramFloat))
    {
        bgeParametersTO.set_parameter2(paramFloat);
        count++;
    }

    if (bgeParameters.GetParam(3, &paramFloat))
    {
        bgeParametersTO.set_parameter3(paramFloat);
        count++;
    }

    if (bgeParameters.GetParam(4, &paramBool))
    {
        bgeParametersTO.set_parameter4(paramBool);
        count++;
    }

    FluidType parameter5;
    if (bgeParameters.GetParam(5, &parameter5))
    {
        bgeParametersTO.set_parameter5((int)parameter5);
        count++;
    }

    std::shared_ptr<SensorReadings> parameter6; // from sensorReadings
    if (bgeParameters.GetParam(6, &parameter6) && (parameter6 != nullptr))
    {
        convert(*parameter6, *(bgeParametersTO.mutable_parameter6()));
        count++;
    }

    if (bgeParameters.GetParam(7, &paramInt)) 
    {
        bgeParametersTO.set_parameter7(paramInt);
        count++;
    }

    if (bgeParameters.GetParam(8, &paramBool)) 
    {
        bgeParametersTO.set_parameter8(paramBool);
        count++;
    }

    if (bgeParameters.GetParam(9, &paramDouble))
    {
        bgeParametersTO.set_parameter9(paramDouble);
        count++;
    }

    if (bgeParameters.GetParam(10, &paramInt)) 
    {
        bgeParametersTO.set_parameter10(paramInt);
        count++;
    }

    if (bgeParameters.GetParam(11, &paramDouble))
    {
        bgeParametersTO.set_parameter11(paramDouble);
        count++;
    }

    if (bgeParameters.GetParam(12, &paramDouble))
    {
        bgeParametersTO.set_parameter12(paramDouble);
        count++;
    }

    if (bgeParameters.GetParam(13, &paramDouble))
    {
        bgeParametersTO.set_parameter13(paramDouble);
        count++;
    }

    if (bgeParameters.GetParam(14, &paramDouble))
    {
        bgeParametersTO.set_parameter14(paramDouble);
        count++;
    }

    if (bgeParameters.GetParam(15, &paramBool)) {
        bgeParametersTO.set_parameter15(paramBool);
        count++;
    }

    if (bgeParameters.GetParam(16, &paramBool)) {
        bgeParametersTO.set_parameter16(paramBool);
        count++;
    }

    // return result (if the count does not match, there was an error in GetParam)
    return (count == bgeParameters.Count());
}

bool AMSerializer::convert(const to::BGEParameters& bgeParametersTO, AnalyticalManager::BGEParameters& bgeParameters)
{
    bool result = true;
    int itemCount = bgeParametersTO.count();

    if (itemCount == 0) return result; // nothing to copy
    if (bgeParameters.Count() != 0) 
    {
        // we expect an empty new bgeParameters. return an error
        result = false;
    }

    if (result && (bgeParameters.Count() < itemCount)) {
        result = bgeParameters.AddParam(bgeParametersTO.parameter0());
    }

    if (result && (bgeParameters.Count() < itemCount)) {
        result = bgeParameters.AddParam(bgeParametersTO.parameter1());
    }

    if (result && (bgeParameters.Count() < itemCount)) {
        result = bgeParameters.AddParam(bgeParametersTO.parameter2());
    }

    if (result && (bgeParameters.Count() < itemCount)) {
        result = bgeParameters.AddParam(bgeParametersTO.parameter3());
    }

    if (result && (bgeParameters.Count() < itemCount)) {
        result = bgeParameters.AddParam(bgeParametersTO.parameter4());
    }

    if (result && (bgeParameters.Count() < itemCount)) {
        result = bgeParameters.AddParam((FluidType)(bgeParametersTO.parameter5()));
    }

    //bgeParameters.SensorReadings parameter6 = bgeParametersTO.sensorreadings parameter6();
    if (result && (bgeParameters.Count() < itemCount)) {
        std::shared_ptr<SensorReadings> parameter6 = std::make_shared<SensorReadings>();
        convert(bgeParametersTO.parameter6(), *parameter6);
        result = bgeParameters.AddParam(parameter6);
    }

    if (result && (bgeParameters.Count() < itemCount)) {
        result = bgeParameters.AddParam(bgeParametersTO.parameter7());
    }

    if (result && (bgeParameters.Count() < itemCount)) {
        result = bgeParameters.AddParam(bgeParametersTO.parameter8());
    }

    if (result && (bgeParameters.Count() < itemCount)) {
        result = bgeParameters.AddParam(bgeParametersTO.parameter9());
    }

    if (result && (bgeParameters.Count() < itemCount)) {
        result = bgeParameters.AddParam(bgeParametersTO.parameter10());
    }

    if (result && (bgeParameters.Count() < itemCount)) {
        result = bgeParameters.AddParam(bgeParametersTO.parameter11());
    }

    if (result && (bgeParameters.Count() < itemCount)) {
        result = bgeParameters.AddParam(bgeParametersTO.parameter12());
    }

    if (result && (bgeParameters.Count() < itemCount)) {
        result = bgeParameters.AddParam(bgeParametersTO.parameter13());
    }

    if (result && (bgeParameters.Count() < itemCount)) {
        result = bgeParameters.AddParam(bgeParametersTO.parameter14());
    }

    if (result && (bgeParameters.Count() < itemCount)) {
        result = bgeParameters.AddParam(bgeParametersTO.parameter15());
    }

    if (result && (bgeParameters.Count() < itemCount)) {
        result = bgeParameters.AddParam(bgeParametersTO.parameter16());
    }

    return result;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////
// Utility methods to serialize & de-serialize data to/from byte-arrays/Transfer Objects
///////////////////////////////////////////////////////////////////////////////////////////////////////

LibraryCallReturnCode AMSerializer::deserializeCalculateBGERequest(
    const char serializedInputData[], 
    const int inputDataSize, 
    std::vector<std::shared_ptr<SensorReadings>>& sensorReadings, 
    AnalyticalManager::BGEParameters& bgeParameters,
    bool& allowNegativeValues,
    std::string& errorMessage)
{
    ::to::CalculateBGERequest calculateBGERequestTO;
    if (!calculateBGERequestTO.ParseFromArray(serializedInputData, inputDataSize)) {
        // ERROR:
        errorMessage = AMSerializerHelper::formatErrorMessage(
            AMSerializerHelper::ERROR_MESSAGE_PROTOBUFDECODE_REQUEST,
            "AMSerializer::deserializeCalculateBGERequest", __LINE__);
        return LibraryCallReturnCode::AM_CPP_PROTOBUF_DECODING_FAILED_DESERIALIZE;
    }

    sensorReadings.reserve(calculateBGERequestTO.sensorreadings_size());

    // Copy vector<SensorReadings> out
    for (int i = 0; i < calculateBGERequestTO.sensorreadings_size(); i++)
    {
        std::shared_ptr<SensorReadings> toSensorReadings = std::make_shared<SensorReadings>();
        convert(calculateBGERequestTO.sensorreadings(i), *toSensorReadings);
        sensorReadings.push_back(toSensorReadings); // object is copied over
    }

    // copy BGEParameters out
    if (calculateBGERequestTO.has_params()) 
    {
        if (!convert(calculateBGERequestTO.params(), bgeParameters)) {
            std::stringstream ss;
            ss << "BGEParameterTO to BGEParameter convert failed at count=" << bgeParameters.Count();
            std::string tmpString = ss.str();
            errorMessage = AMSerializerHelper::formatErrorMessage(
                tmpString.c_str(),
                "AMSerializer::deserializeCalculateBGERequest", __LINE__);
            return LibraryCallReturnCode::AM_CPP_PROTOBUF_DECODING_MISSING_INPUT_PARAMETER;
        }
    }
    else 
    {
        std::string tmpString = std::string(AMSerializerHelper::ERROR_MESSAGE_MISSING_INPUT_PARAMETER) + " BGEParameter is NULL";
        errorMessage = AMSerializerHelper::formatErrorMessage(
            tmpString.c_str(),
            "AMSerializer::deserializeCalculateBGERequest", __LINE__);
        return LibraryCallReturnCode::AM_CPP_PROTOBUF_DECODING_MISSING_INPUT_PARAMETER;
    }

    allowNegativeValues = calculateBGERequestTO.allownegativevalues();

    return LibraryCallReturnCode::SUCCESS;
}

LibraryCallReturnCode AMSerializer::serializeCalculateBGEResponse(
    const std::vector<std::shared_ptr<SensorReadings>>& sensorReadings, 
    const AnalyticalManager::BGEParameters& bgeParameters, 
    char*& serializedResponseData, 
    int& serializedInputDataSize,
    std::string& errorMessage)
{
    ::to::CalculateBGEResponse calculateBGEResponseTO;

    calculateBGEResponseTO.set_errorcode((int)LibraryCallReturnCode::SUCCESS);

    for (int i = 0; i < sensorReadings.size(); i++) {
        ::to::SensorReadings* toSensorReadingsTO = calculateBGEResponseTO.add_sensorreadings(); // to
        convert(*(sensorReadings[i]), *toSensorReadingsTO);
    }

    if (!convert(bgeParameters, *(calculateBGEResponseTO.mutable_params()))) {
        std::stringstream ss;
        ss << "BGEParameter to BGEParameterTO convert failed at count=" << calculateBGEResponseTO.params().count();
        std::string tmpString = ss.str();
        errorMessage = AMSerializerHelper::formatErrorMessage(
            tmpString.c_str(),
            "AMSerializer::serializeCalculateBGEResponse", __LINE__);
        return LibraryCallReturnCode::AM_CPP_PROTOBUF_DECODING_MISSING_INPUT_PARAMETER;
    }


    serializedInputDataSize = calculateBGEResponseTO.ByteSize();
    serializedResponseData = new char[serializedInputDataSize];
    // Caller of this method calls "freeMemory" to delete array.

    if (calculateBGEResponseTO.SerializeToArray((void*)serializedResponseData, serializedInputDataSize))
    {
        return LibraryCallReturnCode::SUCCESS;
    }
    else
    {
        AMSerializerHelper::freeMemory(serializedResponseData);
        errorMessage = AMSerializerHelper::formatErrorMessage(
            AMSerializerHelper::ERROR_MESSAGE_PROTOBUFENCODE_RESPONSE,
            "AMSerializer::serializeCalculateBGEResponse", __LINE__);
        return LibraryCallReturnCode::AM_CPP_PROTOBUF_ENCODING_FAILED_SERIALIZE;
    }
}


LibraryCallReturnCode AMSerializer::deserializeCheckForEarlyInjectionRequest(
    const char serializedInputData[],
    const int inputDataSize,
    std::shared_ptr<SensorReadings>& hematocritReadings,
    std::shared_ptr<SensorReadings>& topHeaterReadings,
    RealTimeHematocritQCReturnCode& previousReturnCode,
    double& airAfterFluidThreshold,
    float& lastRecordedTime,
    double& firstFluid,
    std::string& errorMessage)
{
    // 1. Input check
    if ((hematocritReadings == nullptr) || 
        (topHeaterReadings == nullptr))
    {
        errorMessage = AMSerializerHelper::formatErrorMessage(
            "Invalid Function parameters found. hematocritReadings or topHeaterReadings is nullptr",
            "AMSerializer::deserializeCheckForEarlyInjectionRequest", __LINE__);
        return LibraryCallReturnCode::UNEXPECTED_ERROR; 
    }

    // 2. Protobuf decode
    ::to::CheckForEarlyInjectionRequest checkForEarlyInjectionRequestTO;

    if (!checkForEarlyInjectionRequestTO.ParseFromArray(serializedInputData, inputDataSize)) {
        errorMessage = AMSerializerHelper::formatErrorMessage(
            AMSerializerHelper::ERROR_MESSAGE_PROTOBUFDECODE_REQUEST,
            "AMSerializer::deserializeCheckForEarlyInjectionRequest", __LINE__);
        return LibraryCallReturnCode::AM_CPP_PROTOBUF_DECODING_FAILED_DESERIALIZE;
    }

    // 3. Convert Transfer Object to OUT parameters
    if (checkForEarlyInjectionRequestTO.has_hematocritreadings()) 
    {
        convert(checkForEarlyInjectionRequestTO.hematocritreadings(), *hematocritReadings);
    }
    else 
    {
        std::string tmpString = std::string(AMSerializerHelper::ERROR_MESSAGE_MISSING_INPUT_PARAMETER) + " hematocritreadings is NULL";
        errorMessage = AMSerializerHelper::formatErrorMessage(
            tmpString.c_str(),
            "AMSerializer::deserializeCheckForEarlyInjectionRequest", __LINE__);
        return LibraryCallReturnCode::AM_CPP_PROTOBUF_DECODING_MISSING_INPUT_PARAMETER;
    }

    if (checkForEarlyInjectionRequestTO.has_topheaterreadings()) 
    {
        convert(checkForEarlyInjectionRequestTO.topheaterreadings(), *topHeaterReadings);
    }
    else 
    {
        std::string tmpString = std::string(AMSerializerHelper::ERROR_MESSAGE_MISSING_INPUT_PARAMETER) + " topheaterreadings is NULL";
        errorMessage = AMSerializerHelper::formatErrorMessage(
            tmpString.c_str(),
            "AMSerializer::deserializeCheckForEarlyInjectionRequest", __LINE__);
        return LibraryCallReturnCode::AM_CPP_PROTOBUF_DECODING_MISSING_INPUT_PARAMETER;
    }

    previousReturnCode = (RealTimeHematocritQCReturnCode)checkForEarlyInjectionRequestTO.previousreturncode();
    airAfterFluidThreshold = checkForEarlyInjectionRequestTO.airafterfluidthreshold();
    lastRecordedTime = checkForEarlyInjectionRequestTO.lastrecordedtime();
    firstFluid = checkForEarlyInjectionRequestTO.firstfluid();

    return LibraryCallReturnCode::SUCCESS;
}

// CheckForEarlyInjectionResponse
LibraryCallReturnCode AMSerializer::serializeCheckForEarlyInjectionResponse(
    const std::shared_ptr<SensorReadings>& hematocritReadings,
    const RealTimeHematocritQCReturnCode amRC,
    char*& serializedResponseData,
    int& serializedInputDataSize,
    std::string& errorMessage)
{
    // 1. Input check
    if (hematocritReadings == nullptr) {
        errorMessage = AMSerializerHelper::formatErrorMessage(
            "Invalid Function parameters found. hematocritReadings is nullptr",
            "AMSerializer::serializeCheckForEarlyInjectionResponse", __LINE__);
        return LibraryCallReturnCode::UNEXPECTED_ERROR; // ptr not initialized
    }

    // 2. Convert IN parameters to Transfer Object
    ::to::CheckForEarlyInjectionResponse checkForEarlyInjectionResponseTO;

    checkForEarlyInjectionResponseTO.set_errorcode((int)LibraryCallReturnCode::SUCCESS);

    convert(*hematocritReadings, *(checkForEarlyInjectionResponseTO.mutable_hematocritreadings()));
    checkForEarlyInjectionResponseTO.set_amreturncode((int)amRC);

    // 3. Allocate memory for char[] to hold protobuf encoded byte array
    serializedInputDataSize = checkForEarlyInjectionResponseTO.ByteSize();
    serializedResponseData = new char[serializedInputDataSize];
    // Caller of this method calls "freeMemory" to delete array.


    // 4. Protobuf encode
    if (checkForEarlyInjectionResponseTO.SerializeToArray((void*)serializedResponseData, serializedInputDataSize))
    {
        return LibraryCallReturnCode::SUCCESS;
    }
    else
    {
        AMSerializerHelper::freeMemory(serializedResponseData);
        errorMessage = AMSerializerHelper::formatErrorMessage(
            AMSerializerHelper::ERROR_MESSAGE_PROTOBUFENCODE_RESPONSE,
            "AMSerializer::serializeCheckForEarlyInjectionResponse", __LINE__);
        return LibraryCallReturnCode::AM_CPP_PROTOBUF_ENCODING_FAILED_SERIALIZE;
    }
}


LibraryCallReturnCode AMSerializer::deserializeComputeCalculatedResultsRequest(
    const char serializedInputData[],
    const int inputDataSize,
    std::vector<FinalResult>& measuredResults,
    std::vector<FinalResult>& calculatedResults,
    double& passedctHb,
    double& FiO2,
    double& temperature,
    double& pressure,
    TestMode& testMode,
    double& patientAge,
    Gender& gender,
    eGFRFormula& egfrFormula,
    double& patientHeight,
    AgeCategory& ageCategory,
    double& RQ,
    bool& calculateAlveolar,
    bool& theApplymTCO2,
    std::string& errorMessage)
{
    // 1. Input check

    // 2. Protobuf decode
    ::to::ComputeCalculatedResultsRequest computeCalculatedResultsRequestTO;

    if (!computeCalculatedResultsRequestTO.ParseFromArray(serializedInputData, inputDataSize)) {
        errorMessage = AMSerializerHelper::formatErrorMessage(
            AMSerializerHelper::ERROR_MESSAGE_PROTOBUFDECODE_REQUEST,
            "AMSerializer::deserializeComputeCalculatedResultsRequest", __LINE__);
        return LibraryCallReturnCode::AM_CPP_PROTOBUF_DECODING_FAILED_DESERIALIZE;
    }

    // 3. Convert Transfer Object to OUT parameters

    measuredResults.reserve(computeCalculatedResultsRequestTO.measuredresults_size());

    //repeated FinalResult measuredResults = computeCalculatedResultsRequestTO.repeated finalresult measuredresults();
    for (int i = 0; i < computeCalculatedResultsRequestTO.measuredresults_size(); i++)
    {
        FinalResult toFinalResult;
        convert(computeCalculatedResultsRequestTO.measuredresults(i), toFinalResult);
        measuredResults.push_back(toFinalResult); // object is copied over
    }

    calculatedResults.reserve(computeCalculatedResultsRequestTO.calculatedresults_size());

    //repeated FinalResult calculatedResults = computeCalculatedResultsRequestTO.repeated finalresult calculatedresults();
    for (int i = 0; i < computeCalculatedResultsRequestTO.calculatedresults_size(); i++)
    {
        FinalResult toFinalResult;
        convert(computeCalculatedResultsRequestTO.calculatedresults(i), toFinalResult);
        calculatedResults.push_back(toFinalResult); // object is copied over
    }

    passedctHb = computeCalculatedResultsRequestTO.passedcthb();
    FiO2 = computeCalculatedResultsRequestTO.fio2();
    temperature = computeCalculatedResultsRequestTO.temperature();
    pressure = computeCalculatedResultsRequestTO.pressure();
    testMode = (TestMode)computeCalculatedResultsRequestTO.testmode();
    patientAge = computeCalculatedResultsRequestTO.patientage();
    gender = (Gender)computeCalculatedResultsRequestTO.gender();
    egfrFormula = (eGFRFormula)computeCalculatedResultsRequestTO.egfrformula();
    patientHeight = computeCalculatedResultsRequestTO.patientheight();
    ageCategory = (AgeCategory)computeCalculatedResultsRequestTO.agecategory();
    RQ = computeCalculatedResultsRequestTO.rq();
    calculateAlveolar = computeCalculatedResultsRequestTO.calculatealveolar();
    theApplymTCO2 = computeCalculatedResultsRequestTO.theapplymtco2();

    return LibraryCallReturnCode::SUCCESS;
}

LibraryCallReturnCode AMSerializer::serializeComputeCalculatedResultsResponse(
    const std::vector<FinalResult>& calculatedResults,
    char*& serializedResponseData,
    int& serializedInputDataSize,
    std::string& errorMessage)
{
    // 1. Input check

    // 2. Convert IN parameters to Transfer Object
    ::to::ComputeCalculatedResultsResponse computeCalculatedResultsResponseTO;

    computeCalculatedResultsResponseTO.set_errorcode((int)LibraryCallReturnCode::SUCCESS);

    for (int i = 0; i < calculatedResults.size(); i++) {
        ::to::FinalResult* toFinalResultTO = computeCalculatedResultsResponseTO.add_calculatedresults(); // to
        convert(calculatedResults[i], *toFinalResultTO);
    }

    // 3. Allocate memory for char[] to hold protobuf encoded byte array
    serializedInputDataSize = computeCalculatedResultsResponseTO.ByteSize();
    serializedResponseData = new char[serializedInputDataSize];
    // Caller of this method calls "freeMemory" to delete array.

    // 4. Protobuf encode
    if (computeCalculatedResultsResponseTO.SerializeToArray((void*)serializedResponseData, serializedInputDataSize))
    {
        return LibraryCallReturnCode::SUCCESS;
    }
    else
    {
        AMSerializerHelper::freeMemory(serializedResponseData);
        errorMessage = AMSerializerHelper::formatErrorMessage(
            AMSerializerHelper::ERROR_MESSAGE_PROTOBUFENCODE_RESPONSE,
            "AMSerializer::serializeComputeCalculatedResultsResponse", __LINE__);
        return LibraryCallReturnCode::AM_CPP_PROTOBUF_ENCODING_FAILED_SERIALIZE;
    }
}

LibraryCallReturnCode AMSerializer::deserializeComputeCorrectedResultsRequest(
    const char serializedInputData[],
    const int inputDataSize,
    std::vector<FinalResult>& measuredResults,
    std::vector<FinalResult>& correctedResults,
    double& temperature,
    double& pressure,
    double& FiO2,
    double& RQ,
    bool& calculateAlveolar,
    std::string& errorMessage)
{
    // 1. Input check
    if (measuredResults.size() != 0) {
        measuredResults.clear();
    }
    if (correctedResults.size() != 0) {
        correctedResults.clear();
    }

    // 2. Protobuf decode
    ::to::ComputeCorrectedResultsRequest computeCorrectedResultsRequestTO;

    if (!computeCorrectedResultsRequestTO.ParseFromArray(serializedInputData, inputDataSize)) {
        errorMessage = AMSerializerHelper::formatErrorMessage(
            AMSerializerHelper::ERROR_MESSAGE_PROTOBUFDECODE_REQUEST,
            "AMSerializer::deserializeComputeCorrectedResultsRequest", __LINE__);
        return LibraryCallReturnCode::AM_CPP_PROTOBUF_DECODING_FAILED_DESERIALIZE;
    }

    // 3. Convert Transfer Object to OUT parameters

    measuredResults.reserve(computeCorrectedResultsRequestTO.measuredresults_size());

    //repeated FinalResult measuredResults = computeCorrectedResultsRequestTO.repeated finalresult measuredresults();
    for (int i = 0; i < computeCorrectedResultsRequestTO.measuredresults_size(); i++)
    {
        FinalResult toFinalResult;
        convert(computeCorrectedResultsRequestTO.measuredresults(i), toFinalResult);
        measuredResults.push_back(toFinalResult); // object is copied over
    }

    correctedResults.reserve(computeCorrectedResultsRequestTO.correctedresults_size());

    //repeated FinalResult correctedResults = computeCorrectedResultsRequestTO.repeated finalresult correctedresults();
    for (int i = 0; i < computeCorrectedResultsRequestTO.correctedresults_size(); i++)
    {
        FinalResult toFinalResult;
        convert(computeCorrectedResultsRequestTO.correctedresults(i), toFinalResult);
        correctedResults.push_back(toFinalResult); // object is copied over
    }

    temperature = computeCorrectedResultsRequestTO.temperature();
    pressure = computeCorrectedResultsRequestTO.pressure();
    FiO2 = computeCorrectedResultsRequestTO.fio2();
    RQ = computeCorrectedResultsRequestTO.rq();
    calculateAlveolar = computeCorrectedResultsRequestTO.calculatealveolar();

    return LibraryCallReturnCode::SUCCESS;
}


LibraryCallReturnCode AMSerializer::serializeComputeCorrectedResultsResponse(
    const std::vector<FinalResult>& correctedResults,
    char*& serializedResponseData,
    int& serializedInputDataSize,
    std::string& errorMessage)
{
    // 1. Input check

    // 2. Convert IN parameters to Transfer Object
    ::to::ComputeCorrectedResultsResponse computeCorrectedResultsResponseTO;

    computeCorrectedResultsResponseTO.set_errorcode((int)LibraryCallReturnCode::SUCCESS);

    for (int i = 0; i < correctedResults.size(); i++) {
        ::to::FinalResult* finalResultTO = computeCorrectedResultsResponseTO.add_correctedresults(); // to
        convert(correctedResults[i], *finalResultTO);
    }

    // 3. Allocate memory for char[] to hold protobuf encoded byte array
    serializedInputDataSize = computeCorrectedResultsResponseTO.ByteSize();
    serializedResponseData = new char[serializedInputDataSize];
    // Caller of this method calls "freeMemory" to delete array.


    // 4. Protobuf encode
    if (computeCorrectedResultsResponseTO.SerializeToArray((void*)serializedResponseData, serializedInputDataSize))
    {
        return LibraryCallReturnCode::SUCCESS;
    }
    else
    {
        AMSerializerHelper::freeMemory(serializedResponseData);
        errorMessage = AMSerializerHelper::formatErrorMessage(
            AMSerializerHelper::ERROR_MESSAGE_PROTOBUFENCODE_RESPONSE,
            "AMSerializer::serializeComputeCorrectedResultsResponse", __LINE__);
        return LibraryCallReturnCode::AM_CPP_PROTOBUF_ENCODING_FAILED_SERIALIZE;
    }
}

LibraryCallReturnCode AMSerializer::deserializePerformRealTimeQCRequest(
    const char serializedInputData[],
    const int inputDataSize,
    std::vector<std::shared_ptr<SensorReadings>>& testReadings,
    RealTimeQC& qcStruct,
    float& lastRecordedTime,
    std::string& errorMessage)
{
    // 1. Input check
    if (testReadings.size() != 0) {
        testReadings.clear();
    }

    // 2. Protobuf decode
    ::to::PerformRealTimeQCRequest performRealTimeQCRequestTO;

    if (!performRealTimeQCRequestTO.ParseFromArray(serializedInputData, inputDataSize)) {
        errorMessage = AMSerializerHelper::formatErrorMessage(
            AMSerializerHelper::ERROR_MESSAGE_PROTOBUFDECODE_REQUEST,
            "AMSerializer::deserializePerformRealTimeQCRequest", __LINE__);
        return LibraryCallReturnCode::AM_CPP_PROTOBUF_DECODING_FAILED_DESERIALIZE;
    }

    // 3. Convert Transfer Object to OUT parameters

    testReadings.reserve(performRealTimeQCRequestTO.testreadings_size());

    //repeated SensorReadings testReadings = performRealTimeQCRequestTO.repeated sensorreadings testreadings();
    for (int i = 0; i < performRealTimeQCRequestTO.testreadings_size(); i++)
    {
        std::shared_ptr<SensorReadings> toSensorReadings = std::make_shared<SensorReadings>();
        convert(performRealTimeQCRequestTO.testreadings(i), *toSensorReadings);
        testReadings.push_back(toSensorReadings); // object is copied over
    }

    if (performRealTimeQCRequestTO.has_qcstruct())
    {
        convert(performRealTimeQCRequestTO.qcstruct(), qcStruct);
    }
    else 
    {
        std::string tmpString = std::string(AMSerializerHelper::ERROR_MESSAGE_MISSING_INPUT_PARAMETER) + " qcstruct is NULL";
        errorMessage = AMSerializerHelper::formatErrorMessage(
            tmpString.c_str(),
            "AMSerializer::deserializePerformRealTimeQCRequest", __LINE__);
        return LibraryCallReturnCode::AM_CPP_PROTOBUF_DECODING_MISSING_INPUT_PARAMETER;
    }

    lastRecordedTime = performRealTimeQCRequestTO.lastrecordedtime();

    return LibraryCallReturnCode::SUCCESS;
}

LibraryCallReturnCode AMSerializer::serializePerformRealTimeQCResponse(
    const std::vector<std::shared_ptr<SensorReadings>>& testReadings,
    const RealTimeQCReturnCode amRC,
    char*& serializedResponseData,
    int& serializedInputDataSize,
    std::string& errorMessage)
{
    // 1. Input check

    // 2. Convert IN parameters to Transfer Object
    ::to::PerformRealTimeQCResponse performRealTimeQCResponseTO;

    performRealTimeQCResponseTO.set_errorcode((int)LibraryCallReturnCode::SUCCESS);

    for (int i = 0; i < testReadings.size(); i++) {
        ::to::SensorReadings* toSensorReadingsTO = performRealTimeQCResponseTO.add_testreadings(); // to
        convert(*(testReadings[i]), *toSensorReadingsTO);
    }

    performRealTimeQCResponseTO.set_amreturncode((int)amRC);

    // 3. Allocate memory for char[] to hold protobuf encoded byte array
    serializedInputDataSize = performRealTimeQCResponseTO.ByteSize();
    serializedResponseData = new char[serializedInputDataSize];
    // Caller of this method calls "freeMemory" to delete array.


    // 4. Protobuf encode
    if (performRealTimeQCResponseTO.SerializeToArray((void*)serializedResponseData, serializedInputDataSize))
    {
        return LibraryCallReturnCode::SUCCESS;
    }
    else
    {
        AMSerializerHelper::freeMemory(serializedResponseData);
        errorMessage = AMSerializerHelper::formatErrorMessage(
            AMSerializerHelper::ERROR_MESSAGE_PROTOBUFENCODE_RESPONSE,
            "AMSerializer::serializeComputeCorrectedResultsResponse", __LINE__);
        return LibraryCallReturnCode::AM_CPP_PROTOBUF_ENCODING_FAILED_SERIALIZE;
    }
}
