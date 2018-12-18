package com.epocal.epoctest;

import com.epocal.common.am.*;
import com.epocal.common.types.am.*;
import com.epocal.epoctest.analyticalmanager.AnalyticalManager;
import com.epocal.epoctest.analyticalmanager.LibraryCallReturnCode;
import com.epocal.epoctest.analyticalmanager.CheckForEarlyInjectionResponse;
import com.epocal.epoctest.analyticalmanager.PerformRealTimeQCResponse;
import com.epocal.epoctest.analyticalmanager.ComputeCalculatedResultsResponse;
import com.epocal.epoctest.analyticalmanager.ComputeCorrectedResultsResponse;
import com.epocal.epoctest.analyticalmanager.CalculateBGEResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the examples of how to call Analytical Manager call.
 */
public class ExampleAnalyticalManagerCalls {

    public static void callCheckForEarlyInjection() {
        // 1. Fill the input parameters
        SensorReadings sensorReadings = new SensorReadings();

        // 2. Call AM library call
        CheckForEarlyInjectionResponse response = AnalyticalManager.checkForEarlyInjection(
                sensorReadings,
                sensorReadings,
                RealTimeHematocritQCReturnCode.LowResistance,
                3.14,
                3.14f,
                3.14);

        // 3. Check the response
        if (response.isSuccess()) {
            // 4.a - Success - unpack OUT parameters
            SensorReadings sensorReadingsInResponse = response.getHematocritReadings();
            RealTimeHematocritQCReturnCode returnCodeInResponse = response.getAmReturnCode();
        } else {
            // 4.b - Handle Error
            LibraryCallReturnCode errorCode = response.getErrorCode();
            String errorMessage = response.getErrorMessage();
        }

    }

    public static void callCalculateBGE() {
        // 1. Fill the input parameters
        SensorReadings sensorReadings = new SensorReadings();
        List<SensorReadings> sensorReadingsList = new ArrayList<>();
        sensorReadingsList.add(sensorReadings);
        BGEParameters bgeParameters = createBGEParameters();

        // 2. Call AM library call
        CalculateBGEResponse response = AnalyticalManager.calculateBGE(
                sensorReadingsList,
                bgeParameters);

        // 3. Check the response
        if (response.isSuccess()) {
            // 4.a - Success - unpack OUT parameters
            List<SensorReadings> sensorReadingsListInResponse = response.getSensorReadings();
            BGEParameters bgeParametersInResponse = response.getParams();
        } else {
            // 4.b - Handle Error
            LibraryCallReturnCode errorCode = response.getErrorCode();
            String errorMessage = response.getErrorMessage();
        }

    }

    public static void callComputeCalculatedResults() {
        // 1. Fill the input parameters
        FinalResult finalResult = new FinalResult();
        List<FinalResult> finalResultList = new ArrayList<>();
        finalResultList.add(finalResult);

        List<FinalResult> measuredResults = finalResultList;
        List<FinalResult> calculatedResults = finalResultList;

        double passedctHb = 3.14;
        double FiO2 = 3.14;
        double temperature = 3.14;
        double pressure = 3.14;
        TestMode testMode = TestMode.BloodTest;
        double patientAge = 3.14;
        Gender gender = Gender.Female;
        EGFRFormula egfrFormula = EGFRFormula.Japanese;
        double patientHeight = 3.14;
        AgeCategory ageCategory = AgeCategory.Newborn;
        double RQ = 3.14;
        boolean calculateAlveolar = true;
        boolean theApplymTCO2 = true;

        // 2. Call AM library call
        ComputeCalculatedResultsResponse response = AnalyticalManager.computeCalculatedResults(
                measuredResults,
                calculatedResults,
                passedctHb,
                FiO2,
                temperature,
                pressure,
                testMode,
                patientAge,
                gender,
                egfrFormula,
                patientHeight,
                ageCategory,
                RQ,
                calculateAlveolar,
                theApplymTCO2);

        // 3. Check the response
        if (response.isSuccess()) {
            // 4.a - Success - unpack OUT parameters
            List<FinalResult> calculatedResultsInResponse = response.getCalculatedResults();
        } else {
            // 4.b - Handle Error
            LibraryCallReturnCode errorCode = response.getErrorCode();
            String errorMessage = response.getErrorMessage();
        }
    }

    public static void callComputeCorrectedResults() {
        // 1. Fill the input parameters
        FinalResult finalResult = new FinalResult();
        List<FinalResult> finalResultList = new ArrayList<>();
        finalResultList.add(finalResult);

        List<FinalResult> measuredResults = finalResultList;
        List<FinalResult> correctedResults = finalResultList;
        double FiO2 = 3.14;
        double temperature = 3.14;
        double pressure = 3.14;
        double RQ = 3.14;
        boolean calculateAlveolar = true;

        // 2. Call AM library call
        ComputeCorrectedResultsResponse response = AnalyticalManager.computeCorrectedResults(
                measuredResults,
                correctedResults,
                temperature,
                pressure,
                FiO2,
                RQ,
                calculateAlveolar);

        // 3. Check the response
        if (response.isSuccess()) {
            // 4.a - Success - unpack OUT parameters
            List<FinalResult> correctedResultsInResponse = response.getCorrectedResults();
        } else {
            // 4.b - Handle Error
            LibraryCallReturnCode errorCode = response.getErrorCode();
            String errorMessage = response.getErrorMessage();
        }

    }

    public static void callPerformRealTimeQC() {
        // 1. Fill the input parameters
        RealTimeQC qcStruct = new RealTimeQC();
        SensorReadings sensorReadings = new SensorReadings();
        List<SensorReadings> sensorReadingsList = new ArrayList<>();
        sensorReadingsList.add(sensorReadings);
        float lastRecordedTime = 3.14f;

        // 2. Call AM library call
        PerformRealTimeQCResponse response = AnalyticalManager.performRealTimeQC(
                sensorReadingsList,
                qcStruct,
                lastRecordedTime);

        // 3. Check the response
        if (response.isSuccess()) {
            // 4.a - Success - unpack OUT parameters
            RealTimeQCReturnCode rc = response.getAmReturnCode();
        } else {
            // 4.b - Handle Error
            LibraryCallReturnCode errorCode = response.getErrorCode();
            String errorMessage = response.getErrorMessage();
        }

    }

    public static BGEParameters createBGEParameters() {
        SensorReadings sensorReadings = createSensorReadings();
        sensorReadings.sensorDescriptor = null;

        // Example of assiging the values to BGEParameters.
        // In this example, only few fields are assigned.
        // (You need to assign all fields needed for Analytical Manager Call!)

        // BGEParameter will perform the type validation when addParam() is called.
        // It returns boolean "false" when addParam() is not successful.
        // (You need to check the return value from addParam() and handle error!)
        boolean itemIsAdded = true;
        BGEParameters bgeParameters = new BGEParameters();
        itemIsAdded = itemIsAdded && bgeParameters.addParam(Double.NaN);
        if (!itemIsAdded) {
            // ERROR: Handle Error
        }
        itemIsAdded = itemIsAdded && bgeParameters.addParam(3.14);
        if (!itemIsAdded) {
            // ERROR: handle error
        }
        itemIsAdded = itemIsAdded && bgeParameters.addParam(3.14f);
        if (!itemIsAdded) {
            // ERROR: handle error
        }
        itemIsAdded = itemIsAdded && bgeParameters.addParam(3.14f);
        if (!itemIsAdded) {
            // ERROR: handle error
        }
        itemIsAdded = itemIsAdded && bgeParameters.addParam(true);
        if (!itemIsAdded) {
            // ERROR: handle error
        }
        itemIsAdded = itemIsAdded && bgeParameters.addParam(FluidType.HematocritControl);
        if (!itemIsAdded) {
            // ERROR: handle error
        }
        itemIsAdded = itemIsAdded && bgeParameters.addParam(sensorReadings);
        if (!itemIsAdded) {
            // ERROR: handle error
        }
        itemIsAdded = itemIsAdded && bgeParameters.addParam(999);
        if (!itemIsAdded) {
            // ERROR: handle error
        }
        itemIsAdded = itemIsAdded && bgeParameters.addParam(true);
        if (!itemIsAdded) {
            // ERROR: handle error
        }
        itemIsAdded = itemIsAdded && bgeParameters.addParam(3.14);
        if (!itemIsAdded) {
            // ERROR: handle error
        }
        itemIsAdded = itemIsAdded && bgeParameters.addParam(999);
        if (!itemIsAdded) {
            // ERROR: handle error
        }
        itemIsAdded = itemIsAdded && bgeParameters.addParam(3.14);
        if (!itemIsAdded) {
            // ERROR: handle error
        }
        itemIsAdded = itemIsAdded && bgeParameters.addParam(3.14);
        if (!itemIsAdded) {
            // ERROR: handle error
        }
        itemIsAdded = itemIsAdded && bgeParameters.addParam(3.14);
        if (!itemIsAdded) {
            // ERROR: handle error
        }
        itemIsAdded = itemIsAdded && bgeParameters.addParam(3.14);
        if (!itemIsAdded) {
            // ERROR: handle error
        }
        itemIsAdded = itemIsAdded && bgeParameters.addParam(true);
        if (!itemIsAdded) {
            // ERROR: handle error
        }
        return bgeParameters;
    }

    public static void retrieveBGEParametersValue() {
        BGEParameters bgeParameters = createBGEParameters();
        // Count -- contains how many items in the bgeParameters.
        int itemCount = bgeParameters.count();

        try {
            // When retrieving the value, surround by try-catch
            // If the item is not found in bgeParameter it will raise
            // exception.

            // When retrieving the value, specify the index and type (e.g) Double
            double doubleValue = bgeParameters.getParamDouble(0);
            int intValue = bgeParameters.getParamInt(7);
            boolean boolValue = bgeParameters.getParamBoolean(4);
            FluidType fluidType = bgeParameters.getParamFluidType(5);
            SensorReadings sensorReadings = bgeParameters.getParamSensorReadings(6);

        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException arrayIndexError) {
            // Error: handle error
            // getParam[Type] will throw exception when
            // item is not found at specified index.
            arrayIndexError.printStackTrace();
        }
    }

    static SensorReadings createSensorReadings() {
        Reading reading = new Reading();
        reading.time = 12345;
        reading.value = 6789;

        // Example of assiging the values to SensorInfo.
        // In this example, only few fields are assigned.
        // (You need to assign all fields needed for Analytical Manager Call!)
        SensorInfo sensorInfo = new SensorInfo();
        sensorInfo.channelType = ChannelType.P2;
        sensorInfo.sensorType = Sensors.Conductivity;
        sensorInfo.A = 170;
        sensorInfo.B = 171;
        sensorInfo.C = 172;
        sensorInfo.D = 173;
        sensorInfo.F = 174;
        sensorInfo.G = 175;
        sensorInfo.PowerOffset = 179;

        // Example of assiging the values to Levels.
        // In this example, only few fields are assigned.
        // (You need to assign all fields needed for Analytical Manager Call!)
        Levels levels = new Levels();
        levels.output15 = 53;
        levels.output16 = 54;
        levels.output17 = 55;
        levels.output18 = 56;
        levels.output19 = 57;
        levels.output20 = 58;

        // Example of assiging the values to SensorReadings.
        // In this example, only few fields are assigned.
        // (You need to assign all fields needed for Analytical Manager Call!)
        SensorReadings sensorReadings = new SensorReadings();
        sensorReadings.requirementsFailedQC = true;
        sensorReadings.returnCode = ResultsCalcReturnCode.CalMeanQCHigh;
        sensorReadings.readings.add(reading);
        sensorReadings.readings.add(reading);
        sensorReadings.readingPointer = 0;
        sensorReadings.sensorDescriptor = sensorInfo;
        sensorReadings.levels = levels;
        sensorReadings.extraString = "Hello";
        sensorReadings.resultString = "World";

        return sensorReadings;
    }
}
