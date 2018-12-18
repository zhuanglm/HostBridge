package com.epocal.hostsettings;

/**
 * Created by Zeeshan A Zakaria on 4/11/2017.
 */

public interface IHostSettingsView {
    void showDialogNameSettings();

    void showDialogHospitalSettings();

    void showDialogLoginAuthentication();

    void showDialogEnableFIPS();

    void showDialogInactivityTimer(boolean disable);

    void showDialogInactivityLogout();

    void showDialogPowerOffOnLogout();

    void showDialogTemperatureUnit();

    void showDialogUsername();

    void showDialogPassword();

    void showDialogLanguages();

    void showDialogWorkflows();

    void showDialogAddUser();

    void showDialogEditUser();

    void showDialogSetPrinterFeature();

    void showDialogAddPrinter();

    void showDialogEditPrinter();

    void showDialogBarcodeUserId();

    void showDialogBarcodeUserPassword();

    void showDialogBarcodePatientId();

    void showDialogBarcodeFluidlot();

    void showDialogBarcodeId2();

    void showDialogBarcodeComments();

    void showDialogBarcodeOther();
}
