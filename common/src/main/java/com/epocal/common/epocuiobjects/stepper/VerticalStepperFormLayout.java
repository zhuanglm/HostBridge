package com.epocal.common.epocuiobjects.stepper;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.epocal.common.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Custom layout that implements a vertical stepper form
 */
public class VerticalStepperFormLayout extends RelativeLayout {

    // Style
    protected float alphaOfDisabledElements;
    protected int stepNumberBackgroundColor;
    protected int buttonBackgroundColor;
    protected int buttonPressedBackgroundColor;
    protected int stepNumberTextColor;
    protected int stepTitleTextColor;
    protected int stepSubtitleTextColor;
    protected int buttonTextColor;
    protected int buttonPressedTextColor;
    protected int errorMessageTextColor;
    protected boolean materialDesignInDisabledSteps;
    protected boolean hideKeyboard;
    protected boolean showVerticalLineWhenStepsAreCollapsed;

    // Views
    private ScrollView mScrollView;
    protected LayoutInflater mInflater;
    protected LinearLayout llContent;
    protected List<LinearLayout> stepLayouts;
    protected List<View> stepContentViews;
    protected List<TextView> stepsTitlesViews;
    protected List<TextView> stepsSubtitlesViews;
    protected AppCompatButton confirmationButton;

    // Data
    protected List<String> steps;
    protected List<String> stepsSubtitles;
    protected Integer[] mandatorySteps;

    // Logic
    protected int activeStep = 0;
    protected int visibleStep = 0;
    protected int numberOfSteps;
    protected boolean[] completedSteps;
    protected boolean isInteractionEnabled = true;

    // Listeners and callbacks
    protected VerticalStepperForm verticalStepperFormImplementation;
    protected StepperAnimationCallback animationCallback;

    // Context
    protected Context context;
    protected Activity activity;

    public VerticalStepperFormLayout(Context context) {
        super(context);
        init(context);
    }

    public VerticalStepperFormLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VerticalStepperFormLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mInflater.inflate(R.layout.vertical_stepper_form_layout, this, true);
    }

    public StepperAnimationCallback getAnimationCallback() {
        return animationCallback;
    }

    public void setAnimationCallback(StepperAnimationCallback animationCallback) {
        this.animationCallback = animationCallback;
    }

    /**
     * Returns the title of a step
     *
     * @param stepNumber The step number (counting from 0)
     * @return the title string
     */
    public String getStepTitle(int stepNumber) {
        return steps.get(stepNumber);
    }

    /**
     * Returns the subtitle of a step
     *
     * @param stepNumber The step number (counting from 0)
     * @return the subtitle string
     */
    public String getStepsSubtitles(int stepNumber) {
        if (stepsSubtitles != null) {
            return stepsSubtitles.get(stepNumber);
        }
        return null;
    }

    /**
     * Returns the active step number
     *
     * @return the active step number (counting from 0)
     */
    public int getActiveStepNumber() {
        return activeStep;
    }

    /**
     * Set the title of certain step
     *
     * @param stepNumber The step number (counting from 0)
     * @param title      New title of the step
     */
    public void setStepTitle(int stepNumber, String title) {
        if (title != null && !title.equals("")) {
            steps.set(stepNumber, title);
            TextView titleView = stepsTitlesViews.get(stepNumber);
            if (titleView != null) {
                titleView.setText(title);
            }
        }
    }

    /**
     * Set the subtitle of certain step
     *
     * @param stepNumber The step number (counting from 0)
     * @param subtitle   New subtitle of the step
     */
    public void setStepSubtitle(int stepNumber, String subtitle) {
        if (stepsSubtitles != null && subtitle != null && !subtitle.equals("")) {
            stepsSubtitles.set(stepNumber, subtitle);
            TextView subtitleView = stepsSubtitlesViews.get(stepNumber);
            if (subtitleView != null) {
                subtitleView.setText(subtitle);
            }
        }
    }

    /**
     * Set the active step as completed
     */
    public void setActiveStepAsCompleted() {
        setStepAsCompleted(activeStep);

    }

    /**
     * Set the active step as not completed
     *
     * @param errorMessage Error message that will be displayed (null for no message)
     */
    public void setActiveStepAsUncompleted(String errorMessage) {
        setStepAsUncompleted(activeStep, errorMessage);
    }

    public void setActiveMandatoryStepEditable() {
//        completedSteps[activeStep] = false;

        LinearLayout stepLayout = stepLayouts.get(activeStep);
        RelativeLayout stepHeader = (RelativeLayout) stepLayout.findViewById(R.id.step_header);
        ImageView stepDone = (ImageView) stepHeader.findViewById(R.id.step_done);
        TextView stepNumberTextView = (TextView) stepHeader.findViewById(R.id.step_number);
        LinearLayout circle = (LinearLayout) stepLayout.findViewById(R.id.circle);

        if (activeStep == 0) {
            circle.setBackground(context.getDrawable(R.drawable.circle_step_done_blue));
        } else {
            circle.setBackground(context.getDrawable(R.drawable.circle_step_done_gray));
        }

        stepDone.setVisibility(View.INVISIBLE);
        stepNumberTextView.setVisibility(View.VISIBLE);
    }

    /**
     * This methods marks the active step completed. Since it is used only when checking the mandatory
     * steps, so it is named such, but it can be used for any step whether mandatory or optional.
     */
    public void setActiveMandatoryStepCompleted() {
        completedSteps[activeStep] = true;
        LinearLayout stepLayout = stepLayouts.get(activeStep);
        RelativeLayout stepHeader = (RelativeLayout) stepLayout.findViewById(R.id.step_header);
        ImageView stepDone = (ImageView) stepHeader.findViewById(R.id.step_done);
        TextView stepNumberTextView = (TextView) stepHeader.findViewById(R.id.step_number);
        LinearLayout circle = (LinearLayout) stepLayout.findViewById(R.id.circle);
        circle.setBackground(context.getDrawable(R.drawable.circle_step_done_blue));
        stepDone.setVisibility(View.INVISIBLE);
        stepNumberTextView.setVisibility(View.VISIBLE);
    }

    /**
     * This methods marks the active step incomplete. Since it is used only when checking the mandatory
     * steps, so it is named such, but it can be used for any step whether mandatory or optional.
     */
    public void setActiveMandatoryStepIncomplete() {
        completedSteps[activeStep] = false;
        LinearLayout stepLayout = stepLayouts.get(activeStep);
        RelativeLayout stepHeader = (RelativeLayout) stepLayout.findViewById(R.id.step_header);
        ImageView stepDone = (ImageView) stepHeader.findViewById(R.id.step_done);
        TextView stepNumberTextView = (TextView) stepHeader.findViewById(R.id.step_number);
        LinearLayout circle = (LinearLayout) stepLayout.findViewById(R.id.circle);
        circle.setBackground(context.getDrawable(R.drawable.circle_step_done_blue));
        stepDone.setVisibility(View.INVISIBLE);
        stepNumberTextView.setVisibility(View.VISIBLE);
    }

    /**
     * This method marks a given step as completed, whether it is active or not.
     *
     * @param stepNumber the step number
     */
    public void markStepIconAsCompleted(int stepNumber) {
        completedSteps[stepNumber] = true;
    }

    public void makeConfirmVisible(int stepNumber) {
        LinearLayout stepLayout = stepLayouts.get(stepNumber);
        AppCompatButton confirmButton = stepLayout.findViewById(R.id.confirm_step);
        int[] location = new int[2];
        confirmButton.getLocationOnScreen(location);
        mScrollView.scrollTo(0, location[1]);
    }

    /**
     * Probably a redundant metod from the original library.
     * <p>
     * Set the step as completed
     *
     * @param stepNumber the step number (counting from 0)
     */
    public void setStepAsCompleted(int stepNumber) {
        markStepIconAsCompleted(stepNumber);

        LinearLayout stepLayout = stepLayouts.get(stepNumber);
        RelativeLayout stepHeader = (RelativeLayout) stepLayout.findViewById(R.id.step_header);
        ImageView stepDone = (ImageView) stepHeader.findViewById(R.id.step_done);
        TextView stepNumberTextView = (TextView) stepHeader.findViewById(R.id.step_number);
        LinearLayout errorContainer = (LinearLayout) stepLayout.findViewById(R.id.error_container);
        TextView errorTextView = (TextView) errorContainer.findViewById(R.id.error_message);
        AppCompatButton confirmButton = (AppCompatButton) stepLayout.findViewById(R.id.confirm_step);

        enableStepHeader(stepLayout);

        confirmButton.setEnabled(true);
        confirmButton.setAlpha(1);

        if (stepNumber != activeStep) {
            stepDone.setVisibility(View.VISIBLE);
            stepNumberTextView.setVisibility(View.INVISIBLE);
        }

        errorTextView.setText("");
        //errorContainer.setVisibility(View.GONE);
        Animations.slideUp(errorContainer, animationCallback);
    }

    /**
     * Set the step as not completed
     *
     * @param stepNumber   the step number (counting from 0)
     * @param errorMessage Error message that will be displayed (null for no message)
     */
    public void setStepAsUncompleted(int stepNumber, String errorMessage) {
        completedSteps[stepNumber] = false;

        LinearLayout stepLayout = stepLayouts.get(stepNumber);
        RelativeLayout stepHeader = (RelativeLayout) stepLayout.findViewById(R.id.step_header);
        ImageView stepDone = (ImageView) stepHeader.findViewById(R.id.step_done);
        TextView stepNumberTextView = (TextView) stepHeader.findViewById(R.id.step_number);
        AppCompatButton confirmButton = (AppCompatButton) stepLayout.findViewById(R.id.confirm_step);

        stepDone.setVisibility(View.INVISIBLE);
        stepNumberTextView.setVisibility(View.VISIBLE);

        confirmButton.setEnabled(false);
        confirmButton.setAlpha(alphaOfDisabledElements);

        if (stepNumber != activeStep) {
            disableStepHeader(stepLayout);
        }

        int step = numberOfSteps - 1; // Last index of the target array
        if (stepNumber < step) {
            setStepAsUncompleted(step, null);
        }

        if (errorMessage != null && !errorMessage.equals("")) {
            LinearLayout errorContainer = (LinearLayout) stepLayout.findViewById(R.id.error_container);
            TextView errorTextView = (TextView) errorContainer.findViewById(R.id.error_message);

            errorTextView.setText(errorMessage);
            //errorContainer.setVisibility(View.VISIBLE);
            Animations.slideDown(errorContainer, animationCallback);
        }
    }

    public void setEnableInteraction(boolean enable) {
        isInteractionEnabled = enable;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isInteractionEnabled)
            return true;
        else
            return super.dispatchTouchEvent(ev);
    }

    /**
     * Determines whether the active step is completed or not
     *
     * @return true if the active step is completed; false otherwise
     */
    public boolean isActiveStepCompleted() {
        return isStepCompleted(activeStep);
    }

    /**
     * Determines whether the given step is completed or not
     *
     * @param stepNumber the step number (counting from 0)
     * @return true if the step is completed, false otherwise
     */
    public boolean isStepCompleted(int stepNumber) {
        return completedSteps[stepNumber];
    }

    /**
     * Determines if any step has been completed
     *
     * @return true if at least 1 step has been completed; false otherwise
     */
    public boolean isAnyStepCompleted() {
        for (boolean completedStep : completedSteps) {
            if (completedStep) {
                return true;
            }
        }
        return false;
    }

    /**
     * // CAREFUL - RECURSIVE CALL //
     * Enable and go to the next mandatory step if it is not completed
     */
    public void enableAndGoToNextIncompleteMandatoryStep(Integer step) {
        int length = mandatorySteps.length;
        if (step <= length) {
            if (Arrays.asList(mandatorySteps).contains(step)) {
                enableStep(step, true, false);
                if (!completedSteps[step]) { // If the next mandatory step is not completed, open it.
                    goToStep(step);
                    return; // Stop the recursive call if next incomplete mandatory step is opened
                }
            }
        } else {
            return; // Stop the recursive call if end of array is reached
        }
        enableAndGoToNextIncompleteMandatoryStep(++step);
    }

    /**
     * Go to the selected step
     *
     * @param stepNumber the selected step number (counting from 0)
     */
    public void goToStep(int stepNumber) {
        if (activeStep != stepNumber) {
            if (hideKeyboard) {
                hideSoftKeyboard();
            }
            openStep(stepNumber);
        } else {
            if (visibleStep == stepNumber) {
                closeStepLayout(stepNumber);
                visibleStep = 999;
            } else {
                openStepLayout(stepNumber);
                visibleStep = stepNumber;
            }
        }
    }

    protected void initialiseVerticalStepperForm(Builder builder, Integer[] mandatorySteps) {

        this.verticalStepperFormImplementation = builder.verticalStepperFormImplementation;
        this.activity = builder.activity;
        this.mandatorySteps = mandatorySteps;
        this.alphaOfDisabledElements = builder.alphaOfDisabledElements;
        this.stepNumberBackgroundColor = builder.stepNumberBackgroundColor;
        this.buttonBackgroundColor = builder.buttonBackgroundColor;
        this.buttonPressedBackgroundColor = builder.buttonPressedBackgroundColor;
        this.stepNumberTextColor = builder.stepNumberTextColor;
        this.stepTitleTextColor = builder.stepTitleTextColor;
        this.stepSubtitleTextColor = builder.stepSubtitleTextColor;
        this.buttonTextColor = builder.buttonTextColor;
        this.buttonPressedTextColor = builder.buttonPressedTextColor;
        this.errorMessageTextColor = builder.errorMessageTextColor;
        this.materialDesignInDisabledSteps = builder.materialDesignInDisabledSteps;
        this.hideKeyboard = builder.hideKeyboard;
        this.showVerticalLineWhenStepsAreCollapsed = builder.showVerticalLineWhenStepsAreCollapsed;

        initStepperForm(builder.steps, builder.stepsSubtitles);
    }

    protected void initStepperForm(String[] stepsTitles, String[] stepsSubtitles) {
        setSteps(stepsTitles, stepsSubtitles);

        List<View> stepContentLayouts = new ArrayList<>();
        for (int i = 0; i < numberOfSteps; i++) {
            View stepLayout = verticalStepperFormImplementation.createStepContentView(i);
            stepContentLayouts.add(stepLayout);
        }
        stepContentViews = stepContentLayouts;

        initializeForm();
    }

    protected void setSteps(String[] steps, String[] stepsSubtitles) {
        this.steps = new ArrayList<>(Arrays.asList(steps));
        if (stepsSubtitles != null) {
            this.stepsSubtitles = new ArrayList<>(Arrays.asList(stepsSubtitles));
        } else {
            this.stepsSubtitles = null;
        }
        numberOfSteps = steps.length;
        setAuxVars();
    }

    /**
     * The display of the stepper starts from here.
     */
    protected void initializeForm() {
        stepsTitlesViews = new ArrayList<>();
        stepsSubtitlesViews = new ArrayList<>();
        setUpSteps();
        setObserverForKeyboard();
    }

    // http://stackoverflow.com/questions/2150078/how-to-check-visibility-of-software-keyboard-in-android
    protected void setObserverForKeyboard() {
        llContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //r will be populated with the coordinates of your view that area still visible.
                llContent.getWindowVisibleDisplayFrame(r);
            }
        });
    }

    protected void setUpSteps() {
        stepLayouts = new ArrayList<>();
        // Set up normal steps
        for (int i = 0; i < numberOfSteps; i++) {
            setUpStep(i);
        }
        // Set up confirmation step
        //setUpStep(numberOfSteps);
    }

    protected void setUpStep(int stepNumber) {
        LinearLayout stepLayout = createStepLayout(stepNumber);
        if (stepNumber < numberOfSteps) {
            // The llContent of the step is the corresponding custom view previously created
            RelativeLayout stepContent = (RelativeLayout) stepLayout.findViewById(R.id.step_content);
            stepContent.addView(stepContentViews.get(stepNumber));
        } else {
//            setUpStepLayoutAsConfirmationStepLayout(stepLayout);
        }
        addStepToContent(stepLayout);
    }

    protected void addStepToContent(LinearLayout stepLayout) {
        llContent.addView(stepLayout);
    }

    /**
     * ///
     * Here is where steps are initialized
     * ///
     *
     * @param stepNumber the step number
     * @return the step LinearLayout
     */
    protected LinearLayout createStepLayout(final int stepNumber) {
        LinearLayout stepLayout = generateStepLayout();

//        LinearLayout circle = (LinearLayout) stepLayout.findViewById(R.id.circle);
//        circle.setBackground(context.getDrawable(stepNumber == 0 ? R.drawable.circle_step_done_blue : R.drawable.circle_step_done_gray)); // For the Patient ID, i.e. step 0, the color will always be blue

        TextView stepTitle = (TextView) stepLayout.findViewById(R.id.step_title);
        stepTitle.setText(steps.get(stepNumber));
        stepTitle.setTextColor(stepTitleTextColor);
        stepsTitlesViews.add(stepNumber, stepTitle);

        TextView stepSubtitle = null;
        if (stepsSubtitles != null && stepNumber < stepsSubtitles.size()) {
            String subtitle = stepsSubtitles.get(stepNumber);
            if (subtitle != null) {
                stepSubtitle = (TextView) stepLayout.findViewById(R.id.step_subtitle);
                stepSubtitle.setText(subtitle);
                stepSubtitle.setTextColor(stepSubtitleTextColor);
//                stepSubtitle.setVisibility(View.VISIBLE);
            }
        }
        stepsSubtitlesViews.add(stepNumber, stepSubtitle);

        TextView stepNumberTextView = (TextView) stepLayout.findViewById(R.id.step_number);
        stepNumberTextView.setText(String.valueOf(stepNumber + 1));
        stepNumberTextView.setTextColor(stepNumberTextColor);

        ImageView stepDoneImageView = (ImageView) stepLayout.findViewById(R.id.step_done);
        ImageView stepStepEditable = (ImageView) stepLayout.findViewById(R.id.step_editable);

        // For the optional steps, don't display the step number but show the edit icon instead
        if (!Arrays.asList(mandatorySteps).contains(stepNumber)) {
            stepStepEditable.setVisibility(View.VISIBLE);
            stepNumberTextView.setVisibility(View.INVISIBLE);
        } else {
            stepStepEditable.setVisibility(View.INVISIBLE);
            stepNumberTextView.setVisibility(View.VISIBLE);
        }

        stepDoneImageView.setColorFilter(stepNumberTextColor);

        TextView errorMessage = (TextView) stepLayout.findViewById(R.id.error_message);
        ImageView errorIcon = (ImageView) stepLayout.findViewById(R.id.error_icon);
        errorMessage.setTextColor(errorMessageTextColor);
        errorIcon.setColorFilter(errorMessageTextColor);

        RelativeLayout stepHeader = (RelativeLayout) stepLayout.findViewById(R.id.step_header);
        stepHeader.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int prevStep = stepNumber - 1;
                if (prevStep < 0)
                    prevStep = 0;
                if (verticalStepperFormImplementation.isEditable(stepNumber)) {
                    if (Arrays.asList(mandatorySteps).contains(prevStep) && stepNumber > 0 && !completedSteps[prevStep])
                        return;
                    goToStep(stepNumber);
                }
            }
        });

        AppCompatButton confirmButton = (AppCompatButton) stepLayout.findViewById(R.id.confirm_step);
        setButtonColor(confirmButton, buttonBackgroundColor, buttonTextColor, buttonPressedBackgroundColor, buttonPressedTextColor);
        confirmButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean stepDone = verticalStepperFormImplementation.onConfirmClicked(stepNumber);
                if (stepDone) {  // We don't close a step if there is an error message displaying in any of the fields of this step
                    closeStepLayout(stepNumber);
                    enableAndGoToNextIncompleteMandatoryStep(stepNumber + 1);
                }
                verticalStepperFormImplementation.updateOptionalSteps();
            }
        });

        stepLayouts.add(stepLayout);

        return stepLayout;
    }

    protected LinearLayout generateStepLayout() {
        LayoutInflater inflater = LayoutInflater.from(context);
        return (LinearLayout) inflater.inflate(R.layout.step_layout, llContent, false);
    }

    protected void openStep(int stepNumber) {
        if (stepNumber >= 0 && stepNumber < numberOfSteps) {
            activeStep = stepNumber;

            for (int i = 0; i < numberOfSteps; i++) {
                if (i != stepNumber) {
                    closeStepLayout(i);
                } else {
                    openStepLayout(i);
                    visibleStep = i;
                }
            }

            if (stepNumber == numberOfSteps) {
                setStepAsCompleted(stepNumber);
            }

//            verticalStepperFormImplementation.onStepOpening(stepNumber);
        }
    }

    /**
     * Function control of enablement and expanded state of a step
     *
     * @param stepNumber the step number
     * @param enable     if the step is completed, then show the blue background, otherwise gray
     * @param expand     whether to expand or collapse the step
     */
    public void enableStep(Integer stepNumber, boolean enable, boolean expand) {
        if (stepNumber == null) return; // Stop here if the step number is null
        LinearLayout stepLayout = stepLayouts.get(stepNumber);
        enableStepHeader(stepLayout);
        RelativeLayout stepHeader = (RelativeLayout) stepLayout.findViewById(R.id.step_header);
        stepHeader.setEnabled(true);
        stepHeader.setVisibility(VISIBLE);
        hideVerticalLineInCollapsedStepIfNecessary(stepLayout);

        LinearLayout circle = (LinearLayout) stepLayout.findViewById(R.id.circle);

        if (enable || stepNumber == 0) { // Always keep a check for the Patient ID, which should always be blue
            circle.setBackground(context.getDrawable(R.drawable.circle_step_done_blue));
        } else {
            circle.setBackground(context.getDrawable(R.drawable.circle_step_done_gray));
        }

//        if (stepNumber == 0) {  // Step number 0 is for the Patient ID which will always appear opened when the stepper is loaded
//            openStepLayout(stepNumber);
//            hideSoftKeyboard();
//        } else
        if (expand) {
            openStepLayout(stepNumber);
        } else {
            closeStepLayout(stepNumber);
        }
    }

    /**
     * When a mandatory step is not editable, change its circle to gray and step number to a check mark
     *
     * @param stepNumber the step number
     */
    public void markMandatoryStepAsUneditable(Integer stepNumber) {
        if (stepNumber == null) return;
        LinearLayout stepLayout = stepLayouts.get(stepNumber);
        LinearLayout circle = (LinearLayout) stepLayout.findViewById(R.id.circle);
        RelativeLayout stepHeader = (RelativeLayout) stepLayout.findViewById(R.id.step_header);
        ImageView stepDone = (ImageView) stepHeader.findViewById(R.id.step_done);
        circle.setBackground(context.getDrawable(R.drawable.circle_step_done_blue));
        TextView stepNumberTextView = (TextView) stepHeader.findViewById(R.id.step_number);
        stepNumberTextView.setVisibility(View.INVISIBLE);
        stepDone.setVisibility(VISIBLE);
    }

    /**
     * When a mandatory step is not editable, change its circle to gray and step number to a check mark
     *
     * @param stepNumber the step number
     */
    public void markMandatoryStepAsEditable(Integer stepNumber) {
        if (stepNumber == null) return;
        LinearLayout stepLayout = stepLayouts.get(stepNumber);
        LinearLayout circle = (LinearLayout) stepLayout.findViewById(R.id.circle);
        circle.setBackground(context.getDrawable(R.drawable.circle_step_done_blue));
    }

    /**
     * Disable a given step and collapse it if it is expanded.
     *
     * @param stepNumber the step number
     */
    public void disableStep(Integer stepNumber) {
        if (stepNumber == null) return; // Stop here if the step number is null
        LinearLayout stepLayout = stepLayouts.get(stepNumber);
        LinearLayout circle = (LinearLayout) stepLayout.findViewById(R.id.circle);
        circle.setBackground(context.getDrawable(R.drawable.circle_step_done_gray));
        LinearLayout button = (LinearLayout) stepLayout.findViewById(R.id.confirm_button_container);
        RelativeLayout stepContent = (RelativeLayout) stepLayout.findViewById(R.id.step_content);
        RelativeLayout stepHeader = (RelativeLayout) stepLayout.findViewById(R.id.step_header);
        Animations.slideUp(button, animationCallback);
        Animations.slideUp(stepContent, animationCallback);
        stepHeader.setEnabled(false);
        disableStepHeader(stepLayout);
        showVerticalLineInCollapsedStepIfNecessary(stepLayout);
    }

    /**
     * Simply  hides the step, use enable layout before opening it
     * @param stepNumber
     */
    public void hideStep(Integer stepNumber) {
        if (stepNumber == null) return; // Stop here if the step number is null
        LinearLayout stepLayout = stepLayouts.get(stepNumber);
        LinearLayout circle = (LinearLayout) stepLayout.findViewById(R.id.circle);
        circle.setBackground(context.getDrawable(R.drawable.circle_step_done_gray));
        LinearLayout button = (LinearLayout) stepLayout.findViewById(R.id.confirm_button_container);
        RelativeLayout stepContent = (RelativeLayout) stepLayout.findViewById(R.id.step_content);
        RelativeLayout stepHeader = (RelativeLayout) stepLayout.findViewById(R.id.step_header);
        Animations.slideUp(button, animationCallback);
        Animations.slideUp(stepContent, animationCallback);
        stepHeader.setVisibility(GONE);
        disableStepHeader(stepLayout);
        showVerticalLineInCollapsedStepIfNecessary(stepLayout);
    }

    /**
     * Disable a step
     *
     * @param stepNumber the step number
     */
    protected void closeStepLayout(int stepNumber) {
        visibleStep = 999; // This is a flag to tell the UI that the step clicked is closed. This was important because after validation of a step the UI was going in a state where it won't open again
        LinearLayout stepLayout = stepLayouts.get(stepNumber);
        LinearLayout button = (LinearLayout) stepLayout.findViewById(R.id.confirm_button_container);
        RelativeLayout stepContent = (RelativeLayout) stepLayout.findViewById(R.id.step_content);
        Animations.slideUp(button, animationCallback);
        Animations.slideUp(stepContent, animationCallback);
        showVerticalLineInCollapsedStepIfNecessary(stepLayout);
    }

    /**
     * Enable and expand a step
     *
     * @param stepNumber the step number
     */
    protected void openStepLayout(int stepNumber) {
        LinearLayout stepLayout = stepLayouts.get(stepNumber);
        RelativeLayout stepContent = (RelativeLayout) stepLayout.findViewById(R.id.step_content);
        RelativeLayout stepHeader = (RelativeLayout) stepLayout.findViewById(R.id.step_header);
        ImageView stepDone = (ImageView) stepHeader.findViewById(R.id.step_done);
        LinearLayout button = (LinearLayout) stepLayout.findViewById(R.id.confirm_button_container);

        enableStepHeader(stepLayout);
        verticalStepperFormImplementation.onStepOpening(activeStep); // actions to take place before the step is opened

        Animations.slideDown(stepContent, animationCallback);
        Animations.slideDown(button, animationCallback);

        if (completedSteps[stepNumber] && activeStep != stepNumber && !Arrays.asList(mandatorySteps).contains(stepNumber)) {
            stepDone.setVisibility(View.VISIBLE);
        } else {
            stepDone.setVisibility(View.INVISIBLE);
        }

        hideVerticalLineInCollapsedStepIfNecessary(stepLayout);

    }

    protected void enableStepHeader(LinearLayout stepLayout) {
        setHeaderAppearance(stepLayout, 1, buttonBackgroundColor);
    }

    protected void disableStepHeader(LinearLayout stepLayout) {
        setHeaderAppearance(stepLayout, alphaOfDisabledElements, Color.rgb(176, 176, 176));
    }

    protected void showVerticalLineInCollapsedStepIfNecessary(LinearLayout stepLayout) {
        // The height of the line will be 16dp when the subtitle textview is gone
        if (showVerticalLineWhenStepsAreCollapsed) {
            setVerticalLineNearSubtitleHeightWhenSubtitleIsGone(stepLayout, 16);
        }
    }

    protected void hideVerticalLineInCollapsedStepIfNecessary(LinearLayout stepLayout) {
        // The height of the line will be 0 when the subtitle text is being shown
        if (showVerticalLineWhenStepsAreCollapsed) {
            setVerticalLineNearSubtitleHeightWhenSubtitleIsGone(stepLayout, 0);
        }
    }

    protected void setAuxVars() {
        completedSteps = new boolean[numberOfSteps];
        for (int i = 0; i < (numberOfSteps); i++) {
            completedSteps[i] = false;
        }
    }

    protected void disableConfirmationButton() {
        confirmationButton.setEnabled(false);
        confirmationButton.setAlpha(alphaOfDisabledElements);
    }

    protected void hideSoftKeyboard() {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void prepareSendingAndSend() {
        displayDoneIconInConfirmationStep();
        disableConfirmationButton();
        verticalStepperFormImplementation.sendData();
    }

    protected void displayDoneIconInConfirmationStep() {
        LinearLayout confirmationStepLayout = stepLayouts.get(stepLayouts.size() - 1);
        ImageView stepDone = (ImageView) confirmationStepLayout.findViewById(R.id.step_done);
        TextView stepNumberTextView = (TextView) confirmationStepLayout.findViewById(R.id.step_number);
        stepDone.setVisibility(View.VISIBLE);
        stepNumberTextView.setVisibility(View.INVISIBLE);
    }

    protected int convertDpToPixel(float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    protected void setVerticalLineNearSubtitleHeightWhenSubtitleIsGone(LinearLayout stepLayout, int height) {
        TextView stepSubtitle = (TextView) stepLayout.findViewById(R.id.step_subtitle);
        if (stepSubtitle.getVisibility() == View.GONE) {
            LinearLayout stepLeftLine = (LinearLayout) stepLayout.findViewById(R.id.vertical_line_subtitle);
            LayoutParams params = (LayoutParams) stepLeftLine.getLayoutParams();
            params.height = convertDpToPixel(height);
            stepLeftLine.setLayoutParams(params);
        }
    }

    protected void setHeaderAppearance(LinearLayout stepLayout, float alpha, int stepCircleBackgroundColor) {
        if (!materialDesignInDisabledSteps) {
            RelativeLayout stepHeader = (RelativeLayout) stepLayout.findViewById(R.id.step_header);
            TextView title = (TextView) stepHeader.findViewById(R.id.step_title);
            TextView subtitle = (TextView) stepHeader.findViewById(R.id.step_subtitle);
            LinearLayout circle = (LinearLayout) stepHeader.findViewById(R.id.circle);
            ImageView done = (ImageView) stepHeader.findViewById(R.id.step_done);

            title.setAlpha(alpha);
            circle.setAlpha(alpha);
            done.setAlpha(alpha);

//            if (subtitle.getText() != null && !subtitle.getText().equals("")) {
//                if (alpha == 1) {
//                    subtitle.setVisibility(View.VISIBLE);
//                } else {
//                    subtitle.setVisibility(View.GONE);
//                }
//            }
        } else {
            setStepCircleBackgroundColor(stepLayout, stepCircleBackgroundColor);
        }
    }

    protected void setStepCircleBackgroundColor(LinearLayout stepLayout, int color) {
        LinearLayout circle = (LinearLayout) stepLayout.findViewById(R.id.circle);
        Drawable bg = ContextCompat.getDrawable(context, R.drawable.circle_step_done_blue);
        bg.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        circle.setBackground(bg);
    }

    protected void setButtonColor(AppCompatButton button, int buttonColor, int buttonTextColor,
                                  int buttonPressedColor, int buttonPressedTextColor) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_pressed},
                new int[]{android.R.attr.state_focused},
                new int[]{}
        };
        ColorStateList buttonColours = new ColorStateList(
                states,
                new int[]{
                        buttonPressedColor,
                        buttonPressedColor,
                        buttonColor
                });
        ColorStateList buttonTextColours = new ColorStateList(
                states,
                new int[]{
                        buttonPressedTextColor,
                        buttonPressedTextColor,
                        buttonTextColor
                });
        button.setSupportBackgroundTintList(buttonColours);
        button.setTextColor(buttonTextColours);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        llContent = (LinearLayout) findViewById(R.id.content);
        mScrollView = (ScrollView) findViewById(R.id.scroll_view_stepper);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putInt("activeStep", this.activeStep);
        bundle.putBooleanArray("completedSteps", this.completedSteps);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) // implicit null check
        {
            Bundle bundle = (Bundle) state;
            this.activeStep = bundle.getInt("activeStep");
            this.completedSteps = bundle.getBooleanArray("completedSteps");
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }

    public void updateSubtitle(String value, int stepNumber) {
        TextView textView = stepsSubtitlesViews.get(stepNumber);
        if (textView != null) {
            textView.setText(value);
        }
    }

    public void scrollToHeader(int stepNumber) {
        int childCount = llContent.getChildCount();
        int height = 0;
        for (int i = 0; i < childCount; i++) {
            View view = llContent.getChildAt(stepNumber);
            LinearLayout enclosure = (LinearLayout) view.findViewById(R.id.enclosure);
            height += enclosure.getHeight();

            if (i == stepNumber) {
                final int h = height + 1000;
                Log.d("APP", "Y: " + h);
                mScrollView.post(new Runnable() {
                    public void run() {
                        mScrollView.smoothScrollTo(0, h);
                        mScrollView.pageScroll(View.FOCUS_DOWN);
                    }
                });
                break;
            }
        }
    }
}