package com.epocal.testsettingsfeature.dataentrysettings.fieldsettings;

import com.epocal.common.realmentities.HostConfiguration;
import com.epocal.common.realmentities.InputFieldConfig;
import com.epocal.common.types.InputFieldType;
import com.epocal.common.types.SelenaFamilyType;
import com.epocal.datamanager.HostConfigurationModel;
import com.epocal.datamanager.InputFieldConfigModel;
import com.epocal.datamanager.SelenaModel;

import java.util.HashMap;

import static com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.FieldSettingsMenuAdapter.MENU_ACTION_ALLENS_TEST_ALLOW_USER_INPUT;
import static com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.FieldSettingsMenuAdapter.MENU_ACTION_ALLENS_TEST_EDIT_VALUES;
import static com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.FieldSettingsMenuAdapter.MENU_ACTION_DELIVERY_SYSTEM_ALLOW_USER_INPUT;
import static com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.FieldSettingsMenuAdapter.MENU_ACTION_DELIVERY_SYSTEM_EDIT_VALUES;
import static com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.FieldSettingsMenuAdapter.MENU_ACTION_DRAW_SITE_ALLOW_USER_INPUT;
import static com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.FieldSettingsMenuAdapter.MENU_ACTION_DRAW_SITE_EDIT_VALUES;
import static com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.FieldSettingsMenuAdapter.MENU_ACTION_HEMODILUTION;
import static com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.FieldSettingsMenuAdapter.MENU_ACTION_ID2_MAXIMUM_LENGTH;
import static com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.FieldSettingsMenuAdapter.MENU_ACTION_ID2_MINIMUM_LENGTH;
import static com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.FieldSettingsMenuAdapter.MENU_ACTION_PATIENT_ID_LOOKUP;
import static com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.FieldSettingsMenuAdapter.MENU_ACTION_PATIENT_ID_MAXIMUM_LENGTH;
import static com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.FieldSettingsMenuAdapter.MENU_ACTION_PATIENT_ID_MINIMUM_LENGTH;
import static com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.FieldSettingsMenuAdapter.MENU_ACTION_RESPIRATORY_MODE_ALLOW_USER_INPUT;
import static com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.FieldSettingsMenuAdapter.MENU_ACTION_RESPIRATORY_MODE_EDIT_VALUES;
import static com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.FieldSettingsMenuAdapter.MENU_ACTION_TEST_SELECTION;

/**
 * The Host Settings presenter class
 * <p>
 * Created by Zeeshan A Zakaria on 4/11/2017.
 */

class FieldSettingsPresenter implements IFieldSettingsPresenter {

    private IFieldSettingsView mFieldSettingsView;
    private FieldSettings mFieldSettings;

    FieldSettingsPresenter(IFieldSettingsView fieldSettingsView) {
        this.mFieldSettingsView = fieldSettingsView;
//        mFieldSettingsView = new FieldSettings();
    }

    @Override
    public HashMap<String, String[]> getListItems() {
        return mFieldSettings.getListItems();
    }

    @Override
    public String[] getListHeaders() {
        return mFieldSettings.getHeaders();
    }

    @Override
    public void switchToggled(int childId, boolean itemValue) {
        switch (childId) {
            case MENU_ACTION_PATIENT_ID_LOOKUP:
                HostConfigurationModel hostConfigurationModel = new HostConfigurationModel();
                HostConfiguration hostConfiguration = hostConfigurationModel.getUnmanagedHostConfiguration();
                hostConfiguration.setEnablePatientIdLookup(itemValue);
                hostConfigurationModel.updateHostConfiguration(hostConfiguration);
                break;

            case MENU_ACTION_DELIVERY_SYSTEM_ALLOW_USER_INPUT:
                setAllowCustomEntry(SelenaFamilyType.DELIVERYSYSTEM, itemValue);
                break;

            case MENU_ACTION_DRAW_SITE_ALLOW_USER_INPUT:
                setAllowCustomEntry(SelenaFamilyType.DRAWSITE, itemValue);
                break;

            case MENU_ACTION_RESPIRATORY_MODE_ALLOW_USER_INPUT:
                setAllowCustomEntry(SelenaFamilyType.RESPIRATORYMODE, itemValue);
                break;

            case MENU_ACTION_ALLENS_TEST_ALLOW_USER_INPUT:
                setAllowCustomEntry(SelenaFamilyType.ALLENSTYPE, itemValue);
                break;

            default:
                break;
        }
    }

    @Override
    public boolean groupClicked(int groupId) {
        System.out.print("This itemId is: " + groupId);

        switch (groupId) {
            case MENU_ACTION_TEST_SELECTION:
                mFieldSettingsView.gotoTestSelection();
                break;
        }

        return false;
    }

    @Override
    public boolean itemClicked(int childId) {
        boolean isClickHandled = true;
        boolean allowed;
        int maximumLength;
        int minimumLength;

        InputFieldConfigModel inputFieldConfigModel = new InputFieldConfigModel();
        InputFieldConfig inputFieldConfig;

        switch (childId) {
            case MENU_ACTION_PATIENT_ID_MAXIMUM_LENGTH:
                maximumLength = new InputFieldConfigModel().getInputFieldConfig(InputFieldType.PATIENT_ID).getMaximumLength();
                inputFieldConfig = inputFieldConfigModel.getInputFieldConfig(InputFieldType.PATIENT_ID);
                mFieldSettingsView.showDialogMaxMinLength(inputFieldConfig, Integer.toString(maximumLength), true);
                break;
            case MENU_ACTION_PATIENT_ID_MINIMUM_LENGTH:
                minimumLength = new InputFieldConfigModel().getInputFieldConfig(InputFieldType.PATIENT_ID).getMinimumLength();
                inputFieldConfig = inputFieldConfigModel.getInputFieldConfig(InputFieldType.PATIENT_ID);
                mFieldSettingsView.showDialogMaxMinLength(inputFieldConfig, Integer.toString(minimumLength), false);
                break;
            case MENU_ACTION_ID2_MAXIMUM_LENGTH:
                maximumLength = new InputFieldConfigModel().getInputFieldConfig(InputFieldType.ID_2).getMaximumLength();
                inputFieldConfig = inputFieldConfigModel.getInputFieldConfig(InputFieldType.ID_2);
                mFieldSettingsView.showDialogMaxMinLength(inputFieldConfig, Integer.toString(maximumLength), true);
                break;
            case MENU_ACTION_ID2_MINIMUM_LENGTH:
                minimumLength = new InputFieldConfigModel().getInputFieldConfig(InputFieldType.ID_2).getMinimumLength();
                inputFieldConfig = inputFieldConfigModel.getInputFieldConfig(InputFieldType.ID_2);
                mFieldSettingsView.showDialogMaxMinLength(inputFieldConfig, Integer.toString(minimumLength), false);
                break;
            case MENU_ACTION_DELIVERY_SYSTEM_ALLOW_USER_INPUT:
                allowed = new SelenaModel().isCustomEntryAllowed(SelenaFamilyType.DELIVERYSYSTEM);
                mFieldSettingsView.showDialogAllowUserInput(SelenaFamilyType.DELIVERYSYSTEM, allowed);
                break;
            case MENU_ACTION_DELIVERY_SYSTEM_EDIT_VALUES:
                mFieldSettingsView.gotoSelenaEditValues(SelenaFamilyType.DELIVERYSYSTEM);
//                mFieldSettingsView.gotoDeliverSystemEditValues();
                break;
            case MENU_ACTION_DRAW_SITE_ALLOW_USER_INPUT:
                allowed = new SelenaModel().isCustomEntryAllowed(SelenaFamilyType.DRAWSITE);
                mFieldSettingsView.showDialogAllowUserInput(SelenaFamilyType.DRAWSITE, allowed);
                break;
            case MENU_ACTION_DRAW_SITE_EDIT_VALUES:
                mFieldSettingsView.gotoSelenaEditValues(SelenaFamilyType.DRAWSITE);
                break;
            case MENU_ACTION_RESPIRATORY_MODE_ALLOW_USER_INPUT:
                allowed = new SelenaModel().isCustomEntryAllowed(SelenaFamilyType.RESPIRATORYMODE);
                mFieldSettingsView.showDialogAllowUserInput(SelenaFamilyType.RESPIRATORYMODE, allowed);
                break;
            case MENU_ACTION_RESPIRATORY_MODE_EDIT_VALUES:
                mFieldSettingsView.gotoSelenaEditValues(SelenaFamilyType.RESPIRATORYMODE);
                break;
            case MENU_ACTION_ALLENS_TEST_ALLOW_USER_INPUT:
                allowed = new SelenaModel().isCustomEntryAllowed(SelenaFamilyType.ALLENSTYPE);
                mFieldSettingsView.showDialogAllowUserInput(SelenaFamilyType.ALLENSTYPE, allowed);
                break;
            case MENU_ACTION_ALLENS_TEST_EDIT_VALUES:
                mFieldSettingsView.gotoSelenaEditValues(SelenaFamilyType.ALLENSTYPE);
                break;
            case MENU_ACTION_HEMODILUTION:
//                hostSettingsView.showDialogLanguages();
//                break;
            default:
                isClickHandled = false;
                break;
        }

        return isClickHandled;
    }

    @Override
    public void setAllowCustomEntry(SelenaFamilyType selenaFamilyType, boolean newValue) {
        new SelenaModel().setAllowCustomEntry(selenaFamilyType, newValue);
    }

    @Override
    public void saveInputFieldConfig(InputFieldConfig inputFieldConfig) {
        new InputFieldConfigModel().saveInputFieldConfig(inputFieldConfig);
    }
}
