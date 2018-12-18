package com.epocal.epoctest.testconfiguration;

import com.epocal.common.am.SensorInfo;
import com.epocal.common.types.AnalyteType;
import com.epocal.common.types.ReaderType;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.types.am.ChannelType;
import com.epocal.common.types.am.Sensors;
import com.epocal.epoctest.type.ChannelTypeToSensorType;
import com.epocal.reader.protocolcommontype.Sequence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by dning on 8/30/2017.
 */

public class TestConfiguration {
    public int mReaderType;
    public int CardType;
    public String CardTypeString;

    public TestConfigVersion ConfigVersion;
    public ReaderConfig ReaderConfigSetting;

    public TestConfig TestConfigSetting;
    public DryCardCheckConfig DryCardCheckConfigSetting;
    public SelfCheckConfig SelfCheckConfigSetting;

    public List<ReportableRange> ReportableRanges;
    public List<InsanityRange> InsanityRanges;

    public RealTimeQc RealTimeQCSetting;
    public List<HumidityDetect> HumidityDetects;

    public List<ExtraByte> MiscellaneousExtraInfoSet;

    public List<LimitKey> LimitKeySet;
    public List<ChannelLimit> ChannelLimitSet;

    public HostTestResultConfig HostTestResultConfigSetting;

    public TestConfiguration() {
        mReaderType = ReaderType.NEXTGEN_READER.value;

        ReaderConfigSetting = new ReaderConfig();
        ConfigVersion = new TestConfigVersion();

        TestConfigSetting = new TestConfig();
        DryCardCheckConfigSetting = new DryCardCheckConfig();
        SelfCheckConfigSetting = new SelfCheckConfig();

        ReportableRanges = new ArrayList<ReportableRange>();
        InsanityRanges = new ArrayList<InsanityRange>();

        RealTimeQCSetting = new RealTimeQc();
        HumidityDetects = new ArrayList<HumidityDetect>();

        MiscellaneousExtraInfoSet = new ArrayList<ExtraByte>();
        LimitKeySet = new ArrayList<LimitKey>();
        ChannelLimitSet = new ArrayList<ChannelLimit>();

        HostTestResultConfigSetting = new HostTestResultConfig();
    }

    public void getTestSequence(int sensorLayout, List<Sequence> testSequence, Calendar cardMade) {
        if (testSequence == null) {
            testSequence = new ArrayList<Sequence>();
        } else {
            testSequence.clear();
        }
        for (int i = 0; i < TestConfigSetting.SequenceBlocks.size(); i++) {
            SequenceBlock block = TestConfigSetting.SequenceBlocks.get(i);
            for (int j = i; j < block.Sequences.size(); j++) {
                SequenceItem sequenceItem = block.Sequences.get(j);
                if (sequenceItem.ChannelConfigSetting.SensorLayout != sensorLayout) {
                    continue;
                }
                Sequence newSequence = new Sequence();
                newSequence.ChannelType = sequenceItem.ChannelConfigSetting.ChannelType;
                newSequence.Inputs = sequenceItem.ChannelConfigSetting.Inputs;
                newSequence.Inputs2 = sequenceItem.ChannelConfigSetting.Inputs2;
                newSequence.MUXControl = sequenceItem.ChannelConfigSetting.MuxControl;
                newSequence.ADCMUX = sequenceItem.ChannelConfigSetting.ADCMUX;
                newSequence.VAPP1 = (short)sequenceItem.ChannelConfigSetting.Vapp1;
                newSequence.VAPP2 = (short)sequenceItem.ChannelConfigSetting.Vapp2;
                newSequence.VAPP3 = (short)sequenceItem.ChannelConfigSetting.Vapp3;
                newSequence.VAPP4 = (short)sequenceItem.ChannelConfigSetting.Vapp4;
                newSequence.NumSamples = sequenceItem.ChannelConfigSetting.NumSamples;

                SensorLayout layoutLatest = null;
                Calendar fromDate = com.epocal.util.DateUtil.getZero();

                for (int k = 0; k < TestConfigSetting.SensorLayouts.size(); k++) {
                    SensorLayout layout = TestConfigSetting.SensorLayouts.get(k);
                    if (layout.SensorLayoutNumber == sensorLayout && layout.ChannelType == sequenceItem.ChannelConfigSetting.ChannelType) {
                        if ((cardMade.compareTo(layout.SensorObject.FromDate) >= 0) && (layout.SensorObject.FromDate.compareTo(com.epocal.util.DateUtil.getZero()) >= 0)) {
                            fromDate = layout.SensorObject.FromDate;
                            layoutLatest = layout;
                        }
                    }
                }
                if (layoutLatest == null) {
                    newSequence.SensorType = 0;
                    newSequence.SensorDescriptorNumber = 0;
                } else {
                    newSequence.SensorType = layoutLatest.SensorObject.SensorType;
                    newSequence.SensorDescriptorNumber = layoutLatest.SensorObject.SubSensorType;
                }
                testSequence.add(newSequence);
            }
        }
    }

    public boolean getSensorInfo(short sensorLayoutNumber, List<SensorInfo> sensorInfos, List channelMapping, Calendar cardMade) {
        //for (int i = 0; i < (int)(ChannelType.ENUM_UNINITIALIZED.value - 1); i++)
        for (ChannelType channelType : ChannelType.values()) {
            int i = channelType.value;
            SensorLayout layoutLatest = null;
            Calendar fromDate = com.epocal.util.DateUtil.getZero();

            for (int j = 0; j < TestConfigSetting.SensorLayouts.size(); j++) {
                SensorLayout layout = TestConfigSetting.SensorLayouts.get(j);
                if (layout.SensorLayoutNumber == sensorLayoutNumber && layout.ChannelType == i) {
                    if ((cardMade.compareTo(layout.SensorObject.FromDate) >= 0) && (layout.SensorObject.FromDate.compareTo(com.epocal.util.DateUtil.getZero()) >= 0)) {
                        fromDate = layout.SensorObject.FromDate;
                        layoutLatest = layout;
                    }
                }
            }
            if (layoutLatest != null) {
                ChannelTypeToSensorType tempChannelMapping = new ChannelTypeToSensorType();
                SensorInfo tempSensorInfo = new SensorInfo();

                tempChannelMapping.ChannelType = layoutLatest.ChannelType;
                tempSensorInfo.channelType = ChannelType.convert(layoutLatest.ChannelType);

                tempChannelMapping.SensorType = layoutLatest.SensorObject.SensorType;
                tempSensorInfo.sensorType = Sensors.convert(layoutLatest.SensorObject.SensorType);

                // this number will change if there is a sensor descriptor
                tempChannelMapping.SensorDescriptorNumber = layoutLatest.SensorObject.SubSensorType;

                if (getIndividualSensorInfo(layoutLatest, tempSensorInfo)) {
                    sensorInfos.add(tempSensorInfo);
                    tempChannelMapping.SensorDescriptorNumber = tempSensorInfo.sensorDescriptorNumber;
                }
                channelMapping.add(tempChannelMapping);
            }
        }

        for (int i = 0; i < sensorInfos.size(); i++) {
            ChannelTypeToSensorType tempChannel = new ChannelTypeToSensorType();

            tempChannel.ChannelType = sensorInfos.get(i).channelType.value;
            tempChannel.SensorType = sensorInfos.get(i).sensorType.value;
            tempChannel.SensorDescriptorNumber = sensorInfos.get(i).sensorDescriptorNumber;

            channelMapping.add(tempChannel);
        }

        // must have at least one sensor descriptor or failure
        if (sensorInfos.size() > 0)
            return true;
        else
            return false;
    }

    public boolean getSensorInfoBySensorType(Sensors sensorType, short sensorSubType, SensorInfo sensorInfo, Calendar cardMade) {
        sensorInfo = null;
        SensorLayout layoutLatest = null;
        Calendar fromDate = com.epocal.util.DateUtil.getZero();

        for (int j = 0; j < TestConfigSetting.SensorLayouts.size(); j++) {
            SensorLayout layout = TestConfigSetting.SensorLayouts.get(j);
            if (layout.SensorObject.SensorType == sensorType.value && layout.SensorObject.SubSensorType == sensorSubType) {
                if ((cardMade.compareTo(layout.SensorObject.FromDate) >= 0) && (layout.SensorObject.FromDate.compareTo(com.epocal.util.DateUtil.getZero()) >= 0)) {
                    fromDate = layout.SensorObject.FromDate;
                    layoutLatest = layout;
                }
            }
        }
        if (layoutLatest != null) {
            sensorInfo = new SensorInfo();
            sensorInfo.channelType = ChannelType.convert(layoutLatest.ChannelType);
            sensorInfo.sensorType = Sensors.convert(layoutLatest.SensorObject.SensorType);

            if (getIndividualSensorInfo(layoutLatest, sensorInfo)) {
                return true;
            }
        }
        return false;
    }

    public boolean getIndividualSensorInfo(SensorLayout layoutLatest, SensorInfo sensorInfo) {
        if (layoutLatest.SensorObject.Descriptor != null) {
            sensorInfo.sensorDescriptorNumber = layoutLatest.SensorObject.Descriptor.SubSensorType;
            sensorInfo.sensorType = Sensors.convert(layoutLatest.SensorObject.Descriptor.SensorType);
            sensorInfo.calDelimit = layoutLatest.SensorObject.Descriptor.findWindow(WindowPhase.Calibration).Start;
            sensorInfo.calWindowSize = layoutLatest.SensorObject.Descriptor.findWindow(WindowPhase.Calibration).Length;

            sensorInfo.sampleDelimit = layoutLatest.SensorObject.Descriptor.findWindow(WindowPhase.Sampling).Start;
            sensorInfo.sampleWindowSize = layoutLatest.SensorObject.Descriptor.findWindow(WindowPhase.Sampling).Length;

            sensorInfo.postDelimit = layoutLatest.SensorObject.Descriptor.findWindow(WindowPhase.Post).Start;
            sensorInfo.postWindowSize = layoutLatest.SensorObject.Descriptor.findWindow(WindowPhase.Post).Length;

            sensorInfo.extrapolation = layoutLatest.SensorObject.Descriptor.Extrapolation;
            sensorInfo.calCurveWeight = layoutLatest.SensorObject.Descriptor.CalCurveWeight;
            sensorInfo.sampleCurveWeight = layoutLatest.SensorObject.Descriptor.SampleCurveWeight;
            sensorInfo.calConcentration = layoutLatest.SensorObject.Descriptor.CalConcentration;
            sensorInfo.offset = layoutLatest.SensorObject.Descriptor.Offset;
            sensorInfo.slopeFactor = layoutLatest.SensorObject.Descriptor.SlopeFactor;

            sensorInfo.CalMeanLowQC = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.CalibrationQC).MeanLowQC;
            sensorInfo.CalMeanHighQC = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.CalibrationQC).MeanHighQC;
            sensorInfo.CalDriftLowQC = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.CalibrationQC).DriftLowQC;
            sensorInfo.CalDriftHighQC = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.CalibrationQC).DriftHighQC;
            sensorInfo.CalSecondLowQC = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.CalibrationQC).SecondLowQC;
            sensorInfo.CalSecondHighQC = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.CalibrationQC).SecondHighQC;
            sensorInfo.CalNoiseHighQC = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.CalibrationQC).NoiseHighQC;

            sensorInfo.SampleMeanLowQC = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.SamplingQC).MeanLowQC;
            sensorInfo.SampleMeanHighQC = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.SamplingQC).MeanHighQC;
            sensorInfo.SampleDriftLowQC = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.SamplingQC).DriftLowQC;
            sensorInfo.SampleDriftHighQC = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.SamplingQC).DriftHighQC;
            sensorInfo.SampleSecondLowQC = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.SamplingQC).SecondLowQC;
            sensorInfo.SampleSecondHighQC = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.SamplingQC).SecondHighQC;
            sensorInfo.SampleNoiseHighQC = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.SamplingQC).NoiseHighQC;

            sensorInfo.PostMeanLowQC = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.PostQC).MeanLowQC;
            sensorInfo.PostMeanHighQC = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.PostQC).MeanHighQC;
            sensorInfo.PostDriftLowQC = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.PostQC).DriftLowQC;
            sensorInfo.PostDriftHighQC = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.PostQC).DriftHighQC;
            sensorInfo.PostSecondLowQC = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.PostQC).SecondLowQC;
            sensorInfo.PostSecondHighQC = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.PostQC).SecondHighQC;
            sensorInfo.PostNoiseHighQC = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.PostQC).NoiseHighQC;

            sensorInfo.DeltaDriftLowQC = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.DeltaDriftQC).DriftLowQC;
            sensorInfo.DeltaDriftHighQC = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.DeltaDriftQC).DriftHighQC;

            sensorInfo.readerMeanLow = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.ReaderQC).MeanLowQC;
            sensorInfo.readerMeanHigh = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.ReaderQC).MeanHighQC;
            sensorInfo.readerDriftLow = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.ReaderQC).DriftLowQC;
            sensorInfo.readerDriftHigh = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.ReaderQC).DriftHighQC;
            sensorInfo.readerNoiseLow = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.ReaderQC).NoiseLowQC;
            sensorInfo.readerNoiseHigh = layoutLatest.SensorObject.Descriptor.findQualityControlLimit(QualityControlLimitPhase.ReaderQC).NoiseHighQC;

            sensorInfo.param1 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(0).extraParameterValue;
            sensorInfo.param2 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(1).extraParameterValue;
            sensorInfo.param3 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(2).extraParameterValue;
            sensorInfo.param4 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(3).extraParameterValue;
            sensorInfo.param5 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(4).extraParameterValue;
            sensorInfo.param6 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(5).extraParameterValue;
            sensorInfo.param7 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(6).extraParameterValue;
            sensorInfo.param8 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(7).extraParameterValue;
            sensorInfo.param9 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(8).extraParameterValue;
            sensorInfo.param10 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(9).extraParameterValue;
            sensorInfo.param11 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(10).extraParameterValue;
            sensorInfo.param12 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(11).extraParameterValue;
            sensorInfo.param13 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(12).extraParameterValue;
            sensorInfo.param14 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(13).extraParameterValue;
            sensorInfo.param15 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(14).extraParameterValue;

            try {
                sensorInfo.param16 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(15).extraParameterValue;
                sensorInfo.param17 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(16).extraParameterValue;
                sensorInfo.param18 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(17).extraParameterValue;
                sensorInfo.param19 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(18).extraParameterValue;
                sensorInfo.param20 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(19).extraParameterValue;
                sensorInfo.param21 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(20).extraParameterValue;
                sensorInfo.param22 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(21).extraParameterValue;
                sensorInfo.param23 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(22).extraParameterValue;
                sensorInfo.param24 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(23).extraParameterValue;
                sensorInfo.param25 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(24).extraParameterValue;
                sensorInfo.param26 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(25).extraParameterValue;
                sensorInfo.param27 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(26).extraParameterValue;
                sensorInfo.param28 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(27).extraParameterValue;
                sensorInfo.param29 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(28).extraParameterValue;
                sensorInfo.param30 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(29).extraParameterValue;
            } catch (Exception e) {
                // no new parameters. so theyll all be 0.
            }

            try {
                sensorInfo.param31 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(30).extraParameterValue;
                sensorInfo.param32 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(31).extraParameterValue;
                sensorInfo.param33 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(32).extraParameterValue;
                sensorInfo.param34 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(33).extraParameterValue;
                sensorInfo.param35 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(34).extraParameterValue;
                sensorInfo.param36 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(35).extraParameterValue;
                sensorInfo.param37 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(36).extraParameterValue;
                sensorInfo.param38 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(37).extraParameterValue;
                sensorInfo.param39 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(38).extraParameterValue;
                sensorInfo.param40 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(39).extraParameterValue;
                sensorInfo.param41 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(40).extraParameterValue;
                sensorInfo.param42 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(41).extraParameterValue;
                sensorInfo.param43 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(42).extraParameterValue;
                sensorInfo.param44 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(43).extraParameterValue;
                sensorInfo.param45 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(44).extraParameterValue;
                sensorInfo.param46 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(45).extraParameterValue;
                sensorInfo.param47 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(46).extraParameterValue;
                sensorInfo.param48 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(47).extraParameterValue;
                sensorInfo.param49 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(48).extraParameterValue;
                sensorInfo.param50 = layoutLatest.SensorObject.Descriptor.ExtraParameters.get(49).extraParameterValue;
            } catch (Exception e) {
                // no new parameters. so they all be 0.
            }

            // put this back in to switch to new analytical manager
            sensorInfo.tMinus = layoutLatest.SensorObject.Descriptor.TMinus;
            sensorInfo.tPlus = layoutLatest.SensorObject.Descriptor.TPlus;
            sensorInfo.postCurvatureWeight = layoutLatest.SensorObject.Descriptor.PostCurvatureWeight;

            sensorInfo.bloodPointsToSkip = layoutLatest.SensorObject.Descriptor.BloodPointsToSkip;
            sensorInfo.bloodPointsInWindow = layoutLatest.SensorObject.Descriptor.BloodPointsInWindow;
            sensorInfo.bloodNoiseHigh = layoutLatest.SensorObject.Descriptor.BloodNoiseHigh;

            sensorInfo.aqPointsToSkip = layoutLatest.SensorObject.Descriptor.AqPointsToSkip;
            sensorInfo.aqPointsInWindow = layoutLatest.SensorObject.Descriptor.AqPointsInWindow;
            sensorInfo.aqNoiseHigh = layoutLatest.SensorObject.Descriptor.AqNoiseHigh;

            sensorInfo.lateBloodPointsToSkip = layoutLatest.SensorObject.Descriptor.LateBloodPointsToSkip;
            sensorInfo.lateBloodPointsInWindow = layoutLatest.SensorObject.Descriptor.LateBloodPointsInWindow;
            sensorInfo.lateBloodNoiseHigh = layoutLatest.SensorObject.Descriptor.LateBloodNoiseHigh;

            sensorInfo.lateAqPointsToSkip = layoutLatest.SensorObject.Descriptor.LateAqPointsToSkip;
            sensorInfo.lateAqPointsInWindow = layoutLatest.SensorObject.Descriptor.LateAqPointsInWindow;
            sensorInfo.lateAqNoiseHigh = layoutLatest.SensorObject.Descriptor.LateAqNoiseHigh;

            sensorInfo.rtPointLimitLow = layoutLatest.SensorObject.Descriptor.RTPointLimitLow;
            sensorInfo.rtPointLimitHigh = layoutLatest.SensorObject.Descriptor.RTPointLimitHigh;

            sensorInfo.d1Low = layoutLatest.SensorObject.Descriptor.D1Low;
            sensorInfo.d1High = layoutLatest.SensorObject.Descriptor.D1High;

            sensorInfo.p1d2Low = layoutLatest.SensorObject.Descriptor.P1D2Low;
            sensorInfo.p1d2High = layoutLatest.SensorObject.Descriptor.P1D2High;

            sensorInfo.p2d2Low = layoutLatest.SensorObject.Descriptor.P2D2Low;
            sensorInfo.p2d2High = layoutLatest.SensorObject.Descriptor.P2D2High;

            sensorInfo.p3d2Low = layoutLatest.SensorObject.Descriptor.P3D2Low;
            sensorInfo.p3d2High = layoutLatest.SensorObject.Descriptor.P3D2High;

            sensorInfo.A = layoutLatest.SensorObject.Descriptor.AParam;
            sensorInfo.B = layoutLatest.SensorObject.Descriptor.BParam;
            sensorInfo.C = layoutLatest.SensorObject.Descriptor.CParam;
            sensorInfo.D = layoutLatest.SensorObject.Descriptor.DParam;
            sensorInfo.F = layoutLatest.SensorObject.Descriptor.FParam;
            sensorInfo.G = layoutLatest.SensorObject.Descriptor.GParam;
            sensorInfo.TAmbOffset = layoutLatest.SensorObject.Descriptor.AmbientTempOffset;
            sensorInfo.InjectionTimeOffset = layoutLatest.SensorObject.Descriptor.InjectionTimeOffset;
            sensorInfo.AgeOffset = layoutLatest.SensorObject.Descriptor.AgeOffset;
            sensorInfo.PowerOffset = layoutLatest.SensorObject.Descriptor.PowerOffset;

            return true;
        }
        return false;
    }

    // added by rayzhuang at Oct 12 2018
    // there is not sensor type 19(TopHeaterPid) in SensorLayouts, but it is in SensorDescriptor
    public boolean legacyGetSensorInfoBySensorType(short sensorLayoutNumber,Sensors sensorType,
                                                   short sensorSubType,SensorInfo sensorInfo,
                                                   Calendar cardMade) {

        for (int i = 0; i < TestConfigSetting.SensorLayouts.size(); i++) {
            SensorLayout layout = TestConfigSetting.SensorLayouts.get(i);
            if (layout.SensorLayoutNumber == sensorLayoutNumber) {
                if ((cardMade.compareTo(layout.SensorObject.FromDate) >= 0) &&
                        (layout.SensorObject.FromDate.compareTo(com.epocal.util.DateUtil.getZero()) >= 0)) {
                    sensorInfo.channelType = ChannelType.convert(layout.ChannelType);
                }
            }
        }

        for (int j = 0; j < TestConfigSetting.SensorDescriptors.size(); j++) {
            SensorDescriptor descriptor = TestConfigSetting.SensorDescriptors.get(j);
            if (descriptor.SensorType == sensorType.value && descriptor.SubSensorType == sensorSubType) {
                if(legacyGetIndividualSensorInfo(descriptor,sensorInfo))   return true;
            }
        }

        return false;
    }

    public boolean legacyGetIndividualSensorInfo(SensorDescriptor descriptor, SensorInfo sensorInfo) {
        if (descriptor != null) {
            sensorInfo.sensorDescriptorNumber = descriptor.SubSensorType;
            sensorInfo.sensorType = Sensors.convert(descriptor.SensorType);
            sensorInfo.calDelimit = descriptor.findWindow(WindowPhase.Calibration).Start;
            sensorInfo.calWindowSize = descriptor.findWindow(WindowPhase.Calibration).Length;

            sensorInfo.sampleDelimit = descriptor.findWindow(WindowPhase.Sampling).Start;
            sensorInfo.sampleWindowSize = descriptor.findWindow(WindowPhase.Sampling).Length;

            sensorInfo.postDelimit = descriptor.findWindow(WindowPhase.Post).Start;
            sensorInfo.postWindowSize = descriptor.findWindow(WindowPhase.Post).Length;

            sensorInfo.extrapolation = descriptor.Extrapolation;
            sensorInfo.calCurveWeight = descriptor.CalCurveWeight;
            sensorInfo.sampleCurveWeight = descriptor.SampleCurveWeight;
            sensorInfo.calConcentration = descriptor.CalConcentration;
            sensorInfo.offset = descriptor.Offset;
            sensorInfo.slopeFactor = descriptor.SlopeFactor;

            sensorInfo.CalMeanLowQC = descriptor.findQualityControlLimit(QualityControlLimitPhase.CalibrationQC).MeanLowQC;
            sensorInfo.CalMeanHighQC = descriptor.findQualityControlLimit(QualityControlLimitPhase.CalibrationQC).MeanHighQC;
            sensorInfo.CalDriftLowQC = descriptor.findQualityControlLimit(QualityControlLimitPhase.CalibrationQC).DriftLowQC;
            sensorInfo.CalDriftHighQC = descriptor.findQualityControlLimit(QualityControlLimitPhase.CalibrationQC).DriftHighQC;
            sensorInfo.CalSecondLowQC = descriptor.findQualityControlLimit(QualityControlLimitPhase.CalibrationQC).SecondLowQC;
            sensorInfo.CalSecondHighQC = descriptor.findQualityControlLimit(QualityControlLimitPhase.CalibrationQC).SecondHighQC;
            sensorInfo.CalNoiseHighQC = descriptor.findQualityControlLimit(QualityControlLimitPhase.CalibrationQC).NoiseHighQC;

            sensorInfo.SampleMeanLowQC = descriptor.findQualityControlLimit(QualityControlLimitPhase.SamplingQC).MeanLowQC;
            sensorInfo.SampleMeanHighQC = descriptor.findQualityControlLimit(QualityControlLimitPhase.SamplingQC).MeanHighQC;
            sensorInfo.SampleDriftLowQC = descriptor.findQualityControlLimit(QualityControlLimitPhase.SamplingQC).DriftLowQC;
            sensorInfo.SampleDriftHighQC = descriptor.findQualityControlLimit(QualityControlLimitPhase.SamplingQC).DriftHighQC;
            sensorInfo.SampleSecondLowQC = descriptor.findQualityControlLimit(QualityControlLimitPhase.SamplingQC).SecondLowQC;
            sensorInfo.SampleSecondHighQC = descriptor.findQualityControlLimit(QualityControlLimitPhase.SamplingQC).SecondHighQC;
            sensorInfo.SampleNoiseHighQC = descriptor.findQualityControlLimit(QualityControlLimitPhase.SamplingQC).NoiseHighQC;

            sensorInfo.PostMeanLowQC = descriptor.findQualityControlLimit(QualityControlLimitPhase.PostQC).MeanLowQC;
            sensorInfo.PostMeanHighQC = descriptor.findQualityControlLimit(QualityControlLimitPhase.PostQC).MeanHighQC;
            sensorInfo.PostDriftLowQC = descriptor.findQualityControlLimit(QualityControlLimitPhase.PostQC).DriftLowQC;
            sensorInfo.PostDriftHighQC = descriptor.findQualityControlLimit(QualityControlLimitPhase.PostQC).DriftHighQC;
            sensorInfo.PostSecondLowQC = descriptor.findQualityControlLimit(QualityControlLimitPhase.PostQC).SecondLowQC;
            sensorInfo.PostSecondHighQC = descriptor.findQualityControlLimit(QualityControlLimitPhase.PostQC).SecondHighQC;
            sensorInfo.PostNoiseHighQC = descriptor.findQualityControlLimit(QualityControlLimitPhase.PostQC).NoiseHighQC;

            sensorInfo.DeltaDriftLowQC = descriptor.findQualityControlLimit(QualityControlLimitPhase.DeltaDriftQC).DriftLowQC;
            sensorInfo.DeltaDriftHighQC = descriptor.findQualityControlLimit(QualityControlLimitPhase.DeltaDriftQC).DriftHighQC;

            sensorInfo.readerMeanLow = descriptor.findQualityControlLimit(QualityControlLimitPhase.ReaderQC).MeanLowQC;
            sensorInfo.readerMeanHigh = descriptor.findQualityControlLimit(QualityControlLimitPhase.ReaderQC).MeanHighQC;
            sensorInfo.readerDriftLow = descriptor.findQualityControlLimit(QualityControlLimitPhase.ReaderQC).DriftLowQC;
            sensorInfo.readerDriftHigh = descriptor.findQualityControlLimit(QualityControlLimitPhase.ReaderQC).DriftHighQC;
            sensorInfo.readerNoiseLow = descriptor.findQualityControlLimit(QualityControlLimitPhase.ReaderQC).NoiseLowQC;
            sensorInfo.readerNoiseHigh = descriptor.findQualityControlLimit(QualityControlLimitPhase.ReaderQC).NoiseHighQC;

            sensorInfo.param1 = descriptor.ExtraParameters.get(0).extraParameterValue;
            sensorInfo.param2 = descriptor.ExtraParameters.get(1).extraParameterValue;
            sensorInfo.param3 = descriptor.ExtraParameters.get(2).extraParameterValue;
            sensorInfo.param4 = descriptor.ExtraParameters.get(3).extraParameterValue;
            sensorInfo.param5 = descriptor.ExtraParameters.get(4).extraParameterValue;
            sensorInfo.param6 = descriptor.ExtraParameters.get(5).extraParameterValue;
            sensorInfo.param7 = descriptor.ExtraParameters.get(6).extraParameterValue;
            sensorInfo.param8 = descriptor.ExtraParameters.get(7).extraParameterValue;
            sensorInfo.param9 = descriptor.ExtraParameters.get(8).extraParameterValue;
            sensorInfo.param10 = descriptor.ExtraParameters.get(9).extraParameterValue;
            sensorInfo.param11 = descriptor.ExtraParameters.get(10).extraParameterValue;
            sensorInfo.param12 = descriptor.ExtraParameters.get(11).extraParameterValue;
            sensorInfo.param13 = descriptor.ExtraParameters.get(12).extraParameterValue;
            sensorInfo.param14 = descriptor.ExtraParameters.get(13).extraParameterValue;
            sensorInfo.param15 = descriptor.ExtraParameters.get(14).extraParameterValue;

            try {
                sensorInfo.param16 = descriptor.ExtraParameters.get(15).extraParameterValue;
                sensorInfo.param17 = descriptor.ExtraParameters.get(16).extraParameterValue;
                sensorInfo.param18 = descriptor.ExtraParameters.get(17).extraParameterValue;
                sensorInfo.param19 = descriptor.ExtraParameters.get(18).extraParameterValue;
                sensorInfo.param20 = descriptor.ExtraParameters.get(19).extraParameterValue;
                sensorInfo.param21 = descriptor.ExtraParameters.get(20).extraParameterValue;
                sensorInfo.param22 = descriptor.ExtraParameters.get(21).extraParameterValue;
                sensorInfo.param23 = descriptor.ExtraParameters.get(22).extraParameterValue;
                sensorInfo.param24 = descriptor.ExtraParameters.get(23).extraParameterValue;
                sensorInfo.param25 = descriptor.ExtraParameters.get(24).extraParameterValue;
                sensorInfo.param26 = descriptor.ExtraParameters.get(25).extraParameterValue;
                sensorInfo.param27 = descriptor.ExtraParameters.get(26).extraParameterValue;
                sensorInfo.param28 = descriptor.ExtraParameters.get(27).extraParameterValue;
                sensorInfo.param29 = descriptor.ExtraParameters.get(28).extraParameterValue;
                sensorInfo.param30 = descriptor.ExtraParameters.get(29).extraParameterValue;
            } catch (Exception e) {
                // no new parameters. so theyll all be 0.
            }

            try {
                sensorInfo.param31 = descriptor.ExtraParameters.get(30).extraParameterValue;
                sensorInfo.param32 = descriptor.ExtraParameters.get(31).extraParameterValue;
                sensorInfo.param33 = descriptor.ExtraParameters.get(32).extraParameterValue;
                sensorInfo.param34 = descriptor.ExtraParameters.get(33).extraParameterValue;
                sensorInfo.param35 = descriptor.ExtraParameters.get(34).extraParameterValue;
                sensorInfo.param36 = descriptor.ExtraParameters.get(35).extraParameterValue;
                sensorInfo.param37 = descriptor.ExtraParameters.get(36).extraParameterValue;
                sensorInfo.param38 = descriptor.ExtraParameters.get(37).extraParameterValue;
                sensorInfo.param39 = descriptor.ExtraParameters.get(38).extraParameterValue;
                sensorInfo.param40 = descriptor.ExtraParameters.get(39).extraParameterValue;
                sensorInfo.param41 = descriptor.ExtraParameters.get(40).extraParameterValue;
                sensorInfo.param42 = descriptor.ExtraParameters.get(41).extraParameterValue;
                sensorInfo.param43 = descriptor.ExtraParameters.get(42).extraParameterValue;
                sensorInfo.param44 = descriptor.ExtraParameters.get(43).extraParameterValue;
                sensorInfo.param45 = descriptor.ExtraParameters.get(44).extraParameterValue;
                sensorInfo.param46 = descriptor.ExtraParameters.get(45).extraParameterValue;
                sensorInfo.param47 = descriptor.ExtraParameters.get(46).extraParameterValue;
                sensorInfo.param48 = descriptor.ExtraParameters.get(47).extraParameterValue;
                sensorInfo.param49 = descriptor.ExtraParameters.get(48).extraParameterValue;
                sensorInfo.param50 = descriptor.ExtraParameters.get(49).extraParameterValue;
            } catch (Exception e) {
                // no new parameters. so they all be 0.
            }

            // put this back in to switch to new analytical manager
            sensorInfo.tMinus = descriptor.TMinus;
            sensorInfo.tPlus = descriptor.TPlus;
            sensorInfo.postCurvatureWeight = descriptor.PostCurvatureWeight;

            sensorInfo.bloodPointsToSkip = descriptor.BloodPointsToSkip;
            sensorInfo.bloodPointsInWindow = descriptor.BloodPointsInWindow;
            sensorInfo.bloodNoiseHigh = descriptor.BloodNoiseHigh;

            sensorInfo.aqPointsToSkip = descriptor.AqPointsToSkip;
            sensorInfo.aqPointsInWindow = descriptor.AqPointsInWindow;
            sensorInfo.aqNoiseHigh = descriptor.AqNoiseHigh;

            sensorInfo.lateBloodPointsToSkip = descriptor.LateBloodPointsToSkip;
            sensorInfo.lateBloodPointsInWindow = descriptor.LateBloodPointsInWindow;
            sensorInfo.lateBloodNoiseHigh = descriptor.LateBloodNoiseHigh;

            sensorInfo.lateAqPointsToSkip = descriptor.LateAqPointsToSkip;
            sensorInfo.lateAqPointsInWindow = descriptor.LateAqPointsInWindow;
            sensorInfo.lateAqNoiseHigh = descriptor.LateAqNoiseHigh;

            sensorInfo.rtPointLimitLow = descriptor.RTPointLimitLow;
            sensorInfo.rtPointLimitHigh = descriptor.RTPointLimitHigh;

            sensorInfo.d1Low = descriptor.D1Low;
            sensorInfo.d1High = descriptor.D1High;

            sensorInfo.p1d2Low = descriptor.P1D2Low;
            sensorInfo.p1d2High = descriptor.P1D2High;

            sensorInfo.p2d2Low = descriptor.P2D2Low;
            sensorInfo.p2d2High = descriptor.P2D2High;

            sensorInfo.p3d2Low = descriptor.P3D2Low;
            sensorInfo.p3d2High = descriptor.P3D2High;

            sensorInfo.A = descriptor.AParam;
            sensorInfo.B = descriptor.BParam;
            sensorInfo.C = descriptor.CParam;
            sensorInfo.D = descriptor.DParam;
            sensorInfo.F = descriptor.FParam;
            sensorInfo.G = descriptor.GParam;
            sensorInfo.TAmbOffset = descriptor.AmbientTempOffset;
            sensorInfo.InjectionTimeOffset = descriptor.InjectionTimeOffset;
            sensorInfo.AgeOffset = descriptor.AgeOffset;
            sensorInfo.PowerOffset = descriptor.PowerOffset;

            return true;
        }
        return false;
    }

    public boolean isMeasuredAnalyte(AnalyteName analyte) {
        for (int i = 0; i < HostTestResultConfigSetting.CardSetups.size(); i++) {
            if (HostTestResultConfigSetting.CardSetups.get(i).Analyte == analyte.value) {
                if (HostTestResultConfigSetting.CardSetups.get(i).ResultType == AnalyteType.Measured.value) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public ReportableRange getDefaultReportableRange(AnalyteName analyte) {
        for (int i = 0; i < ReportableRanges.size(); i++) {
            if (ReportableRanges.get(i).Analyte == analyte.value) {
                ReportableRange rr = ReportableRanges.get(i);
                if(rr.Low == Double.NaN)
                    rr.Low = Double.MIN_VALUE;
                if(rr.High == Double.NaN)
                    rr.Low = Double.MAX_VALUE;

                //return rr;        //disabled in debug mode
            }
        }
        ReportableRange temp = new ReportableRange();
        temp.Analyte = analyte.value;
        temp.High = Double.MAX_VALUE;
        temp.Low = Double.NEGATIVE_INFINITY;
        return temp;
    }

    public InsanityRange getInsanityRange(AnalyteName analyte) {
        for (int i = 0; i < InsanityRanges.size(); i++) {
            if (InsanityRanges.get(i).Analyte == analyte.value) {
                return InsanityRanges.get(i);
            }
        }
        InsanityRange temp = new InsanityRange();
        temp.Analyte = analyte.value;
        temp.High = Double.MAX_VALUE;
        temp.Low = Double.MIN_VALUE;
        temp.QAHigh = Double.MAX_VALUE;
        temp.QALow = Double.MIN_VALUE;
        return temp;
    }
}
