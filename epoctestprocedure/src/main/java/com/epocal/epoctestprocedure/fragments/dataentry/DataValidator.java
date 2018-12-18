package com.epocal.epoctestprocedure.fragments.dataentry;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.epocal.common.CU;
import com.epocal.common.realmentities.InputFieldConfig;
import com.epocal.common.realmentities.QASampleInfo;
import com.epocal.common.types.EpocTestFieldType;
import com.epocal.common.types.EpocTestPanelType;
import com.epocal.common.types.InputFieldType;
import com.epocal.common.types.NotifyActionType;
import com.epocal.common.types.QAFluidType;
import com.epocal.common.types.TestType;
import com.epocal.common.types.am.AllensTest;
import com.epocal.common.types.am.BloodSampleType;
import com.epocal.common.types.am.DeliverySystem;
import com.epocal.common.types.am.DrawSites;
import com.epocal.common.types.am.Gender;
import com.epocal.common.types.am.RespiratoryMode;
import com.epocal.datamanager.InputFieldConfigModel;
import com.epocal.datamanager.QASampleInfoModel;
import com.epocal.epoctest.uidriver.TestUIDriver;
import com.epocal.epoctestprocedure.IPatientTestPresenter;
import com.epocal.epoctestprocedure.R;
import com.epocal.common_ui.util.StringResourceValues;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * The validator class which validates the data inserted into the data entry fields of the vertical
 * stepper.
 * <p>
 * Created by Zeeshan A Zakaria on 10/25/2017.
 */

class DataValidator implements IDataValidator {

    private IPatientTestPresenter mIPatientTestPresenter;
    private IVerticalStepperView mView;
    private TestUIDriver mTestUIDriver;
    private Context mContext;

    DataValidator(IVerticalStepperView view, TestUIDriver testUIDriver, Context context, IPatientTestPresenter patientTestPresenter) {
        mView = view;
        mTestUIDriver = testUIDriver;
        mContext = context;
        mIPatientTestPresenter = patientTestPresenter;
    }

    @Override
    public void updateActivityTitle(String title) {
        mIPatientTestPresenter.updateActivityTitle(title);
    }

    @Override
    public boolean validatePatientId(View view) {
        String patientId = getString(view);
        InputFieldConfigModel inputFieldConfigModel = new InputFieldConfigModel();
        InputFieldConfig inputFieldConfig = inputFieldConfigModel.getInputFieldConfig(InputFieldType.PATIENT_ID);
        Integer max = inputFieldConfig.getMaximumLength();
        Integer min = inputFieldConfig.getMinimumLength();
        Integer length = patientId.length();
        boolean returnValue = true;

        if (length >= min && length <= max) {
            mTestUIDriver.getTestData().setSubjectId(patientId);
            mView.hideErrorMessage(view);
            mView.markMandatoryStepCompleted(); // TODO: Check if this line is needed or the code works without it
            updateActivityTitle(mContext.getString(R.string.patient) + " " + patientId);
        } else {
            mView.showErrorMessage(view, mContext.getString(R.string.error_patient_id, min, max));
            mView.markMandatoryStepEditable();
            returnValue = false;
        }
        return returnValue;
    }

    @Override
    public boolean validateHemodilution(View view) {
        Integer value = getRadioValue(view);
        boolean returnValue = true;
        if (value != -1) { // When no radio button is selected, the value is -1
            mTestUIDriver.getTestData().getTestDetail().setHemodilutionApplied(value == 1); // If value is 1, save TRUE, otherwise FALSE
            mView.markMandatoryStepCompleted(); // TODO: Check if this line is needed or the code works without it
        } else {
            mView.markMandatoryStepEditable();
            returnValue = false;
        }
        return returnValue;
    }

    @Override
    public boolean validateLotNumber(View view) {
        String lotNumber = getString(view);
        InputFieldConfigModel inputFieldConfigModel = new InputFieldConfigModel();
        InputFieldConfig inputFieldConfig = inputFieldConfigModel.getInputFieldConfig(InputFieldType.PATIENT_OR_LOT_ID);
        Integer max = inputFieldConfig.getMaximumLength();
        Integer min = inputFieldConfig.getMinimumLength();
        Integer length = lotNumber.length();
        boolean returnValue = true;

        if (length >= min && length <= max) {
            mTestUIDriver.getTestData().getCardLot().setLotNumber(lotNumber);
            mView.hideErrorMessage(view);
            mView.markMandatoryStepCompleted(); // TODO: Check if this line is needed or the code works without it
            updateActivityTitle(mContext.getString(R.string.run_qa_test) + " Step 1 of 2"); //TODO: localize
        } else {
            mView.showErrorMessage(view, mContext.getString(R.string.error_patientorlot_id, min, max));
            mView.markMandatoryStepEditable();
            returnValue = false;
        }
        return returnValue;
    }

    @Override
    public boolean validateFluidType(View view)
    {
        Integer value = getRadioValue(view);
        boolean returnValue = true;
        if (value != -1) { // When no radio button is selected, the value is -1
            String selectedName = getCheckedRadioText(view);
            QASampleInfo selectedSample = new QASampleInfoModel().getUnmanagedSampleInfo(selectedName);
            if(selectedSample != null && mTestUIDriver.getTestData().getTestDetail().getQaSampleInfo() != null &&
                    !mTestUIDriver.getTestData().getTestDetail().getQaSampleInfo().getName().equals(selectedSample.getName())) {
                mTestUIDriver.getTestData().getTestDetail().setQaSampleInfo(selectedSample);
                mTestUIDriver.getTestData().getTestDetail().setSampleType(BloodSampleType.Unspecified); //TODO: should be NONE but enum not defined (check with dean)

                boolean doRecalculation = !selectedSample.getQaRange().getName().equals(mTestUIDriver.getTestData().getTestDetail().getQaSampleInfo().getName());
                //TODO: add logic here (or somewhere else) to handle recalculations

                mView.markMandatoryStepCompleted(); // TODO: Check if this line is needed or the code works without it
                updateActivityTitle(mContext.getString(R.string.run_qa_test) + " Step 2 of 2"); //TODO: localize
            }
        } else {
            mView.markMandatoryStepEditable();
            returnValue = false;
        }
        return returnValue;
    }

    private boolean validateTestType(View view)
    {
        Integer value = getRadioValue(view);
        boolean returnValue = true;
        if (value != -1) { // When no radio button is selected, the value is -1
            switch(value) {
                //TODO: fix this by displaying all the enum options in the UI but using "gone" to hide
                //      unselectable ones so .fromInt() can be used instead of a switch matching the UI view/order
                case 0:
                    mTestUIDriver.getTestData().setType(TestType.QualityControl);
                    break;
                case 1:
                    mTestUIDriver.getTestData().setType(TestType.CalVer);
                    break;
                case 2:
                    mTestUIDriver.getTestData().setType(TestType.Proficiency);
                    break;
                case 3:
                    mTestUIDriver.getTestData().setType(TestType.Other);
                    break;
            }
        } else {
            returnValue = false;
        }
        return returnValue;
    }

    @Override
    public void defaultBehaviour() {
        mView.markCompleted();
    }

    @Override
    public void enableMandatorySteps(ArrayList<StepModel> stepModels) {
        for (StepModel step : stepModels) {
            int stepNumber = step.getStepNumber();
            boolean isMandatory = step.isMandatory();
            boolean isCompleted = step.isCompleted();
            if (isMandatory) {
                mView.enableMandatoryStep(stepNumber, isCompleted, false);
            } else {
                mView.disableStep(stepNumber);
            }
        }
    }

    /**
     * Disable all the steps except the first one, which is the patient ID
     *
     * @param stepModels the array list of all the step
     */
    @Override
    public void disableMandatorySteps(ArrayList<StepModel> stepModels) {
        for (StepModel step : stepModels) {
            int stepNumber = step.getStepNumber();
            mView.disableStep(stepNumber);
        }
    }

    @Override
    public boolean saveCustomField(String fieldName, View view) {
        String value = getString(view);
         mTestUIDriver.saveCustomVariable(fieldName,value);
        return true;
    }

    /**
     * The method validates the input fields.
     *
     * @param fieldType         the Epoc filed type
     * @param view              the view
     * @param customFieldName   the custom field name, which is null when the validation is for the factory defined fields
     * @param initialLoad       the flag to decide the state of this method for its initial validation of the form, and later validations when onClick is called on a field
     * @return returning true of false so that the step could be closed or left opened.
     */
    @Override
    public boolean validateFields(EpocTestFieldType fieldType, View view, String customFieldName, boolean initialLoad, boolean mandatory) {
        ErrorObject errorObject;
        HashMap<EpocTestFieldType, ErrorObject> error = new HashMap<>();
        String value = getString(view);

        switch (fieldType) {
            case PATIENTID: {
                return validatePatientId(view);
            }

            case HEMODILUTION: {
                return validateHemodilution(view);
            }
            case PATIENTID2: {
                InputFieldConfigModel inputFieldConfigModel = new InputFieldConfigModel();
                InputFieldConfig inputFieldConfig = inputFieldConfigModel.getInputFieldConfig(InputFieldType.ID_2);
                Integer max = inputFieldConfig.getMaximumLength();
                Integer min = inputFieldConfig.getMinimumLength();
                Integer length = value.length();
                if (length >= min && length <= max) {
                    mView.hideErrorMessage(view);
                    mTestUIDriver.getTestData().setId2(value);
                } else {
                    //
                    errorObject = new ErrorObject(mContext.getString(R.string.error_patient_id_2, min, max), view);
                    error.put(fieldType, errorObject);
                }
                break;
            }
            case PATIENTTEMPERATURE: {
                switch (value) {
                    case "": {
                        mTestUIDriver.getTestData().getTestDetail().setPatientTemperature((double)-1);
                        break;
                    }
                    default:
                        if (isNumeric(value)) {
                            Double temperature = Double.parseDouble(value);
                            if (temperature < 10.0 || temperature > 44.0) {
                                errorObject = new ErrorObject(mContext.getString(R.string.error_patient_temperature), view);
                                error.put(fieldType, errorObject);
                            } else {
                                mTestUIDriver.getTestData().getTestDetail().setPatientTemperature(temperature);
                            }
                        }
                }
                break;
            }
            case PATIENTGENDER: {
                Integer gender = getRadioValue(view);
                if (gender == -1) {
//                    errorObject = new ErrorObject(mContext.getString(R.string.error), view); TODO:// to be removed eventually if not needed
//                    error.put(fieldType, errorObject);
                } else {
                    mTestUIDriver.getTestData().setGender(Gender.fromInt(gender));
                }
                break;
            }
            case PATIENTAGE: {
                if (!value.equals("")) {
                    Integer age = Integer.parseInt(value);
                    if (age < 0 || age > 130) {
                        errorObject = new ErrorObject(mContext.getString(R.string.error_patient_age), view);
                        error.put(fieldType, errorObject);
                    } else {
                        mTestUIDriver.getTestData().setPatientAge(age);
                    }
                } else {
                    mTestUIDriver.getTestData().setPatientAge(-1);
                }
                break;
            }
            case SAMPLETYPE: {
                Integer key = getInteger(view, fieldType, true);
                switch (key) {
                    case 998:
                        errorObject = new ErrorObject(mContext.getString(R.string.error), view);
                        error.put(fieldType, errorObject);
                        break;
                    case 999:
                        // TODO: Nullify the field
                        break;
                    default:
                        mTestUIDriver.getTestData().getTestDetail().setSampleType(BloodSampleType.fromInt(key));
                }
                break;
            }
            case DRAWSITE: {
                Integer key = getInteger(view, fieldType, true);
                switch (key) {
                    case 998:
                        errorObject = new ErrorObject(mContext.getString(R.string.error), view);
                        error.put(fieldType, errorObject);
                        break;
                    case 999:
                        // TODO: Nullify the field
                        break;
                    default:
                        mTestUIDriver.getTestData().getRespiratoryDetail().setDrawSite(DrawSites.fromInt(key));
                }
                break;
            }
            case DELIVERYSYSTEM: {
                Integer key = getInteger(view, fieldType, true);
                switch (key) {
                    case 998:
                        errorObject = new ErrorObject(mContext.getString(R.string.error), view);
                        error.put(fieldType, errorObject);
                        break;
                    case 999:
                        // TODO: Nullify the field
                        break;
                    default:
                        mTestUIDriver.getTestData().getRespiratoryDetail().setDeliverySystem(DeliverySystem.fromInt(key));
                }
                break;
            }
            case ALLENSTEST: {
                Integer value1 = getRadioValue(view);
                if (value1 == -1) {
                    errorObject = new ErrorObject(mContext.getString(R.string.error), view);
                    error.put(fieldType, errorObject);
                } else {
                    mTestUIDriver.getTestData().getRespiratoryDetail().setRespAllensType(AllensTest.fromInt(value1));
                }
                break;
            }
            case FIO2: {
                switch (value) {
                    case "": {
                        mTestUIDriver.getTestData().getRespiratoryDetail().setFiO2("");
                        break;
                    }
                    default:
                        if (errorCheck(value, 21.0, 100.0)) {
                            errorObject = new ErrorObject(mContext.getString(R.string.error_fi02), view);
                            error.put(fieldType, errorObject);
                        } else {
                            mTestUIDriver.getTestData().getRespiratoryDetail().setFiO2(value);
                        }
                }
                break;
            }
            case MODE: {
                Integer key = getInteger(view, fieldType, true);
                switch (key) {
                    case 998:
                        errorObject = new ErrorObject(mContext.getString(R.string.error), view);
                        error.put(fieldType, errorObject);
                        break;
                    case 999:
                        // TODO: Nullify the field
                        break;
                    default:
                        mTestUIDriver.getTestData().getRespiratoryDetail().setRespiratoryMode(RespiratoryMode.fromInt(key));
                }
                break;
            }
            case VT: {
                switch (value) {
                    case "": {
                        mTestUIDriver.getTestData().getRespiratoryDetail().setTidalVolume("");
                        break;
                    }
                    default:
                        if (isNumeric(value)) {
                            mTestUIDriver.getTestData().getRespiratoryDetail().setTidalVolume(value);
                        } else {
                            errorObject = new ErrorObject(mContext.getString(R.string.error_enter_number), view);
                            error.put(fieldType, errorObject);
                        }
                }
                break;
            }
            case RR: {
                switch (value) {
                    case "": {
                        mTestUIDriver.getTestData().getRespiratoryDetail().setRespiratoryRate(value);
                        break;
                    }
                    default:
                        if (isNumeric(value)) {
                            mTestUIDriver.getTestData().getRespiratoryDetail().setRespiratoryRate(value);
                        } else {
                            errorObject = new ErrorObject(mContext.getString(R.string.error_enter_number), view);
                            error.put(fieldType, errorObject);
                        }
                }
                break;
            }
            case TR: {
                switch (value) {
                    case "": {
                        mTestUIDriver.getTestData().getRespiratoryDetail().setTotalRespiratoryRate(value);
                        break;
                    }
                    default:
                        if (isNumeric(value)) {
                            mTestUIDriver.getTestData().getRespiratoryDetail().setTotalRespiratoryRate(value);
                        } else {
                            errorObject = new ErrorObject(mContext.getString(R.string.error_enter_number), view);
                            error.put(fieldType, errorObject);
                        }
                }
                break;
            }
            case PEEP: {
                switch (value) {
                    case "": {
                        mTestUIDriver.getTestData().getRespiratoryDetail().setPeep(value);
                        break;
                    }
                    default:
                        if (isNumeric(value)) {
                            mTestUIDriver.getTestData().getRespiratoryDetail().setPeep(value);
                        } else {
                            errorObject = new ErrorObject(mContext.getString(R.string.error_enter_number), view);
                            error.put(fieldType, errorObject);
                        }
                }
                break;
            }
            case PS: {
                switch (value) {
                    case "": {
                        mTestUIDriver.getTestData().getRespiratoryDetail().setPs(value);
                        break;
                    }
                    default:
                        if (isNumeric(value)) {
                            mTestUIDriver.getTestData().getRespiratoryDetail().setPs(value);
                        } else {
                            errorObject = new ErrorObject(mContext.getString(R.string.error_enter_number), view);
                            error.put(fieldType, errorObject);
                        }
                }
            }
            case IT: {
                switch (value) {
                    case "": {
                        mTestUIDriver.getTestData().getRespiratoryDetail().setInspiratoryTime(value);
                        break;
                    }
                    default:
                        if (isNumeric(value)) {
                            mTestUIDriver.getTestData().getRespiratoryDetail().setInspiratoryTime(value);
                        } else {
                            errorObject = new ErrorObject(mContext.getString(R.string.error_enter_number), view);
                            error.put(fieldType, errorObject);
                        }
                }
                break;
            }
            case ET: {
                switch (value) {
                    case "": {
                        mTestUIDriver.getTestData().getRespiratoryDetail().setExpiratoryTime(value);
                        break;
                    }
                    default:
                        if (isNumeric(value)) {
                            mTestUIDriver.getTestData().getRespiratoryDetail().setExpiratoryTime(value);
                        } else {
                            errorObject = new ErrorObject(mContext.getString(R.string.error_enter_number), view);
                            error.put(fieldType, errorObject);
                        }
                }
                break;
            }
            case PIP: {
                switch (value) {
                    case "": {
                        mTestUIDriver.getTestData().getRespiratoryDetail().setPeakInspiratoryPressure(value);
                        break;
                    }
                    default:
                        if (isNumeric(value)) {
                            mTestUIDriver.getTestData().getRespiratoryDetail().setPeakInspiratoryPressure(value);
                        } else {
                            errorObject = new ErrorObject(mContext.getString(R.string.error_enter_number), view);
                            error.put(fieldType, errorObject);
                        }
                }
                break;
            }
            case MAP: {
                switch (value) {
                    case "": {
                        mTestUIDriver.getTestData().getRespiratoryDetail().setMeanAirWayPressure(value);
                        break;
                    }
                    default:
                        if (isNumeric(value)) {
                            mTestUIDriver.getTestData().getRespiratoryDetail().setMeanAirWayPressure(value);
                        } else {
                            errorObject = new ErrorObject(mContext.getString(R.string.error_enter_number), view);
                            error.put(fieldType, errorObject);
                        }
                }
                break;
            }
            case FLOW: {
                switch (value) {
                    case "": {
                        mTestUIDriver.getTestData().getRespiratoryDetail().setFlow("");
                        break;
                    }
                    default:
                        if (errorCheck(value, 0.0, 15.0)) {
                            errorObject = new ErrorObject(mContext.getString(R.string.error_flow), view);
                            error.put(fieldType, errorObject);
                        } else {
                            mTestUIDriver.getTestData().getRespiratoryDetail().setFlow(value);
                        }
                }
                break;
            }
            case HERTZ: {
                switch (value) {
                    case "": {
                        mTestUIDriver.getTestData().getRespiratoryDetail().setHertz(value);
                        break;
                    }
                    default:
                        if (isNumeric(value)) {
                            mTestUIDriver.getTestData().getRespiratoryDetail().setHertz(value);
                        } else {
                            errorObject = new ErrorObject(mContext.getString(R.string.error_enter_number), view);
                            error.put(fieldType, errorObject);
                        }
                }
                break;
            }
            case DELTAP: {
                switch (value) {
                    case "": {
                        mTestUIDriver.getTestData().getRespiratoryDetail().setDeltaP(value);
                        break;
                    }
                    default:
                        if (isNumeric(value)) {
                            mTestUIDriver.getTestData().getRespiratoryDetail().setDeltaP(value);
                        } else {
                            errorObject = new ErrorObject(mContext.getString(R.string.error_enter_number), view);
                            error.put(fieldType, errorObject);
                        }
                }
                break;
            }
            case NOPPM: {
                switch (value) {
                    case "": {
                        mTestUIDriver.getTestData().getRespiratoryDetail().setNoPPM(value);
                        break;
                    }
                    default:
                        if (isNumeric(value)) {
                            mTestUIDriver.getTestData().getRespiratoryDetail().setNoPPM(value);
                        } else {
                            errorObject = new ErrorObject(mContext.getString(R.string.error_enter_number), view);
                            error.put(fieldType, errorObject);
                        }
                }
                break;
            }
            case RQ: {
                switch (value) {
                    case "": {
                        if (!mandatory) {
                            mTestUIDriver.getTestData().getRespiratoryDetail().setRq("");
                            break;
                        }
                    }
                    default:
                        if (errorCheck(value, 0.01, 2.00)) {
                            errorObject = new ErrorObject(mContext.getString(R.string.error_rq), view);
                            error.put(fieldType, errorObject);
                        } else {
                            mTestUIDriver.getTestData().getRespiratoryDetail().setRq(value);
                        }
                }
                break;
            }
            case COMMENTS: {
                int commentsLength = value.length();
                if (commentsLength > 255) {
                    errorObject = new ErrorObject(mContext.getString(R.string.error), view);
                    error.put(fieldType, errorObject);
                } else {
                    mTestUIDriver.getTestData().getTestDetail().setComment(value);
                }
                break;
            }
            case ORDERINGPHYSICIAN: {
                mTestUIDriver.getTestData().getTestDetail().setOrderingPhysician(value);
                break;
            }
            case COLLECTEDBY: {
                mTestUIDriver.getTestData().getTestDetail().setCollectedBy(value);
                break;
            }
            case PATIENTLOCATION: {
                mTestUIDriver.getTestData().getTestDetail().setPatientLocation(value);
                break;
            }
            case COLLECTIONDATE: {
                try {
                    if ((value != null) && (!value.isEmpty())) {
                        Date date = CU.epocDateStringToDate(value);
                        mTestUIDriver.getTestData().getTestDetail().setCollectionDate(date);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case COLLECTIONTIME: {
                mTestUIDriver.getTestData().getTestDetail().setCollectionTime(value);
                break;
            }
            case ORDERDATE: {
                try {
                    if ((value != null) && (!value.isEmpty())) {
                        Date date = CU.epocDateStringToDate(value);
                        mTestUIDriver.getTestData().getTestDetail().setOrderDate(date);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case ORDERTIME: {
                mTestUIDriver.getTestData().getTestDetail().setOrderTime(value);
                break;
            }
            case TESTSELECTION: {
                SparseIntArray sparseIntArray = StringResourceValues.setTestPanels();
                int key = StringResourceValues.getKey(sparseIntArray, value, mContext);
                mTestUIDriver.setTestInclusions(EpocTestPanelType.fromInt(key));
                break;
            }
            case NOTIFYACTION: {
                Integer key = getRadioValue(view);
                if (key == -1) {
                    errorObject = new ErrorObject(mContext.getString(R.string.error), view);
                    error.put(fieldType, errorObject);
                } else {
                    mTestUIDriver.getTestData().getTestDetail().setNotifyActionType(NotifyActionType.fromInt(key));
                }
                break;
            }
            case NOTIFYNAME: {
                mTestUIDriver.getTestData().getTestDetail().setNotifyName(value);
                break;
            }
            case NOTIFYDATE: {
                try {
                    if ((value != null) && (!value.isEmpty())) {
                        Date date = CU.epocDateStringToDate(value);
                        mTestUIDriver.getTestData().getTestDetail().setNotifyDate(date);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case NOTIFYTIME: {
                mTestUIDriver.getTestData().getTestDetail().setNotifyTime(value);
                break;
            }
            case READBACK: {
                Integer readBack = getRadioValue(view);
                if (readBack == -1) {
                    errorObject = new ErrorObject(mContext.getString(R.string.error), view);
                    error.put(fieldType, errorObject);
                } else {
                    mTestUIDriver.getTestData().getTestDetail().setReadBack(readBack == 1);
                }
                break;
            }
            case REJECTTEST: {
                Integer rejectTest = getRadioValue(view);
                if (rejectTest == -1) {
                    errorObject = new ErrorObject(mContext.getString(R.string.error), view);
                    error.put(fieldType, errorObject);
                } else {
                    mTestUIDriver.getTestData().setRejected(rejectTest == 1);
                }
                break;
            }
            case LOTNUMBER: {
                return validateLotNumber(view);
            }
            case FLUIDTYPE: {
                return validateFluidType(view);
            }
            case TESTTYPE: {
                return validateTestType(view);
            }
            // Special case. It'll save it right here instead of sending the value back.
            case CUSTOM: {
                 mTestUIDriver.saveCustomVariable(customFieldName, value);
            }
            break;
        }

        if (error.size() == 0) {
            if (!initialLoad) mView.markMandatoryStepCompleted(); // Don't mark mandatory steps completed when stepper is validated at the time of its initialization
            return true;
        } else {
            mView.showAllErrorMessages(error);
            if (!initialLoad) mView.markMandatoryStepIncomplete(); // Don't change the isCompleted value for the stepper's initial load. Only for the mandatory steps, optional steps don't need this
            return false;
        }
    }

    /**
     * This method extracts String value from the given EditText. If there doesn't exist an EditText
     * then return an empty string.
     *
     * @param view the View
     * @return the String value
     */
    @Override
    public String getString(View view) {
        try {
            EditText editText = (EditText) view.findViewById(R.id.field_value);
            if (editText != null) {
                return editText.getText().toString();
            } else {
                RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
                if (radioGroup != null) {
                    int value = radioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton) view.findViewById(value);
                    return ((radioButton != null ? radioButton.getText().toString() : ""));

                } else {
                    TextView fieldValueCombo = (TextView) view.findViewById(R.id.text_view_field_value_combo);
                    AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.act_field_value);
                    if (fieldValueCombo != null && autoCompleteTextView != null) {
                        return autoCompleteTextView.getText().toString();
                    }
                }
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * This method extracts Integer value from the given RadioGroup fields in the Stepper form
     *
     * @param view the View
     * @return the Integer value
     */
    private Integer getInteger(View view, EpocTestFieldType fieldType, boolean autoComplete) {
        String data;
        if (autoComplete) {
            AutoCompleteTextView autocompleteView = (AutoCompleteTextView) view.findViewById(R.id.act_field_value);
            data = autocompleteView.getText().toString();
        } else {
            EditText editText = (EditText) view.findViewById(R.id.field_value);
            data = editText.getText().toString();
        }

        if (data.equals("")) {
            return 999;
        } else {
            SparseIntArray array;
            switch (fieldType) {
                case SAMPLETYPE:
                    array = StringResourceValues.setSampleType();
                    break;
                case DRAWSITE:
                    array = StringResourceValues.setDrawSite();
                    break;
                case DELIVERYSYSTEM:
                    array = StringResourceValues.setDeliverySystem();
                    break;
                case MODE:
                    array = StringResourceValues.setRespiratoryMode();
                    break;
                default:
                    return 998;
            }

            return StringResourceValues.getKey(array, data, mContext);
        }
    }

    /**
     * This method gets the value from the given RadioGroup
     *
     * @param view the View
     * @return the selected Radio value
     */
    private Integer getRadioValue(View view) {
        RadioGroup radioGroup;
        radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        return radioGroup.getCheckedRadioButtonId();
    }

    private String getCheckedRadioText(View view) {
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        Integer idx = radioGroup.getCheckedRadioButtonId();
        View radioButton = radioGroup.findViewById(idx);
        int radioId = radioGroup.indexOfChild(radioButton);
        RadioButton btn = (RadioButton) radioGroup.getChildAt(radioId);
        return (String) btn.getText();
    }

    /**
     * Check the valid range. If it is not a numeric value or if it is not in the range, return true.
     *
     * @param input the input value
     * @param min   the range minimum
     * @param max   the range maximum
     * @return the return flag
     */
    private boolean errorCheck(String input, Double min, Double max) {
        try {
            Double value = Double.parseDouble(input);
            return (value < min || value > max);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * Check if the input value is a valid numeric value of type Double
     *
     * @param value the input value
     * @return the result
     */
    private boolean isNumeric(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
