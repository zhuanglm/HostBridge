package com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.testselection;

import com.epocal.common.types.am.AnalyteName;
import com.epocal.common.types.SelenaOperationTypes;
import com.epocal.datamanager.AnalyteModel;

import java.util.ArrayList;

/**
 * The presenter class for the TestSelectionModel class
 * <p>
 * Created by Zeeshan A Zakaria on 8/2/2017.
 */

class TestSelectionPresenter implements ITestSelectionPresenter {

    // The SelenaOperationTypes are used for the Analytes options since there are no Enums for Analyte Operation Types

    @Override
    public boolean switchEnabledState(AnalyteName analyteName, boolean isChecked) {
        AnalyteModel analyteModel = new AnalyteModel();
        SelenaOperationTypes selenaOperationType = (isChecked ? SelenaOperationTypes.Enable : SelenaOperationTypes.Disable);
        analyteModel.applyAnalyteSelectionRules(analyteName, selenaOperationType);
        return true;
    }

    @Override
    public boolean switchSelectedState(AnalyteName analyteName, boolean isChecked) {
        AnalyteModel analyteModel = new AnalyteModel();
        SelenaOperationTypes selenaOperationType = (isChecked ? SelenaOperationTypes.Select : SelenaOperationTypes.UnSelect);
        analyteModel.applyAnalyteSelectionRules(analyteName, selenaOperationType);
        return true;
    }

    @Override
    public boolean switchEnabledState(ArrayList<AnalyteName> group, boolean isChecked) {
        AnalyteModel analyteModel = new AnalyteModel();
        SelenaOperationTypes selenaOperationType = (isChecked ? SelenaOperationTypes.Enable : SelenaOperationTypes.Disable);
        analyteModel.applyAnalyteSelectionRules(group, selenaOperationType);
        return true;
    }

    @Override
    public boolean switchSelectedState(ArrayList<AnalyteName> group, boolean isChecked) {
        AnalyteModel analyteModel = new AnalyteModel();
        SelenaOperationTypes selenaOperationType = (isChecked ? SelenaOperationTypes.Select : SelenaOperationTypes.UnSelect);
        analyteModel.applyAnalyteSelectionRules(group, selenaOperationType);
        return true;
    }
}