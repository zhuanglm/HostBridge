package com.epocal.common.epocuiobjects.stepper;

import android.app.Activity;
import android.graphics.Color;

/**
 * The builder for the stepper
 *
 * Created by Zeeshan A Zakaria on 10/30/2017.
 */

public class Builder {

    // Required parameters
    private VerticalStepperFormLayout verticalStepperFormLayout;
    String[] steps;
    private Integer[] mandatorySteps;
    VerticalStepperForm verticalStepperFormImplementation;
    Activity activity;

    // Optional parameters
    String[] stepsSubtitles = null;
    float alphaOfDisabledElements = 0.25f;
    int stepNumberBackgroundColor = Color.rgb(63, 81, 181);
    int buttonBackgroundColor = Color.rgb(63, 81, 181);
    int buttonPressedBackgroundColor = Color.rgb(48, 63, 159);
    int stepNumberTextColor = Color.rgb(255, 255, 255);
    int stepTitleTextColor = Color.rgb(33, 33, 33);
    int stepSubtitleTextColor = Color.rgb(162, 162, 162);
    int buttonTextColor = Color.rgb(255, 255, 255);
    int buttonPressedTextColor = Color.rgb(255, 255, 255);
    int errorMessageTextColor = Color.rgb(175, 18, 18);
    boolean materialDesignInDisabledSteps = false;
    boolean hideKeyboard = true;
    boolean showVerticalLineWhenStepsAreCollapsed = false;

    private Builder(VerticalStepperFormLayout stepperLayout,
            String[] steps,
            Integer[] mandatorySteps,
            VerticalStepperForm stepperImplementation,
            Activity activity) {

        this.verticalStepperFormLayout = stepperLayout;
        this.steps = steps;
        this.mandatorySteps = mandatorySteps;
        this.verticalStepperFormImplementation = stepperImplementation;
        this.activity = activity;
    }

    /**
     * Generates an instance of the builder that will set up and initialize the form (after
     * setting up the form it is mandatory to initialize it calling init())
     *
     * @param stepperLayout         the form layout
     * @param stepTitles            a String array with the names of the steps
     * @param stepperImplementation The instance that implements "VerticalStepperForm" interface
     * @param activity              The activity where the form is
     * @return an instance of the builder
     */
    public static Builder newInstance(VerticalStepperFormLayout stepperLayout,
                                      String[] stepTitles,
                                      Integer[] mandatorySteps,
                                      VerticalStepperForm stepperImplementation,
                                      Activity activity) {

        return new Builder(stepperLayout, stepTitles, mandatorySteps, stepperImplementation, activity);
    }

    /**
     * Set the subtitles of the steps
     *
     * @param stepsSubtitles a String array with the subtitles of the steps
     * @return the builder instance
     */
    public Builder stepsSubtitles(String[] stepsSubtitles) {
        this.stepsSubtitles = stepsSubtitles;
        return this;
    }

    /**
     * Set the primary color (background color of the left circles and buttons)
     *
     * @param colorPrimary primary color
     * @return the builder instance
     */
    public Builder primaryColor(int colorPrimary) {
        this.stepNumberBackgroundColor = colorPrimary;
        this.buttonBackgroundColor = colorPrimary;
        return this;
    }

    /**
     * Set the dark primary color (background color of the buttons when clicked)
     *
     * @param colorPrimaryDark primary color (dark)
     * @return the builder instance
     */
    public Builder primaryDarkColor(int colorPrimaryDark) {
        this.buttonPressedBackgroundColor = colorPrimaryDark;
        return this;
    }

    /**
     * Set the background color of the left circles
     *
     * @param stepNumberBackgroundColor background color of the left circles
     * @return the builder instance
     */
    public Builder stepNumberBackgroundColor(int stepNumberBackgroundColor) {
        this.stepNumberBackgroundColor = stepNumberBackgroundColor;
        return this;
    }

    /**
     * Set the background colour of the buttons
     *
     * @param buttonBackgroundColor background color of the buttons
     * @return the builder instance
     */
    public Builder buttonBackgroundColor(int buttonBackgroundColor) {
        this.buttonBackgroundColor = buttonBackgroundColor;
        return this;
    }

    /**
     * Set the background color of the buttons when clicked
     *
     * @param buttonPressedBackgroundColor background color of the buttons when clicked
     * @return the builder instance
     */
    public Builder buttonPressedBackgroundColor(int buttonPressedBackgroundColor) {
        this.buttonPressedBackgroundColor = buttonPressedBackgroundColor;
        return this;
    }

    /**
     * Set the text color of the left circles
     *
     * @param stepNumberTextColor text color of the left circles
     * @return the builder instance
     */
    public Builder stepNumberTextColor(int stepNumberTextColor) {
        this.stepNumberTextColor = stepNumberTextColor;
        return this;
    }

    /**
     * Set the text color of the step title
     *
     * @param stepTitleTextColor the color of the step title
     * @return this builder instance
     */
    public Builder stepTitleTextColor(int stepTitleTextColor) {
        this.stepTitleTextColor = stepTitleTextColor;
        return this;
    }

    /**
     * Set the text color of the step subtitle
     *
     * @param stepSubtitleTextColor the color of the step title
     * @return this builder instance
     */
    public Builder stepSubtitleTextColor(int stepSubtitleTextColor) {
        this.stepSubtitleTextColor = stepSubtitleTextColor;
        return this;
    }

    /**
     * Set the text color of the buttons
     *
     * @param buttonTextColor text color of the buttons
     * @return the builder instance
     */
    public Builder buttonTextColor(int buttonTextColor) {
        this.buttonTextColor = buttonTextColor;
        return this;
    }

    /**
     * Set the text color of the buttons when clicked
     *
     * @param buttonPressedTextColor text color of the buttons when clicked
     * @return the builder instance
     */
    public Builder buttonPressedTextColor(int buttonPressedTextColor) {
        this.buttonPressedTextColor = buttonPressedTextColor;
        return this;
    }

    /**
     * Set the error message color
     *
     * @param errorMessageTextColor error message color
     * @return the builder instance
     */
    public Builder errorMessageTextColor(int errorMessageTextColor) {
        this.errorMessageTextColor = errorMessageTextColor;
        return this;
    }

    /**
     * Set whether or not the disabled steps will have a Material Design look
     *
     * @param materialDesignInDisabledSteps true to use Material Design for disabled steps; false otherwise
     * @return the builder instance
     */
    public Builder materialDesignInDisabledSteps(boolean materialDesignInDisabledSteps) {
        this.materialDesignInDisabledSteps = materialDesignInDisabledSteps;
        return this;
    }

    /**
     * Specify whether or not the keyboard should be hidden at the beginning
     *
     * @param hideKeyboard true to hide the keyboard; false to not hide it
     * @return the builder instance
     */
    public Builder hideKeyboard(boolean hideKeyboard) {
        this.hideKeyboard = hideKeyboard;
        return this;
    }

    /**
     * Specify whether or not the vertical lines should be displayed when steps are collapsed
     *
     * @param showVerticalLineWhenStepsAreCollapsed true to show the lines; false to not
     * @return the builder instance
     */
    public Builder showVerticalLineWhenStepsAreCollapsed(boolean showVerticalLineWhenStepsAreCollapsed) {
        this.showVerticalLineWhenStepsAreCollapsed = showVerticalLineWhenStepsAreCollapsed;
        return this;
    }

    /**
     * Set the alpha level of disabled elements
     *
     * @param alpha alpha level of disabled elements (between 0 and 1)
     * @return the builder instance
     */
    public Builder alphaOfDisabledElements(float alpha) {
        this.alphaOfDisabledElements = alpha;
        return this;
    }

    /**
     * Set up the form and initialize it
     */
    public void init() {
        verticalStepperFormLayout.initialiseVerticalStepperForm(this, mandatorySteps);
    }

}