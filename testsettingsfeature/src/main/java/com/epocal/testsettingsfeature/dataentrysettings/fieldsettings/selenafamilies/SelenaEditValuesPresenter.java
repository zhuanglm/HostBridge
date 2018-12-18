package com.epocal.testsettingsfeature.dataentrysettings.fieldsettings.selenafamilies;

import com.epocal.common.types.SelenaFamilyType;
import com.epocal.common.types.SelenaOperationTypes;
import com.epocal.datamanager.SelenaModel;

/**
 * The presenter class for the TestSelectionModel class
 * <p>
 * Created by Zeeshan A Zakaria on 9/08/2017.
 */

class SelenaEditValuesPresenter implements ISelenaEditValuesPresenter {

    @Override
    public boolean switchEnabledState(String itemName, SelenaFamilyType selenaFamilyType, boolean isChecked) {
        SelenaModel selenaModel = new SelenaModel();
        SelenaOperationTypes selenaOperationType = (isChecked ? SelenaOperationTypes.Enable : SelenaOperationTypes.Disable);
        selenaModel.applySelenaSelectionRules(selenaFamilyType, itemName, selenaOperationType);
        return true;
    }

    @Override
    public boolean switchSelectedState(String itemName, SelenaFamilyType selenaFamilyType, boolean isChecked) {
        SelenaModel selenaModel = new SelenaModel();
        SelenaOperationTypes selenaOperationType = (isChecked ? SelenaOperationTypes.Select : SelenaOperationTypes.UnSelect);
        selenaModel.applySelenaSelectionRules(selenaFamilyType, itemName, selenaOperationType);
        return true;
    }
}