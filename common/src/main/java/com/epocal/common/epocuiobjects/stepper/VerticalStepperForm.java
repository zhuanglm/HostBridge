package com.epocal.common.epocuiobjects.stepper;

import android.view.View;

public interface VerticalStepperForm {

    /**
     * The llContent of the layout of the corresponding step must be generated here. The system will
     * automatically call this method for every step
     * @param stepNumber    the number of the step
     * @return              the view that will be automatically added as the llContent of the step
     */
    View createStepContentView(int stepNumber);

    /**
     * This method starts data validation. If there is no error, true is returned, otherwise false.
     *
     * @param stepNumber    the number of the step
     */
    boolean onConfirmClicked(int stepNumber);

    /**
     *
     */
    void updateOptionalSteps();

    /**
     * This method will be called when the user press the confirmation button
     */
    void sendData();

    /**
     *
     * @param activeStep
     */
    void onStepOpening(int activeStep);

    /**
     *
     */
    boolean isEditable(int stepNumber);
}