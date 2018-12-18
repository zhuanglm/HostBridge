package com.epocal.testhistoryfeature.data.patientdata;

import android.content.Context;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseIntArray;

import com.epocal.common.realmentities.CustomTestInputField;
import com.epocal.common.realmentities.CustomTestVariable;
import com.epocal.common.realmentities.TestInputField;
import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.realmentities.WorkFlow;
import com.epocal.common.realmentities.WorkflowItem;
import com.epocal.common.types.EpocTestFieldGroupType;
import com.epocal.common.types.EpocTestFieldType;
import com.epocal.common.types.am.Gender;
import com.epocal.common_ui.list.IListItem;
import com.epocal.common_ui.list.ListItem;
import com.epocal.common_ui.list.ListItemSectionHeader;
import com.epocal.common_ui.util.StringResourceValues;
import com.epocal.testhistoryfeature.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;

/**
 * This class represents Model object for patientTstInputDataAdapter.
 *
 * <p>
 * The list contains the Patient data in the order to be diplayed in
 * the list view.
 * </p>
 */
public class PatientTestInputDataList {
    private static final String TAG = PatientTestInputDataList.class.getSimpleName();
    private static final String NULL_INPUT_STRING = "-"; // indicate the null value when the user did not enter the data.
    private List<IListItem<String>> mList;
    private SparseIntArray mTestFieldGroupTypeEnum, mTestFieldTypeEnum;
    private PatientTestInputData mPatientData;
    private Map<String, String> mPatientDataCustomFieldToValueMap;
    private Context mContext;


    PatientTestInputDataList(Context context, TestRecord testRecord, WorkFlow workFlow) {
        mContext = context;
        mPatientData = new PatientTestInputData(testRecord);
        mPatientDataCustomFieldToValueMap = buildCustomFieldNameToValueMap(testRecord);
        mTestFieldGroupTypeEnum = StringResourceValues.setEpocTestFieldGroupType();
        mTestFieldTypeEnum = StringResourceValues.setEpocTestFieldType();
        mList = new ArrayList<>();

        buildInputDataSectionFromWorkFlow(workFlow);
    }

    private Map<String, String> buildCustomFieldNameToValueMap(TestRecord testRecord) {
        ArrayMap<String, String> customFieldNameToValueMap = new ArrayMap<>();
        // Build a Map of Custom field name to value map
        RealmList<CustomTestVariable> customFieldList = testRecord.getTestDetail().getCustomTestVariables();
        if ((customFieldList != null) && (!customFieldList.isEmpty())) {
            for (CustomTestVariable customVariable: customFieldList) {
                customFieldNameToValueMap.put(customVariable.getName(), customVariable.getValue());
            }
        }
        return customFieldNameToValueMap;
    }

    private void buildInputDataSectionFromWorkFlow(WorkFlow workFlow) {
        RealmList<WorkflowItem> workflowItems = workFlow.getWorkflowItems();

        for (WorkflowItem wfi : workflowItems) {
            EpocTestFieldGroupType groupType = wfi.getFieldGroupType();
            RealmList<TestInputField> inputFields = wfi.getFieldList(); // The list of the input fields in the group.
            RealmList<CustomTestInputField> customFields = wfi.getCustomFieldList(); // The list of the custom input fields.

            String groupTitle = mContext.getString(mTestFieldGroupTypeEnum.get(groupType.value));
            mList.add(new ListItemSectionHeader(groupTitle));

            // Text field type
            for (TestInputField inputField : inputFields) {
                EpocTestFieldType fieldType = inputField.getFieldType();
                String fieldTitle = mContext.getString(mTestFieldTypeEnum.get(fieldType.value));
                String fieldValue = getPatientTestInputFieldValueAsString(fieldType);
                mList.add(new ListItem(fieldTitle, fieldValue));
            }
            // Custom field type
            for (CustomTestInputField customField : customFields) {
                String fieldTitle = customField.getName();
                String fieldValue = mPatientDataCustomFieldToValueMap.get(fieldTitle);
                fieldValue = (fieldValue == null) ? NULL_INPUT_STRING : fieldValue;
                mList.add(new ListItem(fieldTitle, fieldValue));
            }
        }
    }

    // Find Patient input data for given fieldType.
    // The patient input data is converted to localized string representation of the value.
    private String getPatientTestInputFieldValueAsString(EpocTestFieldType fieldType) {
        Log.v(TAG, "fieldType = "+fieldType.name());

        String value;
        Map<EpocTestFieldType, String> valueMap = mPatientData.testFieldTypeToValueMap;

        switch (fieldType) {
            // String type
            case PATIENTID:
            case PATIENTID2:
            case PATIENTTEMPERATURE:
            case PATIENTAGE:
            case PATIENTLOCATION:
            case FIO2:
            case VT:
            case RR:
            case TR:
            case PEEP:
            case PS:
            case IT:
            case ET:
            case PIP:
            case MAP:
            case FLOW:
            case HERTZ:
            case DELTAP:
            case NOPPM:
            case RQ:
            case COMMENTS:
            case ORDERINGPHYSICIAN:
            case COLLECTEDBY:
            case COLLECTIONDATE:
            case COLLECTIONTIME:
            case ORDERDATE:
            case ORDERTIME:
            case NOTIFYNAME:
            case NOTIFYDATE:
            case NOTIFYTIME:
            case LOTNUMBER: {
                value = valueMap.get(fieldType);
                value = value.isEmpty() ? NULL_INPUT_STRING : value;
                break;
            }

            // Boolean type
            case HEMODILUTION:
            case REJECTTEST:
            case READBACK:
            {
                String[] valuesArray = mContext.getResources().getStringArray(R.array.yes_no); // No/Yes
                value = valueMap.get(fieldType);
                value = value.isEmpty() ? NULL_INPUT_STRING : (value.equals("0") ? valuesArray[0] : valuesArray[1]); 
                break;
            }

            // Enum
            case PATIENTGENDER: {
                Gender gender = mPatientData.gender;
                String[] valuesArray = mContext.getResources().getStringArray(R.array.gender); // Male/Female
                if (Gender.ENUM_UNINITIALIZED == gender) {
                    value = NULL_INPUT_STRING;
                } else if (Gender.Male == gender) {
                    value = valuesArray[0];
                } else {
                    value = valuesArray[1];
                }
                break;
            }

            // Enum
            case SAMPLETYPE: {
                // Map of BloodSampleType.value -> R.string.id
                SparseIntArray keyValues = StringResourceValues.setSampleType();
                int resId = keyValues.get(mPatientData.sampleType.value);
                // If 0, this value is not found or not supported. Assign NULL_INPUT_STRING in this case.
                value = (resId == 0) ? NULL_INPUT_STRING : mContext.getString(resId);
                break;
            }

            // Enum
            case DRAWSITE: {
                // Map of DrawSites.value -> R.string.id
                SparseIntArray keyValues = StringResourceValues.setDrawSite();
                int resId = keyValues.get(mPatientData.drawSite.value);
                // If 0, this value is not found or not supported. Assign NULL_INPUT_STRING in this case.
                value = (resId == 0) ? NULL_INPUT_STRING : mContext.getString(resId);
                break;
            }

            // Enum
            case DELIVERYSYSTEM: {
                // Map of DeliverySystem.value -> R.string.id
                SparseIntArray keyValues = StringResourceValues.setDeliverySystem();
                int resId = keyValues.get(mPatientData.deliverySystem.value);
                // If 0, this value is not found or not supported. Assign NULL_INPUT_STRING in this case.
                value = (resId == 0) ? NULL_INPUT_STRING : mContext.getString(resId);
                break;
            }

            // Enum
            case ALLENSTEST: {
                // Map of AllensTest.value -> R.string.id
                SparseIntArray keyValues = StringResourceValues.setAllensTest();
                int resId = keyValues.get(mPatientData.allensTest.value);
                // If 0, this value is not found or not supported. Assign NULL_INPUT_STRING in this case.
                value = (resId == 0) ? NULL_INPUT_STRING : mContext.getString(resId);
                break;
            }

            // Enum
            case MODE: {
                // Map of RespiratoryMode.value -> R.string.id
                SparseIntArray keyValues = StringResourceValues.setRespiratoryMode();
                int resId = keyValues.get(mPatientData.respiratoryMode.value);
                // If 0, this value is not found or not supported. Assign NULL_INPUT_STRING in this case.
                value = (resId == 0) ? NULL_INPUT_STRING : mContext.getString(resId);
                break;
            }

            // Enum
            case TESTTYPE: {
                // Map of TestType.value -> R.string.id
                SparseIntArray keyValues = StringResourceValues.setTestType();
                int resId = keyValues.get(mPatientData.testType.value);
                // If 0, this value is not found or not supported. Assign NULL_INPUT_STRING in this case.
                value = (resId == 0) ? NULL_INPUT_STRING : mContext.getString(resId);
                break;
            }

            // Enum
            case NOTIFYACTION: {
                // Map of NotifyActionType.value -> R.string.id
                SparseIntArray keyValues = StringResourceValues.setNotifyActionTypes();
                int resId = keyValues.get(mPatientData.notifyAction.value);
                // If 0, this value is not found or not supported. Assign NULL_INPUT_STRING in this case.
                value = (resId == 0) ? NULL_INPUT_STRING : mContext.getString(resId);
                break;
            }

            // Enum
            case FLUIDTYPE: {
                // TODO: FluidType used in QA test does not have localized string.
                // TODO: For now, just leave as NULL
                value = NULL_INPUT_STRING;
                break;
            }

            default: {
                value = NULL_INPUT_STRING;
                break;
            }
        }
        return value;
    }

    public List<IListItem<String>> getList() {
        return mList;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PatientTestInputDataList: START \n");
        if ((mList != null) && (mList.size() > 0)) {
            sb.append("size = ").append(mList.size()).append("\n");
            for (IListItem<String> item : mList) {
                if (item.isSectionHeader()) {
                    sb.append("Section Header = ").append(item.getTitle());
                } else {
                    sb.append("  ").append(item.getTitle()).append(" = ").append(item.getValue());
                }
                sb.append("\n");
            }
        }
        sb.append("PatientTestInputDataList: END \n");
        return sb.toString();
    }
}
