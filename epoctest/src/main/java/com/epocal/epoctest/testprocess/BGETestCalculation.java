package com.epocal.epoctest.testprocess;

import com.epocal.common.am.BGEParameters;
import com.epocal.common.am.SensorInfo;
import com.epocal.common.am.SensorReadings;
//import com.epocal.common.types.HemodilutionPolicy;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.types.am.FluidType;
import com.epocal.common.types.am.RealTimeQCReturnCode;
import com.epocal.common.types.am.ResultsCalcReturnCode;
import com.epocal.common.types.am.Sensors;
import com.epocal.common.types.am.TestMode;
import com.epocal.epoctest.analyticalmanager.AnalyticalManager;
import com.epocal.epoctest.analyticalmanager.CalculateBGEResponse;
import com.epocal.reader.protocolcommontype.HostErrorCode;
import com.epocal.reader.type.AdditionalIdInfo;
import com.epocal.util.DateUtil;

import java.util.List;

/**
 * Created by dning on 10/17/2017.
 */

public class BGETestCalculation implements ITestCalculation.ICalcuate {

    public boolean calculateTestResult(TestDataProcessor testDataProcessor) {
        boolean applymTCO2 = false;
        boolean itemIsAdded = true;

        //modified to get sibCid for compatibility with Legacy reader by rzhuang at Sep 10 2018
        AdditionalIdInfo additionalIdInfo = testDataProcessor.getDeviceIdInfoData().getDeviceIdAdditionalIdInfo();

        BGEParameters bgeParameters = new BGEParameters();
        itemIsAdded = itemIsAdded && bgeParameters.addParam(testDataProcessor.getBubbleDetect());
        itemIsAdded = itemIsAdded && bgeParameters.addParam(testDataProcessor.getSampleDetect());

        if (additionalIdInfo != null) {
            itemIsAdded = itemIsAdded && bgeParameters.addParam(testDataProcessor.getAmbientTemperature());
            itemIsAdded = itemIsAdded && bgeParameters.addParam(testDataProcessor.getBarometricPressureSensor());
        } else {    //legacy
            itemIsAdded = itemIsAdded && bgeParameters.addParam(testDataProcessor.mRsrMsg.hwStatus.getAmbientTemp1());
            itemIsAdded = itemIsAdded && bgeParameters.addParam(testDataProcessor.mRsrMsg.hwStatus.getBarometricPressureSensor());
        }

        //legacy reader always be true for recalculate
        if (testDataProcessor.isTestFirstTimeSaved() && additionalIdInfo != null) {
            itemIsAdded = itemIsAdded && bgeParameters.addParam(false);// param 4 apply multiplication.. only applied first time through
        } else {
            itemIsAdded = itemIsAdded && bgeParameters.addParam(true); // param 4 apply multiplication
        }

        if (testDataProcessor.getTestRecord().getTestMode() == TestMode.BloodTest) {
            itemIsAdded = itemIsAdded && bgeParameters.addParam(FluidType.Blood);
        } else {
            itemIsAdded = itemIsAdded && bgeParameters.addParam(FluidType.Aqueuous);
        }

        // find the sensor descriptor for the top heater pid
        SensorReadings tempSensorReadings = new SensorReadings();
        tempSensorReadings.readings = testDataProcessor.getTopHeaterPid();
        tempSensorReadings.sensorDescriptor = new SensorInfo();

        // if it was not found
        if (additionalIdInfo != null) {
            if (!testDataProcessor.getTestConfiguration().getSensorInfoBySensorType(Sensors.TopHeaterPid, (short) 0, tempSensorReadings.sensorDescriptor, testDataProcessor.getBarcodeInformation().getCardMade())) {
                // put null for the sensor descriptor
                tempSensorReadings.sensorDescriptor = null;
            }
        } else {    //Legacy reader
            if (!testDataProcessor.getTestConfiguration().legacyGetSensorInfoBySensorType(
                    testDataProcessor.mSensorLayoutNumber,Sensors.TopHeaterPid, (short) 0,
                    tempSensorReadings.sensorDescriptor,testDataProcessor.getBarcodeInformation().getCardMade())) {
                // put null for the sensor descriptor
                tempSensorReadings.sensorDescriptor = null;
            }
        }

        // send in the pid sensorreadings and the date the card was made
        itemIsAdded = itemIsAdded && bgeParameters.addParam(tempSensorReadings);// paramter 6 top heater

        // pass in the card age
        itemIsAdded = itemIsAdded && bgeParameters.addParam(DateUtil.daysBetween(testDataProcessor.getBarcodeInformation().getCardMade(),
                testDataProcessor.getTestTime())); // parameter 7 age of card

        //HostConfiguration hostConfig = new HostConfigurationModel().getUnmanagedHostConfiguration();
        // and the sample type (for cpb stuff)
        Boolean isHemodilutionApplied = testDataProcessor.getTestRecord().getTestDetail().getHemodilutionApplied();

        if (testDataProcessor.getTestRecord().getTestMode() == TestMode.BloodTest) {
                if (testDataProcessor.getTestRecord().getTestDetail().getHemodilutionApplied() != null && testDataProcessor.getTestRecord().getTestDetail().getHemodilutionApplied()) {
                    itemIsAdded = itemIsAdded && bgeParameters.addParam(true);
                } else {
                    itemIsAdded = itemIsAdded && bgeParameters.addParam(false);
                }

        } else {
            itemIsAdded = itemIsAdded && bgeParameters.addParam(false);
        }

        itemIsAdded = itemIsAdded && bgeParameters.addParam(testDataProcessor.getBubbleEnd() - testDataProcessor.getBubbleBegin());

        if(additionalIdInfo == null) {  //Legacy Reader
            itemIsAdded = itemIsAdded && bgeParameters.addParam(testDataProcessor.
                    getDeviceIdInfoData().getLegacyDeviceInfo().getSibCid());
            applymTCO2 = testDataProcessor.ifTCO2SupportedByCardTypeAndEnabled();
        } else {
            itemIsAdded = itemIsAdded && bgeParameters.addParam((int) additionalIdInfo.getSibCid());
        }

        for (int i = 0; i < testDataProcessor.getTestReadings().size(); i++) {
            testDataProcessor.getTestReadings().get(i).returnCode = ResultsCalcReturnCode.Success;
            testDataProcessor.getTestReadings().get(i).requirementsFailedQC = false;
        }

        if (testDataProcessor.getTestRecord().getTestMode() == TestMode.BloodTest) {
            // add insanity ranges for agap and bicarb
            itemIsAdded = itemIsAdded && bgeParameters.addParam(testDataProcessor.getTestConfiguration().getInsanityRange(AnalyteName.AnionGap).Low);
            itemIsAdded = itemIsAdded && bgeParameters.addParam(testDataProcessor.getTestConfiguration().getInsanityRange(AnalyteName.AnionGap).High);
            itemIsAdded = itemIsAdded && bgeParameters.addParam(testDataProcessor.getTestConfiguration().getInsanityRange(AnalyteName.HCO3act).Low);
            itemIsAdded = itemIsAdded && bgeParameters.addParam(testDataProcessor.getTestConfiguration().getInsanityRange(AnalyteName.HCO3act).High);
        } else {
            itemIsAdded = itemIsAdded && bgeParameters.addParam(testDataProcessor.getTestConfiguration().getInsanityRange(AnalyteName.AnionGap).QALow);
            itemIsAdded = itemIsAdded && bgeParameters.addParam(testDataProcessor.getTestConfiguration().getInsanityRange(AnalyteName.AnionGap).QAHigh);
            itemIsAdded = itemIsAdded && bgeParameters.addParam(testDataProcessor.getTestConfiguration().getInsanityRange(AnalyteName.HCO3act).QALow);
            itemIsAdded = itemIsAdded && bgeParameters.addParam(testDataProcessor.getTestConfiguration().getInsanityRange(AnalyteName.HCO3act).QAHigh);
        }
        itemIsAdded = itemIsAdded && bgeParameters.addParam(applymTCO2);
        CalculateBGEResponse response = AnalyticalManager.calculateBGE(testDataProcessor.getTestReadings(), bgeParameters);

        // 3. Check the response
        if (!response.isSuccess()) {
            return false;
        }
        List<SensorReadings> sensorReadingsListInResponse = response.getSensorReadings();

        //update original list
        for (int i = 0; i < testDataProcessor.getTestReadings().size(); i++) {
            SensorReadings sensorReadings = null;
            for (int j = 0; j < sensorReadingsListInResponse.size(); j++) {
                if (sensorReadingsListInResponse.get(j).analyte == testDataProcessor.getTestReadings().get(i).analyte) {
                    sensorReadings = sensorReadingsListInResponse.get(j);
                    break;
                }
            }
            if (sensorReadings == null) {
                continue;
            }
            testDataProcessor.getTestReadings().get(i).returnCode = sensorReadings.returnCode;
            testDataProcessor.getTestReadings().get(i).result = sensorReadings.result;
            testDataProcessor.getTestReadings().get(i).realTimeQCFailedEver = sensorReadings.realTimeQCFailedEver;
        }

        for (int i = 0; i < testDataProcessor.getTestReadings().size(); i++) {
            if (testDataProcessor.getTestReadings().get(i).realTimeQCFailedEver == RealTimeQCReturnCode.Failed) {
                testDataProcessor.getTestReadings().get(i).returnCode = ResultsCalcReturnCode.FailedQCEver;
            }
        }

        try {
            // retrieve the samplefailedqc indicator added by AnalyticalManager from the end of the parameters list
            BGEParameters bgeParametersResponse = response.getParams();
            if (bgeParametersResponse.getParamBoolean(bgeParametersResponse.count() - 1)) {
                //todo hardcode mErrorCode = HostErrorCode.NoError for phase 1 SQA validation
                testDataProcessor.setErrorCode(HostErrorCode.NoError);
                //testDataProcessor.setErrorCode(HostErrorCode.SampleFailedQC);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean terminateCalculation() {

        return true;
    }
}
