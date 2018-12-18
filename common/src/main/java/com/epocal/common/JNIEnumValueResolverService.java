package com.epocal.common;

public class JNIEnumValueResolverService {

    static {
        System.loadLibrary("enumcommon-lib");
    }

    private JNIEnumValueResolverService() {}

    /**
     * Native methods to access enums defined in C++ in the 'enumcommon-lib' native library.
     */
    // enum ChannelType
    private native static int jniChannelTypeValue(int enumIndex);
    public static int ChannelTypeValue(int enumIndex){return jniChannelTypeValue(enumIndex);}

    // enum ResultsCalcReturnCode
    private native static int jniResultsCalcReturnCodeValue(int enumIndex);
    public static int ResultsCalcReturnCodeValue(int enumIndex){return jniResultsCalcReturnCodeValue(enumIndex);}

    // enum AnalyteName
    private native static int jniAnalytesValue(int enumIndex);
    public static int AnalyteNameValue(int enumIndex){return jniAnalytesValue(enumIndex);}

    // enum FluidType
    private native static int jniFluidTypeValue(int enumIndex);
    public static int FluidTypeValue(int enumIndex){return jniFluidTypeValue(enumIndex);}

    // enum HumidityReturnCode
    private native static int jniHumidityReturnCodeValue(int enumIndex);
    public static int HumidityReturnCodeValue(int enumIndex){return jniHumidityReturnCodeValue(enumIndex);}

    // enum RealTimeQCReturnCode
    private native static int jniRealTimeQCReturnCodeValue(int enumIndex);
    public static int RealTimeQCReturnCodeValue(int enumIndex){return jniRealTimeQCReturnCodeValue(enumIndex);}

    // enum Sensors
    private native static int jniSensorsValue(int enumIndex);
    public static int SensorsValue(int enumIndex){return jniSensorsValue(enumIndex);}

    // enum AgeCategory
    private native static int jniAgeCategoryValue(int enumIndex);
    public static int AgeCategoryValue(int enumIndex){return jniAgeCategoryValue(enumIndex);}

    // enum DrawSites
    private native static int jniDrawSitesValue(int enumIndex);
    public static int DrawSitesValue(int enumIndex){return jniDrawSitesValue(enumIndex);}

    // enum fio2Units
    private native static int jniFio2UnitsValue(int enumIndex);
    public static int Fio2UnitsValue(int enumIndex){return jniFio2UnitsValue(enumIndex);}

    // enum allensTest
    private native static int jniAllensTestValue(int enumIndex);
    public static int AllensTestValue(int enumIndex){return jniAllensTestValue(enumIndex);}

    // enum BloodSampleType
    private native static int jniBloodSampleTypeValue(int enumIndex);
    public static int BloodSampleTypeValue(int enumIndex){return jniBloodSampleTypeValue(enumIndex);}

    // enum Gender
    private native static int jniGenderValue(int enumIndex);
    public static int GenderValue(int enumIndex){return jniGenderValue(enumIndex);}

    // enum DeliverySystem
    private native static int jniDeliverySystemValue(int enumIndex);
    public static int DeliverySystemValue(int enumIndex){return jniDeliverySystemValue(enumIndex);}

    // enum RespiratoryMode
    private native static int jniRespiratoryModeValue(int enumIndex);
    public static int RespiratoryModeValue(int enumIndex){return jniRespiratoryModeValue(enumIndex);}

    // enum TestMode
    private native static int jniTestModeValue(int enumIndex);
    public static int TestModeValue(int enumIndex){return jniTestModeValue(enumIndex);}

    // enum AGAPType
    private native static int jniAGAPTypeValue(int enumIndex);
    public static int AGAPTypeValue(int enumIndex){return jniAGAPTypeValue(enumIndex);}

    // enum EGFRFormula
    private native static int jniEGFRFormulaValue(int enumIndex);
    public static int EGFRFormulaValue(int enumIndex){return jniEGFRFormulaValue(enumIndex);}

    // enum EGFRType
    private native static int jniEGFRTypeValue(int enumIndex);
    public static int EGFRTypeValue(int enumIndex){return jniEGFRTypeValue(enumIndex);}

    // enum Units
    private native static int jniUnitsValue(int enumIndex);
    public static int UnitsValue(int enumIndex){return jniUnitsValue(enumIndex);}

    // enum RealTimeQCType
    private native static int jniRealTimeQCTypeValue(int enumIndex);
    public static int RealTimeQCTypeValue(int enumIndex){return jniRealTimeQCTypeValue(enumIndex);}

    // enum RealTimeHematocritQCReturnCode
    private native static int jniRealTimeHematocritQCReturnCodeValue(int enumIndex);
    public static int RealTimeHematocritQCReturnCodeValue(int enumIndex){return jniRealTimeHematocritQCReturnCodeValue(enumIndex);}

    // enum BubbleDetectMode
    private native static int jniBubbleDetectModeValue(int enumIndex);
    public static int BubbleDetectModeValue(int enumIndex){return jniBubbleDetectModeValue(enumIndex);}

    // enum Temperatures
    private native static int jniTemperaturesValue(int enumIndex);
    public static int TemperaturesValue(int enumIndex){return jniTemperaturesValue(enumIndex);}
}
