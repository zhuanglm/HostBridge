package com.epocal.epoctestprocedure.fragments.dataentry;

import android.view.View;

import com.epocal.common.types.EpocTestFieldType;

import java.util.ArrayList;

/**
 * The interface to the DataValidator class
 *
 * Created by Zeeshan A Zakaria on 10/25/2017.
 */

interface IDataValidator {

    boolean validateHemodilution(View view);
    boolean validatePatientId(View view);
    boolean validateLotNumber(View view);
    boolean validateFluidType(View view);
    void defaultBehaviour();
    String getString(View view);
    void enableMandatorySteps(ArrayList<StepModel> stepModels);
    void disableMandatorySteps(ArrayList<StepModel> stepModels);
    boolean validateFields(EpocTestFieldType epocTestFieldType, View view, String customFieldName, boolean initialLoad, boolean mandatory);
    boolean saveCustomField(String fieldName, View view);
    void updateActivityTitle(String text);
}
