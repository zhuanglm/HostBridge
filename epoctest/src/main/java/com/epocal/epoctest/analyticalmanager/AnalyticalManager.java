package com.epocal.epoctest.analyticalmanager;

import android.util.Log;

import com.epocal.common.am.BGEParameters;
import com.epocal.common.am.FinalResult;
import com.epocal.common.am.RealTimeQC;
import com.epocal.common.am.SensorReadings;
import com.epocal.common.types.am.AgeCategory;
import com.epocal.common.types.am.EGFRFormula;
import com.epocal.common.types.am.Gender;
import com.epocal.common.types.am.RealTimeHematocritQCReturnCode;
import com.epocal.common.types.am.ResultsCalcReturnCode;
import com.epocal.common.types.am.TestMode;

import java.util.List;

public class AnalyticalManager {
    /**
     * Used to load the 'analyticalmanager-lib' library.
     */
    static {
        System.loadLibrary("analyticalmanager-lib");
    }

    /**
     * Native methods to access enums defined in C++ in the 'analyticalmanager-lib' native library.
     */
    private native static int jniLibraryCallReturnCodeValue(int enumIndex);

    public static int getLibraryCallReturnCodeValue(int enumIndex) {
        return jniLibraryCallReturnCodeValue(enumIndex);
    }

    private native static int jniAnalyticalManagerParamTypeValue(int index);

    public enum AnalyticalManagerParamType {
        /**
         * Set each entry by referencing the C++ enum entry's index for each entry, in
         * sequential order.  The index is used to lookup the value for the enum as defined
         * in the native enum definition.
         *
         * NOTE: enumValue(xyz) => "xyz" is not the property's value, rather it is the index of the
         * entry as defined in the native C++ enum definition.
         *      e.g. C++11
         *              enum class MyEnum : int
         *              {
         *                  enumEntry0 = 123,
         *                  enumEntry1 = 456,
         *                  enumEntry2 = 789,
         *              };
         *
         *           Java
         *              public enum MyEnum {
         *                  enumEntry_Index_0(0),
         *                  enumEntry_Index_1(1),
         *                  enumEntry_Index_2(2);
         *
         *              ...
         *              }
         */

        bubbleDetect(0),
        sampleDetect(1),
        ambientTemperature(2);

        public final int value;
        public final int index;

        AnalyticalManagerParamType(int enumIndex) {
            // Get the actual enum value from the C++ enum via the jniAnalyticalManagerParamTypeValue() JNI call
            this.value = jniAnalyticalManagerParamTypeValue(enumIndex);
            this.index = enumIndex;
        }

        public static AnalyticalManagerParamType convert(int value) {
            int enumIndex = Integer.MAX_VALUE;

            for (AnalyticalManagerParamType rc : AnalyticalManagerParamType.values()) {
                if (rc.value == value) {
                    enumIndex = rc.index;
                    break;
                }
            }

            if (enumIndex == Integer.MAX_VALUE)
                throw new IndexOutOfBoundsException();

            return AnalyticalManagerParamType.values()[enumIndex];
        }
    }

    /**
     * Native methods to call functions defined in C++ in the 'analyticalmanager-lib' native library.
     */
    private native static String jniVersion();

    public static String version() {
        return jniVersion();
    }

    private native static double jniFullNernst();

    public static double fullNernst() {
        return jniFullNernst();
    }

    private native static int jniLowNoiseSIBVersion();

    public static int lowNoiseSIBVersion() {
        return jniLowNoiseSIBVersion();
    }

    private native static byte[] jniCheckForEarlyInjection(byte[] serializedData, int serializedDataSize);

    public static CheckForEarlyInjectionResponse checkForEarlyInjection(SensorReadings hematocritReadings,
                                                                        SensorReadings topHeaterReadings,
                                                                        RealTimeHematocritQCReturnCode previousReturnCode,
                                                                        double airAfterFluidThreshold,
                                                                        float lastRecordedTime,
                                                                        double firstFluid) {
        // 1. Serialize input parameters into a byte array (using protobuf)
        byte[] serializedInputData = new byte[0];
        try {
            serializedInputData = AMSerializer.serializeCheckForEarlyInjectionRequest(
                    hematocritReadings,
                    topHeaterReadings,
                    previousReturnCode,
                    airAfterFluidThreshold,
                    lastRecordedTime,
                    firstFluid);
        } catch (AMException ame) {
            // Create ErrorResponse
            ame.printStackTrace();
            Log.d("AnalyticalManager1", ame.getErrorMessage());
            return CheckForEarlyInjectionResponse.Builder.newBuilder(ame.getErrorCode(), ame.getErrorMessage()).build();
        }

        // 2. Call AM Library on C++
        byte[] serializedReturnedData = jniCheckForEarlyInjection(serializedInputData, serializedInputData.length);

        // 3. Check for NULL ptr
        if ((serializedReturnedData == null) || (serializedReturnedData.length == 0)) {
            Log.d("AnalyticalManager", "AnalyticalManager.checkForEarlyInjection: Response obj is NULL.");
            return CheckForEarlyInjectionResponse.Builder.newBuilder(LibraryCallReturnCode.AM_JAVA_PROTOBUF_DECODING_FAILED_DESERIALIZE, "AnalyticalManager.checkForEarlyInjection: Response obj is NULL.").build();
        }

        // 4. Deserialize response and return to the caller
        try {
            CheckForEarlyInjectionResponse rc = AMSerializer.deserializeCheckForEarlyInjectionResponse(serializedReturnedData);
            Log.d("AnalyticalManager", "AnalyticalManager.checkForEarlyInjection: AM C++ Library called: " + rc.getErrorCode().toString());
            Log.d("AnalyticalManager", "AnalyticalManager.checkForEarlyInjection: CheckForEarlyInjectionResponse = " + rc.getAmReturnCode().toString());
            return rc;
        } catch (AMException ame) {
            // Create ErrorResponse
            ame.printStackTrace();
            Log.d("AnalyticalManager2", ame.getErrorMessage());
            return CheckForEarlyInjectionResponse.Builder.newBuilder(ame.getErrorCode(), ame.getErrorMessage()).build();
        }
    }

    private native static byte[] jniPerformRealTimeQC(byte[] serializedData, int serializedDataSize);

    public static PerformRealTimeQCResponse performRealTimeQC(
            List<SensorReadings> testReadings,
            RealTimeQC qcStruct,
            float lastRecordedTime) {
        // 1. Serialize input parameters into a byte array (using protobuf)
        byte[] serializedInputData = new byte[0];
        try {
            serializedInputData = AMSerializer.serializePerformRealTimeQCRequest(testReadings, qcStruct, lastRecordedTime);
        } catch (AMException ame) {
            // Create ErrorResponse
            ame.printStackTrace();
            Log.d("AnalyticalManager3", ame.getErrorMessage());
            return PerformRealTimeQCResponse.Builder.newBuilder(ame.getErrorCode(), ame.getErrorMessage()).build();
        }

        // 2. Call AM Library on C++
        byte[] serializedReturnedData = jniPerformRealTimeQC(serializedInputData, serializedInputData.length);

        // 3. Check for NULL ptr
        if ((serializedReturnedData == null) || (serializedReturnedData.length == 0)) {
            Log.d("AnalyticalManager", "AnalyticalManager.performRealTimeQC: Response obj is NULL.");
            return PerformRealTimeQCResponse.Builder.newBuilder(LibraryCallReturnCode.AM_JAVA_PROTOBUF_DECODING_FAILED_DESERIALIZE, "AnalyticalManager.performRealTimeQC: Response obj is NULL.").build();
        }

        // 4. Deserialize response and return to the caller
        try {
            PerformRealTimeQCResponse rc = AMSerializer.deserializePerformRealTimeQCResponse(serializedReturnedData);
            Log.d("AnalyticalManager", "AnalyticalManager.performRealTimeQC: AM C++ Library called: " + rc.getErrorCode().toString());
            Log.d("AnalyticalManager", "AnalyticalManager.performRealTimeQC: PerformRealTimeQCResponse = " + rc.getAmReturnCode().toString());
            return rc;
        } catch (AMException ame) {
            // Create ErrorResponse
            ame.printStackTrace();
            Log.d("AnalyticalManager4", ame.getErrorMessage());
            return PerformRealTimeQCResponse.Builder.newBuilder(ame.getErrorCode(), ame.getErrorMessage()).build();
        }
    }

    private native static byte[] jniCalculateBGE(byte[] serializedData, int serializedDataSize);

    public static CalculateBGEResponse calculateBGE(List<SensorReadings> testReadings,
                                                    BGEParameters bgeParameters) {
        // 1. Serialize input parameters into a byte array (using protobuf)
        byte[] serializedInputData = new byte[0];

        // Notify AM that the Host does not want negative values
        // returned for glucose, lactate, or creatinine readings.
        boolean allowNegativeValues = false;

        try {
            serializedInputData = AMSerializer.serializeCalculateBGERequest(testReadings, bgeParameters, allowNegativeValues);
        } catch (AMException ame) {
            // Create ErrorResponse
            ame.printStackTrace();
            Log.d("AnalyticalManager5", ame.getErrorMessage());
            return CalculateBGEResponse.Builder.newBuilder(ame.getErrorCode(), ame.getErrorMessage()).build();
        }

        // 2. Call AM Library on C++
        byte[] serializedReturnedData = jniCalculateBGE(serializedInputData, serializedInputData.length);

        // 3. Check for NULL ptr
        if ((serializedReturnedData == null) || (serializedReturnedData.length == 0)) {
            Log.d("AnalyticalManager", "AnalyticalManager.calculateBGE: Response obj is NULL.");
            return CalculateBGEResponse.Builder.newBuilder(LibraryCallReturnCode.AM_JAVA_PROTOBUF_DECODING_FAILED_DESERIALIZE, "AnalyticalManager.calculateBGE: Response obj is NULL.").build();
        }

        // 4. Deserialize response and return to the caller
        try {
            CalculateBGEResponse rc = AMSerializer.deserializeCalculateBGEResponse(serializedReturnedData);
            Log.d("AnalyticalManager", "AnalyticalManager.calculateBGE: AM C++ Library called: " + rc.getErrorCode().toString());
            return rc;
        } catch (AMException ame) {
            // Create ErrorResponse
            ame.printStackTrace();
            Log.d("AnalyticalManager6", ame.getErrorMessage());
            return CalculateBGEResponse.Builder.newBuilder(ame.getErrorCode(), ame.getErrorMessage()).build();
        }
    }

    private native static boolean jniFailedIQC(int resultsCalReturnCode);

    public static boolean failedIQC(ResultsCalcReturnCode rc) {
        return jniFailedIQC(rc.value);
    }

    private native static boolean jniIsBadValue(double value);

    public static boolean isBadValue(double value) {
        return jniIsBadValue(value);
    }

    private native static byte[] jniComputeCalculatedResults(byte[] serializedData, int serializedDataSize);

    public static ComputeCalculatedResultsResponse computeCalculatedResults(
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
            boolean theApplymTCO2) {

        // 1. Serialize input parameters into a byte array (using protobuf)
        byte[] serializedInputData = new byte[0];
        try {
            serializedInputData = AMSerializer.serializeComputeCalculatedResultsRequest(
                    measuredResults,
                    calculatedResults,
                    passedctHb,
                    FiO2,
                    patientTemperature,
                    ambientPressure,
                    testMode,
                    patientAge,
                    gender,
                    egfrFormula,
                    patientHeight,
                    ageCategory,
                    RQ,
                    calculateAlveolar,
                    theApplymTCO2);
        } catch (AMException ame) {
            // Create ErrorResponse
            ame.printStackTrace();
            Log.d("AnalyticalManager7", ame.getErrorMessage());
            return ComputeCalculatedResultsResponse.Builder.newBuilder(ame.getErrorCode(), ame.getErrorMessage()).build();
        }

        // 2. Call AM Library on C++
        byte[] serializedReturnedData = jniComputeCalculatedResults(serializedInputData, serializedInputData.length);

        // 3. Check for NULL ptr
        if ((serializedReturnedData == null) || (serializedReturnedData.length == 0)) {
            Log.d("AnalyticalManager", "AnalyticalManager.computeCalculatedResults: Response obj is NULL.");
            return ComputeCalculatedResultsResponse.Builder.newBuilder(LibraryCallReturnCode.AM_JAVA_PROTOBUF_DECODING_FAILED_DESERIALIZE, "AnalyticalManager.computeCalculatedResults: Response obj is NULL.").build();
        }

        // 4. Deserialize response and return to the caller
        try {
            ComputeCalculatedResultsResponse rc = AMSerializer.deserializeComputeCalculatedResultsResponse(serializedReturnedData);
            Log.d("AnalyticalManager", "AnalyticalManager.computeCalculatedResults: AM C++ Library called: " + rc.getErrorCode().toString());
            return rc;
        } catch (AMException ame) {
            // Create ErrorResponse
            ame.printStackTrace();
            Log.d("AnalyticalManager8", ame.getErrorMessage());
            return ComputeCalculatedResultsResponse.Builder.newBuilder(ame.getErrorCode(), ame.getErrorMessage()).build();
        }
    }

    private native static byte[] jniComputeCorrectedResults(byte[] serializedData, int serializedDataSize);

    public static ComputeCorrectedResultsResponse computeCorrectedResults(
            List<FinalResult> measuredResults,
            List<FinalResult> correctedResults,
            double patientTemperature,
            double ambientPressure,
            double FiO2,
            double RQ,
            boolean calculateAlveolar) {
        // 1. Serialize input parameters into a byte array (using protobuf)
        byte[] serializedInputData = new byte[0];
        try {
            serializedInputData = AMSerializer.serializeComputeCorrectedResultsRequest(
                    measuredResults,
                    correctedResults,
                    patientTemperature,
                    ambientPressure,
                    FiO2,
                    RQ,
                    calculateAlveolar);
        } catch (AMException ame) {
            // Create ErrorResponse
            ame.printStackTrace();
            Log.d("AnalyticalManager9", ame.getErrorMessage());
            return ComputeCorrectedResultsResponse.Builder.newBuilder(ame.getErrorCode(), ame.getErrorMessage()).build();
        }

        // 2. Call AM Library on C++
        byte[] serializedReturnedData = jniComputeCorrectedResults(serializedInputData, serializedInputData.length);

        // 3. Check for NULL ptr
        if ((serializedReturnedData == null) || (serializedReturnedData.length == 0)) {
            Log.d("AnalyticalManager", "AnalyticalManager.computeCalculatedResults: Response obj is NULL.");
            return ComputeCorrectedResultsResponse.Builder.newBuilder(LibraryCallReturnCode.AM_JAVA_PROTOBUF_DECODING_FAILED_DESERIALIZE, "AnalyticalManager.computeCalculatedResults: Response obj is NULL.").build();
        }

        // 4. Deserialize response and return to the caller
        try {
            ComputeCorrectedResultsResponse rc = AMSerializer.deserializeComputeCorrectedResultsResponse(serializedReturnedData);
            Log.d("AnalyticalManager", "AnalyticalManager.computeCorrectedResults: AM C++ Library called: " + rc.getErrorCode().toString());
            return rc;
        } catch (AMException ame) {
            // Create ErrorResponse
            ame.printStackTrace();
            Log.d("AnalyticalManager10", ame.getErrorMessage());
            return ComputeCorrectedResultsResponse.Builder.newBuilder(ame.getErrorCode(), ame.getErrorMessage()).build();
        }
    }
}
