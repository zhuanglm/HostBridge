package com.epocal.epoctest.analyticalmanager;

import android.util.Log;

import com.epocal.common.am.BGEParameters;
import com.epocal.common.am.FinalResult;
import com.epocal.common.am.HumidityStruct;
import com.epocal.common.am.Levels;
import com.epocal.common.am.Point;
import com.epocal.common.am.Reading;
import com.epocal.common.am.RealTimeQC;
import com.epocal.common.am.SensorInfo;
import com.epocal.common.am.SensorReadings;
import com.epocal.common.types.am.AgeCategory;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.types.am.ChannelType;
import com.epocal.common.types.am.EGFRFormula;
import com.epocal.common.types.am.FluidType;
import com.epocal.common.types.am.Gender;
import com.epocal.common.types.am.HumidityReturnCode;
import com.epocal.common.types.am.RealTimeHematocritQCReturnCode;
import com.epocal.common.types.am.RealTimeQCReturnCode;
import com.epocal.common.types.am.RealTimeQCType;
import com.epocal.common.types.am.ResultsCalcReturnCode;
import com.epocal.common.types.am.Sensors;
import com.epocal.common.types.am.TestMode;
import com.epocal.epoctest.to.*;
import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This file contains the utility methods to convert (regular) class object to Transfer Object (TO)
 * TO objects are used to encode using protobuf protocol to send the data over JNI to call
 * Analytical Manager native functions.
 */
public class AMSerializer {
    private static final String TAG = AMSerializer.class.getSimpleName();

    /**
     * Serialize any TO object
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static byte[] serializeRequest(AbstractMessageLite request) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        request.writeTo(baos);
        baos.close();
        byte[] byteArray = baos.toByteArray();
        return byteArray;
    }

    public static ReadingTO.Reading convert(Reading reading) {
        ReadingTO.Reading.Builder builder = ReadingTO.Reading.newBuilder();
        builder.setTime(reading.time);
        builder.setValue(reading.value);
        return builder.build();
    }

    public static Reading convert(ReadingTO.Reading readingTO) {
        Reading reading = new Reading();
        reading.time = readingTO.getTime();
        reading.value = readingTO.getValue();
        return reading;
    }

    public static HumidityStructTO.HumidityStruct convert(HumidityStruct humidityStruct) {
        HumidityStructTO.HumidityStruct.Builder builder = HumidityStructTO.HumidityStruct.newBuilder();
        builder.setSensorType(humidityStruct.sensorType.value);
        builder.setWindowStart(humidityStruct.windowStart);
        builder.setWindowSize(humidityStruct.windowSize);
        builder.setLow(humidityStruct.low);
        builder.setHigh(humidityStruct.high);
        builder.setExtra1(humidityStruct.extra1);
        builder.setExtra2(humidityStruct.extra2);
        builder.setExtra3(humidityStruct.extra3);
        builder.setExtra4(humidityStruct.extra4);
        builder.setExtra5(humidityStruct.extra5);
        builder.setExtra6(humidityStruct.extra6);
        builder.setExtra7(humidityStruct.extra7);
        builder.setExtra8(humidityStruct.extra8);
        builder.setExtra9(humidityStruct.extra9);
        builder.setExtra10(humidityStruct.extra10);
        return builder.build();
    }

    public static HumidityStruct convert(HumidityStructTO.HumidityStruct humidityStructTO) {
        HumidityStruct humidityStruct = new HumidityStruct();
        humidityStruct.sensorType = Sensors.fromInt(humidityStructTO.getSensorType());
        humidityStruct.windowStart = humidityStructTO.getWindowStart();
        humidityStruct.windowSize = humidityStructTO.getWindowSize();
        humidityStruct.low = humidityStructTO.getLow();
        humidityStruct.high = humidityStructTO.getHigh();
        humidityStruct.extra1 = humidityStructTO.getExtra1();
        humidityStruct.extra2 = humidityStructTO.getExtra2();
        humidityStruct.extra3 = humidityStructTO.getExtra3();
        humidityStruct.extra4 = humidityStructTO.getExtra4();
        humidityStruct.extra5 = humidityStructTO.getExtra5();
        humidityStruct.extra6 = humidityStructTO.getExtra6();
        humidityStruct.extra7 = humidityStructTO.getExtra7();
        humidityStruct.extra8 = humidityStructTO.getExtra8();
        humidityStruct.extra9 = humidityStructTO.getExtra9();
        humidityStruct.extra10 = humidityStructTO.getExtra10();
        return humidityStruct;
    }

    public static SensorInfoTO.SensorInfo convert(SensorInfo sensorInfo) {
        SensorInfoTO.SensorInfo.Builder builder = SensorInfoTO.SensorInfo.newBuilder();
        builder.setChannelType(sensorInfo.channelType.value);
        builder.setSensorType(sensorInfo.sensorType.value);
        builder.setSensorDescriptorNumber(sensorInfo.sensorDescriptorNumber);
        builder.setCalDelimit(sensorInfo.calDelimit);
        builder.setSampleDelimit(sensorInfo.sampleDelimit);
        builder.setPostDelimit(sensorInfo.postDelimit);
        builder.setExtrapolation(sensorInfo.extrapolation);
        builder.setCalWindowSize(sensorInfo.calWindowSize);
        builder.setSampleWindowSize(sensorInfo.sampleWindowSize);
        builder.setPostWindowSize(sensorInfo.postWindowSize);
        builder.setCalCurveWeight(sensorInfo.calCurveWeight);
        builder.setSampleCurveWeight(sensorInfo.sampleCurveWeight);
        builder.setCalConcentration(sensorInfo.calConcentration);
        builder.setOffset(sensorInfo.offset);
        builder.setSlopeFactor(sensorInfo.slopeFactor);
        builder.setCalMeanLowQC(sensorInfo.CalMeanLowQC);
        builder.setCalMeanHighQC(sensorInfo.CalMeanHighQC);
        builder.setCalDriftLowQC(sensorInfo.CalDriftLowQC);
        builder.setCalDriftHighQC(sensorInfo.CalDriftHighQC);
        builder.setCalSecondLowQC(sensorInfo.CalSecondLowQC);
        builder.setCalSecondHighQC(sensorInfo.CalSecondHighQC);
        builder.setCalNoiseHighQC(sensorInfo.CalNoiseHighQC);
        builder.setSampleMeanLowQC(sensorInfo.SampleMeanLowQC);
        builder.setSampleMeanHighQC(sensorInfo.SampleMeanHighQC);
        builder.setSampleDriftLowQC(sensorInfo.SampleDriftLowQC);
        builder.setSampleDriftHighQC(sensorInfo.SampleDriftHighQC);
        builder.setSampleSecondLowQC(sensorInfo.SampleSecondLowQC);
        builder.setSampleSecondHighQC(sensorInfo.SampleSecondHighQC);
        builder.setSampleNoiseHighQC(sensorInfo.SampleNoiseHighQC);
        builder.setPostMeanLowQC(sensorInfo.PostMeanLowQC);
        builder.setPostMeanHighQC(sensorInfo.PostMeanHighQC);
        builder.setPostDriftLowQC(sensorInfo.PostDriftLowQC);
        builder.setPostDriftHighQC(sensorInfo.PostDriftHighQC);
        builder.setPostSecondLowQC(sensorInfo.PostSecondLowQC);
        builder.setPostSecondHighQC(sensorInfo.PostSecondHighQC);
        builder.setPostNoiseHighQC(sensorInfo.PostNoiseHighQC);
        builder.setDeltaDriftLowQC(sensorInfo.DeltaDriftLowQC);
        builder.setDeltaDriftHighQC(sensorInfo.DeltaDriftHighQC);
        builder.setParam1(sensorInfo.param1);
        builder.setParam2(sensorInfo.param2);
        builder.setParam3(sensorInfo.param3);
        builder.setParam4(sensorInfo.param4);
        builder.setParam5(sensorInfo.param5);
        builder.setParam6(sensorInfo.param6);
        builder.setParam7(sensorInfo.param7);
        builder.setParam8(sensorInfo.param8);
        builder.setParam9(sensorInfo.param9);
        builder.setParam10(sensorInfo.param10);
        builder.setParam11(sensorInfo.param11);
        builder.setParam12(sensorInfo.param12);
        builder.setParam13(sensorInfo.param13);
        builder.setParam14(sensorInfo.param14);
        builder.setParam15(sensorInfo.param15);
        builder.setParam16(sensorInfo.param16);
        builder.setParam17(sensorInfo.param17);
        builder.setParam18(sensorInfo.param18);
        builder.setParam19(sensorInfo.param19);
        builder.setParam20(sensorInfo.param20);
        builder.setParam21(sensorInfo.param21);
        builder.setParam22(sensorInfo.param22);
        builder.setParam23(sensorInfo.param23);
        builder.setParam24(sensorInfo.param24);
        builder.setParam25(sensorInfo.param25);
        builder.setParam26(sensorInfo.param26);
        builder.setParam27(sensorInfo.param27);
        builder.setParam28(sensorInfo.param28);
        builder.setParam29(sensorInfo.param29);
        builder.setParam30(sensorInfo.param30);
        builder.setParam31(sensorInfo.param31);
        builder.setParam32(sensorInfo.param32);
        builder.setParam33(sensorInfo.param33);
        builder.setParam34(sensorInfo.param34);
        builder.setParam35(sensorInfo.param35);
        builder.setParam36(sensorInfo.param36);
        builder.setParam37(sensorInfo.param37);
        builder.setParam38(sensorInfo.param38);
        builder.setParam39(sensorInfo.param39);
        builder.setParam40(sensorInfo.param40);
        builder.setParam41(sensorInfo.param41);
        builder.setParam42(sensorInfo.param42);
        builder.setParam43(sensorInfo.param43);
        builder.setParam44(sensorInfo.param44);
        builder.setParam45(sensorInfo.param45);
        builder.setParam46(sensorInfo.param46);
        builder.setParam47(sensorInfo.param47);
        builder.setParam48(sensorInfo.param48);
        builder.setParam49(sensorInfo.param49);
        builder.setParam50(sensorInfo.param50);
        builder.setParam51(sensorInfo.param51);
        builder.setParam52(sensorInfo.param52);
        builder.setParam53(sensorInfo.param53);
        builder.setParam54(sensorInfo.param54);
        builder.setParam55(sensorInfo.param55);
        builder.setParam56(sensorInfo.param56);
        builder.setParam57(sensorInfo.param57);
        builder.setParam58(sensorInfo.param58);
        builder.setParam59(sensorInfo.param59);
        builder.setParam60(sensorInfo.param60);
        builder.setParam61(sensorInfo.param61);
        builder.setParam62(sensorInfo.param62);
        builder.setParam63(sensorInfo.param63);
        builder.setParam64(sensorInfo.param64);
        builder.setParam65(sensorInfo.param65);
        builder.setParam66(sensorInfo.param66);
        builder.setParam67(sensorInfo.param67);
        builder.setParam68(sensorInfo.param68);
        builder.setParam69(sensorInfo.param69);
        builder.setParam70(sensorInfo.param70);
        builder.setParam71(sensorInfo.param71);
        builder.setParam72(sensorInfo.param72);
        builder.setParam73(sensorInfo.param73);
        builder.setParam74(sensorInfo.param74);
        builder.setParam75(sensorInfo.param75);
        builder.setParam76(sensorInfo.param76);
        builder.setParam77(sensorInfo.param77);
        builder.setParam78(sensorInfo.param78);
        builder.setParam79(sensorInfo.param79);
        builder.setParam80(sensorInfo.param80);
        builder.setParam81(sensorInfo.param81);
        builder.setParam82(sensorInfo.param82);
        builder.setParam83(sensorInfo.param83);
        builder.setParam84(sensorInfo.param84);
        builder.setParam85(sensorInfo.param85);
        builder.setParam86(sensorInfo.param86);
        builder.setParam87(sensorInfo.param87);
        builder.setParam88(sensorInfo.param88);
        builder.setParam89(sensorInfo.param89);
        builder.setParam90(sensorInfo.param90);
        builder.setParam91(sensorInfo.param91);
        builder.setParam92(sensorInfo.param92);
        builder.setParam93(sensorInfo.param93);
        builder.setParam94(sensorInfo.param94);
        builder.setParam95(sensorInfo.param95);
        builder.setParam96(sensorInfo.param96);
        builder.setParam97(sensorInfo.param97);
        builder.setParam98(sensorInfo.param98);
        builder.setParam99(sensorInfo.param99);
        builder.setParam100(sensorInfo.param100);
        builder.setReaderMeanLow(sensorInfo.readerMeanLow);
        builder.setReaderMeanHigh(sensorInfo.readerMeanHigh);
        builder.setReaderDriftLow(sensorInfo.readerDriftLow);
        builder.setReaderDriftHigh(sensorInfo.readerDriftHigh);
        builder.setReaderNoiseLow(sensorInfo.readerNoiseLow);
        builder.setReaderNoiseHigh(sensorInfo.readerNoiseHigh);
        builder.setTMinus(sensorInfo.tMinus);
        builder.setTPlus(sensorInfo.tPlus);
        builder.setPostCurvatureWeight(sensorInfo.postCurvatureWeight);
        builder.setBloodPointsToSkip(sensorInfo.bloodPointsToSkip);
        builder.setBloodPointsInWindow(sensorInfo.bloodPointsInWindow);
        builder.setBloodNoiseHigh(sensorInfo.bloodNoiseHigh);
        builder.setAqPointsToSkip(sensorInfo.aqPointsToSkip);
        builder.setAqPointsInWindow(sensorInfo.aqPointsInWindow);
        builder.setAqNoiseHigh(sensorInfo.aqNoiseHigh);
        builder.setLateBloodPointsToSkip(sensorInfo.lateBloodPointsToSkip);
        builder.setLateBloodPointsInWindow(sensorInfo.lateBloodPointsInWindow);
        builder.setLateBloodNoiseHigh(sensorInfo.lateBloodNoiseHigh);
        builder.setLateAqPointsToSkip(sensorInfo.lateAqPointsToSkip);
        builder.setLateAqPointsInWindow(sensorInfo.lateAqPointsInWindow);
        builder.setLateAqNoiseHigh(sensorInfo.lateAqNoiseHigh);
        builder.setRtPointLimitLow(sensorInfo.rtPointLimitLow);
        builder.setRtPointLimitHigh(sensorInfo.rtPointLimitHigh);
        builder.setD1Low(sensorInfo.d1Low);
        builder.setD1High(sensorInfo.d1High);
        builder.setP1D2Low(sensorInfo.p1d2Low);
        builder.setP1D2High(sensorInfo.p1d2High);
        builder.setP2D2Low(sensorInfo.p2d2Low);
        builder.setP2D2High(sensorInfo.p2d2High);
        builder.setP3D2Low(sensorInfo.p3d2Low);
        builder.setP3D2High(sensorInfo.p3d2High);
        builder.setA(sensorInfo.A);
        builder.setB(sensorInfo.B);
        builder.setC(sensorInfo.C);
        builder.setD(sensorInfo.D);
        builder.setF(sensorInfo.F);
        builder.setG(sensorInfo.G);
        builder.setTAmbOffset(sensorInfo.TAmbOffset);
        builder.setInjectionTimeOffset(sensorInfo.InjectionTimeOffset);
        builder.setAgeOffset(sensorInfo.AgeOffset);
        builder.setPowerOffset(sensorInfo.PowerOffset);
        builder.setNeuralNetBlood((sensorInfo.NeuralNetBlood == null) ? "" : sensorInfo.NeuralNetBlood);
        builder.setNeuralNetAQ((sensorInfo.NeuralNetAQ == null) ? "" : sensorInfo.NeuralNetAQ);
        return builder.build();
    }

    public static SensorInfo convert(SensorInfoTO.SensorInfo sensorInfoTO) {
        SensorInfo sensorInfo = new SensorInfo();
        sensorInfo.channelType = ChannelType.fromInt(sensorInfoTO.getChannelType());
        sensorInfo.sensorType = Sensors.fromInt(sensorInfoTO.getSensorType());
        sensorInfo.sensorDescriptorNumber = (byte) sensorInfoTO.getSensorDescriptorNumber();
        sensorInfo.calDelimit = sensorInfoTO.getCalDelimit();
        sensorInfo.sampleDelimit = sensorInfoTO.getSampleDelimit();
        sensorInfo.postDelimit = sensorInfoTO.getPostDelimit();
        sensorInfo.extrapolation = sensorInfoTO.getExtrapolation();
        sensorInfo.calWindowSize = sensorInfoTO.getCalWindowSize();
        sensorInfo.sampleWindowSize = sensorInfoTO.getSampleWindowSize();
        sensorInfo.postWindowSize = sensorInfoTO.getPostWindowSize();
        sensorInfo.calCurveWeight = sensorInfoTO.getCalCurveWeight();
        sensorInfo.sampleCurveWeight = sensorInfoTO.getSampleCurveWeight();
        sensorInfo.calConcentration = sensorInfoTO.getCalConcentration();
        sensorInfo.offset = sensorInfoTO.getOffset();
        sensorInfo.slopeFactor = sensorInfoTO.getSlopeFactor();
        sensorInfo.CalMeanLowQC = sensorInfoTO.getCalMeanLowQC();
        sensorInfo.CalMeanHighQC = sensorInfoTO.getCalMeanHighQC();
        sensorInfo.CalDriftLowQC = sensorInfoTO.getCalDriftLowQC();
        sensorInfo.CalDriftHighQC = sensorInfoTO.getCalDriftHighQC();
        sensorInfo.CalSecondLowQC = sensorInfoTO.getCalSecondLowQC();
        sensorInfo.CalSecondHighQC = sensorInfoTO.getCalSecondHighQC();
        sensorInfo.CalNoiseHighQC = sensorInfoTO.getCalNoiseHighQC();
        sensorInfo.SampleMeanLowQC = sensorInfoTO.getSampleMeanLowQC();
        sensorInfo.SampleMeanHighQC = sensorInfoTO.getSampleMeanHighQC();
        sensorInfo.SampleDriftLowQC = sensorInfoTO.getSampleDriftLowQC();
        sensorInfo.SampleDriftHighQC = sensorInfoTO.getSampleDriftHighQC();
        sensorInfo.SampleSecondLowQC = sensorInfoTO.getSampleSecondLowQC();
        sensorInfo.SampleSecondHighQC = sensorInfoTO.getSampleSecondHighQC();
        sensorInfo.SampleNoiseHighQC = sensorInfoTO.getSampleNoiseHighQC();
        sensorInfo.PostMeanLowQC = sensorInfoTO.getPostMeanLowQC();
        sensorInfo.PostMeanHighQC = sensorInfoTO.getPostMeanHighQC();
        sensorInfo.PostDriftLowQC = sensorInfoTO.getPostDriftLowQC();
        sensorInfo.PostDriftHighQC = sensorInfoTO.getPostDriftHighQC();
        sensorInfo.PostSecondLowQC = sensorInfoTO.getPostSecondLowQC();
        sensorInfo.PostSecondHighQC = sensorInfoTO.getPostSecondHighQC();
        sensorInfo.PostNoiseHighQC = sensorInfoTO.getPostNoiseHighQC();
        sensorInfo.DeltaDriftLowQC = sensorInfoTO.getDeltaDriftLowQC();
        sensorInfo.DeltaDriftHighQC = sensorInfoTO.getDeltaDriftHighQC();
        sensorInfo.param1 = sensorInfoTO.getParam1();
        sensorInfo.param2 = sensorInfoTO.getParam2();
        sensorInfo.param3 = sensorInfoTO.getParam3();
        sensorInfo.param4 = sensorInfoTO.getParam4();
        sensorInfo.param5 = sensorInfoTO.getParam5();
        sensorInfo.param6 = sensorInfoTO.getParam6();
        sensorInfo.param7 = sensorInfoTO.getParam7();
        sensorInfo.param8 = sensorInfoTO.getParam8();
        sensorInfo.param9 = sensorInfoTO.getParam9();
        sensorInfo.param10 = sensorInfoTO.getParam10();
        sensorInfo.param11 = sensorInfoTO.getParam11();
        sensorInfo.param12 = sensorInfoTO.getParam12();
        sensorInfo.param13 = sensorInfoTO.getParam13();
        sensorInfo.param14 = sensorInfoTO.getParam14();
        sensorInfo.param15 = sensorInfoTO.getParam15();
        sensorInfo.param16 = sensorInfoTO.getParam16();
        sensorInfo.param17 = sensorInfoTO.getParam17();
        sensorInfo.param18 = sensorInfoTO.getParam18();
        sensorInfo.param19 = sensorInfoTO.getParam19();
        sensorInfo.param20 = sensorInfoTO.getParam20();
        sensorInfo.param21 = sensorInfoTO.getParam21();
        sensorInfo.param22 = sensorInfoTO.getParam22();
        sensorInfo.param23 = sensorInfoTO.getParam23();
        sensorInfo.param24 = sensorInfoTO.getParam24();
        sensorInfo.param25 = sensorInfoTO.getParam25();
        sensorInfo.param26 = sensorInfoTO.getParam26();
        sensorInfo.param27 = sensorInfoTO.getParam27();
        sensorInfo.param28 = sensorInfoTO.getParam28();
        sensorInfo.param29 = sensorInfoTO.getParam29();
        sensorInfo.param30 = sensorInfoTO.getParam30();
        sensorInfo.param31 = sensorInfoTO.getParam31();
        sensorInfo.param32 = sensorInfoTO.getParam32();
        sensorInfo.param33 = sensorInfoTO.getParam33();
        sensorInfo.param34 = sensorInfoTO.getParam34();
        sensorInfo.param35 = sensorInfoTO.getParam35();
        sensorInfo.param36 = sensorInfoTO.getParam36();
        sensorInfo.param37 = sensorInfoTO.getParam37();
        sensorInfo.param38 = sensorInfoTO.getParam38();
        sensorInfo.param39 = sensorInfoTO.getParam39();
        sensorInfo.param40 = sensorInfoTO.getParam40();
        sensorInfo.param41 = sensorInfoTO.getParam41();
        sensorInfo.param42 = sensorInfoTO.getParam42();
        sensorInfo.param43 = sensorInfoTO.getParam43();
        sensorInfo.param44 = sensorInfoTO.getParam44();
        sensorInfo.param45 = sensorInfoTO.getParam45();
        sensorInfo.param46 = sensorInfoTO.getParam46();
        sensorInfo.param47 = sensorInfoTO.getParam47();
        sensorInfo.param48 = sensorInfoTO.getParam48();
        sensorInfo.param49 = sensorInfoTO.getParam49();
        sensorInfo.param50 = sensorInfoTO.getParam50();
        sensorInfo.param51 = sensorInfoTO.getParam51();
        sensorInfo.param52 = sensorInfoTO.getParam52();
        sensorInfo.param53 = sensorInfoTO.getParam53();
        sensorInfo.param54 = sensorInfoTO.getParam54();
        sensorInfo.param55 = sensorInfoTO.getParam55();
        sensorInfo.param56 = sensorInfoTO.getParam56();
        sensorInfo.param57 = sensorInfoTO.getParam57();
        sensorInfo.param58 = sensorInfoTO.getParam58();
        sensorInfo.param59 = sensorInfoTO.getParam59();
        sensorInfo.param60 = sensorInfoTO.getParam60();
        sensorInfo.param61 = sensorInfoTO.getParam61();
        sensorInfo.param62 = sensorInfoTO.getParam62();
        sensorInfo.param63 = sensorInfoTO.getParam63();
        sensorInfo.param64 = sensorInfoTO.getParam64();
        sensorInfo.param65 = sensorInfoTO.getParam65();
        sensorInfo.param66 = sensorInfoTO.getParam66();
        sensorInfo.param67 = sensorInfoTO.getParam67();
        sensorInfo.param68 = sensorInfoTO.getParam68();
        sensorInfo.param69 = sensorInfoTO.getParam69();
        sensorInfo.param70 = sensorInfoTO.getParam70();
        sensorInfo.param71 = sensorInfoTO.getParam71();
        sensorInfo.param72 = sensorInfoTO.getParam72();
        sensorInfo.param73 = sensorInfoTO.getParam73();
        sensorInfo.param74 = sensorInfoTO.getParam74();
        sensorInfo.param75 = sensorInfoTO.getParam75();
        sensorInfo.param76 = sensorInfoTO.getParam76();
        sensorInfo.param77 = sensorInfoTO.getParam77();
        sensorInfo.param78 = sensorInfoTO.getParam78();
        sensorInfo.param79 = sensorInfoTO.getParam79();
        sensorInfo.param80 = sensorInfoTO.getParam80();
        sensorInfo.param81 = sensorInfoTO.getParam81();
        sensorInfo.param82 = sensorInfoTO.getParam82();
        sensorInfo.param83 = sensorInfoTO.getParam83();
        sensorInfo.param84 = sensorInfoTO.getParam84();
        sensorInfo.param85 = sensorInfoTO.getParam85();
        sensorInfo.param86 = sensorInfoTO.getParam86();
        sensorInfo.param87 = sensorInfoTO.getParam87();
        sensorInfo.param88 = sensorInfoTO.getParam88();
        sensorInfo.param89 = sensorInfoTO.getParam89();
        sensorInfo.param90 = sensorInfoTO.getParam90();
        sensorInfo.param91 = sensorInfoTO.getParam91();
        sensorInfo.param92 = sensorInfoTO.getParam92();
        sensorInfo.param93 = sensorInfoTO.getParam93();
        sensorInfo.param94 = sensorInfoTO.getParam94();
        sensorInfo.param95 = sensorInfoTO.getParam95();
        sensorInfo.param96 = sensorInfoTO.getParam96();
        sensorInfo.param97 = sensorInfoTO.getParam97();
        sensorInfo.param98 = sensorInfoTO.getParam98();
        sensorInfo.param99 = sensorInfoTO.getParam99();
        sensorInfo.param100 = sensorInfoTO.getParam100();
        sensorInfo.readerMeanLow = sensorInfoTO.getReaderMeanLow();
        sensorInfo.readerMeanHigh = sensorInfoTO.getReaderMeanHigh();
        sensorInfo.readerDriftLow = sensorInfoTO.getReaderDriftLow();
        sensorInfo.readerDriftHigh = sensorInfoTO.getReaderDriftHigh();
        sensorInfo.readerNoiseLow = sensorInfoTO.getReaderNoiseLow();
        sensorInfo.readerNoiseHigh = sensorInfoTO.getReaderNoiseHigh();
        sensorInfo.tMinus = sensorInfoTO.getTMinus();
        sensorInfo.tPlus = sensorInfoTO.getTPlus();
        sensorInfo.postCurvatureWeight = sensorInfoTO.getPostCurvatureWeight();
        sensorInfo.bloodPointsToSkip = sensorInfoTO.getBloodPointsToSkip();
        sensorInfo.bloodPointsInWindow = sensorInfoTO.getBloodPointsInWindow();
        sensorInfo.bloodNoiseHigh = sensorInfoTO.getBloodNoiseHigh();
        sensorInfo.aqPointsToSkip = sensorInfoTO.getAqPointsToSkip();
        sensorInfo.aqPointsInWindow = sensorInfoTO.getAqPointsInWindow();
        sensorInfo.aqNoiseHigh = sensorInfoTO.getAqNoiseHigh();
        sensorInfo.lateBloodPointsToSkip = sensorInfoTO.getLateBloodPointsToSkip();
        sensorInfo.lateBloodPointsInWindow = sensorInfoTO.getLateBloodPointsInWindow();
        sensorInfo.lateBloodNoiseHigh = sensorInfoTO.getLateBloodNoiseHigh();
        sensorInfo.lateAqPointsToSkip = sensorInfoTO.getLateAqPointsToSkip();
        sensorInfo.lateAqPointsInWindow = sensorInfoTO.getLateAqPointsInWindow();
        sensorInfo.lateAqNoiseHigh = sensorInfoTO.getLateAqNoiseHigh();
        sensorInfo.rtPointLimitLow = sensorInfoTO.getRtPointLimitLow();
        sensorInfo.rtPointLimitHigh = sensorInfoTO.getRtPointLimitHigh();
        sensorInfo.d1Low = sensorInfoTO.getD1Low();
        sensorInfo.d1High = sensorInfoTO.getD1High();
        sensorInfo.p1d2Low = sensorInfoTO.getP1D2Low();
        sensorInfo.p1d2High = sensorInfoTO.getP1D2High();
        sensorInfo.p2d2Low = sensorInfoTO.getP2D2Low();
        sensorInfo.p2d2High = sensorInfoTO.getP2D2High();
        sensorInfo.p3d2Low = sensorInfoTO.getP3D2Low();
        sensorInfo.p3d2High = sensorInfoTO.getP3D2High();
        sensorInfo.A = sensorInfoTO.getA();
        sensorInfo.B = sensorInfoTO.getB();
        sensorInfo.C = sensorInfoTO.getC();
        sensorInfo.D = sensorInfoTO.getD();
        sensorInfo.F = sensorInfoTO.getF();
        sensorInfo.G = sensorInfoTO.getG();
        sensorInfo.TAmbOffset = sensorInfoTO.getTAmbOffset();
        sensorInfo.InjectionTimeOffset = sensorInfoTO.getInjectionTimeOffset();
        sensorInfo.AgeOffset = sensorInfoTO.getAgeOffset();
        sensorInfo.PowerOffset = sensorInfoTO.getPowerOffset();
        sensorInfo.NeuralNetBlood = sensorInfoTO.getNeuralNetBlood();
        sensorInfo.NeuralNetAQ = sensorInfoTO.getNeuralNetAQ();
        return sensorInfo;
    }

    public static LevelsTO.Levels convert(Levels levels) {
        LevelsTO.Levels.Builder builder = LevelsTO.Levels.newBuilder();
        builder.setCalMean(levels.calMean);
        builder.setCalSlope(levels.calSlope);
        builder.setCalNoise(levels.calNoise);
        builder.setCalSecond(levels.calSecond);
        builder.setCalEx(levels.calEx);
        builder.setSampleMean(levels.sampleMean);
        builder.setSampleSlope(levels.sampleSlope);
        builder.setSampleNoise(levels.sampleNoise);
        builder.setSampleSecond(levels.sampleSecond);
        builder.setSampleEx(levels.sampleEx);
        builder.setPostMean(levels.postMean);
        builder.setPostSlope(levels.postSlope);
        builder.setPostNoise(levels.postNoise);
        builder.setPostSecond(levels.postSecond);
        builder.setPostEx(levels.postEx);
        builder.setAdditionalMean(levels.additionalMean);
        builder.setAdditionalSlope(levels.additionalSlope);
        builder.setAdditionalNoise(levels.additionalNoise);
        builder.setAdditionalSecond(levels.additionalSecond);
        builder.setAdditionalEx(levels.additionalEx);
        builder.setAdditionalFirst(levels.additionalFirst);
        builder.setAdditionalLast(levels.additionalLast);
        builder.setPeakMean(levels.peakMean);
        builder.setPeakSlope(levels.peakSlope);
        builder.setPeakNoise(levels.peakNoise);
        builder.setPeakSecond(levels.peakSecond);
        builder.setPeakEx(levels.peakEx);
        builder.setResponse(levels.response);
        builder.setUncorrectedResponse(levels.uncorrectedResponse);
        builder.setCalLast(levels.calLast);
        builder.setCalFirst(levels.calFirst);
        builder.setSampleFirst(levels.sampleFirst);
        builder.setSampleLast(levels.sampleLast);
        builder.setPostFirst(levels.postFirst);
        builder.setPostLast(levels.postLast);
        builder.setPeakFirst(levels.peakFirst);
        builder.setPeakLast(levels.peakLast);
        builder.setCalWindowMovedBack(levels.calWindowMovedBack);
        builder.setOutput1(levels.output1);
        builder.setOutput2(levels.output2);
        builder.setOutput3(levels.output3);
        builder.setOutput4(levels.output4);
        builder.setOutput5(levels.output5);
        builder.setOutput6(levels.output6);
        builder.setOutput7(levels.output7);
        builder.setOutput8(levels.output8);
        builder.setOutput9(levels.output9);
        builder.setOutput10(levels.output10);
        builder.setOutput11(levels.output11);
        builder.setOutput12(levels.output12);
        builder.setOutput13(levels.output13);
        builder.setOutput14(levels.output14);
        builder.setOutput15(levels.output15);
        builder.setOutput16(levels.output16);
        builder.setOutput17(levels.output17);
        builder.setOutput18(levels.output18);
        builder.setOutput19(levels.output19);
        builder.setOutput20(levels.output20);
        return builder.build();
    }

    public static Levels convert(LevelsTO.Levels levelsTO) {
        Levels levels = new Levels();
        levels.calMean = levelsTO.getCalMean();
        levels.calSlope = levelsTO.getCalSlope();
        levels.calNoise = levelsTO.getCalNoise();
        levels.calSecond = levelsTO.getCalSecond();
        levels.calEx = levelsTO.getCalEx();
        levels.sampleMean = levelsTO.getSampleMean();
        levels.sampleSlope = levelsTO.getSampleSlope();
        levels.sampleNoise = levelsTO.getSampleNoise();
        levels.sampleSecond = levelsTO.getSampleSecond();
        levels.sampleEx = levelsTO.getSampleEx();
        levels.postMean = levelsTO.getPostMean();
        levels.postSlope = levelsTO.getPostSlope();
        levels.postNoise = levelsTO.getPostNoise();
        levels.postSecond = levelsTO.getPostSecond();
        levels.postEx = levelsTO.getPostEx();
        levels.additionalMean = levelsTO.getAdditionalMean();
        levels.additionalSlope = levelsTO.getAdditionalSlope();
        levels.additionalNoise = levelsTO.getAdditionalNoise();
        levels.additionalSecond = levelsTO.getAdditionalSecond();
        levels.additionalEx = levelsTO.getAdditionalEx();
        levels.additionalFirst = levelsTO.getAdditionalFirst();
        levels.additionalLast = levelsTO.getAdditionalLast();
        levels.peakMean = levelsTO.getPeakMean();
        levels.peakSlope = levelsTO.getPeakSlope();
        levels.peakNoise = levelsTO.getPeakNoise();
        levels.peakSecond = levelsTO.getPeakSecond();
        levels.peakEx = levelsTO.getPeakEx();
        levels.response = levelsTO.getResponse();
        levels.uncorrectedResponse = levelsTO.getUncorrectedResponse();
        levels.calLast = levelsTO.getCalLast();
        levels.calFirst = levelsTO.getCalFirst();
        levels.sampleFirst = levelsTO.getSampleFirst();
        levels.sampleLast = levelsTO.getSampleLast();
        levels.postFirst = levelsTO.getPostFirst();
        levels.postLast = levelsTO.getPostLast();
        levels.peakFirst = levelsTO.getPeakFirst();
        levels.peakLast = levelsTO.getPeakLast();
        levels.calWindowMovedBack = levelsTO.getCalWindowMovedBack();
        levels.output1 = levelsTO.getOutput1();
        levels.output2 = levelsTO.getOutput2();
        levels.output3 = levelsTO.getOutput3();
        levels.output4 = levelsTO.getOutput4();
        levels.output5 = levelsTO.getOutput5();
        levels.output6 = levelsTO.getOutput6();
        levels.output7 = levelsTO.getOutput7();
        levels.output8 = levelsTO.getOutput8();
        levels.output9 = levelsTO.getOutput9();
        levels.output10 = levelsTO.getOutput10();
        levels.output11 = levelsTO.getOutput11();
        levels.output12 = levelsTO.getOutput12();
        levels.output13 = levelsTO.getOutput13();
        levels.output14 = levelsTO.getOutput14();
        levels.output15 = levelsTO.getOutput15();
        levels.output16 = levelsTO.getOutput16();
        levels.output17 = levelsTO.getOutput17();
        levels.output18 = levelsTO.getOutput18();
        levels.output19 = levelsTO.getOutput19();
        levels.output20 = levelsTO.getOutput20();
        return levels;
    }

    public static FinalResultTO.FinalResult convert(FinalResult finalResult) {
        FinalResultTO.FinalResult.Builder builder = FinalResultTO.FinalResult.newBuilder();
        builder.setChannelType(finalResult.channelType.value);
        builder.setAnalyte(finalResult.analyte.value);
        builder.setCorrectedWhat(finalResult.correctedWhat.value);
        builder.setReading(finalResult.reading);
        builder.setReturnCode(finalResult.returnCode.value);
        builder.setRequirementsFailedIQC(finalResult.requirementsFailedIQC);
        return builder.build();
    }

    public static FinalResult convert(FinalResultTO.FinalResult finalResultTO) {
        FinalResult finalResult = new FinalResult();
        finalResult.channelType = ChannelType.fromInt(finalResultTO.getChannelType());
        finalResult.analyte = AnalyteName.fromInt(finalResultTO.getAnalyte());
        finalResult.correctedWhat = AnalyteName.fromInt(finalResultTO.getCorrectedWhat());
        finalResult.reading = finalResultTO.getReading();
        finalResult.returnCode = ResultsCalcReturnCode.fromInt(finalResultTO.getReturnCode());
        finalResult.requirementsFailedIQC = finalResultTO.getRequirementsFailedIQC();
        return finalResult;
    }

    public static RealTimeQCTO.RealTimeQC convert(RealTimeQC realTimeQC) {
        RealTimeQCTO.RealTimeQC.Builder builder = RealTimeQCTO.RealTimeQC.newBuilder();
        builder.setEnabled(realTimeQC.enabled);
        builder.setStartTime(realTimeQC.startTime);
        builder.setIntervalTime(realTimeQC.intervalTime);
        builder.setType(realTimeQC.type.value);
        builder.setNumPoints(realTimeQC.numPoints);
        builder.setExtra1(realTimeQC.extra1);
        builder.setExtra2(realTimeQC.extra2);
        builder.setExtra3(realTimeQC.extra3);
        builder.setExtra4(realTimeQC.extra4);
        builder.setExtra5(realTimeQC.extra5);
        builder.setExtra6(realTimeQC.extra6);
        builder.setContinueIfFailed(realTimeQC.continueIfFailed);
        builder.setHumidityUntil(realTimeQC.humidityUntil);
        //builder.setRepeated HumidityStruct humidityConfig(realTimeQC.repeated HumidityStruct humidityConfig);
        for (int i = 0; i < realTimeQC.humidityConfig.size(); i++) {
            builder.addHumidityConfig(convert(realTimeQC.humidityConfig.get(i)));
        }
        return builder.build();
    }

    public static RealTimeQC convert(RealTimeQCTO.RealTimeQC realTimeQCTO) {
        RealTimeQC realTimeQC = new RealTimeQC();
        realTimeQC.enabled = realTimeQCTO.getEnabled();
        realTimeQC.startTime = realTimeQCTO.getStartTime();
        realTimeQC.intervalTime = realTimeQCTO.getIntervalTime();
        realTimeQC.type = RealTimeQCType.fromInt(realTimeQCTO.getType());
        realTimeQC.numPoints = realTimeQCTO.getNumPoints();
        realTimeQC.extra1 = realTimeQCTO.getExtra1();
        realTimeQC.extra2 = realTimeQCTO.getExtra2();
        realTimeQC.extra3 = realTimeQCTO.getExtra3();
        realTimeQC.extra4 = realTimeQCTO.getExtra4();
        realTimeQC.extra5 = realTimeQCTO.getExtra5();
        realTimeQC.extra6 = realTimeQCTO.getExtra6();
        realTimeQC.continueIfFailed = realTimeQCTO.getContinueIfFailed();
        realTimeQC.humidityUntil = realTimeQCTO.getHumidityUntil();
        //realTimeQC.repeated HumidityStruct humidityConfig = realTimeQCTO.getRepeated HumidityStruct humidityConfig();
        for (int i = 0; i < realTimeQCTO.getHumidityConfigCount(); i++) {
            realTimeQC.humidityConfig.add(convert(realTimeQCTO.getHumidityConfig(i)));
        }
        return realTimeQC;
    }

    public static SensorReadingsTO.SensorReadings convert(SensorReadings sensorReadings) {
        SensorReadingsTO.SensorReadings.Builder builder = SensorReadingsTO.SensorReadings.newBuilder();
        builder.setRequirementsFailedQC(sensorReadings.requirementsFailedQC);
        builder.setReturnCode(sensorReadings.returnCode.value);
        builder.setSensorType(sensorReadings.sensorType.value);
        builder.setSensorDescriptorNumber(sensorReadings.sensorDescriptorNumber);
        builder.setChannelType(sensorReadings.channelType.value);
        builder.setAnalyte(sensorReadings.analyte.value);
        builder.setAnalyteString(sensorReadings.analyteString);
        //builder.setRepeated Reading readings(sensorReadings.repeated Reading readings);
        for (int i = 0; i < sensorReadings.readings.size(); i++) {
            builder.addReadings(convert(sensorReadings.readings.get(i)));
        }
        builder.setReadingPointer(sensorReadings.readingPointer);
        builder.setNumThisTypeReading(sensorReadings.numThisTypeReading);
        builder.setResult(sensorReadings.result);
        builder.setMultiplicationFactor(sensorReadings.multiplicationFactor);

        //builder.setSensorInfo sensorDescriptor(sensorReadings.SensorInfo sensorDescriptor);
        if (sensorReadings.sensorDescriptor != null) {
            builder.setSensorDescriptor(convert(sensorReadings.sensorDescriptor));
        }

        builder.setRealTimeQCPassed(sensorReadings.realTimeQCPassed.value);
        builder.setRealTimeQCFailedEver(sensorReadings.realTimeQCFailedEver.value);
        builder.setCheckRealtimeQC(sensorReadings.checkRealtimeQC);
        builder.setReportableLow(sensorReadings.reportableLow);
        builder.setReportableHigh(sensorReadings.reportableHigh);

        //builder.setLevels levels(sensorReadings.Levels levels);
        if (sensorReadings.levels != null) {
            builder.setLevels(convert(sensorReadings.levels));
        }

        builder.setHumidityPassed(sensorReadings.humidityPassed.value);
        builder.setInsanityLow(sensorReadings.insanityLow);
        builder.setInsanityHigh(sensorReadings.insanityHigh);
        builder.setInsanityQALow(sensorReadings.insanityQALow);
        builder.setInsanityQAHigh(sensorReadings.insanityQAHigh);
        builder.setRealTimeQCFailureTotal(sensorReadings.RealTimeQCFailureTotal);
        builder.setHumidityQCFailureTotal(sensorReadings.HumidityQCFailureTotal);
        builder.setAirQCFailureTotal(sensorReadings.AirQCFailureTotal);
        builder.setRealTimeQCFailureOccuranceString(sensorReadings.RealTimeQCFailureOccuranceString);
        builder.setHumidityQCFailureOccuranceString(sensorReadings.HumidityQCFailureOccuranceString);
        builder.setAirQCFailureOccuranceString(sensorReadings.AirQCFailureOccuranceString);
        builder.setExtraString(sensorReadings.extraString);
        builder.setResultString(sensorReadings.resultString);
        return builder.build();
    }

    public static SensorReadings convert(SensorReadingsTO.SensorReadings sensorReadingsTO) {
        SensorReadings sensorReadings = new SensorReadings();
        sensorReadings.requirementsFailedQC = sensorReadingsTO.getRequirementsFailedQC();
        sensorReadings.returnCode = ResultsCalcReturnCode.fromInt(sensorReadingsTO.getReturnCode());
        sensorReadings.sensorType = Sensors.fromInt(sensorReadingsTO.getSensorType());
        sensorReadings.sensorDescriptorNumber = (byte) sensorReadingsTO.getSensorDescriptorNumber();
        sensorReadings.channelType = ChannelType.fromInt(sensorReadingsTO.getChannelType());
        sensorReadings.analyte = AnalyteName.fromInt(sensorReadingsTO.getAnalyte());
        sensorReadings.analyteString = sensorReadingsTO.getAnalyteString();
        //sensorReadings.repeated Reading readings = sensorReadingsTO.getRepeated Reading readings();
        for (int i = 0; i < sensorReadingsTO.getReadingsCount(); i++) {
            sensorReadings.readings.add(convert(sensorReadingsTO.getReadings(i)));
        }
        sensorReadings.readingPointer = sensorReadingsTO.getReadingPointer();
        sensorReadings.numThisTypeReading = sensorReadingsTO.getNumThisTypeReading();
        sensorReadings.result = sensorReadingsTO.getResult();
        sensorReadings.multiplicationFactor = sensorReadingsTO.getMultiplicationFactor();

        //sensorReadings.SensorInfo sensorDescriptor = sensorReadingsTO.getSensorInfo sensorDescriptor();
        if (sensorReadingsTO.hasSensorDescriptor()) {
            sensorReadings.sensorDescriptor = convert(sensorReadingsTO.getSensorDescriptor());
        } else {
            sensorReadings.sensorDescriptor = null;
        }

        sensorReadings.realTimeQCPassed = RealTimeQCReturnCode.fromInt(sensorReadingsTO.getRealTimeQCPassed());
        sensorReadings.realTimeQCFailedEver = RealTimeQCReturnCode.fromInt(sensorReadingsTO.getRealTimeQCFailedEver());
        sensorReadings.checkRealtimeQC = sensorReadingsTO.getCheckRealtimeQC();
        sensorReadings.reportableLow = sensorReadingsTO.getReportableLow();
        sensorReadings.reportableHigh = sensorReadingsTO.getReportableHigh();

        //sensorReadings.Levels levels = sensorReadingsTO.getLevels levels();
        if (sensorReadingsTO.hasLevels()) {
            sensorReadings.levels = convert(sensorReadingsTO.getLevels());
        } else {
            sensorReadings.levels = null;
        }

        sensorReadings.humidityPassed = HumidityReturnCode.fromInt(sensorReadingsTO.getHumidityPassed());
        sensorReadings.insanityLow = sensorReadingsTO.getInsanityLow();
        sensorReadings.insanityHigh = sensorReadingsTO.getInsanityHigh();
        sensorReadings.insanityQALow = sensorReadingsTO.getInsanityQALow();
        sensorReadings.insanityQAHigh = sensorReadingsTO.getInsanityQAHigh();
        sensorReadings.RealTimeQCFailureTotal = sensorReadingsTO.getRealTimeQCFailureTotal();
        sensorReadings.HumidityQCFailureTotal = sensorReadingsTO.getHumidityQCFailureTotal();
        sensorReadings.AirQCFailureTotal = sensorReadingsTO.getAirQCFailureTotal();
        sensorReadings.RealTimeQCFailureOccuranceString = sensorReadingsTO.getRealTimeQCFailureOccuranceString();
        sensorReadings.HumidityQCFailureOccuranceString = sensorReadingsTO.getHumidityQCFailureOccuranceString();
        sensorReadings.AirQCFailureOccuranceString = sensorReadingsTO.getAirQCFailureOccuranceString();
        sensorReadings.extraString = sensorReadingsTO.getExtraString();
        sensorReadings.resultString = sensorReadingsTO.getResultString();
        return sensorReadings;
    }

    public static BGEParametersTO.BGEParameters convert(BGEParameters bgeParameters) throws IndexOutOfBoundsException, IllegalArgumentException {
        BGEParametersTO.BGEParameters.Builder builder = BGEParametersTO.BGEParameters.newBuilder();
        int addedItemCount = 0;
        int itemCount = bgeParameters.count();

        builder.setCount(itemCount);

        if (addedItemCount < itemCount) {
            builder.setParameter0(bgeParameters.getParamDouble(0));
            addedItemCount++;
        }
        if (addedItemCount < itemCount) {
            builder.setParameter1(bgeParameters.getParamDouble(1));
            addedItemCount++;
        }
        if (addedItemCount < itemCount) {
            builder.setParameter2(bgeParameters.getParamFloat(2));
            addedItemCount++;
        }
        if (addedItemCount < itemCount) {
            builder.setParameter3(bgeParameters.getParamFloat(3));
            addedItemCount++;
        }
        if (addedItemCount < itemCount) {
            builder.setParameter4(bgeParameters.getParamBoolean(4));
            addedItemCount++;
        }
        if (addedItemCount < itemCount) {
            FluidType fluidType = bgeParameters.getParamFluidType(5);
            builder.setParameter5(fluidType.value);
            addedItemCount++;
        }
        if (addedItemCount < itemCount) {
            SensorReadings sensorReadings = bgeParameters.getParamSensorReadings(6);
            builder.setParameter6(convert(sensorReadings));
            addedItemCount++;
        }
        if (addedItemCount < itemCount) {
            builder.setParameter7(bgeParameters.getParamInt(7));
            addedItemCount++;
        }
        if (addedItemCount < itemCount) {
            builder.setParameter8(bgeParameters.getParamBoolean(8));
            addedItemCount++;
        }
        if (addedItemCount < itemCount) {
            builder.setParameter9(bgeParameters.getParamDouble(9));
            addedItemCount++;
        }
        if (addedItemCount < itemCount) {
            builder.setParameter10(bgeParameters.getParamInt(10));
            addedItemCount++;
        }
        if (addedItemCount < itemCount) {
            builder.setParameter11(bgeParameters.getParamDouble(11));
            addedItemCount++;
        }
        if (addedItemCount < itemCount) {
            builder.setParameter12(bgeParameters.getParamDouble(12));
            addedItemCount++;
        }
        if (addedItemCount < itemCount) {
            builder.setParameter13(bgeParameters.getParamDouble(13));
            addedItemCount++;
        }
        if (addedItemCount < itemCount) {
            builder.setParameter14(bgeParameters.getParamDouble(14));
            addedItemCount++;
        }
        if (addedItemCount < itemCount) {
            builder.setParameter15(bgeParameters.getParamBoolean(15));
            addedItemCount++;
        }
        if (addedItemCount < itemCount) {
            builder.setParameter16(bgeParameters.getParamBoolean(16));
            addedItemCount++;
        }

        if (itemCount != addedItemCount) {
            // ERROR:
            throw new IllegalArgumentException("BGEParameter to BGEParameterTO convert failed at count=" + addedItemCount);
        }
        return builder.build();
    }

    public static BGEParameters convert(BGEParametersTO.BGEParameters bgeParametersTO) throws IllegalArgumentException {
        BGEParameters bgeParameters = new BGEParameters();

        boolean result = true;
        int itemCount = bgeParametersTO.getCount();

        if (itemCount == 0) {
            return bgeParameters; // nothing to copy
        }

        if (result && (bgeParameters.count() < itemCount)) {
            result = bgeParameters.addParam(bgeParametersTO.getParameter0());
        }
        if (result && (bgeParameters.count() < itemCount)) {
            result = bgeParameters.addParam(bgeParametersTO.getParameter1());
        }
        if (result && (bgeParameters.count() < itemCount)) {
            result = bgeParameters.addParam(bgeParametersTO.getParameter2());
        }
        if (result && (bgeParameters.count() < itemCount)) {
            result = bgeParameters.addParam(bgeParametersTO.getParameter3());
        }
        if (result && (bgeParameters.count() < itemCount)) {
            result = bgeParameters.addParam(bgeParametersTO.getParameter4());
        }
        if (result && (bgeParameters.count() < itemCount)) {
            result = bgeParameters.addParam(FluidType.fromInt(bgeParametersTO.getParameter5()));
        }
        if (result && (bgeParameters.count() < itemCount)) {
            SensorReadingsTO.SensorReadings sensorReadingsTO = bgeParametersTO.getParameter6();
            result = bgeParameters.addParam(convert(sensorReadingsTO));
        }
        if (result && (bgeParameters.count() < itemCount)) {
            result = bgeParameters.addParam(bgeParametersTO.getParameter7());
        }
        if (result && (bgeParameters.count() < itemCount)) {
            result = bgeParameters.addParam(bgeParametersTO.getParameter8());
        }
        if (result && (bgeParameters.count() < itemCount)) {
            result = bgeParameters.addParam(bgeParametersTO.getParameter9());
        }
        if (result && (bgeParameters.count() < itemCount)) {
            result = bgeParameters.addParam(bgeParametersTO.getParameter10());
        }
        if (result && (bgeParameters.count() < itemCount)) {
            result = bgeParameters.addParam(bgeParametersTO.getParameter11());
        }
        if (result && (bgeParameters.count() < itemCount)) {
            result = bgeParameters.addParam(bgeParametersTO.getParameter12());
        }
        if (result && (bgeParameters.count() < itemCount)) {
            result = bgeParameters.addParam(bgeParametersTO.getParameter13());
        }
        if (result && (bgeParameters.count() < itemCount)) {
            result = bgeParameters.addParam(bgeParametersTO.getParameter14());
        }
        if (result && (bgeParameters.count() < itemCount)) {
            result = bgeParameters.addParam(bgeParametersTO.getParameter15());
        }
        if (result && (bgeParameters.count() < itemCount)) {
            result = bgeParameters.addParam(bgeParametersTO.getParameter16());
        }

        if ((result == false) || (bgeParameters.count() != itemCount)) {
            // ERROR:
            throw new IllegalArgumentException("BGEParameterTO to BGEParameter convert failed at count=" + bgeParameters.count());
        }

        return bgeParameters;
    }

    public static PointTO.Point convert(Point point) {
        PointTO.Point.Builder builder = PointTO.Point.newBuilder();
        builder.setX(point.x);
        builder.setY(point.y);
        return builder.build();
    }

    public static Point convert(PointTO.Point pointTO) {
        Point point = new Point();
        point.x = pointTO.getX();
        point.y = pointTO.getY();
        return point;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //// Serialize Request

    public static byte[] serializeCalculateBGERequest(
            List<SensorReadings> sensorReadings,
            BGEParameters bgeParameters,
            boolean allowNegativeValues) throws AMException {
        // 1. check input parameters
        if ((sensorReadings == null) || (bgeParameters == null)) {
            // ERROR missing input
            Log.d("AMSerializer", "AMSerializer.serializeCalculateBGERequest: Missing inputs: sensorReadings or bgeParameters is null.");
            throw new AMException(LibraryCallReturnCode.AM_JAVA_PROTOBUF_ENCODING_ERROR, "AMSerializer.serializeCalculateBGERequest: Missing inputs: sensorReadings or bgeParameters is null.");
        }

        // 2. Create Transfer Object from input parameters
        CalculateBGERequestTO.CalculateBGERequest.Builder builder = CalculateBGERequestTO.CalculateBGERequest.newBuilder();
        CalculateBGERequestTO.CalculateBGERequest request;

        try {
            for (int i = 0; i < sensorReadings.size(); i++) {
                builder.addSensorReadings(convert(sensorReadings.get(i)));
            }
            builder.setParams(convert(bgeParameters));

            builder.setAllowNegativeValues(allowNegativeValues);

            request = builder.build();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            Log.d("AMSerializer1", e.getMessage());
            throw new AMException(LibraryCallReturnCode.AM_JAVA_PROTOBUF_ENCODING_ERROR, e.getMessage());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Log.d("AMSerializer2", e.getMessage());
            throw new AMException(LibraryCallReturnCode.AM_JAVA_PROTOBUF_ENCODING_ERROR, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("AMSerializer", "AMSerializer.serializeCalculateBGERequest: Could not create CalculateBGERequestTO.CalculateBGERequest object. "+e.getMessage());
            throw new AMException(LibraryCallReturnCode.AM_JAVA_PROTOBUF_ENCODING_ERROR, "AMSerializer.serializeCalculateBGERequest: Could not create CalculateBGERequestTO.CalculateBGERequest object. "+e.getMessage());
        }

        // 3. Serialize Transfer Object
        try {
            return serializeRequest(request);
        } catch (IOException ioe) {
            Log.d("AMSerializer", "AMSerializer.serializeCalculateBGERequest: Could not serialize Request object. "+ioe.getMessage());
            throw new AMException(LibraryCallReturnCode.AM_JAVA_PROTOBUF_ENCODING_FAILED_SERIALIZE, "AMSerializer.serializeCalculateBGERequest: Could not serialize Request object. "+ioe.getMessage());
        }
    }

    public static byte[] serializeCheckForEarlyInjectionRequest(
            SensorReadings hematocritReadings,
            SensorReadings topHeaterReadings,
            RealTimeHematocritQCReturnCode previousReturnCode,
            double airAfterFluidThreshold,
            float lastRecordedTime,
            double firstFluid) throws AMException {
        // 1. check input parameters
        if ((hematocritReadings == null) || (topHeaterReadings == null)) {
            // ERROR missing input
            Log.d("AMSerializer", "AMSerializer.serializeCheckForEarlyInjectionRequest: Missing inputs: hematocritReadings or topHeaterReadings is null.");
            throw new AMException(LibraryCallReturnCode.AM_JAVA_PROTOBUF_ENCODING_ERROR, "AMSerializer.serializeCheckForEarlyInjectionRequest: Missing inputs: hematocritReadings or topHeaterReadings is null.");
        }

        // 2. Create Transfer Object from input parameters
        CheckForEarlyInjectionRequestTO.CheckForEarlyInjectionRequest.Builder builder = CheckForEarlyInjectionRequestTO.CheckForEarlyInjectionRequest.newBuilder();
        CheckForEarlyInjectionRequestTO.CheckForEarlyInjectionRequest request;

        try {
            builder.setHematocritReadings(convert(hematocritReadings));
            builder.setTopHeaterReadings(convert(topHeaterReadings));
            builder.setPreviousReturnCode(previousReturnCode.value);
            builder.setAirAfterFluidThreshold(airAfterFluidThreshold);
            builder.setLastRecordedTime(lastRecordedTime);
            builder.setFirstFluid(firstFluid);
            request = builder.build();
        } catch (Exception e) {
            // Error: error in setting TO field from input parameter
            Log.d("AMSerializer", "AMSerializer.serializeCheckForEarlyInjectionRequest: Converting input to TO field failed."+e.getMessage());
            throw new AMException(LibraryCallReturnCode.AM_JAVA_PROTOBUF_ENCODING_ERROR, "AMSerializer.serializeCheckForEarlyInjectionRequest: Converting input to TO field failed."+e.getMessage());
        }

        // 3. Serialize Transfer Object
        try {
            return serializeRequest(request);
        } catch (IOException ioe) {
            Log.d("AMSerializer", "AMSerializer.serializeCheckForEarlyInjectionRequest:  Could not serialize Request object. "+ioe.getMessage());
            throw new AMException(LibraryCallReturnCode.AM_JAVA_PROTOBUF_ENCODING_FAILED_SERIALIZE, "AMSerializer.serializeCheckForEarlyInjectionRequest:  Could not serialize Request object. "+ioe.getMessage());
        }
    }

    public static byte[] serializeComputeCalculatedResultsRequest(
            List<FinalResult> measuredResults,
            List<FinalResult> calculatedResults,
            double passedctHb,
            double FiO2,
            double patientTemperature,
            double ambientPressure,
            TestMode testMode,
            double patientAge,
            Gender gender,
            EGFRFormula egfrFormula,
            double patientHeight,
            AgeCategory ageCategory,
            double RQ,
            boolean calculateAlveolar,
            boolean theApplymTCO2) throws AMException {
        // 1. check input parameters

        // 2. Create Transfer Object from input parameters
        ComputeCalculatedResultsRequestTO.ComputeCalculatedResultsRequest.Builder builder = ComputeCalculatedResultsRequestTO.ComputeCalculatedResultsRequest.newBuilder();
        ComputeCalculatedResultsRequestTO.ComputeCalculatedResultsRequest request;

        try {
            for (int i = 0; i < measuredResults.size(); i++) {
                builder.addMeasuredResults(convert(measuredResults.get(i)));
            }
            for (int i = 0; i < calculatedResults.size(); i++) {
                builder.addCalculatedResults(convert(calculatedResults.get(i)));
            }
            builder.setPassedctHb(passedctHb);
            builder.setFiO2(FiO2);
            builder.setTemperature(patientTemperature);
            builder.setPressure(ambientPressure);
            builder.setTestMode(testMode.value);
            builder.setPatientAge(patientAge);
            builder.setGender(gender.value);
            builder.setEgfrFormula(egfrFormula.value);
            builder.setPatientHeight(patientHeight);
            builder.setAgeCategory(ageCategory.value);
            builder.setRQ(RQ);
            builder.setCalculateAlveolar(calculateAlveolar);
            builder.setTheApplymTCO2(theApplymTCO2);
            request = builder.build();
        } catch (Exception e) {
            // Error: error in setting TO field from input parameter
            Log.d("AMSerializer", "AMSerializer.serializeComputeCalculatedResultsRequest: Converting input to TO field failed. "+e.getMessage());
            throw new AMException(LibraryCallReturnCode.AM_JAVA_PROTOBUF_ENCODING_ERROR, "AMSerializer.serializeComputeCalculatedResultsRequest: Converting input to TO field failed. "+e.getMessage());
        }

        // 3. Serialize Transfer Object
        try {
            return serializeRequest(request);
        } catch (IOException ioe) {
            Log.d("AMSerializer", "AMSerializer.serializeComputeCalculatedResultsRequest:  Could not serialize Request object. "+ioe.getMessage());
            throw new AMException(LibraryCallReturnCode.AM_JAVA_PROTOBUF_ENCODING_FAILED_SERIALIZE, "AMSerializer.serializeComputeCalculatedResultsRequest:  Could not serialize Request object. "+ioe.getMessage());
        }
    }

    public static byte[] serializeComputeCorrectedResultsRequest(
            List<FinalResult> measuredResults,
            List<FinalResult> correctedResults,
            double patientTemperature,
            double ambientPressure,
            double FiO2,
            double RQ,
            boolean calculateAlveolar) throws AMException {
        // 1. check input parameters

        // 2. Create Transfer Object from input parameters
        ComputeCorrectedResultsRequestTO.ComputeCorrectedResultsRequest.Builder builder = ComputeCorrectedResultsRequestTO.ComputeCorrectedResultsRequest.newBuilder();
        ComputeCorrectedResultsRequestTO.ComputeCorrectedResultsRequest request;

        try {
            for (int i = 0; i < measuredResults.size(); i++) {
                builder.addMeasuredResults(convert(measuredResults.get(i)));
            }
            for (int i = 0; i < correctedResults.size(); i++) {
                builder.addCorrectedResults(convert(correctedResults.get(i)));
            }
            builder.setTemperature(patientTemperature);
            builder.setPressure(ambientPressure);
            builder.setFiO2(FiO2);
            builder.setRQ(RQ);
            builder.setCalculateAlveolar(calculateAlveolar);
            request = builder.build();
        } catch (Exception e) {
            // Error: error in setting TO field from input parameter
            Log.d("AMSerializer", "AMSerializer.ComputeCorrectedResultsRequest: Converting input to TO field failed. "+e.getMessage());
            throw new AMException(LibraryCallReturnCode.AM_JAVA_PROTOBUF_ENCODING_ERROR, "AMSerializer.ComputeCorrectedResultsRequest: Converting input to TO field failed. "+e.getMessage());
        }

        // 3. Serialize Transfer Object
        try {
            return serializeRequest(request);
        } catch (IOException ioe) {
            Log.d("AMSerializer", "AMSerializer.ComputeCorrectedResultsRequest:  Could not serialize Request object. "+ioe.getMessage());
            throw new AMException(LibraryCallReturnCode.AM_JAVA_PROTOBUF_ENCODING_FAILED_SERIALIZE, "AMSerializer.ComputeCorrectedResultsRequest:  Could not serialize Request object. "+ioe.getMessage());
        }
    }

    public static byte[] serializePerformRealTimeQCRequest(
            List<SensorReadings> testReadings,
            RealTimeQC qcStruct,
            float lastRecordedTime) throws AMException {
        // 1. check input parameters

        // 2. Create Transfer Object from input parameters
        PerformRealTimeQCRequestTO.PerformRealTimeQCRequest.Builder builder = PerformRealTimeQCRequestTO.PerformRealTimeQCRequest.newBuilder();
        PerformRealTimeQCRequestTO.PerformRealTimeQCRequest request;

        try {
            for (int i = 0; i < testReadings.size(); i++) {
                builder.addTestReadings(convert(testReadings.get(i)));
            }
            builder.setQcStruct(convert(qcStruct));
            builder.setLastRecordedTime(lastRecordedTime);
            request = builder.build();
        } catch (Exception e) {
            // Error: error in setting TO field from input parameter
            Log.d("AMSerializer", "AMSerializer.PerformRealTimeQCRequest: Converting input to TO field failed. "+e.getMessage());
            throw new AMException(LibraryCallReturnCode.AM_JAVA_PROTOBUF_ENCODING_ERROR, "AMSerializer.PerformRealTimeQCRequest: Converting input to TO field failed. "+e.getMessage());
        }

        // 3. Serialize Transfer Object
        try {
            return serializeRequest(request);
        } catch (IOException ioe) {
            Log.d("AMSerializer", "AMSerializer.PerformRealTimeQCRequest:  Could not serialize Request object. "+ioe.getMessage());
            throw new AMException(LibraryCallReturnCode.AM_JAVA_PROTOBUF_ENCODING_FAILED_SERIALIZE, "AMSerializer.PerformRealTimeQCRequest:  Could not serialize Request object. "+ioe.getMessage());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //// Deserialize Response

    public static CheckForEarlyInjectionResponse deserializeCheckForEarlyInjectionResponse(byte[] serializedResponse) throws AMException {
        CheckForEarlyInjectionResponseTO.CheckForEarlyInjectionResponse responseTO;
        try {
            // 1. Deserialize
            responseTO = CheckForEarlyInjectionResponseTO.CheckForEarlyInjectionResponse.newBuilder().mergeFrom(serializedResponse).build();
            // 2. Retrieve for AM-lib returnCode (errorCode)
            LibraryCallReturnCode result = LibraryCallReturnCode.fromInt(responseTO.getErrorCode());
            // 3. Create Response object with mandatory fields: errorCode and errorMessage.
            CheckForEarlyInjectionResponse.Builder builder = CheckForEarlyInjectionResponse.Builder.newBuilder(result, responseTO.getErrorMessage());
            // 4. If errorCode is SUCCESS (indicating Response object is created successfully in AM-lib successfully.
            if (result == LibraryCallReturnCode.SUCCESS) {
                // 5. Unpack OUT fields
                SensorReadings sensorReadings = convert(responseTO.getHematocritReadings());
                RealTimeHematocritQCReturnCode amReturnCode = RealTimeHematocritQCReturnCode.fromInt(responseTO.getAmReturnCode());
                builder.amReturnCode(amReturnCode);
                builder.hematocritReadings(sensorReadings);
            }
            return builder.build();
        } catch (InvalidProtocolBufferException e) {
            Log.d("AMSerializer", "AMSerialize.deserializeCheckForEarlyInjectionResponse: Failed deserialize response. "+e.getMessage());
            throw new AMException(LibraryCallReturnCode.AM_JAVA_PROTOBUF_DECODING_FAILED_DESERIALIZE, "AMSerialize.deserializeCheckForEarlyInjectionResponse: Failed deserialize response. "+e.getMessage());
        }
    }

    public static CalculateBGEResponse deserializeCalculateBGEResponse(byte[] serializedResponse) throws AMException {
        CalculateBGEResponseTO.CalculateBGEResponse responseTO;
        try {
            // 1. Deserialize
            responseTO = CalculateBGEResponseTO.CalculateBGEResponse.newBuilder().mergeFrom(serializedResponse).build();
            // 2. Retrieve for AM-lib returnCode (errorCode)
            LibraryCallReturnCode result = LibraryCallReturnCode.fromInt(responseTO.getErrorCode());
            // 3. Create Response object with mandatory fields: errorCode and errorMessage.
            CalculateBGEResponse.Builder builder = CalculateBGEResponse.Builder.newBuilder(result, responseTO.getErrorMessage());
            // 4. If errorCode is SUCCESS (indicating Response object is created successfully in AM-lib successfully.
            if (result == LibraryCallReturnCode.SUCCESS) {
                // 5. Unpack OUT fields
                List<SensorReadings> sensorReadings = new ArrayList();
                for (int i = 0; i < responseTO.getSensorReadingsCount(); i++) {
                    sensorReadings.add(convert(responseTO.getSensorReadings(i)));
                }
                builder.sensorReadings(sensorReadings);
                builder.params(convert(responseTO.getParams()));
            }
            return builder.build();
        } catch (InvalidProtocolBufferException e) {
            Log.d("AMSerializer", "AMSerialize.deserializeCalculateBGEResponse: Failed deserialize response. "+e.getMessage());
            throw new AMException(LibraryCallReturnCode.AM_JAVA_PROTOBUF_DECODING_FAILED_DESERIALIZE, "AMSerialize.deserializeCalculateBGEResponse: Failed deserialize response. "+e.getMessage());
        }
    }

    public static ComputeCalculatedResultsResponse deserializeComputeCalculatedResultsResponse(byte[] serializedResponse) throws AMException {
        ComputeCalculatedResultsResponseTO.ComputeCalculatedResultsResponse responseTO;
        try {
            // 1. Deserialize
            responseTO = ComputeCalculatedResultsResponseTO.ComputeCalculatedResultsResponse.newBuilder().mergeFrom(serializedResponse).build();
            // 2. Retrieve for AM-lib returnCode (errorCode)
            LibraryCallReturnCode result = LibraryCallReturnCode.fromInt(responseTO.getErrorCode());
            // 3. Create Response object with mandatory fields: errorCode and errorMessage.
            ComputeCalculatedResultsResponse.Builder builder = ComputeCalculatedResultsResponse.Builder.newBuilder(result, responseTO.getErrorMessage());
            // 4. If errorCode is SUCCESS (indicating Response object is created successfully in AM-lib successfully.
            if (result == LibraryCallReturnCode.SUCCESS) {
                // 5. Unpack OUT fields
                List<FinalResult> calculatedResults = new ArrayList();
                for (int i = 0; i < responseTO.getCalculatedResultsCount(); i++) {
                    calculatedResults.add(convert(responseTO.getCalculatedResults(i)));
                }
                builder.calculatedResults(calculatedResults);
            }
            return builder.build();
        } catch (InvalidProtocolBufferException e) {
            Log.d("AMSerializer", "AMSerialize.deserializeComputeCalculatedResultsResponse: Failed deserialize response. "+e.getMessage());
            throw new AMException(LibraryCallReturnCode.AM_JAVA_PROTOBUF_DECODING_FAILED_DESERIALIZE, "AMSerialize.deserializeComputeCalculatedResultsResponse: Failed deserialize response. "+e.getMessage());
        }
    }

    public static ComputeCorrectedResultsResponse deserializeComputeCorrectedResultsResponse(byte[] serializedResponse) throws AMException {
        ComputeCorrectedResultsResponseTO.ComputeCorrectedResultsResponse responseTO;
        try {
            // 1. Deserialize
            responseTO = ComputeCorrectedResultsResponseTO.ComputeCorrectedResultsResponse.newBuilder().mergeFrom(serializedResponse).build();
            // 2. Retrieve for AM-lib returnCode (errorCode)
            LibraryCallReturnCode result = LibraryCallReturnCode.fromInt(responseTO.getErrorCode());
            // 3. Create Response object with mandatory fields: errorCode and errorMessage.
            ComputeCorrectedResultsResponse.Builder builder = ComputeCorrectedResultsResponse.Builder.newBuilder(result, responseTO.getErrorMessage());
            // 4. If errorCode is SUCCESS (indicating Response object is created successfully in AM-lib successfully.
            if (result == LibraryCallReturnCode.SUCCESS) {
                // 5. Unpack OUT fields
                List<FinalResult> correctedResults = new ArrayList();
                for (int i = 0; i < responseTO.getCorrectedResultsCount(); i++) {
                    correctedResults.add(convert(responseTO.getCorrectedResults(i)));
                }
                builder.correctedResults(correctedResults);
            }
            return builder.build();
        } catch (InvalidProtocolBufferException e) {
            Log.d("AMSerializer", "AMSerialize.deserializeComputeCorrectedResultsResponse: Failed deserialize response. "+e.getMessage());
            throw new AMException(LibraryCallReturnCode.AM_JAVA_PROTOBUF_DECODING_FAILED_DESERIALIZE, "AMSerialize.deserializeComputeCorrectedResultsResponse: Failed deserialize response. "+e.getMessage());
        }
    }

    public static PerformRealTimeQCResponse deserializePerformRealTimeQCResponse(byte[] serializedResponse) throws AMException {
        PerformRealTimeQCResponseTO.PerformRealTimeQCResponse responseTO;
        try {
            // 1. Deserialize
            responseTO = PerformRealTimeQCResponseTO.PerformRealTimeQCResponse.newBuilder().mergeFrom(serializedResponse).build();
            // 2. Retrieve for AM-lib returnCode (errorCode)
            LibraryCallReturnCode result = LibraryCallReturnCode.fromInt(responseTO.getErrorCode());
            // 3. Create Response object with mandatory fields: errorCode and errorMessage.
            PerformRealTimeQCResponse.Builder builder = PerformRealTimeQCResponse.Builder.newBuilder(result, responseTO.getErrorMessage());
            // 4. If errorCode is SUCCESS (indicating Response object is created successfully in AM-lib successfully.
            if (result == LibraryCallReturnCode.SUCCESS) {
                // 5. Unpack OUT fields
                builder.amReturnCode(RealTimeQCReturnCode.fromInt(responseTO.getAmReturnCode()));
                List<SensorReadings> testReadings = new ArrayList<SensorReadings>();
                for (int i = 0; i < responseTO.getTestReadingsCount(); i++) {
                    testReadings.add(convert(responseTO.getTestReadings(i)));
                }
                builder.testReadings(testReadings);
            }
            return builder.build();
        } catch (InvalidProtocolBufferException e) {
            Log.d("AMSerializer", "AMSerialize.deserializePerformRealTimeQCResponse: Failed deserialize response. "+e.getMessage());
            throw new AMException(LibraryCallReturnCode.AM_JAVA_PROTOBUF_DECODING_FAILED_DESERIALIZE, "AMSerialize.deserializePerformRealTimeQCResponse: Failed deserialize response. "+e.getMessage());
        }
    }
}
