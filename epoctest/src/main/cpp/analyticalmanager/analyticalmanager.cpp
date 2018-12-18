//
// Created by michde on 8/2/2017.
//

#define _USE_MATH_DEFINES
#include <algorithm>
#include <cmath>

#include "Analytes.h"
#include "HumidityReturnCode.h"
#include "RealTimeQCReturnCode.h"

#include "analyticalmanager.h"

#include "MathHelp.h"

using namespace Epoc::Common::Native::Definitions;

namespace Epoc {
    namespace AM {
        namespace Native {
            namespace Bge {

                void AnalyticalManager::Version(int& major, int& minor, int& micro)
                {
                    major = versionMajor;
                    minor = versionMinor;
                    micro = versionMicro;
                }

#ifndef CPP_IGNORE_THROW_ON_VALUE_OUT_OF_CSHARP_DECIMAL_RANGE
                const double AnalyticalManager::CSharpDecimalMaxValue = 79228162514264337593543950335.0;   //  Represents the largest possible value of System.Decimal.
                const double AnalyticalManager::CSharpDecimalMinValue = -79228162514264337593543950335.0;  //  Represents the smallest possible value of System.Decimal.
#endif

                const double AnalyticalManager::fullNernstVal = 61.54;

                double AnalyticalManager::FullNernst()
                {
                    return fullNernstVal;
                }

                const double AnalyticalManager::po2DeviceReportableLow = 5.0;
                const double AnalyticalManager::po2DeviceReportableHigh = 750.0;

                int AnalyticalManager::LowNoiseSIBVersion()
                {
                    return lowNoiseSIBVersion;
                }

                const double AnalyticalManager::numSecondsNoEarlySample = 8.0;
                const double AnalyticalManager::numSecondsStartEarlySampleChecking = 1.0;
                const double AnalyticalManager::DefaultOxygenValueForGlu = 200.0;
                const double AnalyticalManager::GluSampleMeanCalMeanHighLimitDifferentLimits = 9.0;
                const double AnalyticalManager::readerNoiseLowAbsoluteRealtime = 0.0;
                const double AnalyticalManager::readerNoiseLowBubbleDetect = 1.0;
                const double AnalyticalManager::readerNoiseLowSampleDetect = 2.0;
                const double AnalyticalManager::readerNoiseLowAbsoluteAfterTestEnds = 3.0;
                const double AnalyticalManager::readerNoiseLowDefault = -9999.0;
                const double AnalyticalManager::readerNoiseLowDefault2 = -9999.0;
                const double AnalyticalManager::HctAqThreshold = 7.0;
                const double AnalyticalManager::GlucoseDriftDeltaDriftLow = -0.7;
                const double AnalyticalManager::CreaAgeLimit = 6.0;
                const int AnalyticalManager::DaysofWeek = 7;

                const double AnalyticalManager::defaultTemperature = 37.0;
                const double AnalyticalManager::fio2RangeLow = 21.0;
                const double AnalyticalManager::fio2RangeHigh = 100.0;
                const double AnalyticalManager::rQRangeLow = 0.01;
                const double AnalyticalManager::rQRangeHigh = 2.00;
                const double AnalyticalManager::pH20TermOne = 6.275;
                const double AnalyticalManager::pH20TermTwo = 0.0236;
                const double AnalyticalManager::pH20TermThree = 0.000096;

                const double AnalyticalManager::tco2PhCutoff = 7.608;
                const double AnalyticalManager::tco2Pco2Factor = 0.0307;

                const double AnalyticalManager::NormalNaforTCO2 = 142.0;
                const double AnalyticalManager::bunBicarbFactor = 25.0;

                //eGFR
                const double AnalyticalManager::eGFRmdrSmpConcCreaMin = 0.1;
                const double AnalyticalManager::eGFRmdrPatientAgeMin = 18.0;
                const double AnalyticalManager::eGFRmdrPatientAgeMax = 120.0;

                const double AnalyticalManager::eGFRjpnSmpConcCreaMin = 0.1;
                const double AnalyticalManager::eGFRjpnPatientAgeMin = 22.0;
                const double AnalyticalManager::eGFRjpnPatientAgeMax = 120.0;

                const double AnalyticalManager::eGFRckdSmpConcCreaMin = 0.1;
                const double AnalyticalManager::eGFRckdPatientAgeMin = 19.0;
                const double AnalyticalManager::eGFRckdPatientAgeMax = 120.0;

                const double AnalyticalManager::eGFRswzSmpConcCreaMin = 0.1;
                const double AnalyticalManager::eGFRswzPatientAgeMin = 1.0;
                const double AnalyticalManager::eGFRswzPatientAgeMax = 18.0;
                const double AnalyticalManager::eGFRswzPatientHeightMin = 1.0;
                const double AnalyticalManager::eGFRswzPatientHeightMax = 275.0;

                bool AnalyticalManager::PotsAndGluLacAqBlood(double param)
                {
                    return ((((int)param) & PotsAndGluLacAqBloodMask) != 0);
                }

                bool AnalyticalManager::CreaSeptember2014Equation(double param)
                {
                    return ((((int)param) & CreaSeptember2014EquationMask) != 0);
                }

                bool AnalyticalManager::CreaNoiseVsSecondDipDetect(double param)
                {
                    return ((((int)param) & CreaNoiseVsSecondDipDetectMask) != 0);
                }

                bool AnalyticalManager::AgeAnd30CLogic(double param)
                {
                    return ((((int)param) & AgeAnd30CLogicMask) != 0);
                }

                bool AnalyticalManager::HctVolumeCorrection(double param)
                {
                    return ((((int)param) & HctVolumeCorrectionMask) != 0);
                }

                bool AnalyticalManager::CreaFlagC(double param)
                {
                    return ((((int)param) & CreaFlagCMask) != 0);
                }

                bool AnalyticalManager::AmpNoiseDividedMean(double param)
                {
                    return ((((int)param) & AmpNoiseDividedMeanMask) != 0);
                }

                bool AnalyticalManager::CreatinineMeanMean(double param)
                {
                    return ((((int)param) & CreatinineMeanMeanMask) != 0);
                }

                bool AnalyticalManager::NewCreaQC(double param)
                {
                    return ((((int)param) & NewCreaQCMask) != 0);
                }

                bool AnalyticalManager::CreaAgeCorrection(double param)
                {
                    return ((((int)param) & CreaAgeCorrectionMask) != 0);
                }

                bool AnalyticalManager::MultiplypHEarlyWindowByCalMean(double param)
                {
                    return ((((int)param) & MultiplypHEarlyWindowByCalMeanMask) != 0);
                }

                bool AnalyticalManager::pO2BubbleDetect(double param)
                {
                    return ((((int)param) & pO2BubbleDetectMask) != 0);
                }

                bool AnalyticalManager::pCO2newSlope(double param)
                {
                    return ((((int)param) & pCO2newSlopeMask) != 0);
                }

                bool AnalyticalManager::NewOxygenEquation(double param)
                {
                    return ((((int)param) & NewOxygenEquationMask) != 0);
                }

                bool AnalyticalManager::CalWindowMoveBack(double param)
                {
                    return ((((int)param) & CalWindowMoveBackMask) != 0);
                }

                bool AnalyticalManager::PostMeanMinusSampleMean(double param)
                {
                    return ((((int)param) & PostMeanMinusSampleMeanMask) != 0);
                }

                bool AnalyticalManager::pHEarlyWindow(double param)
                {
                    return ((((int)param) & pHEarlyWindowMask) != 0);
                }

                bool AnalyticalManager::NewNoise(double param)
                {
                    return ((((int)param) & NewNoiseMask) != 0);
                }

                bool AnalyticalManager::NewGlucoseAndLactate(double param)
                {
                    return ((((int)param) & NewGlucoseAndLactateMask) != 0);
                }

                bool AnalyticalManager::NewSodiumCalciumLactateCorrection(double param)
                {
                    return ((((int)param) & NewSodiumCalciumLactateCorrectionMask) != 0);
                }

                bool AnalyticalManager::NewestGlucose(double param)
                {
                    return ((((int)param) & NewestGlucoseMask) != 0);
                }

                bool AnalyticalManager::DecimalMax0OldGlucose(double param)
                {
                    return ((((int)param) & DecimalMax0OldGlucoseMask) != 0);
                }

                bool AnalyticalManager::CreaCalDrift(double param)
                {
                    return ((((int)param) & CreaCalDriftMask) != 0);
                }

                bool AnalyticalManager::LacHighLactateThresh(double param)
                {
                    return ((((int)param) & LacHighLactateThreshMask) != 0);
                }

                bool AnalyticalManager::AWDriftCorrectedPCO2(double param)
                {
                    return ((((int)param) & AWDriftCorrectedPCO2Mask) != 0);
                }

                bool AnalyticalManager::LacpO2(double param)
                {
                    return ((((int)param) & LacpO2Mask) != 0);
                }

                bool AnalyticalManager::NewLactatepO2Correction(double param)
                {
                    return ((((int)param) & NewLactatepO2CorrectionMask) != 0);
                }

                bool AnalyticalManager::LactateEarlyInjection(double param)
                {
                    return ((((int)param) & LactateEarlyInjectionMask) != 0);
                }

                bool AnalyticalManager::BUNInjectionTimeModify(double param)
                {
                    return ((((int)param) & BUNInjectionTimeModifyMask) != 0);
                }

                bool AnalyticalManager::BUNmvSlopAltkCO2LacLogic(double param)
                {
                    return ((((int)param) & BUNmvSlopAltkCO2LacLogicMask) != 0);
                }

                bool AnalyticalManager::TCO2AQSlopeOffsetX0Coeff(double param)
                {
                    return ((((int)param) & TCO2AQSlopeOffsetX0CoeffMask) != 0);
                }

                bool AnalyticalManager::CreaFinalAgeCorrect(double param)
                {
                    return ((((int)param) & CreaFinalAgeCorrectMask) != 0);
                }

                bool AnalyticalManager::NewUpperCalExCreaLimit(double param)
                {
                    return ((((int)param) & NewUpperCalExCreaLimitMask) != 0);
                }

                bool AnalyticalManager::DriftTermCorrectedPCO2(double bitmaskParam)
                {
                    return ((((int)bitmaskParam) & DriftTermCorrectedPCO2Mask) != 0);
                }


                bool AnalyticalManager::BUNAgeCorrectionsResponse(double param)
                {
                    return ((((int)param) & BUNAgeCorrectionsResponseMask) != 0);
                }

                RealTimeHematocritQCReturnCode AnalyticalManager::CheckForEarlyInjection(IN_OUT std::shared_ptr<SensorReadings> hematocritReadings,
                                                                                         IN std::shared_ptr<SensorReadings> topHeaterReadings,
                                                                                         IN RealTimeHematocritQCReturnCode previousReturnCode,
                                                                                         IN double airAfterFluidThreshold,
                                                                                         IN float lastRecordedTime,
                                                                                         IN double firstFluid)
                {
                    RealTimeHematocritQCReturnCode currentReturnCode = RealTimeHematocritQCReturnCode::Success;

                    // if no hematocrit readings, or no topheaterreadings. or fluid hasn't been hit yet,
                    // or we aren't supposed to be looking for 
                    if ((hematocritReadings == nullptr) || (topHeaterReadings == nullptr) || (firstFluid == 0) ||
                        (lastRecordedTime < firstFluid + numSecondsStartEarlySampleChecking) || (hematocritReadings->sensorDescriptor->param18 < 0.0001))
                    {
                        // passed i guess? we didn't actually check it, but this shouldn't happen anyway
                        return RealTimeHematocritQCReturnCode::NotPerformed;
                    }

                    // param17 = expected r offset. param 18 = expected r slope. assume the last heater reading is
                    // the current one that we want
                    double expectedConducitivity = (hematocritReadings->sensorDescriptor->param17 -
                        (*topHeaterReadings->readings)[topHeaterReadings->readings->size() - 1].Value) *
                        hematocritReadings->sensorDescriptor->param18;

                    // assume the last 4 points are the current packet.. 
                    for (int i = ((int)hematocritReadings->readings->size()) - 1; i >= (int)(hematocritReadings->readings->size()) - 4; i--)
                    {
                        // is the recorded time after the early/late fluid threshhold. if its after, apply the late
                        // thresholds. param11 = early/late threshold
                        // param14 = late high limit
                        // param16 = late low limit
                        if (lastRecordedTime > firstFluid + hematocritReadings->sensorDescriptor->param11)
                        {
                            // if we're above the expected + limit then check if we have an air point.. then
                            // we have a one point air.. otherwise we're just 'regular' over and we have
                            // a one point high resistance.
                            // but don't overwrite low resistance with air
                            if ((*hematocritReadings->readings)[i].Value > expectedConducitivity + hematocritReadings->sensorDescriptor->param14)
                            {
                                if (((*hematocritReadings->readings)[i].Value > airAfterFluidThreshold) &&
                                    (currentReturnCode != RealTimeHematocritQCReturnCode::LowResistance))
                                {
                                    currentReturnCode = RealTimeHematocritQCReturnCode::OnePointAir;
                                }
                                else if (currentReturnCode == RealTimeHematocritQCReturnCode::Success)
                                {
                                    currentReturnCode = RealTimeHematocritQCReturnCode::OnePointHighResistance;
                                }
                            }
                            else if ((*hematocritReadings->readings)[i].Value < expectedConducitivity - hematocritReadings->sensorDescriptor->param16)
                            {
                                currentReturnCode = RealTimeHematocritQCReturnCode::OnePointLowResistance;
                            }
                        }
                        else
                        {
                            // otherwise apply the early thresholds
                            // param12 = early high limit
                            // param13 = early low limit
                            if ((*hematocritReadings->readings)[i].Value > expectedConducitivity + hematocritReadings->sensorDescriptor->param12)
                            {
                                // if we're over the expected + limit check if we're in air .. or just regular overlimit
                                // don't overwrite low resistance with air
                                if (((*hematocritReadings->readings)[i].Value > airAfterFluidThreshold) &&
                                    (currentReturnCode != RealTimeHematocritQCReturnCode::LowResistance))
                                {
                                    currentReturnCode = RealTimeHematocritQCReturnCode::OnePointAir;
                                }
                                else if (currentReturnCode == RealTimeHematocritQCReturnCode::Success)
                                {
                                    currentReturnCode = RealTimeHematocritQCReturnCode::OnePointHighResistance;
                                }
                            }
                            else if ((*hematocritReadings->readings)[i].Value < expectedConducitivity - hematocritReadings->sensorDescriptor->param13)
                            {
                                // regular under limit
                                currentReturnCode = RealTimeHematocritQCReturnCode::OnePointLowResistance;
                            }
                        }
                    }

                    // if the current data packet has a one point failure in it
                    if (OnePointFailure(currentReturnCode))
                    {
                        // if the previous data packet also had a point point failure.. then stop the test
                        // and figure out what the return code should be 
                        if (OneOrTwoPointFailure(previousReturnCode))
                        {
                            // if the current or previous packet had a low resistance, report low resistance
                            if ((currentReturnCode == RealTimeHematocritQCReturnCode::OnePointLowResistance) ||
                                (previousReturnCode == RealTimeHematocritQCReturnCode::OnePointLowResistance) ||
                                (previousReturnCode == RealTimeHematocritQCReturnCode::LowResistance))
                            {
                                return RealTimeHematocritQCReturnCode::LowResistance;
                            }
                            // if the current or previous packet had air and we're x seconds ahead of
                            // the first fluid arrival, then report early injection
                            else if (((currentReturnCode == RealTimeHematocritQCReturnCode::OnePointAir) ||
                                (previousReturnCode == RealTimeHematocritQCReturnCode::OnePointAir) ||
                                (previousReturnCode == RealTimeHematocritQCReturnCode::EarlyInjection)) &&
                                (lastRecordedTime >= firstFluid + numSecondsNoEarlySample))
                            {
                                return RealTimeHematocritQCReturnCode::EarlyInjection;
                            }
                            else
                            {
                                // otherwise we've got a mixture of errors and then just report failed qc
                                // which will report as a fluidics check in the software
                                return RealTimeHematocritQCReturnCode::FailedQC;
                            }
                        }
                        else
                        {
                            // return the one point failure so that it can be passed in next time
                            return currentReturnCode;
                        }
                    }
                    else
                    {
                        // no failure in current packet, return success
                        return RealTimeHematocritQCReturnCode::Success;
                    }
                }

                bool AnalyticalManager::OnePointFailure(RealTimeHematocritQCReturnCode returnCode)
                {
                    if ((returnCode == RealTimeHematocritQCReturnCode::OnePointAir) ||
                        (returnCode == RealTimeHematocritQCReturnCode::OnePointHighResistance) ||
                        (returnCode == RealTimeHematocritQCReturnCode::OnePointLowResistance))
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }

                bool AnalyticalManager::OneOrTwoPointFailure(RealTimeHematocritQCReturnCode returnCode)
                {
                    if ((returnCode == RealTimeHematocritQCReturnCode::OnePointAir) ||
                        (returnCode == RealTimeHematocritQCReturnCode::OnePointHighResistance) ||
                        (returnCode == RealTimeHematocritQCReturnCode::OnePointLowResistance) ||
                        (returnCode == RealTimeHematocritQCReturnCode::EarlyInjection) ||
                        (returnCode == RealTimeHematocritQCReturnCode::FailedQC) ||
                        (returnCode == RealTimeHematocritQCReturnCode::LowResistance))
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }

                RealTimeQCReturnCode AnalyticalManager::PerformRealTimeQC(IN_OUT std::vector<std::shared_ptr<SensorReadings>> &testReadings,
                                                                          IN RealTimeQC qcStruct,
                                                                          IN float lastRecordedTime)
                {
                    bool newFailures = false;

                    // we will always call the function and let the function decide what it wants to do.
                    double leftOver = fmod(MathHelp::Round(lastRecordedTime, 1), qcStruct.intervalTime);

                    if (leftOver != 0)
                    {
                        return RealTimeQCReturnCode::NotPerformed;
                    }

                    // check humidity first
                    if (lastRecordedTime <= qcStruct.humidityUntil)
                    {
                        CheckHumidity(testReadings, qcStruct, lastRecordedTime);
                    }
                    // this is the first realtime qc check after the humidity window is over.. so that we only
                    // do this once
                    else if ((lastRecordedTime > qcStruct.humidityUntil) &&
                        (lastRecordedTime <= (qcStruct.humidityUntil + qcStruct.intervalTime)))
                    {
                        if (HaveAllHumidityChecksFailed(testReadings, qcStruct))
                        {
                            return RealTimeQCReturnCode::HumidityFailed;
                        }
                    }

                    // do we need to do anything ? we need to be after the start time and we need to be at
                    // a multiple of the number of points tested.. so we dont test every point
                    if (lastRecordedTime >= qcStruct.startTime)
                    {
                        newFailures = CheckRealtimeQC(testReadings, qcStruct, lastRecordedTime);
                    }

                    bool otherFailures = CheckCreatinineRealtimeQC(testReadings, qcStruct, lastRecordedTime, true);

                    if (otherFailures)
                    {
                        newFailures = true;
                    }

                    otherFailures = CheckAdditionalWindows(testReadings, qcStruct, lastRecordedTime);

                    if (otherFailures)
                    {
                        newFailures = true;
                    }

                    // a reference bubble supercedes other failures
                    if (CheckReferenceBubble(testReadings, qcStruct, lastRecordedTime))
                    {
                        return RealTimeQCReturnCode::ReferenceBubble;
                    }

                    if (newFailures)
                        return RealTimeQCReturnCode::Failed;
                    else
                        return RealTimeQCReturnCode::Success;
                }

                bool AnalyticalManager::CheckReferenceBubble(std::vector<std::shared_ptr<SensorReadings>> &testReadings, RealTimeQC qcStruct, float lastRecordedTime)
                {
                    int numFailedRefBubble = 0;
                    int numFailedPCO2AndRelated = 0;

                    // reference bubble for customers too
                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        if ((testReadings[i]->realTimeQCPassed == RealTimeQCReturnCode::FailedD2High) ||
                            (testReadings[i]->realTimeQCPassed == RealTimeQCReturnCode::FailedD2Low))
                        {
                            numFailedRefBubble++;

                            if ((testReadings[i]->sensorType == Sensors::CarbonDioxide) ||
                                (testReadings[i]->sensorType == Sensors::Glucose) ||
                                (testReadings[i]->sensorType == Sensors::Lactate) ||
                                (testReadings[i]->sensorType == Sensors::Creatinine))
                            {
                                numFailedPCO2AndRelated++;
                            }
                        }
                    }

                    if ((numFailedRefBubble > 1) && (numFailedPCO2AndRelated != numFailedRefBubble))
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }

                bool AnalyticalManager::CheckCreatinineRealtimeQC(std::vector<std::shared_ptr<SensorReadings>> &testReadings, RealTimeQC qcStruct, float lastRecordedTime, bool checkOnlyAtRightTime)
                {
                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        int numFailedHigh = 0;
                        int numFailedLow = 0;

                        // find creatinine... and if it hasn't already failed
                        if ((testReadings[i]->sensorType == Sensors::Creatinine) &&
                            ((testReadings[i]->realTimeQCPassed == RealTimeQCReturnCode::Success) ||
                            (testReadings[i]->realTimeQCPassed == RealTimeQCReturnCode::NotPerformed)))
                        {
                            // if we are in the 3 seconds immediately following the ending of the early window
                            if (!checkOnlyAtRightTime ||
                                (((*testReadings[i]->readings)[testReadings[i]->readings->size() - 1].Time >
                                    testReadings[i]->sensorDescriptor->param18 + testReadings[i]->sensorDescriptor->param19) &&
                                    ((*testReadings[i]->readings)[testReadings[i]->readings->size() - 1].Time <=
                                        testReadings[i]->sensorDescriptor->param18 + testReadings[i]->sensorDescriptor->param19 + qcStruct.intervalTime)))
                            {
                                // go through all the time points and find out whether they are within the limits
                                for (int k = ((int)testReadings[i]->readings->size()) - 1; (*testReadings[i]->readings)[k].Time >= testReadings[i]->sensorDescriptor->param18; k--)
                                {
                                    CSharpOutOfRange(k, testReadings[i]->readings->size());

                                    if (((*testReadings[i]->readings)[k].Time >= testReadings[i]->sensorDescriptor->param18) &&
                                        ((*testReadings[i]->readings)[k].Time <= testReadings[i]->sensorDescriptor->param18 + testReadings[i]->sensorDescriptor->param19))
                                    {
                                        if ((*testReadings[i]->readings)[k].Value < testReadings[i]->sensorDescriptor->param20)
                                        {
                                            // if nothing has failed low yet. add 1. if another point has failed low,
                                            // then there are 2 points that failed low and its a failure
                                            if (numFailedLow == 0)
                                            {
                                                numFailedLow++;
                                            }
                                            else
                                            {
                                                // more than one point is low. failure. we're done
                                                testReadings[i]->realTimeQCPassed = RealTimeQCReturnCode::CreaEarlyWindowLow;
                                                testReadings[i]->realTimeQCFailedEver = RealTimeQCReturnCode::Failed;
                                                return true;
                                            }
                                        }
                                        else if ((*testReadings[i]->readings)[k].Value > testReadings[i]->sensorDescriptor->param21)
                                        {
                                            // if nothing has failed low yet. add 1. if another point has failed low,
                                            // then there are 2 points that failed low and its a failure
                                            if (numFailedHigh == 0)
                                            {
                                                numFailedHigh++;
                                            }
                                            else
                                            {
                                                // more than one point is high. failure.
                                                testReadings[i]->realTimeQCPassed = RealTimeQCReturnCode::CreaEarlyWindowHigh;
                                                testReadings[i]->realTimeQCFailedEver = RealTimeQCReturnCode::Failed;
                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    return false;
                }

                bool AnalyticalManager::CheckAdditionalWindows(std::vector<std::shared_ptr<SensorReadings>> &testReadings, RealTimeQC qcStruct, float lastRecordedTime)
                {
                    bool anyFailures = false;

                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        // for each sensor, check whether we are supposed to test the additional window
                        if (((testReadings[i]->realTimeQCPassed == RealTimeQCReturnCode::Success) ||
                            (testReadings[i]->realTimeQCPassed == RealTimeQCReturnCode::NotPerformed)) &&
                            (testReadings[i]->sensorDescriptor->readerNoiseLow == readerNoiseLowAbsoluteRealtime) &&
                            (testReadings[i]->sensorDescriptor->param50 != 0))
                        {
                            // if no failures, we move on and check the additional window
                            // if we are in the 3 seconds immediately following the ending of the additional window
                            if (((*testReadings[i]->readings)[testReadings[i]->readings->size() - 1].Time >
                                testReadings[i]->sensorDescriptor->param49 + testReadings[i]->sensorDescriptor->param50) &&
                                ((*testReadings[i]->readings)[testReadings[i]->readings->size() - 1].Time <=
                                    testReadings[i]->sensorDescriptor->param49 + testReadings[i]->sensorDescriptor->param50 + qcStruct.intervalTime))
                            {
                                // go through all the time points and find out whether they are within the limits
                                FindWindowParams(testReadings[i]->readings,
                                    testReadings[i]->sensorDescriptor->param49,
                                    testReadings[i]->sensorDescriptor->param50,
                                    testReadings[i]->levels->additionalMean,
                                    testReadings[i]->levels->additionalSlope,
                                    testReadings[i]->levels->additionalNoise,
                                    testReadings[i]->levels->additionalSecond,
                                    testReadings[i]->levels->additionalFirst,
                                    testReadings[i]->levels->additionalLast,
                                    0, false, 0.0, false, 0.0,
                                    DefaultPointsToLeftForSecondDerivative,
                                    DefaultPointsToRightForSecondDerivative);

                                if (testReadings[i]->levels->additionalMean < testReadings[i]->sensorDescriptor->readerMeanLow)
                                {
                                    testReadings[i]->realTimeQCPassed = RealTimeQCReturnCode::AdditionalWindowMeanLow;
                                    testReadings[i]->realTimeQCFailedEver = RealTimeQCReturnCode::Failed;
                                    anyFailures = true;
                                }
                                else if (testReadings[i]->levels->additionalMean > testReadings[i]->sensorDescriptor->readerMeanHigh)
                                {
                                    testReadings[i]->realTimeQCPassed = RealTimeQCReturnCode::AdditionalWindowMeanHigh;
                                    testReadings[i]->realTimeQCFailedEver = RealTimeQCReturnCode::Failed;
                                    anyFailures = true;
                                }
                                else if (testReadings[i]->levels->additionalSlope < testReadings[i]->sensorDescriptor->readerDriftLow)
                                {
                                    testReadings[i]->realTimeQCPassed = RealTimeQCReturnCode::AdditionalWindowDriftLow;
                                    testReadings[i]->realTimeQCFailedEver = RealTimeQCReturnCode::Failed;
                                    anyFailures = true;
                                }
                                else if (testReadings[i]->levels->additionalSlope > testReadings[i]->sensorDescriptor->readerDriftHigh)
                                {
                                    testReadings[i]->realTimeQCPassed = RealTimeQCReturnCode::AdditionalWindowDriftHigh;
                                    testReadings[i]->realTimeQCFailedEver = RealTimeQCReturnCode::Failed;
                                    anyFailures = true;
                                }
                                else if (testReadings[i]->levels->additionalNoise > testReadings[i]->sensorDescriptor->readerNoiseHigh)
                                {
                                    testReadings[i]->realTimeQCPassed = RealTimeQCReturnCode::AdditionalWindowNoiseHigh;
                                    testReadings[i]->realTimeQCFailedEver = RealTimeQCReturnCode::Failed;
                                    anyFailures = true;
                                }
                            }
                        }
                    }
                    return anyFailures;
                }

                bool AnalyticalManager::CheckRealtimeQC(std::vector<std::shared_ptr<SensorReadings>> &testReadings, RealTimeQC qcStruct, float lastRecordedTime)
                {
                    bool newFailures = false;

                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        std::shared_ptr<SensorReadings> tempSensor = testReadings[i];
                        bool currentFailures = false;
                        RealTimeQCReturnCode oldReturnCode = tempSensor->realTimeQCPassed;

                        double last = ((*tempSensor->readings)[tempSensor->readings->size() - 1]).Value;
                        double first = ((*tempSensor->readings)[tempSensor->readings->size() - qcStruct.numPoints]).Value;

                        if ((tempSensor->readings->size() >= (qcStruct.numPoints * 2)) &&
                            (lastRecordedTime >= qcStruct.extra1))
                        {
                            double d2HighLimit = DBL_MAX;
                            double d2LowLimit = -DBL_MAX;

                            // period 1
                            if (lastRecordedTime >= qcStruct.extra3)
                            {
                                d2HighLimit = tempSensor->sensorDescriptor->p3d2High;
                                d2LowLimit = tempSensor->sensorDescriptor->p3d2Low;
                            }
                            else if (lastRecordedTime >= qcStruct.extra2)
                            {
                                d2HighLimit = tempSensor->sensorDescriptor->p2d2High;
                                d2LowLimit = tempSensor->sensorDescriptor->p2d2Low;
                            }
                            else // if (lastRecordedTime > qcStruct.extra1)
                            {
                                d2HighLimit = tempSensor->sensorDescriptor->p1d2High;
                                d2LowLimit = tempSensor->sensorDescriptor->p1d2Low;
                            }

                            // otherwise, we arent supposed to start checking, so do nothing.. the limits will
                            // stay at max and min value and will never fail..
                            double d2 = ((last - first) -
                                ((((*tempSensor->readings)[tempSensor->readings->size() - qcStruct.numPoints - 1]).Value) -
                                (((*tempSensor->readings)[tempSensor->readings->size() - (qcStruct.numPoints * 2)]).Value)));
                            // we need 2 sets of data to find the noise
                            if (d2 > d2HighLimit)
                            {
                                newFailures = currentFailures = true;
                                tempSensor->realTimeQCPassed = RealTimeQCReturnCode::FailedD2High;
                            }
                            else if (d2 < d2LowLimit)
                            {
                                newFailures = currentFailures = true;
                                tempSensor->realTimeQCPassed = RealTimeQCReturnCode::FailedD2Low;
                            }
                            else if ((last - first) > tempSensor->sensorDescriptor->d1High)
                            {
                                newFailures = currentFailures = true;
                                tempSensor->realTimeQCPassed = RealTimeQCReturnCode::FailedD1High;
                            }
                            else if ((last - first) < tempSensor->sensorDescriptor->d1Low)
                            {
                                newFailures = currentFailures = true;
                                tempSensor->realTimeQCPassed = RealTimeQCReturnCode::FailedD1Low;
                            }
                        }

                        if (!currentFailures)
                        {
                            if (((last > tempSensor->sensorDescriptor->rtPointLimitHigh) ||
                                (first > tempSensor->sensorDescriptor->rtPointLimitHigh)) && (lastRecordedTime >= qcStruct.extra4))
                            {
                                tempSensor->realTimeQCPassed = RealTimeQCReturnCode::FailedPointHighQC;
                                currentFailures = newFailures = true;
                            }
                            else if (((last < tempSensor->sensorDescriptor->rtPointLimitLow) ||
                                (first < tempSensor->sensorDescriptor->rtPointLimitLow)) && (lastRecordedTime >= qcStruct.extra4))
                            {
                                tempSensor->realTimeQCPassed = RealTimeQCReturnCode::FailedPointLowQC;
                                newFailures = currentFailures = true;
                            }
                        }

                        // if the old return code was crea early window, put it back, that needs to stay until the end
                        // until someone turns on crea, or until the test ends.. i could make the whole thing skip
                        // if the return code is not success, like additional window and crea checks, but its a big
                        // change and this is easier to verify
                        if ((oldReturnCode == RealTimeQCReturnCode::CreaEarlyWindowHigh) ||
                            (oldReturnCode == RealTimeQCReturnCode::CreaEarlyWindowLow))
                        {
                            tempSensor->realTimeQCPassed = oldReturnCode;

                            // mark this is a failure every time it comes through, otherwise the error will not
                            // trigger when crea is turned on halfway through the test after this has failed early on
                            newFailures = currentFailures = true;
                        }

                        if (!currentFailures)
                        {
                            tempSensor->realTimeQCPassed = RealTimeQCReturnCode::Success;
                        }
                        else
                        {
                            tempSensor->realTimeQCFailedEver = RealTimeQCReturnCode::Failed;
                        }
                    }

                    return newFailures;
                }

                bool AnalyticalManager::HaveAllHumidityChecksFailed(std::vector<std::shared_ptr<SensorReadings>> &testReadings, RealTimeQC qcStruct)
                {
                    // have all humidity checks failed
                    bool allFailed = true;

                    // first check right after humidity ended... check if all humidity checks that had to
                    // be performed failed.. if they did.. then the whole humidity check has failed
                    for (size_t i = 0; i < qcStruct.humidityConfig.size(); i++)
                    {
                        HumidityStruct tempHumidity = qcStruct.humidityConfig[i];

                        // go through the sensors...
                        for (size_t j = 0; j < testReadings.size(); j++)
                        {
                            // we were going to skip realtime qc checking for sensors for which it is off
                            // but instead we will check it, log it in the testrecord for possible future use
                            // and disregard it for purposes of the test, unless it gets turned back on
                            //if (!testReadings[j].checkRealtimQC)
                            //  continue;

                            // we have a match in a sensor type that needs to be checked
                            if (testReadings[j]->sensorType == tempHumidity.sensorType)
                            {
                                if (testReadings[j]->humidityPassed != HumidityReturnCode::Failed)
                                {
                                    allFailed = false;
                                }
                            }
                        }
                    }

                    return allFailed;
                }

                void AnalyticalManager::CheckHumidity(std::vector<std::shared_ptr<SensorReadings>> &testReadings, RealTimeQC qcStruct, float lastRecordedTime)
                {
                    // go through the humidity checks
                    for (size_t i = 0; i < qcStruct.humidityConfig.size(); i++)
                    {
                        HumidityStruct tempHumidity = qcStruct.humidityConfig[i];

                        // if enough time has elapsed.. and the check wasnt done before.. do the check
                        if ((lastRecordedTime >= (tempHumidity.windowStart + tempHumidity.windowSize)) &&
                            (lastRecordedTime <= (tempHumidity.windowStart + tempHumidity.windowSize + qcStruct.intervalTime)))
                        {
                            // go through the sensors...
                            for (size_t j = 0; j < testReadings.size(); j++)
                            {
                                bool allPassed = true;

                                // we have a match in a sensor type that needs to be checked
                                if (((testReadings[j])->sensorType == tempHumidity.sensorType) &&
                                    ((testReadings[j])->humidityPassed == HumidityReturnCode::NotPerformedYet))
                                {
                                    std::shared_ptr<SensorReadings> tempSensor = testReadings[j];

                                    // if we are checking that everything is within the window
                                    if (tempHumidity.extra1 == 0)
                                    {
                                        for (int k = ((int)tempSensor->readings->size()) - 1; (k >= 0); k--)
                                        {
                                            if (((((*tempSensor->readings)[k]).Time >= tempHumidity.windowStart) &&
                                                (((*tempSensor->readings)[k]).Time <= (tempHumidity.windowStart + tempHumidity.windowSize))) &&
                                                ((((*tempSensor->readings)[k]).Value > tempHumidity.high) ||
                                                (((*tempSensor->readings)[k]).Value < tempHumidity.low)))
                                            {
                                                allPassed = false;
                                            }
                                            else if (((*tempSensor->readings)[k]).Time < tempHumidity.windowStart)
                                            {
                                                break;
                                            }
                                        }
                                    }
                                    else if (tempHumidity.extra1 == 1)
                                    {
                                        // check that the mean is within the low/high limit
                                        double mean = 0;
                                        int numPoints = 0;

                                        for (int k = ((int)tempSensor->readings->size()) - 1; k >= 0; k--)
                                        {
                                            if ((((*tempSensor->readings)[k]).Time >= tempHumidity.windowStart) &&
                                                (((*tempSensor->readings)[k]).Time <= (tempHumidity.windowStart + tempHumidity.windowSize)))
                                            {
                                                mean += ((*tempSensor->readings)[k]).Value;
                                                numPoints++;
                                            }
                                            else if (((*tempSensor->readings)[k]).Time < tempHumidity.windowStart)
                                            {
                                                break;
                                            }
                                        }

                                        // calculate mean
                                        mean /= numPoints;

                                        // if the mean is lower than low or higher than high
                                        if ((mean > tempHumidity.high) || (mean < tempHumidity.low))
                                        {
                                            allPassed = false;
                                        }
                                    }
                                    else if (tempHumidity.extra1 == 2)
                                    {
                                        // check that the mean is within the low/high limit
                                        int numPoints = 0;
                                        double sumx = 0;
                                        double sumy = 0;
                                        double sumx2 = 0;
                                        double sumxy = 0;
                                        double slope = 0;


                                        for (int k = ((int)tempSensor->readings->size()) - 1; k >= 0; k--)
                                        {
                                            if ((((*tempSensor->readings)[k]).Time >= tempHumidity.windowStart) &&
                                                (((*tempSensor->readings)[k]).Time <= (tempHumidity.windowStart + tempHumidity.windowSize)))
                                            {
                                                sumx += ((*tempSensor->readings)[k]).Time;
                                                sumy += ((*tempSensor->readings)[k]).Value;
                                                sumx2 += ((*tempSensor->readings)[k]).Time * ((*tempSensor->readings)[k]).Time;
                                                sumxy += ((*tempSensor->readings)[k]).Time * ((*tempSensor->readings)[k]).Value;
                                                numPoints++;
                                            }
                                            else if (((*tempSensor->readings)[k]).Time < tempHumidity.windowStart)
                                            {
                                                break;
                                            }
                                        }

                                        slope = CSharpDecimalCalculation((sumxy - ((sumx * sumy) / numPoints)) / (sumx2 - ((sumx * sumx) / numPoints)));

                                        // if the mean is lower than low or higher than high
                                        if ((slope > tempHumidity.high) || (slope < tempHumidity.low))
                                        {
                                            allPassed = false;
                                        }
                                    }
                                    else
                                    {
                                        allPassed = true;
                                    }

                                    // was there a failure
                                    if (allPassed)
                                    {
                                        tempSensor->humidityPassed = HumidityReturnCode::Success;
                                    }
                                    else
                                    {
                                        tempSensor->humidityPassed = HumidityReturnCode::Failed;
                                    }
                                }
                            }
                        }
                    }
                }

                bool AnalyticalManager::DetectWindowDips(std::shared_ptr<std::vector<Reading>> readings,
                    double windowStart,               // window start
                    double windowSize,                // window size
                    int pointsToSkip,                 // start after this many points
                    int maxNumberPoints,              // max number of points
                    int pointsInWindow,               // points in window
                    double noiseLimit,                // noise high
                    double flagParameter,             // param15 - flags param
                    bool divideByMeanToGetNoise,
                    double subtractFromValues,
                    double lowerLimitForNoise,
                    bool removeHighestLowest)
                {
                    if (pointsInWindow != 0)
                    {
                        bool returnValue = false;
                        int firstPoint = 0;
                        int lastPoint = 0;

                        // very inefficient. fix later - Taha
                        for (size_t i = 0; i < readings->size(); i++)
                        {
                            if (((*readings)[i].Time >= windowStart) && (firstPoint == 0))
                            {
                                firstPoint = i;
                            }

                            if (((*readings)[i].Time > windowStart + windowSize) && (lastPoint == 0))
                            {
                                lastPoint = i - 1;
                                break;
                            }
                        }

                        // we got to the end of the data and hadn't found the end of the window, assume that
                        // the end of the window is the minimum between the end of the data and the maximum
                        // number of points we can do.. if we found the last point.. still see if the maximum
                        // number of points is less than that
                        if (lastPoint == 0)
                        {
                            lastPoint = std::min((int)(readings->size() - 1), firstPoint + maxNumberPoints);
                        }
                        else
                        {
                            lastPoint = std::min(lastPoint, firstPoint + maxNumberPoints);
                        }

                        firstPoint += pointsToSkip;

                        double highestPoint = -DBL_MAX;
                        double lowestPoint = DBL_MAX;
                        int highestIndex = 0;
                        int lowestIndex = 0;

                        if (removeHighestLowest)
                        {
                            // find highest and lowest points
                            for (int i = firstPoint; i <= lastPoint; i++)
                            {
                                CSharpOutOfRange(i, readings->size());

                                if ((*readings)[i].Value > highestPoint)
                                {
                                    highestPoint = (*readings)[i].Value;
                                    highestIndex = i;
                                }

                                if ((*readings)[i].Value < lowestPoint)
                                {
                                    lowestPoint = (*readings)[i].Value;
                                    lowestIndex = i;
                                }
                            }

                            CSharpOutOfRange(highestIndex - 1, readings->size());
                            CSharpOutOfRange(highestIndex, readings->size());
                            CSharpOutOfRange(highestIndex + 1, readings->size());


                            CSharpOutOfRange(lowestIndex - 1, readings->size());
                            CSharpOutOfRange(lowestIndex, readings->size());
                            CSharpOutOfRange(lowestIndex + 1, readings->size());

                            // now we have highest and lowest points.
                            if (std::abs(highestIndex - lowestIndex) != 1)
                            {
                                // if the points are more than 1 point apart, take the mean of the 2 values surrounding these indices
                                (*readings)[highestIndex].Value = ((*readings)[highestIndex - 1].Value + (*readings)[highestIndex + 1].Value) / 2;
                                (*readings)[lowestIndex].Value = ((*readings)[lowestIndex - 1].Value + (*readings)[lowestIndex + 1].Value) / 2;
                            }
                            else
                            {
                                // if the points are 1 apart, make both points equal to the mean of the 2 values around both points
                                (*readings)[lowestIndex].Value = (*readings)[highestIndex].Value =
                                    ((*readings)[std::min(highestIndex, lowestIndex) - 1].Value +
                                    (*readings)[std::max(highestIndex, lowestIndex) + 1].Value) / 2;
                            }
                        }

                        // we have our first and last point positions.. go through and figure out either the noise or the
                        // second derivative and see if it goes outside the limit
                        for (int i = firstPoint; i <= lastPoint; i++)
                        {
                            double valueAtCurrentPoint;
                            double tempMean = 0;
                            double tempSlope = 0;
                            double tempSecond = 0;
                            double tempNoise = 0;
                            int tempFirstPoint = 0;
                            int tempLastPoint = 0;

                            try
                            {
                                CSharpOutOfRange(i - ((pointsInWindow - 1) / 2), readings->size());
                                CSharpOutOfRange(i + (pointsInWindow / 2) + 1, readings->size());

                                FindWindowParams(readings, (*readings)[i - ((pointsInWindow - 1) / 2)].Time,
                                    (*readings)[i + (pointsInWindow / 2) + 1].Time - (*readings)[i - ((pointsInWindow - 1) / 2)].Time,
                                    tempMean, tempSlope, tempNoise, tempSecond, tempFirstPoint,
                                    tempLastPoint, i - ((pointsInWindow - 1) / 2),
                                    // ! is noise.. and it also has to have a divided by mean
                                    (!CreaNoiseVsSecondDipDetect(flagParameter) && divideByMeanToGetNoise),
                                    subtractFromValues, false,
                                    lowerLimitForNoise,
                                    (pointsInWindow - 1) / 2,
                                    pointsInWindow / 2);
                            }
                            catch (...)
                            {
                                // may not be enough points
                            }

                            // assume 0 is noise and 1 is second derivative
                            if (CreaNoiseVsSecondDipDetect(flagParameter))
                            {
                                // divide second by mean
                                if (divideByMeanToGetNoise)
                                {
                                    valueAtCurrentPoint = CSharpDecimalCalculation((tempSecond / std::max(std::abs(tempMean), std::abs(lowerLimitForNoise))) * 100);
                                }
                                else
                                {
                                    valueAtCurrentPoint = tempSecond;
                                }
                            }
                            else
                            {
                                valueAtCurrentPoint = tempNoise;
                            }

                            if (CreaNoiseVsSecondDipDetect(flagParameter))
                            {
                                // second derivative is under the limit
                                if (valueAtCurrentPoint < noiseLimit)
                                {
                                    returnValue = true;
                                    break;
                                }
                            }
                            else
                            {
                                // noise is over the limit
                                if (std::abs(valueAtCurrentPoint) > noiseLimit)
                                {
                                    returnValue = true;
                                    break;
                                }
                            }
                        }

                        if (removeHighestLowest)
                        {
                            // put the highest and lowest points back into the readings
                            (*readings)[highestIndex].Value = highestPoint;
                            (*readings)[lowestIndex].Value = lowestPoint;
                        }

                        return returnValue;
                    }

                    return false;
                }

                ResultsCalcReturnCode AnalyticalManager::FindParams(std::shared_ptr<std::vector<Reading>> readings,
                    double bubbleDetect,
                    double sampleDetect,
                    std::shared_ptr<SensorInfo> sensorInfo,
                    std::shared_ptr<Levels> levels,
                    TestMode testMode,
                    bool useT,
                    bool getSamplePeakParams,
                    bool divideByMeanToGetNoise,
                    double subtractFromValues,
                    TestMode qcAs,
                    double meanLowerLimitForNoise,
                    bool dependentWindowMoveBack)
                {
                    ResultsCalcReturnCode tempRc = ResultsCalcReturnCode::Success;
                    ResultsCalcReturnCode rc = ResultsCalcReturnCode::Success;
                    double lastTime = ((*readings)[readings->size() - 1]).Time;
                    bool calWindowDips = false;
                    bool calWindowDrift = false;

                    // i will only calculate for 1 second of time
                    if (lastTime < (sensorInfo->calDelimit + 1))
                    {
                        return ResultsCalcReturnCode::CannotCalculate;
                    }

                    if (bubbleDetect == 0)
                    {
                        bubbleDetect = lastTime;
                    }

                    // we only check cal window dips for crea
                    if (sensorInfo->sensorType == Sensors::Creatinine)
                    {
                        // detect dips in the creatinine cal window first
                        calWindowDips = DetectWindowDips(readings,
                            bubbleDetect - sensorInfo->calDelimit,  // window start
                            sensorInfo->calWindowSize,              // window size
                            0,                                     // points to skip 0 for cal window
                            99999,                                 // points to stop after
                            (int)sensorInfo->param70,               // points in window
                            sensorInfo->param75,                    // noise high
                            sensorInfo->param15,                    // flag parameter
                            AmpNoiseDividedMean(sensorInfo->param15),
                            subtractFromValues,
                            meanLowerLimitForNoise,
                            true);               // whether to divide noise by mean
                    }

                    double lastCalNoise = DBL_MAX;
                    int i = 0;

                    for (i = 0; i < MaxCalMoveBack + 1; i++)
                    {
                        // get cal parameters.. if not enough cal data for the window specified in
                        // the sensor descriptor, use whatever cal data there is.
                        rc = FindWindowParams(readings,
                            bubbleDetect - sensorInfo->calDelimit - i,
                            std::min(lastTime, sensorInfo->calWindowSize),
                            levels->calMean,
                            levels->calSlope,
                            levels->calNoise,
                            levels->calSecond,
                            levels->calFirst,
                            levels->calLast,
                            0,
                            divideByMeanToGetNoise,
                            0,
                            NewNoise(sensorInfo->param15),
                            meanLowerLimitForNoise,
                            DefaultPointsToLeftForSecondDerivative,
                            DefaultPointsToRightForSecondDerivative);

                        // replaced != success.. because it could be a cal window dip now
                        if (rc == ResultsCalcReturnCode::CannotCalculate)
                            return rc;

                        // dont move back if it's creatinine and
                        // the drift fails low (this is probably a dip within the cal window, and moving back would put
                        // the dip between the cal and sample window
                        if ((sensorInfo->sensorType == Sensors::Creatinine) && (levels->calSlope < sensorInfo->CalDriftLowQC))
                        {
                            // this will also prevent another sensor from moving back crea
                            calWindowDrift = true;
                        }

                        // are we supposed to do this ? no cal move back, we're done. otherwise go into the cal move back code
                        // don't do a move back if there are dips in the cal window..
                        if (!CalWindowMoveBack(sensorInfo->param15) || calWindowDips || calWindowDrift)
                        {
                            break;
                        }
                        else
                        {
                            if (i == 0)
                            {
                                // if its the first pass ...
                                if (levels->calNoise <= sensorInfo->CalNoiseHighQC)
                                {
                                    // and the noise was acceptable then we're done
                                    break;
                                }
                                else
                                {
                                    // else we come back into the loop
                                    lastCalNoise = levels->calNoise;
                                }
                            }
                            else
                            {
                                // if the noise is over the value.. register it and move back
                                if (levels->calNoise > sensorInfo->CalNoiseHighQC)
                                {
                                    lastCalNoise = levels->calNoise;
                                }
                                else
                                {
                                    if (levels->calNoise > lastCalNoise)
                                    {
                                        // noise went up since last noise.. but we're still under... which means
                                        // the last one was under too.. so we need to go back and use the last one
                                        i--;

                                        rc = FindWindowParams(readings,
                                            bubbleDetect - sensorInfo->calDelimit - i,
                                            std::min(lastTime, sensorInfo->calWindowSize),
                                            levels->calMean,
                                            levels->calSlope,
                                            levels->calNoise,
                                            levels->calSecond,
                                            levels->calFirst,
                                            levels->calLast,
                                            0,
                                            divideByMeanToGetNoise,
                                            0,
                                            NewNoise(sensorInfo->param15),
                                            meanLowerLimitForNoise,
                                            DefaultPointsToLeftForSecondDerivative,
                                            DefaultPointsToRightForSecondDerivative);

                                        break;
                                    }
                                    else if (levels->calNoise > (lastCalNoise * 0.9))
                                    {
                                        // there wasnt at least a 10% improvement.. just stop here
                                        break;
                                    }
                                    else if (i == MaxCalMoveBack)
                                    {
                                        // we are at the last one.. the noise keeps improving by more than 10%
                                        // each time.. but we cant go any further.. so just stop
                                        break;
                                    }
                                    else
                                    {
                                        // otherwise there was better than 10% improvement.. we arent at the end,
                                        // so keep going and see how long it keeps improving
                                        lastCalNoise = levels->calNoise;
                                    }
                                }
                            }
                        }
                    }

                    // if there was no error in the window, but there was a dip in the window, then set the
                    // return code to dips.
                    if (calWindowDips && (rc == ResultsCalcReturnCode::Success))
                    {
                        rc = ResultsCalcReturnCode::CalWindowDip;
                    }

                    // the counter tells us how many seconds the cal window had to be moved back until
                    // we got decent noise. if it wasnt moved back it will be 0. max is 6 if we kept
                    // moving it back and couldnt find a good window and the noise (if not something
                    // before), will fail iqc
                    levels->calWindowMovedBack = i;

                    // if the dependent window has moved back, then move back this cal window by 5 and try to get its parameters again
                    if (dependentWindowMoveBack && !calWindowDips && !calWindowDrift)
                    {
                        levels->calWindowMovedBack = MaxCalMoveBack;

                        rc = FindWindowParams(readings,
                            bubbleDetect - sensorInfo->calDelimit - MaxCalMoveBack,
                            std::min(lastTime, sensorInfo->calWindowSize),
                            levels->calMean,
                            levels->calSlope,
                            levels->calNoise,
                            levels->calSecond,
                            levels->calFirst,
                            levels->calLast,
                            0,
                            divideByMeanToGetNoise,
                            0,
                            NewNoise(sensorInfo->param15),
                            meanLowerLimitForNoise,
                            DefaultPointsToLeftForSecondDerivative,
                            DefaultPointsToRightForSecondDerivative);
                    }

                    try
                    {
                        if (sensorInfo->param50 != 0)
                        {
                            double windowStart = 0;

                            if ((sensorInfo->readerNoiseLow == readerNoiseLowBubbleDetect) ||
                                (sensorInfo->readerNoiseLow == readerNoiseLowAbsoluteAfterTestEnds))
                            {
                                if (sensorInfo->readerNoiseLow == readerNoiseLowBubbleDetect)
                                {
                                    windowStart = bubbleDetect - sensorInfo->param49;
                                }
                                else
                                {
                                    windowStart = sensorInfo->param49;
                                }

                                FindWindowParams(readings,
                                    windowStart,
                                    sensorInfo->param50,
                                    levels->additionalMean,
                                    levels->additionalSlope,
                                    levels->additionalNoise,
                                    levels->additionalSecond,
                                    levels->additionalFirst,
                                    levels->additionalLast,
                                    0,
                                    divideByMeanToGetNoise,
                                    0,
                                    NewNoise(sensorInfo->param15),
                                    meanLowerLimitForNoise,
                                    DefaultPointsToLeftForSecondDerivative,
                                    DefaultPointsToRightForSecondDerivative);

                                // ex = additional window extrapolated either back or forward to the extrapolation time from sample detect
                                levels->additionalEx = levels->additionalMean + levels->additionalSlope *
                                    ((sensorInfo->extrapolation) - (windowStart + (sensorInfo->param50 / 2)));
                            }
                        }
                    }
                    catch (...)
                    {
                    }

                    // at this point, we've got cal parameters. if no sample yet, or not enough
                    // time has elapsed
                    if ((sampleDetect == 0) || (lastTime < (sampleDetect + sensorInfo->sampleDelimit)))
                    {
                        return ResultsCalcReturnCode::CannotCalculate;
                    }
                    else
                    {
                        // get sample parameters, if not enough sample data to fill up whole
                        // window, use whatever data there is
                        tempRc = FindWindowParams(readings,
                            sampleDetect + sensorInfo->sampleDelimit,
                            std::min(lastTime - (sampleDetect + sensorInfo->sampleDelimit), sensorInfo->sampleWindowSize),
                            levels->sampleMean,
                            levels->sampleSlope,
                            levels->sampleNoise,
                            levels->sampleSecond,
                            levels->sampleFirst,
                            levels->sampleLast,
                            levels->calLast,
                            divideByMeanToGetNoise,
                            subtractFromValues,
                            NewNoise(sensorInfo->param15),
                            meanLowerLimitForNoise,
                            DefaultPointsToLeftForSecondDerivative,
                            DefaultPointsToRightForSecondDerivative);

                        if (rc == ResultsCalcReturnCode::Success)
                            rc = tempRc;

                        // go through the sample window and try to find any dips or spikes.. make sure we stop checking
                        // after the windows don't matter anymore
                        tempRc = DetectDipsAndSpikes(readings,
                            sensorInfo,
                            sampleDetect,
                            qcAs,
                            sampleDetect + std::max(sensorInfo->postDelimit + sensorInfo->postWindowSize,
                                sensorInfo->sampleDelimit + sensorInfo->sampleWindowSize),
                            divideByMeanToGetNoise);

                        if (rc == ResultsCalcReturnCode::Success)
                            rc = tempRc;

                        if ((rc == ResultsCalcReturnCode::Success) && (sensorInfo->sensorType == Sensors::Creatinine))
                        {
                            if (DetectWindowDips(readings,
                                sampleDetect,
                                std::max(sensorInfo->postDelimit + sensorInfo->postWindowSize,
                                    sensorInfo->sampleDelimit + sensorInfo->sampleWindowSize),
                                    (int)sensorInfo->param71,  // early points to skip
                                (int)sensorInfo->param73,  // points to stop after.. basically late points to skip
                                (int)sensorInfo->param93,  // points in window
                                sensorInfo->param72,       // early noise limit
                                sensorInfo->param15,
                                AmpNoiseDividedMean(sensorInfo->param15),
                                subtractFromValues,
                                meanLowerLimitForNoise,
                                false))
                            {
                                tempRc = ResultsCalcReturnCode::SampleDipEarly;
                            }
                        }

                        if (rc == ResultsCalcReturnCode::Success)
                            rc = tempRc;

                        if ((rc == ResultsCalcReturnCode::Success) && (sensorInfo->sensorType == Sensors::Creatinine))
                        {
                            if (DetectWindowDips(readings,
                                sampleDetect,
                                std::max(sensorInfo->postDelimit + sensorInfo->postWindowSize,
                                    sensorInfo->sampleDelimit + sensorInfo->sampleWindowSize),
                                    (int)sensorInfo->param73,  // late points to skip
                                99999,                    // points to stop after
                                (int)sensorInfo->param93,  // points in window
                                sensorInfo->param74,       // late noise limit
                                sensorInfo->param15,
                                AmpNoiseDividedMean(sensorInfo->param15),
                                subtractFromValues,
                                meanLowerLimitForNoise,
                                false))
                            {
                                tempRc = ResultsCalcReturnCode::SampleDipLate;
                            }
                        }

                        if (rc == ResultsCalcReturnCode::Success)
                            rc = tempRc;
                    }

                    // get post parameters.. parameters may be out of whack if not set correctly, or there may
                    // not be enough data for a post window.. so if it throws an exception, set rc to cnc
                    try
                    {
                        tempRc = FindWindowParams(readings,
                            sampleDetect + sensorInfo->postDelimit,
                            std::min(lastTime - (sampleDetect + sensorInfo->postDelimit), sensorInfo->postWindowSize),
                            levels->postMean,
                            levels->postSlope,
                            levels->postNoise,
                            levels->postSecond,
                            levels->postFirst,
                            levels->postLast,
                            levels->calLast,
                            divideByMeanToGetNoise,
                            0,
                            NewNoise(sensorInfo->param15),
                            meanLowerLimitForNoise,
                            DefaultPointsToLeftForSecondDerivative,
                            DefaultPointsToRightForSecondDerivative);
                    }
                    catch (...)
                    {
                        tempRc = ResultsCalcReturnCode::CannotCalculate;
                    }

                    // if tplus, tminus are not being used and the post window failed, just ignore it becuase
                    // the post window values won't actually be used
                    if (!useT)
                    {
                        if (tempRc != ResultsCalcReturnCode::Success)
                        {
                            tempRc = ResultsCalcReturnCode::Success;
                        }
                    }

                    if (rc == ResultsCalcReturnCode::Success)
                        rc = tempRc;

                    if (getSamplePeakParams)
                    {
                        try
                        {
                            // get params for sw peak
                            tempRc = FindWindowParams(readings,
                                sampleDetect + sensorInfo->param9, // window start
                                sensorInfo->param10, // window size
                                levels->peakMean,
                                levels->peakSlope,
                                levels->peakNoise,
                                levels->peakSecond,
                                levels->peakFirst,
                                levels->peakLast,
                                levels->calLast,
                                divideByMeanToGetNoise,
                                0,
                                NewNoise(sensorInfo->param15),
                                meanLowerLimitForNoise,
                                DefaultPointsToLeftForSecondDerivative,
                                DefaultPointsToRightForSecondDerivative); // use the end of the cal window as the first point to search from
                        }
                        catch (...)
                        {
                            tempRc = ResultsCalcReturnCode::CannotCalculate;
                        }
                    }

                    if (rc == ResultsCalcReturnCode::Success)
                    {
                        rc = tempRc;
                    }

                    // get additional window parameters
                    try
                    {
                        if (sensorInfo->param50 != 0)
                        {
                            double windowStart = 0;

                            if (sensorInfo->readerNoiseLow == readerNoiseLowBubbleDetect)
                            {
                                windowStart = bubbleDetect - sensorInfo->param49;
                            }
                            else if ((sensorInfo->readerNoiseLow == readerNoiseLowDefault) ||
                                (sensorInfo->readerNoiseLow == readerNoiseLowDefault2) ||
                                (sensorInfo->readerNoiseLow == readerNoiseLowSampleDetect))
                            {
                                windowStart = sampleDetect + sensorInfo->param49;
                            }
                            else
                            {
                                // just do it absolute if its not bubble detect or sample detect so that
                                // we have the levels after a test is recalculated. this includes
                                // absolute during realtime and absolute after test ends and
                                windowStart = sensorInfo->param49;
                            }

                            tempRc = FindWindowParams(readings,
                                windowStart,
                                sensorInfo->param50,
                                levels->additionalMean,
                                levels->additionalSlope,
                                levels->additionalNoise,
                                levels->additionalSecond,
                                levels->additionalFirst,
                                levels->additionalLast,
                                0,
                                divideByMeanToGetNoise,
                                0,
                                NewNoise(sensorInfo->param15),
                                meanLowerLimitForNoise,
                                DefaultPointsToLeftForSecondDerivative,
                                DefaultPointsToRightForSecondDerivative);

                            // ex = additional window extrapolated either back or forward to the extrapolation time from sample detect
                            levels->additionalEx = levels->additionalMean + levels->additionalSlope *
                                ((sensorInfo->extrapolation) - (windowStart + (sensorInfo->param50 / 2)));
                        }
                    }
                    catch (...)
                    {
                        tempRc = ResultsCalcReturnCode::CannotCalculate;
                    }

                    if (rc == ResultsCalcReturnCode::Success)
                    {
                        rc = tempRc;
                    }

                    return rc;
                }

                ResultsCalcReturnCode AnalyticalManager::DetectDipsAndSpikes(std::shared_ptr<std::vector<Reading>> readings,
                    std::shared_ptr<SensorInfo> sensorInfo,
                    double sampleDetect,
                    TestMode testMode,
                    double lastTime,
                    bool divideByMeanToGetNoise)
                {
                    ResultsCalcReturnCode rc = ResultsCalcReturnCode::Success;

                    int toSkip;
                    int pointsInWindow;
                    double noiseHigh;

                    int lateToSkip;
                    int latePointsInWindow;
                    double lateNoiseHigh;

                    // if it's blood, do blood dip/spike detection. if its aqueous, do aqueous. if we don't know
                    // just return success
                    if (testMode == TestMode::BloodTest)
                    {
                        toSkip = sensorInfo->bloodPointsToSkip;
                        pointsInWindow = sensorInfo->bloodPointsInWindow;
                        noiseHigh = sensorInfo->bloodNoiseHigh;

                        lateToSkip = sensorInfo->lateBloodPointsToSkip;
                        latePointsInWindow = sensorInfo->lateBloodPointsInWindow;
                        lateNoiseHigh = sensorInfo->lateBloodNoiseHigh;
                    }
                    else
                    {
                        toSkip = sensorInfo->aqPointsToSkip;
                        pointsInWindow = sensorInfo->aqPointsInWindow;
                        noiseHigh = sensorInfo->aqNoiseHigh;

                        lateToSkip = sensorInfo->lateAqPointsToSkip;
                        latePointsInWindow = sensorInfo->lateAqPointsInWindow;
                        lateNoiseHigh = sensorInfo->lateAqNoiseHigh;
                    }
                    /*else
                    {
                    return ResultsCalcReturnCode.Success;
                    }*/

                    bool switched = false;

                    int sampleFirst = 0;
                    size_t idx = 0;

                    while (((*readings)[idx].Time < sampleDetect) && (idx < readings->size()))
                    {
                        idx++;
                    }

                    sampleFirst = idx;

                    // we want to go to the end of the sample window.. or until the time goes over the end of the last
                    // bit of data that matters (thats basically the end of the post or sample window.. whichever is later)
                    for (int i = sampleFirst + toSkip; (i < readings->size() - latePointsInWindow) && ((*readings)[i].Time < lastTime); i++)
                    {
                        CSharpOutOfRange(i, readings->size());

                        double highest = -DBL_MAX;
                        double lowest = DBL_MAX;
                        double first = (*readings)[i].Value;

                        // switch over to late parameters once we've done lateToSkip windows
                        if (!switched && (i >= (sampleFirst + lateToSkip)))
                        {
                            toSkip = lateToSkip;
                            pointsInWindow = latePointsInWindow;
                            noiseHigh = lateNoiseHigh;
                            switched = true;
                        }

                        double noiseValue = 0;

                        // find the highest and lowest data points
                        for (int j = i; j < i + pointsInWindow; j++)
                        {
                            CSharpOutOfRange(j, readings->size());

                            if ((*readings)[j].Value > highest)
                                highest = (*readings)[j].Value;

                            if ((*readings)[j].Value < lowest)
                                lowest = (*readings)[j].Value;
                        }

                        noiseValue = std::abs(highest - lowest);

                        if (divideByMeanToGetNoise)
                        {
                            noiseValue = CSharpDecimalCalculation((noiseValue / GetMean(readings, i, pointsInWindow)) * 100);
                        }


                        if (noiseValue > noiseHigh)
                        {
                            if (std::abs(highest - first) > std::abs(lowest - first))
                            {
                                // the lowest is closer to the first value than the highest.. so a spike
                                if (switched)
                                {
                                    if (rc == ResultsCalcReturnCode::Success)
                                    {
                                        rc = ResultsCalcReturnCode::LateSpikeInSample;
                                    }
                                }
                                else
                                {
                                    if (rc == ResultsCalcReturnCode::Success)
                                    {
                                        rc = ResultsCalcReturnCode::EarlySpikeInSample;
                                    }
                                }
                            }
                            else
                            {
                                // otherwise its a dip.. but only ov
                                if (switched)
                                {
                                    if (rc == ResultsCalcReturnCode::Success)
                                    {
                                        rc = ResultsCalcReturnCode::LateDipInSample;
                                    }
                                }
                                else
                                {
                                    if (rc == ResultsCalcReturnCode::Success)
                                    {
                                        rc = ResultsCalcReturnCode::EarlyDipInSample;
                                    }
                                }
                            }

                            break;
                        }
                    }

                    return rc;
                }

                double AnalyticalManager::GetMean(std::shared_ptr<std::vector<Reading>> readings,
                    int startAt,
                    int howMany)
                {
                    double temp = 0;

                    for (int i = startAt; i < startAt + howMany; i++)
                    {
                        CSharpOutOfRange(i, readings->size());

                        temp += (*readings)[i].Value;
                    }

                    return CSharpDecimalCalculation(temp / howMany);
                }

                ResultsCalcReturnCode AnalyticalManager::CheckAllPointsWithinLimits(std::shared_ptr<SensorReadings> reading,
                    int firstPoint,
                    int lastPoint,
                    double low,
                    double high)
                {
                    for (int i = firstPoint; i <= lastPoint; i++)
                    {
                        CSharpOutOfRange(i, reading->readings->size());

                        if ((*reading->readings)[i].Value < low)
                        {
                            return ResultsCalcReturnCode::Success;
                        }
                        else if ((*reading->readings)[i].Value > high)
                        {
                            return ResultsCalcReturnCode::Success;
                        }
                    }

                    return ResultsCalcReturnCode::SampleWindowAllPointCheck;
                }

                double AnalyticalManager::GetMean(std::shared_ptr<std::vector<Reading>> readings,
                    double startTime,
                    double windowSize)
                {
                    double total = 0;
                    int numPoints = 0;

                    for (size_t i = 0; i < readings->size(); i++)
                    {
                        if (((*readings)[i].Time >= startTime) && ((*readings)[i].Time <= (startTime + windowSize)))
                        {
                            total += (*readings)[i].Value;
                            numPoints++;
                        }
                        else if ((*readings)[i].Time >(startTime + windowSize))
                        {
                            break;
                        }
                    }

                    if (numPoints == 0)
                    {
                        // divide by 0 otherwise
                        return DBL_MAX;
                    }
                    else
                    {
                        return CSharpDecimalCalculation(total / numPoints);
                    }
                }

#ifndef NEXT_GEN
                ResultsCalcReturnCode AnalyticalManager::FindWindowParams(std::shared_ptr<std::vector<Reading>> readings,
                    double windowStart,
                    double windowSize,
                    double &mean,
                    double &slope,
                    double &noise,
                    double &second,
                    int &firstPoint,
                    int &lastPoint,
                    int startAt,
                    bool divideByMeanToGetNoise,
                    double subtractFromValues,
                    bool useNewNoise,
                    double meanLowerLimitForNoise,
                    int pointsToLeft,
                    int pointsToRight)
                {
                    int i;
                    int numPoints = 0;
                    double sumxy = 0;
                    double sumx = 0;
                    double sumy = 0;
                    double sumx2 = 0;
                    double tempDecimal;
                    double offset;
                    double bias;

                    firstPoint = -1;
                    lastPoint = -1;

                    numPoints = GetFirstLastPoint(readings, startAt, windowStart, windowSize, firstPoint, lastPoint);

                    // we need the extra 2 points because the second derivative slope needs
                    // 2 extra points before and after the window to calculate
                    if ((numPoints <= 2) ||
                        (lastPoint <= firstPoint) ||
                        (readings->size() < (lastPoint + pointsToRight)) ||
                        (lastPoint == -1) ||
                        (firstPoint == -1))
                    {
                        return ResultsCalcReturnCode::CannotCalculate;
                    }

                    second = GetSecondDerivative(readings, firstPoint, lastPoint, pointsToLeft, pointsToRight, subtractFromValues);

                    // set the sums back to 0
                    sumx = sumy = sumx2 = sumxy = 0;

                    double sumForBias = 0;
                    double middleOfWindow = (windowStart + 0.5 * windowSize);

                    for (i = firstPoint; i <= lastPoint; i++)
                    {
                        CSharpOutOfRange(i, readings->size());

                        sumx += (*readings)[i].Time;
                        sumy += (*readings)[i].Value - subtractFromValues;
                        sumx2 += (*readings)[i].Time * (*readings)[i].Time;
                        sumxy += (*readings)[i].Time * ((*readings)[i].Value - subtractFromValues);
                        //sumxy += (*readings)[i].Time * (*readings)[i].Value;
                    }

                    mean = CSharpDecimalCalculation(sumy / numPoints);
                    slope = CSharpDecimalCalculation((sumxy - ((sumx * sumy) / numPoints)) / (sumx2 - ((sumx * sumx) / numPoints)));
                    offset = CSharpDecimalCalculation(mean - slope * (sumx / numPoints));

                    for (i = firstPoint; i <= lastPoint; i++)
                    {
                        CSharpOutOfRange(i, readings->size());

                        sumForBias += std::pow((*readings)[i].Time - middleOfWindow, 2) *
                            second / 2 +
                            slope * (*readings)[i].Time +
                            offset;
                    }

                    // calculate bias
                    bias = mean - CSharpDecimalCalculation(sumForBias / numPoints);
                    tempDecimal = 0;
                    double curvedNoise = 0;
                    double flatNoise = 0;
                    double factor = 1;

                    for (i = firstPoint; i <= lastPoint; i++)
                    {
                        CSharpOutOfRange(i, readings->size());

                        tempDecimal = (((*readings)[i].Value - subtractFromValues) -
                            slope * (*readings)[i].Time - offset);
                        flatNoise += CSharpDecimalCalculation((tempDecimal * tempDecimal) / (numPoints - 2));

                        // new noise .. see srsrciqc
                        tempDecimal = ((*readings)[i].Value - subtractFromValues) -
                            slope * (*readings)[i].Time - offset - factor *
                            (std::pow((*readings)[i].Time - middleOfWindow, 2) *
                            (second / 2) + bias);

                        curvedNoise += CSharpDecimalCalculation((tempDecimal * tempDecimal) / (numPoints - 2));
                    }

                    curvedNoise = sqrt(curvedNoise);
                    flatNoise = sqrt(flatNoise);

                    if (divideByMeanToGetNoise)
                    {
                        curvedNoise = CSharpDecimalCalculation(curvedNoise / std::max(std::abs(mean), std::abs(meanLowerLimitForNoise))) * 100;
                        flatNoise = CSharpDecimalCalculation(flatNoise / std::max(std::abs(mean), std::abs(meanLowerLimitForNoise))) * 100;
                    }

                    if (useNewNoise)
                    {
                        noise = std::min(flatNoise, curvedNoise);
                    }
                    else
                    {
                        noise = flatNoise;
                    }

                    return ResultsCalcReturnCode::Success;
                }


                int AnalyticalManager::GetFirstLastPoint(std::shared_ptr<std::vector<Reading>> readings, int startAt, double windowStart, double windowSize, int &firstPoint, int &lastPoint)
                {
                    int numPoints = 0;
                    if (readings->size() < 2)
                    {
                        return numPoints;
                    }
                    firstPoint = GetFirstGreaterPoint(startAt, readings->size() - 1, readings, windowStart);

                    if (firstPoint == -1)
                    {
                        return numPoints;
                    }
                    lastPoint = GetFirstGreaterPoint(firstPoint, readings->size() - 1, readings, windowStart + windowSize);

                    numPoints = lastPoint - firstPoint;
                    lastPoint = lastPoint - 1;
                    return numPoints;
                }

                int AnalyticalManager::GetFirstGreaterPoint(int startIdx, int endIdx, std::shared_ptr<std::vector<Reading>> readings, double windowStart)
                {
                    int roundingdecimal = 2;
                    int mid = (startIdx + endIdx) / 2;
                    double midVal = MathHelp::Round((readings->at(mid)).Time, roundingdecimal);
                    int nextIdx = 0;
                    if (readings->size() <= mid + 1)
                    {
                        nextIdx = readings->size() - 1;
                    }
                    else
                    {
                        nextIdx = mid + 1;
                    }
                    double midVal2 = MathHelp::Round((readings->at(nextIdx)).Time, roundingdecimal);
                    double winVal = MathHelp::Round(windowStart, roundingdecimal);
                    double endVal = MathHelp::Round((readings->at(endIdx)).Time, roundingdecimal);
                    if (windowStart > endVal)
                    {
                        return -1;
                    }
                    if ((startIdx + endIdx) % 2 != 0)
                    {
                        if ((winVal <= midVal2) && (winVal > midVal))
                        {
                            return mid + 1;
                        }
                    }
                    if (midVal > winVal)
                    {
                        if (startIdx == mid)
                        {
                            return mid;
                        }
                        return GetFirstGreaterPoint(startIdx, mid, readings, windowStart);
                    }
                    else if (midVal == winVal)
                    {
                        return mid;
                    }
                    else //(midVal < winVal)
                    {
                        if (mid == endIdx)
                        {
                            return -1;
                        }
                        return GetFirstGreaterPoint(mid, endIdx, readings, windowStart);
                    }
                }
#else
                // NOTE: This uses the optimization changes by Aditya Matam(co-op student)
                ResultsCalcReturnCode AnalyticalManager::FindWindowParams(std::shared_ptr<std::vector<Reading>> readings,
                    double windowStart,
                    double windowSize,
                    double &mean,
                    double &slope,
                    double &noise,
                    double &second,
                    int &firstPoint,
                    int &lastPoint,
                    int startAt,
                    bool divideByMeanToGetNoise,
                    double subtractFromValues,
                    bool useNewNoise,
                    double meanLowerLimitForNoise,
                    int pointsToLeft,
                    int pointsToRight)
                {
                    int i;
                    int numPoints = 0;
                    double sumxy = 0;
                    double sumx = 0;
                    double sumy = 0;
                    double sumx2 = 0;
                    double tempDecimal;
                    double offset;
                    double bias;
                    double endTime = 0;
                    double timeStep = 0;
                    double timeDifference = -1; // Time from end of results to start of window
                    double windowEnd = 0;
                    int firstPointGuess = -1;
                    int lastPointGuess = -1;
                    
                    firstPoint = -1;
                    lastPoint = -1;
                    if (readings->size() > 1)
                    {
                        endTime = (*readings)[readings->size()-1].Time;
                        timeStep = (*readings)[readings->size()-1].Time - (*readings)[readings->size()-2].Time;
                        timeDifference = endTime - windowStart; // Time from end of results to start of window
                        windowEnd = windowStart + windowSize;
                    }
                    // Otherwise timeDifference stays at -1 and the if statement below is skipped
                    if (timeDifference >= 0 && windowEnd <= endTime)
                    {
                        // Mathematically determine an initial guess for the start and end points
                        // Then check if the guess was right: if not, perform a search to find the actual point
                        firstPointGuess = (readings->size()-1) - timeDifference/timeStep;
                        CSharpOutOfRange(firstPointGuess, readings->size());

                        // Off by more than a time step or time at first point is less than time at window start
                        if ((*readings)[firstPointGuess].Time > windowStart)
                        {
                            // Time at first point is greater than window start -> decrease until just above
                            for (int x = firstPointGuess; (*readings)[firstPointGuess].Time > windowStart; x--)
                            {
                                firstPointGuess = x;
                            }
                        }
                        // Time at first point is less than at window start -> increase until just above
                        else 
                        {
                            
                            for (int x = firstPointGuess; (*readings)[firstPointGuess].Time < windowStart ; x++)
                            {
                                firstPointGuess = x;
                            }
                        }
                        
                        lastPointGuess = firstPointGuess + windowSize/timeStep;
                        CSharpOutOfRange(lastPointGuess, readings->size());

                        // Off by more than a time step, or time at the last point is greater than the window size
                        // Time at last point is less than total window -> increment until just above
                        if ((*readings)[lastPointGuess].Time < windowStart+windowSize)
                        {
                            for (int x = lastPointGuess; (*readings)[lastPointGuess].Time < windowStart+windowSize ; x++)
                            {
                                lastPointGuess = x;
                            }
                        }
                        // Time at last point is greater than total window, decrement until just before, then take that point
                        else
                        {
                            for (int x = lastPointGuess; (*readings)[lastPointGuess].Time >= windowStart + windowSize; x--)
                            {
                                lastPointGuess = x;
                            }
                        }
                        
                        numPoints = lastPointGuess - firstPointGuess + 1; // Inclusive
                        firstPoint = firstPointGuess;
                        lastPoint = lastPointGuess;
                    }

                    // we need the extra 2 points because the second derivative slope needs
                    // 2 extra points before and after the window to calculate
                    if ((numPoints <= 2) ||
                        (lastPoint <= firstPoint) ||
                        (readings->size() < (lastPoint + pointsToRight)) ||
                        (lastPoint == -1) ||
                        (firstPoint == -1))
                    {
                        return ResultsCalcReturnCode::CannotCalculate;
                    }

                    second = GetSecondDerivative(readings, firstPoint, lastPoint, pointsToLeft, pointsToRight, subtractFromValues);

                    // set the sums back to 0
                    sumx = sumy = sumx2 = sumxy = 0;

                    double sumForBias = 0;
                    double middleOfWindow = (windowStart + 0.5 * windowSize);

                    for (i = firstPoint; i <= lastPoint; i++)
                    {
                        CSharpOutOfRange(i, readings->size());

                        sumx += (*readings)[i].Time;
                        sumy += (*readings)[i].Value - subtractFromValues;
                        sumx2 += (*readings)[i].Time * (*readings)[i].Time;
                        sumxy += (*readings)[i].Time * ((*readings)[i].Value - subtractFromValues);
                        //sumxy += (*readings)[i].Time * (*readings)[i].Value;
                    }

                    mean = sumy / numPoints;
                    slope = (sumxy - ((sumx * sumy) / numPoints)) / (sumx2 - ((sumx * sumx) / numPoints));
                    offset = mean - slope * (sumx / numPoints);

                    for (i = firstPoint; i <= lastPoint; i++)
                    {
                        CSharpOutOfRange(i, readings->size());

                        sumForBias += std::pow((*readings)[i].Time - middleOfWindow, 2) *
                            second / 2 +
                            slope * (*readings)[i].Time +
                            offset;
                    }

                    // calculate bias
                    bias = mean - (sumForBias / numPoints);
                    tempDecimal = 0;
                    double curvedNoise = 0;
                    double flatNoise = 0;
                    double factor = 1;

                    for (i = firstPoint; i <= lastPoint; i++)
                    {
                        CSharpOutOfRange(i, readings->size());

                        tempDecimal = (((*readings)[i].Value - subtractFromValues) -
                            slope * (*readings)[i].Time - offset);
                        flatNoise += (tempDecimal * tempDecimal) / (numPoints - 2);

                        // new noise .. see srsrciqc
                        tempDecimal = ((*readings)[i].Value - subtractFromValues) -
                            slope * (*readings)[i].Time - offset - factor *
                            (pow((*readings)[i].Time - middleOfWindow, 2) *
                            (second / 2) + bias);

                        curvedNoise += (tempDecimal * tempDecimal) / (numPoints - 2);
                    }

                    curvedNoise = sqrt(curvedNoise);
                    flatNoise = sqrt(flatNoise);

                    if (divideByMeanToGetNoise)
                    {
                        curvedNoise = (curvedNoise / std::max(std::abs(mean), std::abs(meanLowerLimitForNoise))) * 100;
                        flatNoise = (flatNoise / std::max(std::abs(mean), std::abs(meanLowerLimitForNoise))) * 100;
                    }

                    if (useNewNoise)
                    {
                        noise = std::min(flatNoise, curvedNoise);
                    }
                    else
                    {
                        noise = flatNoise;
                    }

                    return ResultsCalcReturnCode::Success;
                }
#endif

                double AnalyticalManager::GetSecondDerivative(std::shared_ptr<std::vector<Reading>> readings,
                    int firstPoint,
                    int lastPoint,
                    int pointsToLeftForSlope,
                    int pointsToRightForSlope,
                    double subtractFromValues)
                {
                    int i = 0;

                    double tempDecimal = 0;
                    double sumx = 0;
                    double sumy = 0;
                    double sumx2 = 0;
                    double sumxy = 0;

                    // do the second derivative first, because we need it for the noise calculation
                    // for each point, substitute a derivative point into the slope calculations. use derivMean instead of mean
                    for (i = firstPoint; i <= lastPoint; i++)
                    {
                        tempDecimal = GetSlope(readings, i, pointsToLeftForSlope, pointsToRightForSlope, subtractFromValues);

                        CSharpOutOfRange(i, readings->size());
                        double decimalTime = (*readings)[i].Time;

                        //derivMean += currentDerivative.Time;
                        sumx += decimalTime;
                        sumy += tempDecimal;
                        sumx2 += decimalTime * decimalTime;
                        sumxy += decimalTime * tempDecimal;
                    }

                    double denominator = (double)(lastPoint - firstPoint + 1);
                    return CSharpDecimalCalculation((sumxy - CSharpDecimalCalculation((sumx * sumy) / denominator)) /
                                                    (sumx2 - CSharpDecimalCalculation((sumx * sumx) / denominator)));
                }

                double AnalyticalManager::GetSlope(std::shared_ptr<std::vector<Reading>> readings,
                    int currentIndex,
                    int includeToLeft,
                    int includeToRight,
                    double subtractFromValues)
                {
                    double sumx = 0;
                    double sumy = 0;
                    double sumxy = 0;
                    double sumx2 = 0;

                    for (int i = currentIndex - includeToLeft; i <= currentIndex + includeToRight; i++)
                    {
                        CSharpOutOfRange(i, readings->size());

                        sumx += (*readings)[i].Time;
                        sumy += (*readings)[i].Value - subtractFromValues;
                        sumx2 += (*readings)[i].Time * (*readings)[i].Time;
                        sumxy += (*readings)[i].Time * ((*readings)[i].Value - subtractFromValues);
                    }

                    double denominator = (((double)includeToLeft + (double)includeToRight) + 1.0);
                    return CSharpDecimalCalculation((sumxy - CSharpDecimalCalculation((sumx * sumy) / denominator)) /
                                                    (sumx2 - CSharpDecimalCalculation((sumx * sumx) / denominator)));
                }

                ResultsCalcReturnCode AnalyticalManager::ValidateAllQC(std::shared_ptr<Levels> levels,
                    std::shared_ptr<SensorInfo> sensorInfo,
                    bool checkSWPeakLimits,
                    bool qcCalExInsteadOfCalMean)
                {
                    if (qcCalExInsteadOfCalMean)
                    {
                        // cal qc
                        if (levels->calEx < sensorInfo->CalMeanLowQC)
                            return ResultsCalcReturnCode::CalMeanQCLow;

                        if (levels->calEx > sensorInfo->CalMeanHighQC)
                            return ResultsCalcReturnCode::CalMeanQCHigh;
                    }
                    else
                    {
                        // cal qc
                        if (levels->calMean < sensorInfo->CalMeanLowQC)
                            return ResultsCalcReturnCode::CalMeanQCLow;

                        if (levels->calMean > sensorInfo->CalMeanHighQC)
                            return ResultsCalcReturnCode::CalMeanQCHigh;
                    }

                    if (levels->calSlope < sensorInfo->CalDriftLowQC)
                        return ResultsCalcReturnCode::CalDriftQCLow;

                    if (levels->calSlope > sensorInfo->CalDriftHighQC)
                        return ResultsCalcReturnCode::CalDriftQCHigh;

                    if (levels->calSecond < sensorInfo->CalSecondLowQC)
                        return ResultsCalcReturnCode::CalSecondQCLow;

                    if (levels->calSecond > sensorInfo->CalSecondHighQC)
                        return ResultsCalcReturnCode::CalSecondQCHigh;

                    if (levels->calNoise > sensorInfo->CalNoiseHighQC)
                        return ResultsCalcReturnCode::CalNoiseQCHigh;

                    // sample qc
                    if (levels->sampleMean < sensorInfo->SampleMeanLowQC)
                        return ResultsCalcReturnCode::SampleMeanQCLow;

                    if (levels->sampleMean > sensorInfo->SampleMeanHighQC)
                        return ResultsCalcReturnCode::SampleMeanQCHigh;

                    if (levels->sampleSlope < sensorInfo->SampleDriftLowQC)
                        return ResultsCalcReturnCode::SampleDriftQCLow;

                    if (levels->sampleSlope > sensorInfo->SampleDriftHighQC)
                        return ResultsCalcReturnCode::SampleDriftQCHigh;

                    if (levels->sampleSecond < sensorInfo->SampleSecondLowQC)
                        return ResultsCalcReturnCode::SampleSecondQCLow;

                    if (levels->sampleSecond > sensorInfo->SampleSecondHighQC)
                        return ResultsCalcReturnCode::SampleSecondQCHigh;

                    if (levels->sampleNoise > sensorInfo->SampleNoiseHighQC)
                        return ResultsCalcReturnCode::SampleNoiseQCHigh;

                    // delta drift
                    if ((levels->sampleSlope - levels->calSlope) < sensorInfo->DeltaDriftLowQC)
                        return ResultsCalcReturnCode::DeltaDriftLow;

                    if ((levels->sampleSlope - levels->calSlope) > sensorInfo->DeltaDriftHighQC)
                        return ResultsCalcReturnCode::DeltraDriftHigh;

                    // skip the post validation
                    // post qc
                    if (levels->postMean < sensorInfo->PostMeanLowQC)
                        return ResultsCalcReturnCode::PostMeanQCLow;

                    if (levels->postMean > sensorInfo->PostMeanHighQC)
                        return ResultsCalcReturnCode::PostMeanQCHigh;

                    if (levels->postSlope < sensorInfo->PostDriftLowQC)
                        return ResultsCalcReturnCode::PostDriftQCLow;

                    if (levels->postSlope > sensorInfo->PostDriftHighQC)
                        return ResultsCalcReturnCode::PostDriftQCHigh;

                    if (levels->postSecond < sensorInfo->PostSecondLowQC)
                        return ResultsCalcReturnCode::PostSecondQCLow;

                    if (levels->postSecond > sensorInfo->PostSecondHighQC)
                        return ResultsCalcReturnCode::PostSecondQCHigh;

                    if (levels->postNoise > sensorInfo->PostNoiseHighQC)
                        return ResultsCalcReturnCode::PostNoiseQCHigh;

                    if (checkSWPeakLimits)
                    {
                        // use the sample window return codes for now
                        if (levels->peakNoise > sensorInfo->param12)
                        {
                            return ResultsCalcReturnCode::SWPeakNoiseHigh;
                        }
                        else if (levels->peakSlope < sensorInfo->param13)
                        {
                            return ResultsCalcReturnCode::SWPeakDriftLow;
                        }
                        else if (levels->peakSlope > sensorInfo->param14)
                        {
                            return ResultsCalcReturnCode::SWPeakDriftHigh;
                        }
                    }

                    if ((sensorInfo->readerNoiseLow == readerNoiseLowBubbleDetect) ||
                        (sensorInfo->readerNoiseLow == readerNoiseLowSampleDetect) ||
                        (sensorInfo->readerNoiseLow == readerNoiseLowAbsoluteAfterTestEnds))
                    {
                        if (levels->additionalMean < sensorInfo->readerMeanLow)
                            return ResultsCalcReturnCode::AdditionalMeanLow;

                        if (levels->additionalMean > sensorInfo->readerMeanHigh)
                            return ResultsCalcReturnCode::AdditionalMeanHigh;

                        if (levels->additionalSlope < sensorInfo->readerDriftLow)
                            return ResultsCalcReturnCode::AdditionalDriftLow;

                        if (levels->additionalSlope > sensorInfo->readerDriftHigh)
                            return ResultsCalcReturnCode::AdditionalDriftHigh;

                        if (levels->additionalNoise > sensorInfo->readerNoiseHigh)
                            return ResultsCalcReturnCode::AdditionalNoiseHigh;
                    }

                    return ResultsCalcReturnCode::Success;
                }

                ResultsCalcReturnCode AnalyticalManager::ValidateReportableRangeOnlyReturnCode(double &result, double low, double high)
                {
                    if (result < low)
                    {
                        return ResultsCalcReturnCode::UnderReportableRange;
                    }
                    else if (result > high)
                    {
                        return ResultsCalcReturnCode::OverReportableRange;
                    }
                    else
                    {
                        return ResultsCalcReturnCode::Success;
                    }
                }

                ResultsCalcReturnCode AnalyticalManager::ValidateInsanityRangeOnlyReturnCode(double &result, double low, double high)
                {
                    if (result < low)
                    {
                        return ResultsCalcReturnCode::UnderInsanityRange;
                    }
                    else if (result > high)
                    {
                        return ResultsCalcReturnCode::OverInsanityRange;
                    }
                    else
                    {
                        return ResultsCalcReturnCode::Success;
                    }
                }

                double AnalyticalManager::GetInterpolation(std::shared_ptr<std::vector<Reading>> readings,
                    double tMinus,
                    double tPlus,
                    double extrapTime,
                    double driftAtTMinus,
                    double driftAtTPlus)
                {
                    return GetInterpolation(readings, tMinus, tPlus, extrapTime, driftAtTMinus, driftAtTPlus, 0);
                }

                double AnalyticalManager::GetInterpolation(std::shared_ptr<std::vector<Reading>> readings,
                    double tMinus,
                    double tPlus,
                    double extrapTime,
                    double driftAtTMinus,
                    double driftAtTPlus,
                    int startAt)
                {
                    double tempDecimal = 0.0;
                    double timeInterval = (((*readings)[1]).Time - ((*readings)[0]).Time);
                    int iAtExtrap = INT_MAX;
                    int iAtTPlus = INT_MAX;
                    int iAtTMinus = INT_MAX;
                    int i;

                    for (i = ((int)readings->size()) - 1; i >= startAt; i--)
                    {
                        if (MathHelp::Round(((*readings)[i]).Time, 1) < MathHelp::Round(extrapTime, 1))
                        {
                            iAtExtrap = i + 1;
                            break;
                        }
                    }

                    if (iAtExtrap == INT_MAX)
                        return 0.0;

                    for (i = ((int)readings->size()) - 1; i >= startAt; i--)
                    {
                        if (MathHelp::Round(((*readings)[i]).Time, 1) < MathHelp::Round(tMinus, 1))
                        {
                            iAtTMinus = i + 1;
                            break;
                        }
                    }

                    if (iAtTMinus == INT_MAX)
                        return 0.0;

                    for (i = ((int)readings->size()) - 1; i >= startAt; i--)
                    {
                        if (MathHelp::Round(((*readings)[i]).Time, 1) < MathHelp::Round(tPlus, 1))
                        {
                            iAtTPlus = i + 1;
                            break;
                        }
                    }

                    if (iAtTPlus == INT_MAX)
                        return 0.0;

                    for (i = iAtTPlus; i <= iAtExtrap; i++)
                    {
                        /*
                        double decimalI = i;
                        double decimalIAtTPlus = iAtTPlus;
                        double decimalIAtTMinus = iAtTMinus;

                        double temp5 = (decimalI - decimalIAtTPlus) / (decimalIAtTMinus - decimalIAtTPlus);
                        double temp6 = (driftAtTMinus - driftAtTPlus) * temp5;
                        double temp7 = driftAtTPlus + temp6;
                        double temp8 = timeInterval * temp7;

                        otherSum += temp8;
                        */
                        tempDecimal += CSharpDecimalCalculation(timeInterval * (driftAtTPlus +
                                                                    (driftAtTMinus - driftAtTPlus) *
                                                                    ((((double)(i)) - ((double)iAtTPlus)) /
                                                                        (((double)iAtTMinus) - ((double)iAtTPlus)))));
                    }

                    return tempDecimal;
                }

                ResultsCalcReturnCode AnalyticalManager::CalculateEx(std::shared_ptr<std::vector<Reading>> readings,
                    std::shared_ptr<SensorInfo> sensorInfo,
                    double bubbleDetect,
                    double sampleDetect,
                    std::shared_ptr<Levels> levels,
                    bool toUseT,
                    TestMode testMode,
                    bool calculateDefaultResponse,
                    std::shared_ptr<SensorReadings> topHeaterReadings,
                    int ageOfCard,
                    double ambientTemperature,
                    bool useAbsoluteValueInInjectionTime,
                    bool getSamplePeakParams,
                    bool divideByMeanToGetNoise,
                    double subtractFromSampleWindow,
                    double ageParameter,
                    double tauParameter,
                    TestMode qcAs,
                    bool qcCalExInsteadOfCalMean,
                    double meanUpperLimitForNoise,
                    bool dependentMoveBackOccurred,
                    std::shared_ptr<AgeCorrectionsQABlood> ageCorrections,
                    bool isBlood)
                {
                    ResultsCalcReturnCode rc;
                    double extrapTime = sensorInfo->extrapolation + sampleDetect;

                    double tMinus = 0.0;
                    double tPlus = 0.0;

                    double power = (topHeaterReadings == nullptr) ? 0.0 : topHeaterReadings->levels->response;

                    // figure out where tminus is
                    if (sensorInfo->tMinus < 0)
                    {
                        tMinus = (bubbleDetect + sensorInfo->tMinus);
                    }
                    else
                    {
                        tMinus = (sampleDetect + sensorInfo->tMinus);
                    }

                    // figure out where tplus is
                    if (sensorInfo->tPlus < 0)
                    {
                        tPlus = (bubbleDetect + sensorInfo->tPlus);
                    }
                    else
                    {
                        tPlus = (sampleDetect + sensorInfo->tPlus);
                    }

                    // whether to use the tplus and tminus is dependant on whether the useT is set and whether tplus
                    // is higher than the extaptime.. if it is not used, then findwindowparam that finds the post
                    // window parameters wont cause a failure if it throws or returns cnc
                    bool useT = (toUseT && (tPlus <= extrapTime));

                    if (readings->size() == 0)
                    {
                        return ResultsCalcReturnCode::CannotCalculate;
                    }

                    rc = FindParams(readings, bubbleDetect, sampleDetect, sensorInfo, levels, testMode, useT, getSamplePeakParams, divideByMeanToGetNoise, subtractFromSampleWindow, qcAs, meanUpperLimitForNoise, dependentMoveBackOccurred);

                    if (rc == ResultsCalcReturnCode::CannotCalculate)
                        return rc;

                    // get calex now because it might be needed for validate all qc
                    double extrapDist = (sensorInfo->extrapolation + sampleDetect) -
                        (((((*readings)[levels->calFirst]).Time) +
                        (((*readings)[levels->calLast]).Time)) / 2);
                    levels->calEx = levels->calMean + extrapDist *
                        (levels->calSlope + (extrapDist / 2) * levels->calSecond * sensorInfo->calCurveWeight);

                    if (useT)
                    {
                        if (extrapTime > tPlus)
                        {
                            // first do interpolation
                            //double calExtrap =
                            //double driftAtTMinus = levels->calSlope + levels

                            // Drift@t+Sensor  = DriftExCW,Sensor calculated with ExtrapTime Sensor = t+ Sensor
                            // DriftExCW,Sensor = DriftCW,Sensor + ( ExtrapDistCW,Sensor / 2 ) * Second derivativeCW,Sensor  * CurvatureWeightCW,Sensor
                            // ExtrapTime Sensor = extrapolation time read from configuration file + sample detect time.

                            double tPlusExtrap = tPlus -
                                (((((*readings)[levels->calFirst]).Time) +
                                (((*readings)[levels->calLast]).Time)) / 2);

                            double driftExTPlus = levels->calSlope +
                                (((tPlusExtrap) / 2) * levels->calSecond * sensorInfo->calCurveWeight);

                            double CalExAtTPlus = levels->calMean + tPlusExtrap * driftExTPlus;

                            double tMinusExtrap = tMinus -
                                (((((*readings)[levels->postFirst]).Time) +
                                (((*readings)[levels->postLast]).Time)) / 2);

                            double driftExTMinus = levels->postSlope +
                                ((tMinusExtrap / 2) * levels->postSecond * sensorInfo->postCurvatureWeight);

                            double calExAtTMinus = 0.0;

                            if (tPlus != tMinus)
                            {
                                if (extrapTime <= tMinus)
                                {
                                    levels->calEx = CalExAtTPlus + GetInterpolation(readings, tMinus, tPlus,
                                        extrapTime, driftExTMinus, driftExTPlus); ;
                                }
                                else
                                {
                                    extrapDist = extrapTime - (((((*readings)[levels->postFirst]).Time) +
                                        (((*readings)[levels->postLast]).Time)) / 2);

                                    calExAtTMinus = CalExAtTPlus + GetInterpolation(readings, tMinus, tPlus, tMinus, driftExTMinus, driftExTPlus);

                                    double driftExPost = levels->postSlope + (extrapDist / 2) * levels->postSecond * sensorInfo->postCurvatureWeight;

                                    levels->calEx = calExAtTMinus + (extrapTime - tMinus) * driftExPost;
                                }
                            }
                        }
                    }

                    double oldMeanLow = sensorInfo->CalMeanLowQC;
                    double oldMeanHigh = sensorInfo->CalMeanHighQC;

                    if (tauParameter != 0)
                    {
                        sensorInfo->CalMeanLowQC = sensorInfo->CalMeanLowQC + ageParameter * (std::exp(-ageOfCard / tauParameter) - 1);
                        sensorInfo->CalMeanHighQC = sensorInfo->CalMeanHighQC + ageParameter * (std::exp(-ageOfCard / tauParameter) - 1);
                    }

                    ResultsCalcReturnCode tempRc = ValidateAllQC(levels, sensorInfo, getSamplePeakParams, qcCalExInsteadOfCalMean);

                    if (tauParameter != 0)
                    {
                        // and put the calmean back to what it was before.. otherwise we will keep correcting it every time
                        sensorInfo->CalMeanLowQC = oldMeanLow;
                        sensorInfo->CalMeanHighQC = oldMeanHigh;
                    }

                    // dont overwrite if there was an error
                    if (rc == ResultsCalcReturnCode::Success)
                        rc = tempRc;

                    // moved extraptime uptop
                    //double extrapTime = sensorInfo->extrapolation + sampleDetect;
                    //double extrapDist = ((sensorInfo->extrapolation + sampleDetect)) - (((((Reading)(*readings)[levels->calFirst]).Time) + (((Reading)(*readings)[levels->calLast]).Time)) / 2);
                    //double CalExAtOriginalExtrapDist = levels->calMean + extrapDist * (levels->calSlope + (extrapDist / 2) * levels->calSecond * sensorInfo->calCurveWeight);



                    // now do sample ex
                    //extrapDist = (sensorInfo->extrapolation + sampleDetect) -
                    //    (((((Reading)(*readings)[levels->sampleFirst]).Time) +
                    //    (((Reading)(*readings)[levels->sampleLast]).Time)) / 2);

                    //levels->sampleEx = levels->sampleMean + extrapDist * (levels->sampleSlope + (extrapDist / 2) * levels->sampleSecond * sensorInfo->sampleCurveWeight);

                    extrapDist = 0; // (sensorInfo->extrapolation + sampleDetect) - (((((Reading)(*readings)[levels->sampleFirst]).Time) + (((Reading)(*readings)[levels->sampleLast]).Time)) / 2);
                    levels->sampleEx = levels->sampleMean + extrapDist * (levels->sampleSlope + (extrapDist / 2) * levels->sampleSecond * sensorInfo->sampleCurveWeight);

                    // do a default response value
                    if (calculateDefaultResponse)
                    {
                        levels->uncorrectedResponse = levels->response = (levels->sampleEx - levels->calEx);

                        /*
                        if (getSamplePeakParams)
                        {
                        // do the sw peak window .. multiply the PEAK factor (param 11) by the difference between
                        // sample ex and peak mean. so to turn this correction off, set param6 to 0.
                        levels->response += ((sensorInfo->param11)) * (levels->sampleEx - levels->peakMean);
                        levels->postEx = levels->peakMean;
                        levels->postSecond = levels->peakSlope;
                        }
                        */

                        double driftTermCorr = 0.0;
                        if (sensorInfo->sensorType == Sensors::CarbonDioxide)
                        {
                            // Calculate the drift for PCO2
                            driftTermCorr = CalculateDriftTermCorrectedPCO2(sensorInfo, levels, topHeaterReadings, isBlood);
                        }

                        double sensorRawMv = levels->response + driftTermCorr;

                        double tempDecimal = 0.0;

                        if ((power >= 0) && (ageOfCard >= 0))
                        {
                            // for amperometrics. they can pass null instead of making a dummy object. we make the dummy object here
                            // b, b2 and b3 will be 0, so it will zero out any term corrections
                            if (ageCorrections == nullptr)
                            {
                                ageCorrections = std::make_shared<AgeCorrectionsQABlood>();
                                //uses ageCorrections->GSensor instead of sensorInfo->G in formula
                                //assign sensorInfo->G to ageCorrections->GSensor as default value.
                                ageCorrections->GSensor = sensorInfo->G;
                            }

                            if (!isBlood && (sensorInfo->sensorType == Sensors::Chloride) && AgeAnd30CLogic(sensorInfo->param15))
                            {
                                // redmine 10589
                                // may need to set the various coefficients to 0
                                if (sensorRawMv > ageCorrections->MvCut)
                                {
                                    ageCorrections->B2Sensor = ageCorrections->B3Sensor = 0;
                                }
                                else
                                {
                                    if (ageOfCard <= ageCorrections->AgeCut)
                                    {
                                        ageCorrections->BSensor = ageCorrections->B3Sensor = 0;
                                    }
                                    else
                                    {
                                        ageCorrections->BSensor = ageCorrections->B2Sensor = 0;
                                    }
                                }
                            }
                            else
                            {
                                if ((sensorInfo->sensorType == Sensors::Chloride) ||
                                    (sensorInfo->sensorType == Sensors::pH) ||
                                    (sensorInfo->sensorType == Sensors::CarbonDioxide) ||
                                    (sensorInfo->sensorType == Sensors::Sodium) ||
                                    (sensorInfo->sensorType == Sensors::Potassium) ||
                                    (sensorInfo->sensorType == Sensors::Calcium) ||
                                    (sensorInfo->sensorType == Sensors::TCO2))

                                {
                                    // may need to set the various coefficients to 0
                                    if (sensorRawMv <= ageCorrections->MvCut)
                                    {
                                        ageCorrections->B2Sensor = ageCorrections->B3Sensor = 0;
                                    }
                                    else
                                    {
                                        if (ageOfCard <= ageCorrections->AgeCut)
                                        {
                                            ageCorrections->BSensor = ageCorrections->B3Sensor = 0;
                                        }
                                        else
                                        {
                                            ageCorrections->BSensor = ageCorrections->B2Sensor = 0;
                                        }
                                    }
                                }
                                else if (sensorInfo->sensorType == Sensors::Urea)
                                {
                                    // may need to set the various coefficients to 0
                                    if (sensorRawMv <= ageCorrections->MvCut)
                                    {
                                        ageCorrections->B2Sensor = ageCorrections->B3Sensor = 0;
                                    }
                                    else
                                    {
                                        if (ageOfCard <= ageCorrections->AgeCut)
                                        {
                                            if (BUNAgeCorrectionsResponse(sensorInfo->param15))
                                            {
                                                ageCorrections->B3Sensor = 0;
                                            }
                                            else
                                            {
                                                ageCorrections->BSensor = ageCorrections->B3Sensor = 0;
                                            }
                                        }
                                        else
                                        {
                                            if (BUNAgeCorrectionsResponse(sensorInfo->param15))
                                            {
                                                ageCorrections->B2Sensor = 0;
                                            }
                                            else
                                            {
                                                ageCorrections->BSensor = ageCorrections->B2Sensor = 0;
                                            }
                                        }
                                    }
                                }
                            }

                            double fSensor = ageCorrections->FSensor;
                            double fPrime = ageCorrections->FPrimeSensor;
                            double fDoublePrime = ageCorrections->FDoublePrimeSensor;
                            double lowMvInjTimeCut = ageCorrections->LowMvInjTimeCut;
                            double highMvInjTimeCut = ageCorrections->HighMvInjTimeCut;

                            if (sensorInfo->sensorType == Sensors::Urea)
                            {
                                double fINJMvCut, fINJ1, fINJ2, fINJ3, fINJ4;

                                if (isBlood)
                                {
                                    fINJMvCut = sensorInfo->param47;
                                    fINJ1 = sensorInfo->param48;
                                    fINJ2 = sensorInfo->param51;
                                    fINJ3 = sensorInfo->param52;
                                    fINJ4 = sensorInfo->param53;
                                }
                                else
                                {
                                    fINJMvCut = sensorInfo->param54;
                                    fINJ1 = sensorInfo->param55;
                                    fINJ2 = sensorInfo->param56;
                                    fINJ3 = sensorInfo->param57;
                                    fINJ4 = sensorInfo->param58;
                                }

                                if (levels->uncorrectedResponse <= fINJMvCut)
                                {
                                    fSensor = fINJ1 * std::pow(levels->uncorrectedResponse, 3) + fINJ2 * std::pow(levels->uncorrectedResponse, 2) + fINJ3 * levels->uncorrectedResponse + fINJ4;
                                }

                                fPrime = 0.0;
                                fDoublePrime = 0.0;
                            }

                            if (!useAbsoluteValueInInjectionTime)
                            {
                                levels->output10 = CSharpDecimalCalculation((ageCorrections->BSensor * ((double)ageOfCard - sensorInfo->AgeOffset)) +
                                                                            (ageCorrections->B2Sensor * (levels->response - ageCorrections->MvCut) * ((double)ageOfCard - sensorInfo->AgeOffset)) +
                                                                            (ageCorrections->B3Sensor * (levels->response - ageCorrections->MvCut) * ((double)ageOfCard - sensorInfo->AgeOffset)));

                                tempDecimal = CSharpDecimalCalculation((sensorInfo->A * (power - sensorInfo->PowerOffset)) +
                                                                       (ageCorrections->BSensor * ((double)ageOfCard - sensorInfo->AgeOffset)) +
                                                                       (ageCorrections->B2Sensor * (sensorRawMv - ageCorrections->MvCut) * ((double)ageOfCard - sensorInfo->AgeOffset)) +
                                                                       (ageCorrections->B3Sensor * (sensorRawMv - ageCorrections->MvCut) * ((double)ageOfCard - sensorInfo->AgeOffset)) +
                                                                       (sensorInfo->C * (power - sensorInfo->PowerOffset) * ((double)ageOfCard - sensorInfo->AgeOffset)) +
                                                                       (sensorInfo->D * (ambientTemperature - sensorInfo->TAmbOffset)) +
                                                                       (fSensor * (bubbleDetect - sensorInfo->InjectionTimeOffset)) +
                                                                       (sensorRawMv < lowMvInjTimeCut ? (fPrime * (sensorRawMv - lowMvInjTimeCut) * (bubbleDetect - sensorInfo->InjectionTimeOffset)) : 0) +
                                                                       (sensorRawMv > highMvInjTimeCut ? (fDoublePrime * (sensorRawMv - highMvInjTimeCut) * (bubbleDetect - sensorInfo->InjectionTimeOffset)) : 0) +
                                                                       (ageCorrections->GSensor * (bubbleDetect - sensorInfo->InjectionTimeOffset) * ((double)ageOfCard - sensorInfo->AgeOffset)));
                            }
                            else
                            {
                                levels->output10 = CSharpDecimalCalculation((ageCorrections->BSensor * ((double)ageOfCard - sensorInfo->AgeOffset)) +
                                                                            (ageCorrections->B2Sensor * (levels->response - ageCorrections->MvCut) * ((double)ageOfCard - sensorInfo->AgeOffset)) +
                                                                            (ageCorrections->B3Sensor * (levels->response - ageCorrections->MvCut) * ((double)ageOfCard - sensorInfo->AgeOffset)));

                                tempDecimal = CSharpDecimalCalculation((sensorInfo->A * (power - sensorInfo->PowerOffset)) +
                                                                       (ageCorrections->BSensor * ((double)ageOfCard - sensorInfo->AgeOffset)) +
                                                                       (ageCorrections->B2Sensor * (sensorRawMv - ageCorrections->MvCut) * ((double)ageOfCard - sensorInfo->AgeOffset)) +
                                                                       (ageCorrections->B3Sensor * (sensorRawMv - ageCorrections->MvCut) * ((double)ageOfCard - sensorInfo->AgeOffset)) +
                                                                       (sensorInfo->C * (power - sensorInfo->PowerOffset) * ((double)ageOfCard - sensorInfo->AgeOffset)) +
                                                                       (sensorInfo->D * (ambientTemperature - sensorInfo->TAmbOffset)) +
                                                                       (fSensor * std::abs((bubbleDetect - sensorInfo->InjectionTimeOffset))) +
                                                                       (sensorRawMv < lowMvInjTimeCut ? (fPrime * (sensorRawMv - lowMvInjTimeCut) * std::abs(bubbleDetect - sensorInfo->InjectionTimeOffset)) : 0) +
                                                                       (sensorRawMv > highMvInjTimeCut ? (fDoublePrime * (sensorRawMv - highMvInjTimeCut) * std::abs(bubbleDetect - sensorInfo->InjectionTimeOffset)) : 0) +
                                                                       (ageCorrections->GSensor * std::abs(bubbleDetect - sensorInfo->InjectionTimeOffset) * ((double)ageOfCard - sensorInfo->AgeOffset)));
                            }
                        }

                        levels->response = sensorRawMv + tempDecimal;
                    }

                    // return error if there was one
                    return rc;
                }

                ResultsCalcReturnCode AnalyticalManager::CalculateMeasuredTCO2(std::shared_ptr<SensorReadings> tco2Readings,
                                                                               std::shared_ptr<SensorReadings> pco2Reading,
                                                                               std::shared_ptr<SensorReadings> phReading,
                                                                               std::shared_ptr<SensorReadings> naReading,
                                                                               double bubbleDetect,
                                                                               double sampleDetect,
                                                                               double actualBicarb,
                                                                               TestMode testMode,
                                                                               std::shared_ptr<SensorReadings> topHeaterReadings,
                                                                               int ageOfCard,
                                                                               double ambientTemperature,
                                                                               bool isBlood,
                                                                               bool applyVolumeCorrections)
                {
                    ResultsCalcReturnCode rc;
                    std::shared_ptr<SensorInfo> sensorInfo = tco2Readings->sensorDescriptor;

                    FinalResult phResult;
                    FinalResult pco2Result;
                    double slopeFactor;
                    double offset;

                    double mTCO2q = sensorInfo->param1;
                    double mTCO2k = sensorInfo->param2;
                    double mTCO2alpha = sensorInfo->param3;
                    double mTCO2beta = sensorInfo->param4;

                    double TCO2OffsetAQ = sensorInfo->param5; //Offset for AQ
                    double TCO2SlopeAQ = sensorInfo->param6; //SlopeFactor for AQ
                    double x0 = sensorInfo->param7;
                    double naResultReading;

                    phResult.analyte = Analytes::pH;
                    phResult.returnCode = phReading->returnCode;
                    phResult.reading = phReading->result;
                    phResult.requirementsFailedIQC = phReading->requirementsFailedQC;

                    pco2Result.analyte = Analytes::CarbonDioxide;
                    pco2Result.returnCode = pco2Reading->returnCode;
                    pco2Result.reading = pco2Reading->result;
                    pco2Result.requirementsFailedIQC = pco2Reading->requirementsFailedQC;

                    naResultReading = naReading->result;

                    if ((!isBlood) || (isBlood && !CanBeMeasuredAsInput(naReading)))
                    {
                        mTCO2alpha = 0;
                        mTCO2beta = 0;
                        naResultReading = NormalNaforTCO2;
                    }

                    slopeFactor = sensorInfo->slopeFactor;
                    offset = sensorInfo->offset;

                    if (TCO2AQSlopeOffsetX0Coeff(sensorInfo->param15))
                    {
                        if (!isBlood)
                        {
                            slopeFactor = TCO2SlopeAQ;
                            offset = TCO2OffsetAQ;
                        }
                    }
                    else
                    {
                        x0 = 0;
                    }

                    if (isnan(pco2Result.reading) || isnan(phResult.reading)
                        || pco2Result.returnCode != ResultsCalcReturnCode::Success || phResult.returnCode != ResultsCalcReturnCode::Success
                        || FailedIQC(pco2Result.returnCode) || FailedIQC(phResult.returnCode) || pco2Result.requirementsFailedIQC || phResult.requirementsFailedIQC
                        )
                    {
                        tco2Readings->analyte = Analytes::MeasuredTCO2;
                        tco2Readings->result = NAN;
                        tco2Readings->returnCode = ResultsCalcReturnCode::CannotCalculate;
                        return tco2Readings->returnCode;
                    }

                    try
                    {
                        tco2Readings->analyte = Analytes::MeasuredTCO2;
                        tco2Readings->result = ((pco2Result.reading + mTCO2q) *
                            (std::pow(10, (phResult.reading - mTCO2k - tco2PhCutoff + mTCO2alpha * (NormalNaforTCO2 - naResultReading))) + tco2Pco2Factor +
                                std::pow(10, (mTCO2beta * (NormalNaforTCO2 - naResultReading))) - 1) - x0)
                            * slopeFactor + offset;

                        tco2Readings->returnCode = rc = ResultsCalcReturnCode::Success;
                    }
                    catch (...)
                    {
                        tco2Readings->analyte = Analytes::MeasuredTCO2;
                        tco2Readings->result = NAN;
                        tco2Readings->returnCode = ResultsCalcReturnCode::CannotCalculate;
                        return tco2Readings->returnCode;
                    }

                    // if there were no qc failures up until now, check the reportable range
                    // otherwise theres no point in checking the reportable range.. and just
                    // return whatever error code we got before...
                    if (rc == ResultsCalcReturnCode::Success)
                    {
                        tco2Readings->returnCode = rc = ValidateInsanityRangeOnlyReturnCode(tco2Readings->result,
                            testMode == TestMode::BloodTest ? tco2Readings->insanityLow : tco2Readings->insanityQALow,
                            testMode == TestMode::BloodTest ? tco2Readings->insanityHigh : tco2Readings->insanityQAHigh);
                    }

                    if ((rc == ResultsCalcReturnCode::Success) && (tco2Readings->realTimeQCFailedEver == RealTimeQCReturnCode::Failed))
                    {
                        tco2Readings->returnCode = rc = ResultsCalcReturnCode::FailedQCEver;
                    }

                    if ((rc == ResultsCalcReturnCode::Success) && (testMode == TestMode::BloodTest))
                    {
                        tco2Readings->returnCode = ValidateReportableRangeOnlyReturnCode(tco2Readings->result, tco2Readings->reportableLow, tco2Readings->reportableHigh);
                    }
                    else
                    {
                        tco2Readings->returnCode = rc;
                    }

                    // return the return code
                    return tco2Readings->returnCode;
                }

                ResultsCalcReturnCode AnalyticalManager::CalculateBUN(std::shared_ptr<SensorReadings> bunReadings,
                                                                      std::shared_ptr<SensorReadings> pco2Reading,
                                                                      std::shared_ptr<SensorReadings> hctReading,
                                                                      std::shared_ptr<SensorReadings> lactateReading,
                                                                      std::shared_ptr<SensorReadings> potassiumReading,
                                                                      double bubbleDetect,
                                                                      double sampleDetect,
                                                                      double actualBicarb,
                                                                      TestMode testMode,
                                                                      std::shared_ptr<SensorReadings> topHeaterReadings,
                                                                      int ageOfCard,
                                                                      double ambientTemperature,
                                                                      bool isBlood,
                                                                      double calciumAdditionalDrift)
                {
                    ResultsCalcReturnCode rc;
                    std::shared_ptr<SensorInfo> sensorInfo = bunReadings->sensorDescriptor;
                    std::shared_ptr<Levels> sensorLevels = bunReadings->levels;
                    auto tempLevels = std::make_shared<Levels>();
                    auto tempSensorInfo = std::make_shared<SensorInfo>();

                    auto ageCorrections = std::make_shared<AgeCorrectionsQABlood>();
                    double doubleSlopeFactor;
                    double calConcentration;
                    double calConcentrationK;
                    double offset;
                    double lactK;
                    double lactOff;

                    double AWStart = sensorInfo->param49;
                    double AWDuration = sensorInfo->param50;

                    double bicarbCorrect;//CO2corrAQ

                    double k; //bcBUNAQ

                    double BUNLacCut;
                    double P;

                    double CO2MvCut;
                    double AltkCO2;

                    double SlpMvCutLo;
                    double SlpMvCutHi;
                    double SlopeAdj1;
                    double SlopeAdj2;
                    double SlopeAdj3;
                    double HctCorrBUN;
                    double HctOffset;
                    double OffsetMvCutLo;
                    double OffsetMvCutHi;
                    double ConcOffsetAdjustLo;
                    double ConcOffsetAdjustMid;
                    double ConcOffsetHi;
                    double mvSlopeCutLow;
                    double mvSlopeAdjLow;

                    double PCO2MBUN;
                    double PCO2INJBUN;
                    double KNH4KBUN;

                    CO2MvCut = sensorInfo->param59;
                    AltkCO2 = sensorInfo->param60;

                    if (isBlood)
                    {
                        BUNLacCut = sensorInfo->param6;
                        P = sensorInfo->param7;
                        SlpMvCutLo = sensorInfo->param61;
                        SlpMvCutHi = sensorInfo->param62;
                        SlopeAdj1 = sensorInfo->param63;
                        SlopeAdj2 = sensorInfo->param64;
                        SlopeAdj3 = sensorInfo->param65;
                        HctCorrBUN = sensorInfo->param66;
                        HctOffset = sensorInfo->param67;
                        OffsetMvCutLo = sensorInfo->param68;
                        OffsetMvCutHi = sensorInfo->param69;
                        ConcOffsetAdjustLo = sensorInfo->param70;
                        ConcOffsetAdjustMid = sensorInfo->param71;
                        ConcOffsetHi = sensorInfo->param72;
                        mvSlopeCutLow = sensorInfo->param85;
                        mvSlopeAdjLow = sensorInfo->param86;
                        PCO2MBUN = sensorInfo->param90;
                        PCO2INJBUN = sensorInfo->param92;
                    }
                    else
                    {
                        BUNLacCut = sensorInfo->param22;
                        P = sensorInfo->param23;
                        SlpMvCutLo = sensorInfo->param73;
                        SlpMvCutHi = sensorInfo->param74;
                        SlopeAdj1 = sensorInfo->param75;
                        SlopeAdj2 = sensorInfo->param76;
                        SlopeAdj3 = sensorInfo->param77;
                        HctCorrBUN = sensorInfo->param78;
                        HctOffset = sensorInfo->param79;
                        OffsetMvCutLo = sensorInfo->param80;
                        OffsetMvCutHi = sensorInfo->param81;
                        ConcOffsetAdjustLo = sensorInfo->param82;
                        ConcOffsetAdjustMid = sensorInfo->param83;
                        ConcOffsetHi = sensorInfo->param84;
                        mvSlopeCutLow = sensorInfo->param87;
                        mvSlopeAdjLow = sensorInfo->param88;
                        PCO2MBUN = sensorInfo->param91;
                        PCO2INJBUN = sensorInfo->param93;
                    }

                    KNH4KBUN = sensorInfo->param94;

                    if (isBlood)
                    {
                        doubleSlopeFactor = sensorInfo->slopeFactor;
                        offset = sensorInfo->offset;
                        calConcentration = sensorInfo->calConcentration;
                        calConcentrationK = potassiumReading->sensorDescriptor->calConcentration;
                        lactK = sensorInfo->param4;
                        lactOff = sensorInfo->param5;

                        ageCorrections->BSensor = sensorInfo->B;
                        ageCorrections->B2Sensor = sensorInfo->param29;
                        ageCorrections->B3Sensor = sensorInfo->param31;
                        ageCorrections->MvCut = sensorInfo->param33;
                        ageCorrections->AgeCut = sensorInfo->param35;
                        ageCorrections->FSensor = sensorInfo->F;

                        ageCorrections->HSensor = sensorInfo->param37;
                        ageCorrections->JSensor = sensorInfo->param39;
                        ageCorrections->KSensor = sensorInfo->param41;
                        ageCorrections->earlyAgeCut = sensorInfo->param43;

                        bicarbCorrect = sensorInfo->param2;
                        k = sensorInfo->param3;
                    }
                    else
                    {
                        doubleSlopeFactor = sensorInfo->param24;
                        offset = sensorInfo->param25;
                        calConcentration = sensorInfo->param26;
                        calConcentrationK = potassiumReading->sensorDescriptor->param26;
                        lactK = sensorInfo->param20;
                        lactOff = sensorInfo->param21;

                        ageCorrections->BSensor = sensorInfo->param27;
                        ageCorrections->B2Sensor = sensorInfo->param30;
                        ageCorrections->B3Sensor = sensorInfo->param32;
                        ageCorrections->MvCut = sensorInfo->param34;
                        ageCorrections->AgeCut = sensorInfo->param36;
                        ageCorrections->FSensor = sensorInfo->param28;

                        ageCorrections->HSensor = sensorInfo->param38;
                        ageCorrections->JSensor = sensorInfo->param40;
                        ageCorrections->KSensor = sensorInfo->param42;
                        ageCorrections->earlyAgeCut = sensorInfo->param44;

                        bicarbCorrect = sensorInfo->param46;
                        k = sensorInfo->param45;
                    }
                    ageCorrections->GSensor = sensorInfo->G;

                    CopySensorInfo(tempSensorInfo, sensorInfo);
                    tempSensorInfo->sampleDelimit = tempSensorInfo->param9;
                    tempSensorInfo->sampleWindowSize = tempSensorInfo->param10;
                    tempSensorInfo->extrapolation = tempSensorInfo->param9 + (tempSensorInfo->param10 / 2.0);
                    tempSensorInfo->SampleMeanLowQC = -99999;
                    tempSensorInfo->SampleMeanHighQC = 99999;
                    tempSensorInfo->SampleNoiseHighQC = tempSensorInfo->param12;
                    tempSensorInfo->SampleDriftLowQC = tempSensorInfo->param13;
                    tempSensorInfo->SampleDriftHighQC = tempSensorInfo->param14;
                    tempSensorInfo->SampleSecondLowQC = -99999;
                    tempSensorInfo->SampleSecondHighQC = 99999;
                    tempSensorInfo->DeltaDriftLowQC = -99999;
                    tempSensorInfo->DeltaDriftHighQC = 99999;

                    // first get the transient window settings.. pretending that the sample transient window is the sample window for
                    // the purposes of cal extrapolation
                    rc = CalculateEx(bunReadings->readings, tempSensorInfo, bubbleDetect, sampleDetect, tempLevels, true, testMode, true, topHeaterReadings, ageOfCard, ambientTemperature, false, false, false, 0, tempSensorInfo->param16, tempSensorInfo->param17, testMode, false, 0.0, false,
                        nullptr, isBlood);

                    // convert the error codes for sample to sw peak
                    if (rc == ResultsCalcReturnCode::SampleNoiseQCHigh)
                    {
                        rc = ResultsCalcReturnCode::SWPeakNoiseHigh;
                    }
                    else if (rc == ResultsCalcReturnCode::SampleDriftQCLow)
                    {
                        rc = ResultsCalcReturnCode::SWPeakDriftLow;
                    }
                    else if (rc == ResultsCalcReturnCode::SampleDriftQCHigh)
                    {
                        rc = ResultsCalcReturnCode::SWPeakDriftHigh;
                    }
                    else
                    {
                        rc = ResultsCalcReturnCode::Success;
                    }

                    if (rc == ResultsCalcReturnCode::Success || rc == ResultsCalcReturnCode::SWPeakDriftHigh || rc == ResultsCalcReturnCode::SWPeakDriftLow || rc == ResultsCalcReturnCode::SWPeakNoiseHigh)
                    {
                        ResultsCalcReturnCode tempRC;
                        tempRC = CalculateEx(bunReadings->readings, sensorInfo, bubbleDetect, sampleDetect, sensorLevels, true, testMode, true, topHeaterReadings, ageOfCard, ambientTemperature, false, false, false, 0, sensorInfo->param16, sensorInfo->param17, testMode, false, 0.0, false, ageCorrections, isBlood);
                        if (rc == ResultsCalcReturnCode::Success)
                        {
                            rc = tempRC;
                        }
                    }

                    // this is the error that can get returned that we cant recover from..
                    // ...that there werent enough data points to calculate.
                    // if its just a qc failure.. continue.. but flag it as a qc failure as we
                    // leave
                    if ((rc == ResultsCalcReturnCode::CannotCalculate) || ((pco2Reading != nullptr) && (pco2Reading->returnCode == ResultsCalcReturnCode::CannotCalculate)))
                    {
                        bunReadings->result = NAN;

                        // uncorrected may have failed iqc.. we dont want to overwrite the old return code
                        if (bunReadings->returnCode == ResultsCalcReturnCode::Success)
                        {
                            bunReadings->returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                    }
                    else
                    {
                        // if i depend on something that failed qc... or something i depend on depends on something
                        // that fails qc, then i ultimately depend on something that fails qc
                        if ((pco2Reading != nullptr) && (FailedIQC(pco2Reading->returnCode) || pco2Reading->requirementsFailedQC))
                        {
                            bunReadings->requirementsFailedQC = true;
                        }

                        double bunOffset = sensorInfo->param1;
                        double lactKBUN = lactK;
                        double lactOffBUN = lactOff;

                        double swDiff = (sensorLevels->sampleEx - sensorLevels->calEx) - (tempLevels->sampleEx - tempLevels->calEx);
                        double decimalOffset = 0.0;
                        double decimalSlopeFactor = 0.0;
                        decimalOffset = offset;
                        decimalSlopeFactor = doubleSlopeFactor;
                        double potassiumResult = 0;

                        if (!BUNmvSlopAltkCO2LacLogic(sensorInfo->param15))
                        {
                            if (!isBlood && (sensorLevels->uncorrectedResponse <= CO2MvCut))
                            {
                                k = AltkCO2;
                            }
                        }
                        sensorLevels->response += ((sensorInfo->param11)) * swDiff;
                        if (BUNInjectionTimeModify(sensorInfo->param15))
                        {
                            sensorLevels->response += (k + PCO2MBUN * sensorLevels->uncorrectedResponse + PCO2INJBUN * (bubbleDetect - sensorInfo->InjectionTimeOffset)) * log(std::max(1.0, pco2Reading->result) / pco2Reading->sensorDescriptor->calConcentration);
                        }
                        else
                        {
                            sensorLevels->response += k * (pco2Reading->result - pco2Reading->sensorDescriptor->calConcentration);
                        }

                        if (isBlood && !isnan(calciumAdditionalDrift))
                        {
                            decimalSlopeFactor = decimalSlopeFactor + sensorInfo->param18 * (sensorInfo->param19 + calciumAdditionalDrift);
                        }

                        if (!BUNmvSlopAltkCO2LacLogic(sensorInfo->param15))
                        {
                            if (sensorLevels->response < mvSlopeCutLow)
                            {
                                sensorLevels->response = sensorLevels->response + mvSlopeAdjLow * (sensorLevels->response - mvSlopeCutLow);
                            }
                        }

                        if (sensorLevels->response <= SlpMvCutLo)
                        {
                            decimalSlopeFactor = decimalSlopeFactor + SlopeAdj1 * (sensorLevels->response - SlpMvCutLo);
                        }
                        else if (sensorLevels->response <= SlpMvCutHi)
                        {
                            decimalSlopeFactor = decimalSlopeFactor + SlopeAdj2 * (sensorLevels->response - SlpMvCutLo);
                        }
                        else
                        {
                            decimalSlopeFactor = decimalSlopeFactor + SlopeAdj2 * (SlpMvCutHi - SlpMvCutLo) + SlopeAdj3 * (sensorLevels->response - SlpMvCutHi);
                        }

                        //sensorLevels->output4 = swDiff;
                        sensorLevels->peakEx = tempLevels->calEx;
                        sensorLevels->peakMean = tempLevels->sampleMean;
                        sensorLevels->peakSlope = tempLevels->sampleSlope;
                        sensorLevels->peakNoise = tempLevels->sampleNoise;
                        sensorLevels->peakSecond = tempLevels->sampleSecond;

                        if (FailedIQC(potassiumReading->returnCode) || potassiumReading->requirementsFailedQC || potassiumReading->returnCode == ResultsCalcReturnCode::CannotCalculate)
                        {
                            KNH4KBUN = 0;
                        }

                        if (!isnan(potassiumReading->result))
                        {
                            potassiumResult = potassiumReading->result;
                        }

                        if ((AgeAnd30CLogic(sensorInfo->param15)) && ((double)ageOfCard >= ageCorrections->earlyAgeCut))
                        {
                            bunReadings->result = CSharpDecimalCalculation((calConcentration + bunOffset + (KNH4KBUN * calConcentrationK)) *
                                std::pow(10, ((sensorLevels->response -
                                (ageCorrections->HSensor * std::pow(M_E, ageCorrections->JSensor * ageOfCard) + ageCorrections->KSensor)) /
                                    (decimalSlopeFactor * FullNernst()))) -
                                bunOffset - (KNH4KBUN * potassiumResult));
                        }
                        else
                        {
                            bunReadings->result = CSharpDecimalCalculation((calConcentration + bunOffset + (KNH4KBUN * calConcentrationK)) *
                                std::pow(10, ((sensorLevels->response - decimalOffset) / (decimalSlopeFactor * FullNernst())))
                                - bunOffset - (KNH4KBUN * potassiumResult));
                        }

                        if (!isnan(actualBicarb))
                        {
                            bunReadings->result = CSharpDecimalCalculation((bunReadings->result) *
                                                                           ((1.0) + (bicarbCorrect * ((actualBicarb)-bunBicarbFactor))));
                        }

                        if (!CanBeMeasuredAsInput(hctReading))//If Hct is not available do not report BUN for blood
                        {
                            if (isBlood)
                            {
                                // uncorrected may have failed iqc.. we dont want to overwrite the old return code
                                if (bunReadings->returnCode == ResultsCalcReturnCode::Success)
                                {
                                    rc = bunReadings->returnCode = ResultsCalcReturnCode::CannotCalculate;
                                }
                            }
                            else
                            {
                                HctCorrBUN = 0;
                            }
                        }

                        bunReadings->result = CSharpDecimalCalculation(bunReadings->result * ((1.0) + HctCorrBUN * (hctReading->result - HctOffset)));

                        if (sensorLevels->response <= OffsetMvCutLo)
                        {
                            bunReadings->result = bunReadings->result + ConcOffsetHi + ConcOffsetAdjustMid * (OffsetMvCutLo - OffsetMvCutHi) + ConcOffsetAdjustLo * (sensorLevels->response - OffsetMvCutLo);
                        }
                        else if (sensorLevels->response <= OffsetMvCutHi)
                        {
                            bunReadings->result = (bunReadings->result + ConcOffsetHi + ConcOffsetAdjustMid * CSharpDecimalCalculation(sensorLevels->response - OffsetMvCutHi));
                        }
                        else
                        {
                            bunReadings->result = bunReadings->result + ConcOffsetHi;
                        }

                        if (!BUNmvSlopAltkCO2LacLogic(sensorInfo->param15))
                        {
                            if (CanBeMeasuredAsInput(lactateReading) && (bunReadings->result > BUNLacCut) && (lactateReading->result > lactOffBUN))
                            {
                                bunReadings->result = bunReadings->result - lactKBUN * (lactateReading->result - lactOffBUN) * (std::pow(bunReadings->result, P));
                            }
                        }

                        // we used a pco2 reading that was failed iqc (under/over r range is ok).. we need to flag
                        // this as cnc so it doesnt get displayed to the customer
                        if ((pco2Reading->returnCode != ResultsCalcReturnCode::Success) &&
                            (pco2Reading->returnCode != ResultsCalcReturnCode::OverReportableRange) &&
                            (pco2Reading->returnCode != ResultsCalcReturnCode::UnderReportableRange) &&
                            (rc == ResultsCalcReturnCode::Success))
                        {
                            rc = ResultsCalcReturnCode::CannotCalculate;
                        }

                        // if there were no qc failures up until now, check the reportable range
                        // otherwise theres no point in checking the reportable range.. and just
                        // return whatever error code we got before...
                        if (rc == ResultsCalcReturnCode::Success)
                        {
                            bunReadings->returnCode = rc = ValidateInsanityRangeOnlyReturnCode(bunReadings->result,
                                testMode == TestMode::BloodTest ? bunReadings->insanityLow : bunReadings->insanityQALow,
                                testMode == TestMode::BloodTest ? bunReadings->insanityHigh : bunReadings->insanityQAHigh);
                        }

                        if ((rc == ResultsCalcReturnCode::Success) && (bunReadings->realTimeQCFailedEver == RealTimeQCReturnCode::Failed))
                        {
                            bunReadings->returnCode = rc = ResultsCalcReturnCode::FailedQCEver;
                        }

                        if ((rc == ResultsCalcReturnCode::Success) && (testMode == TestMode::BloodTest))
                        {
                            bunReadings->returnCode = ValidateReportableRangeOnlyReturnCode(bunReadings->result, bunReadings->reportableLow, bunReadings->reportableHigh);
                        }
                        else
                        {
                            bunReadings->returnCode = rc;
                        }
                    }

                    return bunReadings->returnCode;
                }

                ResultsCalcReturnCode AnalyticalManager::CalculateSodium(std::shared_ptr<SensorReadings> sodiumReadings,
                                                                         std::shared_ptr<SensorReadings> pco2Reading,
                                                                         std::shared_ptr<SensorReadings> lactateReading,
                                                                         double bubbleDetect,
                                                                         double sampleDetect,
                                                                         double actualBicarb,
                                                                         TestMode testMode,
                                                                         std::shared_ptr<SensorReadings> topHeaterReadings,
                                                                         int ageOfCard,
                                                                         double ambientTemperature,
                                                                         bool isBlood,
                                                                         bool applyVolumeCorrections,
                                                                         bool estimate,
                                                                         double calciumAdditionalDrift)
                {
                    ResultsCalcReturnCode rc;
                    std::shared_ptr<SensorInfo> sensorInfo = sodiumReadings->sensorDescriptor;
                    std::shared_ptr<Levels> sensorLevels = sodiumReadings->levels;
                    auto tempLevels = std::make_shared<Levels>();
                    auto tempSensorInfo = std::make_shared<SensorInfo>();

                    auto ageCorrections = std::make_shared<AgeCorrectionsQABlood>();
                    double doubleSlopeFactor;
                    double calConcentration;
                    double offset;
                    double lactK;
                    double lactOff;
                    double altOffset;
                    double altSlope;

                    if (isBlood || !PotsAndGluLacAqBlood(sensorInfo->param15))
                    {
                        doubleSlopeFactor = sensorInfo->slopeFactor;
                        offset = sensorInfo->offset;
                        calConcentration = sensorInfo->calConcentration;
                        lactK = sensorInfo->param4;
                        lactOff = sensorInfo->param5;
                        altOffset = sensorInfo->param6;
                        altSlope = sensorInfo->param7;

                        ageCorrections->BSensor = sensorInfo->B;
                        ageCorrections->B2Sensor = sensorInfo->param29;
                        ageCorrections->B3Sensor = sensorInfo->param31;
                        ageCorrections->MvCut = sensorInfo->param33;
                        ageCorrections->AgeCut = sensorInfo->param35;
                        ageCorrections->FSensor = sensorInfo->F;

                        ageCorrections->HSensor = sensorInfo->param37;
                        ageCorrections->JSensor = sensorInfo->param39;
                        ageCorrections->KSensor = sensorInfo->param41;
                        ageCorrections->earlyAgeCut = sensorInfo->param43;

                        ageCorrections->LowMvInjTimeCut = sensorInfo->param57;
                        ageCorrections->FPrimeSensor = sensorInfo->param58;
                        ageCorrections->HighMvInjTimeCut = sensorInfo->param59;
                        ageCorrections->FDoublePrimeSensor = sensorInfo->param60;
                    }
                    else
                    {
                        doubleSlopeFactor = sensorInfo->param24;
                        offset = sensorInfo->param25;
                        calConcentration = sensorInfo->param26;
                        lactK = sensorInfo->param20;
                        lactOff = sensorInfo->param21;
                        altOffset = sensorInfo->param22;
                        altSlope = sensorInfo->param23;

                        ageCorrections->BSensor = sensorInfo->param27;
                        ageCorrections->B2Sensor = sensorInfo->param30;
                        ageCorrections->B3Sensor = sensorInfo->param32;
                        ageCorrections->MvCut = sensorInfo->param34;
                        ageCorrections->AgeCut = sensorInfo->param36;
                        ageCorrections->FSensor = sensorInfo->param28;

                        ageCorrections->HSensor = sensorInfo->param38;
                        ageCorrections->JSensor = sensorInfo->param40;
                        ageCorrections->KSensor = sensorInfo->param42;
                        ageCorrections->earlyAgeCut = sensorInfo->param44;

                        ageCorrections->LowMvInjTimeCut = sensorInfo->param61;
                        ageCorrections->FPrimeSensor = sensorInfo->param62;
                        ageCorrections->HighMvInjTimeCut = sensorInfo->param63;
                        ageCorrections->FDoublePrimeSensor = sensorInfo->param64;
                    }
                    ageCorrections->GSensor = sensorInfo->G;

                    CopySensorInfo(tempSensorInfo, sensorInfo);
                    tempSensorInfo->sampleDelimit = tempSensorInfo->param9;
                    tempSensorInfo->sampleWindowSize = tempSensorInfo->param10;
                    tempSensorInfo->extrapolation = tempSensorInfo->param9 + (tempSensorInfo->param10 / 2.0);
                    tempSensorInfo->SampleMeanLowQC = -99999;
                    tempSensorInfo->SampleMeanHighQC = 99999;
                    tempSensorInfo->SampleNoiseHighQC = tempSensorInfo->param12;
                    tempSensorInfo->SampleDriftLowQC = tempSensorInfo->param13;
                    tempSensorInfo->SampleDriftHighQC = tempSensorInfo->param14;
                    tempSensorInfo->SampleSecondLowQC = -99999;
                    tempSensorInfo->SampleSecondHighQC = 99999;
                    tempSensorInfo->DeltaDriftLowQC = -99999;
                    tempSensorInfo->DeltaDriftHighQC = 99999;

                    // first get the transient window settings.. pretending that the sample transient window is the sample window for
                    // the purposes of cal extrapolation
                    rc = CalculateEx(sodiumReadings->readings, tempSensorInfo, bubbleDetect, sampleDetect, tempLevels, true, testMode, true, topHeaterReadings, ageOfCard, ambientTemperature, false, false, false, 0, tempSensorInfo->param16, tempSensorInfo->param17, testMode, false, 0.0, false,
                        nullptr, isBlood);

                    // convert the error codes for sample to sw peak
                    if (rc == ResultsCalcReturnCode::SampleNoiseQCHigh)
                    {
                        rc = ResultsCalcReturnCode::SWPeakNoiseHigh;
                    }
                    else if (rc == ResultsCalcReturnCode::SampleDriftQCLow)
                    {
                        rc = ResultsCalcReturnCode::SWPeakDriftLow;
                    }
                    else if (rc == ResultsCalcReturnCode::SampleDriftQCHigh)
                    {
                        rc = ResultsCalcReturnCode::SWPeakDriftHigh;
                    }
                    else
                    {
                        rc = ResultsCalcReturnCode::Success;
                    }

                    if (rc == ResultsCalcReturnCode::Success)
                    {
                        rc = CalculateEx(sodiumReadings->readings, sensorInfo, bubbleDetect, sampleDetect, sensorLevels, true, testMode, true, topHeaterReadings, ageOfCard, ambientTemperature, false, false, false, 0, sensorInfo->param16, sensorInfo->param17, testMode, false, 0.0, false, ageCorrections, isBlood);
                    }

                    // this is the error that can get returned that we cant recover from..
                    // ...that there werent enough data points to calculate.
                    // if its just a qc failure.. continue.. but flag it as a qc failure as we
                    // leave
                    if ((rc == ResultsCalcReturnCode::CannotCalculate) ||
                        ((pco2Reading != nullptr) && (pco2Reading->returnCode == ResultsCalcReturnCode::CannotCalculate) && !estimate))
                    {
                        sodiumReadings->result = NAN;

                        // uncorrected may have failed iqc.. we dont want to overwrite the old return code
                        if (sodiumReadings->returnCode == ResultsCalcReturnCode::Success)
                        {
                            sodiumReadings->returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                    }
                    else
                    {
                        // if i depend on something that failed qc... or something i depend on depends on something
                        // that fails qc, then i ultimately depend on something that fails qc
                        if ((pco2Reading != nullptr) && (FailedIQC(pco2Reading->returnCode) || pco2Reading->requirementsFailedQC) && !estimate)
                        {
                            sodiumReadings->requirementsFailedQC = true;
                        }

                        double sodiumOffset = sensorInfo->param1;
                        double bicarbCorrect = sensorInfo->param2;
                        double k = sensorInfo->param3;
                        double lactKNa = lactK;
                        double lactOffNa = lactOff;

                        double swDiff = (sensorLevels->sampleEx - sensorLevels->calEx) - (tempLevels->sampleEx - tempLevels->calEx);
                        double decimalOffset = 0.0;
                        double decimalSlopeFactor = 0.0;

                        // if lactate is available and didnt fail qc.. OR if its an old testconfig, then pick up the offset from offset and slope
                        if (((lactateReading != nullptr) && !FailedIQC(lactateReading->returnCode) && !lactateReading->requirementsFailedQC) ||
                            !NewSodiumCalciumLactateCorrection(sensorInfo->param15))
                        {
                            decimalOffset = offset;
                            decimalSlopeFactor = doubleSlopeFactor;

                            if (isBlood && !isnan(calciumAdditionalDrift))
                            {
                                decimalSlopeFactor = decimalSlopeFactor + sensorInfo->param18 * (sensorInfo->param19 + calciumAdditionalDrift);
                            }
                        }
                        else
                        {
                            // so this is new and lactate is not available.. go to alt offset and slope
                            decimalOffset = altOffset;
                            decimalSlopeFactor = altSlope;
                        }

                        //sensorLevels->output4 = swDiff;
                        sensorLevels->response += sensorInfo->param11 * swDiff;

                        sensorLevels->peakEx = tempLevels->calEx;
                        sensorLevels->peakMean = tempLevels->sampleMean;
                        sensorLevels->peakSlope = tempLevels->sampleSlope;
                        sensorLevels->peakNoise = tempLevels->sampleNoise;
                        sensorLevels->peakSecond = tempLevels->sampleSecond;

                        if (!estimate)
                        {
                            //Implement f-factor for all sensors
                            try
                            {
                                if (sensorInfo->param56 != 0) //FWindowSize
                                {
                                    auto tempSensorInfo2 = std::make_shared<SensorInfo>();
                                    CopySensorInfo(tempSensorInfo2, sensorInfo);
                                    tempSensorInfo2->sampleWindowSize = sensorInfo->param56; //FWindowSize;
                                    tempSensorInfo2->sampleDelimit = sensorInfo->param55; //FWindowStart;

                                    auto tempLevels2 = std::make_shared<Levels>();
                                    ResultsCalcReturnCode rc2 = CalculateEx(sodiumReadings->readings, tempSensorInfo2, bubbleDetect, sampleDetect, tempLevels2,
                                        true, testMode, true, topHeaterReadings, ageOfCard, ambientTemperature, false, false, false, 0, tempSensorInfo2->param16, tempSensorInfo2->param17,
                                        testMode, false, 0.0, false,
                                        nullptr, isBlood);

                                    // calculate f parameter for AT output
                                    sensorLevels->output11 = tempLevels2->calEx;
                                    sensorLevels->output12 = tempLevels2->sampleMean;
                                    sensorLevels->output13 = tempLevels2->sampleNoise;
                                    sensorLevels->output14 = tempLevels2->sampleSlope;

                                    double f = CSharpDecimalCalculation((sensorLevels->sampleEx - sensorLevels->calEx) / (tempLevels2->sampleMean - tempLevels2->calEx));
                                    sensorLevels->output15 = f;
                                }
                            }
                            catch (...)
                            {
                                sensorLevels->output11 = 0.0;
                                sensorLevels->output12 = 0.0;
                                sensorLevels->output13 = 0.0;
                                sensorLevels->output14 = 0.0;
                                sensorLevels->output15 = 0.0;
                            }
                        }

                        //sensorLevels->response = sensorLevels->sampleEx - sensorLevels->calEx;
                        if (!estimate)
                        {
                            sensorLevels->response += k * (pco2Reading->result - pco2Reading->sensorDescriptor->calConcentration);
                        }

                        if ((((lactateReading != nullptr) && !FailedIQC(lactateReading->returnCode) && !lactateReading->requirementsFailedQC) ||
                            !NewSodiumCalciumLactateCorrection(sensorInfo->param15)) &&
                            (AgeAnd30CLogic(sensorInfo->param15) && ((double)ageOfCard >= ageCorrections->earlyAgeCut) && !estimate))
                        {
                            sodiumReadings->result = CSharpDecimalCalculation((calConcentration + sodiumOffset) *
                                std::pow(10, ((sensorLevels->response -
                                (ageCorrections->HSensor * std::pow(M_E, ageCorrections->JSensor * ageOfCard) + ageCorrections->KSensor)) /
                                    (decimalSlopeFactor * FullNernst()))) - sodiumOffset);
                        }
                        else
                        {
                            sodiumReadings->result = CSharpDecimalCalculation((calConcentration + sodiumOffset) *
                                std::pow(10, ((sensorLevels->response - decimalOffset) /
                                (decimalSlopeFactor * FullNernst()))) - sodiumOffset);
                        }

                        if (!estimate)
                        {
                            if (!isnan(actualBicarb))
                            {
                                sodiumReadings->result = CSharpDecimalCalculation(sodiumReadings->result *
                                                                                  (1.0 + (bicarbCorrect * (actualBicarb - 25.0))));
                            }

                            if (NewSodiumCalciumLactateCorrection(sensorInfo->param15))
                            {
                                if ((lactateReading != nullptr) && !FailedIQC(lactateReading->returnCode) && !lactateReading->requirementsFailedQC)
                                {
                                    sodiumReadings->result = CSharpDecimalCalculation(sodiumReadings->result - lactKNa * (lactateReading->result - lactOffNa));
                                }
                            }

                            if (isBlood && applyVolumeCorrections && (sodiumReadings->result >= sensorInfo->param46))
                            {
                                sodiumReadings->result = sodiumReadings->result + sensorInfo->param45 * (sodiumReadings->result - sensorInfo->param46);
                            }

                            // we used a pco2 reading that was failed iqc (under/over r range is ok).. we need to flag
                            // this as cnc so it doesnt get displayed to the customer
                            if ((pco2Reading->returnCode != ResultsCalcReturnCode::Success) &&
                                (pco2Reading->returnCode != ResultsCalcReturnCode::OverReportableRange) &&
                                (pco2Reading->returnCode != ResultsCalcReturnCode::UnderReportableRange) &&
                                (rc == ResultsCalcReturnCode::Success))
                            {
                                rc = ResultsCalcReturnCode::CannotCalculate;
                            }
                        }

                        // if there were no qc failures up until now, check the reportable range
                        // otherwise theres no point in checking the reportable range.. and just
                        // return whatever error code we got before...
                        if (rc == ResultsCalcReturnCode::Success)
                        {
                            sodiumReadings->returnCode = rc = ValidateInsanityRangeOnlyReturnCode(sodiumReadings->result,
                                testMode == TestMode::BloodTest ? sodiumReadings->insanityLow : sodiumReadings->insanityQALow,
                                testMode == TestMode::BloodTest ? sodiumReadings->insanityHigh : sodiumReadings->insanityQAHigh);
                        }

                        if ((rc == ResultsCalcReturnCode::Success) && (sodiumReadings->realTimeQCFailedEver == RealTimeQCReturnCode::Failed))
                        {
                            sodiumReadings->returnCode = rc = ResultsCalcReturnCode::FailedQCEver;
                        }

                        if ((rc == ResultsCalcReturnCode::Success) && (testMode == TestMode::BloodTest) && !estimate)
                        {
                            sodiumReadings->returnCode = ValidateReportableRangeOnlyReturnCode(sodiumReadings->result,
                                sodiumReadings->reportableLow,
                                sodiumReadings->reportableHigh);
                        }
                        else
                        {
                            sodiumReadings->returnCode = rc;
                        }
                    }

                    return sodiumReadings->returnCode;
                }

                ResultsCalcReturnCode AnalyticalManager::CalMeanAgeCorrection(int ageOfCard, ResultsCalcReturnCode rc, std::shared_ptr<Levels> sensorLevels, std::shared_ptr<SensorInfo> sensorInfo, double ageParameter, double tauParameter)
                {
                    if (tauParameter != 0)
                    {
                        double oldMeanLow = sensorInfo->CalMeanLowQC;
                        double oldMeanHigh = sensorInfo->CalMeanHighQC;

                        sensorInfo->CalMeanLowQC = sensorInfo->CalMeanLowQC + ageParameter * (std::exp(-ageOfCard / tauParameter) - 1);
                        sensorInfo->CalMeanHighQC = sensorInfo->CalMeanHighQC + ageParameter * (std::exp(-ageOfCard / tauParameter) - 1);

                        // need to revalidate mean qc.. but we want to put the calmean limits back after..                        
                        // overwrite the old return code with a new one.. this checks the corrected mean
                        // the reason we need to revalidate everything is that say the old mean doesnt
                        // pass, and then stops there.. if the new calmean passes, there may be something
                        // else waiting to fail behind that
                        if ((rc == ResultsCalcReturnCode::Success) || (rc == ResultsCalcReturnCode::CalMeanQCHigh) ||
                            (rc == ResultsCalcReturnCode::CalMeanQCLow))
                        {
                            rc = ValidateAllQC(sensorLevels, sensorInfo, false, false);
                        }

                        // and put the calmean back to what it was before.. otherwise we will keep correcting it every time
                        sensorInfo->CalMeanLowQC = oldMeanLow;
                        sensorInfo->CalMeanHighQC = oldMeanHigh;
                    }

                    return rc;
                }

                ResultsCalcReturnCode AnalyticalManager::CalculatePotassium(std::shared_ptr<SensorReadings> potassiumReadings,
                                                                            std::shared_ptr<SensorReadings> pco2Reading,
                                                                            std::shared_ptr<SensorReadings> lactateReadings,
                                                                            double bubbleDetect,
                                                                            double sampleDetect,
                                                                            double actualBicarb,
                                                                            TestMode testMode,
                                                                            std::shared_ptr<SensorReadings> topHeaterReadings,
                                                                            int ageOfCard,
                                                                            double ambientTemperature,
                                                                            bool isBlood,
                                                                            bool applyVolumeCorrections,
                                                                            bool estimate,
                                                                            double calciumAdditionalDrift)
                {
                    ResultsCalcReturnCode rc;
                    std::shared_ptr<SensorInfo> sensorInfo = potassiumReadings->sensorDescriptor;
                    std::shared_ptr<Levels> sensorLevels = potassiumReadings->levels;
                    auto tempLevels = std::make_shared<Levels>();

                    auto ageCorrections = std::make_shared<AgeCorrectionsQABlood>();
                    double doubleSlopeFactor;
                    double calConcentration;
                    double offset;
                    double lactK;
                    double lactOff;
                    double altOffset;
                    double altSlope;

                    if (isBlood || !PotsAndGluLacAqBlood(sensorInfo->param15))
                    {
                        doubleSlopeFactor = sensorInfo->slopeFactor;
                        offset = sensorInfo->offset;
                        calConcentration = sensorInfo->calConcentration;
                        lactK = sensorInfo->param4;
                        lactOff = sensorInfo->param5;
                        altOffset = sensorInfo->param6;
                        altSlope = sensorInfo->param7;

                        ageCorrections->BSensor = sensorInfo->B;
                        ageCorrections->B2Sensor = sensorInfo->param29;
                        ageCorrections->B3Sensor = sensorInfo->param31;
                        ageCorrections->MvCut = sensorInfo->param33;
                        ageCorrections->AgeCut = sensorInfo->param35;
                        ageCorrections->FSensor = sensorInfo->F;

                        ageCorrections->HSensor = sensorInfo->param37;
                        ageCorrections->JSensor = sensorInfo->param39;
                        ageCorrections->KSensor = sensorInfo->param41;
                        ageCorrections->earlyAgeCut = sensorInfo->param43;

                        ageCorrections->LowMvInjTimeCut = sensorInfo->param57;
                        ageCorrections->FPrimeSensor = sensorInfo->param58;
                        ageCorrections->HighMvInjTimeCut = sensorInfo->param59;
                        ageCorrections->FDoublePrimeSensor = sensorInfo->param60;
                    }
                    else
                    {
                        doubleSlopeFactor = sensorInfo->param24;
                        offset = sensorInfo->param25;
                        calConcentration = sensorInfo->param26;
                        lactK = sensorInfo->param20;
                        lactOff = sensorInfo->param21;
                        altOffset = sensorInfo->param22;
                        altSlope = sensorInfo->param23;

                        ageCorrections->BSensor = sensorInfo->param27;
                        ageCorrections->B2Sensor = sensorInfo->param30;
                        ageCorrections->B3Sensor = sensorInfo->param32;
                        ageCorrections->MvCut = sensorInfo->param34;
                        ageCorrections->AgeCut = sensorInfo->param36;
                        ageCorrections->FSensor = sensorInfo->param28;

                        ageCorrections->HSensor = sensorInfo->param38;
                        ageCorrections->JSensor = sensorInfo->param40;
                        ageCorrections->KSensor = sensorInfo->param42;
                        ageCorrections->earlyAgeCut = sensorInfo->param44;

                        ageCorrections->LowMvInjTimeCut = sensorInfo->param61;
                        ageCorrections->FPrimeSensor = sensorInfo->param62;
                        ageCorrections->HighMvInjTimeCut = sensorInfo->param63;
                        ageCorrections->FDoublePrimeSensor = sensorInfo->param64;
                    }
                    ageCorrections->GSensor = sensorInfo->G;

                    auto tempSensorInfo = std::make_shared<SensorInfo>();
                    CopySensorInfo(tempSensorInfo, sensorInfo);
                    tempSensorInfo->sampleDelimit = tempSensorInfo->param9;
                    tempSensorInfo->sampleWindowSize = tempSensorInfo->param10;
                    tempSensorInfo->extrapolation = tempSensorInfo->param9 + (tempSensorInfo->param10 / 2.0);
                    tempSensorInfo->SampleMeanLowQC = -99999;
                    tempSensorInfo->SampleMeanHighQC = 99999;
                    tempSensorInfo->SampleNoiseHighQC = tempSensorInfo->param12;
                    tempSensorInfo->SampleDriftLowQC = tempSensorInfo->param13;
                    tempSensorInfo->SampleDriftHighQC = tempSensorInfo->param14;
                    tempSensorInfo->SampleSecondLowQC = -99999;
                    tempSensorInfo->SampleSecondHighQC = 99999;
                    tempSensorInfo->DeltaDriftLowQC = -99999;
                    tempSensorInfo->DeltaDriftHighQC = 99999;

                    // first get the transient window settings.. pretending that the sample transient window is the sample window for
                    // the purposes of cal extrapolation
                    rc = CalculateEx(potassiumReadings->readings, tempSensorInfo, bubbleDetect, sampleDetect, tempLevels, true, testMode, true, topHeaterReadings, ageOfCard, ambientTemperature, false, false, false, 0, tempSensorInfo->param16, tempSensorInfo->param17, testMode, false, 0.0, false, nullptr, isBlood);

                    // convert the error codes for sample to sw peak
                    if (rc == ResultsCalcReturnCode::SampleNoiseQCHigh)
                    {
                        rc = ResultsCalcReturnCode::SWPeakNoiseHigh;
                    }
                    else if (rc == ResultsCalcReturnCode::SampleDriftQCLow)
                    {
                        rc = ResultsCalcReturnCode::SWPeakDriftLow;
                    }
                    else if (rc == ResultsCalcReturnCode::SampleDriftQCHigh)
                    {
                        rc = ResultsCalcReturnCode::SWPeakDriftHigh;
                    }
                    else
                    {
                        rc = ResultsCalcReturnCode::Success;
                    }

                    if (rc == ResultsCalcReturnCode::Success)
                    {
                        rc = CalculateEx(potassiumReadings->readings, sensorInfo, bubbleDetect, sampleDetect, sensorLevels, true, testMode, true, topHeaterReadings, ageOfCard, ambientTemperature, false, false, false, 0, sensorInfo->param16, sensorInfo->param17, testMode, false, 0.0, false, ageCorrections, isBlood);
                    }

                    // this is the error that can get returned that we cant recover from..
                    // ...that there werent enough data points to calculate.
                    // if its just a qc failure.. continue.. but flag it as a qc failure as we
                    // leave
                    if ((rc == ResultsCalcReturnCode::CannotCalculate) ||
                        ((pco2Reading != nullptr) && (pco2Reading->returnCode == ResultsCalcReturnCode::CannotCalculate) && !estimate))
                    {
                        potassiumReadings->result = NAN;

                        // uncorrected may have failed iqc.. we dont want to overwrite the old return code
                        if (potassiumReadings->returnCode == ResultsCalcReturnCode::Success)
                        {
                            potassiumReadings->returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                    }
                    else
                    {
                        if ((pco2Reading != nullptr) &&
                            (FailedIQC(pco2Reading->returnCode) || pco2Reading->requirementsFailedQC) && !estimate)
                        {
                            potassiumReadings->requirementsFailedQC = true;
                        }

                        double potassiumOffset = sensorInfo->param1;
                        double bicarbCorrect = sensorInfo->param2;
                        double k = sensorInfo->param3;

                        double swDiff = (sensorLevels->sampleEx - sensorLevels->calEx) - (tempLevels->sampleEx - tempLevels->calEx);

                        double lactKK = lactK;
                        double lactOffK = lactOff;

                        double decimalOffset = 0.0;
                        double decimalSlopeFactor = 0.0;

                        // if lactate is available and didnt fail qc.. OR if its an old testconfig, then pick up the offset from offset and slope
                        if (((lactateReadings != nullptr) && !FailedIQC(lactateReadings->returnCode) && !lactateReadings->requirementsFailedQC) ||
                            !NewSodiumCalciumLactateCorrection(sensorInfo->param15))
                        {
                            decimalOffset = offset;
                            decimalSlopeFactor = doubleSlopeFactor;

                            if (isBlood && !isnan(calciumAdditionalDrift))
                            {
                                decimalSlopeFactor = decimalSlopeFactor + sensorInfo->param18 * (sensorInfo->param19 + calciumAdditionalDrift);
                            }
                        }
                        else
                        {
                            // so this is new and lactate is not available.. go to alt offset and slope
                            decimalOffset = altOffset;
                            decimalSlopeFactor = altSlope;
                        }

                        if (!estimate)
                        {
                            //Implement f-factor for all sensors
                            try
                            {
                                if (sensorInfo->param56 != 0) //FWindowSize
                                {
                                    auto tempSensorInfo2 = std::make_shared<SensorInfo>();
                                    CopySensorInfo(tempSensorInfo2, sensorInfo);
                                    tempSensorInfo2->sampleWindowSize = sensorInfo->param56; //FWindowSize;
                                    tempSensorInfo2->sampleDelimit = sensorInfo->param55; //FWindowStart;

                                    auto tempLevels2 = std::make_shared<Levels>();
                                    ResultsCalcReturnCode rc2 = CalculateEx(potassiumReadings->readings, tempSensorInfo2, bubbleDetect, sampleDetect, tempLevels2,
                                        true, testMode, true, topHeaterReadings, ageOfCard, ambientTemperature, false, false, false, 0, tempSensorInfo2->param16, tempSensorInfo2->param17,
                                        testMode, false, 0.0, false, ageCorrections, isBlood);

                                    // calculate f parameter for AT output
                                    sensorLevels->output11 = tempLevels2->calEx;
                                    sensorLevels->output12 = tempLevels2->sampleMean;
                                    sensorLevels->output13 = tempLevels2->sampleNoise;
                                    sensorLevels->output14 = tempLevels2->sampleSlope;

                                    double f = CSharpDecimalCalculation((sensorLevels->sampleEx - sensorLevels->calEx) / (tempLevels2->sampleMean - tempLevels2->calEx));
                                    sensorLevels->output15 = f;
                                }
                            }
                            catch (...)
                            {
                                sensorLevels->output11 = 0.0;
                                sensorLevels->output12 = 0.0;
                                sensorLevels->output13 = 0.0;
                                sensorLevels->output14 = 0.0;
                                sensorLevels->output15 = 0.0;
                            }
                        }
                        //sensorLevels->output4 = swDiff;
                        sensorLevels->response += ((sensorInfo->param11)) * swDiff;

                        sensorLevels->peakEx = tempLevels->calEx;
                        sensorLevels->peakMean = tempLevels->sampleMean;
                        sensorLevels->peakSlope = tempLevels->sampleSlope;
                        sensorLevels->peakNoise = tempLevels->sampleNoise;
                        sensorLevels->peakSecond = tempLevels->sampleSecond;

                        if (!estimate)
                        {
                            sensorLevels->response += k * (pco2Reading->result - pco2Reading->sensorDescriptor->calConcentration);
                        }

                        if ((((lactateReadings != nullptr) && !FailedIQC(lactateReadings->returnCode) && !lactateReadings->requirementsFailedQC) ||
                            !NewSodiumCalciumLactateCorrection(sensorInfo->param15)) &&
                            (AgeAnd30CLogic(sensorInfo->param15) && ((double)ageOfCard >= ageCorrections->earlyAgeCut) && !estimate))
                        {
                            potassiumReadings->result = CSharpDecimalCalculation((calConcentration + potassiumOffset) *
                                std::pow(10, CSharpDecimalCalculation((sensorLevels->response -
                                (ageCorrections->HSensor * std::pow(M_E, ageCorrections->JSensor * ageOfCard) + ageCorrections->KSensor)) /
                                    (decimalSlopeFactor * FullNernst()))) - potassiumOffset);
                        }
                        else
                        {
                            potassiumReadings->result = CSharpDecimalCalculation(((calConcentration + potassiumOffset) *
                                std::pow(10, CSharpDecimalCalculation((sensorLevels->response - decimalOffset) /
                                (decimalSlopeFactor * FullNernst())))) - potassiumOffset);
                        }

                        if (!estimate)
                        {
                            if (!isnan(actualBicarb))
                            {
                                potassiumReadings->result = CSharpDecimalCalculation((potassiumReadings->result) * (1.0 + (bicarbCorrect * ((actualBicarb) - 25))));
                            }

                            if (NewSodiumCalciumLactateCorrection(sensorInfo->param15))
                            {
                                if ((lactateReadings != nullptr) && !FailedIQC(lactateReadings->returnCode) && !lactateReadings->requirementsFailedQC)
                                {
                                    potassiumReadings->result = CSharpDecimalCalculation(potassiumReadings->result - lactKK * (lactateReadings->result - lactOffK));
                                }
                            }

                            if (isBlood && applyVolumeCorrections && (potassiumReadings->result >= sensorInfo->param46))
                            {
                                potassiumReadings->result = potassiumReadings->result + sensorInfo->param45 * (potassiumReadings->result - sensorInfo->param46);
                            }

                            // we used a pco2 reading that was failed iqc or under/over range.. we need to flag
                            // this as cnc so it doesnt get displayed to the customer
                            // we used a pco2 reading that was failed iqc (under/over r range is ok).. we need to flag
                            // this as cnc so it doesnt get displayed to the customer
                            if ((pco2Reading->returnCode != ResultsCalcReturnCode::Success) &&
                                (pco2Reading->returnCode != ResultsCalcReturnCode::OverReportableRange) &&
                                (pco2Reading->returnCode != ResultsCalcReturnCode::UnderReportableRange) &&
                                (rc == ResultsCalcReturnCode::Success))
                            {
                                rc = ResultsCalcReturnCode::CannotCalculate;
                            }
                        }

                        // if there were no qc failures up until now, check the reportable range
                        // otherwise theres no point in checking the reportable range.. and just
                        // return whatever error code we got before...
                        if (rc == ResultsCalcReturnCode::Success)
                        {
                            potassiumReadings->returnCode = rc = ValidateInsanityRangeOnlyReturnCode(potassiumReadings->result,
                                testMode == TestMode::BloodTest ? potassiumReadings->insanityLow : potassiumReadings->insanityQALow,
                                testMode == TestMode::BloodTest ? potassiumReadings->insanityHigh : potassiumReadings->insanityQAHigh);
                        }

                        if ((rc == ResultsCalcReturnCode::Success) && (potassiumReadings->realTimeQCFailedEver == RealTimeQCReturnCode::Failed))
                        {
                            potassiumReadings->returnCode = rc = ResultsCalcReturnCode::FailedQCEver;
                        }

                        if ((rc == ResultsCalcReturnCode::Success) && (testMode == TestMode::BloodTest) && !estimate)
                        {
                            potassiumReadings->returnCode = ValidateReportableRangeOnlyReturnCode(potassiumReadings->result, potassiumReadings->reportableLow, potassiumReadings->reportableHigh);
                        }
                        else
                        {
                            potassiumReadings->returnCode = rc;
                        }
                    }

                    return potassiumReadings->returnCode;
                }

                ResultsCalcReturnCode AnalyticalManager::CalculateChloride(std::shared_ptr<SensorReadings> chlorideReadings,
                                                                           std::shared_ptr<SensorReadings> pco2Reading,
                                                                           std::shared_ptr<SensorReadings> lactateReadings,
                                                                           std::shared_ptr<SensorReadings> hctReadings,
                                                                           std::shared_ptr<SensorReadings> po2Reading,
                                                                           std::shared_ptr<SensorReadings> invBunReading,
                                                                           double bubbleDetect,
                                                                           double sampleDetect,
                                                                           double actualBicarb,
                                                                           TestMode testMode,
                                                                           std::shared_ptr<SensorReadings> topHeaterReadings,
                                                                           int ageOfCard,
                                                                           double ambientTemperature,
                                                                           bool isBlood,
                                                                           bool applyVolumeCorrections,
                                                                           double calciumAdditionalDrift,
                                                                           bool bunAvailable)
                {
                    ResultsCalcReturnCode rc;
                    std::shared_ptr<SensorInfo> sensorInfo = chlorideReadings->sensorDescriptor;
                    std::shared_ptr<Levels> sensorLevels = chlorideReadings->levels;
                    auto tempLevels = std::make_shared<Levels>();
                    auto tempSensorInfo = std::make_shared<SensorInfo>();
                    auto ageCorrections = std::make_shared<AgeCorrectionsQABlood>();

                    double doubleSlopeFactor;
                    double calConcentration;
                    double offset;
                    double lactK;
                    double lactOff;
                    double altOffset;
                    double altSlope;

                    double BUNCM1 = sensorInfo->param65;
                    double BUNCM2 = sensorInfo->param66;
                    double BUNCM3 = sensorInfo->param67;
                    double CLBUNOFF = sensorInfo->param68;

                    if (isBlood || !PotsAndGluLacAqBlood(sensorInfo->param15))
                    {
                        doubleSlopeFactor = sensorInfo->slopeFactor;
                        offset = sensorInfo->offset;
                        calConcentration = sensorInfo->calConcentration;
                        lactK = sensorInfo->param4;
                        lactOff = sensorInfo->param5;
                        altOffset = sensorInfo->param6;
                        altSlope = sensorInfo->param7;

                        ageCorrections->BSensor = sensorInfo->B;
                        ageCorrections->B2Sensor = sensorInfo->param35;
                        ageCorrections->B3Sensor = sensorInfo->param37;
                        ageCorrections->MvCut = sensorInfo->param39;
                        ageCorrections->AgeCut = sensorInfo->param41;
                        ageCorrections->FSensor = sensorInfo->F;

                        ageCorrections->LowMvInjTimeCut = sensorInfo->param57;
                        ageCorrections->FPrimeSensor = sensorInfo->param58;
                        ageCorrections->HighMvInjTimeCut = sensorInfo->param59;
                        ageCorrections->FDoublePrimeSensor = sensorInfo->param60;
                    }
                    else
                    {
                        doubleSlopeFactor = sensorInfo->param30;
                        offset = sensorInfo->param31;
                        calConcentration = sensorInfo->param32;
                        lactK = sensorInfo->param26;
                        lactOff = sensorInfo->param27;
                        altOffset = sensorInfo->param28;
                        altSlope = sensorInfo->param29;

                        ageCorrections->BSensor = sensorInfo->param33;
                        ageCorrections->B2Sensor = sensorInfo->param36;
                        ageCorrections->B3Sensor = sensorInfo->param38;
                        ageCorrections->MvCut = sensorInfo->param40;
                        ageCorrections->AgeCut = sensorInfo->param42;
                        ageCorrections->FSensor = sensorInfo->param34;

                        ageCorrections->LowMvInjTimeCut = sensorInfo->param61;
                        ageCorrections->FPrimeSensor = sensorInfo->param62;
                        ageCorrections->HighMvInjTimeCut = sensorInfo->param63;
                        ageCorrections->FDoublePrimeSensor = sensorInfo->param64;
                    }
                    ageCorrections->GSensor = sensorInfo->G;

                    CopySensorInfo(tempSensorInfo, sensorInfo);
                    tempSensorInfo->sampleDelimit = tempSensorInfo->param9;
                    tempSensorInfo->sampleWindowSize = tempSensorInfo->param10;
                    tempSensorInfo->extrapolation = tempSensorInfo->param9 + (tempSensorInfo->param10 / 2.0);
                    tempSensorInfo->SampleMeanLowQC = -99999;
                    tempSensorInfo->SampleMeanHighQC = 99999;
                    tempSensorInfo->SampleNoiseHighQC = tempSensorInfo->param12;
                    tempSensorInfo->SampleDriftLowQC = tempSensorInfo->param13;
                    tempSensorInfo->SampleDriftHighQC = tempSensorInfo->param14;
                    tempSensorInfo->SampleSecondLowQC = -99999;
                    tempSensorInfo->SampleSecondHighQC = 99999;
                    tempSensorInfo->DeltaDriftLowQC = -99999;
                    tempSensorInfo->DeltaDriftHighQC = 99999;

                    // first get the transient window settings.. pretending that the sample transient window is the sample window for
                    // the purposes of cal extrapolation
                    rc = CalculateEx(chlorideReadings->readings, tempSensorInfo, bubbleDetect, sampleDetect, tempLevels, true, testMode, true, topHeaterReadings, ageOfCard, ambientTemperature, false, false, false, 0, sensorInfo->param16, sensorInfo->param17, testMode, false, 0.0, false, nullptr, isBlood);

                    // convert the error codes for sample to sw peak
                    if (rc == ResultsCalcReturnCode::SampleNoiseQCHigh)
                    {
                        rc = ResultsCalcReturnCode::SWPeakNoiseHigh;
                    }
                    else if (rc == ResultsCalcReturnCode::SampleDriftQCLow)
                    {
                        rc = ResultsCalcReturnCode::SWPeakDriftLow;
                    }
                    else if (rc == ResultsCalcReturnCode::SampleDriftQCHigh)
                    {
                        rc = ResultsCalcReturnCode::SWPeakDriftHigh;
                    }
                    else
                    {
                        rc = ResultsCalcReturnCode::Success;
                    }

                    if (rc == ResultsCalcReturnCode::Success)
                    {
                        rc = CalculateEx(chlorideReadings->readings, sensorInfo, bubbleDetect, sampleDetect, sensorLevels, true, testMode, true, topHeaterReadings, ageOfCard, ambientTemperature, false, false, false, 0, sensorInfo->param16, sensorInfo->param17, testMode, false, 0.0, false, ageCorrections, isBlood);
                    }

                    // this is the error that can get returned that we cant recover from..
                    // ...that there werent enough data points to calculate.
                    // if its just a qc failure.. continue.. but flag it as a qc failure as we
                    // leave
                    if ((rc == ResultsCalcReturnCode::CannotCalculate) || (pco2Reading->returnCode == ResultsCalcReturnCode::CannotCalculate))
                    {
                        chlorideReadings->result = NAN;

                        // uncorrected may have failed iqc.. we dont want to overwrite the old return code
                        if (chlorideReadings->returnCode == ResultsCalcReturnCode::Success)
                        {
                            chlorideReadings->returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                    }
                    else
                    {
                        double chlorideOffset = sensorInfo->param1;
                        double bicarbCorrect = sensorInfo->param2;
                        double k = sensorInfo->param3;

                        sensorLevels->response += ((sensorInfo->param11)) * ((sensorLevels->sampleEx - sensorLevels->calEx) - (tempLevels->sampleEx - tempLevels->calEx));

                        sensorLevels->peakEx = tempLevels->sampleEx;
                        sensorLevels->peakMean = tempLevels->sampleMean;
                        sensorLevels->peakSlope = tempLevels->sampleSlope;
                        sensorLevels->peakNoise = tempLevels->sampleNoise;
                        sensorLevels->peakSecond = tempLevels->sampleSecond;

                        double lactKCl = lactK;
                        double lactOffCl = lactOff;
                        double decimalOffset = 0.0;
                        double decimalSlopeFactor = 0.0;
                        double AgeThreshpO2Corr = sensorInfo->param20;
                        double DeltaiCalThreshpO2Corr = sensorInfo->param21;
                        double OiC1 = sensorInfo->param22;
                        double OiC2 = sensorInfo->param23;
                        double OffsetFact = sensorInfo->param24;
                        double CorrFact = sensorInfo->param25;
                        double correctedpO2Mean = po2Reading->levels->output1; // this value is placed in output1 by calculatepo2

                        double X_Factor;

                        if (isBlood)
                        {
                            X_Factor = sensorInfo->param53;
                        }
                        else
                        {
                            X_Factor = sensorInfo->param54;
                        }

                        //Implement f-factor for all sensors
                        try
                        {
                            if (sensorInfo->param56 != 0) //FWindowSize
                            {
                                auto tempSensorInfo2 = std::make_shared<SensorInfo>();
                                CopySensorInfo(tempSensorInfo2, sensorInfo);
                                tempSensorInfo2->sampleWindowSize = sensorInfo->param56; //FWindowSize;
                                tempSensorInfo2->sampleDelimit = sensorInfo->param55; //FWindowStart;

                                auto tempLevels2 = std::make_shared<Levels>();
                                ResultsCalcReturnCode rc2 = CalculateEx(chlorideReadings->readings, tempSensorInfo2, bubbleDetect, sampleDetect, tempLevels2,
                                    true, testMode, true, topHeaterReadings, ageOfCard, ambientTemperature, false, false, false, 0, tempSensorInfo2->param16, tempSensorInfo2->param17,
                                    testMode, false, 0.0, false, ageCorrections, isBlood);

                                // calculate f parameter for AT output
                                sensorLevels->output11 = tempLevels2->calEx;
                                sensorLevels->output12 = tempLevels2->sampleMean;
                                sensorLevels->output13 = tempLevels2->sampleNoise;
                                sensorLevels->output14 = tempLevels2->sampleSlope;

                                double f = CSharpDecimalCalculation((sensorLevels->sampleEx - sensorLevels->calEx) / (tempLevels2->sampleMean - tempLevels2->calEx));
                                sensorLevels->output15 = f;
                            }
                        }
                        catch (...)
                        {
                            sensorLevels->output11 = 0.0;
                            sensorLevels->output12 = 0.0;
                            sensorLevels->output13 = 0.0;
                            sensorLevels->output14 = 0.0;
                            sensorLevels->output15 = 0.0;
                        }

                        // apply po2corrected mean correction after initial response but before lactate
                        if (CanBeUsedAsInput(po2Reading->returnCode))
                        {
                            if ((ageOfCard > AgeThreshpO2Corr) && ((correctedpO2Mean - (OiC1 * (AgeAnd30CLogic(sensorInfo->param15) ? log((double)ageOfCard) : ageOfCard) + OiC2)) > DeltaiCalThreshpO2Corr))
                            {
                                sensorLevels->response = sensorLevels->response + OffsetFact + CorrFact * ageOfCard * (correctedpO2Mean - (OiC1 * (AgeAnd30CLogic(sensorInfo->param15) ? log((double)ageOfCard) : ageOfCard) + OiC2));
                            }
                        }
                        // else no error code and response is left the same

                        // if lactate is available and didnt fail qc.. OR if its an old testconfig, then pick up the offset from offset and slope
                        if (((lactateReadings != nullptr) && !FailedIQC(lactateReadings->returnCode) && !lactateReadings->requirementsFailedQC) ||
                            !NewSodiumCalciumLactateCorrection(sensorInfo->param15))
                        {
                            decimalOffset = offset;
                            decimalSlopeFactor = doubleSlopeFactor;

                            if (isBlood && !isnan(calciumAdditionalDrift))
                            {
                                decimalSlopeFactor = decimalSlopeFactor + sensorInfo->param18 * (sensorInfo->param19 + calciumAdditionalDrift);
                            }
                        }
                        else
                        {
                            // so this is new and lactate is not available.. go to alt offset and slope
                            decimalOffset = altOffset;
                            decimalSlopeFactor = altSlope;
                        }

                        sensorLevels->response += k * (pco2Reading->result - pco2Reading->sensorDescriptor->calConcentration);

                        //sensorLevels->response = sensorLevels->sampleEx - sensorLevels->calEx;

                        chlorideReadings->result = CSharpDecimalCalculation(((calConcentration + chlorideOffset) *
                            std::pow(10, CSharpDecimalCalculation((sensorLevels->response - decimalOffset) /
                            (-1 * decimalSlopeFactor * FullNernst()))))
                            - chlorideOffset);

                        sensorLevels->output1 = CSharpDecimalCalculation(chlorideReadings->result);

                        if (!isnan(actualBicarb))
                        {
                            chlorideReadings->result = CSharpDecimalCalculation((chlorideReadings->result) *
                                (1.0 +
                                (bicarbCorrect *
                                    ((actualBicarb)-25))));
                        }

                        sensorLevels->output2 = CSharpDecimalCalculation(chlorideReadings->result);

                        if (NewSodiumCalciumLactateCorrection(sensorInfo->param15))
                        {
                            if ((lactateReadings != nullptr) && !FailedIQC(lactateReadings->returnCode) && !lactateReadings->requirementsFailedQC)
                            {
                                chlorideReadings->result = CSharpDecimalCalculation(chlorideReadings->result - lactKCl * (lactateReadings->result - lactOffCl));
                            }
                        }

                        // Redmine 12492. New Chloride cal drift correction
                        chlorideReadings->result = CSharpDecimalCalculation(chlorideReadings->result / (1 - (X_Factor * sensorLevels->calSlope)));

                        if (isBlood && applyVolumeCorrections && (chlorideReadings->result >= sensorInfo->param46))
                        {
                            chlorideReadings->result = chlorideReadings->result + sensorInfo->param45 * (chlorideReadings->result - sensorInfo->param46);
                        }

                        double temVal = BUNCM1 * (std::max(DaysofWeek, ageOfCard) * std::max(DaysofWeek, ageOfCard)) + BUNCM2 * std::max(DaysofWeek, ageOfCard) + BUNCM3;
                        if (bunAvailable ||
                            ((!bunAvailable) && (invBunReading != nullptr) && (!FailedIQC(invBunReading->returnCode)) && (invBunReading->levels->calMean > temVal)))
                        {
                            chlorideReadings->result = chlorideReadings->result + CLBUNOFF;
                        }

                        sensorLevels->output18 = 0;
                        if ((!bunAvailable) && (invBunReading != nullptr))
                        {
                            if (!FailedIQC(invBunReading->returnCode))
                            {
                                sensorLevels->output18 = invBunReading->levels->calMean;
                            }
                        }

                        sensorLevels->output3 = CSharpDecimalCalculation(chlorideReadings->result);

                        // we used a pco2 reading that was failed iqc or under/over range or if hct failed qc, or if hct
                        // recovery was used.. we need to flag this as cnc so it doesnt get displayed to the customer.
                        // ths dependency of hct failQC, or hct recovery is requested ONLY for blood mode
                        if (isBlood)
                        {
                            if ((rc == ResultsCalcReturnCode::Success) &&
                                (FailedIQC(pco2Reading->returnCode) ||
                                    FailedIQC(hctReadings->returnCode) ||
                                    (hctReadings->levels->output4 == 1)))
                            {
                                rc = ResultsCalcReturnCode::CannotCalculate;

                                // if hct recovery was done, then cl- is cnc, but the whole card is considered an iqc failure
                                if (hctReadings->levels->output4 == 1)
                                {
                                    chlorideReadings->requirementsFailedQC = true;
                                }
                            }
                        }
                        else
                        {
                            if ((rc == ResultsCalcReturnCode::Success) &&
                                FailedIQC(pco2Reading->returnCode))
                            {
                                rc = ResultsCalcReturnCode::CannotCalculate;
                            }
                        }
                        // if there were no qc failures up until now, check the reportable range
                        // otherwise theres no point in checking the reportable range.. and just
                        // return whatever error code we got before...
                        if (rc == ResultsCalcReturnCode::Success)
                        {
                            chlorideReadings->returnCode = rc = ValidateInsanityRangeOnlyReturnCode(chlorideReadings->result,
                                testMode == TestMode::BloodTest ? chlorideReadings->insanityLow : chlorideReadings->insanityQALow,
                                testMode == TestMode::BloodTest ? chlorideReadings->insanityHigh : chlorideReadings->insanityQAHigh);
                        }

                        if ((rc == ResultsCalcReturnCode::Success) && (chlorideReadings->realTimeQCFailedEver == RealTimeQCReturnCode::Failed))
                        {
                            chlorideReadings->returnCode = rc = ResultsCalcReturnCode::FailedQCEver;
                        }

                        if ((rc == ResultsCalcReturnCode::Success) && (testMode == TestMode::BloodTest))
                        {
                            chlorideReadings->returnCode = ValidateReportableRangeOnlyReturnCode(chlorideReadings->result, chlorideReadings->reportableLow, chlorideReadings->reportableHigh);
                        }
                        else
                        {
                            chlorideReadings->returnCode = rc;
                        }
                    }

                    return chlorideReadings->returnCode;
                }

                ResultsCalcReturnCode AnalyticalManager::CalculatepH(std::shared_ptr<SensorReadings> pHReadings,
                                                                     std::shared_ptr<SensorReadings> pco2Reading,
                                                                     std::shared_ptr<SensorReadings> hctReading,
                                                                     double bubbleDetect,
                                                                     double sampleDetect,
                                                                     double actualBicarb,
                                                                     TestMode testMode,
                                                                     std::shared_ptr<SensorReadings> topHeaterReadings,
                                                                     int ageOfCard,
                                                                     double ambientTemperature,
                                                                     bool isBlood,
                                                                     bool applyVolumeCorrections,
                                                                     double calciumAdditionalDrift)
                {
                    ResultsCalcReturnCode rc = ResultsCalcReturnCode::Success;
                    std::shared_ptr<SensorInfo> sensorInfo = pHReadings->sensorDescriptor;
                    std::shared_ptr<Levels> sensorLevels = pHReadings->levels;
                    auto tempSensorInfo = std::make_shared<SensorInfo>();
                    auto tempLevels = std::make_shared<Levels>();
                    auto ageCorrections = std::make_shared<AgeCorrectionsQABlood>();
                    auto ageCorrectionsTemp = std::make_shared<AgeCorrectionsQABlood>();

                    double doubleSlopeFactor;
                    double calConcentration;
                    double offset;

                    if (isBlood || !PotsAndGluLacAqBlood(sensorInfo->param15))
                    {
                        doubleSlopeFactor = sensorInfo->slopeFactor;
                        offset = sensorInfo->offset;
                        calConcentration = sensorInfo->calConcentration;

                        ageCorrections->BSensor = sensorInfo->B;
                        ageCorrections->B2Sensor = sensorInfo->param29;
                        ageCorrections->B3Sensor = sensorInfo->param31;
                        ageCorrections->MvCut = sensorInfo->param33;
                        ageCorrections->AgeCut = sensorInfo->param35;
                        ageCorrections->FSensor = sensorInfo->F;

                        ageCorrections->LowMvInjTimeCut = sensorInfo->param57;
                        ageCorrections->FPrimeSensor = sensorInfo->param58;
                        ageCorrections->HighMvInjTimeCut = sensorInfo->param59;
                        ageCorrections->FDoublePrimeSensor = sensorInfo->param60;
                    }
                    else
                    {
                        doubleSlopeFactor = sensorInfo->param24;
                        offset = sensorInfo->param25;
                        calConcentration = sensorInfo->param26;

                        ageCorrections->BSensor = sensorInfo->param27;
                        ageCorrections->B2Sensor = sensorInfo->param30;
                        ageCorrections->B3Sensor = sensorInfo->param32;
                        ageCorrections->MvCut = sensorInfo->param34;
                        ageCorrections->AgeCut = sensorInfo->param36;
                        ageCorrections->FSensor = sensorInfo->param28;

                        ageCorrections->LowMvInjTimeCut = sensorInfo->param61;
                        ageCorrections->FPrimeSensor = sensorInfo->param62;
                        ageCorrections->HighMvInjTimeCut = sensorInfo->param63;
                        ageCorrections->FDoublePrimeSensor = sensorInfo->param64;
                    }
                    ageCorrections->GSensor = sensorInfo->G;

                    // do ph early window only if its not an aqueous or if hct failed qc
                    if (pHEarlyWindow(sensorInfo->param15) && (testMode == TestMode::BloodTest) &&
                        (((hctReading->result > hctpHThreshold) && !FailedIQC(hctReading->returnCode)) ||
                            FailedIQC(hctReading->returnCode)))
                    {
                        CopySensorInfo(tempSensorInfo, sensorInfo);
                        tempSensorInfo->sampleDelimit = tempSensorInfo->param9;
                        tempSensorInfo->sampleWindowSize = tempSensorInfo->param10;
                        tempSensorInfo->extrapolation = tempSensorInfo->param9 + (tempSensorInfo->param10 / 2.0);
                        tempSensorInfo->SampleMeanLowQC = -99999;
                        tempSensorInfo->SampleMeanHighQC = 99999;
                        tempSensorInfo->SampleNoiseHighQC = 99999;
                        tempSensorInfo->SampleDriftLowQC = -99999;
                        tempSensorInfo->SampleDriftHighQC = 99999;
                        tempSensorInfo->SampleSecondLowQC = -99999;
                        tempSensorInfo->SampleSecondHighQC = 99999;
                        tempSensorInfo->DeltaDriftLowQC = -99999;
                        tempSensorInfo->DeltaDriftHighQC = 99999;

                        // first get the transient window settings.. pretending that the sample transient window is the sample window for
                        // the purposes of cal extrapolation
                        rc = CalculateEx(pHReadings->readings, tempSensorInfo, bubbleDetect, sampleDetect, tempLevels, true, testMode, true, topHeaterReadings, ageOfCard, ambientTemperature, false, false, false, 0, tempSensorInfo->param20, tempSensorInfo->param21, testMode, false, 0.0, false, nullptr, isBlood);

                        if (rc != ResultsCalcReturnCode::CannotCalculate)
                        {
                            double responseEarlyWindow = tempLevels->sampleMean - tempLevels->calEx;
                            double deltaDrift = tempLevels->sampleSlope - tempLevels->calSlope;
                            double k1 = sensorInfo->param11;
                            double k2 = sensorInfo->param12;
                            double a1 = sensorInfo->param13;
                            double a2 = sensorInfo->param14;
                            double upperLimit = sensorInfo->param16;
                            double lowerLimit = sensorInfo->param17;

                            double multiplier = 1.0;

                            if (MultiplypHEarlyWindowByCalMean(sensorInfo->param15))
                            {
                                multiplier = tempLevels->calMean;
                            }

                            double a1Calc = (deltaDrift - (a1 * (responseEarlyWindow + ((ageOfCard - k1) * k2)))) * multiplier;
                            double a2Calc = (deltaDrift - (a2 * (responseEarlyWindow + ((ageOfCard - k1) * k2)))) * multiplier;

                            sensorLevels->peakEx = responseEarlyWindow;
                            sensorLevels->peakMean = a2Calc;
                            sensorLevels->peakSlope = deltaDrift;
                            sensorLevels->peakNoise = tempLevels->sampleNoise;
                            sensorLevels->peakSecond = tempLevels->sampleSecond;

                            if (rc == ResultsCalcReturnCode::Success)
                            {
                                if (a1Calc > upperLimit)
                                {
                                    rc = pHReadings->returnCode = ResultsCalcReturnCode::EarlyWindowHigh;
                                }
                                else if (a2Calc < lowerLimit)
                                {
                                    rc = pHReadings->returnCode = ResultsCalcReturnCode::EarlyWindowLow;
                                }
                            }
                        }
                    }
                    else
                    {
                        rc = ResultsCalcReturnCode::Success;
                    }

                    ResultsCalcReturnCode tempRc = CalculateEx(pHReadings->readings, sensorInfo, bubbleDetect, sampleDetect, sensorLevels, true, testMode, true, topHeaterReadings, ageOfCard, ambientTemperature, false, false, false, 0, sensorInfo->param20, sensorInfo->param21, testMode, false, 0.0, false, ageCorrections, isBlood);

                    // make sure we only overwrite the return code if it's a success
                    if (rc == ResultsCalcReturnCode::Success)
                    {
                        rc = tempRc;
                    }

                    // this is the error that can get returned that we cant recover from..
                    // ...that there werent enough data points to calculate.
                    // if its just a qc failure.. continue.. but flag it as a qc failure as we
                    // leave
                    if ((tempRc == ResultsCalcReturnCode::CannotCalculate) ||
                        (rc == ResultsCalcReturnCode::CannotCalculate) ||
                        (pco2Reading->returnCode == ResultsCalcReturnCode::CannotCalculate))
                    {
                        pHReadings->result = NAN;

                        // uncorrected may have failed iqc.. we dont want to overwrite the old return code
                        if (pHReadings->returnCode == ResultsCalcReturnCode::Success)
                        {
                            pHReadings->returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                    }
                    else
                    {
                        //Implement f-factor for all sensors
                        try
                        {
                            if (sensorInfo->param56 != 0) //FWindowSize
                            {
                                auto tempSensorInfo2 = std::make_shared<SensorInfo>();
                                CopySensorInfo(tempSensorInfo2, sensorInfo);
                                tempSensorInfo2->sampleWindowSize = sensorInfo->param56; //FWindowSize;
                                tempSensorInfo2->sampleDelimit = sensorInfo->param55; //FWindowStart;

                                auto tempLevels2 = std::make_shared<Levels>();
                                ResultsCalcReturnCode rc2 = CalculateEx(pHReadings->readings, tempSensorInfo2, bubbleDetect, sampleDetect, tempLevels2,
                                    true, testMode, true, topHeaterReadings, ageOfCard, ambientTemperature, false, false, false, 0, tempSensorInfo2->param20, tempSensorInfo2->param21,
                                    testMode, false, 0.0, false, ageCorrections, isBlood);

                                // calculate f parameter for AT output
                                sensorLevels->output11 = tempLevels2->calEx;
                                sensorLevels->output12 = tempLevels2->sampleMean;
                                sensorLevels->output13 = tempLevels2->sampleNoise;
                                sensorLevels->output14 = tempLevels2->sampleSlope;

                                double f = CSharpDecimalCalculation((sensorLevels->sampleEx - sensorLevels->calEx) / (tempLevels2->sampleMean - tempLevels2->calEx));
                                sensorLevels->output15 = f;
                            }
                        }
                        catch (...)
                        {
                            sensorLevels->output11 = 0.0;
                            sensorLevels->output12 = 0.0;
                            sensorLevels->output13 = 0.0;
                            sensorLevels->output14 = 0.0;
                            sensorLevels->output15 = 0.0;
                        }
                        //sensorLevels->response = sensorLevels->sampleEx - sensorLevels->calEx;
                        if (FailedIQC(pco2Reading->returnCode) || pco2Reading->requirementsFailedQC)
                        {
                            pHReadings->requirementsFailedQC = true;
                        }

                        double slopeFactor = doubleSlopeFactor;

                        if (isBlood && !isnan(calciumAdditionalDrift))
                        {
                            slopeFactor = slopeFactor + sensorInfo->param22 * (sensorInfo->param23 + calciumAdditionalDrift);
                        }

                        double bicarbCorrect = sensorInfo->param1;
                        double k = sensorInfo->param2;

                        sensorLevels->response += k * (pco2Reading->result - pco2Reading->sensorDescriptor->calConcentration);

                        pHReadings->result = CSharpDecimalCalculation(calConcentration - ((sensorLevels->response - offset) / (slopeFactor * FullNernst())));

                        double cutoff = sensorInfo->param18;
                        double ageSlopeCorrection = sensorInfo->param19;

                        // new ph stuff from gerhard
                        if (pHReadings->result > cutoff)
                        {
                            pHReadings->result = CSharpDecimalCalculation(calConcentration - ((sensorLevels->response - offset) / (slopeFactor * FullNernst() - (ageSlopeCorrection * (ageOfCard - sensorInfo->AgeOffset)))));
                        }

                        if (!isnan(actualBicarb))
                        {
                            pHReadings->result = CSharpDecimalCalculation((pHReadings->result) - (bicarbCorrect * ((actualBicarb)-25)));
                        }

                        if (isBlood && applyVolumeCorrections)
                        {
                            pHReadings->result = pHReadings->result + sensorInfo->param45 * (pHReadings->result - sensorInfo->param46);
                        }

                        // we used a pco2 reading that was failed iqc (under/over r range is ok).. we need to flag
                        // this as cnc so it doesnt get displayed to the customer
                        if ((pco2Reading->returnCode != ResultsCalcReturnCode::Success) &&
                            (pco2Reading->returnCode != ResultsCalcReturnCode::OverReportableRange) &&
                            (pco2Reading->returnCode != ResultsCalcReturnCode::UnderReportableRange) &&
                            (rc == ResultsCalcReturnCode::Success))
                        {
                            rc = ResultsCalcReturnCode::CannotCalculate;
                        }

                        if (rc == ResultsCalcReturnCode::Success)
                        {
                            pHReadings->returnCode = rc = ValidateInsanityRangeOnlyReturnCode(pHReadings->result,
                                testMode == TestMode::BloodTest ? pHReadings->insanityLow : pHReadings->insanityQALow,
                                testMode == TestMode::BloodTest ? pHReadings->insanityHigh : pHReadings->insanityQAHigh);
                        }

                        if ((rc == ResultsCalcReturnCode::Success) && (pHReadings->realTimeQCFailedEver == RealTimeQCReturnCode::Failed))
                        {
                            pHReadings->returnCode = rc = ResultsCalcReturnCode::FailedQCEver;
                        }

                        if ((rc == ResultsCalcReturnCode::Success) && (testMode == TestMode::BloodTest))
                        {
                            pHReadings->returnCode = rc = ValidateReportableRangeOnlyReturnCode(pHReadings->result, pHReadings->reportableLow, pHReadings->reportableHigh);
                        }
                    }

                    return pHReadings->returnCode = rc;
                }

                bool AnalyticalManager::PreCalculateHematocrit(std::vector<std::shared_ptr<SensorReadings>> &testReadings,
                                                               double bubbleDetect,
                                                               double sampleDetect,
                                                               TestMode testMode,
                                                               bool applyHemodilution,
                                                               std::shared_ptr<SensorReadings> topHeaterReadings,
                                                               int ageOfCard,
                                                               double ambientTemperature)
                {
                    try
                    {
                        std::shared_ptr<SensorReadings> sodiumReadings = nullptr;
                        std::shared_ptr<SensorReadings> potassiumReadings = nullptr;

                        for (size_t i = 0; i < testReadings.size(); i++)
                        {
                            if (testReadings[i]->sensorType == Sensors::Sodium)
                            {
                                sodiumReadings = testReadings[i];
                            }

                            if (testReadings[i]->sensorType == Sensors::Potassium)
                            {
                                potassiumReadings = testReadings[i];
                            }
                        }

                        CalculateSodium(sodiumReadings, nullptr, nullptr, bubbleDetect, sampleDetect, NAN,
                            testMode, topHeaterReadings, ageOfCard, ambientTemperature, false, false, true, NAN);

                        CalculatePotassium(potassiumReadings, nullptr, nullptr, bubbleDetect, sampleDetect, NAN,
                            testMode, topHeaterReadings, ageOfCard, ambientTemperature, false, false, true, NAN);

                        for (size_t i = 0; i < testReadings.size(); i++)
                        {
                            if (testReadings[i]->sensorType == Sensors::Conductivity)
                            {
                                ResultsCalcReturnCode rc = CalculateHematocrit(testReadings[i], sodiumReadings,
                                    potassiumReadings, bubbleDetect, sampleDetect, TestMode::BloodTest, applyHemodilution, testMode, true, false, true, NAN);

                                testReadings[i]->levels->output8 = CSharpDecimalCalculation(testReadings[i]->result);

                                // blood and (failed qc) or (component failed qc) or (result is > 7))
                                // urr/orr are not failures and will fall into the > 7 comparison
                                if ((testMode == TestMode::BloodTest) &&
                                    (FailedIQC(testReadings[i]->returnCode) ||
                                        testReadings[i]->requirementsFailedQC ||
                                        (testReadings[i]->result > HctAqThreshold)))
                                {
                                    return true;
                                }
                                else
                                {
                                    return false;
                                }
                            }
                        }
                    }
                    catch (...)
                    {
                    }

                    return false;
                }

                bool AnalyticalManager::PreCalculateCalcium(std::vector<std::shared_ptr<SensorReadings>> &testReadings,
                    double bubbleDetect,
                    double sampleDetect,
                    TestMode testMode,
                    std::shared_ptr<SensorReadings> topHeaterReadings,
                    int ageOfCard,
                    double ambientTemperature,
                    bool isBlood,
                    double bubbleWidth)
                {
                    try
                    {
                        std::shared_ptr<SensorReadings> calciumReadings = nullptr;
                        ResultsCalcReturnCode rc = ResultsCalcReturnCode::CannotCalculate;

                        for (size_t i = 0; i < testReadings.size(); i++)
                        {
                            if (testReadings[i]->sensorType == Sensors::Calcium)
                            {
                                calciumReadings = testReadings[i];
                                break;
                            }
                        }

                        if (calciumReadings != nullptr)
                        {
                            std::shared_ptr<SensorInfo> sensorInfo = calciumReadings->sensorDescriptor;
                            std::shared_ptr<Levels> sensorLevels = calciumReadings->levels;
                            auto tempLevels = std::make_shared<Levels>();

                            auto ageCorrections = std::make_shared<AgeCorrectionsQABlood>();
                            double doubleSlopeFactor;
                            double calConcentration;
                            double offset;
                            double lactK;
                            double lactOff;
                            double altOffset;
                            double altSlope;

                            if (isBlood || !PotsAndGluLacAqBlood(sensorInfo->param15))
                            {
                                doubleSlopeFactor = sensorInfo->slopeFactor;
                                offset = sensorInfo->offset;
                                calConcentration = sensorInfo->calConcentration;
                                lactK = sensorInfo->param4;
                                lactOff = sensorInfo->param5;
                                altOffset = sensorInfo->param6;
                                altSlope = sensorInfo->param7;

                                ageCorrections->BSensor = sensorInfo->B;
                                ageCorrections->B2Sensor = sensorInfo->param29;
                                ageCorrections->B3Sensor = sensorInfo->param31;
                                ageCorrections->MvCut = sensorInfo->param33;
                                ageCorrections->AgeCut = sensorInfo->param35;
                                ageCorrections->FSensor = sensorInfo->F;
                                ageCorrections->GSensor = sensorInfo->G;

                                ageCorrections->HSensor = sensorInfo->param37;
                                ageCorrections->JSensor = sensorInfo->param39;
                                ageCorrections->KSensor = sensorInfo->param41;
                                ageCorrections->earlyAgeCut = sensorInfo->param43;

                                ageCorrections->LowMvInjTimeCut = sensorInfo->param57;
                                ageCorrections->FPrimeSensor = sensorInfo->param58;
                                ageCorrections->HighMvInjTimeCut = sensorInfo->param59;
                                ageCorrections->FDoublePrimeSensor = sensorInfo->param60;
                            }
                            else
                            {
                                doubleSlopeFactor = sensorInfo->param24;
                                offset = sensorInfo->param25;
                                calConcentration = sensorInfo->param26;
                                lactK = sensorInfo->param20;
                                lactOff = sensorInfo->param21;
                                altOffset = sensorInfo->param22;
                                altSlope = sensorInfo->param23;

                                ageCorrections->BSensor = sensorInfo->param27;
                                ageCorrections->B2Sensor = sensorInfo->param30;
                                ageCorrections->B3Sensor = sensorInfo->param32;
                                ageCorrections->MvCut = sensorInfo->param34;
                                ageCorrections->AgeCut = sensorInfo->param36;
                                ageCorrections->FSensor = sensorInfo->param28;
                                ageCorrections->GSensor = sensorInfo->param54;

                                ageCorrections->HSensor = sensorInfo->param38;
                                ageCorrections->JSensor = sensorInfo->param40;
                                ageCorrections->KSensor = sensorInfo->param42;
                                ageCorrections->earlyAgeCut = sensorInfo->param44;

                                ageCorrections->LowMvInjTimeCut = sensorInfo->param61;
                                ageCorrections->FPrimeSensor = sensorInfo->param62;
                                ageCorrections->HighMvInjTimeCut = sensorInfo->param63;
                                ageCorrections->FDoublePrimeSensor = sensorInfo->param64;
                            }

                            auto tempSensorInfo = std::make_shared<SensorInfo>();

                            CopySensorInfo(tempSensorInfo, sensorInfo);
                            tempSensorInfo->sampleDelimit = tempSensorInfo->param9;
                            tempSensorInfo->sampleWindowSize = tempSensorInfo->param10;
                            tempSensorInfo->extrapolation = tempSensorInfo->param9 + (tempSensorInfo->param10 / 2.0);
                            tempSensorInfo->SampleMeanLowQC = -99999;
                            tempSensorInfo->SampleMeanHighQC = 99999;
                            tempSensorInfo->SampleNoiseHighQC = tempSensorInfo->param12;
                            tempSensorInfo->SampleDriftLowQC = tempSensorInfo->param13;
                            tempSensorInfo->SampleDriftHighQC = tempSensorInfo->param14;
                            tempSensorInfo->SampleSecondLowQC = -99999;
                            tempSensorInfo->SampleSecondHighQC = 99999;
                            tempSensorInfo->DeltaDriftLowQC = -99999;
                            tempSensorInfo->DeltaDriftHighQC = 99999;

                            // first get the transient window settings.. pretending that the sample transient window is the sample window for
                            // the purposes of cal extrapolation
                            rc = CalculateEx(calciumReadings->readings, tempSensorInfo, bubbleDetect, sampleDetect, tempLevels,
                                true, testMode, true, topHeaterReadings, ageOfCard, ambientTemperature, false, false, false, 0, sensorInfo->param16, sensorInfo->param17, testMode, false, 0.0, false, nullptr, isBlood);

                            // convert the error codes for sample to sw peak
                            if (rc == ResultsCalcReturnCode::SampleNoiseQCHigh)
                            {
                                rc = ResultsCalcReturnCode::SWPeakNoiseHigh;
                            }
                            else if (rc == ResultsCalcReturnCode::SampleDriftQCLow)
                            {
                                rc = ResultsCalcReturnCode::SWPeakDriftLow;
                            }
                            else if (rc == ResultsCalcReturnCode::SampleDriftQCHigh)
                            {
                                rc = ResultsCalcReturnCode::SWPeakDriftHigh;
                            }
                            else
                            {
                                rc = ResultsCalcReturnCode::Success;
                            }

                            if (rc == ResultsCalcReturnCode::Success)
                            {
                                rc = CalculateEx(calciumReadings->readings, sensorInfo, bubbleDetect, sampleDetect, sensorLevels,
                                    true, testMode, true, topHeaterReadings, ageOfCard, ambientTemperature, false, false, false, 0, sensorInfo->param16, sensorInfo->param17, testMode, false, 0.0, false, ageCorrections, isBlood);
                            }

                            // this is the error that can get returned that we cant recover from..
                            // ...that there werent enough data points to calculate.
                            // if its just a qc failure.. continue.. but flag it as a qc failure as we
                            // leave
                            if (rc == ResultsCalcReturnCode::CannotCalculate)
                            {
                                calciumReadings->result = NAN;

                                // uncorrected may have failed iqc.. we dont want to overwrite the old return code
                                if (calciumReadings->returnCode == ResultsCalcReturnCode::Success)
                                {
                                    calciumReadings->returnCode = ResultsCalcReturnCode::CannotCalculate;
                                }
                            }
                            else
                            {
                                double calciumOffset = sensorInfo->param1;
                                double bicarbCorrect = sensorInfo->param2;
                                double k = sensorInfo->param3;
                                double lactKCa = lactK;
                                double lactOffCa = lactOff;

                                double decimalOffset = 0.0;
                                double decimalSlopeFactor = 0.0;

                                double AgeThreshpO2Corr = sensorInfo->param45;
                                double DeltaiCalThreshpO2Corr = sensorInfo->param46;
                                double OiC1 = sensorInfo->param47;
                                double OiC2 = sensorInfo->param48;
                                double OffsetFact = sensorInfo->param51;
                                double CorrFact = sensorInfo->param52;

                                // if lactate is available and didnt fail qc.. OR if its an old testconfig, then pick up the offset from offset and slope
                                if (!NewSodiumCalciumLactateCorrection(sensorInfo->param15))
                                {
                                    decimalOffset = offset;
                                    decimalSlopeFactor = doubleSlopeFactor;
                                }
                                else
                                {
                                    // so this is new and lactate is not available.. go to alt offset and slope
                                    decimalOffset = altOffset;
                                    decimalSlopeFactor = altSlope;
                                }

                                double swDiff = (sensorLevels->sampleEx - sensorLevels->calEx) - (tempLevels->sampleEx - tempLevels->calEx);

                                sensorLevels->response += sensorInfo->param11 * swDiff;

                                sensorLevels->peakEx = tempLevels->calEx;
                                sensorLevels->peakMean = tempLevels->sampleMean;
                                sensorLevels->peakSlope = tempLevels->sampleSlope;
                                sensorLevels->peakNoise = tempLevels->sampleNoise;
                                sensorLevels->peakSecond = tempLevels->sampleSecond;

                                calciumReadings->result = CSharpDecimalCalculation(((calConcentration + calciumOffset) *
                                    std::pow(10, CSharpDecimalCalculation((sensorLevels->response - decimalOffset) /
                                    (decimalSlopeFactor * 0.5 * FullNernst())))) - calciumOffset);

                                // bicarb not available yet

                                // lactate not available yet

                                // pco2 not available yet

                                if (rc == ResultsCalcReturnCode::Success)
                                {
                                    calciumReadings->returnCode = rc = ValidateInsanityRangeOnlyReturnCode(calciumReadings->result,
                                        testMode == TestMode::BloodTest ? calciumReadings->insanityLow : calciumReadings->insanityQALow,
                                        testMode == TestMode::BloodTest ? calciumReadings->insanityHigh : calciumReadings->insanityQAHigh);
                                }

                                // change calcium return code to failed qc ever if calcium failed realtime qc
                                if ((rc == ResultsCalcReturnCode::Success) && (calciumReadings->realTimeQCFailedEver == RealTimeQCReturnCode::Failed))
                                {
                                    calciumReadings->returnCode = rc = ResultsCalcReturnCode::FailedQCEver;
                                }

                                if ((rc == ResultsCalcReturnCode::Success) && (testMode == TestMode::BloodTest))
                                {
                                    calciumReadings->returnCode = ValidateReportableRangeOnlyReturnCode(calciumReadings->result, calciumReadings->reportableLow, calciumReadings->reportableHigh);
                                }
                                else
                                {
                                    calciumReadings->returnCode = rc;
                                }

                                // calculate calcium additional drift
                                // get number of window moves and save original slope
                                int numWindowMoves = (int)sensorInfo->param65;
                                double originalAdditionalSlope = calciumReadings->levels->additionalSlope;
                                double maximumSlope = originalAdditionalSlope;
                                double originalAdditionalWindow = sensorInfo->param49;
                                double maximumAdditionalWindow = sensorInfo->param49;

                                // for the number of window moves
                                for (int i = 0; i < numWindowMoves; i++)
                                {
                                    sensorInfo->param49 += 0.2;

                                    FindParams(calciumReadings->readings, bubbleDetect, sampleDetect, sensorInfo, sensorLevels, testMode, true, false, false, 0, testMode, 0.0, false);

                                    if (sensorLevels->additionalSlope > maximumSlope)
                                    {
                                        maximumSlope = sensorLevels->additionalSlope;
                                        maximumAdditionalWindow = sensorInfo->param49;
                                    }
                                }

                                if ((bubbleWidth != 0) && !isnan(bubbleWidth))
                                {
                                    if (!(CSharpDecimalCalculation(maximumSlope * sensorInfo->param67 / bubbleWidth) > sensorInfo->param68))
                                    {
                                        // add offset to original window and get aw slope
                                        sensorInfo->param49 = originalAdditionalWindow + sensorInfo->param66;
                                        FindParams(calciumReadings->readings, bubbleDetect, sampleDetect, sensorInfo, sensorLevels, testMode, true, false, false, 0, testMode, 0.0, false);

                                        // use the slope from this offsetted window
                                        maximumSlope = sensorLevels->additionalSlope;
                                        sensorLevels->output16 = CSharpDecimalCalculation(maximumSlope * sensorInfo->param67 / bubbleWidth);
                                    }
                                    else
                                    {
                                        sensorInfo->param49 = maximumAdditionalWindow;
                                        FindParams(calciumReadings->readings, bubbleDetect, sampleDetect, sensorInfo, sensorLevels, testMode, true, false, false, 0, testMode, 0.0, false);
                                        sensorLevels->additionalSlope = maximumSlope;
                                        sensorLevels->output16 = CSharpDecimalCalculation(maximumSlope * sensorInfo->param67 / bubbleWidth);
                                    }
                                }

                                // Store calcium additional window original location in output20. after calculatecalcium it will be copied back 
                                sensorLevels->output20 = CSharpDecimalCalculation(originalAdditionalWindow);

                                if (((calciumReadings->returnCode == ResultsCalcReturnCode::Success) ||
                                    (calciumReadings->returnCode == ResultsCalcReturnCode::UnderReportableRange) ||
                                    (calciumReadings->returnCode == ResultsCalcReturnCode::OverReportableRange))
                                    && !calciumReadings->requirementsFailedQC
                                    && (CSharpDecimalCalculation(maximumSlope * sensorInfo->param67 / bubbleWidth) > sensorInfo->param68))
                                {
                                    return true;
                                }
                                else
                                {
                                    return false;
                                }
                            }
                        }
                    }
                    catch (...) {}

                    return false;
                }

                ResultsCalcReturnCode AnalyticalManager::CalculateUncorrectedpH(std::shared_ptr<SensorReadings> pHReadings,
                                                                                std::shared_ptr<SensorReadings> pco2Readings,
                                                                                double bubbleDetect,
                                                                                double sampleDetect,
                                                                                TestMode testMode,
                                                                                std::shared_ptr<SensorReadings> topHeaterReadings,
                                                                                int ageOfCard,
                                                                                double ambientTemperature,
                                                                                bool isBlood,
                                                                                bool applyVolumeCorrections,
                                                                                double calciumAdditionalDrift)
                {
                    ResultsCalcReturnCode rc;
                    std::shared_ptr<SensorInfo> sensorInfo = pHReadings->sensorDescriptor;
                    std::shared_ptr<Levels> sensorLevels = pHReadings->levels;
                    auto tempSensorInfo = std::make_shared<SensorInfo>();
                    auto tempLevels = std::make_shared<Levels>();
                    auto ageCorrections = std::make_shared<AgeCorrectionsQABlood>();
                    double doubleSlopeFactor;
                    double calConcentration;
                    double offset;

                    if (isBlood || !PotsAndGluLacAqBlood(sensorInfo->param15))
                    {
                        doubleSlopeFactor = sensorInfo->slopeFactor;
                        offset = sensorInfo->offset;
                        calConcentration = sensorInfo->calConcentration;

                        ageCorrections->BSensor = sensorInfo->B;
                        ageCorrections->B2Sensor = sensorInfo->param29;
                        ageCorrections->B3Sensor = sensorInfo->param31;
                        ageCorrections->MvCut = sensorInfo->param33;
                        ageCorrections->AgeCut = sensorInfo->param35;
                        ageCorrections->FSensor = sensorInfo->F;

                        ageCorrections->LowMvInjTimeCut = sensorInfo->param57;
                        ageCorrections->FPrimeSensor = sensorInfo->param58;
                        ageCorrections->HighMvInjTimeCut = sensorInfo->param59;
                        ageCorrections->FDoublePrimeSensor = sensorInfo->param60;
                    }
                    else
                    {
                        doubleSlopeFactor = sensorInfo->param24;
                        offset = sensorInfo->param25;
                        calConcentration = sensorInfo->param26;

                        ageCorrections->BSensor = sensorInfo->param27;
                        ageCorrections->B2Sensor = sensorInfo->param30;
                        ageCorrections->B3Sensor = sensorInfo->param32;
                        ageCorrections->MvCut = sensorInfo->param34;
                        ageCorrections->AgeCut = sensorInfo->param36;
                        ageCorrections->FSensor = sensorInfo->param28;

                        ageCorrections->LowMvInjTimeCut = sensorInfo->param61;
                        ageCorrections->FPrimeSensor = sensorInfo->param62;
                        ageCorrections->HighMvInjTimeCut = sensorInfo->param63;
                        ageCorrections->FDoublePrimeSensor = sensorInfo->param64;
                    }
                    ageCorrections->GSensor = sensorInfo->G;

                    if (pHEarlyWindow(sensorInfo->param15) && (testMode == TestMode::BloodTest))
                    {
                        CopySensorInfo(tempSensorInfo, sensorInfo);
                        tempSensorInfo->sampleDelimit = tempSensorInfo->param9;
                        tempSensorInfo->sampleWindowSize = tempSensorInfo->param10;
                        tempSensorInfo->extrapolation = tempSensorInfo->param9 + (tempSensorInfo->param10 / 2.0);
                        tempSensorInfo->SampleMeanLowQC = -99999;
                        tempSensorInfo->SampleMeanHighQC = 99999;
                        tempSensorInfo->SampleNoiseHighQC = 99999;
                        tempSensorInfo->SampleDriftLowQC = -99999;
                        tempSensorInfo->SampleDriftHighQC = 99999;
                        tempSensorInfo->SampleSecondLowQC = -99999;
                        tempSensorInfo->SampleSecondHighQC = 99999;
                        tempSensorInfo->DeltaDriftLowQC = -99999;
                        tempSensorInfo->DeltaDriftHighQC = 99999;

                        // first get the transient window settings.. pretending that the sample transient window is the sample window for
                        // the purposes of cal extrapolation
                        rc = CalculateEx(pHReadings->readings, tempSensorInfo, bubbleDetect, sampleDetect, tempLevels, true, testMode, true, topHeaterReadings, ageOfCard, ambientTemperature, false, false, false, 0, sensorInfo->param20, sensorInfo->param21, testMode, false, 0.0, false, nullptr, isBlood);

                        if (rc != ResultsCalcReturnCode::CannotCalculate)
                        {
                            double responseEarlyWindow = tempLevels->sampleMean - tempLevels->calEx;
                            double deltaDrift = tempLevels->sampleSlope - tempLevels->calSlope;
                            double k1 = sensorInfo->param11;
                            double k2 = sensorInfo->param12;
                            double a1 = sensorInfo->param13;
                            double a2 = sensorInfo->param14;
                            double upperLimit = sensorInfo->param16;
                            double lowerLimit = sensorInfo->param17;

                            double multiplier = 1.0;

                            if (MultiplypHEarlyWindowByCalMean(sensorInfo->param15))
                            {
                                multiplier = tempLevels->calMean;
                            }

                            double a1Calc = (deltaDrift - (a1 * (responseEarlyWindow + ((ageOfCard - k1) * k2)))) * multiplier;
                            double a2Calc = (deltaDrift - (a2 * (responseEarlyWindow + ((ageOfCard - k1) * k2)))) * multiplier;

                            sensorLevels->peakEx = responseEarlyWindow;
                            sensorLevels->peakMean = a2Calc;
                            sensorLevels->peakSlope = deltaDrift;
                            sensorLevels->peakNoise = tempLevels->sampleNoise;
                            sensorLevels->peakSecond = tempLevels->sampleSecond;

                            if (rc == ResultsCalcReturnCode::Success)
                            {
                                if (a1Calc > upperLimit)
                                {
                                    rc = pHReadings->returnCode = ResultsCalcReturnCode::EarlyWindowHigh;
                                }
                                else if (a2Calc < lowerLimit)
                                {
                                    rc = pHReadings->returnCode = ResultsCalcReturnCode::EarlyWindowLow;
                                }
                            }
                        }
                    }
                    else
                    {
                        rc = ResultsCalcReturnCode::Success;
                    }

                    ResultsCalcReturnCode tempRc = CalculateEx(pHReadings->readings, sensorInfo, bubbleDetect, sampleDetect, sensorLevels, true, testMode, true, topHeaterReadings, ageOfCard, ambientTemperature, false, false, false, 0, sensorInfo->param20, sensorInfo->param21, testMode, false, 0.0, false, ageCorrections, isBlood);

                    // make sure we only overwrite the return code if it's a success
                    if (rc == ResultsCalcReturnCode::Success)
                    {
                        rc = tempRc;
                    }

                    // this is the error that can get returned that we cant recover from..
                    // ...that there werent enough data points to calculate.
                    // if its just a qc failure.. continue.. but flag it as a qc failure as we
                    // leave
                    if ((rc == ResultsCalcReturnCode::CannotCalculate) || (tempRc == ResultsCalcReturnCode::CannotCalculate))
                    {
                        pHReadings->result = NAN;
                        return (pHReadings->returnCode = rc);
                    }

                    double k = sensorInfo->param2;

                    double ageSlopeCorrection = sensorInfo->param19;
                    double slopeFactor = doubleSlopeFactor;

                    if (isBlood && !isnan(calciumAdditionalDrift))
                    {
                        slopeFactor = slopeFactor + sensorInfo->param22 * (sensorInfo->param23 + calciumAdditionalDrift);
                    }

                    // make k correction to uncorrectedph
                    if (pco2Readings != nullptr)
                    {
                        sensorLevels->response += k * CSharpDecimalCalculation(pco2Readings->result - pco2Readings->sensorDescriptor->calConcentration);
                    }

                    sensorLevels->output3 = sensorLevels->response;

                    pHReadings->result = CSharpDecimalCalculation(calConcentration - ((sensorLevels->response - offset) / (slopeFactor * FullNernst())));

                    double cutoff = sensorInfo->param18;

                    // new ph stuff from gerhard
                    if (pHReadings->result > cutoff)
                    {
                        pHReadings->result = CSharpDecimalCalculation(calConcentration - ((sensorLevels->response - offset) / (slopeFactor * FullNernst() - (ageSlopeCorrection * (ageOfCard - sensorInfo->AgeOffset)))));
                    }

                    if (applyVolumeCorrections && isBlood)
                    {
                        pHReadings->result = pHReadings->result + sensorInfo->param45 * (pHReadings->result - sensorInfo->param46);
                    }

                    if (rc == ResultsCalcReturnCode::Success)
                    {
                        pHReadings->returnCode = rc = ValidateInsanityRangeOnlyReturnCode(pHReadings->result,
                            testMode == TestMode::BloodTest ? pHReadings->insanityLow : pHReadings->insanityQALow,
                            testMode == TestMode::BloodTest ? pHReadings->insanityHigh : pHReadings->insanityQAHigh);
                    }

                    if ((rc == ResultsCalcReturnCode::Success) && (pHReadings->realTimeQCFailedEver == RealTimeQCReturnCode::Failed))
                    {
                        pHReadings->returnCode = rc = ResultsCalcReturnCode::FailedQCEver;
                    }

                    if (rc == ResultsCalcReturnCode::Success)
                    {
                        pHReadings->returnCode = rc = ValidateReportableRangeOnlyReturnCode(pHReadings->result, pHReadings->reportableLow, pHReadings->reportableHigh);
                    }

                    sensorLevels->output5 = CSharpDecimalCalculation(pHReadings->result);
                    return pHReadings->returnCode = rc;
                }

                ResultsCalcReturnCode AnalyticalManager::CalculateBGESensor(std::shared_ptr<SensorReadings> currentSensor,
                                                                            std::vector<std::shared_ptr<SensorReadings>> &testReadings,
                                                                            double bicarb,
                                                                            double bubbleDetect,
                                                                            double sampleDetect,
                                                                            double ambientTemperature,
                                                                            double ambientPressure,
                                                                            TestMode testMode,
                                                                            std::shared_ptr<SensorReadings> topHeaterReadings,
                                                                            int ageOfCard,
                                                                            bool applyHemodilution,
                                                                            int sibVersion,
                                                                            bool isBlood,
                                                                            bool applyVolumeCorrection,
                                                                            double calciumAdditionalDrift,
                                                                            bool allowNegativeValues)
                {
                    try
                    {
                        if (currentSensor->sensorType == Sensors::Urea)
                        {
                            // calculate bun.. we need pco2
                            std::shared_ptr<SensorReadings> pco2Reading = nullptr;
                            std::shared_ptr<SensorReadings> lactateReading = nullptr;
                            std::shared_ptr<SensorReadings> hctReading = nullptr;
                            std::shared_ptr<SensorReadings> potassiumReading = nullptr;

                            for (size_t i = 0; i < testReadings.size(); i++)
                            {
                                if (testReadings[i]->sensorType == Sensors::CarbonDioxide)
                                {
                                    pco2Reading = testReadings[i];
                                }

                                if (testReadings[i]->sensorType == Sensors::Lactate)
                                {
                                    lactateReading = testReadings[i];
                                }

                                if (testReadings[i]->sensorType == Sensors::Conductivity)
                                {
                                    hctReading = testReadings[i];
                                }

                                if (testReadings[i]->sensorType == Sensors::Potassium)
                                {
                                    potassiumReading = testReadings[i];
                                }
                            }

                            if (pco2Reading == nullptr)
                            {
                                currentSensor->result = NAN;
                                currentSensor->returnCode = ResultsCalcReturnCode::CannotCalculate;
                                return currentSensor->returnCode;
                            }
                            else if (hctReading == nullptr)
                            {
                                currentSensor->result = NAN;
                                currentSensor->returnCode = ResultsCalcReturnCode::CannotCalculate;
                                return currentSensor->returnCode;
                            }
                            else if (potassiumReading == nullptr)
                            {
                                currentSensor->result = NAN;
                                currentSensor->returnCode = ResultsCalcReturnCode::CannotCalculate;
                                return currentSensor->returnCode;
                            }
                            else
                            {
                                currentSensor->returnCode = CalculateBUN(currentSensor,
                                                                         pco2Reading,
                                                                         hctReading,
                                                                         lactateReading,
                                                                         potassiumReading,
                                                                         bubbleDetect,
                                                                         sampleDetect,
                                                                         bicarb,
                                                                         testMode,
                                                                         topHeaterReadings,
                                                                         ageOfCard,
                                                                         ambientTemperature,
                                                                         isBlood,
                                                                         calciumAdditionalDrift);
                                return currentSensor->returnCode;
                            }
                        }
                        else if (currentSensor->sensorType == Sensors::TCO2)
                        {
                            // calculate sodium.. we need pco2
                            std::shared_ptr<SensorReadings> pco2Reading = nullptr;
                            std::shared_ptr<SensorReadings> phReading = nullptr;
                            std::shared_ptr<SensorReadings> naReading = nullptr;

                            for (size_t i = 0; i < testReadings.size(); i++)
                            {
                                if (testReadings[i]->sensorType == Sensors::CarbonDioxide)
                                {
                                    pco2Reading = testReadings[i];
                                }

                                if (testReadings[i]->sensorType == Sensors::pH)
                                {
                                    phReading = testReadings[i];
                                }

                                if (testReadings[i]->sensorType == Sensors::Sodium)
                                {
                                    naReading = testReadings[i];
                                }
                            }

                            if ((pco2Reading == nullptr) || (phReading == nullptr) || (naReading == nullptr))
                            {
                                currentSensor->result = NAN;
                                currentSensor->returnCode = ResultsCalcReturnCode::CannotCalculate;
                                return currentSensor->returnCode;
                            }
                            else
                            {
                                currentSensor->returnCode = CalculateMeasuredTCO2(currentSensor,
                                                                                  pco2Reading,
                                                                                  phReading,
                                                                                  naReading,
                                                                                  bubbleDetect,
                                                                                  sampleDetect,
                                                                                  bicarb,
                                                                                  testMode,
                                                                                  topHeaterReadings,
                                                                                  ageOfCard,
                                                                                  ambientTemperature,
                                                                                  isBlood,
                                                                                  applyVolumeCorrection);
                                return currentSensor->returnCode;
                            }
                        }
                        else if (currentSensor->sensorType == Sensors::Sodium)
                        {
                            // calculate sodium.. we need pco2
                            std::shared_ptr<SensorReadings> pco2Reading = nullptr;
                            std::shared_ptr<SensorReadings> lactateReading = nullptr;

                            for (size_t i = 0; i < testReadings.size(); i++)
                            {
                                if (testReadings[i]->sensorType == Sensors::CarbonDioxide)
                                {
                                    pco2Reading = testReadings[i];
                                }

                                if (testReadings[i]->sensorType == Sensors::Lactate)
                                {
                                    lactateReading = testReadings[i];
                                }
                            }

                            if (pco2Reading == nullptr)
                            {
                                currentSensor->result = NAN;
                                currentSensor->returnCode = ResultsCalcReturnCode::CannotCalculate;
                                return currentSensor->returnCode;
                            }
                            else
                            {
                                currentSensor->returnCode = CalculateSodium(currentSensor,
                                                                            pco2Reading,
                                                                            lactateReading,
                                                                            bubbleDetect,
                                                                            sampleDetect,
                                                                            bicarb,
                                                                            testMode,
                                                                            topHeaterReadings,
                                                                            ageOfCard,
                                                                            ambientTemperature,
                                                                            isBlood,
                                                                            applyVolumeCorrection,
                                                                            false,
                                                                            calciumAdditionalDrift);
                                return currentSensor->returnCode;
                            }
                        }
                        else if (currentSensor->sensorType == Sensors::Potassium)
                        {
                            // calculate potassium.. we need pco2
                            std::shared_ptr<SensorReadings> pco2Reading = nullptr;
                            std::shared_ptr<SensorReadings> lactateReading = nullptr;

                            for (size_t i = 0; i < testReadings.size(); i++)
                            {
                                if (testReadings[i]->sensorType == Sensors::CarbonDioxide)
                                {
                                    pco2Reading = testReadings[i];
                                }

                                if (testReadings[i]->sensorType == Sensors::Lactate)
                                {
                                    lactateReading = testReadings[i];
                                }
                            }

                            if (pco2Reading == nullptr)
                            {
                                currentSensor->result = NAN;
                                currentSensor->returnCode = ResultsCalcReturnCode::CannotCalculate;
                                return currentSensor->returnCode;
                            }
                            else
                            {
                                currentSensor->returnCode = CalculatePotassium(currentSensor,
                                                                               pco2Reading,
                                                                               lactateReading,
                                                                               bubbleDetect,
                                                                               sampleDetect,
                                                                               bicarb,
                                                                               testMode,
                                                                               topHeaterReadings,
                                                                               ageOfCard,
                                                                               ambientTemperature,
                                                                               isBlood,
                                                                               applyVolumeCorrection,
                                                                               false,
                                                                               calciumAdditionalDrift);
                                return currentSensor->returnCode;
                            }
                        }
                        else if (currentSensor->sensorType == Sensors::Chloride)
                        {
                            // calculate chloride.. we need pco2
                            std::shared_ptr<SensorReadings> pco2Reading = nullptr;
                            std::shared_ptr<SensorReadings> hctReading = nullptr;
                            std::shared_ptr<SensorReadings> lactateReading = nullptr;
                            std::shared_ptr<SensorReadings> po2Reading = nullptr;
                            std::shared_ptr<SensorReadings> goldReading = nullptr;
                            bool bunAvailable = false;

                            for (size_t i = 0; i < testReadings.size(); i++)
                            {
                                if (testReadings[i]->sensorType == Sensors::CarbonDioxide)
                                {
                                    pco2Reading = testReadings[i];
                                }

                                if (testReadings[i]->sensorType == Sensors::Oxygen)
                                {
                                    po2Reading = testReadings[i];
                                }

                                if (testReadings[i]->sensorType == Sensors::Lactate)
                                {
                                    lactateReading = testReadings[i];
                                }

                                if (testReadings[i]->sensorType == Sensors::Conductivity)
                                {
                                    hctReading = testReadings[i];
                                }

                                if (testReadings[i]->sensorType == Sensors::Gold)
                                {
                                    goldReading = testReadings[i];
                                }

                                if (testReadings[i]->sensorType == Sensors::Urea)
                                {
                                    bunAvailable = true;
                                }
                            }

                            if (hctReading == nullptr)
                            {
                                currentSensor->result = NAN;
                                currentSensor->returnCode = ResultsCalcReturnCode::CannotCalculate;
                                return currentSensor->returnCode;
                            }
                            else
                            {
                                currentSensor->returnCode = CalculateChloride(currentSensor,
                                                                              pco2Reading,
                                                                              lactateReading,
                                                                              hctReading,
                                                                              po2Reading,
                                                                              goldReading,
                                                                              bubbleDetect,
                                                                              sampleDetect,
                                                                              bicarb,
                                                                              testMode,
                                                                              topHeaterReadings,
                                                                              ageOfCard,
                                                                              ambientTemperature,
                                                                              isBlood,
                                                                              applyVolumeCorrection,
                                                                              calciumAdditionalDrift,
                                                                              bunAvailable);
                                return currentSensor->returnCode;
                            }
                        }
                        else if (currentSensor->sensorType == Sensors::Calcium)
                        {
                            // calculate calcium.. we need pco2
                            std::shared_ptr<SensorReadings> pco2Reading = nullptr;
                            std::shared_ptr<SensorReadings> lactateReading = nullptr;
                            std::shared_ptr<SensorReadings> po2Reading = nullptr;

                            for (size_t i = 0; i < testReadings.size(); i++)
                            {
                                if (testReadings[i]->sensorType == Sensors::CarbonDioxide)
                                {
                                    pco2Reading = testReadings[i];
                                }

                                if (testReadings[i]->sensorType == Sensors::Oxygen)
                                {
                                    po2Reading = testReadings[i];
                                }

                                if (testReadings[i]->sensorType == Sensors::Lactate)
                                {
                                    lactateReading = testReadings[i];
                                }
                            }

                            if ((pco2Reading == nullptr) || (po2Reading == nullptr))
                            {
                                currentSensor->result = NAN;
                                currentSensor->returnCode = ResultsCalcReturnCode::CannotCalculate;
                                return currentSensor->returnCode;
                            }
                            else
                            {
                                currentSensor->returnCode = CalculateCalcium(currentSensor,
                                                                             pco2Reading,
                                                                             lactateReading,
                                                                             po2Reading,
                                                                             bubbleDetect,
                                                                             sampleDetect,
                                                                             bicarb,
                                                                             testMode,
                                                                             topHeaterReadings,
                                                                             ageOfCard,
                                                                             ambientTemperature,
                                                                             isBlood,
                                                                             applyVolumeCorrection,
                                                                             calciumAdditionalDrift);

                                // we used use output 20 in precalculatecalcium to store the original additional window location
                                currentSensor->sensorDescriptor->param49 = currentSensor->levels->output20;
                                return currentSensor->returnCode;
                            }
                        }
                        else if (currentSensor->sensorType == Sensors::pH)
                        {
                            // calculate ph.. we need pco2
                            std::shared_ptr<SensorReadings> pco2Reading = nullptr;
                            std::shared_ptr<SensorReadings> hctReading = nullptr;
                            std::shared_ptr<SensorReadings> calciumReading = nullptr;

                            for (size_t i = 0; i < testReadings.size(); i++)
                            {
                                if (testReadings[i]->sensorType == Sensors::CarbonDioxide)
                                {
                                    pco2Reading = testReadings[i];
                                }

                                if (testReadings[i]->sensorType == Sensors::Conductivity)
                                {
                                    hctReading = testReadings[i];
                                }
                            }

                            if ((pco2Reading == nullptr) || (hctReading == nullptr))
                            {
                                currentSensor->result = NAN;
                                currentSensor->returnCode = ResultsCalcReturnCode::CannotCalculate;
                                return currentSensor->returnCode;
                            }
                            else
                            {
                                currentSensor->returnCode = CalculatepH(currentSensor,
                                                                        pco2Reading,
                                                                        hctReading,
                                                                        bubbleDetect,
                                                                        sampleDetect,
                                                                        bicarb,
                                                                        testMode,
                                                                        topHeaterReadings,
                                                                        ageOfCard,
                                                                        ambientTemperature,
                                                                        isBlood,
                                                                        applyVolumeCorrection,
                                                                        calciumAdditionalDrift);
                                return currentSensor->returnCode;
                            }
                        }
                        else if (currentSensor->sensorType == Sensors::Oxygen)
                        {
                            // calculate ph.. we need pco2
                            std::shared_ptr<SensorReadings> glucoseReadings = nullptr;

                            for (size_t i = 0; i < testReadings.size(); i++)
                            {
                                if (testReadings[i]->sensorType == Sensors::Glucose)
                                {
                                    glucoseReadings = testReadings[i];
                                    break;
                                }
                            }

                            currentSensor->returnCode = CalculatePO2(currentSensor,
                                glucoseReadings,
                                bubbleDetect,
                                sampleDetect,
                                ambientTemperature,
                                ambientPressure,
                                testMode,
                                ageOfCard,
                                sibVersion);
                            return currentSensor->returnCode;
                        }
                        else if (currentSensor->sensorType == Sensors::Glucose)
                        {
                            std::shared_ptr<SensorReadings> oxygenReading = nullptr;
                            std::shared_ptr<SensorReadings> potassiumReading = nullptr;
                            std::shared_ptr<SensorReadings> co2Reading = nullptr;
                            std::shared_ptr<SensorReadings> hctReading = nullptr;

                            for (size_t i = 0; i < testReadings.size(); i++)
                            {
                                if (testReadings[i]->sensorType == Sensors::Oxygen)
                                {
                                    oxygenReading = testReadings[i];
                                }

                                if (testReadings[i]->sensorType == Sensors::Potassium)
                                {
                                    potassiumReading = testReadings[i];
                                }

                                if (testReadings[i]->sensorType == Sensors::CarbonDioxide)
                                {
                                    co2Reading = testReadings[i];
                                }

                                if (testReadings[i]->sensorType == Sensors::Conductivity)
                                {
                                    hctReading = testReadings[i];
                                }
                            }

                            currentSensor->returnCode = CalculateGlucose(currentSensor,
                                bubbleDetect,
                                sampleDetect,
                                testMode,
                                ageOfCard,
                                oxygenReading,
                                co2Reading,
                                hctReading,
                                sibVersion,
                                isBlood,
                                allowNegativeValues);
                            return currentSensor->returnCode;
                        }
                        else if (currentSensor->sensorType == Sensors::Lactate)
                        {
                            std::shared_ptr<SensorReadings> oxygenReading = nullptr;
                            std::shared_ptr<SensorReadings> potassiumReading = nullptr;
                            std::shared_ptr<SensorReadings> co2Reading = nullptr;
                            std::shared_ptr<SensorReadings> hctReading = nullptr;

                            for (size_t i = 0; i < testReadings.size(); i++)
                            {
                                if (testReadings[i]->sensorType == Sensors::Oxygen)
                                {
                                    oxygenReading = testReadings[i];
                                }

                                if (testReadings[i]->sensorType == Sensors::CarbonDioxide)
                                {
                                    co2Reading = testReadings[i];
                                }

                                if (testReadings[i]->sensorType == Sensors::Conductivity)
                                {
                                    hctReading = testReadings[i];
                                }
                            }

                            currentSensor->returnCode = CalculateLactate(currentSensor,
                                bubbleDetect,
                                sampleDetect,
                                testMode,
                                ageOfCard,
                                oxygenReading,
                                co2Reading,
                                hctReading,
                                sibVersion,
                                isBlood,
                                allowNegativeValues);
                            return currentSensor->returnCode;
                        }
                        else if (currentSensor->sensorType == Sensors::CarbonDioxide)
                        {
                            currentSensor->returnCode = CalculatePCO2(currentSensor,
                                                                      bubbleDetect,
                                                                      sampleDetect,
                                                                      bicarb,
                                                                      testMode,
                                                                      topHeaterReadings,
                                                                      ageOfCard,
                                                                      ambientTemperature,
                                                                      applyVolumeCorrection,
                                                                      isBlood,
                                                                      calciumAdditionalDrift);
                            return currentSensor->returnCode;
                        }
                        else if (currentSensor->sensorType == Sensors::Conductivity)
                        {
                            // calculate hematocrit
                            std::shared_ptr<SensorReadings> sodiumReading = nullptr;
                            std::shared_ptr<SensorReadings> potassiumReading = nullptr;

                            for (size_t i = 0; i < testReadings.size(); i++)
                            {
                                if (testReadings[i]->sensorType == Sensors::Sodium)
                                {
                                    sodiumReading = testReadings[i];
                                }

                                if (testReadings[i]->sensorType == Sensors::Potassium)
                                {
                                    potassiumReading = testReadings[i];
                                }
                            }

                            // sodium found.. calculate hematocrit
                            currentSensor->returnCode = CalculateHematocrit(currentSensor,
                                sodiumReading,
                                potassiumReading,
                                bubbleDetect,
                                sampleDetect,
                                testMode,
                                applyHemodilution,
                                testMode,
                                false,
                                applyVolumeCorrection,
                                isBlood,
                                calciumAdditionalDrift);
                            return currentSensor->returnCode;
                        }
                        else if (currentSensor->sensorType == Sensors::Creatinine)
                        {
                            std::shared_ptr<SensorReadings> oxygenReading = nullptr;
                            std::shared_ptr<SensorReadings> potassiumReading = nullptr;
                            std::shared_ptr<SensorReadings> co2Reading = nullptr;
                            std::shared_ptr<SensorReadings> hctReadings = nullptr;

                            for (size_t i = 0; i < testReadings.size(); i++)
                            {
                                if (testReadings[i]->sensorType == Sensors::Oxygen)
                                {
                                    oxygenReading = testReadings[i];
                                }

                                if (testReadings[i]->sensorType == Sensors::Potassium)
                                {
                                    potassiumReading = testReadings[i];
                                }

                                if (testReadings[i]->sensorType == Sensors::CarbonDioxide)
                                {
                                    co2Reading = testReadings[i];
                                }

                                if (testReadings[i]->sensorType == Sensors::Conductivity)
                                {
                                    hctReadings = testReadings[i];
                                }
                            }

                            currentSensor->returnCode = CalculateCreatinine(currentSensor,
                                                                            bubbleDetect,
                                                                            sampleDetect,
                                                                            testMode,
                                                                            topHeaterReadings,
                                                                            ageOfCard,
                                                                            oxygenReading,
                                                                            potassiumReading,
                                                                            co2Reading,
                                                                            hctReadings,
                                                                            bicarb,
                                                                            sibVersion,
                                                                            isBlood,
                                                                            ambientPressure,
                                                                            allowNegativeValues);
                            return currentSensor->returnCode;
                        }
                    }
                    catch (...)
                    {
                        // only override return code if it is success, not anything else. iqc failures will remain
                        if (currentSensor->returnCode == ResultsCalcReturnCode::Success)
                        {
                            currentSensor->returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }

                        currentSensor->result = NAN;
                        return currentSensor->returnCode;
                    }

                    return ResultsCalcReturnCode::UnexplainedFailure;
                }

                bool AnalyticalManager::IsChannelTypeASensor(ChannelType channelType)
                {
#ifdef NEXT_GEN
                    if (((channelType >= ChannelType::P1) && (channelType <= ChannelType::A4)) ||
                        ((channelType >= ChannelType::A2_STIP) && (channelType <= ChannelType::A4_STIM)) ||
                        ((channelType >= ChannelType::A1ST0) && (channelType <= ChannelType::A4Sti)) ||
                        ((channelType >= ChannelType::CH_2_A1) && (channelType <= ChannelType::CH_2_A4)))
#else
                    if (((channelType >= ChannelType::P1) && (channelType <= ChannelType::A4)) ||
                        ((channelType >= ChannelType::A2_STIP) && (channelType <= ChannelType::A4_STIM)) ||
                        ((channelType >= ChannelType::A1ST0) && (channelType <= ChannelType::A4Sti)))
#endif
                        return true;
                    else
                        return false;
                }

                void AnalyticalManager::SetAllReturnCodesCnc(std::vector<std::shared_ptr<SensorReadings>> &testReadings)
                {
                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        testReadings[i]->result = NAN;
                        testReadings[i]->returnCode = ResultsCalcReturnCode::CannotCalculate;
                    }
                }

            }
        }
    }
}
