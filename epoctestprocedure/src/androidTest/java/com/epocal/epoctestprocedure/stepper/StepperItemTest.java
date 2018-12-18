package com.epocal.epoctestprocedure.stepper;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.suitebuilder.annotation.SmallTest;

import com.epocal.common.realmentities.InputFieldConfig;
import com.epocal.common.realmentities.TestInputField;
import com.epocal.common.types.EpocTestFieldType;
import com.epocal.common.types.InputFieldType;
import com.epocal.common_ui.util.StringResourceValues;
import com.epocal.datamanager.InputFieldConfigModel;
import com.epocal.datamanager.realmrepository.PrimaryKeyFactory;
import com.epocal.datamanager.realmrepository.RepositoryManager;
import com.epocal.epoctestprocedure.R;
import com.epocal.epoctestprocedure.stepper.types.StepperHintType;
import com.epocal.epoctestprocedure.stepper.types.StepperType;
//import com.epocal.epoctestprocedure.R;

import io.realm.Realm;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SmallTest
public class StepperItemTest {
    private Context instrumentationCtx;


    private TestInputField createTestInputField (EpocTestFieldType fieldType, int displayOrder){
        TestInputField retval = new TestInputField();
        retval.setDisplayOrder(displayOrder);
        retval.setFieldType(fieldType);
        return retval;
    }

    @org.junit.Before
    public void setUp() throws Exception {
        instrumentationCtx = InstrumentationRegistry.getContext();

        Realm.init(instrumentationCtx);

        Realm realm = Realm.getDefaultInstance();

        try {
            PrimaryKeyFactory.getInstance().initialize(realm);
        } catch (IllegalStateException e) {
            //this is ok
        }
        if (realm.isEmpty()) {
            RepositoryManager.FactoryInitializeRealm(realm);
        }
        realm.close();
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    /**
     * Tests that patient id stepper item is configured with the right UI settings, validation settings
     */
    @org.junit.Test
    public void TestPatientId() {
        EpocTestFieldType testFieldType = EpocTestFieldType.PATIENTID;
        StepperType stepperType = StepperType.VS_BARCODE;
        StepperHintType stepperHintType = StepperHintType.VS_HINT_ENTERORSCAN;
        StepperItem stepperItem = new StepperItem(instrumentationCtx, createTestInputField(testFieldType,10));

        // test ui
        assertThat(String.format("%s is not displayed %s",testFieldType,stepperType), stepperItem.getDataType(), is(stepperType));
        assertThat(String.format("%s hint type should %s",testFieldType,stepperHintType), stepperItem.getHintType(), is(stepperHintType));
        InputFieldConfig inputFieldConfig = new InputFieldConfigModel().getInputFieldConfig(InputFieldType.PATIENT_ID);
        assertThat(String.format("%s incorrect error string",testFieldType), stepperItem.getErrorMessage(),is(instrumentationCtx.getString(R.string.error_patient_id, inputFieldConfig.getMinimumLength(), inputFieldConfig.getMaximumLength())));

        // test validation
        assertThat(String.format("%s. validation failed for empty string",testFieldType),stepperItem.validate(""),is(false));
        assertThat(String.format("%s. validation failed for normal string",testFieldType),stepperItem.validate("patientid1"),is(true));
        String strOverAllowedMaximum = new String(new char[inputFieldConfig.getMaximumLength()+1]).replace("\0", "p");
        assertThat(String.format("%s. validation failed for string over max length",testFieldType),stepperItem.validate(strOverAllowedMaximum),is(false));

        assertThat(String.format("%s. validation failed for Integer type",testFieldType), stepperItem.validate(4), is(false));
        assertThat(String.format("%s. validation failed for Object type",testFieldType), stepperItem.validate(new Object()), is(false));
    }

    /**
     * Tests that id2 stepper item is configured with the right UI settings, validation settings
     */
    @org.junit.Test
    public void TestPatientId2() {
        EpocTestFieldType testFieldType = EpocTestFieldType.PATIENTID2;
        StepperType stepperType = StepperType.VS_STRING;
        StepperHintType stepperHintType = StepperHintType.VS_HINT_ENTER;
        StepperItem stepperItem = new StepperItem(instrumentationCtx, createTestInputField(testFieldType,4));

        // test ui
        assertThat(String.format("%s is not displayed %s",testFieldType,stepperType), stepperItem.getDataType(), is(stepperType));
        assertThat(String.format("%s hint type should %s",testFieldType,stepperHintType), stepperItem.getHintType(), is(stepperHintType));
        assertThat(String.format("%s incorrect error string",testFieldType), stepperItem.getErrorMessage(),is(instrumentationCtx.getString(R.string.error)));

        // test validation
        assertThat(String.format("%s validation failed for empty string",testFieldType), stepperItem.validate(""),is(true));
        assertThat(String.format("%s validation failed for normal string",testFieldType), stepperItem.validate("patientid2"),is(true));

        assertThat(String.format("%s validation failed for Integer type",testFieldType), stepperItem.validate(4), is(false));
        assertThat(String.format("%s validation failed for Object type",testFieldType), stepperItem.validate(new Object()), is(false));
    }

    /**
     * Tests that sampletype stepper item is configured with the right UI settings, validation settings
     */
    @org.junit.Test
    public void TestSampleType() {
        EpocTestFieldType testFieldType = EpocTestFieldType.SAMPLETYPE;
        StepperType stepperType = StepperType.VS_COMBO;
        StepperHintType stepperHintType = StepperHintType.VS_HINT_ENTERORSELECT;
        StepperItem stepperItem = new StepperItem(instrumentationCtx, createTestInputField(testFieldType,4));

        // test ui
        assertThat(String.format("%s is not displayed %s",testFieldType,stepperType), stepperItem.getDataType(), is(stepperType));
        assertThat(String.format("%s hint type should %s",testFieldType,stepperHintType), stepperItem.getHintType(), is(stepperHintType));
        assertThat(String.format("%s incorrect error string",testFieldType), stepperItem.getErrorMessage(),is(instrumentationCtx.getString(R.string.error)));

        String mixedVenous = instrumentationCtx.getString(com.epocal.common_ui.R.string.mixed_venous);
        // test validation
        assertThat(String.format("%s validation failed for %s",testFieldType, mixedVenous), stepperItem.validate(mixedVenous),is(true));
        assertThat(String.format("%s validation failed for unknown sample type",testFieldType), stepperItem.validate("poison"),is(false));

        assertThat(String.format("%s validation failed for Integer type",testFieldType), stepperItem.validate(4), is(false));
        assertThat(String.format("%s validation failed for Object type",testFieldType), stepperItem.validate(new Object()), is(false));
    }

    /**
     * Tests that patient temperature stepper item is configured with the right UI settings, validation settings
     */
    @org.junit.Test
    public void TestPatientTemperature() {
        EpocTestFieldType testFieldType = EpocTestFieldType.PATIENTTEMPERATURE;
        StepperType stepperType = StepperType.VS_INTEGER;
        StepperHintType stepperHintType = StepperHintType.VS_HINT_ENTER;
        StepperItem stepperItem = new StepperItem(instrumentationCtx, createTestInputField(testFieldType,4));

        // test ui
        assertThat(String.format("%s is not displayed %s",testFieldType,stepperType), stepperItem.getDataType(), is(stepperType));
        assertThat(String.format("%s hint type should %s",testFieldType,stepperHintType), stepperItem.getHintType(), is(stepperHintType));
        assertThat(String.format("%s incorrect error string",testFieldType), stepperItem.getErrorMessage(),is(instrumentationCtx.getString(R.string.error_patient_temperature)));

        // test validation
        assertThat(String.format("%s validation failed for empty string",testFieldType), stepperItem.validate(""),is(true));
        assertThat(String.format("%s validation failed for string with only letters",testFieldType), stepperItem.validate("words"),is(false));
        assertThat(String.format("%s validation failed for string with only numbers",testFieldType), stepperItem.validate("23"),is(true));
        assertThat(String.format("%s validation failed for string with only numbers2",testFieldType), stepperItem.validate("23.23"),is(true));
        assertThat(String.format("%s validation failed for string with letters and numbers",testFieldType), stepperItem.validate("ab2312"),is(false));
        assertThat(String.format("%s validation failed for normal symbols",testFieldType), stepperItem.validate("&*^*@"),is(false));

        assertThat(String.format("%s validation failed for Integer type",testFieldType), stepperItem.validate(14), is(true));
        assertThat(String.format("%s validation failed for Double type",testFieldType), stepperItem.validate(14.5), is(true));
        assertThat(String.format("%s validation failed for Boolean type",testFieldType), stepperItem.validate(new Boolean(false)), is(false));

        assertThat(String.format("%s validation failed temperature within range",testFieldType), stepperItem.validate(24.5), is(true));
        assertThat(String.format("%s validation failed temperature out of range",testFieldType), stepperItem.validate(124.5), is(false));
    }

    /**
     * Tests that hemodilution stepper item is configured with the right UI settings, validation settings
     */
    @org.junit.Test
    public void TestHemodilution() {
        EpocTestFieldType testFieldType = EpocTestFieldType.HEMODILUTION;
        StepperType stepperType = StepperType.VS_RADIO;
        StepperHintType stepperHintType = StepperHintType.VS_HINT_ENTER;
        StepperItem stepperItem = new StepperItem(instrumentationCtx, createTestInputField(testFieldType,4));

        // test ui
        assertThat(String.format("%s is not displayed %s",testFieldType,stepperType), stepperItem.getDataType(), is(stepperType));
        assertThat(String.format("%s hint type should %s",testFieldType,stepperHintType), stepperItem.getHintType(), is(stepperHintType));
        assertThat(String.format("%s incorrect error string",testFieldType), stepperItem.getErrorMessage(),is(""));

        // test validation
        assertThat(String.format("%s validation failed for empty string",testFieldType), stepperItem.validate(""),is(true));
        assertThat(String.format("%s validation failed for non empty string",testFieldType), stepperItem.validate("words"),is(false));
        assertThat(String.format("%s validation failed for string with only numbers",testFieldType), stepperItem.validate("1"),is(true));

        assertThat(String.format("%s validation failed for Integer(-1) type",testFieldType), stepperItem.validate(-1), is(false));
        assertThat(String.format("%s validation failed for Integer(0) type",testFieldType), stepperItem.validate(0), is(true));
        assertThat(String.format("%s validation failed for Integer(1) type",testFieldType), stepperItem.validate(1), is(true));
        assertThat(String.format("%s validation failed for Double type",testFieldType), stepperItem.validate(1), is(true));
        assertThat(String.format("%s validation failed for Boolean type",testFieldType), stepperItem.validate(new Boolean(false)), is(false));
        assertThat(String.format("%s validation failed for null type",testFieldType), stepperItem.validate(null), is(false));
    }

    /**
     * Tests that fluid type stepper item is configured with the right UI settings, validation settings
     */
    @org.junit.Test
    public void TestFluidType() {
        EpocTestFieldType testFieldType = EpocTestFieldType.FLUIDTYPE;
        StepperType stepperType = StepperType.VS_RADIO;
        StepperHintType stepperHintType = StepperHintType.VS_HINT_ENTER;
        StepperItem stepperItem = new StepperItem(instrumentationCtx, createTestInputField(testFieldType,4));

        // test ui
        assertThat(String.format("%s is not displayed %s",testFieldType,stepperType), stepperItem.getDataType(), is(stepperType));
        assertThat(String.format("%s hint type should %s",testFieldType,stepperHintType), stepperItem.getHintType(), is(stepperHintType));
        assertThat(String.format("%s incorrect error string",testFieldType), stepperItem.getErrorMessage(),is(""));

        // test validation
        assertThat(String.format("%s validation failed for UNKNOWN",testFieldType), stepperItem.validate(0),is(true));
        assertThat(String.format("%s validation failed for DEFAULT",testFieldType), stepperItem.validate(1),is(true));
        assertThat(String.format("%s validation failed for unselected",testFieldType), stepperItem.validate(-1),is(false));

        assertThat(String.format("%s validation failed for Integer type",testFieldType), stepperItem.validate(4), is(false));
        assertThat(String.format("%s validation failed for Object type",testFieldType), stepperItem.validate(new Object()), is(false));
    }

    /**
     * Tests that fluid type stepper item is configured with the right UI settings, validation settings
     */
    @org.junit.Test
    public void TestTestType() {
        EpocTestFieldType testFieldType = EpocTestFieldType.TESTTYPE;
        StepperType stepperType = StepperType.VS_RADIO;
        StepperHintType stepperHintType = StepperHintType.VS_HINT_ENTER;
        StepperItem stepperItem = new StepperItem(instrumentationCtx, createTestInputField(testFieldType,4));

        // test ui
        assertThat(String.format("%s is not displayed %s",testFieldType,stepperType), stepperItem.getDataType(), is(stepperType));
        assertThat(String.format("%s hint type should %s",testFieldType,stepperHintType), stepperItem.getHintType(), is(stepperHintType));
        assertThat(String.format("%s incorrect error string",testFieldType), stepperItem.getErrorMessage(),is(""));

        // test validation
        String calVer = instrumentationCtx.getString(com.epocal.common_ui.R.string.testtype_calver);
        int key = StringResourceValues.getKey(StringResourceValues.setTestTypeForTestProcedure(),calVer,instrumentationCtx);
        // test validation
        assertThat(String.format("%s validation failed for %s",testFieldType,calVer), stepperItem.validate(key),is(true));
        assertThat(String.format("%s validation failed for unselected",testFieldType), stepperItem.validate(-1),is(false));

        assertThat(String.format("%s validation failed for Integer type",testFieldType), stepperItem.validate(4), is(false));
        assertThat(String.format("%s validation failed for Object type",testFieldType), stepperItem.validate(new Object()), is(false));
    }
}