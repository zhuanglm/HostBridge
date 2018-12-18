package com.epocal.hostsettings;

import android.content.Context;

import com.epocal.common.LanguageTypeUtil;
import com.epocal.common.types.LanguageType;
import com.epocal.common_ui.types.UIValueBucket;
import com.epocal.datamanager.DeviceSettingsModel;
import com.epocal.datamanager.HostConfigurationModel;
import com.epocal.datamanager.WorkflowRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_ADD_PRINTER;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_ADD_USER;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_BARCODE_CHANGE_PASSWORD;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_BARCODE_CHANGE_PATIENTID;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_BARCODE_CHANGE_USERID;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_BARCOE_CHANGE_COMMENTS;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_BARCOE_CHANGE_FLUIDLOT;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_BARCOE_CHANGE_ID2;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_BARCOE_CHANGE_OTHER;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_CHANGE_HOSPITAL_NAME;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_CHANGE_LOGIN_AUTH;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_CHANGE_NAME;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_CHANGE_PASSWORD;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_CHANGE_USERNAME;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_EDIT_PRINTER;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_EDIT_USER;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_ENABLE_FIPS;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_ENABLE_INACTIVITY_LOGOUT;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_NO_UPGRADE;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_SET_INACTIVITY_TIMER;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_SET_LANGUAGE;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_SET_POWEROFF_ON_LOGOUT;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_SET_PRINTER_FEATURES;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_SET_TEMPERATURE_UNIT;
import static com.epocal.hostsettings.HostSettingsMenuAdapter.MENU_ACTION_SET_WORKFLOW;

/**
 * The Host Settings presenter class
 * <p>
 * Created by Zeeshan A Zakaria on 4/11/2017.
 */

class HostSettingsPresenter implements IHostSettingsPresenter {

    private IHostSettingsView hostSettingsView;
    private HostSettings hostSettings;
    private HostConfigurationModel mHostConfigurationModel;
    private DeviceSettingsModel mDeviceSettingsModel;

    HostSettingsPresenter(IHostSettingsView hostSettingsView) {
        this.hostSettingsView = hostSettingsView;
        hostSettings = new HostSettings();

        mHostConfigurationModel = new HostConfigurationModel();
        mDeviceSettingsModel = new DeviceSettingsModel();
    }

    @Override
    public HashMap<String, String[]> getListItems() {
        return hostSettings.getListItems();
    }

    @Override
    public String[] getListHeaders() {
        return hostSettings.getHeaders();
    }

    @Override
    public boolean itemClicked(int childId) {
        boolean isClickHandled = true;

        switch (childId) {
            case MENU_ACTION_CHANGE_NAME:
                hostSettingsView.showDialogNameSettings();
                break;
            case MENU_ACTION_CHANGE_HOSPITAL_NAME:
                hostSettingsView.showDialogHospitalSettings();
                break;
            case MENU_ACTION_CHANGE_LOGIN_AUTH:
                hostSettingsView.showDialogLoginAuthentication();
                break;
            case MENU_ACTION_ENABLE_FIPS:
                hostSettingsView.showDialogEnableFIPS();
                break;
            case MENU_ACTION_SET_INACTIVITY_TIMER:
                break;
            case MENU_ACTION_ENABLE_INACTIVITY_LOGOUT:
                hostSettingsView.showDialogInactivityLogout();
                break;
            case MENU_ACTION_SET_POWEROFF_ON_LOGOUT:
                hostSettingsView.showDialogPowerOffOnLogout();
                break;
            case MENU_ACTION_SET_TEMPERATURE_UNIT:
                hostSettingsView.showDialogTemperatureUnit();
                break;
            case MENU_ACTION_CHANGE_USERNAME:
                hostSettingsView.showDialogUsername();
                break;
            case MENU_ACTION_CHANGE_PASSWORD:
                hostSettingsView.showDialogPassword();
                break;
            case MENU_ACTION_SET_LANGUAGE:
                hostSettingsView.showDialogLanguages();
                break;
            case MENU_ACTION_SET_WORKFLOW:
                hostSettingsView.showDialogWorkflows();
                break;
            case MENU_ACTION_ADD_USER:
                hostSettingsView.showDialogAddUser();
                break;
            case MENU_ACTION_EDIT_USER:
                hostSettingsView.showDialogEditUser();
                break;
            case MENU_ACTION_SET_PRINTER_FEATURES:
                hostSettingsView.showDialogSetPrinterFeature();
                break;
            case MENU_ACTION_ADD_PRINTER:
                hostSettingsView.showDialogAddPrinter();
                break;
            case MENU_ACTION_EDIT_PRINTER:
                hostSettingsView.showDialogEditPrinter();
                break;
            case MENU_ACTION_BARCODE_CHANGE_USERID:
                hostSettingsView.showDialogBarcodeUserId();
                break;
            case MENU_ACTION_BARCODE_CHANGE_PASSWORD:
                hostSettingsView.showDialogBarcodeUserPassword();
                break;
            case MENU_ACTION_BARCODE_CHANGE_PATIENTID:
                hostSettingsView.showDialogBarcodePatientId();
                break;
            case MENU_ACTION_BARCOE_CHANGE_FLUIDLOT:
                hostSettingsView.showDialogBarcodeFluidlot();
                break;
            case MENU_ACTION_BARCOE_CHANGE_ID2:
                hostSettingsView.showDialogBarcodeId2();
                break;
            case MENU_ACTION_BARCOE_CHANGE_COMMENTS:
                hostSettingsView.showDialogBarcodeComments();
                break;
            case MENU_ACTION_BARCOE_CHANGE_OTHER:
                hostSettingsView.showDialogBarcodeOther();
                break;
            case MENU_ACTION_NO_UPGRADE:
                break;
            default:
                isClickHandled = false;
                break;
        }

        return isClickHandled;
    }

    public ArrayList<UIValueBucket> generateParametersForPrinterFeature() {
        ArrayList<UIValueBucket> somethingList = new ArrayList<>();
        UIValueBucket pair = new UIValueBucket();
        pair.setID(0);
        if (mHostConfigurationModel.isPrintRangeIfHighLow()) {
            pair.setValueString("TRUE");
        } else {
            pair.setValueString("FALSE");
        }
        somethingList.add(pair);

        pair = new UIValueBucket();
        pair.setID(1);
        if (mHostConfigurationModel.isPrintQARange()) {
            pair.setValueString("TRUE");
        } else {
            pair.setValueString("FALSE");
        }
        somethingList.add(pair);

        pair = new UIValueBucket();
        pair.setID(2);
        if (mHostConfigurationModel.isPrintQAInfo()) {
            pair.setValueString("TRUE");
        } else {
            pair.setValueString("FALSE");
        }
        somethingList.add(pair);
        return somethingList;
    }

    public void handlePrinterFeatureUpdate(int idx) {
        switch (idx) {
            case 0:
                if (mHostConfigurationModel.isPrintRangeIfHighLow()) {
                    mHostConfigurationModel.setPrintRangeIfHighLow(false);
                } else {
                    mHostConfigurationModel.setPrintRangeIfHighLow(true);
                }
                break;
            case 1:
                if (mHostConfigurationModel.isPrintQARange()) {
                    mHostConfigurationModel.setPrintQARange(false);
                } else {
                    mHostConfigurationModel.setPrintQARange(true);
                }
                break;
            case 2:
                if (mHostConfigurationModel.isPrintQAInfo()) {
                    mHostConfigurationModel.setPrintQAInfo(false);
                } else {
                    mHostConfigurationModel.setPrintQAInfo(true);
                }
                break;
        }
    }

    public ArrayList<UIValueBucket> generateParametersForHostLanguage(Context context) {
        List<LanguageType> mLanguageTypeList = LanguageTypeUtil.buildAvailableLanguageTypeList(context);
        ArrayList<UIValueBucket> list = new ArrayList<>();
        for (LanguageType type : mLanguageTypeList) {
            UIValueBucket v = new UIValueBucket();
            v.setID(type.getValue());
            v.setTextString(LanguageTypeUtil.fromLanguageTypeToLanguageString(context, type));
            list.add(v);
        }
        return list;
    }

    public void handleHostLanguageUpdate(int idx) {
        mDeviceSettingsModel.setLanguageType(LanguageType.fromInt(idx), true);
    }

    public ArrayList<UIValueBucket> generateParametersForWorkFlow(Context context) {
        final String[] allWorkflows = new WorkflowRepository().getAllWorkflowNames();
        ArrayList<UIValueBucket> list = new ArrayList<>();
        int idx = 0;
        for (String name : allWorkflows) {
            UIValueBucket v = new UIValueBucket();
            v.setID(idx);
            v.setTextString(name);
            list.add(v);
            idx++;
        }
        return list;
    }

}
