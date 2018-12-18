//
// Created by michde on 8/2/2017.
//

#define _USE_MATH_DEFINES
#include <algorithm>
#include <cmath>

#include "Analytes.h"
#include "FluidType.h"
#include "RealTimeQCReturnCode.h"

#include "analyticalmanager.h"

#include "MathHelp.h"

using namespace Epoc::Common::Native::Definitions;

namespace Epoc {
    namespace AM {
        namespace Native {
            namespace Bge {

                void AnalyticalManager::CalculateBGE(IN_OUT std::vector<std::shared_ptr<SensorReadings>> &testReadings, IN_OUT BGEParameters &parameters, IN bool allowNegativeValues)
                {
                    double bubbleDetect;
                    double sampleDetect;
                    float ambientTemperatureFloat;
                    float ambientPressureFloat;
                    double ambientTemperature;
                    double ambientPressure;
                    bool applyMultiplicationFactor;
                    FluidType fluidType;
                    std::shared_ptr<SensorReadings> topHeaterPower = nullptr;
                    int ageOfCard;
                    bool applyHemodilution = false;
                    double fluidAfterFluidThreshold = 150000;
                    double maxBubbleWidth= 3.4;
                    double bubbleWidth = NAN;
                    int sibVersion = 0;
                    double anionInsanityLow;
                    double anionInsanityHigh;
                    double bicarbInsanityLow;
                    double bicarbInsanityHigh;
                    bool applymTCO2 = false;
                    TestMode testMode;
                    bool sampleFailedQC = false;


                    // we need 9 parameters.. less is bad
                    if (parameters.Count() < 9)
                    {
                        SetAllReturnCodesCnc(testReadings);
                        parameters.SetParam(8, true);
                        return;
                    }

                    try
                    {
                        parameters.GetParam(0, &bubbleDetect);
                        parameters.GetParam(1, &sampleDetect);
                        if (parameters.GetParam(2, &ambientTemperatureFloat)) {
                            // convert ambientTemperature from float to double
                            ambientTemperature = ambientTemperatureFloat;
                        }
                        else {
                            throw std::invalid_argument("BGEParameter.GetParam(2, value): value has non float type.");
                        }
                        if (parameters.GetParam(3, &ambientPressureFloat)) {
                            // convert ambientPressure from float to double
                            ambientPressure = ambientPressureFloat;
                        }
                        else {
                            throw std::invalid_argument("BGEParameter.GetParam(3, value): value has non float type.");
                        }
                        parameters.GetParam(4, &applyMultiplicationFactor);
                        parameters.GetParam(5, &fluidType);
                        parameters.GetParam(6, &topHeaterPower);
                        parameters.GetParam(7, &ageOfCard);

                        // analytical tools may be a bit behind here.. so make accommodations for the
                        // 9th parameter not being there
                        if (!parameters.GetParam(8, &applyHemodilution))
                        {
                            applyHemodilution = false;
                        }

                        if (!parameters.GetParam(9, &bubbleWidth))
                        {
                            bubbleWidth = NAN;
                        }

                        if (!parameters.GetParam(10, &sibVersion))
                        {
                            sibVersion = 0;
                        }

                        bool found = true;
                        if (!parameters.GetParam(11, &anionInsanityLow) ||
                            !parameters.GetParam(12, &anionInsanityHigh) ||
                            !parameters.GetParam(13, &bicarbInsanityLow) ||
                            !parameters.GetParam(14, &bicarbInsanityHigh))
                            found = false;

                        // If any of the 4 above GetParam() calls fail, set all 4 values
                        if (!found)
                        {
                            anionInsanityLow = -99999;
                            anionInsanityHigh = 99999;
                            bicarbInsanityLow = -99999;
                            bicarbInsanityHigh = 99999;
                        }

                        // find out whether to use tco2 instead of chco3 in agap/k
                        if (!parameters.GetParam(15, &applymTCO2))
                        {
                            applymTCO2 = false;
                        }
                    }
                    catch (...)
                    {
                        SetAllReturnCodesCnc(testReadings);
                        parameters.AddParam(true);
                        return;
                    }

                    // Set the test type.. this will determine how hematocrit is calculated
                    if ((fluidType == FluidType::Blood) || (fluidType == FluidType::Unknown) || (fluidType == FluidType::AutoDetect))
                        testMode = TestMode::BloodTest;
                    else
                        testMode = TestMode::QA;

                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        if (IsChannelTypeASensor(testReadings[i]->channelType) && applyMultiplicationFactor)
                        {
                            (testReadings[i])->FixValues();
                        }
                    }

                    std::shared_ptr<std::vector<Reading>> unSmoothedGold = nullptr;
                    std::shared_ptr<std::vector<Reading>> unSmoothedBun = nullptr;
                    size_t locationOfBun = 0;
                    size_t locationOfGold = 0;

                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        if (testReadings[i]->sensorType == Sensors::Urea)
                        {
                            locationOfBun = i;

                            // create a list of the right size
                            unSmoothedBun = std::make_shared<std::vector<Reading>>();

                            // store the old BUN readings somewhere
                            unSmoothedBun->reserve(testReadings[i]->readings->size());
                            for (size_t j = 0; j < testReadings[i]->readings->size(); j++)
                            {
                                Reading tempReading;
                                tempReading.Time = (*(testReadings[i]->readings))[j].Time;
                                tempReading.Value = (*(testReadings[i]->readings))[j].Value;
                                unSmoothedBun->push_back(tempReading);
                            }

                            SmoothBUNReadings(testReadings[i], unSmoothedBun);
                            break;
                        }
                    }

                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        if (testReadings[i]->sensorType == Sensors::Gold)
                        {
                            locationOfGold = i;

                            // create a list of the right size
                            unSmoothedGold = std::make_shared<std::vector<Reading>>(); 

                            // store the old BUN readings somewhere
                            unSmoothedGold->reserve(testReadings[i]->readings->size());
                            for (size_t j = 0; j < testReadings[i]->readings->size(); j++)
                            {
                                Reading tempReading;
                                tempReading.Time = (*(testReadings[i]->readings))[j].Time;
                                tempReading.Value = (*(testReadings[i]->readings))[j].Value;
                                unSmoothedGold->push_back(tempReading);
                            }

                            SmoothBUNReadings(testReadings[i], unSmoothedGold);
                            break;
                        }
                    }

                    // now we have to determine, via hematocrit, if we have to move the sampledetect time
                    // sample detect is sent in as ref, so now the function can change it
                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        if (testReadings[i]->sensorType == Sensors::Conductivity)
                        {
                            try
                            {
                                PreTestHematocrit(testReadings[i], fluidAfterFluidThreshold, maxBubbleWidth, bubbleDetect, testMode, sampleDetect);
                            }
                            catch (...)
                            {
                            }

                            // if this hasnt changed, then it hasnt changed.. if it has, it has.. so either way
                            // doing this is no problem
                            parameters.SetParam(1, sampleDetect);

                            break;
                        }
                    }

                    if ((topHeaterPower == nullptr) || (topHeaterPower->sensorDescriptor == nullptr))
                    {
                        // make it so that the extra age/thermal stuff will produce zero
                        topHeaterPower->levels->response = -1;
                        ageOfCard = -1;
                    }
                    else
                    {
                        try
                        {
                            // first do the whole power thing.. what we really want from this is calmean and samplemean
                            // the limits for everything except the mean should be set wide enough so that it won't fail
                            // this way.. the noise in particular wont cause the cal window to be moved backwards
                            FindParams(topHeaterPower->readings,
                                       bubbleDetect,
                                       sampleDetect,
                                       topHeaterPower->sensorDescriptor,
                                       topHeaterPower->levels,
                                       testMode,
                                       false,
                                       false,
                                       false,
                                       0.0,
                                       testMode,
                                       0.0,
                                       false);

                            // we will use response for the power.. which is just the samplemean - calmean
                            topHeaterPower->levels->response = topHeaterPower->levels->sampleMean - topHeaterPower->levels->calMean;
                        }
                        catch (...)
                        {
                            topHeaterPower->levels->response = -1;
                            ageOfCard = -1;
                        }
                    }

                    bool isBlood = PreCalculateHematocrit(testReadings, bubbleDetect, sampleDetect, testMode, applyHemodilution, topHeaterPower, ageOfCard, ambientTemperature);
                    bool applyVolumeCorrection = PreCalculateCalcium(testReadings, bubbleDetect, sampleDetect, testMode, topHeaterPower, ageOfCard, ambientTemperature, isBlood, bubbleWidth);

                    // now we will have a fluid type.. so go ahead and calculate pco2 and ph
                    double uncorrectedBicarb = NAN;
                    std::shared_ptr<SensorReadings> pco2Readings = nullptr;
                    std::shared_ptr<SensorReadings> phReadings = nullptr;
                    std::shared_ptr<SensorReadings> calciumReadings = nullptr;

                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        if (testReadings[i]->sensorType == Sensors::pH)
                        {
                            phReadings = testReadings[i];
                        }
                        else if (testReadings[i]->sensorType == Sensors::CarbonDioxide)
                        {
                            pco2Readings = testReadings[i];
                        }
                        if (testReadings[i]->sensorType == Sensors::Calcium)
                        {
                            calciumReadings = testReadings[i];
                        }
                    }

                    double calciumAdditionalDrift;

                    if (((calciumReadings->returnCode != ResultsCalcReturnCode::Success) &&
                         (calciumReadings->returnCode != ResultsCalcReturnCode::OverReportableRange) &&
                         (calciumReadings->returnCode != ResultsCalcReturnCode::UnderReportableRange)) ||
                        (calciumReadings->requirementsFailedQC))
                    {
                        calciumAdditionalDrift = NAN;
                    }
                    else
                    {
                        calciumAdditionalDrift = calciumReadings->levels->additionalSlope;
                    }

                    if ((phReadings != nullptr) && (pco2Readings != nullptr))
                    {
                        try
                        {
                            CalculateUncorrectedPCO2(pco2Readings, bubbleDetect, sampleDetect, testMode, topHeaterPower, ageOfCard, ambientTemperature, applyVolumeCorrection, isBlood, calciumAdditionalDrift);
                        }
                        catch (...)
                        {
                            pco2Readings->returnCode = ResultsCalcReturnCode::CannotCalculate;
                            pco2Readings->result = NAN;
                        }

                        try
                        {
                            CalculateUncorrectedpH(phReadings, pco2Readings, bubbleDetect, sampleDetect, testMode, topHeaterPower, ageOfCard, ambientTemperature, isBlood, applyVolumeCorrection, calciumAdditionalDrift);
                        }
                        catch (...)
                        {
                            phReadings->returnCode = ResultsCalcReturnCode::CannotCalculate;
                            phReadings->result = NAN;
                        }

                        // if pco2 has not failed iqc and is not cnc and if ph has not failed iqc and is not cnc,
                        // then do bicarb. over/under rr is ok
                        // in other words, use out of reportable range
                        if (!FailedIQC(pco2Readings->returnCode) &&
                            !FailedIQC(phReadings->returnCode) &&
                            (pco2Readings->returnCode != ResultsCalcReturnCode::CannotCalculate) &&
                            (phReadings->returnCode != ResultsCalcReturnCode::CannotCalculate))
                        {
                            uncorrectedBicarb = ComputeCalculatedUncorrectedActualBicarbonate(pco2Readings->result,
                                                                                              pco2Readings->returnCode,
                                                                                              phReadings->result,
                                                                                              phReadings->returnCode);
                        }
                    }

                    ResultsCalcReturnCode co2Rc = ResultsCalcReturnCode::Success;

                    // everything was fine, so do carbon dioxide
                    for (size_t j = 0; j < testReadings.size(); j++)
                    {
                        if (testReadings[j]->sensorType == Sensors::CarbonDioxide)
                        {
                            co2Rc = CalculateBGESensor(testReadings[j], testReadings, uncorrectedBicarb,
                                                       bubbleDetect, sampleDetect, ambientTemperature, ambientPressure, testMode,
                                                       topHeaterPower, ageOfCard, applyHemodilution, sibVersion,
                                                       isBlood, applyVolumeCorrection, calciumAdditionalDrift, allowNegativeValues);

                            if (PostOrSampleFailedIQC(co2Rc) || EarlySpikeDip(co2Rc) || LateSpikeDip(co2Rc))
                            {
                                sampleFailedQC = true;
                            }
                        }
                    }

                    // this is a change.. we will go ahead and do all the other sensors, even if co2 failed qc
                    // that way they can see them in AT.. afterwards we will cnc everything
                    // so that the effect to the customer will be the same except that instead of everything being
                    // cnc, some of them may be failed iqc
                    // lactate and glucose need oxygen, so we need to do them after
                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        // glu and lact need po2, so do that next
                        if (testReadings[i]->sensorType == Sensors::Oxygen)
                        {
                            ResultsCalcReturnCode rc = CalculateBGESensor(testReadings[i], testReadings, uncorrectedBicarb,
                                                                          bubbleDetect, sampleDetect, ambientTemperature, ambientPressure, testMode,
                                                                          topHeaterPower, ageOfCard, applyHemodilution, sibVersion,
                                                                          isBlood, applyVolumeCorrection, calciumAdditionalDrift, allowNegativeValues);
                        }
                    }

                    // lactate and glucose need oxygen, so we need to do them after oxygen
                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        // glu and lact after po2
                        if (testReadings[i]->sensorType == Sensors::Lactate)
                        {
                            ResultsCalcReturnCode rc = CalculateBGESensor(testReadings[i], testReadings, uncorrectedBicarb,
                                                                          bubbleDetect, sampleDetect, ambientTemperature, ambientPressure, testMode,
                                                                          topHeaterPower, ageOfCard, applyHemodilution, sibVersion,
                                                                          isBlood, applyVolumeCorrection, calciumAdditionalDrift, allowNegativeValues);
                        }
                    }

                    // do them in whatever order.. later we will add them to the values in a certain order
                    // just dont do po2, pco2, glu and lact again.. and dont do hct yet
                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        if ((testReadings[i]->channelType >= ChannelType::P1 && testReadings[i]->channelType <= ChannelType::A4) &&
                            testReadings[i]->sensorType != Sensors::CarbonDioxide &&
                            testReadings[i]->sensorType != Sensors::Lactate &&
                            testReadings[i]->sensorType != Sensors::Glucose &&
                            testReadings[i]->sensorType != Sensors::Oxygen &&
                            testReadings[i]->sensorType != Sensors::pH &&
                            testReadings[i]->sensorType != Sensors::Chloride &&
                            testReadings[i]->sensorType != Sensors::TCO2 &&
                            testReadings[i]->sensorType != Sensors::Urea)
                        {
                            CalculateBGESensor(testReadings[i], testReadings, uncorrectedBicarb,
                                               bubbleDetect, sampleDetect, ambientTemperature, ambientPressure, testMode,
                                               topHeaterPower, ageOfCard, applyHemodilution, sibVersion,
                                               isBlood, applyVolumeCorrection, calciumAdditionalDrift, allowNegativeValues);
                        }
                    }

                    // now do hematocrit, which happens to be conductivity
                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        // use conductivity as hematocrit
                        if (testReadings[i]->sensorType == Sensors::Conductivity)
                        {
                            ResultsCalcReturnCode rc = CalculateBGESensor(testReadings[i], testReadings, uncorrectedBicarb,
                                                                          bubbleDetect, sampleDetect, ambientTemperature, ambientPressure, testMode,
                                                                          topHeaterPower, ageOfCard, applyHemodilution, sibVersion,
                                                                          isBlood, applyVolumeCorrection, calciumAdditionalDrift, allowNegativeValues);

                            // NEW CODE!
                            // if co2 failed sample qc, but hct is under r (qc fluid in blood mode?)
                            if (sampleFailedQC && (rc == ResultsCalcReturnCode::UnderReportableRange))
                                sampleFailedQC = false;
                        }
                    }

                    // lactate and glucose need oxygen, so we need to do them after oxygen
                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        // glu and lact after po2
                        if (testReadings[i]->sensorType == Sensors::Glucose ||
                            testReadings[i]->sensorType == Sensors::Creatinine)
                        {
                            ResultsCalcReturnCode rc = CalculateBGESensor(testReadings[i], testReadings, uncorrectedBicarb,
                                                                          bubbleDetect, sampleDetect, ambientTemperature, ambientPressure, testMode,
                                                                          topHeaterPower, ageOfCard, applyHemodilution, sibVersion,
                                                                          isBlood, applyVolumeCorrection, calciumAdditionalDrift, allowNegativeValues);
                        }
                    }


                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        // cl- after hct
                        if (testReadings[i]->sensorType == Sensors::Gold)
                        {
                            testReadings[i]->sensorType = testReadings[i]->sensorDescriptor->sensorType = Sensors::Urea;

                            ResultsCalcReturnCode rc = CalculateBGESensor(testReadings[i], testReadings, uncorrectedBicarb,
                                                                          bubbleDetect, sampleDetect, ambientTemperature, ambientPressure, testMode,
                                                                          topHeaterPower, ageOfCard, applyHemodilution, sibVersion,
                                                                          isBlood, applyVolumeCorrection, calciumAdditionalDrift, allowNegativeValues);

                            testReadings[i]->sensorType = testReadings[i]->sensorDescriptor->sensorType = Sensors::Gold;
                        }
                    }

                    // NEW es 3.15.1: do cl after hematocrit because we need to look at hct to determine whether hct failed qc or
                    // was recovered using recovery algorithm
                    // NEW es 3.18.0: move chloride after oxygen. chloride needs corrected po2 correction
                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        // cl- after hct
                        if (testReadings[i]->sensorType == Sensors::Chloride)
                        {
                            ResultsCalcReturnCode rc = CalculateBGESensor(testReadings[i], testReadings, uncorrectedBicarb,
                                                                          bubbleDetect, sampleDetect, ambientTemperature, ambientPressure, testMode,
                                                                          topHeaterPower, ageOfCard, applyHemodilution, sibVersion,
                                                                          isBlood, applyVolumeCorrection, calciumAdditionalDrift, allowNegativeValues);
                        }
                    }

                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        // NEW: do ph after hematocrit because we need to look at hct to determine early window ph failure
                        // glu and lact after po2
                        if (testReadings[i]->sensorType == Sensors::pH)
                        {
                            ResultsCalcReturnCode rc = CalculateBGESensor(testReadings[i], testReadings, uncorrectedBicarb,
                                                                          bubbleDetect, sampleDetect, ambientTemperature, ambientPressure, testMode,
                                                                          topHeaterPower, ageOfCard, applyHemodilution, sibVersion,
                                                                          isBlood, applyVolumeCorrection, calciumAdditionalDrift, allowNegativeValues);
                        }
                    }

                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        // TCO2 and Urea at the end.. after ph, because we need bicarb
                        if ((testReadings[i]->sensorType == Sensors::Urea) ||
                            (testReadings[i]->sensorType == Sensors::TCO2))
                        {
                            ResultsCalcReturnCode rc = CalculateBGESensor(testReadings[i], testReadings, uncorrectedBicarb,
                                                                          bubbleDetect, sampleDetect, ambientTemperature, ambientPressure, testMode,
                                                                          topHeaterPower, ageOfCard, applyHemodilution, sibVersion,
                                                                          isBlood, applyVolumeCorrection, calciumAdditionalDrift, allowNegativeValues);
                        }
                    }

                    int numFailedSamplePostLateSpikeDip = 0;

                    // do the rest
                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        // check for the spike/dips for samplefailedqc. this doenst include
                        // hct now. too many suckbacks
                        if (EarlySpikeDipIsSampleDelivery(testReadings[i]->sensorType) &&
                            EarlySpikeDip(testReadings[i]->returnCode))
                        {
                            sampleFailedQC = true;
                        }

                        if (testReadings[i]->sensorType == Sensors::Oxygen)
                        {
                            if (testReadings[i]->returnCode == ResultsCalcReturnCode::SampleDriftQCHigh)
                            {
                                sampleFailedQC = true;
                            }
                        }

                        if (testMode == TestMode::BloodTest)
                        {
                            if ((PostOrSampleFailedIQC(testReadings[i]->returnCode) ||
                                 LateSpikeDip(testReadings[i]->returnCode)))
                            {
                                numFailedSamplePostLateSpikeDip++;

                                if (numFailedSamplePostLateSpikeDip >= 3)
                                {
                                    sampleFailedQC = true;
                                }
                            }
                        }
                    }

                    if (!sampleFailedQC)
                    {
                        if (testMode == TestMode::BloodTest)
                        {
                            // if hematocrit failed iqc.. only do this for blood
                            int numFailed = 0;

                            // do the rest
                            for (size_t i = 0; i < testReadings.size(); i++)
                            {
                                // if it not hematocrit and it failed.. add it to the total
                                // if lactate fails for interferent detected, don't add it.. because glucose
                                // will have also failed and we don't want to treat it as 2 failures
                                if (FailedIQC(testReadings[i]->returnCode))
                                {
                                    numFailed++;
                                }
                            }

                            if (numFailed >= 3)
                            {
                                SetAllNotFailedQCToCNC(testReadings);
                            }
                        }
                    }

                    // if hct is return code 11 or 12, sample delivery the whole card
                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        if (testReadings[i]->sensorType == Sensors::Conductivity)
                        {
                            if (testReadings[i]->returnCode == ResultsCalcReturnCode::SampleMeanQCHigh ||
                                (testReadings[i]->returnCode == ResultsCalcReturnCode::SampleDriftQCLow) ||
                                (testReadings[i]->returnCode == ResultsCalcReturnCode::SampleDriftQCHigh))
                            {
                                sampleFailedQC = true;
                            }

                            break;
                        }
                    }

                    if (!sampleFailedQC && !isnan(bubbleWidth))
                    {
                        sampleFailedQC = DetermineHctShortSampleDelivery(testReadings, bubbleWidth, bubbleDetect, sampleDetect);
                    }

                    if (!sampleFailedQC)
                    {
                        sampleFailedQC = DetermineCalculatedResultsSampleDelivery(testReadings,
                                                                                  anionInsanityLow,
                                                                                  anionInsanityHigh,
                                                                                  bicarbInsanityLow,
                                                                                  bicarbInsanityHigh,
                                                                                  applymTCO2);
                    }

                    int po2Readings = -1;
                    int hctReadings = -1;
                    int lactReadings = -1;

                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        if (testReadings[i]->sensorType == Sensors::Oxygen)
                        {
                            po2Readings = i;
                        }

                        if (testReadings[i]->sensorType == Sensors::Conductivity)
                        {
                            hctReadings = i;
                        }

                        if (testReadings[i]->sensorType == Sensors::Lactate)
                        {
                            lactReadings = i;
                        }
                    }

                    if (po2Readings != -1)
                    {
                        for (size_t i = 0; i < testReadings.size(); i++)
                        {
                            if (testReadings[i]->sensorType == Sensors::Creatinine)
                            {
                                testReadings[i]->levels->output10 = testReadings[po2Readings]->levels->calMean;
                            }

                            if ((testReadings[i]->sensorType == Sensors::Chloride) ||
                                (testReadings[i]->sensorType == Sensors::Oxygen) ||
                                (testReadings[i]->sensorType == Sensors::Calcium))
                            {
                                testReadings[i]->levels->output7 = testReadings[po2Readings]->levels->output1;
                            }
                        }
                    }

                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        if (pco2Readings != nullptr)
                        {
                            testReadings[i]->levels->output1 = pco2Readings->levels->response;
                            testReadings[i]->levels->output2 = pco2Readings->levels->calSlope;
                            testReadings[i]->levels->output6 = pco2Readings->levels->output6;
                            testReadings[i]->levels->output4 = pco2Readings->levels->output4;

                            if ((testReadings[i]->sensorType != Sensors::Oxygen) ||
                                (testReadings[i]->sensorType != Sensors::Glucose) ||
                                (testReadings[i]->sensorType != Sensors::Lactate) ||
                                (testReadings[i]->sensorType != Sensors::Creatinine))
                            {
                                if (!IsBadValue(pco2Readings->result))
                                {
                                    testReadings[i]->levels->output17 = CSharpDecimalCalculation(pco2Readings->result);
                                }
                            }
                        }

                        if (phReadings != nullptr)
                        {
                            testReadings[i]->levels->output3 = phReadings->levels->output3;
                            testReadings[i]->levels->output5 = phReadings->levels->output5;
                        }

                        if ((po2Readings != -1) && (!IsBadValue(testReadings[po2Readings]->result)) &&
                            (testReadings[i]->sensorType != Sensors::Oxygen) &&
                            (testReadings[i]->sensorType != Sensors::Chloride) &&
                            (testReadings[i]->sensorType != Sensors::Calcium))
                        {
                            testReadings[i]->levels->output7 = CSharpDecimalCalculation(testReadings[po2Readings]->result);
                        }

                        if (hctReadings != -1)
                        {
                            if ((testReadings[i]->sensorType == Sensors::Oxygen) ||
                                (testReadings[i]->sensorType == Sensors::Glucose) ||
                                (testReadings[i]->sensorType == Sensors::Creatinine))
                            {
                                if (!IsBadValue(testReadings[hctReadings]->result))
                                {
                                    testReadings[i]->levels->output8 = CSharpDecimalCalculation(testReadings[hctReadings]->result);
                                }
                            }
                            else
                            {
                                testReadings[i]->levels->output8 = testReadings[hctReadings]->levels->output8;
                            }
                        }

                        if ((lactReadings != -1) && (!IsBadValue(testReadings[lactReadings]->result)) &&
                            (testReadings[i]->sensorType != Sensors::Creatinine))
                        {
                            testReadings[i]->levels->output10 = CSharpDecimalCalculation(testReadings[lactReadings]->result);
                        }

                        if (testReadings[i]->sensorType != Sensors::Creatinine)
                        {
                            if (!isnan(calciumAdditionalDrift))
                            {
                                testReadings[i]->levels->output9 = CSharpDecimalCalculation(calciumAdditionalDrift);
                            }

                            testReadings[i]->levels->output16 = calciumReadings->levels->output16;
                        }
                    }

                    if (unSmoothedBun != nullptr)
                    {
                        RestoreBUNReadings(testReadings[locationOfBun], unSmoothedBun);
                    }
                    if (unSmoothedGold != nullptr)
                    {
                        RestoreBUNReadings(testReadings[locationOfGold], unSmoothedGold);
                    }

                    if (!allowNegativeValues && testMode == TestMode::QA)
                    {
                        for (size_t i = 0; i < testReadings.size(); i++)
                        {
                            if ((testReadings[i]->sensorType == Sensors::CarbonDioxide) ||
                                (testReadings[i]->sensorType == Sensors::TCO2))
                            {
                                if (testReadings[i]->returnCode == ResultsCalcReturnCode::Success)
                                {
                                    if (testReadings[i]->result < 0)
                                    {
                                        testReadings[i]->result = 0;
                                    }
                                }
                            }
                        }
                    }

                    // add the indicator that tells the calling object that a sample qc failure happened
                    // a sample qc failure is co2 or hct qc failure during sample
                    parameters.AddParam(sampleFailedQC);
                }

                void AnalyticalManager::SmoothBUNReadings(std::shared_ptr<SensorReadings> sensorReadings, std::shared_ptr<std::vector<Reading>> originalBUNReadings)
                {
                    int numberOfPointsToEitherSide = (int)sensorReadings->sensorDescriptor->param89;

                    // if not at least 1 point with enough readings to either side
                    if (sensorReadings->readings->size() < (numberOfPointsToEitherSide + numberOfPointsToEitherSide + 1))
                    {
                        return;
                    }

                    // start and end so that points to the left and right are guaranteed to be there
                    for (size_t i = numberOfPointsToEitherSide; i < sensorReadings->readings->size() - numberOfPointsToEitherSide; i++)
                    {
                        (*(sensorReadings->readings))[i].Value = 0;

                        // add up number of points before, that point, and number of points after. e.g. 2 points. start at 2. go from 0 to < 2 + 2 + 1 = 5. giving 5 points, 0, 1, 2, 3, 4.
                        for (size_t j = i - numberOfPointsToEitherSide; j < i + numberOfPointsToEitherSide + 1; j++)
                        {
                            CSharpOutOfRange(j, originalBUNReadings->size());

                            // add the value
                            (*(sensorReadings->readings))[i].Value += (*originalBUNReadings)[j].Value;
                        }

                        // divide by number of points to get average
                        (*(sensorReadings->readings))[i].Value /= numberOfPointsToEitherSide + numberOfPointsToEitherSide + 1;
                    }
                }

                void AnalyticalManager::RestoreBUNReadings(std::shared_ptr<SensorReadings> sensorReadings, std::shared_ptr<std::vector<Reading>> originalBUNReadings)
                {
                    for (size_t i = 0; i < sensorReadings->readings->size(); i++)
                    {
                        (*(sensorReadings->readings))[i].Value = (*originalBUNReadings)[i].Value;
                    }
                }

                bool AnalyticalManager::DetermineCalculatedResultsSampleDelivery(std::vector<std::shared_ptr<SensorReadings>> &testReadings,
                                                                                 double anionInsanityLow,
                                                                                 double anionInsanityHigh,
                                                                                 double bicarbInsanityLow,
                                                                                 double bicarbInsanityHigh,
                                                                                 bool applymTCO2)
                {
                    double sodiumValue = NAN;
                    double pco2Value = NAN;
                    double phValue = NAN;
                    double chlorideValue = NAN;
                    double tco2Value = NAN;
                    bool anyBicarbIQCFailures = false;
                    bool anyAGapIQCFailures = false;

                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        if (testReadings[i]->sensorType == Sensors::Sodium)
                        {
                            if (FailedIQC(testReadings[i]->returnCode) || testReadings[i]->requirementsFailedQC)
                            {
                                anyAGapIQCFailures = true;
                            }
                            else
                            {
                                sodiumValue = testReadings[i]->result;
                            }
                        }
                        else if (testReadings[i]->sensorType == Sensors::CarbonDioxide)
                        {
                            if (FailedIQC(testReadings[i]->returnCode) || testReadings[i]->requirementsFailedQC)
                            {
                                anyBicarbIQCFailures = true;
                            }
                            else
                            {
                                pco2Value = testReadings[i]->result;
                            }
                        }
                        else if (testReadings[i]->sensorType == Sensors::pH)
                        {
                            if (FailedIQC(testReadings[i]->returnCode) || testReadings[i]->requirementsFailedQC)
                            {
                                anyBicarbIQCFailures = true;
                            }
                            else
                            {
                                phValue = testReadings[i]->result;
                            }
                        }
                        else if (testReadings[i]->sensorType == Sensors::Chloride)
                        {
                            if (FailedIQC(testReadings[i]->returnCode) || testReadings[i]->requirementsFailedQC)
                            {
                                anyAGapIQCFailures = true;
                            }
                            else
                            {
                                chlorideValue = testReadings[i]->result;
                            }
                        }
                        else if (testReadings[i]->sensorType == Sensors::TCO2)
                        {
                            if (FailedIQC(testReadings[i]->returnCode) || testReadings[i]->requirementsFailedQC)
                            {
                                anyAGapIQCFailures = true;
                            }
                            else
                            {
                                tco2Value = testReadings[i]->result;
                            }
                        }
                    }

                    double correctedBicarb = NAN;

                    // if all the values exist
                    if (!anyBicarbIQCFailures &&
                        !isnan(pco2Value) &&
                        !isnan(phValue))
                    {
                        // get corrected bicarb and check against insanity ranges
                        correctedBicarb = ComputeCalculatedActualBicarbonateGivenValues(pco2Value, phValue);

                        if ((correctedBicarb < bicarbInsanityLow) || (correctedBicarb > bicarbInsanityHigh))
                        {
                            return true;
                        }
                    }

                    if (!anyAGapIQCFailures &&
                        !isnan(sodiumValue) &&
                        !isnan(chlorideValue) &&
                        !isnan(correctedBicarb) &&
                        !isnan(tco2Value))
                    {
                        double anionGap = ComputeAnionGapGivenValues(sodiumValue, chlorideValue, correctedBicarb, tco2Value, applymTCO2);

                        if ((anionGap < anionInsanityLow) || (anionGap > anionInsanityHigh))
                        {
                            return true;
                        }
                    }

                    return false;
                }

                bool AnalyticalManager::EarlySpikeDipIsSampleDelivery(Sensors sensor)
                {
                    if (sensor == Sensors::Oxygen ||
                        sensor == Sensors::CarbonDioxide ||
                        sensor == Sensors::pH)
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }

                void AnalyticalManager::SetAllNotFailedQCToCNC(std::vector<std::shared_ptr<SensorReadings>> &testReadings)
                {
                    // since we are cnc'ing because of an iqc failure, we need to say that these cnc's are because
                    // the requirements failed qc
                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        // use conductivity as hematocrit
                        if (!FailedIQC(testReadings[i]->returnCode))
                        {
                            (testReadings[i])->returnCode = ResultsCalcReturnCode::CannotCalculate;
                            (testReadings[i])->requirementsFailedQC = true; ;
                        }
                    }
                }

                bool AnalyticalManager::EarlySpikeDip(ResultsCalcReturnCode rc)
                {
                    if ((rc == ResultsCalcReturnCode::EarlyDipInSample) ||
                        (rc == ResultsCalcReturnCode::EarlySpikeInSample))
                    {
                        return true;
                    }

                    return false;
                }

                bool AnalyticalManager::LateSpikeDip(ResultsCalcReturnCode rc)
                {
                    if ((rc == ResultsCalcReturnCode::LateDipInSample) ||
                        (rc == ResultsCalcReturnCode::LateSpikeInSample))
                    {
                        return true;
                    }

                    return false;
                }

                bool AnalyticalManager::SpikeDipFailed(ResultsCalcReturnCode rc)
                {
                    if ((rc == ResultsCalcReturnCode::EarlyDipInSample) ||
                        (rc == ResultsCalcReturnCode::EarlySpikeInSample) ||
                        (rc == ResultsCalcReturnCode::LateDipInSample) ||
                        (rc == ResultsCalcReturnCode::LateSpikeInSample) ||
                        (rc == ResultsCalcReturnCode::SpikeInSample) ||
                        (rc == ResultsCalcReturnCode::DipInSample))
                    {
                        return true;
                    }

                    return false;
                }

                bool AnalyticalManager::CalFailedIQC(ResultsCalcReturnCode rc)
                {
                    if ((rc == ResultsCalcReturnCode::CalDriftQCHigh) ||
                        (rc == ResultsCalcReturnCode::CalDriftQCLow) ||
                        (rc == ResultsCalcReturnCode::CalMeanQCHigh) ||
                        (rc == ResultsCalcReturnCode::CalMeanQCLow) ||
                        (rc == ResultsCalcReturnCode::CalNoiseQCHigh) ||
                        (rc == ResultsCalcReturnCode::CalSecondQCHigh) ||
                        (rc == ResultsCalcReturnCode::CalSecondQCLow) ||
                        (rc == ResultsCalcReturnCode::FailedQCEver))
                    {
                        return true;
                    }

                    return false;
                }

                bool AnalyticalManager::PostOrSampleFailedIQC(ResultsCalcReturnCode rc)
                {
                    if ((rc == ResultsCalcReturnCode::SampleDriftQCHigh) ||
                        (rc == ResultsCalcReturnCode::SampleDriftQCLow) ||
                        (rc == ResultsCalcReturnCode::SampleMeanQCHigh) ||
                        (rc == ResultsCalcReturnCode::SampleMeanQCLow) ||
                        (rc == ResultsCalcReturnCode::SampleNoiseQCHigh) ||
                        (rc == ResultsCalcReturnCode::SampleSecondQCHigh) ||
                        (rc == ResultsCalcReturnCode::SampleSecondQCLow) ||
                        (rc == ResultsCalcReturnCode::SampleWindowNoise) ||
                        (rc == ResultsCalcReturnCode::DeltaDriftLow) ||
                        (rc == ResultsCalcReturnCode::DeltraDriftHigh) ||
                        (rc == ResultsCalcReturnCode::PostDriftQCHigh) ||
                        (rc == ResultsCalcReturnCode::PostDriftQCLow) ||
                        (rc == ResultsCalcReturnCode::PostMeanQCHigh) ||
                        (rc == ResultsCalcReturnCode::PostMeanQCLow) ||
                        (rc == ResultsCalcReturnCode::PostNoiseQCHigh) ||
                        (rc == ResultsCalcReturnCode::PostSecondQCHigh) ||
                        (rc == ResultsCalcReturnCode::PostSecondQCLow) ||
                        (rc == ResultsCalcReturnCode::PostNoiseQCHigh) ||
                        (rc == ResultsCalcReturnCode::PO2Bubble) ||
                        (rc == ResultsCalcReturnCode::EarlyWindowHigh) ||
                        (rc == ResultsCalcReturnCode::EarlyWindowLow))
                    {
                        return true;
                    }

                    return false;
                }

                bool AnalyticalManager::OutOfReportable(ResultsCalcReturnCode rc)
                {
                    if ((rc == ResultsCalcReturnCode::UnderReportableRange) ||
                        (rc == ResultsCalcReturnCode::OverReportableRange))
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }

                bool AnalyticalManager::CanBeUsedAsInput(ResultsCalcReturnCode rc)
                {
                    if (OutOfReportable(rc) || FailedIQC(rc))
                    {
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }

                bool AnalyticalManager::CanBeMeasuredAsInput(std::shared_ptr<SensorReadings> reading)
                {
                    if (reading == nullptr)
                    {
                        return false;
                    }
                    else if (FailedIQC(reading->returnCode) || reading->returnCode == ResultsCalcReturnCode::CannotCalculate || reading->requirementsFailedQC || isnan(reading->result))
                    {
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }

                bool AnalyticalManager::FailedIQC(ResultsCalcReturnCode rc)
                {
                    if (((rc >= ResultsCalcReturnCode::FailedQCStart) && (rc <= ResultsCalcReturnCode::FailedQCLast)) ||
                        (rc == ResultsCalcReturnCode::DipInSample) ||
                        (rc == ResultsCalcReturnCode::SpikeInSample) ||
                        (rc == ResultsCalcReturnCode::EarlyDipInSample) ||
                        (rc == ResultsCalcReturnCode::EarlySpikeInSample) ||
                        (rc == ResultsCalcReturnCode::LateDipInSample) ||
                        (rc == ResultsCalcReturnCode::LateSpikeInSample) ||
                        (rc == ResultsCalcReturnCode::OverInsanityRange) ||
                        (rc == ResultsCalcReturnCode::UnderInsanityRange) ||
                        (rc == ResultsCalcReturnCode::SampleWindowNoise) ||
                        (rc == ResultsCalcReturnCode::DeltraDriftHigh) ||
                        (rc == ResultsCalcReturnCode::DeltaMeanPostSample) ||
                        (rc == ResultsCalcReturnCode::InterferentDetected) ||
                        (rc == ResultsCalcReturnCode::SWPeakDriftLow) ||
                        (rc == ResultsCalcReturnCode::SWPeakDriftHigh) ||
                        (rc == ResultsCalcReturnCode::SWPeakNoiseHigh) ||
                        (rc == ResultsCalcReturnCode::PO2Bubble) ||
                        (rc == ResultsCalcReturnCode::EarlyWindowLow) ||
                        (rc == ResultsCalcReturnCode::EarlyWindowHigh) ||
                        (rc == ResultsCalcReturnCode::CreaEarlyWindowLow) ||
                        (rc == ResultsCalcReturnCode::CreaEarlyWindowHigh) ||
                        (rc == ResultsCalcReturnCode::CreaDriftFailure) ||
                        (rc == ResultsCalcReturnCode::HematocritShortSample) ||
                        (rc == ResultsCalcReturnCode::FailedQCEver) ||
                        (rc == ResultsCalcReturnCode::CalWindowDip) ||
                        (rc == ResultsCalcReturnCode::SampleDipEarly) ||
                        (rc == ResultsCalcReturnCode::SampleDipLate) ||
                        (rc == ResultsCalcReturnCode::SampleWindowAllPointCheck) ||
                        (rc == ResultsCalcReturnCode::CreaSampleBubble))
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }

                bool AnalyticalManager::DetermineHctShortSampleDelivery(std::vector<std::shared_ptr<SensorReadings>> &testReadings,
                                                                        double bubbleWidth,
                                                                        double bubbleDetect,
                                                                        double sampleDetect)
                {
                    std::shared_ptr<SensorReadings> hctReadings = nullptr;

                    for (size_t i = 0; i < testReadings.size(); i++)
                    {
                        if (testReadings[i]->sensorType == Sensors::Conductivity)
                        {
                            hctReadings = testReadings[i];
                            break;
                        }
                    }

                    if (hctReadings == nullptr)
                    {
                        return false;
                    }

                    double fullBubWidth = hctReadings->sensorDescriptor->param22;
                    int fullBubPoints = (int)hctReadings->sensorDescriptor->param23;
                    double fullBubThreshold = hctReadings->sensorDescriptor->param24;

                    int numPointsOver = 0;

                    if (bubbleWidth <= fullBubWidth)
                    {
                        return false;
                    }

                    // for all points between bubble and sample detect inclusive, check if over the threshold
                    // if over the threshold, increment and check whether we reached the threshold of points over.
                    // if we have, it's done and there is no sample delivery failure
                    for (size_t i = 0; i < hctReadings->readings->size(); i++)
                    {
                        if (((*(hctReadings->readings))[i].Time >= bubbleDetect) &&
                            ((*(hctReadings->readings))[i].Time <= sampleDetect))
                        {
                            if ((*(hctReadings->readings))[i].Value > fullBubThreshold)
                            {
                                numPointsOver++;

                                // reached threshold. not a sample delivery failure
                                if (numPointsOver >= fullBubPoints)
                                {
                                    return false;
                                }
                            }
                        }
                    }

                    // did not reach number of points over the threshold. this is a sample delivery failure
                    hctReadings->returnCode = ResultsCalcReturnCode::HematocritShortSample;
                    return true;
                }

                ResultsCalcReturnCode AnalyticalManager::CalculatePCO2(std::shared_ptr<SensorReadings> carbonDioxideReadings,
                                                                       double bubbleDetect,
                                                                       double sampleDetect,
                                                                       double actualBicarb,
                                                                       TestMode testMode,
                                                                       std::shared_ptr<SensorReadings> topHeaterReadings,
                                                                       int ageOfCard,
                                                                       double ambientTemperature,
                                                                       bool applyVolumeCorrections,
                                                                       bool isBlood,
                                                                       double calciumAdditionalDrift)
                {
                    ResultsCalcReturnCode rc;
                    std::shared_ptr<Levels> sensorLevels = carbonDioxideReadings->levels;
                    auto tempSensorInfo = std::make_shared<SensorInfo>();
                    auto tempLevels = std::make_shared<Levels>();

                    // we don't want to change the original sensor info.. so copy it over
                    auto sensorInfo = std::make_shared<SensorInfo>();
                    CopySensorInfo(sensorInfo, carbonDioxideReadings->sensorDescriptor);

                    auto ageCorrections = std::make_shared<AgeCorrectionsQABlood>();
                    double doubleSlopeFactor;
                    double calConcentration;
                    double offset;

                    // NOTE: We'll use the Blood Test params (65-68) if the bit for drift term corrected
                    //       in PCO2 is _not_set_ or if this is actually a Blood Test
                    bool useBloodTestParam = ((!DriftTermCorrectedPCO2(sensorInfo->param15)) || isBlood);

                    double LoConcCut = useBloodTestParam ? sensorInfo->param65 : sensorInfo->param96;
                    double LoConcCorr = useBloodTestParam ? sensorInfo->param66 : sensorInfo->param97;
                    double LoAgePivot = useBloodTestParam ? sensorInfo->param67 : sensorInfo->param98;
                    double LoConcOFF = useBloodTestParam ? sensorInfo->param68 : sensorInfo->param99;

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
                    rc = CalculateEx(carbonDioxideReadings->readings, tempSensorInfo, bubbleDetect, sampleDetect, tempLevels, true,
                                     testMode, true, topHeaterReadings, ageOfCard, ambientTemperature, false, false, false, 0, sensorInfo->param20,
                                     sensorInfo->param21, testMode, false, 0.0, false, nullptr, isBlood);

                    sensorLevels->peakEx = tempLevels->calEx;
                    sensorLevels->peakMean = tempLevels->sampleMean;
                    sensorLevels->peakSlope = tempLevels->sampleSlope;
                    sensorLevels->peakNoise = tempLevels->sampleNoise;
                    sensorLevels->peakSecond = tempLevels->sampleSecond;

                    if (testMode == TestMode::QA)
                    {
                        // if hematocrit is under 5, we will assume its an aqueous solution and use the modified sample window
                        sensorInfo->sampleDelimit = carbonDioxideReadings->sensorDescriptor->param3;
                        sensorInfo->extrapolation = carbonDioxideReadings->sensorDescriptor->param4;
                        sensorInfo->sampleWindowSize = carbonDioxideReadings->sensorDescriptor->param5;

                        sensorInfo->postDelimit = carbonDioxideReadings->sensorDescriptor->param6;
                        sensorInfo->tMinus = carbonDioxideReadings->sensorDescriptor->param7;
                        sensorInfo->postWindowSize = carbonDioxideReadings->sensorDescriptor->param8;

                        if (carbonDioxideReadings->sensorDescriptor->param19 != 0)
                        {
                            sensorInfo->DeltaDriftLowQC = carbonDioxideReadings->sensorDescriptor->param19;
                        }
                    }

                    rc = CalculateEx(carbonDioxideReadings->readings, sensorInfo, bubbleDetect, sampleDetect, sensorLevels, true,
                                     testMode, true, topHeaterReadings, ageOfCard, ambientTemperature, true, false, false, 0,
                                     sensorInfo->param20, sensorInfo->param21, testMode, false, 0.0, false, ageCorrections, isBlood);

                    // this is the error that can get returned that we cant recover from..
                    // ...that there werent enough data points to calculate.
                    // if its just a qc failure.. continue.. but flag it as a qc failure as we
                    // leave
                    if (rc == ResultsCalcReturnCode::CannotCalculate)
                    {
                        // dont overwrite the old return code
                        if (carbonDioxideReadings->returnCode == ResultsCalcReturnCode::Success)
                        {
                            carbonDioxideReadings->result = NAN;
                            carbonDioxideReadings->returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                    }
                    else
                    {
                        double co2Offset = sensorInfo->param1;
                        double bicarbCorrect = sensorInfo->param2;
                        double co2Slope = 0.0;
                        double earlyDriftLimit = sensorInfo->param13;
                        double earlySlopeFactor = sensorInfo->param14;
                        double lateSlopeFactor = sensorInfo->param17;
                        double lateInjectionTimeOffset = sensorInfo->param18;

                        double slopeFactor = doubleSlopeFactor;

                        if (!isnan(calciumAdditionalDrift) && (isBlood || (!AWDriftCorrectedPCO2(tempSensorInfo->param15) && !isBlood)))
                        {
                            slopeFactor = slopeFactor + sensorInfo->param22 * (sensorInfo->param23 + calciumAdditionalDrift);
                        }

                        if (pCO2newSlope(sensorInfo->param15))
                        {
                            if ((bubbleDetect < sensorInfo->InjectionTimeOffset) && (sensorLevels->calSlope > earlyDriftLimit))
                            {
                                sensorLevels->output9 = 0;
                                co2Slope = (slopeFactor * FullNernst()) *
                                           (1.0 - (earlySlopeFactor *
                                                   (bubbleDetect - sensorInfo->InjectionTimeOffset)));
                            }
                            else if (bubbleDetect > lateInjectionTimeOffset)
                            {
                                sensorLevels->output9 = 1;
                                co2Slope = (slopeFactor * FullNernst()) *
                                           (1.0 - (lateSlopeFactor *
                                                   (bubbleDetect - lateInjectionTimeOffset)));
                            }
                            else
                            {
                                sensorLevels->output9 = 2;
                                co2Slope = slopeFactor * FullNernst();
                            }
                        }
                        else
                        {
                            sensorLevels->output9 = 3;
                            // this is what it was before..
                            co2Slope = slopeFactor * FullNernst();
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
                                ResultsCalcReturnCode rc2 = CalculateEx(carbonDioxideReadings->readings, tempSensorInfo2, bubbleDetect, sampleDetect, tempLevels2, true,
                                                                        testMode, true, topHeaterReadings, ageOfCard, ambientTemperature, true, false, false, 0,
                                                                        tempSensorInfo2->param20, tempSensorInfo2->param21, testMode, false, 0.0, false, ageCorrections, isBlood);

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

                        carbonDioxideReadings->result = CSharpDecimalCalculation(((calConcentration + co2Offset) *
                                                          std::pow(10, CSharpDecimalCalculation((sensorLevels->response - offset) /
                                                                        co2Slope))) - co2Offset);

                        if (!isnan(actualBicarb))
                        {
                            carbonDioxideReadings->result = CSharpDecimalCalculation((carbonDioxideReadings->result) *
                                                             (1.0 +
                                                              (bicarbCorrect *
                                                               ((actualBicarb) - 25))));
                        }

                        if (isBlood && applyVolumeCorrections && (carbonDioxideReadings->result >= sensorInfo->param46))
                        {
                            carbonDioxideReadings->result = carbonDioxideReadings->result + sensorInfo->param45 * (carbonDioxideReadings->result - sensorInfo->param46);
                        }

                        if (!isBlood && (carbonDioxideReadings->result < LoConcCut))
                        {
                            carbonDioxideReadings->result = carbonDioxideReadings->result + LoConcCorr * (LoAgePivot - ageOfCard) * (LoAgePivot - ageOfCard) + LoConcOFF;
                        }

                        double postSampleMeanLimit;

                        if ((testMode == TestMode::QA) && (sensorInfo->param16 != 0))
                        {
                            postSampleMeanLimit = sensorInfo->param16;
                        }
                        else
                        {
                            postSampleMeanLimit = sensorInfo->param12;
                        }

                        if ((std::abs(sensorLevels->postMean - sensorLevels->sampleMean) > postSampleMeanLimit) &&
                            (rc == ResultsCalcReturnCode::Success) && PostMeanMinusSampleMean(sensorInfo->param15))
                        {
                            carbonDioxideReadings->returnCode = rc = ResultsCalcReturnCode::DeltaMeanPostSample;
                        }

                        if (rc == ResultsCalcReturnCode::Success)
                        {
                            carbonDioxideReadings->returnCode = rc = ValidateInsanityRangeOnlyReturnCode(carbonDioxideReadings->result,
                                                                                                         testMode == TestMode::BloodTest ? carbonDioxideReadings->insanityLow : carbonDioxideReadings->insanityQALow,
                                                                                                         testMode == TestMode::BloodTest ? carbonDioxideReadings->insanityHigh : carbonDioxideReadings->insanityQAHigh);
                        }

                        // turn co2 return code to failed qc ever if realtime qc failed during the test and there were no other failures
                        if ((rc == ResultsCalcReturnCode::Success) && (carbonDioxideReadings->realTimeQCFailedEver == RealTimeQCReturnCode::Failed))
                        {
                            carbonDioxideReadings->returnCode = rc = ResultsCalcReturnCode::FailedQCEver;
                        }

                        if ((rc == ResultsCalcReturnCode::Success) && (testMode == TestMode::BloodTest))
                        {
                            carbonDioxideReadings->returnCode = ValidateReportableRangeOnlyReturnCode(carbonDioxideReadings->result, carbonDioxideReadings->reportableLow, carbonDioxideReadings->reportableHigh);
                        }
                        else
                        {
                            carbonDioxideReadings->returnCode = rc;
                        }
                    }

                    return carbonDioxideReadings->returnCode;
                }

                double AnalyticalManager::CalculateDriftTermCorrectedPCO2(std::shared_ptr<SensorInfo> sensorInfo, std::shared_ptr<Levels> levels, std::shared_ptr<SensorReadings> topHeaterReadings, bool isBlood)
                {
                    if ((sensorInfo->sensorType != Sensors::CarbonDioxide) ||
                        (!DriftTermCorrectedPCO2(sensorInfo->param15)))
                        return 0.0;

                    double driftTermPivot = isBlood ? sensorInfo->param69 : sensorInfo->param81;

                    double driftCorrLo = isBlood ? sensorInfo->param70 : sensorInfo->param82;
                    double driftCorrMid = isBlood ? sensorInfo->param71 : sensorInfo->param83;
                    double driftCorrHi = isBlood ? sensorInfo->param72 : sensorInfo->param84;

                    double respCutLo = isBlood ? sensorInfo->param73 : sensorInfo->param85;
                    double respCutMid = isBlood ? sensorInfo->param74 : sensorInfo->param86;
                    double respCutHi = isBlood ? sensorInfo->param75 : sensorInfo->param87;

                    double respPivotLo = isBlood ? sensorInfo->param76 : sensorInfo->param88;
                    double respPivotMid = isBlood ? sensorInfo->param77 : sensorInfo->param89;
                    double respPivotHi1 = isBlood ? sensorInfo->param78 : sensorInfo->param90;
                    double respPivotHi2 = isBlood ? sensorInfo->param79 : sensorInfo->param91;
                    double respPivotHi3 = isBlood ? sensorInfo->param80 : sensorInfo->param92;

                    double thvdUpper = sensorInfo->param93;
                    double thvdLower = sensorInfo->param94;
                    double thvdNominal = sensorInfo->param95;

                    double driftTermCorr = 0.0;

                    double topHeaterVoltageDelta = topHeaterReadings->levels->sampleMean - topHeaterReadings->levels->calMean;

                    double driftCW_pCO2 = levels->calSlope;
                    double driftPW_pCO2 = levels->postSlope;

                    //1. RspRawPCO2 = SmpExPCO2 - CalExPCO2;
                    double smpExPCO2 = levels->sampleEx;
                    double calExPCO2 = levels->calEx;
                    double rspRawPCO2 = smpExPCO2 - calExPCO2;

                    //2. If TopHeaterVoltageDelta > THVDUpper OR TopHeaterVoltageDelta < THVDLower then
                    //      TopHeaterVoltageDelta = TopHeaterVoltageDeltaNominal;
                    if ((topHeaterVoltageDelta > thvdUpper) || (topHeaterVoltageDelta < thvdLower))
                        topHeaterVoltageDelta = thvdNominal;

                    //3. DriftTerm = DriftCW,pCO2 * DriftPW,pCO2 * TopHeaterVoltageDelta;
                    double driftTerm = driftCW_pCO2 * driftPW_pCO2 * topHeaterVoltageDelta;

                    //4. If  RspRawPCO2 <= RespCutLo AND DriftTerm >= DriftTermPivot then
                    //      DriftTermCorr = DriftCorrLo * (DriftTerm - DriftTermPivot) * (RspRawPCO2 - RespPivotLo);
                    if ((rspRawPCO2 <= respCutLo) && (driftTerm >= driftTermPivot))
                        driftTermCorr = driftCorrLo * (driftTerm - driftTermPivot) * (rspRawPCO2 - respPivotLo);

                    //5. If  RspRawPCO2 > RespCutLo AND DriftTerm > DriftTermPivot then
                    if ((rspRawPCO2 > respCutLo) && (driftTerm > driftTermPivot))
                    {
                        //5a. If RspRawPCO2 <= RespCutMid then
                        //        DriftTermCorr = DriftCorrMid * (DriftTerm - DriftTermPivot) * (RespPivotMid - RspRawPCO2);
                        if (rspRawPCO2 <= respCutMid)
                            driftTermCorr = driftCorrMid * (driftTerm - driftTermPivot) * (respPivotMid - rspRawPCO2);

                        //5b. If RspRawPCO2 > RespCutMid AND RspRawPCO2 < RespCutHi then
                        //        DriftTermCorr = DriftCorrHi * (DriftTerm - DriftTermPivot) * ((RespPivotHi1*RspRawPCO2 + RespPivotHi2) - RspRawPCO2);
                        else if ((rspRawPCO2 > respCutMid) && (rspRawPCO2 < respCutHi))
                            driftTermCorr = driftCorrHi * (driftTerm - driftTermPivot) * ((respPivotHi1 * rspRawPCO2 + respPivotHi2) - rspRawPCO2);

                        //5c. If RspRawPCO2 >= RespCutHi then
                        //        DriftTermCorr = DriftCorrHi * (DriftTerm - DriftTermPivot) * (RespPivotHi3 - RspRawPCO2);
                        else if (rspRawPCO2 >= respCutHi)
                            driftTermCorr = driftCorrHi * (driftTerm - driftTermPivot) * (respPivotHi3 - rspRawPCO2);
                    }

                    return driftTermCorr;
                }

                ResultsCalcReturnCode AnalyticalManager::CalculateUncorrectedPCO2(std::shared_ptr<SensorReadings> carbonDioxideReadings,
                                                                                  double bubbleDetect,
                                                                                  double sampleDetect,
                                                                                  TestMode testMode,
                                                                                  std::shared_ptr<SensorReadings> topHeaterReadings,
                                                                                  int ageOfCard,
                                                                                  double ambientTemperature,
                                                                                  bool applyVolumeCorrections,
                                                                                  bool isBlood,
                                                                                  double calciumAdditionalDrift)
                {
                    ResultsCalcReturnCode rc;

                    // we dont want to change the original sensor info.. so copy it over
                    auto tempSensorInfo = std::make_shared<SensorInfo>();
                    std::shared_ptr<Levels> sensorLevels = carbonDioxideReadings->levels;
                    CopySensorInfo(tempSensorInfo, carbonDioxideReadings->sensorDescriptor);

                    auto ageCorrections = std::make_shared<AgeCorrectionsQABlood>();
                    double doubleSlopeFactor;
                    double calConcentration;
                    double offset;

                    if (isBlood || !PotsAndGluLacAqBlood(tempSensorInfo->param15))
                    {
                        doubleSlopeFactor = tempSensorInfo->slopeFactor;
                        offset = tempSensorInfo->offset;
                        calConcentration = tempSensorInfo->calConcentration;

                        ageCorrections->BSensor = tempSensorInfo->B;
                        ageCorrections->B2Sensor = tempSensorInfo->param29;
                        ageCorrections->B3Sensor = tempSensorInfo->param31;
                        ageCorrections->MvCut = tempSensorInfo->param33;
                        ageCorrections->AgeCut = tempSensorInfo->param35;
                        ageCorrections->FSensor = tempSensorInfo->F;

                        ageCorrections->LowMvInjTimeCut = tempSensorInfo->param57;
                        ageCorrections->FPrimeSensor = tempSensorInfo->param58;
                        ageCorrections->HighMvInjTimeCut = tempSensorInfo->param59;
                        ageCorrections->FDoublePrimeSensor = tempSensorInfo->param60;
                    }
                    else
                    {
                        doubleSlopeFactor = tempSensorInfo->param24;
                        offset = tempSensorInfo->param25;
                        calConcentration = tempSensorInfo->param26;

                        ageCorrections->BSensor = tempSensorInfo->param27;
                        ageCorrections->B2Sensor = tempSensorInfo->param30;
                        ageCorrections->B3Sensor = tempSensorInfo->param32;
                        ageCorrections->MvCut = tempSensorInfo->param34;
                        ageCorrections->AgeCut = tempSensorInfo->param36;
                        ageCorrections->FSensor = tempSensorInfo->param28;

                        ageCorrections->LowMvInjTimeCut = tempSensorInfo->param61;
                        ageCorrections->FPrimeSensor = tempSensorInfo->param62;
                        ageCorrections->HighMvInjTimeCut = tempSensorInfo->param63;
                        ageCorrections->FDoublePrimeSensor = tempSensorInfo->param64;
                    }

                    ageCorrections->GSensor = tempSensorInfo->G;

                    // aqueous windows
                    if (testMode == TestMode::QA)
                    {
                        // if hematocrit is under 5, we will assume its an aqueous solution and use the aqueous sample window
                        tempSensorInfo->sampleDelimit = carbonDioxideReadings->sensorDescriptor->param3;
                        tempSensorInfo->extrapolation = carbonDioxideReadings->sensorDescriptor->param4;
                        tempSensorInfo->sampleWindowSize = carbonDioxideReadings->sensorDescriptor->param5;

                        tempSensorInfo->postDelimit = carbonDioxideReadings->sensorDescriptor->param6;
                        tempSensorInfo->tMinus = carbonDioxideReadings->sensorDescriptor->param7;
                        tempSensorInfo->postWindowSize = carbonDioxideReadings->sensorDescriptor->param8;

                        if (carbonDioxideReadings->sensorDescriptor->param19 != 0)
                        {
                            tempSensorInfo->DeltaDriftLowQC = carbonDioxideReadings->sensorDescriptor->param19;
                        }
                    }

                    rc = CalculateEx(carbonDioxideReadings->readings, tempSensorInfo, bubbleDetect, sampleDetect,
                                     sensorLevels, true, testMode, true, topHeaterReadings, ageOfCard, ambientTemperature, true, false, false, 0, tempSensorInfo->param20, tempSensorInfo->param21, testMode, false, 0.0, false, ageCorrections, isBlood);

                    // this is the error that can get returned that we cant recover from..
                    // ...that there werent enough data points to calculate.
                    // if its just a qc failure.. continue.. but flag it as a qc failure as we
                    // leave
                    if (rc == ResultsCalcReturnCode::CannotCalculate)
                    {
                        carbonDioxideReadings->result = NAN;
                        carbonDioxideReadings->returnCode = rc;
                    }
                    else
                    {
                        double co2Offset = tempSensorInfo->param1;
                        double bicarbCorrect = tempSensorInfo->param2;
                        double co2Slope = 0.0;
                        double earlyDriftLimit = tempSensorInfo->param13;
                        double earlySlopeFactor = tempSensorInfo->param14;
                        double lateSlopeFactor = tempSensorInfo->param17;
                        double lateInjectionTimeOffset = tempSensorInfo->param18;

                        double slopeFactor = doubleSlopeFactor;

                        if (!isnan(calciumAdditionalDrift) && (isBlood || (!AWDriftCorrectedPCO2(tempSensorInfo->param15) && !isBlood)))
                        {
                            slopeFactor = slopeFactor + tempSensorInfo->param22 * (tempSensorInfo->param23 + calciumAdditionalDrift);
                        }

                        if (pCO2newSlope(tempSensorInfo->param15))
                        {
                            if ((bubbleDetect < tempSensorInfo->InjectionTimeOffset) && (sensorLevels->calSlope > earlyDriftLimit))
                            {
                                co2Slope = (slopeFactor * FullNernst()) *
                                           (1.0 - (earlySlopeFactor *
                                                   (bubbleDetect - tempSensorInfo->InjectionTimeOffset)));
                            }
                            else if (bubbleDetect > lateInjectionTimeOffset)
                            {
                                co2Slope = (slopeFactor * FullNernst()) *
                                           (1.0 - (lateSlopeFactor *
                                                   (bubbleDetect - lateInjectionTimeOffset)));
                            }
                            else
                            {
                                co2Slope = slopeFactor * FullNernst();
                            }
                        }
                        else
                        {
                            // this is what it was before..
                            co2Slope = slopeFactor * FullNernst();
                        }

                        sensorLevels->output4 = sensorLevels->response;

                        carbonDioxideReadings->result = CSharpDecimalCalculation(((calConcentration + co2Offset) *
                                                          std::pow(10, CSharpDecimalCalculation((sensorLevels->response - offset) /
                                                                        co2Slope))) - co2Offset);

                        if (isBlood && applyVolumeCorrections && (carbonDioxideReadings->result >= carbonDioxideReadings->sensorDescriptor->param46))
                        {
                            carbonDioxideReadings->result = carbonDioxideReadings->result + carbonDioxideReadings->sensorDescriptor->param45 * (carbonDioxideReadings->result - carbonDioxideReadings->sensorDescriptor->param46);
                        }

                        rc = CalMeanAgeCorrection(ageOfCard, rc, sensorLevels, tempSensorInfo, tempSensorInfo->param20, tempSensorInfo->param21);

                        double postSampleMeanLimit;

                        if ((testMode == TestMode::QA) && (tempSensorInfo->param16 != 0))
                        {
                            postSampleMeanLimit = tempSensorInfo->param16;
                        }
                        else
                        {
                            postSampleMeanLimit = tempSensorInfo->param12;
                        }

                        if ((std::abs(sensorLevels->postMean - sensorLevels->sampleMean) > postSampleMeanLimit) &&
                            (rc == ResultsCalcReturnCode::Success) && PostMeanMinusSampleMean(tempSensorInfo->param15))
                        {
                            carbonDioxideReadings->returnCode = rc = ResultsCalcReturnCode::DeltaMeanPostSample;
                        }

                        if (rc == ResultsCalcReturnCode::Success)
                        {
                            carbonDioxideReadings->returnCode = rc = ValidateInsanityRangeOnlyReturnCode(carbonDioxideReadings->result,
                                                                                                         testMode == TestMode::BloodTest ? carbonDioxideReadings->insanityLow : carbonDioxideReadings->insanityQALow,
                                                                                                         testMode == TestMode::BloodTest ? carbonDioxideReadings->insanityHigh : carbonDioxideReadings->insanityQAHigh);
                        }

                        if ((rc == ResultsCalcReturnCode::Success) && (carbonDioxideReadings->realTimeQCFailedEver == RealTimeQCReturnCode::Failed))
                        {
                            carbonDioxideReadings->returnCode = rc = ResultsCalcReturnCode::FailedQCEver;
                        }

                        if ((rc == ResultsCalcReturnCode::Success) && (testMode == TestMode::BloodTest))
                        {
                            carbonDioxideReadings->returnCode = ValidateReportableRangeOnlyReturnCode(carbonDioxideReadings->result, carbonDioxideReadings->reportableLow, carbonDioxideReadings->reportableHigh);
                        }
                        else
                        {
                            carbonDioxideReadings->returnCode = rc;
                        }
                    }

                    sensorLevels->output6 = CSharpDecimalCalculation(carbonDioxideReadings->result);
                    return carbonDioxideReadings->returnCode;
                }

                ResultsCalcReturnCode AnalyticalManager::CalculateCalcium(std::shared_ptr<SensorReadings> calciumReadings,
                                                                          std::shared_ptr<SensorReadings> pco2Reading,
                                                                          std::shared_ptr<SensorReadings> lactateReading,
                                                                          std::shared_ptr<SensorReadings> po2Reading,
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
                    ResultsCalcReturnCode rc;
                    std::shared_ptr<SensorInfo> sensorInfo = calciumReadings->sensorDescriptor;
                    std::shared_ptr<Levels> sensorLevels = calciumReadings->levels;
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
                    double xiCa;

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

                    xiCa = sensorInfo->param53;
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
                    if ((rc == ResultsCalcReturnCode::CannotCalculate) || (pco2Reading->returnCode == ResultsCalcReturnCode::CannotCalculate))
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
                        if (FailedIQC(pco2Reading->returnCode) || pco2Reading->requirementsFailedQC)
                        {
                            calciumReadings->requirementsFailedQC = true;
                        }

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
                        double correctedpO2Mean = po2Reading->levels->output1; // this value is placed in output1 by calculatepo2

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
                                ResultsCalcReturnCode rc2 = CalculateEx(calciumReadings->readings, tempSensorInfo2, bubbleDetect, sampleDetect, tempLevels2,
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

                        double swDiff = (sensorLevels->sampleEx - sensorLevels->calEx) - (tempLevels->sampleEx - tempLevels->calEx);

                        sensorLevels->response += ((sensorInfo->param11)) * swDiff;

                        // apply po2corrected mean correction after initial response.. only do if age and 30c logic flag is on
                        if (AgeAnd30CLogic(sensorInfo->param15) && CanBeUsedAsInput(po2Reading->returnCode))
                        {
                            if ((ageOfCard > AgeThreshpO2Corr) && ((correctedpO2Mean - (OiC1 * log((double)ageOfCard) + OiC2)) > DeltaiCalThreshpO2Corr))
                            {
                                sensorLevels->response = sensorLevels->response + OffsetFact + CorrFact * ageOfCard * (correctedpO2Mean - (OiC1 *
                                                                                                                                           log((double)ageOfCard) + OiC2));
                            }
                        }

                        sensorLevels->peakEx = tempLevels->calEx;
                        sensorLevels->peakMean = tempLevels->sampleMean;
                        sensorLevels->peakSlope = tempLevels->sampleSlope;
                        sensorLevels->peakNoise = tempLevels->sampleNoise;
                        sensorLevels->peakSecond = tempLevels->sampleSecond;

                        sensorLevels->response += k * (pco2Reading->result - pco2Reading->sensorDescriptor->calConcentration);

                        if ((((lactateReading != nullptr) && !FailedIQC(lactateReading->returnCode) && !lactateReading->requirementsFailedQC) ||
                             !NewSodiumCalciumLactateCorrection(sensorInfo->param15)) &&
                            (AgeAnd30CLogic(sensorInfo->param15) && ((double)ageOfCard >= ageCorrections->earlyAgeCut)))
                        {
                            calciumReadings->result = CSharpDecimalCalculation((calConcentration + calciumOffset) *
                                                       std::pow(10, CSharpDecimalCalculation((sensorLevels->response -
                                                                      (ageCorrections->HSensor * std::pow(M_E, ageCorrections->JSensor * ageOfCard) + ageCorrections->KSensor)) /
                                                                     (decimalSlopeFactor * 0.5 * FullNernst()))) -
                                                       calciumOffset);
                        }
                        else
                        {
                            calciumReadings->result = CSharpDecimalCalculation(((calConcentration + calciumOffset) *
                                                        std::pow(10, CSharpDecimalCalculation((sensorLevels->response - decimalOffset) /
                                                                      (decimalSlopeFactor * 0.5 * FullNernst()))))
                                                       - calciumOffset);
                        }

                        if (!isnan(actualBicarb))
                        {
                            calciumReadings->result = CSharpDecimalCalculation((calciumReadings->result) *
                                                       (1.0 +
                                                        (bicarbCorrect *
                                                         ((actualBicarb) - 25))));
                        }

                        if (NewSodiumCalciumLactateCorrection(sensorInfo->param15))
                        {
                            if ((lactateReading != nullptr) && !FailedIQC(lactateReading->returnCode) && !lactateReading->requirementsFailedQC)
                            {
                                calciumReadings->result = CSharpDecimalCalculation(calciumReadings->result - lactKCa * (lactateReading->result - lactOffCa));
                            }
                        }

                        calciumReadings->result = CSharpDecimalCalculation(calciumReadings->result / (1 - (xiCa * tempLevels->calSlope)));

                        if (isBlood && applyVolumeCorrections && (calciumReadings->result >= sensorInfo->param70))
                        {
                            calciumReadings->result = calciumReadings->result + sensorInfo->param69 * (calciumReadings->result - sensorInfo->param70);
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

                        if (rc == ResultsCalcReturnCode::Success)
                        {
                            calciumReadings->returnCode = rc = ValidateInsanityRangeOnlyReturnCode(calciumReadings->result,
                                                                                                   testMode == TestMode::BloodTest ? calciumReadings->insanityLow : calciumReadings->insanityQALow,
                                                                                                   testMode == TestMode::BloodTest ? calciumReadings->insanityHigh : calciumReadings->insanityQAHigh);
                        }

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
                    }

                    return calciumReadings->returnCode;
                }

                ResultsCalcReturnCode AnalyticalManager::CalculatePO2(std::shared_ptr<SensorReadings> oxygenReadings,
                                                                      std::shared_ptr<SensorReadings> glucoseReadings,
                                                                      double bubbleDetect,
                                                                      double sampleDetect,
                                                                      double ambientTemperature,
                                                                      double pressure,
                                                                      TestMode testMode,
                                                                      int ageOfCard,
                                                                      int sibVersion)
                {
                    ResultsCalcReturnCode rc;
                    auto sensorInfo = std::make_shared<SensorInfo>();
                    CopySensorInfo(sensorInfo, oxygenReadings->sensorDescriptor);
                    std::shared_ptr<Levels> sensorLevels = oxygenReadings->levels;

                    if ((testMode == TestMode::QA) && (sensorInfo->param25 != 0))
                    {
                        sensorInfo->SampleDriftHighQC = sensorInfo->param25;
                    }

                    // use alternate noise when old SIB
                    if (sibVersion < LowNoiseSIBVersion())
                    {
                        if (sensorInfo->param28 > 0.00001)
                        {
                            sensorInfo->CalNoiseHighQC = sensorInfo->param28;
                        }

                        if (sensorInfo->param29 > 0.00001)
                        {
                            sensorInfo->SampleNoiseHighQC = sensorInfo->param29;
                        }

                        if (sensorInfo->param30 > 0.00001)
                        {
                            sensorInfo->PostNoiseHighQC = sensorInfo->param30;
                        }
                    }

                    rc = CalculateEx(oxygenReadings->readings, sensorInfo, bubbleDetect, sampleDetect, sensorLevels,
                                     false, testMode, false, nullptr, 0, 0, false, false, false, 0, 0, 0, testMode, false, sensorInfo->param36, false, nullptr, false);

                    // this is the error that can get returned that we cant recover from..
                    // ...that there werent enough data points to calculate.
                    // if its just a qc failure.. continue.. but flag it as a qc failure as we
                    // leave
                    if (rc == ResultsCalcReturnCode::CannotCalculate)
                    {
                        oxygenReadings->result = NAN;

                        // uncorrected may have failed iqc.. we dont want to overwrite the old return code
                        if (oxygenReadings->returnCode == ResultsCalcReturnCode::Success)
                        {
                            oxygenReadings->returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                    }
                    else
                    {
                        if ((sensorInfo->B > -0.0001) && (sensorInfo->B < 0.0001))
                        {                    // from the main equation
                            double divcut = sensorInfo->param1;
                            double s_high = sensorInfo->param2;
                            double s_lo = sensorInfo->param3;
                            double y1 = sensorInfo->param4;
                            double y2 = sensorInfo->param5;
                            double a_high = sensorInfo->param6;
                            double a_low = sensorInfo->param7;

                            // for po2 @ cal release
                            double pressureAtSeal = sensorInfo->param8;
                            double m = sensorInfo->param9;

                            // for po2 @ cal at test
                            double k = sensorInfo->param10;
                            double numSecondsTop = sensorInfo->param11;
                            double numSecondsBottom = sensorInfo->param12;
                            double multiplicationFactor = sensorInfo->param13;

                            double middleOfCalWindow = ((((*oxygenReadings->readings)[sensorLevels->calFirst]).Time) +
                                                        ((((*oxygenReadings->readings)[sensorLevels->calLast]).Time))) / 2;
                            double decimalAmbient = ambientTemperature;
                            double decimalPressure = pressure;

                            double a;
                            double b = sensorInfo->slopeFactor;

                            if (ageOfCard >= b)
                            {
                                a = 0.0;
                            }
                            else
                            {
                                a = sensorInfo->offset;
                            }

                            double sample;
                            double cal;

                            // for mean-mean calculation. if param14 is 1 use samplemean and calmean
                            // instead of samplex and calex
                            if (sensorInfo->param14 == 1)
                            {
                                sample = sensorLevels->sampleMean;
                                cal = sensorLevels->calMean;
                            }
                            else
                            {
                                sample = sensorLevels->sampleEx;
                                cal = sensorLevels->calEx;
                            }

                            sensorLevels->uncorrectedResponse = sensorLevels->response = CSharpDecimalCalculation(sample / cal);

                            try
                            {
                                if (sensorInfo->param56 != 0) //FWindowSize
                                {
                                    auto tempSensorInfo2 = std::make_shared<SensorInfo>();
                                    CopySensorInfo(tempSensorInfo2, sensorInfo);
                                    tempSensorInfo2->sampleWindowSize = sensorInfo->param56; //FWindowSize;
                                    tempSensorInfo2->sampleDelimit = sensorInfo->param55; //FWindowStart;

                                    auto tempLevels2 = std::make_shared<Levels>();
                                    ResultsCalcReturnCode rc2 = CalculateEx(oxygenReadings->readings, tempSensorInfo2, bubbleDetect, sampleDetect, tempLevels2,
                                                                            false, testMode, false, nullptr, 0, 0, false, false, false, 0, 0, 0, testMode, false, tempSensorInfo2->param36, false, nullptr, false);

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

                            double firstMultiplier = 0.000306;
                            double secondMultiplier = 0.033098;
                            double additionFactor = 1.809078;

                            double div = CSharpDecimalCalculation((sample + y1 * std::pow(sample, 2) + y2 * std::pow(sample, 3)) /
                                                                  (cal + y1 * std::pow(cal, 2) + y2 * std::pow(cal, 3)));

                            double pcalrelease = CSharpDecimalCalculation((0.2094) *
                                                                          (pressureAtSeal - 36.6 - (0.0319 * decimalAmbient * decimalAmbient) + (0.0969 * decimalAmbient)) *
                                                                          (((firstMultiplier)* decimalAmbient * decimalAmbient) - (secondMultiplier * decimalAmbient) + additionFactor) *
                                                                          (1 - (((m - a * (b - ageOfCard)) * pressureAtSeal) / (ConvertKpaTommHg(decimalPressure)))));

                            double pcaltest = CSharpDecimalCalculation((1.0 - k) * pcalrelease +
                                                                       (0.2094) * k * (ConvertKpaTommHg(decimalPressure) - (77)) +
                                                                       (multiplicationFactor *
                                                                       ((std::pow(M_E, (-((middleOfCalWindow - numSecondsTop) / numSecondsBottom)))) - 1.0)));

                            if (div > divcut)
                            {
                                oxygenReadings->result = CSharpDecimalCalculation(pcaltest * s_high * ((div - a_high) / (1.0 - a_high)));
                            }
                            else
                            {
                                oxygenReadings->result = CSharpDecimalCalculation(pcaltest * s_lo * ((div - a_low) / (1.0 - a_low)));
                            }
                        }
                        else
                        {
                            // from the main equation
                            double divcut = sensorInfo->param1;
                            double s_high = sensorInfo->param2;
                            double s_lo = sensorInfo->param3;
                            double y1 = sensorInfo->param4;
                            double y2 = sensorInfo->param5;
                            double a_high = sensorInfo->param6;
                            double a_low = sensorInfo->param7;

                            // for po2 @ cal release
                            double pressureAtSeal = sensorInfo->param8;
                            double m = sensorInfo->param9;

                            // for po2 @ cal at test
                            double k = sensorInfo->param10;
                            double numSecondsTop = sensorInfo->param11;
                            double numSecondsBottom = sensorInfo->param12;
                            double multiplicationFactor = sensorInfo->param13;

                            double middleOfCalWindow = ((((*oxygenReadings->readings)[sensorLevels->calFirst]).Time) +
                                                        ((((*oxygenReadings->readings)[sensorLevels->calLast]).Time))) / 2;
                            double decimalAmbient = ambientTemperature;
                            double decimalPressure = pressure;

                            double d = 0.0;
                            double ageOffset = sensorInfo->AgeOffset;

                            double a1 = sensorInfo->param23;
                            double a2 = sensorInfo->param24;

                            double e = sensorInfo->param37;
                            double f = sensorInfo->param38;

                            double correctedMean = sensorLevels->calMean +
                                                   (a1 * (ageOfCard - ageOffset)) +
                                                   (a2 * (ConvertKpaTommHg(decimalPressure) - pressureAtSeal));

                            // we need this for chloride's corrected po2 correction
                            sensorLevels->output1 = correctedMean;
                            sensorLevels->output2 = sensorLevels->calMean;

                            // need to revalidate mean qc.. but we want to put the calmean back after.. the
                            // corrected calmean is going to be in postsecond
                            double previousCalMean = sensorLevels->calMean;
                            sensorLevels->calMean = correctedMean;

                            // overwrite the old return code with a new one.. this checks the corrected mean
                            // the reason we need to revalidate everything is that say the old mean doesnt
                            // pass, and then stops there.. if the new calmean passes, there may be something
                            // else waiting to fail behind that
                            if ((rc == ResultsCalcReturnCode::Success) || (rc == ResultsCalcReturnCode::CalMeanQCHigh) ||
                                (rc == ResultsCalcReturnCode::CalMeanQCLow))
                            {
                                rc = ValidateAllQC(sensorLevels, sensorInfo, false, false);
                            }

                            // put the corrected mean into post second.. and put the calmean back to what it was before
                            sensorLevels->postSecond = correctedMean;
                            sensorLevels->calMean = previousCalMean;

                            if (ageOfCard < ageOffset)
                            {
                                d = sensorInfo->D;
                            }
                            else
                            {
                                d = 0.0;
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
                                    ResultsCalcReturnCode rc2 = CalculateEx(oxygenReadings->readings, tempSensorInfo2, bubbleDetect, sampleDetect, tempLevels2,
                                                                            false, testMode, false, nullptr, 0, 0, false, false, false, 0, 0, 0, testMode, false, tempSensorInfo2->param36, false, nullptr, false);

                                    // calculate f parameter for AT output
                                    sensorLevels->output11 = tempLevels2->calEx;
                                    sensorLevels->output12 = tempLevels2->sampleMean;
                                    sensorLevels->output13 = tempLevels2->sampleNoise;
                                    sensorLevels->output14 = tempLevels2->sampleSlope;

                                    double f2 = CSharpDecimalCalculation((sensorLevels->sampleEx - sensorLevels->calEx) / (tempLevels2->sampleMean - tempLevels2->calEx));
                                    sensorLevels->output15 = f2;
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
                            double sample;
                            double cal;

                            // for mean-mean calculation. if param14 is 1 use samplemean and calmean
                            // instead of samplex and calex
                            if (sensorInfo->param14 == 1)
                            {
                                sample = sensorLevels->sampleMean;
                                cal = sensorLevels->calMean;
                            }
                            else
                            {
                                sample = sensorLevels->sampleEx;
                                cal = sensorLevels->calEx;
                            }

                            d = 0;

                            if (ageOfCard < sensorInfo->AgeOffset)
                            {
                                d = sensorInfo->D;
                            }

                            sensorLevels->uncorrectedResponse = sensorLevels->response = CSharpDecimalCalculation(sample / cal);

                            double div = CSharpDecimalCalculation((sample + y1 * std::pow(sample, 2) + y2 * std::pow(sample, 3)) /
                                         (cal + y1 * std::pow(cal, 2) + y2 * std::pow(cal, 3)));

                            // redmine 10590.. divide age correction by div raised to the power of e
                            double pcaltest = sensorInfo->calConcentration +
                                              (sensorInfo->A * (decimalAmbient - sensorInfo->TAmbOffset)) +
                                              (sensorInfo->B * (ConvertKpaTommHg(decimalPressure) - pressureAtSeal)) +
                                              (sensorInfo->C * (sampleDetect - sensorInfo->InjectionTimeOffset)) +
                                              (d * (ageOfCard - sensorInfo->AgeOffset) / ((std::pow(div, e))));

                            if (div > divcut)
                            {
                                oxygenReadings->result = CSharpDecimalCalculation(pcaltest * s_high * ((div - a_high) / (1.0 - a_high)));
                            }
                            else
                            {
                                oxygenReadings->result = CSharpDecimalCalculation(pcaltest * s_lo * ((div - a_low) / (1.0 - a_low)));
                            }

                            // we may need to do a weighting between the old equation and this new one
                            if (NewOxygenEquation(sensorInfo->param15))
                            {
                                double z = sensorInfo->param16;
                                double y = sensorInfo->param17;
                                double x = sensorInfo->param18;
                                double t = sensorInfo->param19;
                                double v = sensorInfo->param20;
                                double w = sensorInfo->param21;
                                double u = sensorInfo->param31;
                                double calMeanThreshold = sensorInfo->param32;
                                double weightB = sensorInfo->param33;

                                double weight = sensorInfo->param22;

                                // redmine 10590.. multiple age correction by div raised to the power of f
                                double correctionsTerm = (t * (ageOfCard - ageOffset) * ((std::pow(div, f)))) +
                                                         (v * (decimalAmbient - sensorInfo->TAmbOffset)) +
                                                         (w * (ConvertKpaTommHg(decimalPressure) - pressureAtSeal));

                                double smpConc2 = (z * (sample) * (sample)) +
                                                  (y * (sample)) +
                                                  x +
                                                  correctionsTerm;

                                double correctionsTermFinal = u * (sampleDetect - sensorInfo->InjectionTimeOffset);

                                if (sensorLevels->calMean > calMeanThreshold - (a2 * (ConvertKpaTommHg(decimalPressure) - pressureAtSeal)))
                                {
                                    oxygenReadings->result = CSharpDecimalCalculation(((1 - weightB) * smpConc2) +
                                                              (weightB * oxygenReadings->result))
                                                             + correctionsTermFinal;
                                }
                                else
                                {
                                    oxygenReadings->result = CSharpDecimalCalculation(((1 - weight) * smpConc2) +
                                                              (weight * oxygenReadings->result))
                                                             + correctionsTermFinal;
                                }
                            }
                        }
                    }

                    //if ((rc == ResultsCalcReturnCode::Success) && (glucoseReadings != nullptr) &&
                    //    ((glucoseReadings.returnCode == ResultsCalcReturnCode::EarlyDipInSample) ||
                    //    (glucoseReadings.returnCode == ResultsCalcReturnCode::EarlySpikeInSample)))
                    //{
                    //    // if glucose fails early spike dip then oxygen is cnc
                    //    rc = ResultsCalcReturnCode::CannotCalculate;
                    //}

                    if (pO2BubbleDetect(sensorInfo->param15))
                    {
                        // po2 bubble catchers
                        if (pO2HasSampleBubble(oxygenReadings, sensorInfo, sampleDetect, sensorLevels->peakSlope, sensorLevels->peakNoise, sensorLevels->peakSecond))
                        {
                            if (rc == ResultsCalcReturnCode::Success)
                            {
                                rc = ResultsCalcReturnCode::PO2Bubble;
                            }
                        }
                    }

                    if (rc == ResultsCalcReturnCode::Success)
                    {
                        rc = CheckAllPointsWithinLimits(oxygenReadings, sensorLevels->sampleFirst, sensorLevels->sampleLast, sensorInfo->param34, sensorInfo->param35);
                    }

                    if (rc == ResultsCalcReturnCode::Success)
                    {
                        oxygenReadings->returnCode = rc = ValidateInsanityRangeOnlyReturnCode(oxygenReadings->result,
                                                                                              testMode == TestMode::BloodTest ? oxygenReadings->insanityLow : oxygenReadings->insanityQALow,
                                                                                              testMode == TestMode::BloodTest ? oxygenReadings->insanityHigh : oxygenReadings->insanityQAHigh);
                    }

                    // turn oxygen return code to failed qc ever if realtime qc failed during the test and there were no other failures
                    if ((rc == ResultsCalcReturnCode::Success) && (oxygenReadings->realTimeQCFailedEver == RealTimeQCReturnCode::Failed))
                    {
                        oxygenReadings->returnCode = rc = ResultsCalcReturnCode::FailedQCEver;
                    }

                    if ((rc == ResultsCalcReturnCode::Success) && (testMode == TestMode::BloodTest))
                    {
                        oxygenReadings->returnCode = ValidateReportableRangeOnlyReturnCode(oxygenReadings->result, oxygenReadings->reportableLow, oxygenReadings->reportableHigh);
                    }
                    else
                    {
                        oxygenReadings->returnCode = rc;
                    }

                    return oxygenReadings->returnCode;
                }

                bool AnalyticalManager::pO2HasSampleBubble(std::shared_ptr<SensorReadings> oxygenReadings,
                                                           std::shared_ptr<SensorInfo> sensorInfo,
                                                           double sampleDetect,
                                                           double& refSlope,
                                                           double& refNoise,
                                                           double& refSecond)
                {
                    bool hasBubble = false;
                    double mean = 0;
                    double slope = 0;
                    double second = 0;
                    double noise = 0;
                    int firstPoint = 0;
                    int lastPoint = 0;
                    int sampleDetectStart = 0;
                    double cumulativeNoise = 0;

                    ResultsCalcReturnCode rc = FindWindowParams(oxygenReadings->readings,
                                                                sampleDetect + 4.6,
                                                                0.8, mean, slope, noise, second,
                                                                firstPoint, lastPoint, 0, false, 0, false, sensorInfo->param36,
                                                                DefaultPointsToLeftForSecondDerivative,
                                                                DefaultPointsToRightForSecondDerivative);

                    refSlope = slope;
                    refSecond = noise;

                    if (rc == ResultsCalcReturnCode::CannotCalculate)
                    {
                        return false;
                    }
                    else if (noise > sensorInfo->param27)
                    {
                        // if noise on the window fails.. forget the window. but pass back the noise level
                        return false;
                    }

                    // find sample start data point
                    for (size_t i = 0; i < oxygenReadings->readings->size(); i++)
                    {
                        if ((*oxygenReadings->readings)[i].Time >= sampleDetect)
                        {
                            sampleDetectStart = i;
                            break;
                        }
                    }

                    // not enough total data points to do this check
                    if (sampleDetectStart + numPointsInPO2BubbleDetect > oxygenReadings->readings->size())
                    {
                        return false;
                    }

                    for (int i = numPointsInPO2BubbleDetectThatAreZero; i < numPointsInPO2BubbleDetect; i++)
                    {
                        CSharpOutOfRange(sampleDetectStart + i, oxygenReadings->readings->size());
                        CSharpOutOfRange(sampleDetectStart + i - 1, oxygenReadings->readings->size());

                        if (slope > 0)
                        {
                            cumulativeNoise += std::abs(std::min(((*oxygenReadings->readings)[sampleDetectStart + i].Value -
                                                                  (*oxygenReadings->readings)[sampleDetectStart + i - 1].Value) +
                                                                 0.05, 0.0));
                        }
                        else
                        {
                            cumulativeNoise += std::abs(std::max((*oxygenReadings->readings)[sampleDetectStart + i].Value -
                                                                 (*oxygenReadings->readings)[sampleDetectStart + i - 1].Value -
                                                                 0.08, 0.0));
                        }
                    }

                    refNoise = cumulativeNoise;

                    if (cumulativeNoise > sensorInfo->param26)
                        hasBubble = true;

                    return hasBubble;
                }

                double AnalyticalManager::DecimalMax0(double value)
                {
                    if (value < 0)
                    {
                        return 0.0;
                    }
                    else
                    {
                        return value;
                    }
                }

                ResultsCalcReturnCode AnalyticalManager::CalculateGlucose(std::shared_ptr<SensorReadings> glucoseReadings,
                                                                          double bubbleDetect,
                                                                          double sampleDetect,
                                                                          TestMode testMode,
                                                                          int ageOfCard,
                                                                          std::shared_ptr<SensorReadings> oxygenReadings,
                                                                          std::shared_ptr<SensorReadings> co2Readings,
                                                                          std::shared_ptr<SensorReadings> hctReadings,
                                                                          int sibVersion,
                                                                          bool isBlood,
                                                                          bool allowNegativeValues)
                {
                    if (!NewestGlucose(glucoseReadings->sensorDescriptor->param15))
                    {
                        return CalculateGlucoseOrLactate(glucoseReadings, bubbleDetect, sampleDetect, testMode,
                                                         ageOfCard, oxygenReadings, co2Readings, hctReadings, sibVersion, isBlood, allowNegativeValues);
                    }
                    else
                    {
                        ResultsCalcReturnCode rc;
                        std::shared_ptr<SensorInfo> sensorInfo = glucoseReadings->sensorDescriptor;
                        std::shared_ptr<Levels> sensorLevels = glucoseReadings->levels;

                        double b = sensorInfo->param16;
                        double c = sensorInfo->param17;

                        double oldCalMeanLowQC = sensorInfo->CalMeanLowQC;
                        double oldCalMeanHighQC = sensorInfo->CalMeanHighQC;

                        // use alternate noise when old SIB
                        if (sibVersion < LowNoiseSIBVersion())
                        {
                            if (sensorInfo->param27 > 0.00001)
                            {
                                sensorInfo->CalNoiseHighQC = sensorInfo->param27;
                            }

                            if (sensorInfo->param28 > 0.00001)
                            {
                                sensorInfo->SampleNoiseHighQC = sensorInfo->param28;
                            }

                            if (sensorInfo->param29 > 0.00001)
                            {
                                sensorInfo->PostNoiseHighQC = sensorInfo->param29;
                            }
                        }

                        // change the low mean limit
                        sensorInfo->CalMeanLowQC = (sensorInfo->CalMeanLowQC +
                                                    (b * (ageOfCard - sensorInfo->AgeOffset)) +
                                                    (c * (sampleDetect - sensorInfo->InjectionTimeOffset)));

                        // need to do this because old glu/lact uses param25 for something else
                        if (NewGlucoseAndLactate(sensorInfo->param15))
                        {
                            sensorInfo->CalMeanHighQC = (sensorInfo->CalMeanHighQC +
                                                         (sensorInfo->param25 * (ageOfCard - sensorInfo->AgeOffset)) +
                                                         (sensorInfo->param30 * (sampleDetect - sensorInfo->InjectionTimeOffset)));
                        }

                        rc = CalculateEx(glucoseReadings->readings, sensorInfo, bubbleDetect, sampleDetect, sensorLevels,
                                         true, testMode, false, nullptr, 0, 0, false, false, false, 0.0, 0, 0, testMode, false, sensorInfo->param47, false, nullptr, isBlood);

                        // put the old limits back. the host keeps passing the same sensor info back in, so if we don't do this
                        // we would keep changing the value based on an already changed value every time there's a save
                        sensorInfo->CalMeanLowQC = oldCalMeanLowQC;
                        sensorInfo->CalMeanHighQC = oldCalMeanHighQC;

                        sensorLevels->peakEx = sensorInfo->PostSecondLowQC;
                        sensorLevels->peakMean = sensorInfo->PostSecondHighQC;
                        sensorLevels->peakNoise = sensorLevels->postSecond;
                        sensorLevels->peakSlope = (double)rc;

                        double originalSampleDriftHighQC = sensorInfo->SampleDriftHighQC;
                        double originalPostDriftHighQC = sensorInfo->PostDriftHighQC;
                        double originalDeltaDriftHighQC = sensorInfo->DeltaDriftHighQC;
                        double originalSampleSecondLowQC = sensorInfo->SampleSecondLowQC;

                        if (rc != ResultsCalcReturnCode::CannotCalculate)
                        {
                            // for these specific limits.. we may have to redo the qc because the qc may depend on the sample mean
                            if ((rc == ResultsCalcReturnCode::SampleDriftQCHigh) ||
                                (rc == ResultsCalcReturnCode::PostDriftQCHigh) ||
                                (rc == ResultsCalcReturnCode::DeltraDriftHigh) ||
                                (rc == ResultsCalcReturnCode::SampleSecondQCLow))
                            {
                                // set different drift limits if it was a very low mean, and re-do the qc checking.
                                // the limit we check against is a fixed limit for glucose and param11 for lactate
                                if (CSharpDecimalCalculation(sensorLevels->sampleMean / sensorLevels->calMean) > glucoseReadings->sensorDescriptor->param11)
                                {
                                    sensorInfo->SampleDriftHighQC = std::max(1.5, sensorInfo->SampleDriftHighQC);
                                    sensorInfo->PostDriftHighQC = std::max(1.5, sensorInfo->PostDriftHighQC);
                                    sensorInfo->DeltaDriftHighQC = std::max(1.5, sensorInfo->DeltaDriftHighQC);
                                    sensorInfo->SampleSecondLowQC = std::min(-0.15, sensorInfo->SampleSecondLowQC);
                                    rc = ValidateAllQC(sensorLevels, sensorInfo, false, false);
                                }
                            }

                            // from the newest equation
                            double slopeFactorLow = sensorInfo->slopeFactor;
                            double y0S0 = sensorInfo->param1;
                            double O2B = sensorInfo->param2;
                            double y1S0 = sensorInfo->param3;
                            double y2S0 = sensorInfo->param4;
                            double y3S0 = sensorInfo->param5;
                            double y0C0 = sensorInfo->param6;
                            double y1C0 = sensorInfo->param7;
                            double y2C0 = sensorInfo->param8;
                            double O2E = sensorInfo->param9;
                            double O2A = sensorInfo->param10;
                            // param 11 is up top .. high threshold for post in div
                            // param12 is Post sample mean limit
                            double y3C0 = sensorInfo->param13;
                            // param14 is mean/mean
                            // param15 is the flags parameter
                            // param16 is age correction b up top
                            // param17 is age correction c up top
                            // param18 is short window start below
                            // param19 is short window size below
                            // param20 is short window low limit below
                            // param21 is short window high limit below
                            double O2C = sensorInfo->param22;
                            double y0Age = sensorInfo->param23;
                            double y0Inj = sensorInfo->param24;
                            // param25 cmean upper b age above
                            double y0O2 = sensorInfo->param26;
                            // param27 - param29 low gain reader limits
                            // param30 c mean upper c inj time
                            double y1Age = sensorInfo->param31;
                            double y1Inj = sensorInfo->param32;
                            double divCutAQSLP = sensorInfo->param33;
                            double lowHctThresholdAQSLP = sensorInfo->param34;
                            double MAQSLP = sensorInfo->param35;
                            double y2Age = sensorInfo->param36;
                            double y2Inj = sensorInfo->param37;
                            double y3Age = sensorInfo->param38;
                            double y3Inj = sensorInfo->param39;
                            double pO2Offset = sensorInfo->param40;
                            double divCut = sensorInfo->param41;
                            double aHi = sensorInfo->param42;
                            double aLo = sensorInfo->param43;
                            double y2AgeInj = sensorInfo->param44;

                            double sample = 0.0;
                            double cal = 0.0;
                            double post = sensorLevels->postMean;
                            double oxygenValue;
                            double ageOfCardDecimal = ageOfCard;
                            double sampleDetectDecimal = sampleDetect;
                            double ageOffsetDecimal = sensorInfo->AgeOffset;
                            double injectionOffsetDecimal = sensorInfo->InjectionTimeOffset;

                            // for mean-mean calculation. if param14 is 1 use samplemean and calmean
                            // instead of samplex and calex
                            if (sensorInfo->param14 == 1)
                            {
                                sample = sensorLevels->sampleMean;
                                cal = sensorLevels->calMean;
                            }
                            else
                            {
                                sample = sensorLevels->sampleEx;
                                cal = sensorLevels->calEx;
                            }

                            sensorLevels->uncorrectedResponse = sensorLevels->response = CSharpDecimalCalculation(sample / cal);

                            try
                            {
                                if (sensorInfo->param56 != 0) //FWindowSize
                                {
                                    auto tempSensorInfo2 = std::make_shared<SensorInfo>();
                                    CopySensorInfo(tempSensorInfo2, sensorInfo);
                                    tempSensorInfo2->sampleWindowSize = sensorInfo->param56; //FWindowSize;
                                    tempSensorInfo2->sampleDelimit = sensorInfo->param55; //FWindowStart;

                                    auto tempLevels2 = std::make_shared<Levels>();
                                    ResultsCalcReturnCode rc2 = CalculateEx(glucoseReadings->readings, tempSensorInfo2, bubbleDetect, sampleDetect, tempLevels2,
                                                                            true, testMode, false, nullptr, 0, 0, false, false, false, 0.0, 0, 0, testMode, false,
                                                                            tempSensorInfo2->param47, false, nullptr, isBlood);

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


                            if ((oxygenReadings != nullptr) && !FailedIQC(oxygenReadings->returnCode) && (oxygenReadings->returnCode != ResultsCalcReturnCode::CannotCalculate))
                            {
                                oxygenValue = oxygenReadings->result;
                            }
                            else
                            {
                                // glucose continues on with a default value for oxygen
                                oxygenValue = DefaultOxygenValueForGlu;
                            }

                            double y0S = y0S0 + y0Age * DecimalMax0(ageOfCardDecimal - ageOffsetDecimal) +
                                         y0Inj * DecimalMax0(sampleDetectDecimal - injectionOffsetDecimal) +
                                         y0O2 * (oxygenValue - pO2Offset);

                            double y1S = y1S0 + y1Age * DecimalMax0(ageOfCardDecimal - ageOffsetDecimal) +
                                         y1Inj * DecimalMax0(sampleDetectDecimal - injectionOffsetDecimal);

                            double y2S = y2S0 + y2Age * DecimalMax0(ageOfCardDecimal - ageOffsetDecimal) +
                                         y2Inj * DecimalMax0(sampleDetectDecimal - injectionOffsetDecimal) +
                                         y2AgeInj * DecimalMax0(ageOfCardDecimal - ageOffsetDecimal) *
                                         DecimalMax0(sampleDetectDecimal - injectionOffsetDecimal);

                            double y3S = y3S0 + y3Age * DecimalMax0(ageOfCardDecimal - ageOffsetDecimal) +
                                         y3Inj * DecimalMax0(sampleDetectDecimal - injectionOffsetDecimal);

                            double y0C = y0C0 + y0Age * DecimalMax0(ageOfCardDecimal - ageOffsetDecimal) +
                                         y0Inj * DecimalMax0(sampleDetectDecimal - injectionOffsetDecimal) +
                                         y0O2 * (oxygenValue - pO2Offset);

                            double y1C = y1C0 + y1Age * DecimalMax0(ageOfCardDecimal - ageOffsetDecimal) +
                                         y1Inj * DecimalMax0(sampleDetectDecimal - injectionOffsetDecimal);

                            double y2C = y2C0 + y2Age * DecimalMax0(ageOfCardDecimal - ageOffsetDecimal) +
                                         y2Inj * DecimalMax0(sampleDetectDecimal - injectionOffsetDecimal) +
                                         y2AgeInj * DecimalMax0(ageOfCardDecimal - ageOffsetDecimal) *
                                         DecimalMax0(sampleDetectDecimal - injectionOffsetDecimal);

                            double y3C = y3C0 + y3Age * DecimalMax0(ageOfCardDecimal - ageOffsetDecimal) +
                                         y3Inj * DecimalMax0(sampleDetectDecimal - injectionOffsetDecimal);

                            double div = CSharpDecimalCalculation((y0S + sample + (y1S * (sample - cal)) + (y2S * (sample * sample)) +
                                                                    (y3S * (sample * sample * sample))) /
                                                                  (y0C + (y1C * cal) + (y2C * (cal * cal)) + (y3C * (cal * cal * cal))));

                            div = div * (1 + ((O2E * (div * div)) + (O2C * div) + O2B) *
                                             ((std::exp((-0.693 / O2A) * oxygenValue))));

                            if ((hctReadings != nullptr) && !FailedIQC(hctReadings->returnCode) && !hctReadings->requirementsFailedQC)
                            {
                                if ((div < divCutAQSLP) && (hctReadings->result < lowHctThresholdAQSLP))
                                {
                                    div = div * (1 - MAQSLP);
                                }
                            }

                            if (div >= divCut)
                            {
                                double slopeFactorHi = CSharpDecimalCalculation(slopeFactorLow * (1 - aLo) * (divCut - aHi) / ((1.0 - aHi) * (divCut - aLo)));
                                glucoseReadings->result = CSharpDecimalCalculation(slopeFactorHi * sensorInfo->calConcentration * (div - aHi) / (1.0 - aHi));
                            }
                            else
                            {
                                glucoseReadings->result = CSharpDecimalCalculation(slopeFactorLow * sensorInfo->calConcentration * (div - aLo) / (1.0 - aLo));
                            }

                            if ((std::abs(sensorLevels->postMean - sensorLevels->sampleMean) > sensorInfo->param12) &&
                                (rc == ResultsCalcReturnCode::Success) && PostMeanMinusSampleMean(sensorInfo->param15))
                            {
                                glucoseReadings->returnCode = rc = ResultsCalcReturnCode::DeltaMeanPostSample;
                            }

                            if ((co2Readings != nullptr) &&
                                CalFailedIQC(co2Readings->returnCode) &&
                                (rc == ResultsCalcReturnCode::Success))
                            {
                                rc = glucoseReadings->returnCode = ResultsCalcReturnCode::CannotCalculate;
                                glucoseReadings->requirementsFailedQC = true;
                            }

                            // if window size is less than 0.1 (backwards compatibility for testconfigs that have this param set to 0)
                            if (sensorInfo->param19 > 0.1)
                            {
                                double meanOfISampleWindow = GetMean(glucoseReadings->readings,
                                                                     sampleDetect + sensorInfo->param18, // window start measured from sample detect
                                                                     sensorInfo->param19); // window size

                                //sensorLevels->output4 = 0.0;

                                // there is a case where there are no points in the window
                                if (meanOfISampleWindow != DBL_MAX)
                                {
                                    if (sensorLevels->calMean != 0)
                                    {
                                        // divide isample by ical to get value to check against the limits
                                        meanOfISampleWindow = CSharpDecimalCalculation(meanOfISampleWindow / sensorLevels->calMean);
                                    }

                                    //sensorLevels->output4 = meanOfISampleWindow;

                                    if ((rc == ResultsCalcReturnCode::Success) && (meanOfISampleWindow < sensorInfo->param20)) // param20 low limit
                                    {
                                        rc = glucoseReadings->returnCode = ResultsCalcReturnCode::PostSecondQCLow;
                                    }
                                    else if ((rc == ResultsCalcReturnCode::Success) && (meanOfISampleWindow > sensorInfo->param21)) // param21 high limit
                                    {
                                        rc = glucoseReadings->returnCode = ResultsCalcReturnCode::PostSecondQCHigh;
                                    }
                                }
                            }

                            if (rc == ResultsCalcReturnCode::Success)
                            {
                                rc = CheckAllPointsWithinLimits(glucoseReadings, sensorLevels->sampleFirst, sensorLevels->sampleLast, sensorInfo->param45, sensorInfo->param46);
                            }

                            if (rc == ResultsCalcReturnCode::Success)
                            {
                                glucoseReadings->returnCode = rc = ValidateInsanityRangeOnlyReturnCode(glucoseReadings->result,
                                                                                                       testMode == TestMode::BloodTest ? glucoseReadings->insanityLow : glucoseReadings->insanityQALow,
                                                                                                       testMode == TestMode::BloodTest ? glucoseReadings->insanityHigh : glucoseReadings->insanityQAHigh);
                            }

                            // turn glucose return code to failed qc ever if realtime qc failed during the test and there were no other failures
                            if ((rc == ResultsCalcReturnCode::Success) && (glucoseReadings->realTimeQCFailedEver == RealTimeQCReturnCode::Failed))
                            {
                                glucoseReadings->returnCode = rc = ResultsCalcReturnCode::FailedQCEver;
                            }

                            if ((rc == ResultsCalcReturnCode::Success) && (testMode == TestMode::BloodTest))
                            {
                                glucoseReadings->returnCode = rc = ValidateReportableRangeOnlyReturnCode(glucoseReadings->result, glucoseReadings->reportableLow, glucoseReadings->reportableHigh);
                            }
                            else
                            {
                                glucoseReadings->returnCode = rc;
                            }

                            if (!allowNegativeValues)
                            {
                                // Do not display values less than zero
                                if (glucoseReadings->result < 0)
                                {
                                    glucoseReadings->result = 0;
                                }
                            }
                        }

                        return glucoseReadings->returnCode = rc;
                    }
                }

                ResultsCalcReturnCode AnalyticalManager::CalculateLactate(std::shared_ptr<SensorReadings> lactateReadings,
                                                                          double bubbleDetect,
                                                                          double sampleDetect,
                                                                          TestMode testMode,
                                                                          int ageOfCard,
                                                                          std::shared_ptr<SensorReadings> oxygenReadings,
                                                                          std::shared_ptr<SensorReadings> co2Readings,
                                                                          std::shared_ptr<SensorReadings> hctReadings,
                                                                          int sibVersion,
                                                                          bool isBlood,
                                                                          bool allowNegativeValues)
                {
                    return CalculateGlucoseOrLactate(lactateReadings, bubbleDetect, sampleDetect, testMode,
                                                     ageOfCard, oxygenReadings, co2Readings, hctReadings, sibVersion, isBlood, allowNegativeValues);
                }

                ResultsCalcReturnCode AnalyticalManager::CalculateGlucoseOrLactate(std::shared_ptr<SensorReadings> passedReadings,
                                                                                   double bubbleDetect,
                                                                                   double sampleDetect,
                                                                                   TestMode testMode,
                                                                                   int ageOfCard,
                                                                                   std::shared_ptr<SensorReadings> oxygenReadings,
                                                                                   std::shared_ptr<SensorReadings> co2Readings,
                                                                                   std::shared_ptr<SensorReadings> hctReadings,
                                                                                   int sibVersion,
                                                                                   bool isBlood,
                                                                                   bool allowNegativeValues)
                {
                    ResultsCalcReturnCode rc;
                    std::shared_ptr<SensorInfo> sensorInfo = passedReadings->sensorDescriptor;
                    std::shared_ptr<Levels> sensorLevels = passedReadings->levels;

                    double b = sensorInfo->param16;
                    double c = sensorInfo->param17;

                    double oldCalMeanLowQC = sensorInfo->CalMeanLowQC;
                    double oldCalMeanHighQC = sensorInfo->CalMeanHighQC;
                    double rLac;
                    if (isBlood)
                    {
                        rLac = sensorInfo->param53;
                    }
                    else
                    {
                        rLac = sensorInfo->param54;
                    }
                    // use alternate noise when old SIB
                    if (sibVersion < LowNoiseSIBVersion())
                    {
                        if (sensorInfo->param27 > 0.00001)
                        {
                            sensorInfo->CalNoiseHighQC = sensorInfo->param27;
                        }

                        if (sensorInfo->param28 > 0.00001)
                        {
                            sensorInfo->SampleNoiseHighQC = sensorInfo->param28;
                        }

                        if (sensorInfo->param29 > 0.00001)
                        {
                            sensorInfo->PostNoiseHighQC = sensorInfo->param29;
                        }
                    }

                    // change the low mean limit
                    sensorInfo->CalMeanLowQC = (sensorInfo->CalMeanLowQC +
                                                (b * (ageOfCard - sensorInfo->AgeOffset)) +
                                                (c * (sampleDetect - sensorInfo->InjectionTimeOffset)));

                    // need to do this because old glu/lact uses param25 for something else
                    if (NewGlucoseAndLactate(sensorInfo->param15))
                    {
                        sensorInfo->CalMeanHighQC = (sensorInfo->CalMeanHighQC +
                                                     (sensorInfo->param25 * (ageOfCard - sensorInfo->AgeOffset)) +
                                                     (sensorInfo->param30 * (sampleDetect - sensorInfo->InjectionTimeOffset)));
                    }

                    rc = CalculateEx(passedReadings->readings, sensorInfo, bubbleDetect, sampleDetect, sensorLevels,
                                     true, testMode, false, nullptr, 0, 0, false, false, false, 0.0, 0, 0, testMode, false,
                                     passedReadings->sensorType == Sensors::Glucose ? sensorInfo->param47 : sensorInfo->param41, false, nullptr, isBlood);

                    // put the old limits back. the host keeps passing the same sensor info back in, so if we don't do this
                    // we would keep changing the value based on an already changed value every time there's a save
                    sensorInfo->CalMeanLowQC = oldCalMeanLowQC;
                    sensorInfo->CalMeanHighQC = oldCalMeanHighQC;

                    sensorLevels->peakEx = sensorInfo->PostSecondLowQC;
                    sensorLevels->peakMean = sensorInfo->PostSecondHighQC;
                    sensorLevels->peakNoise = sensorLevels->postSecond;
                    sensorLevels->peakSlope = (double)rc;

                    double originalSampleDriftHighQC = sensorInfo->SampleDriftHighQC;
                    double originalPostDriftHighQC = sensorInfo->PostDriftHighQC;
                    double originalDeltaDriftHighQC = sensorInfo->DeltaDriftHighQC;
                    double originalSampleSecondLowQC = sensorInfo->SampleSecondLowQC;
                    double originalDeltaDriftLowQC = sensorInfo->DeltaDriftLowQC;
                    double originalSampleDriftLowQC = sensorInfo->SampleDriftLowQC;

                    if (rc != ResultsCalcReturnCode::CannotCalculate)
                    {
                        // for these specific limits.. we may have to redo the qc because the qc may depend on the sample mean
                        if ((rc == ResultsCalcReturnCode::SampleDriftQCHigh) ||
                            (rc == ResultsCalcReturnCode::PostDriftQCHigh) ||
                            (rc == ResultsCalcReturnCode::DeltraDriftHigh) ||
                            (rc == ResultsCalcReturnCode::SampleSecondQCLow) ||
                            (rc == ResultsCalcReturnCode::DeltaDriftLow) ||
                            (rc == ResultsCalcReturnCode::SampleDriftQCLow))
                        {
                            // set different drift limits if it was a very low mean, and re-do the qc checking.
                            // the limit we check against is a fixed limit for glucose and param11 for lactate
                            if (CSharpDecimalCalculation(sensorLevels->sampleMean / sensorLevels->calMean) >
                                (passedReadings->sensorType == Sensors::Glucose ?
                                 GluSampleMeanCalMeanHighLimitDifferentLimits :
                                 passedReadings->sensorDescriptor->param11))
                            {
                                sensorInfo->SampleDriftHighQC = std::max(1.5, sensorInfo->SampleDriftHighQC);
                                sensorInfo->PostDriftHighQC = std::max(1.5, sensorInfo->PostDriftHighQC);
                                sensorInfo->DeltaDriftHighQC = std::max(1.5, sensorInfo->DeltaDriftHighQC);
                                sensorInfo->SampleSecondLowQC = std::min(-0.15, sensorInfo->SampleSecondLowQC);

                                // if its glucose, replace these 2 limits with the min of a default value and param48
                                if (passedReadings->sensorType == Sensors::Glucose)
                                {
                                    sensorInfo->SampleDriftLowQC = std::min(GlucoseDriftDeltaDriftLow, sensorInfo->SampleDriftLowQC);
                                    sensorInfo->DeltaDriftLowQC = std::min(GlucoseDriftDeltaDriftLow, sensorInfo->DeltaDriftLowQC);
                                }

                                rc = ValidateAllQC(sensorLevels, sensorInfo, false, false);
                            }
                        }

                        if (NewGlucoseAndLactate(sensorInfo->param15))
                        {
                            // from the new equation
                            double divcut = sensorInfo->param1;
                            double s_lo = sensorInfo->param3;
                            double b_glu = sensorInfo->param2;

                            double y1 = sensorInfo->param4;
                            double y2 = sensorInfo->param5;
                            double a_high = sensorInfo->param6;
                            double a_low = sensorInfo->param7;
                            double s_high = CSharpDecimalCalculation(s_lo * (1 - a_low) * ((divcut - a_high) / ((1 - a_high) * (divcut - a_low))));
                            double calConcentrationHigh = sensorInfo->calConcentration;
                            double j_glu = sensorInfo->param13;
                            double k_glu = sensorInfo->param26;

                            // for po2 @ cal release
                            double d1_glu = sensorInfo->param8;
                            double d2_glu = sensorInfo->param64;
                            double e_glu = sensorInfo->param9;
                            double a_glu = sensorInfo->param10;
                            double c_glu = sensorInfo->param22;
                            double f_glu = sensorInfo->param23;

                            double g_glu;
                            double p_glu = sensorInfo->param36;

                            if (isBlood || !PotsAndGluLacAqBlood(sensorInfo->param15))
                            {
                                g_glu = sensorInfo->param24;
                                p_glu = sensorInfo->param36;
                            }
                            else
                            {
                                g_glu = sensorInfo->param51;
                                p_glu = sensorInfo->param52;
                            }

                            double sample = 0.0;
                            double cal = 0.0;
                            double post = sensorLevels->postMean;

                            double divCutExp = sensorInfo->param31;
                            double l_glu = sensorInfo->param32;


                            double lowpo2Threshold = sensorInfo->param37;
                            double lowpo2Exponent = sensorInfo->param38;

                            double sampleDiv = 0.0;
                            double postDiv = 0.0;
                            double originalDiv = 0.0;

                            double s = sensorInfo->param57; // S (bld correction):
                            double t = sensorInfo->param58; // T (aq correction):
                            double u = sensorInfo->param59; // U (CMean pivot):
                            double w = sensorInfo->param60; // W (DIV thresh):
                            double x = sensorInfo->param61; // X (pO2 theshold):
                            double y = sensorInfo->param62; // Y (DIV/PDIV)2 threshold):
                            double z = sensorInfo->param63; // Z (correction):

                            double a2 = sensorInfo->param65;

                            // for mean-mean calculation. if param14 is 1 use samplemean and calmean
                            // instead of samplex and calex
                            if (sensorInfo->param14 == 1)
                            {
                                sample = sensorLevels->sampleMean;
                                cal = sensorLevels->calMean;
                            }
                            else
                            {
                                sample = sensorLevels->sampleEx;
                                cal = sensorLevels->calEx;
                            }

                            sensorLevels->uncorrectedResponse = CSharpDecimalCalculation(sample / cal);

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
                                    ResultsCalcReturnCode rc2 = CalculateEx(passedReadings->readings, tempSensorInfo2, bubbleDetect, sampleDetect, tempLevels2,
                                                                            true, testMode, false, nullptr, 0, 0, false, false, false, 0.0, 0, 0, testMode, false,
                                                                            passedReadings->sensorType == Sensors::Glucose ? tempSensorInfo2->param47 : tempSensorInfo2->param41, false, nullptr, isBlood);

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

                            // div done using sample window. sampelDiv.
                            originalDiv = sampleDiv = sensorLevels->response = CSharpDecimalCalculation((sample +
                                                                                                            (y1 * sample * sample) +
                                                                                                            (y2 * sample * sample * sample))
                                                                                                        /
                                                                                                        (cal +
                                                                                                            (y1 * cal * cal) +
                                                                                                            (y2 * cal * cal * cal)));

                            double divThreshold = 0.0;

                            if (sensorInfo->sensorType == Sensors::Glucose)
                            {
                                divThreshold = sensorInfo->param11;
                            }
                            else if (sensorInfo->sensorType == Sensors::Lactate)
                            {
                                if (LacHighLactateThresh(sensorInfo->param15))
                                {
                                    divThreshold = sensorInfo->param42;
                                }
                                else
                                {
                                    divThreshold = DBL_MAX;
                                }
                            }

                            // calculate postDiv to be used later
                            postDiv = CSharpDecimalCalculation((post +
                                                                    (y1 * post * post) +
                                                                    (y2 * post * post * post))
                                                                /
                                                                (cal +
                                                                    (y1 * cal * cal) +
                                                                    (y2 * cal * cal * cal)));

                            // if response > high glucose limit then use post window instead of sample..
                            // march 2016.. this applies to both glu & lac, but they use different thresholds.
                            // lactate needs to walk down param42 roads to become a concentration
                            if (sensorLevels->response > divThreshold)
                            {
                                // use the post div instead
                                sensorLevels->response = originalDiv = postDiv;

                                sensorLevels->uncorrectedResponse = CSharpDecimalCalculation(post / cal);

                                // put the original limits back in and send for QC
                                sensorInfo->SampleDriftHighQC = originalSampleDriftHighQC;
                                sensorInfo->PostDriftHighQC = originalPostDriftHighQC;
                                sensorInfo->DeltaDriftHighQC = originalDeltaDriftHighQC;
                                sensorInfo->SampleSecondLowQC = originalSampleSecondLowQC;
                                sensorInfo->SampleDriftLowQC = originalSampleDriftLowQC;
                                sensorInfo->DeltaDriftLowQC = originalDeltaDriftLowQC;

                                if (rc == ResultsCalcReturnCode::Success)
                                {
                                    rc = ValidateAllQC(sensorLevels, sensorInfo, false, false);
                                }

                                // for these specific limits.. we may have to redo the qc because the qc may depend on the sample mean
                                if ((rc == ResultsCalcReturnCode::SampleDriftQCHigh) ||
                                    (rc == ResultsCalcReturnCode::PostDriftQCHigh) ||
                                    (rc == ResultsCalcReturnCode::DeltraDriftHigh) ||
                                    (rc == ResultsCalcReturnCode::SampleSecondQCLow) ||
                                    (rc == ResultsCalcReturnCode::DeltaDriftLow) ||
                                    (rc == ResultsCalcReturnCode::SampleDriftQCLow))
                                {
                                    // set different drift limits if it was a very low mean, and re-do the qc checking.
                                    if (CSharpDecimalCalculation(sensorLevels->postMean / sensorLevels->calMean) >
                                        (passedReadings->sensorType == Sensors::Glucose ?
                                         GluSampleMeanCalMeanHighLimitDifferentLimits :
                                         passedReadings->sensorDescriptor->param11))
                                    {
                                        sensorInfo->SampleDriftHighQC = std::max(1.5, sensorInfo->SampleDriftHighQC);
                                        sensorInfo->PostDriftHighQC = std::max(1.5, sensorInfo->PostDriftHighQC);
                                        sensorInfo->DeltaDriftHighQC = std::max(1.5, sensorInfo->DeltaDriftHighQC);
                                        sensorInfo->SampleSecondLowQC = std::min(-0.15, sensorInfo->SampleSecondLowQC);

                                        // if its glucose, replace these 2 limits with the min of a default value and the existing limit
                                        if (passedReadings->sensorType == Sensors::Glucose)
                                        {
                                            sensorInfo->SampleDriftLowQC = std::min(GlucoseDriftDeltaDriftLow, sensorInfo->SampleDriftLowQC);
                                            sensorInfo->DeltaDriftLowQC = std::min(GlucoseDriftDeltaDriftLow, sensorInfo->DeltaDriftLowQC);
                                        }

                                        rc = ValidateAllQC(sensorLevels, sensorInfo, false, false);
                                    }
                                }
                            }

                            // find divprime based on oxygen correction
                            double divPrime = sensorLevels->response;

                            double oxygenValue;

                            if ((oxygenReadings != nullptr) && !FailedIQC(oxygenReadings->returnCode) && (oxygenReadings->returnCode != ResultsCalcReturnCode::CannotCalculate))
                            {
                                oxygenValue = oxygenReadings->result;
                            }
                            else
                            {
                                // lactate fails if o2 had failed, but lets still proceed
                                if (passedReadings->sensorType == Sensors::Lactate)
                                {
                                    // don't override return code if there was a qc failure
                                    if (rc == ResultsCalcReturnCode::Success)
                                    {
                                        rc = ResultsCalcReturnCode::CannotCalculate;
                                    }

                                    passedReadings->requirementsFailedQC = true;
                                    oxygenValue = oxygenReadings->result;
                                }
                                else
                                {
                                    // glucose continues on with a default value for oxygen
                                    oxygenValue = DefaultOxygenValueForGlu;
                                }
                            }


                            // different a based on div and oxygen value
                            if ((passedReadings->sensorType == Sensors::Lactate) && NewLactatepO2Correction(sensorInfo->param15))
                            {
                                if ((divPrime >= w) && (oxygenValue <= x))
                                {
                                    a_glu = a2;
                                }
                            }

                            // div to div'
                            divPrime = CSharpDecimalCalculation(divPrime * (1.0 + (e_glu * divPrime * divPrime + c_glu * divPrime + b_glu) *
                                                                std::pow(M_E, ((-0.693 / a_glu) * oxygenValue))));

                            // div' to div''
                            if (divPrime <= j_glu)
                            {
                                divPrime = divPrime - (k_glu * oxygenValue);
                            }


                            if (DecimalMax0OldGlucose(sensorInfo->param15))
                            {
                                // only use d2 (< divcut multiplier) if its new Mar 2017 Lactate logic, and div prime < divcut, otherwise use old coefficient d (=d1_glu). backwards compatible
                                if ((sensorInfo->sensorType == Sensors::Lactate) && LacpO2(sensorInfo->param15) && (divPrime < divcut))
                                {
                                    // div'' to div'''
                                    divPrime = DecimalMax0(divPrime - f_glu) * d2_glu * DecimalMax0(sampleDetect - sensorInfo->InjectionTimeOffset) + divPrime;
                                }
                                else
                                {
                                    divPrime = DecimalMax0(divPrime - f_glu) * d1_glu * DecimalMax0(sampleDetect - sensorInfo->InjectionTimeOffset) + divPrime;
                                }

                            }
                            else
                            {
                                // div'' to div'''
                                divPrime = (divPrime - f_glu) * d1_glu * (sampleDetect - sensorInfo->InjectionTimeOffset) + divPrime;
                            }

                            if (DecimalMax0OldGlucose(sensorInfo->param15))
                            {
                                if (oxygenValue < lowpo2Threshold)
                                {
                                    divPrime = DecimalMax0(divPrime - p_glu) * g_glu * DecimalMax0(ageOfCard - sensorInfo->AgeOffset) * (std::pow(divPrime, lowpo2Exponent)) + divPrime;
                                }
                                else
                                {
                                    // div''' to div''''
                                    divPrime = DecimalMax0(divPrime - p_glu) * g_glu * DecimalMax0(ageOfCard - sensorInfo->AgeOffset) + divPrime;
                                }
                            }
                            else
                            {
                                // div''' to div''''
                                divPrime = divPrime * (1.0 + g_glu * divPrime * (ageOfCard - sensorInfo->AgeOffset));
                            }

                            // div'''' to div'''''. redmine 4520
                            if (divPrime >= divCutExp)
                            {
                                divPrime = divPrime * std::pow(M_E, ((divPrime - divCutExp) * l_glu));
                            }

                            // for glucose and lactate.. hct correction
                            double divCutAQSLP = sensorInfo->param33;
                            double LowHctThreshold = sensorInfo->param34;
                            double m_glu = sensorInfo->param35;

                            // only do this if hematocrit hasnt failed iqc, otherwise skip
                            if (!FailedIQC(hctReadings->returnCode) && !hctReadings->requirementsFailedQC)
                            {
                                if ((divPrime < divCutAQSLP) && (hctReadings->result < LowHctThreshold))
                                {
                                    divPrime *= (1.0 - m_glu);
                                }
                            }

                            // div''''' to result
                            if (divPrime >= divcut)
                            {
                                passedReadings->result = CSharpDecimalCalculation(s_high * calConcentrationHigh * ((divPrime - a_high) / (1 - a_high)));

                                if (!LacpO2(sensorInfo->param15) && (passedReadings->sensorType == Sensors::Lactate))
                                {
                                    passedReadings->result = CSharpDecimalCalculation(passedReadings->result / (1 - (rLac * sensorLevels->calSlope)));
                                }
                            }
                            else
                            {
                                passedReadings->result = CSharpDecimalCalculation(s_lo * sensorInfo->calConcentration * ((divPrime - a_low) / (1 - a_low)));
                            }

                            // do this check only for lactate
                            if (passedReadings->sensorType == Sensors::Lactate)
                            {
                                // lactate changes for ES 3.25.0. Redmine 12118
                                if (LacpO2(sensorInfo->param15))
                                {
                                    passedReadings->result = CSharpDecimalCalculation(passedReadings->result / (1 - (rLac * sensorLevels->calSlope)));

                                    if (LactateEarlyInjection(sensorInfo->param15))
                                    {
                                        double YPrime = sensorInfo->param66;      // Y'
                                        double ZPrime = sensorInfo->param67;      // Z'
                                        double EarlyInjCut = sensorInfo->param68; // EarlyInjCut

                                        // correction to result based on sdiv/pdiv
                                        if ((originalDiv >= w) && (oxygenReadings->result <= x) && (CSharpDecimalCalculation((sampleDiv / postDiv) * (sampleDiv / postDiv)) <= y) && (sampleDetect >= EarlyInjCut))
                                        {
                                            passedReadings->result = CSharpDecimalCalculation(passedReadings->result + ((y - ((sampleDiv / postDiv) * (sampleDiv / postDiv))) / z));
                                        }
                                        else if ((originalDiv >= w) && (oxygenReadings->result <= x) && (CSharpDecimalCalculation((sampleDiv / postDiv) * (sampleDiv / postDiv)) <= YPrime) && (sampleDetect < EarlyInjCut))
                                        {
                                            passedReadings->result = CSharpDecimalCalculation(passedReadings->result + ((YPrime - ((sampleDiv / postDiv) * (sampleDiv / postDiv))) / ZPrime));
                                        }
                                    }
                                    else
                                    {
                                        if ((divPrime >= w) && (oxygenReadings->result <= x) && CSharpDecimalCalculation((sampleDiv / postDiv) * (sampleDiv / postDiv)) <= y)
                                        {
                                            passedReadings->result = CSharpDecimalCalculation(passedReadings->result + ((y - ((sampleDiv / postDiv) * (sampleDiv / postDiv))) / z));
                                        }
                                    }

                                    if (isBlood)
                                    {
                                        passedReadings->result = CSharpDecimalCalculation(passedReadings->result / (1.0 - (s * (sensorLevels->calMean - u))));
                                    }
                                    else
                                    {
                                        passedReadings->result = CSharpDecimalCalculation(passedReadings->result / (1.0 - (t * (sensorLevels->calMean - u) / originalDiv)));
                                    }
                                }
                            }
                        }
                        else
                        {
                            // from the old equation
                            double divcut = sensorInfo->param1;
                            double s_lo = sensorInfo->param3;
                            double oxygenDivThreshold = sensorInfo->param2;
                            double y1 = sensorInfo->param4;
                            double y2 = sensorInfo->param5;
                            double a_high = sensorInfo->param6;
                            double a_low = sensorInfo->param7;
                            double s_high = CSharpDecimalCalculation(s_lo * (1 - a_low) * ((divcut - a_high) / ((1 - a_high) * (divcut - a_low))));
                            double calConcentrationHigh = sensorInfo->calConcentration;
                            double j = sensorInfo->param13;
                            double k = sensorInfo->param26;

                            // for po2 @ cal release
                            double injectionTimeCoefficient = sensorInfo->param8;
                            double e_lac = sensorInfo->param9;
                            double oxygenThreshold = sensorInfo->param10;
                            double c_lac = sensorInfo->param22;
                            double f_lac = sensorInfo->param23;
                            double g_lac = sensorInfo->param24;
                            double h_power = sensorInfo->param25;

                            double sample = 0.0;
                            double cal = 0.0;

                            // for mean-mean calculation. if param14 is 1 use samplemean and calmean
                            // instead of samplex and calex
                            if (sensorInfo->param14 == 1)
                            {
                                sample = sensorLevels->sampleMean;
                                cal = sensorLevels->calMean;
                            }
                            else
                            {
                                sample = sensorLevels->sampleEx;
                                cal = sensorLevels->calEx;
                            }

                            sensorLevels->uncorrectedResponse = CSharpDecimalCalculation(sample / cal);

                            sensorLevels->response = CSharpDecimalCalculation((sample +
                                                                                    (y1 * sample * sample) +
                                                                                    (y2 * sample * sample * sample))
                                                                              /
                                                                              (cal +
                                                                                    (y1 * cal * cal) +
                                                                                    (y2 * cal * cal * cal)));

                            // find divprime based on oxygen correction
                            double divPrime = sensorLevels->response;

                            // if oxygen didnt fail qc, apply the oxygen correction to div and make it divprime
                            if ((oxygenReadings != nullptr) && !FailedIQC(oxygenReadings->returnCode))
                            {
                                if (oxygenReadings->result < oxygenThreshold)
                                {
                                    if (divPrime > oxygenDivThreshold)
                                    {
                                        divPrime = divPrime * std::pow(((c_lac * oxygenReadings->result) + e_lac), h_power);
                                    }
                                    else
                                    {
                                        divPrime = divPrime * std::pow(((f_lac * oxygenReadings->result) + g_lac), h_power);
                                    }
                                }

                                if (divPrime <= j)
                                {
                                    divPrime = divPrime - (k * oxygenReadings->result);
                                }
                            }

                            if (divPrime >= divcut)
                            {
                                divPrime = divPrime * (1 + (injectionTimeCoefficient * (sampleDetect - 200)));
                            }

                            if (divPrime >= divcut)
                            {
                                passedReadings->result = CSharpDecimalCalculation(s_high * calConcentrationHigh * ((divPrime - a_high) / (1 - a_high)));
                            }
                            else
                            {
                                passedReadings->result = CSharpDecimalCalculation(s_lo * sensorInfo->calConcentration * ((divPrime - a_low) / (1 - a_low)));
                            }
                        }
                    }

                    if ((std::abs(sensorLevels->postMean - sensorLevels->sampleMean) > sensorInfo->param12) &&
                        (rc == ResultsCalcReturnCode::Success) && PostMeanMinusSampleMean(sensorInfo->param15))
                    {
                        passedReadings->returnCode = rc = ResultsCalcReturnCode::DeltaMeanPostSample;
                    }

                    if ((co2Readings != nullptr) &&
                        CalFailedIQC(co2Readings->returnCode) &&
                        (rc == ResultsCalcReturnCode::Success))
                    {
                        rc = passedReadings->returnCode = ResultsCalcReturnCode::CannotCalculate;
                        passedReadings->requirementsFailedQC = true;
                    }

                    // 3.5.0 glu no longer depends on oxygen
                    // if oxygen fails cal mean qc high, then glu is cnc. some new requirement from john k and anca
                    if ((oxygenReadings != nullptr) && (FailedIQC(oxygenReadings->returnCode) || oxygenReadings->requirementsFailedQC)
                        && (rc == ResultsCalcReturnCode::Success))
                    {
                        // do this check only for lactate
                        if (passedReadings->sensorType != Sensors::Glucose)
                        {
                            rc = passedReadings->returnCode = ResultsCalcReturnCode::CannotCalculate;
                            passedReadings->requirementsFailedQC = true;
                        }
                    }

                    // if window size is less than 0.1 (backwards compatibility for testconfigs that have this param set to 0)
                    if (sensorInfo->param19 > 0.1)
                    {
                        double meanOfISampleWindow = GetMean(passedReadings->readings,
                                                             sampleDetect + sensorInfo->param18, // window start measured from sample detect
                                                             sensorInfo->param19); // window size

                        //sensorLevels->output4 = 0.0;

                        // there is a case where there are no points in the window
                        if (meanOfISampleWindow != DBL_MAX)
                        {
                            if (sensorLevels->calMean != 0)
                            {
                                // divide isample by ical to get value to check against the limits
                                meanOfISampleWindow = CSharpDecimalCalculation(meanOfISampleWindow / sensorLevels->calMean);
                            }

                            //sensorLevels->output4 = meanOfISampleWindow;

                            if ((rc == ResultsCalcReturnCode::Success) && (meanOfISampleWindow < sensorInfo->param20)) // param20 low limit
                            {
                                rc = passedReadings->returnCode = ResultsCalcReturnCode::PostSecondQCLow;
                            }
                            else if ((rc == ResultsCalcReturnCode::Success) && (meanOfISampleWindow > sensorInfo->param21)) // param21 high limit
                            {
                                rc = passedReadings->returnCode = ResultsCalcReturnCode::PostSecondQCHigh;
                            }
                        }
                    }

                    if (rc == ResultsCalcReturnCode::Success)
                    {
                        if (passedReadings->sensorType == Sensors::Glucose)
                        {
                            rc = CheckAllPointsWithinLimits(passedReadings, sensorLevels->sampleFirst, sensorLevels->sampleLast, sensorInfo->param45, sensorInfo->param46);
                        }
                        else if (passedReadings->sensorType == Sensors::Lactate)
                        {
                            rc = CheckAllPointsWithinLimits(passedReadings, sensorLevels->sampleFirst, sensorLevels->sampleLast, sensorInfo->param39, sensorInfo->param40);
                        }
                    }

                    if (rc == ResultsCalcReturnCode::Success)
                    {
                        passedReadings->returnCode = rc = ValidateInsanityRangeOnlyReturnCode(passedReadings->result,
                                                                                              testMode == TestMode::BloodTest ? passedReadings->insanityLow : passedReadings->insanityQALow,
                                                                                              testMode == TestMode::BloodTest ? passedReadings->insanityHigh : passedReadings->insanityQAHigh);
                    }

                    // turn passed return code to failed qc ever if realtime qc failed during the test and there were no other failures
                    if ((rc == ResultsCalcReturnCode::Success) && (passedReadings->realTimeQCFailedEver == RealTimeQCReturnCode::Failed))
                    {
                        passedReadings->returnCode = rc = ResultsCalcReturnCode::FailedQCEver;
                    }

                    if ((rc == ResultsCalcReturnCode::Success) && (testMode == TestMode::BloodTest))
                    {
                        passedReadings->returnCode = ValidateReportableRangeOnlyReturnCode(passedReadings->result, passedReadings->reportableLow, passedReadings->reportableHigh);
                    }
                    else
                    {
                        passedReadings->returnCode = rc;
                    }

                    if (!allowNegativeValues)
                    {
                        // Do not display values less than zero
                        if (passedReadings->result < 0)
                        {
                            passedReadings->result = 0;
                        }
                    }

                    return passedReadings->returnCode;
                }

                ResultsCalcReturnCode AnalyticalManager::CalculateCreatinine(std::shared_ptr<SensorReadings> creatinineReadings,
                                                                             double bubbleDetect,
                                                                             double sampleDetect,
                                                                             TestMode testMode,
                                                                             std::shared_ptr<SensorReadings> topHeaterReadings,
                                                                             int ageOfCard,
                                                                             std::shared_ptr<SensorReadings> oxygenReadings,
                                                                             std::shared_ptr<SensorReadings> potassiumReadings,
                                                                             std::shared_ptr<SensorReadings> co2Readings,
                                                                             std::shared_ptr<SensorReadings> hctReadings,
                                                                             double actualBicarb,
                                                                             int sibVersion,
                                                                             bool isBlood,
                                                                             double ambientPressure,
                                                                             bool allowNegativeValues)
                {
                    ResultsCalcReturnCode rc;
                    std::shared_ptr<SensorInfo> sensorInfo = creatinineReadings->sensorDescriptor;
                    std::shared_ptr<Levels> sensorLevels = creatinineReadings->levels;
                    auto tempSensorInfo = std::make_shared<SensorInfo>();
                    double postDriftLow = sensorInfo->PostDriftLowQC;
                    double postDriftHigh = sensorInfo->PostDriftHighQC;

                    // copy the old limits to a temporary object
                    CopySensorInfo(tempSensorInfo, sensorInfo);

                    UpdateCreaLimits(sensorInfo, ageOfCard, sampleDetect);

                    rc = CalculateEx(creatinineReadings->readings, sensorInfo, bubbleDetect, sampleDetect, sensorLevels,
                                     true, testMode, false, nullptr, 0, 0, false, false, false, 0, 0, 0, testMode,
                                     NewCreaQC(sensorInfo->param15) ? true : false, sensorInfo->param69, oxygenReadings->levels->calWindowMovedBack != 0, nullptr, isBlood);

                    // put the old limits back from the temporary object
                    CopySensorInfo(sensorInfo, tempSensorInfo);

                    sensorLevels->peakEx = sensorInfo->PostSecondLowQC;
                    sensorLevels->peakMean = sensorInfo->PostSecondHighQC;
                    sensorLevels->peakNoise = sensorLevels->postSecond;
                    sensorLevels->peakSlope = (double)rc;

                    double concentration = 0.0;

                    if ((rc != ResultsCalcReturnCode::CannotCalculate) && !FailedIQC(rc))
                    {
                        if (creatinineReadings->realTimeQCPassed == RealTimeQCReturnCode::CreaEarlyWindowLow)
                        {
                            rc = ResultsCalcReturnCode::CreaEarlyWindowLow;
                        }
                        else if (creatinineReadings->realTimeQCPassed == RealTimeQCReturnCode::CreaEarlyWindowHigh)
                        {
                            rc = ResultsCalcReturnCode::CreaEarlyWindowHigh;
                        }
                    }

                    if (rc != ResultsCalcReturnCode::CannotCalculate)
                    {
                        double o2pivot = sensorInfo->param1;
                        double o2divconvergence = sensorInfo->param2;
                        double o2divPower = sensorInfo->param2;
                        double o2concconvergence = sensorInfo->param3;
                        double o2divSlope = sensorInfo->param3;
                        double bicarbPivot = sensorInfo->param4;
                        double bicarbDivConvergence = sensorInfo->param5;
                        double bicarbDivPower = sensorInfo->param5;
                        double bicarbConcConvergence = sensorInfo->param6;
                        double bicarbDivSlope = sensorInfo->param6;
                        double hctPivot = sensorInfo->param7;
                        double hctDivConvergence = sensorInfo->param8;
                        double hctConcConvergence = sensorInfo->param9;
                        double injTimePivot = sensorInfo->param10;
                        double injTimeDivConvergence = sensorInfo->param11;
                        double injTimeConcConvergence = sensorInfo->param12;
                        double nonLinDivCut = sensorInfo->param13;
                        double nonLinC = sensorInfo->param22;
                        double nonLinY0 = sensorInfo->param23;
                        double nonLinY1 = sensorInfo->param24;
                        double nonLinY2 = sensorInfo->param26;
                        double alphaO2 = sensorInfo->param26;
                        double nonLinLowC = sensorInfo->param27;
                        double sampleDriftLimit = sensorInfo->param28;
                        double postDriftLimit = sensorInfo->param29;
                        double sampleMeanLimit = sensorInfo->param31;
                        double postCorrectionInt = sensorInfo->param32;
                        double postCorrectionSlope = sensorInfo->param33;
                        double nonLinLowY0 = sensorInfo->param34;
                        double nonLinLowY1 = sensorInfo->param35;
                        double nonLinLowY2 = sensorInfo->param36;
                        double betaO2 = sensorInfo->param36;
                        double qcSlope = sensorInfo->param37;
                        double qcSlopeLow = sensorInfo->param38;
                        double QCHctThresh = sensorInfo->param39;
                        double CalDivCutAq = sensorInfo->param40;
                        double CalOffsetAqLow = sensorInfo->param41;
                        double CalInterceptAqLow = sensorInfo->param42;
                        double CalSlopeAqLow = sensorInfo->param43;
                        double highPivot = sensorInfo->param44;
                        double alphaBicarb = sensorInfo->param44;
                        double highSlope = sensorInfo->param45;
                        double betaBicarb = sensorInfo->param45;
                        double CalOffsetAqHigh = sensorInfo->param46;
                        double CalInterceptAqHigh = sensorInfo->param47;
                        double CalSlopeAqHigh = sensorInfo->param48;
                        double CalDivCutBlood = sensorInfo->param51;
                        double AgeConcInterceptLow1Aq = sensorInfo->param52;
                        double AgeConcInterceptLow2Aq = sensorInfo->param53;
                        double CalOffsetBloodLow = sensorInfo->param54;
                        double CalInterceptBloodLow = sensorInfo->param55;
                        double AgeConcInterceptHigh1Aq = sensorInfo->param56;
                        double CalSlopeBloodLow = sensorInfo->param57;
                        double AgeConcInterceptHigh2Aq = sensorInfo->param58;
                        double CalOffsetBloodHigh = sensorInfo->param59;
                        double AgeConcInterceptLow1 = sensorInfo->param60;
                        double AgeConcInterceptLow2 = sensorInfo->param61;
                        double CalInterceptBloodHigh = sensorInfo->param62;
                        double CalSlopeBloodHigh = sensorInfo->param63;
                        double AgeConcInterceptHigh1 = sensorInfo->param64;
                        double CalAgeCutoff = sensorInfo->param65;
                        double AgeConcInterceptHigh2 = sensorInfo->param66;
                        double AgeSlopeLow1 = sensorInfo->param41;
                        double AgeSlopeLow2 = sensorInfo->param42;
                        double AgeSlopeLow1Aq = sensorInfo->param47;
                        double AgeSlopeLow2Aq = sensorInfo->param48;
                        double o2ConcThreshold = sensorInfo->param76;
                        double o2ReductionSlope = sensorInfo->param77;
                        double HctDivPower = sensorInfo->param78;
                        double HctDivSlope = sensorInfo->param79;
                        double AgeCutoffTemp = sensorInfo->param80;
                        double HighDivLimit = sensorInfo->param81;
                        double PressureCoeffTemp = sensorInfo->param82;
                        double SecondOrderCoeffTempO2iCal = sensorInfo->param83;
                        double FirstOrderCoeffTempO2iCal = sensorInfo->param84;
                        double InterceptTempO2ICal = sensorInfo->param85;
                        double DeltaO2iCalThresholdTemp = sensorInfo->param86;
                        double OffsetSlopeTempAq = sensorInfo->param87;
                        double OffsetInterceptTempAq = sensorInfo->param88;
                        double FirstOrderCoeffTempAq30C = sensorInfo->param89;
                        double InterceptTempAQ30C = sensorInfo->param90;
                        double FirstOrderCoeffTempAQRT = sensorInfo->param91;
                        double InterceptTempAQRT = sensorInfo->param92;
                        double OffsetSlopeTempBlood = sensorInfo->param94;
                        double OffsetInterceptTempBlood = sensorInfo->param95;
                        double FirstOrderCoeffTempBlood30C = sensorInfo->param96;
                        double InterceptTempBlood30C = sensorInfo->param97;
                        double FirstOrderCoeffTempBloodRT = sensorInfo->param98;
                        double InterceptTempBloodRT = sensorInfo->param99;
                        double Z2InjTimeAQ = sensorInfo->param100;
                        double Z1InjTimeAQ = sensorInfo->param26; // previously alphao2
                        double Z0InjTimeAQ = sensorInfo->param36; // previously betao2
                        double W2InjTimeAQ = sensorInfo->param52; // previously AgeConcInterceptLow1Aq
                        double W1InjTimeAQ = sensorInfo->param53; // previously AgeConcInterceptLow2Aq
                        double W0InjTimeAQ = sensorInfo->param56; // previously AgeConcInterceptHigh1Aq
                        double Z2InjTimeBlood = sensorInfo->param58; // previously AgeConcInterceptHigh2Aq
                        double Z1InjTimeBlood = sensorInfo->param60; // previously ageConcInterceptLow1
                        double Z0InjTimeBlood = sensorInfo->param61; // previously ageConcInterceptLow2
                        double W2InjTimeBlood = sensorInfo->param64; // previously ageConcInterceptHigh1
                        double W1InjTimeBlood = sensorInfo->param66; // previously ageConcInterceptHigh2
                        double W0InjTimeBlood = sensorInfo->param45; // previously betabicarb
                        double QCOffset = sensorInfo->param67; // previously low limit for zero amp detection. we now use oxygen's limit, which is the same
                        double QCOffsetLow = sensorInfo->param68; // previously high limit for zero amp detection. we now use oxygen's limit, which is the same
                        double SampleConcThreshold = sensorInfo->A;
                        double SampleDriftIntercept = sensorInfo->B;
                        double SampleDriftSlope = sensorInfo->C;
                        double FWindowStart = sensorInfo->D;
                        double FWindowSize = sensorInfo->F;
                        double Z2O2 = sensorInfo->param2;
                        double Z1O2 = sensorInfo->param3;
                        double Z0O2 = sensorInfo->param5;
                        double W2O2 = sensorInfo->param6;
                        double W1O2 = sensorInfo->param8;
                        double W0O2 = sensorInfo->param9;
                        double Z2HCO3 = sensorInfo->param11;
                        double Z1HCO3 = sensorInfo->param12;
                        double Z0HCO3 = sensorInfo->G;
                        double W2HCO3 = sensorInfo->TAmbOffset;
                        double W1HCO3 = sensorInfo->InjectionTimeOffset;
                        double W0HCO3 = sensorInfo->AgeOffset;
                        double Z2Hct = sensorInfo->PowerOffset;
                        double Z1Hct = sensorInfo->param44;
                        double Z0Hct = sensorInfo->param76;
                        double W2Hct = sensorInfo->param77;
                        double W1Hct = sensorInfo->param78;
                        double W0Hct = sensorInfo->param79;

                        // march 2016 mandatory 27.n
                        double SampleConcLowThreshold = 1;//sensorInfo->tPlus;
                        double CalDriftFactor = 1.6;// sensorInfo->tMinus;

                        bool creaSep2014 = CreaSeptember2014Equation(sensorInfo->param15);
                        bool creaFlagC = CreaFlagC(sensorInfo->param15);

                        double CutiC1 = sensorInfo->param40;   //CutiC1
                        double CutiC2 = sensorInfo->param41;   //CutiC2
                        double CutiC3 = sensorInfo->param42;   //CutiC3
                        double CutiC4 = sensorInfo->param43;   //CutiC4
                        double iCCoeffHi = sensorInfo->param46;    //iCCoeffHi
                        double iCCoeffLo = sensorInfo->param47;    //iCCoeffLo
                        double CutConciCHi = sensorInfo->param48;  //CutConciCHi
                        double CutConciCLo = sensorInfo->param51;      //CutConciCLo
                        double iCalL1 = sensorInfo->param54;   //iCalL1
                        double iCalL2 = sensorInfo->param55;   //iCalL2

                        double EarlyAgeConc = sensorInfo->param57;
                        double EarlyAgeCut = sensorInfo->param59;
                        double EarlyAgeCorr = sensorInfo->param62;

                        if (creaSep2014)
                        {
                            alphaO2 = 0.0;
                            betaO2 = 0.0;
                            alphaBicarb = 0.0;
                            betaBicarb = 0.0;
                            AgeConcInterceptLow1Aq = 0.0;
                            AgeConcInterceptLow2Aq = 0.0;
                            AgeConcInterceptHigh1Aq = 0.0;
                            AgeConcInterceptHigh2Aq = 0.0;
                            AgeConcInterceptLow1 = 0.0;
                            AgeConcInterceptLow2 = 0.0;
                            AgeConcInterceptHigh1 = 0.0;
                            AgeConcInterceptHigh2 = 0.0;
                        }

                        double calDriftDivCalExLow = postDriftLow;
                        double calDriftDivCalExHigh = postDriftHigh;

                        double div = 0.0;
                        double o2Concentration;
                        double hctConcentration;

                        ResultsCalcReturnCode hematocritReturnCode = ResultsCalcReturnCode::Success;
                        bool hematocritRequirementsFailedQC = false;
                        double hctResult = 0.0;

                        ResultsCalcReturnCode hematocritEstReturnCode = ResultsCalcReturnCode::Success;
                        bool hematocritEstRequirementsFailedQC = false;
                        double hctEstResult = 0.0;

                        if (hctReadings != nullptr)
                        {
                            sensorLevels->output9 = hctEstResult = hctReadings->levels->output1;
                            hematocritEstReturnCode = (ResultsCalcReturnCode)(int)hctReadings->levels->output2;
                            hematocritEstRequirementsFailedQC = hctReadings->levels->output3 == 0.0 ? false : true;

                            if (testMode == TestMode::QA)
                            {
                                // use hct-est for qa mode. these are stored in hct outputs
                                hctEstResult = 0.0;
                                hctResult = 0.0;
                                hematocritReturnCode = ResultsCalcReturnCode::Success;
                                hematocritRequirementsFailedQC = false;
                                hematocritEstReturnCode = ResultsCalcReturnCode::Success;
                                hematocritEstRequirementsFailedQC = false;
                            }
                            else
                            {
                                hctResult = hctReadings->result;
                                hematocritReturnCode = hctReadings->returnCode;
                                hematocritRequirementsFailedQC = hctReadings->requirementsFailedQC;
                            }
                        }

                        // if qa mode, result is 0 and dont worry about return code
                        if (testMode == TestMode::QA)
                        {
                            hctConcentration = hctResult;
                        }
                        else
                        {
                            if ((hctReadings == nullptr) || FailedIQC(hematocritReturnCode) || hematocritRequirementsFailedQC)
                            {
                                hctConcentration = hctPivot;

                                // if no hct then cnc. redmine 7596
                                if (rc == ResultsCalcReturnCode::Success)
                                {
                                    rc = ResultsCalcReturnCode::CannotCalculate;
                                }
                            }
                            else
                            {
                                // use 0 if hct < 0
                                if (hctResult >= 0)
                                {
                                    hctConcentration = hctResult;
                                }
                                else
                                {
                                    hctConcentration = 0.0;
                                }
                            }
                        }

                        // if oxygen can be used as input: Not (failed iqc or reportable low or reportable high)
                        // requirement states testmode and not fluid type - req needs updating
                        if (isBlood)
                        {
                            if (FailedIQC(oxygenReadings->returnCode) || oxygenReadings->requirementsFailedQC ||
                                (oxygenReadings->result < po2DeviceReportableLow) || (oxygenReadings->result > po2DeviceReportableHigh))
                            {
                                if (rc == ResultsCalcReturnCode::Success)
                                {
                                    rc = creatinineReadings->returnCode = ResultsCalcReturnCode::CannotCalculate;
                                }

                                o2Concentration = 200;
                            }
                            else
                            {
                                o2Concentration = oxygenReadings->result;
                            }
                        }
                        else
                        {
                            if (FailedIQC(oxygenReadings->returnCode) || oxygenReadings->requirementsFailedQC)
                            {
                                if (rc == ResultsCalcReturnCode::Success)
                                {
                                    rc = creatinineReadings->returnCode = ResultsCalcReturnCode::CannotCalculate;
                                }

                                o2Concentration = 200;
                            }
                            else
                            {
                                o2Concentration = oxygenReadings->result;
                            }
                        }

                        // recalculate sample ex if necessary
                        if ((sensorLevels->sampleSlope > sampleDriftLimit) && (sensorLevels->postSlope < postDriftLimit))
                        {
                            sensorLevels->sampleEx = (postCorrectionSlope * sensorLevels->postSlope) + postCorrectionInt;
                        }

                        //Implement f-factor for all sensors
                        double sample, cal;
                        sample = 0.0;
                        cal = 0.0;
                        // for mean-mean calculation. if param14 is 1 use samplemean and calmean
                        // instead of samplex and calex
                        if (CreatinineMeanMean(sensorInfo->param15))
                        {
                            sample = sensorLevels->sampleMean;
                            cal = sensorLevels->calMean;
                        }
                        else
                        {
                            sample = sensorLevels->sampleEx;
                            cal = sensorLevels->calEx;
                        }

                        // response same as uncorrected response for now
                        div = sensorLevels->response = sensorLevels->uncorrectedResponse = CSharpDecimalCalculation(sample / cal);

                        try
                        {
                            if (FWindowSize != 0)
                            {
                                auto tempSensorInfo2 = std::make_shared<SensorInfo>();
                                CopySensorInfo(tempSensorInfo2, sensorInfo);
                                tempSensorInfo2->sampleWindowSize = (double)FWindowSize;
                                tempSensorInfo2->sampleDelimit = (double)FWindowStart;

                                auto tempLevels2 = std::make_shared<Levels>();
                                ResultsCalcReturnCode rc2 = CalculateEx(creatinineReadings->readings, tempSensorInfo2, bubbleDetect, sampleDetect, tempLevels2,
                                                                        true, testMode, false, nullptr, 0, 0, false, false, false, 0, 0, 0, testMode,
                                                                        NewCreaQC(tempSensorInfo2->param15) ? true : false, tempSensorInfo2->param69, oxygenReadings->levels->calWindowMovedBack != 0, nullptr, isBlood);

                                // calculate f parameter for AT output
                                sensorLevels->output11 = tempLevels2->calEx;
                                sensorLevels->output12 = tempLevels2->sampleMean;
                                sensorLevels->output13 = tempLevels2->sampleNoise;
                                sensorLevels->output14 = tempLevels2->sampleSlope;

                                double f = CSharpDecimalCalculation((sample - cal) / (tempLevels2->sampleMean - cal));
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


                        // if we failed for this specific return code... set the limit higher and go back through calculateex
                        // it may or may not fail for this limit again.. but if it passes, it will be overreportablerange
                        // instead of failediqc. if it fails again, then it just gives failediqc
                        if ((div > HighDivLimit) && (rc == ResultsCalcReturnCode::SampleDipLate))
                        {
                            // copy the old limits to a temporary object
                            CopySensorInfo(tempSensorInfo, sensorInfo);

                            UpdateCreaLimits(sensorInfo, ageOfCard, sampleDetect);

                            sensorInfo->param74 = sensorInfo->param72;

                            rc = CalculateEx(creatinineReadings->readings, sensorInfo, bubbleDetect, sampleDetect, sensorLevels,
                                             true, testMode, false, nullptr, 0, 0, false, false, false, 0, 0, 0, testMode,
                                             NewCreaQC(sensorInfo->param15) ? true : false, sensorInfo->param69, oxygenReadings->levels->calWindowMovedBack != 0, nullptr, isBlood);

                            // put the old limits back from the temporary object
                            CopySensorInfo(sensorInfo, tempSensorInfo);
                        }

                        // step 4 from SRS-RCIQC
                        div = div - (((Z2O2 * div * div) + Z1O2 * div + Z0O2) * (o2Concentration - o2pivot) * (o2Concentration - o2pivot) +
                                     (W2O2 * div * div + W1O2 * div + W0O2) * (o2Concentration - o2pivot));

                        // bicardb correction. step 5.
                        if (!isnan(actualBicarb))
                        {
                            div = div - (((Z2HCO3 * div * div) + Z1HCO3 * div + Z0HCO3) * (actualBicarb - bicarbPivot) * (actualBicarb - bicarbPivot) +
                                         (W2HCO3 * div * div + W1HCO3 * div + W0HCO3) * (actualBicarb - bicarbPivot));
                        }
                        else
                        {
                            // if no bicarb then cnc. redmine 7596
                            if (rc == ResultsCalcReturnCode::Success)
                            {
                                rc = ResultsCalcReturnCode::CannotCalculate;
                            }
                        }


                        // hctconcentration = hct-est in qa mode and hct in blood mode. step 6 from srs.
                        div = div - (((Z2Hct * div * div) + Z1Hct * div + Z0Hct) * (hctConcentration - hctPivot) * (hctConcentration - hctPivot) +
                                     (W2Hct * div * div + W1Hct * div + W0Hct) * (hctConcentration - hctPivot));


                        if (ageOfCard > AgeCutoffTemp)
                        {
                            if (hctEstResult < QCHctThresh)
                            {
                                if (CSharpDecimalCalculation((oxygenReadings->levels->calMean + PressureCoeffTemp * ((ambientPressure / mmHgtoKpa) - 760)) -
                                    (SecondOrderCoeffTempO2iCal * ageOfCard * ageOfCard + FirstOrderCoeffTempO2iCal * ageOfCard +
                                     InterceptTempO2ICal)) > DeltaO2iCalThresholdTemp)
                                {
                                    div = (div * (1 + OffsetSlopeTempAq) + OffsetInterceptTempAq) - ageOfCard *
                                                                                                    (FirstOrderCoeffTempAq30C * div + InterceptTempAQ30C);
                                }
                                else
                                {
                                    div = div - ageOfCard * (FirstOrderCoeffTempAQRT * div + InterceptTempAQRT);
                                }
                            }
                            else
                            {
                                // hct est over threshold
                                if (CSharpDecimalCalculation((oxygenReadings->levels->calMean + PressureCoeffTemp * ((ambientPressure / mmHgtoKpa) - 760)) -
                                    (SecondOrderCoeffTempO2iCal * ageOfCard * ageOfCard + FirstOrderCoeffTempO2iCal * ageOfCard +
                                     InterceptTempO2ICal)) > DeltaO2iCalThresholdTemp)
                                {
                                    div = (div * (1 + OffsetSlopeTempBlood) + OffsetInterceptTempBlood) - ageOfCard *
                                                                                                          (FirstOrderCoeffTempBlood30C * div + InterceptTempBlood30C);
                                }
                                else
                                {
                                    div = div - ageOfCard * (FirstOrderCoeffTempBloodRT * div + InterceptTempBloodRT);
                                }
                            }
                        }

                        if (hctEstResult < QCHctThresh)
                        {
                            div = div - ((Z2InjTimeAQ * div * div + Z1InjTimeAQ * div + Z0InjTimeAQ) *
                                         ((sampleDetect - injTimePivot) * (sampleDetect - injTimePivot)) +
                                         (W2InjTimeAQ * div * div + W1InjTimeAQ * div + W0InjTimeAQ) * (sampleDetect - injTimePivot));
                        }
                        else
                        {
                            div = div - ((Z2InjTimeBlood * div * div + Z1InjTimeBlood * div + Z0InjTimeBlood) *
                                         ((sampleDetect - injTimePivot) * (sampleDetect - injTimePivot)) +
                                         (W2InjTimeBlood * div * div + W1InjTimeBlood * div + W0InjTimeBlood) * (sampleDetect - injTimePivot));
                        }

                        if (div > nonLinDivCut)
                        {
                            concentration = div + nonLinC + (nonLinY0 * div) + (nonLinY1 * div * div);
                        }
                        else
                        {
                            concentration = div + nonLinLowC + (nonLinLowY0 * div) + (nonLinLowY1 * div * div);
                        }

                        if (!(FailedIQC(hematocritEstReturnCode) || hematocritEstRequirementsFailedQC))
                        {
                            if (hctEstResult < QCHctThresh)
                            {
                                if (concentration > nonLinDivCut)
                                {
                                    concentration = qcSlope * (concentration + QCOffset);
                                }
                                else
                                {
                                    concentration = qcSlopeLow * (concentration + QCOffsetLow);
                                }
                            }
                        }

                        if (CreaFinalAgeCorrect(sensorInfo->param15))
                        {
                            double cutCPower = (CutiC1 * ageOfCard * ageOfCard * ageOfCard) + (CutiC2 * ageOfCard * ageOfCard) + (CutiC3 * ageOfCard) + CutiC4;
                            if ((cal > (cutCPower)) && (concentration > CutConciCHi))
                            {
                                concentration = concentration + iCCoeffHi * (cal - (cutCPower)) * (concentration - CutConciCHi);
                            }
                            else if ((cal > (cutCPower)) && (concentration < CutConciCLo))
                            {
                                concentration = concentration + iCCoeffLo * ((cutCPower)-cal) * (CutConciCLo - concentration);
                            }
                        }

                        if (concentration >= EarlyAgeConc && ageOfCard <= EarlyAgeCut)
                        {
                            concentration = concentration + EarlyAgeCorr * (EarlyAgeCut - ageOfCard);
                        }

                        if (rc == ResultsCalcReturnCode::Success)
                        {
                            if (CSharpDecimalCalculation(sensorLevels->calSlope / sensorLevels->calEx) < calDriftDivCalExLow)
                            {
                                rc = ResultsCalcReturnCode::GenericQCLow;
                            }
                            else if (CSharpDecimalCalculation(sensorLevels->calSlope / sensorLevels->calEx) > calDriftDivCalExHigh)
                            {
                                rc = ResultsCalcReturnCode::GenericQCHigh;
                            }
                        }

                        if ((co2Readings != nullptr) && CalFailedIQC(co2Readings->returnCode) &&
                            (rc == ResultsCalcReturnCode::Success))
                        {
                            rc = creatinineReadings->returnCode = ResultsCalcReturnCode::CannotCalculate;
                            creatinineReadings->requirementsFailedQC = true;
                        }

                        if (!NewCreaQC(sensorInfo->param15))
                        {
                            if (rc == ResultsCalcReturnCode::Success)
                            {
                                if ((sensorLevels->additionalMean - sensorLevels->calMean) < 0.02)
                                {
                                    if (sensorLevels->calEx > -0.4)
                                    {
                                        if ((sensorLevels->calSlope > -0.002) && (sensorLevels->calSlope < 0.002))
                                        {
                                            rc = ResultsCalcReturnCode::CreaDriftFailure;
                                        }
                                    }
                                }
                            }
                        }
                        else
                        {
                            // amean - calex 4.1.2.1.11.1
                            double additionalMinusCalExLow = sensorInfo->DeltaDriftLowQC;

                            if (rc == ResultsCalcReturnCode::Success)
                            {
                                if ((sensorLevels->additionalMean - sensorLevels->calEx) < additionalMinusCalExLow)
                                {
                                    rc = ResultsCalcReturnCode::CreaDriftFailure;
                                }
                            }
                        }

                        if (rc == ResultsCalcReturnCode::Success)
                        {
                            rc = CheckAllPointsWithinLimits(creatinineReadings, sensorLevels->sampleFirst, sensorLevels->sampleLast, oxygenReadings->sensorDescriptor->param34, oxygenReadings->sensorDescriptor->param35);
                        }

                        if (!allowNegativeValues)
                        {
                            // Do not display values less than zero
                            if (concentration < 0)
                            {
                                concentration = 0;
                            }
                        }

                        //crea sample bubble detection
                        //if new formula is applied, don't use this old one.
                        // crea cal drift check march 2016 mandatory
                        if ((rc == ResultsCalcReturnCode::Success) && CreaCalDrift(sensorInfo->param15))
                        {
                            if ((concentration < SampleConcThreshold) && (concentration > SampleConcLowThreshold))
                            {
                                if (CSharpDecimalCalculation(sensorLevels->sampleSlope - (sensorLevels->calSlope / CalDriftFactor)) >
                                    CSharpDecimalCalculation(SampleDriftIntercept + (concentration * SampleDriftSlope)))
                                {
                                    rc = ResultsCalcReturnCode::CreaSampleBubble;
                                }
                            }
                        }
                        else if (!CreaCalDrift(sensorInfo->param15))
                        {
                            if (concentration < SampleConcThreshold)
                            {
                                if (sensorLevels->sampleSlope > SampleDriftIntercept + concentration * SampleDriftSlope)
                                {
                                    if (rc == ResultsCalcReturnCode::Success)
                                    {
                                        rc = ResultsCalcReturnCode::CreaSampleBubble;
                                    }
                                }
                            }
                        }

                        creatinineReadings->result = CSharpDecimalCalculation(concentration);

                        if (rc == ResultsCalcReturnCode::Success)
                        {
                            creatinineReadings->returnCode = rc = ValidateInsanityRangeOnlyReturnCode(creatinineReadings->result,
                                                                                                      testMode == TestMode::BloodTest ? creatinineReadings->insanityLow : creatinineReadings->insanityQALow,
                                                                                                      testMode == TestMode::BloodTest ? creatinineReadings->insanityHigh : creatinineReadings->insanityQAHigh);
                        }

                        // change crea rc to failedqcever if crea failed realtime qc
                        if ((rc == ResultsCalcReturnCode::Success) && (creatinineReadings->realTimeQCFailedEver == RealTimeQCReturnCode::Failed))
                        {
                            creatinineReadings->returnCode = rc = ResultsCalcReturnCode::FailedQCEver;
                        }

                        if ((rc == ResultsCalcReturnCode::Success) && (testMode == TestMode::BloodTest))
                        {
                            creatinineReadings->returnCode = rc = ValidateReportableRangeOnlyReturnCode(
                                                                             creatinineReadings->result,
                                                                             creatinineReadings->reportableLow,
                                                                             creatinineReadings->reportableHigh);
                        }
                    }

                    // Only perform the Crea Neural Net calculations if we're __not__ running on the EPOCAL_TARGET_HOST (i.e. Host)
#ifndef EPOCAL_TARGET_HOST
                    // Use the Neural Net Coeffs to calculate Crea's Conc. Adjusted
                    double nnCreaConcAdjusted = NAN;

                    double resultHct = (hctReadings->result < 0 || testMode == TestMode::QA) ? 0 : hctReadings->result;
                    double resultOxygen = oxygenReadings->result;

                    std::shared_ptr<NeuralNetCoeff> nnCreaCoeffAQ = GetNeuralNetCreaCoeff(creatinineReadings->sensorDescriptor->NeuralNetAQ, sensorLevels, sampleDetect, bubbleDetect, topHeaterReadings, ageOfCard, resultHct, resultOxygen, actualBicarb);
                    std::shared_ptr<NeuralNetCoeff> nnCreaCoeffBlood = GetNeuralNetCreaCoeff(creatinineReadings->sensorDescriptor->NeuralNetBlood, sensorLevels, sampleDetect, bubbleDetect, topHeaterReadings, ageOfCard, resultHct, resultOxygen, actualBicarb);

                    if (((nnCreaCoeffAQ != nullptr && nnCreaCoeffAQ->ParsingErrorCode() == NeuralNetCoeff::ParsingCoeffsErrorCode::SUCCESS)) &&
                        ((nnCreaCoeffBlood != nullptr && nnCreaCoeffBlood->ParsingErrorCode() == NeuralNetCoeff::ParsingCoeffsErrorCode::SUCCESS)))
                    {
                        // We have successfully parsed coefficients for both Aqueous(Mixed) and Blood... proceed with the Neural Net calculations...
                        double nnCreaConcAdjustedMixed = nnCreaCoeffAQ->Calculate();
                        if (!isnan(nnCreaConcAdjustedMixed))
                        {
                            // If TestMode = BloodTest and ConcAdjustedMixed < ConcCutBlood, then recalculate using the BloodOnlyModel
                            if ((testMode == TestMode::BloodTest) && (nnCreaConcAdjustedMixed < nnCreaCoeffBlood->concCut))
                            {
                                nnCreaConcAdjusted = nnCreaCoeffBlood->Calculate();
                            }
                            else
                            {
                                // Use the Mixed result
                                nnCreaConcAdjusted = nnCreaConcAdjustedMixed;
                            }
                        }
                    }

                    // Only set output 20 if there were no Neural Net calculation errors (i.e. nnCreaConcAdjusted != NaN)
                    if (!isnan(nnCreaConcAdjusted))
                    {
                        sensorLevels->output20 = CSharpDecimalCalculation(nnCreaConcAdjusted);
                    }
#endif
                    sensorLevels->output19 = CSharpDecimalCalculation(creatinineReadings->result);

                    return creatinineReadings->returnCode = rc;
                }

                std::shared_ptr<NeuralNetCoeff> AnalyticalManager::GetNeuralNetCreaCoeff(std::string neuralNetRawCoeff,
                                                                                         std::shared_ptr<Levels> sensorLevels,
                                                                                         double sampleDetect,
                                                                                         double bubbleDetect,
                                                                                         std::shared_ptr<SensorReadings> topHeaterReadings,
                                                                                         double ageOfCard,
                                                                                         double resultHct,
                                                                                         double resultOxygen,
                                                                                         double actualBicarb)
                {
                    std::shared_ptr<NeuralNetCoeff> neuralNetCreaCoeff = std::make_shared<NeuralNetCoeff>();

                    neuralNetCreaCoeff->ParseRawCoefficients(neuralNetRawCoeff);

                    neuralNetCreaCoeff->vectorMappingValue.insert({ NeuralNetCoeffVector::CalMean, sensorLevels->calMean });
                    neuralNetCreaCoeff->vectorMappingValue.insert({ NeuralNetCoeffVector::CalDrift, sensorLevels->calSlope });
                    neuralNetCreaCoeff->vectorMappingValue.insert({ NeuralNetCoeffVector::CalSecond, sensorLevels->calSecond });
                    neuralNetCreaCoeff->vectorMappingValue.insert({ NeuralNetCoeffVector::CalNoise, sensorLevels->calNoise });
                    neuralNetCreaCoeff->vectorMappingValue.insert({ NeuralNetCoeffVector::CalExtrap, sensorLevels->calEx });

                    neuralNetCreaCoeff->vectorMappingValue.insert({ NeuralNetCoeffVector::SampleMean, sensorLevels->sampleMean });
                    neuralNetCreaCoeff->vectorMappingValue.insert({ NeuralNetCoeffVector::SampleDrift, sensorLevels->sampleSlope });
                    neuralNetCreaCoeff->vectorMappingValue.insert({ NeuralNetCoeffVector::SampleSecond, sensorLevels->sampleSecond });
                    neuralNetCreaCoeff->vectorMappingValue.insert({ NeuralNetCoeffVector::SampleNoise, sensorLevels->sampleNoise });

                    neuralNetCreaCoeff->vectorMappingValue.insert({ NeuralNetCoeffVector::PostMean, sensorLevels->postMean });
                    neuralNetCreaCoeff->vectorMappingValue.insert({ NeuralNetCoeffVector::PostDrift, sensorLevels->postSlope });
                    neuralNetCreaCoeff->vectorMappingValue.insert({ NeuralNetCoeffVector::PostSecond, sensorLevels->postSecond });
                    neuralNetCreaCoeff->vectorMappingValue.insert({ NeuralNetCoeffVector::PostNoise, sensorLevels->postNoise });

                    neuralNetCreaCoeff->vectorMappingValue.insert({ NeuralNetCoeffVector::AdditionalMean, sensorLevels->additionalMean });
                    neuralNetCreaCoeff->vectorMappingValue.insert({ NeuralNetCoeffVector::AdditionalDrift, sensorLevels->additionalSlope });

                    neuralNetCreaCoeff->vectorMappingValue.insert({ NeuralNetCoeffVector::SampleDetect, sampleDetect });
                    neuralNetCreaCoeff->vectorMappingValue.insert({ NeuralNetCoeffVector::BubbleDetect, bubbleDetect });
                    neuralNetCreaCoeff->vectorMappingValue.insert({ NeuralNetCoeffVector::TopHeaterPower, topHeaterReadings->levels->response });
                    neuralNetCreaCoeff->vectorMappingValue.insert({ NeuralNetCoeffVector::AgeOfCard, ageOfCard });

                    neuralNetCreaCoeff->vectorMappingValue.insert({ NeuralNetCoeffVector::HematocritConc, resultHct });
                    neuralNetCreaCoeff->vectorMappingValue.insert({ NeuralNetCoeffVector::OxygenConc, resultOxygen });
                    neuralNetCreaCoeff->vectorMappingValue.insert({ NeuralNetCoeffVector::BicarbConc, actualBicarb });

                    return neuralNetCreaCoeff;
                }

                void AnalyticalManager::UpdateCreaLimits(std::shared_ptr<SensorInfo> sensorInfo, double ageOfCard, double sampleDetect)
                {
                    if (!NewCreaQC(sensorInfo->param15))
                    {
                        double cdriftParam = sensorInfo->param16;
                        double cmeanLowerBAge = sensorInfo->param25;
                        double cmeanLowerCInjectionTime = sensorInfo->param17;
                        double cMeanUpperCInjectionTime = sensorInfo->param30;

                        // change the low mean limit
                        sensorInfo->CalMeanLowQC = (double)(sensorInfo->CalMeanLowQC +
                                                           (cmeanLowerBAge * (ageOfCard - sensorInfo->AgeOffset)) +
                                                           (cmeanLowerCInjectionTime * (sampleDetect - sensorInfo->InjectionTimeOffset)));

                        sensorInfo->CalMeanHighQC = (double)(sensorInfo->CalMeanHighQC +
                                                            (cmeanLowerBAge * (ageOfCard - sensorInfo->AgeOffset)) +
                                                            (cMeanUpperCInjectionTime * (sampleDetect - sensorInfo->InjectionTimeOffset)));

                        // change the low mean limit
                        sensorInfo->CalDriftLowQC = (double)(sensorInfo->CalDriftLowQC +
                                                            (cdriftParam * (sampleDetect - sensorInfo->InjectionTimeOffset)));

                        sensorInfo->CalDriftHighQC = (double)(sensorInfo->CalDriftHighQC +
                                                             (cdriftParam * (sampleDetect - sensorInfo->InjectionTimeOffset)));
                    }
                    else
                    {
                        // new crea qc
                        double calExQCLowNominal = sensorInfo->CalMeanLowQC;
                        double calExQCHighNominal = sensorInfo->CalMeanHighQC;
                        double calDriftQCLowNominal = sensorInfo->CalDriftLowQC;
                        double calDriftQCHighNominal = sensorInfo->CalDriftHighQC;
                        double calExQCAgePivot = sensorInfo->CalSecondLowQC;
                        double additionalMeanQCAgePivot = sensorInfo->CalSecondHighQC;
                        double calDriftQCSlopeHigh = sensorInfo->SampleSecondLowQC;
                        double additionalMeanQCSlopeLow = sensorInfo->SampleSecondHighQC;
                        double additionalMeanQCSlopeHigh1 = sensorInfo->PostSecondLowQC;
                        double additionalMeanQCSlopeHigh2 = sensorInfo->PostSecondHighQC;
                        double additionalDriftQCSlopeLow = sensorInfo->DeltaDriftHighQC;
                        double additionalMeanQCLowNominal = sensorInfo->readerMeanLow;
                        double additionalMeanQCHighNominal = sensorInfo->readerMeanHigh;
                        double additionalDriftQCLowNominal = sensorInfo->readerDriftLow;
                        double calDriftQCSlopeLow = sensorInfo->param16;
                        double calExQCSlopeLow = sensorInfo->param17;
                        double calExQCSlopeHigh1 = sensorInfo->param25;
                        double calExQCSlopeHigh2 = sensorInfo->param30;
                        double iCalL1 = sensorInfo->param54;
                        double iCalL2 = sensorInfo->param55;

                        sensorInfo->CalMeanLowQC = calExQCLowNominal + calExQCSlopeLow * ageOfCard;

                        if (NewUpperCalExCreaLimit(sensorInfo->param15))
                        {
                            if (ageOfCard >= CreaAgeLimit)
                            {
                                sensorInfo->CalMeanHighQC = iCalL1 * log(ageOfCard) + iCalL2;
                            }
                            else
                            {
                                sensorInfo->CalMeanHighQC = iCalL1 * log(CreaAgeLimit) + iCalL2;
                            }
                        }
                        else
                        {
                            if (ageOfCard < calExQCAgePivot)
                            {
                                sensorInfo->CalMeanHighQC = calExQCHighNominal + (calExQCSlopeHigh1 * ageOfCard);
                            }
                            else
                            {
                                sensorInfo->CalMeanHighQC = calExQCHighNominal + (calExQCSlopeHigh1 * calExQCAgePivot) +
                                    (calExQCSlopeHigh2 * (ageOfCard - calExQCAgePivot));
                            }
                        }

                        sensorInfo->readerMeanLow = (float)(additionalMeanQCLowNominal + (additionalMeanQCSlopeLow * ageOfCard));

                        if (ageOfCard < additionalMeanQCAgePivot)
                        {
                            sensorInfo->readerMeanHigh = (float)(additionalMeanQCHighNominal + (additionalMeanQCSlopeHigh1 * ageOfCard));
                        }
                        else
                        {
                            sensorInfo->readerMeanHigh = (float)(additionalMeanQCHighNominal + (additionalMeanQCSlopeHigh1 * additionalMeanQCAgePivot) +
                                                                (additionalMeanQCSlopeHigh2 * (ageOfCard - additionalMeanQCAgePivot)));
                        }

                        sensorInfo->readerDriftLow = (float)(additionalDriftQCLowNominal + (additionalDriftQCSlopeLow * ageOfCard));

                        sensorInfo->CalDriftLowQC = calDriftQCLowNominal + (calDriftQCSlopeLow * sampleDetect);
                        sensorInfo->CalDriftHighQC = calDriftQCHighNominal + (calDriftQCSlopeHigh * sampleDetect);

                        sensorInfo->CalSecondLowQC = -99999;
                        sensorInfo->CalSecondHighQC = 99999;

                        sensorInfo->SampleSecondLowQC = -99999;
                        sensorInfo->SampleSecondHighQC = 99999;

                        sensorInfo->PostSecondLowQC = -99999;
                        sensorInfo->PostSecondHighQC = 99999;

                        sensorInfo->DeltaDriftLowQC = -99999;
                        sensorInfo->DeltaDriftHighQC = 99999;
                    }

                    // these 2 limits mean something else for crea relaunch, so set them wide so they dont trip validateallqc
                    sensorInfo->PostDriftLowQC = -999999;
                    sensorInfo->PostDriftHighQC = 999999;
                }

                double AnalyticalManager::ConvertKpaTommHg(double kpa)
                {
                    return kpa / mmHgtoKpa;
                }

                const double AnalyticalManager::mmHgtoKpa = 0.1333;
                const double AnalyticalManager::mmolTomgdl = 4.0;
                const double AnalyticalManager::mmolTomeqlCa = 2.0;
                const double AnalyticalManager::meqlTomgdlCa = 2.0;

                ResultsCalcReturnCode AnalyticalManager::PreTestHematocrit(std::shared_ptr<SensorReadings> hematocritReadings,
                                                                           double fluidAfterFluidThreshold,
                                                                           double maxBubbleWidth,
                                                                           double bubbleDetect,
                                                                           TestMode testMode,
                                                                           double& sampleDetect)
                {
                    // if param7 is 1, then do this, otherwise don't do anything
                    if (hematocritReadings->sensorDescriptor->param7 == 1)
                    {
                        // go through a first round of calculations for the hematocrit window.. just assume it's blood test
                        // for the dips and spikes
                        bool divideByMeanToGetNoise = false;

                        if (hematocritReadings->sensorDescriptor->param6 == 1)
                        {
                            divideByMeanToGetNoise = true;
                        }

                        hematocritReadings->returnCode = CalculateEx(hematocritReadings->readings, hematocritReadings->sensorDescriptor, bubbleDetect, sampleDetect, hematocritReadings->levels,
                                                                     false, testMode, false, nullptr, 0, 0, false, false, divideByMeanToGetNoise, 0, 0, 0, testMode, false, 0.0, false, nullptr, false);

                        // if one of the sample windows failed.. we will try to find a new bubble
                        if (PostOrSampleFailedIQC(hematocritReadings->returnCode) || SpikeDipFailed(hematocritReadings->returnCode))
                        {
                            int startAt = 0;
                            int bubbleBegin = 0;

                            // start from the end of the cal window, that we way don't have as far to look
                            for (size_t i = hematocritReadings->levels->calLast; i < hematocritReadings->readings->size(); i++)
                            {
                                // find the bubbledetect so we don't overshoot it on our way back.. but remember to only set
                                // it once
                                if ((MathHelp::Round((*(hematocritReadings->readings))[i].Time, 1) >= MathHelp::Round(bubbleDetect, 1)) &&
                                    (bubbleBegin == 0))
                                {
                                    bubbleBegin = i;
                                }

                                // find the location which is maxbubblewidth ahead of the bubbledetect time.. and thats
                                // where we will go back from..
                                if (MathHelp::Round((*(hematocritReadings->readings))[i].Time, 1) >= MathHelp::Round(bubbleDetect + maxBubbleWidth, 1))
                                {
                                    startAt = i;
                                    break;
                                }
                            }

                            bool recoverHematocrit = false;

                            // only do this if the hematocrit value already came down.
                            if ((*(hematocritReadings->readings))[startAt].Value < fluidAfterFluidThreshold)
                            {
                                // now, going back from the biggest bubble width.. start looking at the threashold
                                for (int i = startAt; i >= bubbleBegin; i--)
                                {
                                    // as long as the values say fluid.. as soon as it goes air.. we move forward from that
                                    // point and set the sample detect accordingly.
                                    CSharpOutOfRange(i, hematocritReadings->readings->size());
                                    if ((*(hematocritReadings->readings))[i].Value < fluidAfterFluidThreshold)
                                    {
                                        continue;
                                    }
                                    else
                                    {
                                        // so we went over the threshold.. check the 2 points before that.. they
                                        // need to be over the threshold too, otherwise it's no good.
                                        CSharpOutOfRange(i - 1, hematocritReadings->readings->size());
                                        CSharpOutOfRange(i - 2, hematocritReadings->readings->size());

                                        if (((*(hematocritReadings->readings))[i - 1].Value > fluidAfterFluidThreshold) &&
                                            ((*(hematocritReadings->readings))[i - 2].Value > fluidAfterFluidThreshold))
                                        {
                                            // find the decimal part of the time and make sure we round up
                                            double leftOver = (*(hematocritReadings->readings))[i].Time - MathHelp::Round((*(hematocritReadings->readings))[i].Time, 0);

                                            // this tells us how many 0.2's we are in to the current second and removes the remainder.
                                            int asAnInt = (int)(leftOver * 5) + 2;

                                            // take the raw number of seconds we're at and add the number of samples to that..
                                            // and that's our next sample detect time
                                            sampleDetect = MathHelp::Round((*(hematocritReadings->readings))[i].Time, 0) + (asAnInt / 5.0);
                                        }

                                        break;
                                    }
                                }
                            }
                        }
                    }

                    return ResultsCalcReturnCode::Success;
                }

                ResultsCalcReturnCode AnalyticalManager::CalculateHematocrit(std::shared_ptr<SensorReadings> hematocritReadings,
                                                                             std::shared_ptr<SensorReadings> sodiumReadings,
                                                                             std::shared_ptr<SensorReadings> potassiumReadings,
                                                                             double bubbleDetect,
                                                                             double sampleDetect,
                                                                             TestMode testMode,
                                                                             bool applyHemodilution,
                                                                             TestMode qcAs,
                                                                             bool isEstimated,
                                                                             bool applyVolumeCorrections,
                                                                             bool isBlood,
                                                                             double calciumAdditionalDrift)
                {
                    std::shared_ptr<SensorInfo> sensorInfo = hematocritReadings->sensorDescriptor;
                    std::shared_ptr<Levels> sensorLevels = hematocritReadings->levels;
                    double sodiumResult;
                    double potassiumResult;

                    bool divideByMeanToGetNoise = false;

                    double hctCut = 0;
                    double CaAWdriftpivot = 0;
                    double CaAWDriftlimit = 0;
                    double alpha = 0;
                    double beta = 0;

                    hctCut = sensorInfo->param25;
                    CaAWdriftpivot = sensorInfo->param26;
                    CaAWDriftlimit = sensorInfo->param27;

                    if (isBlood)
                    {
                        alpha = sensorInfo->param28;
                        beta = sensorInfo->param29;
                    }
                    else
                    {
                        alpha = sensorInfo->param30;
                        beta = sensorInfo->param31;
                    }

                    // check param6 for whether to divide noiseby mean
                    if (sensorInfo->param6 == 1)
                    {
                        divideByMeanToGetNoise = true;
                    }
                    hematocritReadings->requirementsFailedQC = false;
                    hematocritReadings->returnCode = CalculateEx(hematocritReadings->readings, sensorInfo, bubbleDetect, sampleDetect, sensorLevels,
                                                                 false, testMode, false, nullptr, 0, 0, false, false, divideByMeanToGetNoise, 0, 0, 0, testMode, false, 0.0, false, nullptr, false);

                    // if hematocrit failed sample delivery with either the new window or the old one, we need to attempt a hematocrit recovery
                    if ((PostOrSampleFailedIQC(hematocritReadings->returnCode) || SpikeDipFailed(hematocritReadings->returnCode)) &&
                        (sensorInfo->param8 == 1))
                    {
                        auto tempSensorInfo = std::make_shared<SensorInfo>();
                        CopySensorInfo(tempSensorInfo, sensorInfo);

                        // set the small hematocrit sample window parameters
                        tempSensorInfo->sampleDelimit = 0.8;
                        tempSensorInfo->sampleWindowSize = 0.8;
                        tempSensorInfo->SampleMeanLowQC = 1500;
                        tempSensorInfo->SampleMeanHighQC = 40000;
                        tempSensorInfo->SampleDriftLowQC = -500;
                        tempSensorInfo->SampleDriftHighQC = 500;

                        // noise is dependent on param6
                        if (tempSensorInfo->param6 == 1)
                        {
                            tempSensorInfo->SampleNoiseHighQC = 0.75;
                        }
                        else
                        {
                            tempSensorInfo->SampleNoiseHighQC = 1500;
                        }

                        // turn off spike dip detection and post window checking and delta drifts
                        tempSensorInfo->lateAqNoiseHigh = 99999;
                        tempSensorInfo->lateBloodNoiseHigh = 99999;
                        tempSensorInfo->aqNoiseHigh = 99999;
                        tempSensorInfo->bloodNoiseHigh = 99999;
                        tempSensorInfo->PostDriftLowQC = -99999;
                        tempSensorInfo->PostDriftHighQC = 99999;
                        tempSensorInfo->PostMeanLowQC = -99999;
                        tempSensorInfo->PostMeanHighQC = 999999;
                        tempSensorInfo->PostNoiseHighQC = 99999;
                        tempSensorInfo->DeltaDriftLowQC = -99999;
                        tempSensorInfo->DeltaDriftHighQC = 99999;

                        // send it through and use that return code.. this is our last try at this.. if it
                        // doesnt work, then hematocrit won't work.
                        hematocritReadings->returnCode = CalculateEx(hematocritReadings->readings, tempSensorInfo, bubbleDetect, sampleDetect, sensorLevels,
                                                                     false, testMode, false, nullptr, 0, 0, false, false, divideByMeanToGetNoise, 650, 0, 0, qcAs, false, 0.0, false, nullptr, false);

                        // this means the recovery algorithm worked
                        if (!FailedIQC(hematocritReadings->returnCode))
                        {
                            // set output4 in hematocrit levels. chloride is going to use this later
                            hematocritReadings->levels->output4 = 1;
                        }
                    }

                    // this is the error that can get returned that we cant recover from..
                    // ...that there werent enough data points to calculate.
                    // if its just a qc failure.. continue.. but flag it as a qc failure as we
                    // leave
                    if ((hematocritReadings->returnCode == ResultsCalcReturnCode::CannotCalculate) ||
                        (((sodiumReadings == nullptr) || (potassiumReadings == nullptr)) && (testMode == TestMode::BloodTest)))
                    {
                        hematocritReadings->result = NAN;
                        hematocritReadings->requirementsFailedQC = true;

                        // uncorrected may have failed iqc.. we dont want to overwrite the old return code
                        if (hematocritReadings->returnCode == ResultsCalcReturnCode::Success)
                        {
                            hematocritReadings->returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                    }
                    else
                    {
                        double series = sensorInfo->offset;
                        double a = sensorInfo->param1;
                        double b = sensorInfo->param2;
                        double c;
                        double Mco = sensorInfo->param20;
                        double Bco = sensorInfo->param21;

                        // backwards compatibility to old testconfig that will have this param as 0, so we dont cause a divide by 0
                        if (Mco < 0.00001)
                        {
                            Mco = 1;
                        }

                        double nmTP = 0;

                        // taken out because cpb sample type is not being used
                        // use nmtp if sample type is cpb
                        if (applyHemodilution)
                        {
                            nmTP = sensorInfo->param4;
                        }

                        double nmHct = sensorInfo->param5;

                        if (sensorInfo->param3 == 1)
                        {
                            sensorLevels->uncorrectedResponse = sensorLevels->response = CSharpDecimalCalculation((sensorLevels->sampleMean - series) / (sensorLevels->calMean - series));
                        }
                        else
                        {
                            sensorLevels->uncorrectedResponse = sensorLevels->response = CSharpDecimalCalculation((sensorLevels->sampleEx - series) / (sensorLevels->calEx - series));
                        }

                        if (testMode == TestMode::QA)
                        {
                            // uncorrected hematocrit
                            c = 1.07;
                            double firstHematocrit = CSharpDecimalCalculation(((sensorLevels->response - c) /
                                                                               (a * sensorLevels->response + c * b)) * 100);

                            if (isEstimated)
                            {
                                if (Mco != 0)
                                {
                                    firstHematocrit = CSharpDecimalCalculation((firstHematocrit - Bco) / Mco);
                                }
                            }
                            else
                            {
                                if (HctVolumeCorrection(sensorInfo->param15))
                                {
                                    if (!isEstimated && applyVolumeCorrections && (firstHematocrit >= hctCut))
                                    {
                                        firstHematocrit = CSharpDecimalCalculation((firstHematocrit - Bco) / (Mco + (isBlood ? sensorInfo->param28 : sensorInfo->param30) * (firstHematocrit - hctCut)));
                                    }
                                    else
                                    {
                                        firstHematocrit = CSharpDecimalCalculation((firstHematocrit - Bco) / Mco);
                                    }
                                }
                                else
                                {
                                    if (Mco != 0)
                                    {
                                        if ((!isnan(calciumAdditionalDrift)) && (firstHematocrit >= hctCut) && ((calciumAdditionalDrift - CaAWdriftpivot) > CaAWDriftlimit))
                                        {
                                            firstHematocrit = CSharpDecimalCalculation((firstHematocrit - Bco) / (Mco + alpha * (firstHematocrit - hctCut) + beta * (calciumAdditionalDrift - CaAWdriftpivot) * (firstHematocrit - hctCut)));
                                        }
                                        else
                                        {
                                            firstHematocrit = CSharpDecimalCalculation((firstHematocrit - Bco) / Mco);
                                        }
                                    }
                                }
                            }

                            hematocritReadings->result = std::max(firstHematocrit, CSharpDecimalCalculation((firstHematocrit + nmTP) / (1 + (nmTP / nmHct))));

                            // (firstHematocrit + std::max(0.0, nmTP * (1 - (firstHematocrit/nmHct))));
                            // if no failed iqc. we dont care about sodium, cause its not used for QA mode.
                            if (hematocritReadings->returnCode == ResultsCalcReturnCode::Success)
                            {
                                hematocritReadings->returnCode = ValidateInsanityRangeOnlyReturnCode(hematocritReadings->result,
                                                                                                     testMode == TestMode::BloodTest ? hematocritReadings->insanityLow : hematocritReadings->insanityQALow,
                                                                                                     testMode == TestMode::BloodTest ? hematocritReadings->insanityHigh : hematocritReadings->insanityQAHigh);
                            }

                            // turn oxygen return code to failed qc ever if realtime qc failed during the test and there were no other failures
                            if ((hematocritReadings->returnCode == ResultsCalcReturnCode::Success) && (hematocritReadings->realTimeQCFailedEver == RealTimeQCReturnCode::Failed))
                            {
                                hematocritReadings->returnCode = ResultsCalcReturnCode::FailedQCEver;
                            }

                            if (hematocritReadings->returnCode == ResultsCalcReturnCode::Success)
                            {
                                // uncorrected hematocrit
                                hematocritReadings->returnCode = ResultsCalcReturnCode::UncorrectedHematocrit;
                            }
                        }
                        else //if (fluidType == FluidType::Blood)
                        {
                            bool sodiumOrPotassiumFailure = false;
                            sodiumResult = 0;
                            potassiumResult = 0;

                            // remember that the reason we arent reporting hematocrit is because its requirements
                            // failed qc (and not because they were over range for instance).
                            if (FailedIQC(sodiumReadings->returnCode) || sodiumReadings->requirementsFailedQC)
                            {
                                // use the cal concentration
                                sodiumResult = sodiumReadings->sensorDescriptor->calConcentration;
                                hematocritReadings->requirementsFailedQC = true;

                                if (hematocritReadings->returnCode == ResultsCalcReturnCode::Success)
                                {
                                    sodiumOrPotassiumFailure = true;
                                    hematocritReadings->returnCode = ResultsCalcReturnCode::CannotCalculate;
                                }
                            }
                            else
                            {
                                // we can use over/under range in blood mode, but not qa
                                if ((sodiumReadings->returnCode != ResultsCalcReturnCode::Success) &&
                                    ((testMode == TestMode::QA) ||
                                     ((testMode == TestMode::BloodTest) &&
                                      ((sodiumReadings->returnCode != ResultsCalcReturnCode::UnderReportableRange) &&
                                       (sodiumReadings->returnCode != ResultsCalcReturnCode::OverReportableRange)))))
                                {
                                    if (hematocritReadings->returnCode == ResultsCalcReturnCode::Success)
                                    {
                                        sodiumOrPotassiumFailure = true;
                                        hematocritReadings->returnCode = ResultsCalcReturnCode::CannotCalculate;
                                    }
                                }

                                // use the regular sodium result for calculation even if it's out of range
                                sodiumResult = sodiumReadings->result;
                            }

                            if (FailedIQC(potassiumReadings->returnCode) || potassiumReadings->requirementsFailedQC)
                            {
                                // use the cal concentration
                                potassiumResult = potassiumReadings->sensorDescriptor->calConcentration;
                                hematocritReadings->requirementsFailedQC = true;

                                if (hematocritReadings->returnCode == ResultsCalcReturnCode::Success)
                                {
                                    sodiumOrPotassiumFailure = true;
                                    hematocritReadings->returnCode = ResultsCalcReturnCode::CannotCalculate;
                                }
                            }
                            else
                            {
                                // we can use over/under range in blood mode, but not qc
                                if ((potassiumReadings->returnCode != ResultsCalcReturnCode::Success) &&
                                    ((testMode == TestMode::QA) ||
                                     ((testMode == TestMode::BloodTest) &&
                                      ((potassiumReadings->returnCode != ResultsCalcReturnCode::UnderReportableRange) &&
                                       (potassiumReadings->returnCode != ResultsCalcReturnCode::OverReportableRange)))))
                                {
                                    if (hematocritReadings->returnCode == ResultsCalcReturnCode::Success)
                                    {
                                        sodiumOrPotassiumFailure = true;
                                        hematocritReadings->returnCode = ResultsCalcReturnCode::CannotCalculate;
                                    }
                                }

                                // use the actual potassium result..
                                potassiumResult = potassiumReadings->result;
                            }

                            double l_na;

                            if (sensorInfo->param9 < 0.0001)
                            {
                                l_na = 1.0;
                            }
                            else
                            {
                                l_na = sensorInfo->param9;
                            }

                            double l_k = sensorInfo->param10;

                            try
                            {
                                c = CSharpDecimalCalculation((((l_na * sodiumReadings->sensorDescriptor->calConcentration) +
                                      (l_k * potassiumReadings->sensorDescriptor->calConcentration)) /
                                     ((l_na * sodiumResult) +
                                      (l_k * potassiumResult))) * 1.07);
                            }
                            catch (...)
                            {
                                // catch divide by zero exception.. you never know
                                c = 1.07;
                            }

                            double firstHematocrit = CSharpDecimalCalculation(((sensorLevels->response - c) /
                                                                               (a * sensorLevels->response + c * b)) * 100);

                            if (isEstimated)
                            {
                                if (Mco != 0)
                                {
                                    firstHematocrit = CSharpDecimalCalculation((firstHematocrit - Bco) / Mco);
                                }
                            }
                            else
                            {
                                if (HctVolumeCorrection(sensorInfo->param15))
                                {
                                    if (!isEstimated && applyVolumeCorrections && (firstHematocrit >= hctCut))
                                    {
                                        firstHematocrit = CSharpDecimalCalculation((firstHematocrit - Bco) / (Mco + (isBlood ? sensorInfo->param28 : sensorInfo->param30) * (firstHematocrit - hctCut)));
                                    }
                                    else
                                    {
                                        firstHematocrit = CSharpDecimalCalculation((firstHematocrit - Bco) / Mco);
                                    }
                                }
                                else
                                {
                                    if (Mco != 0)
                                    {
                                        if ((!isnan(calciumAdditionalDrift)) && (firstHematocrit >= hctCut) && ((calciumAdditionalDrift - CaAWdriftpivot) > CaAWDriftlimit))
                                        {
                                            firstHematocrit = CSharpDecimalCalculation((firstHematocrit - Bco) / (Mco + alpha * (firstHematocrit - hctCut) + beta * (calciumAdditionalDrift - CaAWdriftpivot) * (firstHematocrit - hctCut)));
                                        }
                                        else
                                        {
                                            firstHematocrit = CSharpDecimalCalculation((firstHematocrit - Bco) / Mco);
                                        }
                                    }
                                }
                            }

                            hematocritReadings->result = std::max(firstHematocrit, CSharpDecimalCalculation((firstHematocrit + nmTP) / (1 + (nmTP / nmHct))));

                            // k2 vs k3. k2 needs to be multiplied by 4.25%
                            if (sensorInfo->param19 != 0)
                            {
                                hematocritReadings->result = hematocritReadings->result * 1.0425;
                            }

                            if ((hematocritReadings->returnCode == ResultsCalcReturnCode::Success) ||
                                ((hematocritReadings->returnCode == ResultsCalcReturnCode::CannotCalculate) &&
                                 sodiumOrPotassiumFailure))
                            {
                                ResultsCalcReturnCode tempRc = ValidateInsanityRangeOnlyReturnCode(hematocritReadings->result,
                                                                                                   testMode == TestMode::BloodTest ? hematocritReadings->insanityLow : hematocritReadings->insanityQALow,
                                                                                                   testMode == TestMode::BloodTest ? hematocritReadings->insanityHigh : hematocritReadings->insanityQAHigh);

                                if (!((hematocritReadings->returnCode == ResultsCalcReturnCode::CannotCalculate) &&
                                      (tempRc == ResultsCalcReturnCode::Success)))
                                {
                                    hematocritReadings->returnCode = tempRc;
                                }
                            }

                            // change hct return code to failedqcever if there was a realtime qc failure but then success after
                            if ((hematocritReadings->returnCode == ResultsCalcReturnCode::Success) && (hematocritReadings->realTimeQCFailedEver == RealTimeQCReturnCode::Failed))
                            {
                                hematocritReadings->returnCode = ResultsCalcReturnCode::FailedQCEver;
                            }

                            if (hematocritReadings->returnCode == ResultsCalcReturnCode::Success)
                            {
                                // otherwise, the sodium value was in range, so validate the reportable range
                                ResultsCalcReturnCode tempRc = ValidateReportableRangeOnlyReturnCode(hematocritReadings->result, hematocritReadings->reportableLow, hematocritReadings->reportableHigh);

                                if (!((hematocritReadings->returnCode == ResultsCalcReturnCode::CannotCalculate) &&
                                      (tempRc == ResultsCalcReturnCode::Success)))
                                {
                                    hematocritReadings->returnCode = tempRc;
                                }
                            }

                            // estimated hematocrit
                            if (isEstimated)
                            {
                                sensorLevels->output1 = CSharpDecimalCalculation(hematocritReadings->result);
                                sensorLevels->output2 = (int)hematocritReadings->returnCode;
                                sensorLevels->output3 = hematocritReadings->requirementsFailedQC ? 1.0 : 0.0;
                            }
                        }
                    }

                    return hematocritReadings->returnCode;
                }

                std::shared_ptr<FinalResult> AnalyticalManager::FindReading(std::vector<FinalResult> &results, Analytes analyteToFind)
                {
                    std::shared_ptr<FinalResult> result = nullptr;

                    // take the first reading
                    for (size_t i = 0; i < results.size(); i++)
                    {
                        if (results[i].analyte == analyteToFind)
                        {
                            // copy the reading, whatever it is
                            result = std::make_shared<FinalResult>(results[i]); //.returnCode;
                            break;
                        }
                    }

                    return result; //  ResultsCalcReturnCode::RequirementsNotFound;
                }

                void AnalyticalManager::ComputeCalculatedActualBicarbonate(std::vector<FinalResult> &measuredResults,
                                                                           std::vector<FinalResult> &calculatedResults)
                {
                    // figure out whether we can do actual bicarbonate or not
                    std::shared_ptr<FinalResult> pco2Result = FindReading(measuredResults, Analytes::CarbonDioxide);
                    std::shared_ptr<FinalResult> phResult = FindReading(measuredResults, Analytes::pH);

                    // if they werent found, dont do a calculation. if they were found and didnt pass qc
                    // do the calculation, but flag it
                    if ((pco2Result != nullptr) && (phResult != nullptr))
                    {
                        FinalResult cfr;
                        cfr.analyte = Analytes::ActualBicarbonate;

                        // compute corrected oxygen
                        cfr.reading = pco2Result->reading * std::pow(10, phResult->reading - 7.608);

                        // invalid pco2 or ph. so just flag it.. but we'll still have the value
                        if (FailedIQC(pco2Result->returnCode) || FailedIQC(phResult->returnCode) ||
                            phResult->requirementsFailedIQC || pco2Result->requirementsFailedIQC)
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                            cfr.requirementsFailedIQC = true;
                        }
                        else if ((pco2Result->returnCode != ResultsCalcReturnCode::Success) ||
                                 (phResult->returnCode != ResultsCalcReturnCode::Success))
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                        else
                        {
                            cfr.returnCode = ResultsCalcReturnCode::Success;
                        }

                        calculatedResults.push_back(cfr);
                    }
                }

                double AnalyticalManager::ComputeCalculatedUncorrectedActualBicarbonate(double uncorrectedCO2,
                                                                                        ResultsCalcReturnCode CO2ReturnCode,
                                                                                        double uncorrectedpH,
                                                                                        ResultsCalcReturnCode pHReturnCode)
                {
                    // do this calculation as long as co2 and ph were calculated and havent failed iqc.
                    // it doesnt matter if they're over the reportable range or under it
                    if (FailedIQC(CO2ReturnCode) ||
                            FailedIQC(pHReturnCode) ||
                            (CO2ReturnCode == ResultsCalcReturnCode::CannotCalculate) ||
                            (pHReturnCode == ResultsCalcReturnCode::CannotCalculate) ||
                            isnan(uncorrectedCO2) ||
                            isnan(uncorrectedpH))
                    {
                        return NAN;
                    }
                    else
                    {
                        return (uncorrectedCO2 * std::pow(10, uncorrectedpH - 7.608));
                    }
                }

                void AnalyticalManager::ComputeCalculatedBaseExcessECF(std::vector<FinalResult> &measuredResults,
                                                                       std::vector<FinalResult> &calculatedResults)
                {
                    std::shared_ptr<FinalResult> hco3Result = FindReading(calculatedResults, Analytes::ActualBicarbonate);
                    std::shared_ptr<FinalResult> phResult = FindReading(measuredResults, Analytes::pH);

                    if ((hco3Result != nullptr) && (phResult != nullptr))
                    {
                        FinalResult cfr;
                        cfr.analyte = Analytes::BaseExcessECF;
                        cfr.reading = hco3Result->reading - 24.8 + (16.2 * (phResult->reading - 7.4));

                        // if readings were valid, calculate. otherwise invalid result
                        if ((FailedIQC(hco3Result->returnCode) || FailedIQC(phResult->returnCode) ||
                             phResult->requirementsFailedIQC || hco3Result->requirementsFailedIQC))
                        {
                            cfr.requirementsFailedIQC = true;
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                        else if ((phResult->returnCode != ResultsCalcReturnCode::Success) ||
                                 (hco3Result->returnCode != ResultsCalcReturnCode::Success))
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                        else
                        {
                            cfr.returnCode = ResultsCalcReturnCode::Success;

                        }

                        calculatedResults.push_back(cfr);
                    }
                }

                void AnalyticalManager::ComputeCalculatedBaseExcessBlood(std::vector<FinalResult> &measuredResults,
                                                                         std::vector<FinalResult> &calculatedResults)
                {
                    std::shared_ptr<FinalResult> hco3Result = FindReading(calculatedResults, Analytes::ActualBicarbonate);
                    std::shared_ptr<FinalResult> phResult = FindReading(measuredResults, Analytes::pH);
                    std::shared_ptr<FinalResult> HgbResult = FindReading(calculatedResults, Analytes::Hemoglobin);

                    if ((hco3Result != nullptr) && (phResult != nullptr) && (HgbResult != nullptr))
                    {
                        FinalResult cfr;

                        cfr.analyte = Analytes::BaseExcessBlood;
                        cfr.reading = (1 - (0.014 * HgbResult->reading)) * (hco3Result->reading - 24.8 + (((1.43 * HgbResult->reading) + 7.7) * (phResult->reading - 7.4)));

                        // if readings were valid, calculate. otherwise invalid result
                        if (FailedIQC(hco3Result->returnCode) || FailedIQC(phResult->returnCode) || FailedIQC(HgbResult->returnCode) ||
                            hco3Result->requirementsFailedIQC || phResult->requirementsFailedIQC)
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                            cfr.requirementsFailedIQC = true;
                        }
                        if ((hco3Result->returnCode != ResultsCalcReturnCode::Success) ||
                            (phResult->returnCode != ResultsCalcReturnCode::Success))
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                        else
                        {
                            // don't even bother checking the hgb return code, unless it fails iqc.. which can't even happen
                            // the cases that land here are
                            // hct fails qc: hgb uses 42 as hematocrit but still reports cnc
                            // hct has sodium or potassium that fail qc: hct uses cal sodium or potassium. reports cnc but still has a number
                            // hct has a sodium or potassium that's out of range. hct uses this value, reports cnc but provides a number
                            // so either way we're ok to report success no matter what happened to hgb/hct
                            cfr.returnCode = ResultsCalcReturnCode::Success;
                        }

                        calculatedResults.push_back(cfr);
                    }
                }

                void AnalyticalManager::ComputeCalculatedOxygenSaturation(std::vector<FinalResult> &measuredResults,
                                                                          std::vector<FinalResult> &calculatedResults)
                {
                    std::shared_ptr<FinalResult> o2Result = FindReading(measuredResults, Analytes::Oxygen);
                    std::shared_ptr<FinalResult> pHResult = FindReading(measuredResults, Analytes::pH);
                    std::shared_ptr<FinalResult> bicarbResult = FindReading(calculatedResults, Analytes::ActualBicarbonate);

                    if ((o2Result != nullptr) && (pHResult != nullptr) && (bicarbResult != nullptr))
                    {
                        FinalResult cfr;
                        cfr.analyte = Analytes::OxygenSaturation;

                        /*
                        // BAYER o2sat formula - now using istat formula
                        double n = oxygenReading * std::pow(10, (0.48 * (phReading - 7.4)) - 0.0013 * beReading);
        
                        // compute o2 saturation
                        cfr.reading = ((std::pow(n, 4) - 15 * std::pow(n, 3) + 2045 * std::pow(n, 2) + 2000 * n) /
                            (std::pow(n, 4) - 15 * std::pow(n, 3) + 2400 * std::pow(n, 2) - 31100 * n + (2.4 * std::pow(10, 6)))) * 100;\
                         */

                        // istat formula.. calculate x first, then use it in the equation
                        double x = o2Result->reading * std::pow(10,
                                                                      (0.48 * (pHResult->reading - 7.4)) -
                                                                      (0.0013 * (bicarbResult->reading - 25)));

                        cfr.reading = 100 * ((std::pow(x, 3) + (150 * x)) /
                                             (std::pow(x, 3) + (150 * x) + 23400.0));

                        // invalid values, ergo invalid result
                        if (FailedIQC(o2Result->returnCode) || FailedIQC(pHResult->returnCode) || FailedIQC(bicarbResult->returnCode) ||
                            o2Result->requirementsFailedIQC || pHResult->requirementsFailedIQC || bicarbResult->requirementsFailedIQC)
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                            cfr.requirementsFailedIQC = true;
                        }
                        else if ((o2Result->returnCode != ResultsCalcReturnCode::Success) ||
                                 (pHResult->returnCode != ResultsCalcReturnCode::Success) ||
                                 (bicarbResult->returnCode != ResultsCalcReturnCode::Success))
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                        else
                        {
                            cfr.returnCode = ResultsCalcReturnCode::Success;
                        }

                        calculatedResults.push_back(cfr);
                    }
                }

                void AnalyticalManager::ComputeCalculatedTotalCO2(std::vector<FinalResult> &measuredResults,
                                                                  std::vector<FinalResult> &calculatedResults)
                {
                    std::shared_ptr<FinalResult> co2Result = FindReading(measuredResults, Analytes::CarbonDioxide);
                    std::shared_ptr<FinalResult> bicarbResult = FindReading(calculatedResults, Analytes::ActualBicarbonate);

                    if ((co2Result != nullptr) && (bicarbResult != nullptr))
                    {
                        FinalResult cfr;
                        cfr.analyte = Analytes::TotalCO2;

                        // compute total co2
                        cfr.reading = bicarbResult->reading + (0.0307 * co2Result->reading);

                        // invalid pco2, ergo invalid corrected actual bicarbonate
                        if (FailedIQC(co2Result->returnCode) || FailedIQC(bicarbResult->returnCode) ||
                            co2Result->requirementsFailedIQC || bicarbResult->requirementsFailedIQC)
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                            cfr.requirementsFailedIQC = true;
                        }
                        if ((co2Result->returnCode != ResultsCalcReturnCode::Success) ||
                            (bicarbResult->returnCode != ResultsCalcReturnCode::Success))
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                        else
                        {
                            cfr.returnCode = ResultsCalcReturnCode::Success;
                        }

                        calculatedResults.push_back(cfr);
                    }
                }

                void AnalyticalManager::ComputeCalculatedAlveolarO2(std::vector<FinalResult> &measuredResults,
                                                                    std::vector<FinalResult> &calculatedResults,
                                                                    double ambientPressure,
                                                                    double fio2,
                                                                    double RQ)
                {
                    std::shared_ptr<FinalResult> co2Result = FindReading(measuredResults, Analytes::CarbonDioxide);

                    if (co2Result != nullptr)
                    {
                        FinalResult cfr;
                        cfr.analyte = Analytes::AlveolarO2;
                        if (isnan(fio2) || isnan(RQ) || (fio2 < fio2RangeLow) || (fio2 > fio2RangeHigh) || (RQ < rQRangeLow) || (RQ > rQRangeHigh))
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                            calculatedResults.push_back(cfr);
                            return;
                        }
                        double temperature = defaultTemperature;
                        double pH2O = pH20TermOne * std::pow(10, (pH20TermTwo * (temperature - defaultTemperature) - pH20TermThree * (temperature - defaultTemperature) * (temperature - defaultTemperature))); //unit kPa
                        // compute and unit is mmHg
                        cfr.reading = ((fio2 / 100) * (ConvertKpaTommHg(ambientPressure) - ConvertKpaTommHg(pH2O))) - (co2Result->reading * (1 / RQ - (fio2 / 100) * (1 / RQ - 1)));

                        // invalid pco2, ergo invalid alveolar tension
                        if (FailedIQC(co2Result->returnCode) || co2Result->requirementsFailedIQC)
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                            cfr.requirementsFailedIQC = true;
                        }
                        else if (co2Result->returnCode != ResultsCalcReturnCode::Success)
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                        else
                        {
                            cfr.returnCode = ResultsCalcReturnCode::Success;
                        }

                        calculatedResults.push_back(cfr);
                    }
                }

                void AnalyticalManager::ComputeCorrectedAlveolarO2(std::vector<FinalResult> &correctedResults,
                                                                   double ambientPressure,
                                                                   double fio2,
                                                                   double RQ,
                                                                   double patientTemperature)
                {
                    std::shared_ptr<FinalResult> co2Result = FindReading(correctedResults, Analytes::correctedCO2);

                    if (co2Result != nullptr)
                    {
                        FinalResult cfr;
                        cfr.analyte = Analytes::correctedAlveolarO2;
                        cfr.correctedWhat = Analytes::AlveolarO2;

                        if (isnan(fio2) || isnan(RQ) || isnan(patientTemperature) || (fio2 < fio2RangeLow) || (fio2 > fio2RangeHigh) || (RQ < rQRangeLow) || (RQ > rQRangeHigh))
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                            correctedResults.push_back(cfr);
                            return;
                        }

                        double pH2O = pH20TermOne * std::pow(10, (pH20TermTwo * (patientTemperature - defaultTemperature) - pH20TermThree * (patientTemperature - defaultTemperature) * (patientTemperature - defaultTemperature))); //unit kPa
                        // compute and unit is mmHg
                        cfr.reading = ((fio2 / 100) * (ConvertKpaTommHg(ambientPressure) - ConvertKpaTommHg(pH2O))) - (co2Result->reading * (1 / RQ - (fio2 / 100) * (1 / RQ - 1)));

                        // invalid pco2, ergo invalid alveolar tension
                        if (FailedIQC(co2Result->returnCode) || co2Result->requirementsFailedIQC)
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                            cfr.requirementsFailedIQC = true;
                        }
                        else if (co2Result->returnCode != ResultsCalcReturnCode::Success)
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                        else
                        {
                            cfr.returnCode = ResultsCalcReturnCode::Success;
                        }

                        correctedResults.push_back(cfr);
                    }
                }

                void AnalyticalManager::ComputeCalculatedArtAlvOxDiff(std::vector<FinalResult> &measuredResults,
                                                                      std::vector<FinalResult> &calculatedResults)
                {
                    std::shared_ptr<FinalResult> o2tResult = FindReading(measuredResults, Analytes::Oxygen);
                    std::shared_ptr<FinalResult> alvResult = FindReading(calculatedResults, Analytes::AlveolarO2);

                    if ((alvResult != nullptr) && (o2tResult != nullptr))
                    {
                        FinalResult cfr;
                        cfr.analyte = Analytes::ArtAlvOxDiff;
                        cfr.reading = alvResult->reading - o2tResult->reading;

                        // invalid dependant readings, ergo invalid reading
                        if (FailedIQC(alvResult->returnCode) || FailedIQC(o2tResult->returnCode) ||
                            alvResult->requirementsFailedIQC || o2tResult->requirementsFailedIQC)
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                            cfr.requirementsFailedIQC = true;
                        }
                        else if ((alvResult->returnCode != ResultsCalcReturnCode::Success) ||
                                 (o2tResult->returnCode != ResultsCalcReturnCode::Success))
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                        else
                        {
                            cfr.returnCode = ResultsCalcReturnCode::Success;
                        }

                        calculatedResults.push_back(cfr);
                    }
                }

                void AnalyticalManager::ComputeCorrectedArtAlvOxDiff(std::vector<FinalResult> &correctedResults)
                {
                    std::shared_ptr<FinalResult> o2tResult = FindReading(correctedResults, Analytes::correctedPO2);
                    std::shared_ptr<FinalResult> alvResult = FindReading(correctedResults, Analytes::correctedAlveolarO2);

                    if ((alvResult != nullptr) && (o2tResult != nullptr))
                    {
                        FinalResult cfr;
                        cfr.analyte = Analytes::correctedArtAlvOxDiff;
                        cfr.reading = alvResult->reading - o2tResult->reading;
                        cfr.correctedWhat = Analytes::ArtAlvOxDiff;
                        // invalid dependant readings, ergo invalid reading
                        if (FailedIQC(alvResult->returnCode) || FailedIQC(o2tResult->returnCode) ||
                            alvResult->requirementsFailedIQC || o2tResult->requirementsFailedIQC)
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                            cfr.requirementsFailedIQC = true;
                        }
                        else if ((alvResult->returnCode != ResultsCalcReturnCode::Success) ||
                                 (o2tResult->returnCode != ResultsCalcReturnCode::Success))
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                        else
                        {
                            cfr.returnCode = ResultsCalcReturnCode::Success;
                        }

                        correctedResults.push_back(cfr);
                    }
                }

                void AnalyticalManager::ComputeCalculatedArtAlvOxRatio(std::vector<FinalResult> &measuredResults,
                                                                       std::vector<FinalResult> &calculatedResults)
                {
                    std::shared_ptr<FinalResult> o2tResult = FindReading(measuredResults, Analytes::Oxygen);
                    std::shared_ptr<FinalResult> alvResult = FindReading(calculatedResults, Analytes::AlveolarO2);

                    if ((alvResult != nullptr) && (o2tResult != nullptr))
                    {
                        FinalResult cfr;
                        cfr.analyte = Analytes::ArtAlvOxRatio;
                        if (alvResult->reading != 0)
                        {
                            cfr.reading = (o2tResult->reading / alvResult->reading) * 100; //alveolar ratio default unit is percentage
                        }

                        // invalid dependant readings, ergo invalid reading
                        if (FailedIQC(alvResult->returnCode) || FailedIQC(o2tResult->returnCode) ||
                            alvResult->requirementsFailedIQC || o2tResult->requirementsFailedIQC)
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                            cfr.requirementsFailedIQC = true;
                        }
                        else if ((alvResult->returnCode != ResultsCalcReturnCode::Success) ||
                                 (o2tResult->returnCode != ResultsCalcReturnCode::Success) || (alvResult->reading == 0))
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                        else
                        {
                            cfr.returnCode = ResultsCalcReturnCode::Success;
                        }

                        calculatedResults.push_back(cfr);
                    }
                }

                void AnalyticalManager::ComputeCorrectedArtAlvOxRatio(std::vector<FinalResult> &correctedResults)
                {
                    std::shared_ptr<FinalResult> o2tResult = FindReading(correctedResults, Analytes::correctedPO2);
                    std::shared_ptr<FinalResult> alvResult = FindReading(correctedResults, Analytes::correctedAlveolarO2);

                    if ((alvResult != nullptr) && (o2tResult != nullptr))
                    {
                        FinalResult cfr;
                        cfr.analyte = Analytes::correctedArtAlvOxRatio;
                        if (alvResult->reading != 0)
                        {
                            cfr.reading = (o2tResult->reading / alvResult->reading) * 100; //alveolar ratio default unit is percentage
                        }
                        cfr.correctedWhat = Analytes::ArtAlvOxRatio;
                        // invalid dependant readings, ergo invalid reading
                        if (FailedIQC(alvResult->returnCode) || FailedIQC(o2tResult->returnCode) ||
                            alvResult->requirementsFailedIQC || o2tResult->requirementsFailedIQC)
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                            cfr.requirementsFailedIQC = true;
                        }
                        else if ((alvResult->returnCode != ResultsCalcReturnCode::Success) ||
                                 (o2tResult->returnCode != ResultsCalcReturnCode::Success) || (alvResult->reading == 0))
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                        else
                        {
                            cfr.returnCode = ResultsCalcReturnCode::Success;
                        }

                        correctedResults.push_back(cfr);
                    }
                }

                void AnalyticalManager::ComputeCalculatedBUNCreaRatio(std::vector<FinalResult> &measuredResults,
                                                                      std::vector<FinalResult> &calculatedResults)
                {
                    std::shared_ptr<FinalResult> bunResult = FindReading(measuredResults, Analytes::BUN);
                    std::shared_ptr<FinalResult> creaResult = FindReading(measuredResults, Analytes::Creatinine);

                    if ((creaResult != nullptr) && (bunResult != nullptr))
                    {
                        FinalResult cfr;
                        cfr.analyte = Analytes::BUNCreaRatio;
                        if (isnan(creaResult->reading) || creaResult->reading < 0.1 || isnan(bunResult->reading))
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                        else
                        {
                            cfr.reading = (bunResult->reading / creaResult->reading);

                            // invalid dependant readings, ergo invalid reading
                            if (FailedIQC(creaResult->returnCode) || FailedIQC(bunResult->returnCode) ||
                                creaResult->requirementsFailedIQC || bunResult->requirementsFailedIQC)
                            {
                                cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                                cfr.requirementsFailedIQC = true;
                            }
                            else if ((creaResult->returnCode != ResultsCalcReturnCode::Success) ||
                                (bunResult->returnCode != ResultsCalcReturnCode::Success))
                            {
                                cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                            }
                            else
                            {
                                cfr.returnCode = ResultsCalcReturnCode::Success;
                            }
                        }
                        calculatedResults.push_back(cfr);
                    }
                }

                void AnalyticalManager::ComputeCalculatedHemoglobin(std::vector<FinalResult> &measuredResults,
                                                                    std::vector<FinalResult> &calculatedResults,
                                                                    TestMode testMode)
                {
                    // figure out whether we can do hemoglobin or not                        
                    std::shared_ptr<FinalResult> hemResult = FindReading(measuredResults, Analytes::Hematocrit);

                    // if they werent found, dont do a calculation. if they were found and didnt pass qc
                    // do the calculation, but flag it
                    if (hemResult != nullptr)
                    {
                        FinalResult cfr;
                        cfr.analyte = Analytes::Hemoglobin;

                        if (FailedIQC(hemResult->returnCode) || isnan(hemResult->reading))
                        {
                            // substitute regular hematocrit if hematocrit failed qc or its the
                            // special case of cannot calculate that yeilds nan
                            // other cases of cannot calculate still yield a number (like when
                            // sodium and potassium are over/under range or have failed qc and 
                            // have had cal sodium and potassium used in the hematocrit calculation instead                    
                            cfr.reading = 42 / 2.941;
                        }
                        else
                        {
                            cfr.reading = hemResult->reading / 2.941;
                        }

                        // invalid hematocrit, ergo invalid corrected hemoglobin
                        if (FailedIQC(hemResult->returnCode) || hemResult->requirementsFailedIQC)
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                            cfr.requirementsFailedIQC = true;
                        }

                        // this is really just the out of range case.. we want it to cnc. but we dont want
                        // requirement failed qc because then be(b) would be cnc.. and we actually want
                        // to use the hemoglobin for be(b) if hct was out of range.
                        if ((hemResult->returnCode != ResultsCalcReturnCode::Success) &&
                            (hemResult->returnCode != ResultsCalcReturnCode::UncorrectedHematocrit))
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                        else
                        {
                            cfr.returnCode = ResultsCalcReturnCode::Success;
                        }

                        calculatedResults.push_back(cfr);
                    }
                }

                bool AnalyticalManager::IsBadValue(double val)
                {
                    if (isnan(val) || isinf(val) || DBL_MAX == val)
                        return true;
                    else
                        return false;
                }

                void AnalyticalManager::ComputeCalculatedResults(IN std::vector<FinalResult> &measuredResults,
                                                                 OUT std::vector<FinalResult> &calculatedResults,
                                                                 IN double passedctHb,
                                                                 IN double FiO2,
                                                                 IN double patientTemperature,
                                                                 IN double ambientPressure,
                                                                 IN TestMode testMode,
                                                                 IN double patientAge,
                                                                 IN Gender gender,
                                                                 IN eGFRFormula egfrFormula,
                                                                 IN double patientHeight,
                                                                 IN AgeCategory ageCategory,
                                                                 IN double RQ,
                                                                 IN bool calculateAlveolar,
                                                                 IN bool theApplymTCO2)
                {
                    bool applymTCO2 = theApplymTCO2;
                    double ctHb = passedctHb;

                    ComputeCalculatedHemoglobin(measuredResults, calculatedResults, testMode);

                    ComputeCalculatedActualBicarbonate(measuredResults, calculatedResults);
                    //sO2(T) need cHCO3 but we do corrected value before calculated value.
                    //ComputeCorrectedOxygenSaturation(calculatedResults, correctedResults);

                    // total co2 after actual bicarbonate
                    ComputeCalculatedTotalCO2(measuredResults, calculatedResults);

                    // base excess after actual bicarbonate
                    ComputeCalculatedBaseExcessECF(measuredResults, calculatedResults);


                    ComputeCalculatedBaseExcessBlood(measuredResults, calculatedResults);

                    // o2 saturation after base excess blood
                    ComputeCalculatedOxygenSaturation(measuredResults, calculatedResults);

                    if (calculateAlveolar)
                    {
                        ComputeCalculatedAlveolarO2(measuredResults, calculatedResults, ambientPressure, FiO2, RQ);
                        ComputeCalculatedArtAlvOxDiff(measuredResults, calculatedResults);
                        ComputeCalculatedArtAlvOxRatio(measuredResults, calculatedResults);
                    }

                    // BUN/Crea
                    ComputeCalculatedBUNCreaRatio(measuredResults, calculatedResults);

                    // agap or agapk... either one
                    ComputeCalculatedAnionGap(measuredResults, calculatedResults, AGAPType::AGAP, applymTCO2);
                    ComputeCalculatedAnionGap(measuredResults, calculatedResults, AGAPType::AGAPK, applymTCO2);

                    if (egfrFormula == eGFRFormula::MDRD)
                    {
                        // Only compute eGFRmdr(a)
                        ComputeCalculatedeGFRmdr(measuredResults, calculatedResults, eGFRType::eGFR, testMode, patientAge, gender);
                        ComputeCalculatedeGFRmdr(measuredResults, calculatedResults, eGFRType::eGFRa, testMode, patientAge, gender);
                    }
                    else if (egfrFormula == eGFRFormula::Japanese)
                    {
                        // Only compute eGFR Japan
                        ComputeCalculatedeGFRjpn(measuredResults, calculatedResults, eGFRType::eGFRj, testMode, patientAge, gender, patientHeight, ageCategory);
                    }

                    // Always compute eGFRckd(a) and eGFRswz
                    ComputeCalculatedeGFRckd(measuredResults, calculatedResults, eGFRType::eGFRckd, testMode, patientAge, gender);
                    ComputeCalculatedeGFRckd(measuredResults, calculatedResults, eGFRType::eGFRckda, testMode, patientAge, gender);

                    ComputeCalculatedeGFRswz(measuredResults, calculatedResults, eGFRType::eGFRswz, testMode, patientAge, patientHeight);

#ifdef EPOCAL_TARGET_HOST
                    if (testMode == TestMode::QA)
                    {
                        for (size_t i = 0; i < calculatedResults.size(); i++)
                        {
                            FinalResult* rc = &(calculatedResults[i]);
                            if ((rc->analyte == Analytes::TotalCO2) || (rc->analyte == Analytes::ActualBicarbonate))
                            {
                                if (rc->returnCode == ResultsCalcReturnCode::Success)
                                {
                                    if (rc->reading < 0)
                                    {
                                        rc->reading = 0;
                                    }
                                }
                            }
                        }
                    }
#endif
                }

                void AnalyticalManager::ComputeCorrectedResults(IN std::vector<FinalResult> &measuredResults,
                                                                OUT std::vector<FinalResult> &correctedResults,
                                                                IN double patientTemperature,
                                                                IN double ambientPressure,
                                                                IN double FiO2,
                                                                IN double RQ,
                                                                IN bool calculateAlveolar)
                {
                    // if a temperature is entered
                    if (!IsBadValue(patientTemperature))
                    {
                        ComputeCorrectedpH(measuredResults, correctedResults, patientTemperature);
                        ComputeCorrectedCarbonDioxide(measuredResults, correctedResults, patientTemperature);
                        ComputeCorrectedOxygen(measuredResults, correctedResults, patientTemperature);

                        if (calculateAlveolar)
                        {
                            ComputeCorrectedAlveolarO2(correctedResults, ambientPressure, FiO2, RQ, patientTemperature);
                            ComputeCorrectedArtAlvOxDiff(correctedResults);
                            ComputeCorrectedArtAlvOxRatio(correctedResults);
                        }
                    }
                }

                void AnalyticalManager::ComputeCorrectedpH(std::vector<FinalResult> &measuredResults,
                                                           std::vector<FinalResult> &correctedResults,
                                                           double patientTemperature)
                {
                    // figure out whether we can do corrected ph or not
                    std::shared_ptr<FinalResult> phResult = FindReading(measuredResults, Analytes::pH);

                    // if ph wasnt found, dont do a calculation. if they were found and didnt pass qc
                    // do the calculation, but flag it
                    if (phResult != nullptr)
                    {
                        FinalResult cfr;
                        cfr.analyte = Analytes::CorrectedpH;
                        cfr.correctedWhat = Analytes::pH;
                        cfr.reading = phResult->reading +
                                      (-0.0147 + (0.0065 * (7.4 - phResult->reading))) * (patientTemperature - 37.0);

                        // invalid oxygen, ergo invalid corrected oxygen
                        if (FailedIQC(phResult->returnCode) || phResult->requirementsFailedIQC)
                        {
                            cfr.requirementsFailedIQC = true;
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                        else if (phResult->returnCode != ResultsCalcReturnCode::Success)
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                        else
                        {
                            // compute corrected oxygen
                            cfr.returnCode = ResultsCalcReturnCode::Success;
                        }

                        correctedResults.push_back(cfr);
                    }
                }

                void AnalyticalManager::ComputeCorrectedCarbonDioxide(std::vector<FinalResult> &measuredResults,
                                                                      std::vector<FinalResult> &correctedResults,
                                                                      double patientTemperature)
                {
                    // figure out whether we can do corrected co2 or not
                    std::shared_ptr<FinalResult> co2Result = FindReading(measuredResults, Analytes::CarbonDioxide);

                    // if co2 wasnt found, dont do a calculation. if they were found and didnt pass qc
                    // do the calculation, but flag it
                    if (co2Result != nullptr)
                    {
                        FinalResult cfr;
                        cfr.analyte = Analytes::correctedCO2;
                        cfr.correctedWhat = Analytes::CarbonDioxide;

                        // compute corrected co2
                        cfr.reading = std::pow(10, log10(co2Result->reading) - ((37.0 - patientTemperature) * 0.019));

                        // invalid co2, ergo invalid corrected co2
                        if (FailedIQC(co2Result->returnCode) || co2Result->requirementsFailedIQC)
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                            cfr.requirementsFailedIQC = true;
                        }
                        else if (co2Result->returnCode != ResultsCalcReturnCode::Success)
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                        else
                        {
                            cfr.returnCode = ResultsCalcReturnCode::Success;
                        }

                        correctedResults.push_back(cfr);
                    }
                }

                void AnalyticalManager::ComputeCorrectedOxygen(std::vector<FinalResult> &measuredResults,
                                                               std::vector<FinalResult> &correctedResults,
                                                               double patientTemperature)
                {
                    std::shared_ptr<FinalResult> o2Result = FindReading(measuredResults, Analytes::Oxygen);

                    // if oxygen wasnt found in the results, it will return unexplained failure
                    // so if its unexplained failure, dont do anything, otherwise go in
                    if (o2Result != nullptr)
                    {
                        FinalResult cfr;
                        cfr.analyte = Analytes::correctedPO2;
                        cfr.correctedWhat = Analytes::Oxygen;

                        // compute corrected oxygen
                        cfr.reading = std::pow(10, log10(o2Result->reading) -
                                                   ((((std::pow(o2Result->reading, 3.88) *
                                                       std::pow((double)10, -11) * 5.49) + 0.071) /
                                                     ((9.72 * std::pow((double)10, -9) *
                                                       std::pow(o2Result->reading, 3.88)) + 2.30))
                                                    * (37.0 - patientTemperature)));

                        // invalid oxygen, ergo invalid corrected oxygen
                        if (FailedIQC(o2Result->returnCode) || o2Result->requirementsFailedIQC)
                        {
                            cfr.requirementsFailedIQC = true;
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                        else if (o2Result->returnCode != ResultsCalcReturnCode::Success)
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }
                        else
                        {
                            cfr.returnCode = ResultsCalcReturnCode::Success;
                        }

                        correctedResults.push_back(cfr);
                    }
                }

                /// <summary>
                /// Computes the calculated eGFR MDRD
                /// </summary>
                /// <param name="measuredResults">Measured input results</param>
                /// <param name="calculatedResults">Calculated output results</param>
                /// <param name="egfrType">The eGFR type to be computed</param>
                /// <param name="testMode">The test mode: Blood or QA</param>
                /// <param name="patientAge">The test patient's age</param>
                /// <param name="gender">The test patient's gender</param>
                /// <remarks>
                /// As per the "ES, Software Requirements Specification, Results Calculation and Internal Quality Control"
                /// documentation, as stated in section 4.1.1.5.12.2:
                ///     If test type is �QA test� then 18 is used for age and male is used for gender.
                /// </remarks>
                void AnalyticalManager::ComputeCalculatedeGFRmdr(std::vector<FinalResult> &measuredResults,
                                                                 std::vector<FinalResult> &calculatedResults,
                                                                 eGFRType egfrType,
                                                                 TestMode testMode,
                                                                 double patientAge,
                                                                 Gender gender)
                {
                    std::shared_ptr<FinalResult> creaRC = FindReading(measuredResults, Analytes::Creatinine);

                    // if creatinine is on the card
                    if (creaRC != nullptr)
                    {
                        // mdrd needs a gender, and age of between 18 and 120. if we dont have these things, we cnc
                        if ((egfrType == eGFRType::eGFR || egfrType == eGFRType::eGFRa) &&
                            (gender != Gender::Unknown) && (!isnan(patientAge)))
                        {
                            FinalResult cfr;

                            cfr.analyte = (egfrType == eGFRType::eGFRa) ? Analytes::eGFRa : Analytes::eGFR;

                            if (creaRC->reading < eGFRmdrSmpConcCreaMin)
                            {
                                //4.1.1.5.12.1 - If SmpConcCrea < 0.1 then eGFR values are not reported.
                                cfr.reading = 0;
                                cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                            }
                            else
                            {
                                //4.1.1.5.12 - eGFR (estimated Glomerular Filtration Rate), also GFRmdr
                                //             IDMS-traceable MDRD type:
                                //             eGFR = 175 * ( (SmpConcCrea)^(-1.154) ) *
                                //                          ( (Age)^(-0.203) ) *
                                //                          (0.742 if female, 1 if male)
                                //
                                //             IDMS-traceable MDRD type for African American:
                                //             eGFR-a = 175 * ( (SmpConcCrea)^(-1.154) ) *
                                //                            ( (Age)^(-0.203) ) *
                                //                            (0.742 if female, 1 if male) * 1.212

                                const double firstConst = 175;
                                double genderMultiplier = (gender == Gender::Female) ? 0.742 : 1.0;
                                double africanAmericanModifier = (egfrType == eGFRType::eGFRa) ? 1.212 : 1.0;
                                const double creaRaise = -1.154;
                                const double ageRaise = -0.203;

                                cfr.reading = firstConst * std::pow(creaRC->reading, creaRaise) *
                                    std::pow(patientAge, ageRaise) *
                                    genderMultiplier * africanAmericanModifier;

                                // if crea actually failed, or the age is not in accepted bounds, we are cnc
                                if ((creaRC->returnCode != ResultsCalcReturnCode::Success) ||
                                    ((patientAge < eGFRmdrPatientAgeMin) || (patientAge > eGFRmdrPatientAgeMax)))
                                {
                                    //4.1.1.5.12 - Do not report if less that 18 years old or greater than 120 years old.
                                    cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                                }
                            }

                            // add it to the results
                            calculatedResults.push_back(cfr);
                        }
                    }
                }

                /// <summary>
                /// Computes the calculated eGFR for Japanese software
                /// </summary>
                /// <param name="measuredResults">Measured input results</param>
                /// <param name="calculatedResults">Calculated output results</param>
                /// <param name="egfrType">The eGFR type to be computed</param>
                /// <param name="testMode">The test mode: Blood or QA</param>
                /// <param name="patientAge">The test patient's age</param>
                /// <param name="gender">The test patient's gender</param>
                /// <param name="patientHeight">The test patient's height</param>
                /// <param name="ageCategory">The test patient's age category</param>
                /// <remarks>
                /// As per the "ES, Software Requirements Specification, Results Calculation and Internal Quality Control"
                /// documentation, as stated in section 4.1.1.5.12.3:
                ///     If test type is �QA test� (eGFR for Japanese software) then 22 is used for age and male is used for gender.
                /// </remarks>
                void AnalyticalManager::ComputeCalculatedeGFRjpn(std::vector<FinalResult> &measuredResults,
                                                                 std::vector<FinalResult> &calculatedResults,
                                                                 eGFRType egfrType,
                                                                 TestMode testMode,
                                                                 double patientAge,
                                                                 Gender gender,
                                                                 double patientHeight,
                                                                 AgeCategory ageCategory)
                {
                    std::shared_ptr<FinalResult> creaRC = FindReading(measuredResults, Analytes::Creatinine);

                    // if creatinine is on the card
                    if (creaRC != nullptr)
                    {
                        FinalResult cfr;

                        //4.1.1.5.12.3 - eGFR for Japanese software
                        //               For Age > 21 years old:
                        if (ageCategory == AgeCategory::Adult)
                        {
                            if ((egfrType == eGFRType::eGFRj) &&
                                (gender != Gender::Unknown) && (!isnan(patientAge)))
                            {
                                cfr.analyte = Analytes::eGFRj;

                                if (creaRC->reading < eGFRjpnSmpConcCreaMin)
                                {
                                    //4.1.1.5.12.1 - If SmpConcCrea < 0.1 then eGFR values are not reported.
                                    cfr.reading = 0;
                                    cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                                }
                                else
                                {
                                    //4.1.1.5.12.3 - For Age > 21 years old:
                                    //               eGFR = 194 * ( (SmpConcCrea)^(-1.094) ) *
                                    //                            ( (Age)^(-0.287) ) *
                                    //                            (0.739 if female, 1 if male)

                                    const double firstConst = 194;
                                    const double creaRaise = -1.094;
                                    const double ageRaise = -0.287;
                                    double genderMultiplier = (gender == Gender::Female) ? 0.739 : 1.0;

                                    cfr.reading = firstConst * std::pow(creaRC->reading, creaRaise) *
                                        std::pow(patientAge, ageRaise) *
                                        genderMultiplier;

                                    // if crea actually failed, we are cnc
                                    if ((creaRC->returnCode != ResultsCalcReturnCode::Success) ||
                                        ((patientAge < eGFRjpnPatientAgeMin) || (patientAge > eGFRjpnPatientAgeMax)))
                                    {
                                        cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                                    }
                                }

                                // add it to the results
                                calculatedResults.push_back(cfr);
                            }
                        }
                        //4.1.1.5.12.3 - eGFR for Japanese software
                        //               For Age 0-21 years old:
                        else if ((ageCategory != AgeCategory::Adult) &&
                            (ageCategory != AgeCategory::None))
                        {
                            if ((egfrType == eGFRType::eGFRj) &&
                                (!isnan(patientHeight) &&
                                (((ageCategory == AgeCategory::Youth) && gender != Gender::Unknown) ||
                                    (ageCategory != AgeCategory::Youth))))
                            {
                                // non adult japanese egfr
                                double k = 0;

                                if (ageCategory == AgeCategory::Premature)
                                {
                                    //4.1.1.5.12.3 - Premature      K = 0.33
                                    k = 0.33;
                                }
                                else if (ageCategory == AgeCategory::Newborn)
                                {
                                    //4.1.1.5.12.3 - Full term      K = 0.45
                                    k = 0.45;
                                }
                                else if (ageCategory == AgeCategory::Infant)
                                {
                                    //4.1.1.5.12.3 - 2-12 year old  K = 0.55
                                    k = 0.55;
                                }
                                else if (ageCategory == AgeCategory::Youth)
                                {
                                    //4.1.1.5.12.3 - 13-21 yeard old female     K = 0.55
                                    //               13-21 yeard old male       K = 0.7
                                    k = (gender == Gender::Female) ? 0.55 : 0.7;
                                }

                                cfr.analyte = Analytes::eGFRj;

                                // 4.1.1.5.12.3 - For Age 0-21 years old:
                                //                eGFR = K * ( height / (SmpConcCrea+0.2) )
                                cfr.reading = k * (patientHeight / (creaRC->reading + 0.2));

                                if (creaRC->returnCode == ResultsCalcReturnCode::Success)
                                {
                                    cfr.returnCode = ResultsCalcReturnCode::Success;
                                }
                                else
                                {
                                    cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                                }

                                // add it to the results
                                calculatedResults.push_back(cfr);
                            }
                            else
                            {
                                cfr.analyte = Analytes::eGFRj;
                                cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;

                                // add it to the results
                                calculatedResults.push_back(cfr);
                            }
                        }
                    }
                }

                /// <summary>
                /// Computes the calculated eGFR ckd
                /// </summary>
                /// <param name="measuredResults">Measured input results</param>
                /// <param name="calculatedResults">Calculated output results</param>
                /// <param name="egfrType">The eGFR type to be computed</param>
                /// <param name="testMode">The test mode: Blood or QA</param>
                /// <param name="patientAge">The test patient's age</param>
                /// <param name="gender">The test patient's gender</param>
                void AnalyticalManager::ComputeCalculatedeGFRckd(std::vector<FinalResult> &measuredResults,
                                                                 std::vector<FinalResult> &calculatedResults,
                                                                 eGFRType egfrType,
                                                                 TestMode testMode,
                                                                 double patientAge,
                                                                 Gender gender)
                {
                    //4.1.1.5.15.2 - If test type is �QA test� then 19 is used for age and male is used for gender.
                    if (testMode == TestMode::QA)
                    {
                        patientAge = 19;
                        gender = Gender::Male;
                    }

                    std::shared_ptr<FinalResult> creaRC = FindReading(measuredResults, Analytes::Creatinine);

                    // if creatinine is on the card
                    if (creaRC != nullptr)
                    {
                        // ckd needs an age between 19 and 120. if we dont have these things, we cnc
                        if ((egfrType == eGFRType::eGFRckd || egfrType == eGFRType::eGFRckda) &&
                            (gender != Gender::Unknown) && (!isnan(patientAge)))
                        {
                            FinalResult cfr;

                            cfr.analyte = (egfrType == eGFRType::eGFRckd) ? Analytes::GFRckd : Analytes::GFRckda;

                            if (creaRC->reading < eGFRckdSmpConcCreaMin)
                            {
                                //4.1.1.5.15.1 - If SmpConcCrea < 0.1 then GFRckd values are not reported.
                                cfr.reading = 0;
                                cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                            }
                            else
                            {
                                //4.1.1.5.15 - GFRckd (estimated Glomerular Filtration Rate)
                                //             CKD-EPI equation, male:
                                //                  GFRckd =   141 * min (SmpConcCrea/0.9,1)^(-0.411) *
                                //                                   max (SmpConcCrea/0.9,1)^(-1.209) *
                                //                                   0.993^(Age)
                                //
                                //             CKD-EPI equation for African American, male:
                                //                  GFRckd-a = 141 * min (SmpConcCrea/0.9,1)^(-0.411) *
                                //                                   max (SmpConcCrea/0.9,1)^(-1.209) *
                                //                                   0.993^(Age) * 1.159
                                //
                                //             CKD-EPI equation, female:
                                //                  GFRckd =   141 * min (SmpConcCrea/0.7,1)^(-0.329) *
                                //                                   max (SmpConcCrea/0.7,1)^(-1.209) *
                                //                                   0.993^(Age) * 1.018
                                //
                                //             CKD-EPI equation for African American, female:
                                //                  GFRckd-a = 141 * min (SmpConcCrea/0.7,1)^(-0.329) *
                                //                                   max (SmpConcCrea/0.7,1)^(-1.209) *
                                //                                   0.993^(Age) * 1.018 * 1.159

                                const double firstConst = 141;
                                double africanAmericanModifier = (egfrType == eGFRType::eGFRckda) ? 1.159 : 1.0;
                                double genderMultiplier = (gender == Gender::Female) ? 1.018 : 1.0;
                                double genderConcCreaModifier = (gender == Gender::Female) ? 0.7 : 0.9;
                                double genderMinConcCreaRaise = (gender == Gender::Female) ? -0.329 : -0.411;
                                const double maxConcCreaRaise = -1.209;
                                const double patientAgeBase = 0.993;

                                cfr.reading = firstConst * std::pow(std::min(creaRC->reading / genderConcCreaModifier, 1.0), genderMinConcCreaRaise) *
                                    std::pow(std::max(creaRC->reading / genderConcCreaModifier, 1.0), maxConcCreaRaise) *
                                    std::pow(patientAgeBase, patientAge) * genderMultiplier * africanAmericanModifier;

                                // if crea actually failed, or the age is not in accepted bounds, we are cnc
                                if ((creaRC->returnCode != ResultsCalcReturnCode::Success) ||
                                    ((patientAge < eGFRckdPatientAgeMin) || (patientAge > eGFRckdPatientAgeMax)))  //4.1.1.5.15 - Do not report if less that 19 years old or greater than 120 years old.
                                {
                                    cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                                }
                            }

                            // add it to the results
                            calculatedResults.push_back(cfr);
                        }
                    }
                }

                /// <summary>
                /// Computes the calculated eGFR swz
                /// </summary>
                /// <param name="measuredResults">Measured input results</param>
                /// <param name="calculatedResults">Calculated output results</param>
                /// <param name="egfrType">The eGFR type to be computed</param>
                /// <param name="testMode">The test mode: Blood or QA</param>
                /// <param name="patientAge">The test patient's age</param>
                /// <param name="patientHeight">The test patient's height</param>
                void AnalyticalManager::ComputeCalculatedeGFRswz(std::vector<FinalResult> &measuredResults,
                                                                 std::vector<FinalResult> &calculatedResults,
                                                                 eGFRType egfrType,
                                                                 TestMode testMode,
                                                                 double patientAge,
                                                                 double patientHeight)
                {
                    //4.1.1.5.16.2 - If test type is �QA test� then 100 cm is used for height.
                    if (testMode == TestMode::QA)
                        patientHeight = 100;

                    std::shared_ptr<FinalResult> creaRC = FindReading(measuredResults, Analytes::Creatinine);

                    // if creatinine is on the card
                    if (creaRC != nullptr)
                    {
                        // swz has needs an age between 1 and 18. if we dont have these things, we cnc
                        if ((egfrType == eGFRType::eGFRswz) &&
                            !isnan(patientAge) &&
                            !isnan(patientHeight))
                        {
                            FinalResult cfr;

                            cfr.analyte = Analytes::GFRswz;

                            if (creaRC->reading < eGFRswzSmpConcCreaMin)
                            {
                                //4.1.1.5.16.1 - If SmpConcCrea < 0.1 then GFRswz values are not reported.
                                cfr.reading = 0;
                                cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                            }
                            else
                            {
                                const double firstConst = 0.413;

                                //4.1.1.5.16 - GFRswz = 0.413 * height / SmpConcCrea.
                                cfr.reading = firstConst * patientHeight / creaRC->reading;

                                // if crea actually failed, or the age is not in accepted bounds, we are cnc
                                if ((creaRC->returnCode != ResultsCalcReturnCode::Success) ||
                                    ((patientAge < eGFRswzPatientAgeMin) || (patientAge > eGFRswzPatientAgeMax)) ||                 //4.1.1.5.16 - Do not report if less than 1 years old or greater than 18 years old.
                                    ((patientHeight < eGFRswzPatientHeightMin) || (patientHeight > eGFRswzPatientHeightMax)))      // Do not report if patient height is less than 1 cm or greater than 275 cm.
                                {
                                    cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                                }
                            }

                            // add it to the results
                            calculatedResults.push_back(cfr);
                        }
                    }
                }

                double AnalyticalManager::ComputeCalculatedActualBicarbonateGivenValues(double pco2Value, double phValue)
                {
                    std::vector<FinalResult> calculatedResults;
                    std::vector<FinalResult> measuredResults;

                    FinalResult pco2Result;

                    pco2Result.analyte = Analytes::CarbonDioxide;
                    pco2Result.returnCode = ResultsCalcReturnCode::Success;
                    pco2Result.reading = pco2Value;

                    measuredResults.push_back(pco2Result);

                    FinalResult phResult;

                    phResult.analyte = Analytes::pH;
                    phResult.returnCode = ResultsCalcReturnCode::Success;
                    phResult.reading = phValue;

                    measuredResults.push_back(phResult);

                    ComputeCalculatedActualBicarbonate(measuredResults, calculatedResults);

                    // there's only 1 value in here, and it's bicarb
                    return ((FinalResult)calculatedResults[0]).reading;
                }

                // assumes all 3 values are within range and passed qc
                double AnalyticalManager::ComputeAnionGapGivenValues(double sodiumValue,
                                                                     double chlorideValue,
                                                                     double bicarbValue,
                                                                     double mTCO2Value,
                                                                     bool applymTCO2)
                {
                    std::vector<FinalResult> calculatedResults;
                    std::vector<FinalResult> measuredResults;

                    FinalResult sodiumResult;

                    sodiumResult.analyte = Analytes::Sodium;
                    sodiumResult.returnCode = ResultsCalcReturnCode::Success;
                    sodiumResult.reading = sodiumValue;

                    measuredResults.push_back(sodiumResult);

                    FinalResult chlorideResult;

                    chlorideResult.analyte = Analytes::Chloride;
                    chlorideResult.returnCode = ResultsCalcReturnCode::Success;
                    chlorideResult.reading = chlorideValue;

                    measuredResults.push_back(chlorideResult);

                    FinalResult bicarbResult;

                    bicarbResult.analyte = Analytes::ActualBicarbonate;
                    bicarbResult.returnCode = ResultsCalcReturnCode::Success;
                    bicarbResult.reading = bicarbValue;

                    calculatedResults.push_back(bicarbResult);

                    if (applymTCO2)
                    {
                        FinalResult tco2Result;

                        tco2Result.analyte = Analytes::MeasuredTCO2;
                        tco2Result.returnCode = ResultsCalcReturnCode::Success;
                        tco2Result.reading = mTCO2Value;

                        measuredResults.push_back(tco2Result);
                    }

                    ComputeCalculatedAnionGap(measuredResults, calculatedResults, AGAPType::AGAP, applymTCO2);

                    // there's only 1 value in here, and it's anion gap
                    return ((FinalResult)calculatedResults[1]).reading;
                }

                void AnalyticalManager::ComputeCalculatedAnionGap(std::vector<FinalResult> &measuredResults,
                                                                  std::vector<FinalResult> &calculatedResults,
                                                                  AGAPType agapType,
                                                                  bool applymTCO2)
                {
                    std::shared_ptr<FinalResult> naRC = FindReading(measuredResults, Analytes::Sodium);
                    std::shared_ptr<FinalResult> bicarbRC;
                    if (applymTCO2)
                    {
                        bicarbRC = FindReading(measuredResults, Analytes::MeasuredTCO2);
                    }
                    else
                    {
                        bicarbRC = FindReading(calculatedResults, Analytes::ActualBicarbonate);
                    }
                    std::shared_ptr<FinalResult> kRC = FindReading(measuredResults, Analytes::Potassium);
                    std::shared_ptr<FinalResult> clRC = FindReading(measuredResults, Analytes::Chloride);

                    if ((clRC != nullptr) && (bicarbRC != nullptr) && (naRC != nullptr))
                    {
                        FinalResult cfr;

                        if (agapType == AGAPType::AGAP)
                        {
                            cfr.analyte = Analytes::AnionGap;

                            // compute anion gap without K
                            cfr.reading = MathHelp::Round(naRC->reading) - MathHelp::Round(clRC->reading) - MathHelp::Round((applymTCO2 ? bicarbRC->reading - 1 : bicarbRC->reading), 1);
                        }
                        else
                        {
                            if (kRC != nullptr)
                            {
                                cfr.analyte = Analytes::AnionGapK;

                                // compute anion gap with K
                                cfr.reading = MathHelp::Round(naRC->reading) + MathHelp::Round(kRC->reading, 1) - MathHelp::Round(clRC->reading) - MathHelp::Round((applymTCO2 ? bicarbRC->reading - 1 : bicarbRC->reading), 1);
                            }
                            else
                            {
                                return;
                            }
                        }

                        // invalid pco2, ergo invalid corrected actual bicarbonate
                        if ((clRC->returnCode == ResultsCalcReturnCode::Success) &&
                            (naRC->returnCode == ResultsCalcReturnCode::Success) &&
                            (bicarbRC->returnCode == ResultsCalcReturnCode::Success))
                        {
                            if (agapType == AGAPType::AGAPK)
                            {
                                if (kRC->returnCode == ResultsCalcReturnCode::Success)
                                {
                                    cfr.returnCode = ResultsCalcReturnCode::Success;
                                }
                                else
                                {
                                    cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                                }
                            }
                            else
                            {
                                cfr.returnCode = ResultsCalcReturnCode::Success;
                            }
                        }
                        else
                        {
                            cfr.returnCode = ResultsCalcReturnCode::CannotCalculate;
                        }

                        calculatedResults.push_back(cfr);
                    }
                }

                void AnalyticalManager::CopySensorInfo(std::shared_ptr<SensorInfo> &toSensorInfo, const std::shared_ptr<SensorInfo> fromSensorInfo)
                {
                    toSensorInfo->channelType = fromSensorInfo->channelType;
                    toSensorInfo->sensorType = fromSensorInfo->sensorType;
                    toSensorInfo->sensorDescriptorNumber = fromSensorInfo->sensorDescriptorNumber;
                    toSensorInfo->calDelimit = fromSensorInfo->calDelimit;
                    toSensorInfo->sampleDelimit = fromSensorInfo->sampleDelimit;
                    toSensorInfo->postDelimit = fromSensorInfo->postDelimit;
                    toSensorInfo->extrapolation = fromSensorInfo->extrapolation;
                    toSensorInfo->calWindowSize = fromSensorInfo->calWindowSize;
                    toSensorInfo->sampleWindowSize = fromSensorInfo->sampleWindowSize;
                    toSensorInfo->postWindowSize = fromSensorInfo->postWindowSize;
                    toSensorInfo->calCurveWeight = fromSensorInfo->calCurveWeight;
                    toSensorInfo->sampleCurveWeight = fromSensorInfo->sampleCurveWeight;
                    toSensorInfo->calConcentration = fromSensorInfo->calConcentration;
                    toSensorInfo->offset = fromSensorInfo->offset;
                    toSensorInfo->slopeFactor = fromSensorInfo->slopeFactor;
                    toSensorInfo->CalMeanLowQC = fromSensorInfo->CalMeanLowQC;
                    toSensorInfo->CalMeanHighQC = fromSensorInfo->CalMeanHighQC;
                    toSensorInfo->CalDriftLowQC = fromSensorInfo->CalDriftLowQC;
                    toSensorInfo->CalDriftHighQC = fromSensorInfo->CalDriftHighQC;
                    toSensorInfo->CalSecondLowQC = fromSensorInfo->CalSecondLowQC;
                    toSensorInfo->CalSecondHighQC = fromSensorInfo->CalSecondHighQC;
                    toSensorInfo->CalNoiseHighQC = fromSensorInfo->CalNoiseHighQC;
                    toSensorInfo->SampleMeanLowQC = fromSensorInfo->SampleMeanLowQC;
                    toSensorInfo->SampleMeanHighQC = fromSensorInfo->SampleMeanHighQC;
                    toSensorInfo->SampleDriftLowQC = fromSensorInfo->SampleDriftLowQC;
                    toSensorInfo->SampleDriftHighQC = fromSensorInfo->SampleDriftHighQC;
                    toSensorInfo->SampleSecondLowQC = fromSensorInfo->SampleSecondLowQC;
                    toSensorInfo->SampleSecondHighQC = fromSensorInfo->SampleSecondHighQC;
                    toSensorInfo->SampleNoiseHighQC = fromSensorInfo->SampleNoiseHighQC;
                    toSensorInfo->PostMeanLowQC = fromSensorInfo->PostMeanLowQC;
                    toSensorInfo->PostMeanHighQC = fromSensorInfo->PostMeanHighQC;
                    toSensorInfo->PostDriftLowQC = fromSensorInfo->PostDriftLowQC;
                    toSensorInfo->PostDriftHighQC = fromSensorInfo->PostDriftHighQC;
                    toSensorInfo->PostSecondLowQC = fromSensorInfo->PostSecondLowQC;
                    toSensorInfo->PostSecondHighQC = fromSensorInfo->PostSecondHighQC;
                    toSensorInfo->PostNoiseHighQC = fromSensorInfo->PostNoiseHighQC;
                    toSensorInfo->DeltaDriftLowQC = fromSensorInfo->DeltaDriftLowQC;
                    toSensorInfo->DeltaDriftHighQC = fromSensorInfo->DeltaDriftHighQC;
                    toSensorInfo->param1 = fromSensorInfo->param1;
                    toSensorInfo->param2 = fromSensorInfo->param2;
                    toSensorInfo->param3 = fromSensorInfo->param3;
                    toSensorInfo->param4 = fromSensorInfo->param4;
                    toSensorInfo->param5 = fromSensorInfo->param5;
                    toSensorInfo->param6 = fromSensorInfo->param6;
                    toSensorInfo->param7 = fromSensorInfo->param7;
                    toSensorInfo->param8 = fromSensorInfo->param8;
                    toSensorInfo->param9 = fromSensorInfo->param9;
                    toSensorInfo->param10 = fromSensorInfo->param10;
                    toSensorInfo->param11 = fromSensorInfo->param11;
                    toSensorInfo->param12 = fromSensorInfo->param12;
                    toSensorInfo->param13 = fromSensorInfo->param13;
                    toSensorInfo->param14 = fromSensorInfo->param14;
                    toSensorInfo->param15 = fromSensorInfo->param15;
                    toSensorInfo->param16 = fromSensorInfo->param16;
                    toSensorInfo->param17 = fromSensorInfo->param17;
                    toSensorInfo->param18 = fromSensorInfo->param18;
                    toSensorInfo->param19 = fromSensorInfo->param19;
                    toSensorInfo->param20 = fromSensorInfo->param20;
                    toSensorInfo->param21 = fromSensorInfo->param21;
                    toSensorInfo->param22 = fromSensorInfo->param22;
                    toSensorInfo->param23 = fromSensorInfo->param23;
                    toSensorInfo->param24 = fromSensorInfo->param24;
                    toSensorInfo->param25 = fromSensorInfo->param25;
                    toSensorInfo->param26 = fromSensorInfo->param26;
                    toSensorInfo->param27 = fromSensorInfo->param27;
                    toSensorInfo->param28 = fromSensorInfo->param28;
                    toSensorInfo->param29 = fromSensorInfo->param29;
                    toSensorInfo->param30 = fromSensorInfo->param30;
                    toSensorInfo->param30 = fromSensorInfo->param30;
                    toSensorInfo->param31 = fromSensorInfo->param31;
                    toSensorInfo->param32 = fromSensorInfo->param32;
                    toSensorInfo->param33 = fromSensorInfo->param33;
                    toSensorInfo->param34 = fromSensorInfo->param34;
                    toSensorInfo->param35 = fromSensorInfo->param35;
                    toSensorInfo->param36 = fromSensorInfo->param36;
                    toSensorInfo->param37 = fromSensorInfo->param37;
                    toSensorInfo->param38 = fromSensorInfo->param38;
                    toSensorInfo->param39 = fromSensorInfo->param39;
                    toSensorInfo->param40 = fromSensorInfo->param40;
                    toSensorInfo->param41 = fromSensorInfo->param41;
                    toSensorInfo->param42 = fromSensorInfo->param42;
                    toSensorInfo->param43 = fromSensorInfo->param43;
                    toSensorInfo->param44 = fromSensorInfo->param44;
                    toSensorInfo->param45 = fromSensorInfo->param45;
                    toSensorInfo->param46 = fromSensorInfo->param46;
                    toSensorInfo->param47 = fromSensorInfo->param47;
                    toSensorInfo->param48 = fromSensorInfo->param48;
                    toSensorInfo->param49 = fromSensorInfo->param49;
                    toSensorInfo->param50 = fromSensorInfo->param50;
                    toSensorInfo->param51 = fromSensorInfo->param51;
                    toSensorInfo->param52 = fromSensorInfo->param52;
                    toSensorInfo->param53 = fromSensorInfo->param53;
                    toSensorInfo->param54 = fromSensorInfo->param54;
                    toSensorInfo->param55 = fromSensorInfo->param55;
                    toSensorInfo->param56 = fromSensorInfo->param56;
                    toSensorInfo->param57 = fromSensorInfo->param57;
                    toSensorInfo->param58 = fromSensorInfo->param58;
                    toSensorInfo->param59 = fromSensorInfo->param59;
                    toSensorInfo->param60 = fromSensorInfo->param60;
                    toSensorInfo->param61 = fromSensorInfo->param61;
                    toSensorInfo->param62 = fromSensorInfo->param62;
                    toSensorInfo->param63 = fromSensorInfo->param63;
                    toSensorInfo->param64 = fromSensorInfo->param64;
                    toSensorInfo->param65 = fromSensorInfo->param65;
                    toSensorInfo->param66 = fromSensorInfo->param66;
                    toSensorInfo->param67 = fromSensorInfo->param67;
                    toSensorInfo->param68 = fromSensorInfo->param68;
                    toSensorInfo->param69 = fromSensorInfo->param69;
                    toSensorInfo->param70 = fromSensorInfo->param70;
                    toSensorInfo->param71 = fromSensorInfo->param71;
                    toSensorInfo->param72 = fromSensorInfo->param72;
                    toSensorInfo->param73 = fromSensorInfo->param73;
                    toSensorInfo->param74 = fromSensorInfo->param74;
                    toSensorInfo->param75 = fromSensorInfo->param75;
                    toSensorInfo->param76 = fromSensorInfo->param76;
                    toSensorInfo->param77 = fromSensorInfo->param77;
                    toSensorInfo->param78 = fromSensorInfo->param78;
                    toSensorInfo->param79 = fromSensorInfo->param79;
                    toSensorInfo->param80 = fromSensorInfo->param80;
                    toSensorInfo->param81 = fromSensorInfo->param81;
                    toSensorInfo->param82 = fromSensorInfo->param82;
                    toSensorInfo->param83 = fromSensorInfo->param83;
                    toSensorInfo->param84 = fromSensorInfo->param84;
                    toSensorInfo->param85 = fromSensorInfo->param85;
                    toSensorInfo->param86 = fromSensorInfo->param86;
                    toSensorInfo->param87 = fromSensorInfo->param87;
                    toSensorInfo->param88 = fromSensorInfo->param88;
                    toSensorInfo->param89 = fromSensorInfo->param89;
                    toSensorInfo->param90 = fromSensorInfo->param90;
                    toSensorInfo->param91 = fromSensorInfo->param91;
                    toSensorInfo->param92 = fromSensorInfo->param92;
                    toSensorInfo->param93 = fromSensorInfo->param93;
                    toSensorInfo->param94 = fromSensorInfo->param94;
                    toSensorInfo->param95 = fromSensorInfo->param95;
                    toSensorInfo->param96 = fromSensorInfo->param96;
                    toSensorInfo->param97 = fromSensorInfo->param97;
                    toSensorInfo->param98 = fromSensorInfo->param98;
                    toSensorInfo->param99 = fromSensorInfo->param99;
                    toSensorInfo->param100 = fromSensorInfo->param100;
                    toSensorInfo->readerMeanLow = fromSensorInfo->readerMeanLow;
                    toSensorInfo->readerMeanHigh = fromSensorInfo->readerMeanHigh;
                    toSensorInfo->readerDriftLow = fromSensorInfo->readerDriftLow;
                    toSensorInfo->readerDriftHigh = fromSensorInfo->readerDriftHigh;
                    toSensorInfo->readerNoiseLow = fromSensorInfo->readerNoiseLow;
                    toSensorInfo->readerNoiseHigh = fromSensorInfo->readerNoiseHigh;
                    toSensorInfo->tMinus = fromSensorInfo->tMinus;
                    toSensorInfo->tPlus = fromSensorInfo->tPlus;
                    toSensorInfo->postCurvatureWeight = fromSensorInfo->postCurvatureWeight;
                    toSensorInfo->bloodPointsToSkip = fromSensorInfo->bloodPointsToSkip;
                    toSensorInfo->bloodPointsInWindow = fromSensorInfo->bloodPointsInWindow;
                    toSensorInfo->bloodNoiseHigh = fromSensorInfo->bloodNoiseHigh;
                    toSensorInfo->aqPointsToSkip = fromSensorInfo->aqPointsToSkip;
                    toSensorInfo->aqPointsInWindow = fromSensorInfo->aqPointsInWindow;
                    toSensorInfo->aqNoiseHigh = fromSensorInfo->aqNoiseHigh;

                    toSensorInfo->rtPointLimitLow = fromSensorInfo->rtPointLimitLow;
                    toSensorInfo->rtPointLimitHigh = fromSensorInfo->rtPointLimitHigh;
                    toSensorInfo->d1Low = fromSensorInfo->d1Low;
                    toSensorInfo->d1High = fromSensorInfo->d1High;
                    toSensorInfo->p1d2Low = fromSensorInfo->p1d2Low;
                    toSensorInfo->p1d2High = fromSensorInfo->p1d2High;
                    toSensorInfo->p2d2Low = fromSensorInfo->p2d2Low;
                    toSensorInfo->p2d2High = fromSensorInfo->p2d2High;
                    toSensorInfo->p3d2Low = fromSensorInfo->p3d2Low;
                    toSensorInfo->p3d2High = fromSensorInfo->p3d2High;
                    toSensorInfo->A = fromSensorInfo->A;
                    toSensorInfo->B = fromSensorInfo->B;
                    toSensorInfo->C = fromSensorInfo->C;
                    toSensorInfo->D = fromSensorInfo->D;
                    toSensorInfo->F = fromSensorInfo->F;
                    toSensorInfo->G = fromSensorInfo->G;
                    toSensorInfo->TAmbOffset = fromSensorInfo->TAmbOffset;
                    toSensorInfo->InjectionTimeOffset = fromSensorInfo->InjectionTimeOffset;
                    toSensorInfo->AgeOffset = fromSensorInfo->AgeOffset;
                    toSensorInfo->PowerOffset = fromSensorInfo->PowerOffset;

                    toSensorInfo->lateBloodPointsToSkip = fromSensorInfo->lateBloodPointsToSkip;
                    toSensorInfo->lateBloodPointsInWindow = fromSensorInfo->lateBloodPointsInWindow;
                    toSensorInfo->lateBloodNoiseHigh = fromSensorInfo->lateBloodNoiseHigh;
                    toSensorInfo->lateAqPointsToSkip = fromSensorInfo->lateAqPointsToSkip;
                    toSensorInfo->lateAqPointsInWindow = fromSensorInfo->lateAqPointsInWindow;
                    toSensorInfo->lateAqNoiseHigh = fromSensorInfo->lateAqNoiseHigh;

                    toSensorInfo->NeuralNetBlood = fromSensorInfo->NeuralNetBlood;
                    toSensorInfo->NeuralNetAQ = fromSensorInfo->NeuralNetAQ;
                }
            }
        }
    }
}
