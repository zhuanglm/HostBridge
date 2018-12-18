package com.epocal.epoctestprocedure.fragments.dataentry;

import android.view.View;

import com.epocal.common.epocobjects.WorkflowField;
import com.epocal.common.types.EpocTestFieldType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The View part of the MVP
 *
 * Created by Zeeshan A Zakaria on 10/25/2017.
 */

interface IVerticalStepperView {
    void markCompleted();
    void markUncompleted(String value);
    void markMandatoryStepEditable();
    void markMandatoryStepCompleted();
    void markMandatoryStepIncomplete();
    void enableMandatoryStep(int stepNumber, boolean isCompleted, boolean expand);
    void disableStep(Integer stepNumber);
    void enableStep(Integer stepNumber);
    void enableOrDisableOptionalSteps();
    void showAllErrorMessages(HashMap<EpocTestFieldType, ErrorObject> error);
    void showErrorMessage(View view, String errorMessage);
    void hideErrorMessage(View view);
    void updateSubtitle(String value, int stepNumber);
    void disableDocumentResults();
    void enableDocumentResults();
    void openDocumentResults();
    void updateFieldsMakeAllEditable();
    void updateFieldsEditability(ArrayList<WorkflowField> nonEditableFields);
}