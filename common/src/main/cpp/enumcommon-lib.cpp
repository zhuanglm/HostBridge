
// Created by michde on 8/14/2017.
//

#include <jni.h>

#include <limits.h>
#include <AllensTest.h>
#include <Fio2Units.h>
#include <RespiratoryMode.h>
#include <TestMode.h>
#include <AGAPType.h>
#include <EGFRFormula.h>
#include <EGFRType.h>

#include "ChannelType.h"
#include "ResultsCalcReturnCode.h"
#include "HumidityReturnCodeMapper.h"
#include "RealTimeQCReturnCodeMapper.h"
#include "SensorsMapper.h"
#include "FluidTypeMapper.h"
#include "AgeCategoryMapper.h"
#include "DrawSitesMapper.h"
#include "BloodSampleTypeMapper.h"
#include "Gender.h"
#include "DeliverySystemMapper.h"
#include "UnitsMapper.h"
#include "RealTimeQCTypeMapper.h"
#include "RealTimeHematocritQCReturnCodeMapper.h"
#include "BubbleDetectModeMapper.h"
#include "TemperaturesMapper.h"
#include "RespiratoryModeMapper.h"
#include "ChannelTypeMapper.h"
#include "AnalytesMapper.h"
#include "ResultsCalcReturnCodeMapper.h"
#include "HgbResultMapper.h"

using namespace Epoc::Common::Native::Definitions;

#ifdef __cplusplus
extern "C" {
#endif  // __cplusplus

//*********//
/// ENUMS ///
//*********//

/////////////////////////////
/// ChannelType           ///
/////////////////////////////
JNIEXPORT jint JNICALL
Java_com_epocal_common_JNIEnumValueResolverService_jniChannelTypeValue(JNIEnv *env, jobject self,
                                                                       jint enumIndex)
{
    return (jint) ChannelTypeMapper::mapValue((int) enumIndex);
}


/////////////////////////////
/// ResultsCalcReturnCode ///
/////////////////////////////
JNIEXPORT jint JNICALL
Java_com_epocal_common_JNIEnumValueResolverService_jniResultsCalcReturnCodeValue(JNIEnv *env,
                                                                                 jobject self,
                                                                                 jint enumIndex)
{
    return (jint) ResultsCalcReturnCodeMapper::mapValue((int) enumIndex);
}

/////////////////////////////
/// Analytes              ///
/////////////////////////////
JNIEXPORT jint JNICALL
Java_com_epocal_common_JNIEnumValueResolverService_jniAnalytesValue(JNIEnv *env, jobject self,
                                                                    jint enumIndex)
{
    return (jint) AnalytesMapper::mapValue((int) enumIndex);
}

/////////////////////////////
/// HumidityReturnCode    ///
/////////////////////////////
JNIEXPORT jint JNICALL
Java_com_epocal_common_JNIEnumValueResolverService_jniHumidityReturnCodeValue(JNIEnv *env, jobject self,
                                                                       jint index) {
    return (jint) HumidityReturnCodeMapper::mapValue((int)index);
}

/////////////////////////////
/// RealTimeQCReturnCode  ///
/////////////////////////////
JNIEXPORT jint JNICALL
Java_com_epocal_common_JNIEnumValueResolverService_jniRealTimeQCReturnCodeValue(JNIEnv *env, jobject self,
                                                                              jint index) {
    return (jint) RealTimeQCReturnCodeMapper::mapValue((int)index);
}

/////////////////////////////
/// Sensors               ///
/////////////////////////////
JNIEXPORT jint JNICALL
Java_com_epocal_common_JNIEnumValueResolverService_jniSensorsValue(JNIEnv *env, jobject self,
                                                                   jint index) {
    return (jint) SensorsMapper::mapValue((int) index);
}


/////////////////////////////
/// FluidType             ///
/////////////////////////////
JNIEXPORT jint JNICALL
Java_com_epocal_common_JNIEnumValueResolverService_jniFluidTypeValue(JNIEnv *env, jobject self,
                                                              jint index) {
    return (jint) FluidTypeMapper::mapValue((int) index);
}

/////////////////////////////
/// AgeCategory           ///
/////////////////////////////
JNIEXPORT jint JNICALL
Java_com_epocal_common_JNIEnumValueResolverService_jniAgeCategoryValue(JNIEnv *env, jobject self,
                                                                       jint index) {
    return (jint) AgeCategoryMapper::mapValue((int) index);
}

/////////////////////////////
/// DrawSites             ///
/////////////////////////////
JNIEXPORT jint JNICALL
Java_com_epocal_common_JNIEnumValueResolverService_jniDrawSitesValue(JNIEnv *env, jobject self,
                                                                     jint enumIndex)
{
    return (jint) DrawSitesMapper::mapValue((int) enumIndex);
}

/////////////////////////////
/// Fio2Units             ///
/////////////////////////////
JNIEXPORT jint JNICALL
Java_com_epocal_common_JNIEnumValueResolverService_jniFio2UnitsValue(JNIEnv *env, jobject self,
                                                                     jint index) {
    jint enumValue = INT_MAX;
    switch(index)
    {
        case 0:
            enumValue = (jint)fio2Units::Percentage;
            break;
        case 1:
            enumValue = (jint)fio2Units::lpm;
            break;
    }
    return enumValue;
}

/////////////////////////////
/// AllensTest            ///
/////////////////////////////
JNIEXPORT jint JNICALL
Java_com_epocal_common_JNIEnumValueResolverService_jniAllensTestValue(JNIEnv *env, jobject self,
                                                                      jint index)
{
    jint enumValue = INT_MAX;
    switch (index) {
        case 0:
            enumValue = (jint) allensTest::Positive;
            break;
        case 1:
            enumValue = (jint) allensTest::Negative;
            break;
        case 2:
            enumValue = (jint) allensTest::NA;
            break;
    }
    return enumValue;
}

/////////////////////////////
/// BloodSampleType       ///
/////////////////////////////
JNIEXPORT jint JNICALL Java_com_epocal_common_JNIEnumValueResolverService_jniBloodSampleTypeValue(JNIEnv *env,
                                                                                                  jobject self,
                                                                                                  jint index)
{
    return (jint) BloodSampleTypeMapper::mapValue((int) index);
}

/////////////////////////////
/// Gender                ///
/////////////////////////////
JNIEXPORT jint JNICALL Java_com_epocal_common_JNIEnumValueResolverService_jniGenderValue(JNIEnv *env, jobject self,
                                                                                         jint index)
{
    jint enumValue = INT_MAX;
    switch(index)
    {
        case 0:
            enumValue = (jint)Gender::Male;
            break;
        case 1:
            enumValue = (jint)Gender::Female;
            break;
        case 2:
            enumValue = (jint)Gender::Unknown;
            break;
    }
    return enumValue;
}

/////////////////////////////
/// DeliverySystem        ///
/////////////////////////////
JNIEXPORT jint JNICALL
Java_com_epocal_common_JNIEnumValueResolverService_jniDeliverySystemValue(JNIEnv *env, jobject self,
                                                                          jint index)
{
    return (jint) DeliverySystemMapper::mapValue((int) index);
}

/////////////////////////////
/// RespiratoryMode       ///
/////////////////////////////
JNIEXPORT jint JNICALL Java_com_epocal_common_JNIEnumValueResolverService_jniRespiratoryModeValue(JNIEnv *env,
                                                                                                  jobject self,
                                                                                                  jint enumIndex)
{
    return (jint) RespiratoryModeMapper::mapValue((int) enumIndex);
}

/////////////////////////////
/// TestMode              ///
/////////////////////////////
JNIEXPORT jint JNICALL
Java_com_epocal_common_JNIEnumValueResolverService_jniTestModeValue(JNIEnv *env, jobject self,
                                                                    jint enumIndex)
{
    jint enumValue = INT_MAX;
    switch (enumIndex) {
        case 0:
            enumValue = (jint)TestMode::BloodTest;
            break;
        case 1:
            enumValue = (jint)TestMode::QA;
            break;
    }
    return enumValue;
}

/////////////////////////////
/// AGAPType              ///
/////////////////////////////
JNIEXPORT jint JNICALL
Java_com_epocal_common_JNIEnumValueResolverService_jniAGAPTypeValue(JNIEnv *env, jobject self,
                                                                    jint enumIndex)
{
    jint enumValue = INT_MAX;
    switch (enumIndex) {
        case 0:
            enumValue = (jint)AGAPType::AGAP;
            break;
        case 1:
            enumValue = (jint)AGAPType::AGAPK;
            break;
    }
    return enumValue;
}

/////////////////////////////
/// eGFRFormula           ///
/////////////////////////////
JNIEXPORT jint JNICALL
Java_com_epocal_common_JNIEnumValueResolverService_jniEGFRFormulaValue(JNIEnv *env, jobject self,
                                                                       jint enumIndex)
{
    jint enumValue = INT_MAX;
    switch (enumIndex) {
        case 0:
            enumValue = (jint)eGFRFormula::None;
            break;
        case 1:
            enumValue = (jint)eGFRFormula::MDRD;
            break;
        case 2:
            enumValue = (jint)eGFRFormula::CockroftGalt;
            break;
        case 3:
            enumValue = (jint)eGFRFormula::Japanese;
            break;
    }
    return enumValue;
}

/////////////////////////////
/// eGFRType              ///
/////////////////////////////
JNIEXPORT jint JNICALL
Java_com_epocal_common_JNIEnumValueResolverService_jniEGFRTypeValue(JNIEnv *env, jobject self,
                                                                    jint enumIndex)
{
    jint enumValue = INT_MAX;
    switch (enumIndex) {
        case 0:
            enumValue = (jint)eGFRType::eGFR;
            break;
        case 1:
            enumValue = (jint)eGFRType::eGFRa;
            break;
        case 2:
            enumValue = (jint)eGFRType::eGFRj;
            break;
        case 3:
            enumValue = (jint)eGFRType::eGFRckd;
            break;
        case 4:
            enumValue = (jint)eGFRType::eGFRckda;
            break;
        case 5:
            enumValue = (jint)eGFRType::eGFRswz;
            break;
    }
    return enumValue;
}

/////////////////////////////
/// Units                 ///
/////////////////////////////
JNIEXPORT jint JNICALL
Java_com_epocal_common_JNIEnumValueResolverService_jniUnitsValue(JNIEnv *env, jobject self,
                                                                 jint enumIndex)
{
    return (jint) UnitsMapper::mapValue((int) enumIndex);
}

/////////////////////////////
/// RealTimeQCType        ///
/////////////////////////////
JNIEXPORT jint JNICALL
Java_com_epocal_common_JNIEnumValueResolverService_jniRealTimeQCTypeValue(JNIEnv *env, jobject self,
                                                                          jint enumIndex)
{
    return (jint) RealTimeQCTypeMapper::mapValue((int) enumIndex);
}

///////////////////////////////////////
/// RealTimeHematocritQCReturnCode  ///
//////////////////////////////////////
JNIEXPORT jint JNICALL Java_com_epocal_common_JNIEnumValueResolverService_jniRealTimeHematocritQCReturnCodeValue(
        JNIEnv *env, jobject self,
        jint enumIndex)
{
    return (jint) RealTimeHematocritQCReturnCodeMapper::mapValue((int) enumIndex);
}

/////////////////////////////
/// BubbleDetectMode      ///
/////////////////////////////
JNIEXPORT jint JNICALL Java_com_epocal_common_JNIEnumValueResolverService_jniBubbleDetectModeValue(JNIEnv *env,
                                                                                                   jobject self,
                                                                                                   jint enumIndex)
{
    return (jint) BubbleDetectModeMapper::mapValue((int) enumIndex);
}

/////////////////////////////
/// Temperatures          ///
/////////////////////////////
JNIEXPORT jint JNICALL
Java_com_epocal_common_JNIEnumValueResolverService_jniTemperaturesValue(JNIEnv *env, jobject self,
                                                                        jint enumIndex)
{
    return (jint) TemperaturesMapper::mapValue((int) enumIndex);
}

/////////////////////////////
/// HgbResult             ///
/////////////////////////////
JNIEXPORT jint JNICALL
Java_com_epocal_common_JNIEnumValueResolverService_jniHgbResultValue(JNIEnv *env, jobject self,
                                                                     jint index)
{
    return (jint) HgbResultMapper::mapValue((int) index);
}

#ifdef __cplusplus
}
#endif  // __cplusplus

