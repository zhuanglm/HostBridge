package com.epocal.epoctestprocedure.fragments.dataentry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.epocal.common.CU;
import com.epocal.common.epocobjects.AnalyteOption;
import com.epocal.common.epocobjects.WorkflowField;
import com.epocal.common.epocuiobjects.stepper.Builder;
import com.epocal.common.epocuiobjects.stepper.StepperAnimationCallback;
import com.epocal.common.epocuiobjects.stepper.VerticalStepperForm;
import com.epocal.common.realmentities.TestInputField;
import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.realmentities.WorkFlow;
import com.epocal.common.realmentities.WorkflowItem;
import com.epocal.common.types.EpocTestFieldGroupType;
import com.epocal.common.types.EpocTestFieldType;
import com.epocal.common.types.EpocTestPanelType;
import com.epocal.common.types.am.AnalyteName;
import com.epocal.common_ui.util.StringResourceValues;
import com.epocal.epoctest.uidriver.TestUIDriver;
import com.epocal.epoctestprocedure.IPatientTestPresenter;
import com.epocal.epoctestprocedure.PatientTestActivity;
import com.epocal.epoctestprocedure.R;
import com.epocal.epoctestprocedure.fragments.dataentry.DateAndTime.ConveyDateTime;
import com.epocal.epoctestprocedure.fragments.dataentry.DateAndTime.DatePickerFragment;
import com.epocal.epoctestprocedure.fragments.dataentry.DateAndTime.TimePickerFragment;
import com.epocal.epoctestprocedure.stepper.IStepperItem;
import com.epocal.epoctestprocedure.stepper.IStepperValue;
import com.epocal.epoctestprocedure.stepper.StepperItem;
import com.epocal.epoctestprocedure.stepper.StepperItemBuilder;
import com.epocal.epoctestprocedure.stepper.types.StepperValueType;
import com.epocal.hardware.EMDKScanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;

/**
 * This class converts Workflow input data to the format understand by VerticalStepperInputFormLayout widget.
 * Also acts as interface for your input from the widget
 */
public class VerticalStepper implements VerticalStepperForm, IVerticalStepperView, ConveyDateTime {
    private static final String TAG = VerticalStepper.class.getSimpleName();
    private static final ColorStateList sColorStateListRadioButton = new ColorStateList(
            new int[][]{new int[]{-android.R.attr.state_checked},
                    new int[]{android.R.attr.state_checked}
            },
            new int[]{
                    Color.GRAY, Color.rgb(33, 150, 243)
            }
    );
    private boolean mOptionalsEnabled = false;
    private Context mContext;
    private VerticalStepperInputLayout mVerticalStepperInputLayout;
    private LayoutInflater mInflater;
    private ArrayList<StepModel> mStepModels; // TODO: Covert to HashMap <EpocTestField, StepModel> so that instead of going over for loops, we could extract the right StepModel by its field name
    private EMDKScanner mEMDKScanner;
    private SparseIntArray mTestFieldGroupTypeEnum, mTestFieldTypeEnum;
    private RealmList<WorkflowItem> mWorkflowGroup;
    private FragmentManager mFragmentManager;
    private TestUIDriver mTestUIDriver;
    private StepperItemBuilder mStepperItemBuilder;
    private IPatientTestPresenter mPatientTestPresenter;


    private DialogFragment mActiveDialogFragment = null; // one of CustomTestSelectionDialog, TimePicker, DatePicker
    private Dialog mActiveDialog = null; // Yes/No confirm dialog for REJECT TEST field creates Dialog

    public VerticalStepper(Context context, VerticalStepperInputLayout verticalStepperInputLayout, WorkFlow workFlow, TestUIDriver testUIDriver, FragmentManager fragmentManager, IPatientTestPresenter patientTestPresenter) {
        mStepModels = new ArrayList<>();
        mContext = context;
        mTestUIDriver = testUIDriver;
        mInflater = LayoutInflater.from(mContext);
        mVerticalStepperInputLayout = verticalStepperInputLayout;
        mTestFieldGroupTypeEnum = StringResourceValues.setEpocTestFieldGroupType();
        mTestFieldTypeEnum = StringResourceValues.setEpocTestFieldType();
        mWorkflowGroup = workFlow.getWorkflowItems();
        mStepperItemBuilder = new StepperItemBuilder();
        mFragmentManager = fragmentManager;
        mPatientTestPresenter = patientTestPresenter;

        // TODO: Remove - debugging printout
        printWorkflow(mContext, workFlow);

    }

    /**
     * Validating mandatory steps except for the Patient ID which is dealt with separately.
     */
    public void validateAllMandatorySteps() {
        boolean disableRemaining = false;
        for (StepModel stepModel : mStepModels) {
            int stepNumber = stepModel.getStepNumber();
            boolean isMandatory = stepModel.isMandatory();
            if (stepNumber >= 0 && isMandatory) {
                if (!disableRemaining) {
                    View view = stepModel.getViewGroup();
                    EpocTestFieldType fieldType = stepModel.getEpocTestFieldType();
                    boolean returnValue = stepModel.getStepperItem().isValidated(mTestUIDriver);
                    if (returnValue) {
                        hideErrorMessage(view);
                        stepModel.setCompleted(returnValue);
                        mVerticalStepperInputLayout.markStepIconAsCompleted(stepNumber);
                    } else {
                        disableRemaining = true;
                    }
                }

                if (disableRemaining) {
                    mVerticalStepperInputLayout.enableStep(stepNumber, false, false);
                }
            }
        }
    }

    public void buildForm() {
        int colorPrimary = ContextCompat.getColor(mContext, R.color.primaryBlueNew);
        int colorPrimaryDark = ContextCompat.getColor(mContext, R.color.primaryBlueNew);
        int size = mWorkflowGroup.size();
        int stepNumber = 0; // First step is labelled as 1 but in the Stepper array it starts at 0

        mStepperItemBuilder.getGroups().clear();
        for (int i = 0; i < size; i++) {
            WorkflowItem workflowItem = mWorkflowGroup.get(i);
            mStepperItemBuilder.populateFromWorkflowItem(mContext, workflowItem);
        }

        for(Map.Entry<EpocTestFieldGroupType,List<StepperItem>> entry : mStepperItemBuilder.getGroups().entrySet()) {
            EpocTestFieldGroupType groupType = entry.getKey();
            List<StepperItem> vsItem = entry.getValue();
            if(groupType == EpocTestFieldGroupType.MANDATORY) {
                for (int i = 0; i < vsItem.size(); i++) {
                    View view = createView(vsItem.get(i));
                    StepModel stepModel = new StepModel(vsItem.get(i).getTitle(), true, true, groupType, vsItem.get(i).getTestFieldType(), stepNumber++);
                    stepModel.setEpocTestFieldGroupType(EpocTestFieldGroupType.MANDATORY);
                    stepModel.setStepperItem(vsItem.get(i));
                    ViewGroup viewGroup = createStepContainerView();
                    view.findViewById(R.id.field_title).setVisibility(View.GONE); // hide field title
                    viewGroup.addView(view);
                    stepModel.setViewGroup(viewGroup);
                    mStepModels.add(stepModel);
                }
            } else if(groupType == EpocTestFieldGroupType.QA_TEST_TYPE || groupType == EpocTestFieldGroupType.QA_COMMENTS) {
                    for (int i = 0; i < vsItem.size(); i++) {
                        View view = createView(vsItem.get(i));
                        StepModel stepModel = new StepModel(vsItem.get(i).getTitle(), false, true, groupType, vsItem.get(i).getTestFieldType(), stepNumber++);
                        stepModel.setEpocTestFieldGroupType(groupType);
                        stepModel.setStepperItem(vsItem.get(i));
                        ViewGroup viewGroup = createStepContainerView();
                        view.findViewById(R.id.field_title).setVisibility(View.GONE); // hide field title
                        viewGroup.addView(view);
                        stepModel.setViewGroup(viewGroup);
                        mStepModels.add(stepModel);
                    }
            } else if (groupType == EpocTestFieldGroupType.COMPLIANCE || groupType == EpocTestFieldGroupType.CUSTOMTESTVARIABLES_OPTIONAL) {
                String groupTitle = mContext.getString(mTestFieldGroupTypeEnum.get(groupType.value));
                StepModel stepModel = new StepModel(groupTitle, false, true, groupType, EpocTestFieldType.UNKNOWN, stepNumber++);
                stepModel.setEpocTestFieldGroupType(groupType);
                ViewGroup viewGroup = createStepContainerView();
                for (int i = 0; i < vsItem.size(); i++) {
                    if(vsItem.get(i).isCustom())
                        continue;
                    EpocTestFieldType fieldType = vsItem.get(i).getTestFieldType();
                    int fieldOrder = vsItem.get(i).getDisplayOrder();
                    View view = createView(vsItem.get(i));
                    StepModel.ChildFields childField = stepModel.new ChildFields(fieldType, view, fieldOrder, false);
                    childField.setStepperItem(vsItem.get(i));
                    stepModel.addToChildFields(childField);
                    if (view != null) {
                        viewGroup.addView(view);
                    }
                }

                // Custom fields
                for (int i = 0; i < vsItem.size(); i++) {
                    if(!vsItem.get(i).isCustom())
                        continue;
                    int fieldOrder = vsItem.get(i).getDisplayOrder();
                    View view = createView(vsItem.get(i));
                    StepModel.ChildFields childField = stepModel.new ChildFields(EpocTestFieldType.CUSTOM, view, fieldOrder, true);
                    childField.setCustomFieldName(vsItem.get(i).getTitle());
                    childField.setStepperItem(vsItem.get(i));
                    stepModel.addCustomFieldValue(vsItem.get(i).getTitle(), view);
                    stepModel.addToChildFields(childField);
                    if (view != null) {
                        viewGroup.addView(view);
                    }
                }
                stepModel.setViewGroup(viewGroup);
                mStepModels.add(stepModel);
            }
            // Optional fields
            else {
                String groupTitle = mContext.getString(mTestFieldGroupTypeEnum.get(groupType.value));
                StepModel stepModel = new StepModel(groupTitle, false, true, groupType, EpocTestFieldType.UNKNOWN, stepNumber++);
                stepModel.setEpocTestFieldGroupType(groupType);
                ViewGroup viewGroup = createStepContainerView();
                // Collect input fields as group
                for (int i = 0; i < vsItem.size(); i++) {
                    EpocTestFieldType fieldType = vsItem.get(i).getTestFieldType();
                    int fieldOrder = vsItem.get(i).getDisplayOrder();
                    stepModel.setFieldOrder(fieldOrder);
                    View view = createView(vsItem.get(i));
                    StepModel.ChildFields childFields = stepModel.new ChildFields(fieldType, view, fieldOrder, false);
                    childFields.setStepperItem(vsItem.get(i));
                    stepModel.addToChildFields(childFields);
                }

                stepModel.sortChildFields();
                ArrayList<StepModel.ChildFields> childFields = stepModel.getChildFields();
                for (StepModel.ChildFields childFields1 : childFields) {
                    View view = childFields1.getView();
                    if (view != null) {
                        viewGroup.addView(view);
                    }
                }
                stepModel.setViewGroup(viewGroup);
                mStepModels.add(stepModel);
            }
        }

        // Here we find and initialize the form
        String[] stepTitles = new String[mStepModels.size()];
        String[] stepSubTitles = new String[mStepModels.size()];
        Integer[] mandatorySteps = new Integer[mStepModels.size()];
        int count = 0;
        for (StepModel stepModel : mStepModels) {
            stepTitles[count] = stepModel.getTitle();
            stepSubTitles[count] = mContext.getString(R.string.OptionalStep);
            if(stepModel.isMandatory())
                stepSubTitles[count] = stepModel.getStepperItem().getSubtitleValue(mTestUIDriver);
            else {
                switch (stepModel.getEpocTestFieldGroupType()) {
                    case COMPLIANCE:
                        stepSubTitles[count] = mContext.getString(R.string.RequiredByHospitalPolicy);
                        break;
                    default:
                        break;
                }
            }
            mandatorySteps[count] = (stepModel.isMandatory() ? stepModel.getStepNumber() : 999);
            count++;
        }
        Builder.newInstance(mVerticalStepperInputLayout, stepTitles, mandatorySteps, this, (Activity) mContext)
                .primaryColor(colorPrimary)
                .primaryDarkColor(colorPrimaryDark)
                .stepsSubtitles(stepSubTitles)
                .init();

        disableMandatorySteps();
        enableMandatoryStep(0, false, false); // Enable the Patient ID
    }

    private View createView(StepperItem stepperItem) {
        String hint = "";
        switch(stepperItem.getHintType()) {
            case VS_HINT_ENTERORSCAN:
                hint = mContext.getString(R.string.textinput_data_entry_or_scan_hint, stepperItem.getTitle());
                break;
            case VS_HINT_ENTER:
                hint = mContext.getString(R.string.textinput_data_entry_hint, stepperItem.getTitle());
                break;
            case VS_HINT_ENTERORSELECT:
                hint = mContext.getString(R.string.textinput_data_entry_or_select_hint, stepperItem.getTitle());
                break;
        }

        View view = null;
        switch(stepperItem.getDataType()) {
            case VS_BARCODE:
                {
                    String value = "";
                    if (stepperItem.getValue(mTestUIDriver).getType() == StepperValueType.VT_STRING)
                        value = (String) stepperItem.getValue(mTestUIDriver).getValue();

                    view = createTextFieldView(mInflater, stepperItem.getTitle(), value, hint, false, true, stepperItem.getErrorMessage());
                    ImageView imageViewBarcode = view.findViewById(R.id.image_view_barcode);

                    imageViewBarcode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mEMDKScanner = new EMDKScanner(mContext);
                        }
                    });
                }
                break;
            case VS_STRING:
                {
                    String value = "";
                    if (stepperItem.getValue(mTestUIDriver).getType() == StepperValueType.VT_STRING)
                        value = (String) stepperItem.getValue(mTestUIDriver).getValue();

                    view = createTextFieldView(mInflater, stepperItem.getTitle(), value, hint, false, false, stepperItem.getErrorMessage());
                }
                break;
            case VS_INTEGER:
                {
                    String value = "";
                    if (stepperItem.getValue(mTestUIDriver).getType() == StepperValueType.VT_STRING)
                        value = (String) stepperItem.getValue(mTestUIDriver).getValue();

                    view = createTextFieldView(mInflater, stepperItem.getTitle(), value, hint, true, false, stepperItem.getErrorMessage());
                }
                break;
            case VS_RADIO:
                {
                    Integer value = -1;
                    if (stepperItem.getValue(mTestUIDriver).getType() == StepperValueType.VT_ARRAY)
                        value = (Integer) stepperItem.getValue(mTestUIDriver).getValue();
                    view = createRadioGroup(mInflater, stepperItem.getTitle(), stepperItem.getOptionsArray(), true, value);
                }
                break;
            case VS_COMBO:
                {
                    String value = "";
                    if (stepperItem.getValue(mTestUIDriver).getType() == StepperValueType.VT_STRING)
                        value = (String) stepperItem.getValue(mTestUIDriver).getValue();
                    view = createComboBox(mInflater, stepperItem.getTitle(), hint, stepperItem.getOptionsArray(), value);
                }
                break;
            case VS_DATE:
                {
                    String value = "";
                    if (stepperItem.getValue(mTestUIDriver).getType() == StepperValueType.VT_STRING)
                        value = (String) stepperItem.getValue(mTestUIDriver).getValue();
                    view = createDateTimeView(mInflater, stepperItem.getTitle(), value, hint, true);
                }
                break;
            case VS_TIME:
                {
                    String value = "";
                    if (stepperItem.getValue(mTestUIDriver).getType() == StepperValueType.VT_STRING)
                        value = (String) stepperItem.getValue(mTestUIDriver).getValue();
                    view = createDateTimeView(mInflater, stepperItem.getTitle(), value, hint, false);
                }
                break;
        }
        if (stepperItem.getTestFieldType() != null) {
            switch (stepperItem.getTestFieldType()) {
                case REJECTTEST:
                    setOnClickRejectTest(view);
                    break;
            }
        }
        return view;
    }

    private void updateView(StepperItem stepperItem) {
        ViewData viewData = findView(stepperItem);
        if(viewData == null) {
            throw new NullPointerException("View not found!");
        }

        switch(stepperItem.getDataType()) {
            case VS_BARCODE:
            case VS_STRING:
            case VS_INTEGER:
            case VS_COMBO:
            case VS_DATE:
            case VS_TIME:
                {
                    IStepperValue stepperValue = stepperItem.getValue(mTestUIDriver);
                    if(stepperValue.getType() == StepperValueType.VT_STRING)
                        updateFieldValueText(viewData.view, (String)stepperValue.getValue(), viewData.stepNumber);
                }
                break;
            case VS_RADIO:
                {
                    IStepperValue stepperValue = stepperItem.getValue(mTestUIDriver);
                    if(stepperValue.getType() == StepperValueType.VT_ARRAY) {
                        RadioGroup radioGroup = viewData.view.findViewById(R.id.radio_group);
                        radioGroup.check((Integer)stepperValue.getValue());
                    }
                }
                break;
        }
        if (stepperItem.getTestFieldType() != null) {
            switch (stepperItem.getTestFieldType()) {
                case TESTSELECTION:
                    setTestPanels();
                    break;
            }
        }
    }

    public ViewData findView(StepperItem stepperItem) {
        for (StepModel stepModel : mStepModels) {
            ArrayList<StepModel.ChildFields> childFields = stepModel.getChildFields();
            if (childFields.size() == 0) { // Mandatory fields
                EpocTestFieldType fieldType = stepModel.getEpocTestFieldType();
                if(fieldType == stepperItem.getTestFieldType())
                    return new ViewData(stepModel.getViewGroup(), stepModel.getStepNumber());
            } else { // Group fields
                for (StepModel.ChildFields field : childFields) {
                    EpocTestFieldType fieldType = field.getEpocTestFieldType();
                    if (fieldType == stepperItem.getTestFieldType())
                        return new ViewData(field.getView(), -1);
                }
            }
        }
        return null;
    }

    private ViewGroup createStepContainerView() {
        LinearLayout parent = new LinearLayout(mContext);
        parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        parent.setOrientation(LinearLayout.VERTICAL);
        return parent;
    }

    /**
     * This method resets the fields of the views inside a step. It takes into account the fields of
     * the optional, compliance and custom fields. It is used when a step is created, or when one of
     * the field header is clicked.
     */
    private void resetAllSubFieldsOfAStep() {
        int stepNumber = mVerticalStepperInputLayout.getActiveStepNumber();
        for (StepModel stepModel : mStepModels) {

            int step = stepModel.getStepNumber();
            if (step == stepNumber) {
                HashMap<String, View> stepCustomSubViews = stepModel.getCustomChildFieldValues();
                if (stepCustomSubViews.size() != 0) {
                    for (View view : stepCustomSubViews.values()) {
                        setViewFields(view, stepModel.isEditable());
                    }
                    break;
                } else {
                    ArrayList<StepModel.ChildFields> childFields = stepModel.getChildFields(); // For the optional groups
                    for (StepModel.ChildFields field : childFields) {
                        View view = field.getView();
                        setViewFields(view, field.isEditable());
                    }
                    break;
                }
            }
        }

        // Special case for the Test Panels
        setTestPanels(); // TODO: Need a check to run only when the test panel step is opened. Currently it is called multiple times.
//        setMockTestPanels(); // Dev panel
    }


    /**
     * This method initializes the fields of a given view, by setting up their header values, text view
     * edit text values, the radio groups and the combo boxes. It also devices which value to make
     * visible and which ones to invisible.
     *
     * @param view the view from which the fields are extracted.
     */
    private void setViewFields(View view, boolean isEditable) {
        TextView tvTitleView = (TextView) view.findViewById(R.id.field_title);
        TextView tvFieldValueText = (TextView) view.findViewById(R.id.text_view_field_value_text);
        TextView tvFieldValueRadio = (TextView) view.findViewById(R.id.text_view_field_value_radio);
        TextView tvFieldValueCombo = (TextView) view.findViewById(R.id.text_view_field_value_combo);
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.act_field_value);
        EditText etValueView = (EditText) view.findViewById(R.id.field_value);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        ImageView imageView = (ImageView) view.findViewById(R.id.combobox_dropdown_btn);

        if (tvTitleView != null) {
            titleRestingState(tvTitleView);
            if (isEditable) {
                tvTitleView.setEnabled(true);
                tvTitleView.setClickable(true);
            } else {
                tvTitleView.setEnabled(false);
                tvTitleView.setClickable(false);
            }
        }
        if (etValueView != null) etValueView.setVisibility(View.INVISIBLE);
        if (radioGroup != null) radioGroup.setVisibility(View.GONE);
        if (tvFieldValueText != null) {
            if (etValueView != null) {
                Log.d("HOST4", "EditView text is: " + etValueView.getText());
                tvFieldValueText.setText(etValueView.getText());
            } else if (radioGroup != null) {
                int value = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(value);
                tvFieldValueText.setText((radioButton != null ? radioButton.getText().toString() : ""));
            }
            tvFieldValueText.setVisibility(View.VISIBLE);
        }
        if (tvFieldValueRadio != null && radioGroup != null) {
            int value = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) view.findViewById(value);
            String text = tvFieldValueRadio.getText().toString();
            if (radioButton == null && text.equals(mContext.getString(R.string.msg_card_needed))) { // Special case for the Test Selection
                tvFieldValueRadio.setText(text);
            } else {
                tvFieldValueRadio.setText((radioButton != null ? radioButton.getText().toString() : "")); // For all other radio groups
            }
            tvFieldValueRadio.setVisibility(View.VISIBLE);
        }
        if (tvFieldValueCombo != null) {
            if (autoCompleteTextView != null) {
                autoCompleteTextView.setVisibility(View.GONE);
                tvFieldValueCombo.setText(autoCompleteTextView.getText());
                tvFieldValueCombo.setVisibility(View.VISIBLE);
            }
        }
        if (imageView != null) {
            imageView.setVisibility(View.GONE);
        }
    }

    private View createDateTimeView(LayoutInflater inflater, final String title, String value, String hint, final boolean date) {
        if (value == null) {
            value = "";
        }
        final View fieldView = inflater.inflate(R.layout.field_group_text, null, false);
        final TextView tvTitleView = (TextView) fieldView.findViewById(R.id.field_title);
        final TextView tvFieldValue = (TextView) fieldView.findViewById(R.id.text_view_field_value_text);
        final EditText etValueView = (EditText) fieldView.findViewById(R.id.field_value);
        etValueView.setText(value);
        tvFieldValue.setText((value.isEmpty() ? hint : value));
        tvTitleView.setText(title);

        // TODO: Investigate why both TextView and EditView are needed?
        tvFieldValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetAllSubFieldsOfAStep();
                if (date) {
                    DatePickerFragment datePickerFragment = new DatePickerFragment();
                    datePickerFragment.setParent(VerticalStepper.this);
                    datePickerFragment.setView(fieldView);
                    TextView tv = (TextView) view;
                    String formattedDateString = tv.getText().toString();
                    if (!formattedDateString.isEmpty()) {
                        Bundle args = new Bundle();
                        args.putString(CU.EPOC_DATE_STRING_KEY, formattedDateString);
                        datePickerFragment.setArguments(args);
                    }
                    datePickerFragment.show(mFragmentManager, "datePicker");
                    mActiveDialogFragment = datePickerFragment;
                } else {
                    TimePickerFragment timePickerFragment = new TimePickerFragment();
                    timePickerFragment.setParent(VerticalStepper.this);
                    timePickerFragment.setView(fieldView);
                    TextView tv = (TextView) view;
                    String formattedTimeString = tv.getText().toString();
                    if (!formattedTimeString.isEmpty()) {
                        Bundle args = new Bundle();
                        args.putString(CU.EPOC_TIME_STRING_KEY, formattedTimeString);
                        timePickerFragment.setArguments(args);
                    }
                    timePickerFragment.show(mFragmentManager, "timePicker");
                    mActiveDialogFragment = timePickerFragment;
                }
            }
        });

        return fieldView;
    }

    private View createTextFieldView(LayoutInflater inflater, final String title, final String value, String hint, boolean isNumeric, boolean showBarcode, String errorMessage) {
        View fieldView = inflater.inflate(R.layout.field_group_text, null, false);
        final TextView tvTitleView = (TextView) fieldView.findViewById(R.id.field_title);
        final TextView tvErrorMsg = (TextView) fieldView.findViewById(R.id.tv_error_message);
        final TextView tvFieldValue = (TextView) fieldView.findViewById(R.id.text_view_field_value_text);
        final EditText etValueView = (EditText) fieldView.findViewById(R.id.field_value);
        ImageView ivScannerIcon = (ImageView) fieldView.findViewById(R.id.image_view_barcode);
        tvErrorMsg.setText(errorMessage);

        etValueView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                /*
                 * Forces the edit text to scroll up to the top of the stepper so that the confirm button can be visible.
                 * Only applies to MANDATORY items because the confirm button is directly below the edittext control and
                 * can be made visible. For non mandatory items the confirm button would be at the bottom of all the
                 * controls and wouldn't be visible anyway.
                 *
                 * Also automatically request focus if the vertical stepper is expanded and the edittext control gains focus.
                 */
                if (hasFocus) {
                    final int step = mVerticalStepperInputLayout.getActiveStepNumber();
                    StepModel stepModel = mStepModels.get(step);
                    boolean isMandatory = stepModel.isMandatory();
                    Activity activity = (Activity) mContext;
                    if(hasFocus && isMandatory && ((PatientTestActivity)activity).isVerticalStepperExpanded()) {
                        InputMethodManager imm = (InputMethodManager) mContext.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                        IMMResult result = new IMMResult();
                        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT, result);
                        result.setSoftKeyboardListener(new IMMResult.SoftKeyboardResultListener() {
                            @Override
                            public void onSoftKeyboardResult(int result) {
                                if (result == InputMethodManager.RESULT_SHOWN) {
                                    //mVerticalStepperInputLayout.makeConfirmVisible(step);
                                }
                            }
                        });
                    }
                } else {
                    hideSoftKeyboard();
                }
            }
        });

        tvTitleView.setText(title);
        if (isNumeric) { // For the numeric fields, show numeric keyboard with support for the decimal input
            etValueView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        etValueView.setHint(hint);
        etValueView.setText(value);
        tvFieldValue.setText(value);

        tvFieldValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetAllSubFieldsOfAStep();
                int step = mVerticalStepperInputLayout.getActiveStepNumber();
                StepModel stepModel = mStepModels.get(step);
                boolean isMandatory = stepModel.isMandatory();
                if (!isMandatory) {
                    int isVisible = tvFieldValue.getVisibility();
                    if (isVisible == View.VISIBLE) {
                        tvFieldValue.setVisibility(View.INVISIBLE);
                        etValueView.setVisibility(View.VISIBLE);
                        titleFloatingState(tvTitleView);
                    } else {
                        tvFieldValue.setVisibility(View.VISIBLE);
                        etValueView.setVisibility(View.INVISIBLE);
                        titleRestingState(tvTitleView);
                    }
                }
            }
        });

        if (showBarcode)
            ivScannerIcon.setVisibility(View.VISIBLE);
        else
            ivScannerIcon.setVisibility(View.GONE);

        return fieldView;
    }

    private View createRadioGroup(LayoutInflater inflater, final String title, String[] valueArray, final boolean isVertical, Integer checked) {
        View view = inflater.inflate(R.layout.field_group_radio, null, false);
        final TextView tvTitleView = (TextView) view.findViewById(R.id.field_title);
        final TextView tvFieldValue = (TextView) view.findViewById(R.id.text_view_field_value_radio);
        tvTitleView.setText(title);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        if (isVertical) {
            radioGroup.setOrientation(LinearLayout.VERTICAL);
        } else {
            radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        }

        //create radio buttons
        for (int i = 0; i < valueArray.length; i++) {
            RadioButton radioButton = new RadioButton(mContext);
            radioButton.setText(valueArray[i]);
            radioButton.setId(i);
            radioButton.setButtonTintList(sColorStateListRadioButton);
            radioGroup.addView(radioButton);
        }

        // The null value indicates that no value is selected in a radio group
        if (checked != null) {
            if (checked == -2) { // Needed for the TestSelection at the time of its initialization
                tvFieldValue.setText(mContext.getString(R.string.msg_card_needed));
                radioGroup.setVisibility(View.GONE);
            } else {
                radioGroup.check(checked);
            }
        }

        tvFieldValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetAllSubFieldsOfAStep();
                int isVisible = tvFieldValue.getVisibility();
                int step = mVerticalStepperInputLayout.getActiveStepNumber();
                boolean isMandatory = mStepModels.get(step).isMandatory();
                if (!isMandatory) {
                    if (isVisible == View.VISIBLE) {
                        tvFieldValue.setVisibility(View.GONE);
                        radioGroup.setVisibility(View.VISIBLE);
                        titleFloatingState(tvTitleView);
                    } else {
                        tvFieldValue.setVisibility(View.VISIBLE);
                        radioGroup.setVisibility(View.GONE);
                        titleRestingState(tvTitleView);
                    }
                }

                // Handle the click on the TestSelection field. This is done in a separate method instead of here because that method is also called externally
                if (mStepModels.get(step).getEpocTestFieldGroupType() == EpocTestFieldGroupType.TESTSELECTION_OPTIONAL) {
                    setTestPanels();
                }
            }
        });

        return view;
    }

    private View createComboBox(LayoutInflater inflater, final String title, String hint, String[] suggestions, final String value) {
        View view = inflater.inflate(R.layout.field_group_combobox, null, false);
        final TextView tvTitleView = (TextView) view.findViewById(R.id.field_title);
        final TextView tvFieldValue = (TextView) view.findViewById(R.id.text_view_field_value_combo);
        final AutoCompleteTextView autocompleteView = (AutoCompleteTextView) view.findViewById(R.id.act_field_value);
        final ImageButton imageButton = (ImageButton) view.findViewById(R.id.combobox_dropdown_btn);
        autocompleteView.setHint(hint);
        autocompleteView.setText(value);
        autocompleteView.setThreshold(1); // num of char before drop-suggestion is shown. make it large so no suggestion drop down.
        int layoutItemId = android.R.layout.simple_dropdown_item_1line;
        List<String> suggestionList = Arrays.asList(suggestions);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, layoutItemId, suggestionList);
        autocompleteView.setAdapter(adapter);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (!autocompleteView.getText().toString().equals("")) {
                    adapter.getFilter().filter(null); // If the field is empty, don't do this, otherwise dropdown won't show.
                }
                autocompleteView.showDropDown();
            }
        });

        Log.d("", "Value is: " + value);
        tvTitleView.setText(title);
        tvFieldValue.setText(value);
        tvFieldValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetAllSubFieldsOfAStep();
                int isVisible = tvFieldValue.getVisibility();
                if (isVisible == View.VISIBLE) {
                    tvFieldValue.setVisibility(View.GONE);
                    autocompleteView.setVisibility(View.VISIBLE);
                    imageButton.setVisibility(View.VISIBLE);
                    titleFloatingState(tvTitleView);
                } else {
                    tvFieldValue.setVisibility(View.VISIBLE);
                    autocompleteView.setVisibility(View.GONE);
                    imageButton.setVisibility(View.GONE);
                    titleRestingState(tvTitleView);
                }
            }
        });

        return view;
    }

    /**
     * The llContent of the layout of the corresponding step must be generated here. The system will
     * automatically call this method for every step
     *
     * @param stepNumber the number of the step
     * @return The view that will be automatically added as the llContent of the step
     */
    @Override
    public View createStepContentView(int stepNumber) {
        return mStepModels.get(stepNumber).getViewGroup();
    }

    private boolean validate(View view, IStepperItem item) {
        switch(item.getDataType()) {
            case VS_COMBO:
                return item.validateAndSave(mTestUIDriver, getStringValueFromView(view));
            case VS_RADIO:
                return item.validateAndSave(mTestUIDriver, getRadioValueFromView(view));
            case VS_TIME:
            case VS_DATE:
            case VS_INTEGER:
            case VS_STRING:
            case VS_BARCODE:
                return item.validateAndSave(mTestUIDriver, getStringValueFromView(view));
        }
        return false;
    }

    private String getStringValueFromView(View view) {
        try {
            AutoCompleteTextView autocompleteView = view.findViewById(R.id.act_field_value);
            if( autocompleteView != null)
                return autocompleteView.getText().toString();
            else {
                EditText editText = (EditText) view.findViewById(R.id.field_value);
                if (editText != null) {
                    return editText.getText().toString();
                } else {
                    RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
                    if (radioGroup != null) {
                        int value = radioGroup.getCheckedRadioButtonId();
                        RadioButton radioButton = (RadioButton) view.findViewById(value);
                        return ((radioButton != null ? radioButton.getText().toString() : ""));

                    } else {
                        TextView fieldValueCombo = (TextView) view.findViewById(R.id.text_view_field_value_combo);
                        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.act_field_value);
                        if (fieldValueCombo != null && autoCompleteTextView != null) {
                            return autoCompleteTextView.getText().toString();
                        }
                    }
                    return "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private Integer getRadioValueFromView(View view) {
        RadioGroup radioGroup;
        radioGroup = view.findViewById(R.id.radio_group);
        Integer radioButtonID = radioGroup.getCheckedRadioButtonId();
        View radioButton = radioGroup.findViewById(radioButtonID);
        if (radioButton != null)
            return radioGroup.indexOfChild(radioButton);
        else
            return -1;
    }

    /**
     * Once the Confirm button is clicked, validate and save the data of this step
     *
     * @param stepNumber the number of the step
     */
    @Override
    public boolean onConfirmClicked(int stepNumber) {
        StepModel stepModel = mStepModels.get(stepNumber);
        boolean isMandatory = stepModel.isMandatory();
        boolean returnValue;
        int errors = 0;

        if (stepModel.getChildFields().size() == 0) { // stepmodel has only one control
            EnumSet<EpocTestFieldType> epocTestFieldTypes = EnumSet.noneOf(EpocTestFieldType.class);
            View view = mStepModels.get(stepNumber).getViewGroup();
            clearMandatoryErrorMessages(view);
            EpocTestFieldType fieldType = stepModel.getEpocTestFieldType();
            returnValue = validate(view, mStepModels.get(stepNumber).getStepperItem());
            stepModel.setCompleted(returnValue);
            if (!returnValue) {
                showErrorMessage(view, ((StepperItem)stepModel.getStepperItem()).getErrorMessage());
                errors++;

                if (isMandatory)
                    markMandatoryStepIncomplete();
            } else {
                hideErrorMessage(view);
                updateSubtitle(getStringValueFromView(view), stepNumber);

                // todo move this somewhere nicer
                switch (fieldType) {
                    case PATIENTID:
                        String patientId = (String)stepModel.getStepperItem().getValue(mTestUIDriver).getValue();
                        mPatientTestPresenter.updateActivityTitle(mContext.getString(R.string.patient) + " " + patientId);
                        break;
                }

                epocTestFieldTypes.add(fieldType);
                if (isMandatory) {
                    markMandatoryStepCompleted();
                    if (mTestUIDriver.isCalculationDone())
                        mPatientTestPresenter.mandatoryStepsCompleted();
                }
                else
                    markCompleted();
            }

            mTestUIDriver.saveTestData(epocTestFieldTypes);
        } else { // Group fields, includes optional, compliance and custom fields
            clearGroupErrorMessages(stepModel);

            EnumSet<EpocTestFieldType> epocTestFieldTypes = EnumSet.noneOf(EpocTestFieldType.class);
            // Factory defined fields
            ArrayList<StepModel.ChildFields> childFields = stepModel.getChildFields();
            for (StepModel.ChildFields field : childFields) {
                View view = field.getView();
                EpocTestFieldType fieldType = field.getEpocTestFieldType();
                String customFieldName = field.getCustomFieldName();
                returnValue = validate(view, field.getStepperItem());
                stepModel.setCompleted(returnValue);
                if (returnValue) {
                    epocTestFieldTypes.add(fieldType);
                    hideErrorMessage(view);
                    markCompleted();
                } else {
                    errors++;
                    showErrorMessage(view, ((StepperItem)field.getStepperItem()).getErrorMessage());
                }
            }

            mTestUIDriver.saveTestData(epocTestFieldTypes);
        }

        returnValue = (errors == 0);

        return returnValue; // It won't go to the next step if there is even one error in the group
    }

    /**
     * This method will be called when the user press the confirmation button
     */
    @Override
    public void sendData() {

    }

    /**
     * Utility method to hide keyboard
     */
    public void hideSoftKeyboard() {
        Activity activity = (Activity) mContext;
        View v = activity.getWindow().getDecorView().getRootView();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * Print out the llContent of workflow in a readable format for debugging purpose
     *
     * @param context  the context
     * @param workflow the workflow
     */
    private static void printWorkflow(Context context, WorkFlow workflow) {
        String[] testGroupTypeStrings = context.getResources().getStringArray(R.array.EpocTestFieldGroupType);
        String[] testFieldTypeStrings = context.getResources().getStringArray(R.array.EpocTestFieldType);

        RealmList<WorkflowItem> steps = workflow.getWorkflowItems();
        for (int i = 0; i < steps.size(); i++) {
            Log.d(TAG, "step=" + i);
            WorkflowItem step = steps.get(i);
            EpocTestFieldGroupType groupType = step.getFieldGroupType();
            Log.d(TAG, "groupType=" + groupType + " name=" + testGroupTypeStrings[groupType.ordinal()]);
            RealmList<TestInputField> inputFields = step.getFieldList();
            for (int j = 0; j < inputFields.size(); j++) {
                TestInputField field = inputFields.get(j);
                EpocTestFieldType fieldType = field.getFieldType();
//                Log.d(TAG, "fieldType="+fieldType+" name="+testFieldTypeStrings[fieldType.ordinal()]);
            }
        }
    }

    @Override
    public void markCompleted() {
        mVerticalStepperInputLayout.setActiveStepAsCompleted();
        mStepModels.get(mVerticalStepperInputLayout.getActiveStepNumber()).setCompleted(true);
    }

    @Override
    public void markUncompleted(String value) {
        mVerticalStepperInputLayout.setActiveStepAsUncompleted(value);
        mStepModels.get(mVerticalStepperInputLayout.getActiveStepNumber()).setCompleted(false);
    }

    @Override
    public void markMandatoryStepEditable() {
        mVerticalStepperInputLayout.setActiveMandatoryStepEditable();
        mStepModels.get(mVerticalStepperInputLayout.getActiveStepNumber()).setCompleted(false);
    }

    @Override
    public void markMandatoryStepCompleted() {
        int stepNumber = mVerticalStepperInputLayout.getActiveStepNumber();
        if (mStepModels.get(stepNumber).isMandatory()) {
            mVerticalStepperInputLayout.setActiveMandatoryStepCompleted();
            mStepModels.get(stepNumber).setCompleted(true);
        }
    }

    @Override
    public void markMandatoryStepIncomplete() {
        int stepNumber = mVerticalStepperInputLayout.getActiveStepNumber();
        if (mStepModels.get(stepNumber).isMandatory()) {
            mVerticalStepperInputLayout.setActiveMandatoryStepIncomplete();
            mStepModels.get(stepNumber).setCompleted(false);
        }
    }

    @Override
    public void enableMandatoryStep(int stepNumber, boolean isCompleted, boolean expand) {
        StepModel stepModel = mStepModels.get(stepNumber);
        if (stepModel != null && stepModel.isMandatory()) {
            mVerticalStepperInputLayout.enableStep(stepNumber, isCompleted, expand);
        }
    }

    public void disableMandatorySteps() {
        for (StepModel step : mStepModels) {
            int stepNumber = step.getStepNumber();
            disableStep(stepNumber);
        }
    }

    public void enableMandatorySteps() {
        for (StepModel step : mStepModels) {
            int stepNumber = step.getStepNumber();
            boolean isMandatory = step.isMandatory();
            boolean isCompleted = step.isCompleted();
            if (isMandatory) {
                enableMandatoryStep(stepNumber, isCompleted, false);
            } else {
                disableStep(stepNumber);
            }
        }
    }

    @Override
    public void disableStep(Integer stepNumber) {
        mVerticalStepperInputLayout.disableStep(stepNumber);
    }

    @Override
    public void enableStep(Integer stepNumber) {
        mVerticalStepperInputLayout.enableStep(stepNumber, true, false);
    }

    public void hideStep(Integer stepNumber) {
        mVerticalStepperInputLayout.hideStep(stepNumber);
    }

    @Override
    public void enableOrDisableOptionalSteps() {
        boolean value = areAllMandatoryStepsCompleted();
        if (value != mOptionalsEnabled) {
            mOptionalsEnabled = value;
            for (StepModel step : mStepModels) {
                // Deal with the optional steps except the Documents Results step since it is controlled via separate events.
//                if (step.isOptional()) { // DEV
                if (step.isOptional() && step.getEpocTestFieldGroupType() != EpocTestFieldGroupType.DOCUMENTRESULTS_OPTIONAL) {
                    int stepNumber = step.getStepNumber();
                    if (value)
                        mVerticalStepperInputLayout.enableStep(stepNumber, true, false);
                    else {
                        mVerticalStepperInputLayout.disableStep(stepNumber);
                    }
                }
            }
        }
    }

    @Override
    public void updateOptionalSteps() {
        enableOrDisableOptionalSteps();
    }

    /**
     * Check if all the mandatory values are completed. If yes, return true, otherwise return false.
     *
     * @return the return value
     */
    public boolean areAllMandatoryStepsCompleted() {
        boolean value = true;
        for (StepModel step : mStepModels) {

            String title = step.getTitle();
            boolean isMandatory = step.isMandatory();
            boolean isCompleted = step.isCompleted();
            Log.d("HOST4", "Step " + title + " is " + isMandatory + " and completed is: " + isCompleted);

            if (isMandatory && !isCompleted) {
                value = false;
                break;
            }
        }

        return value;
    }

    /**
     * This method hides all the visible error messages from a group step.
     *
     * @param stepModel the Step which's fields are being cleaned
     */
    private void clearGroupErrorMessages(StepModel stepModel) {

//        HashMap<EpocTestFieldType, View> map = stepModel.getChildFieldValues();
//        for (EpocTestFieldType fieldType : map.keySet()) {
//            View view = map.get(fieldType);
//            TextView tvErrorMessage = (TextView) view.findViewById(R.id.tv_error_message);
//            if (tvErrorMessage != null && tvErrorMessage.getVisibility() == View.VISIBLE) {
//                tvErrorMessage.setVisibility(View.GONE);
//            }
//        }

        ArrayList<StepModel.ChildFields> childFields = stepModel.getChildFields();
        for (StepModel.ChildFields field : childFields) {
            View view = field.getView();
            TextView tvErrorMessage = (TextView) view.findViewById(R.id.tv_error_message);
            if (tvErrorMessage != null && tvErrorMessage.getVisibility() == View.VISIBLE) {
                tvErrorMessage.setVisibility(View.GONE);
            }
        }
    }

    /**
     * This method hides all the visible error messages from a mandatory step.
     *
     * @param view the Step which's fields are being cleaned
     */
    private void clearMandatoryErrorMessages(View view) {
        TextView tvErrorMessage = (TextView) view.findViewById(R.id.tv_error_message);
        if (tvErrorMessage != null && tvErrorMessage.getVisibility() == View.VISIBLE) {
            tvErrorMessage.setVisibility(View.GONE);
        }
    }

    /**
     * Display the error messages in a given step. Also return true and false accordingly
     *
     * @param error the HashMap containing the Views which have error messages.
     */
    @Override
    public void showAllErrorMessages(HashMap<EpocTestFieldType, ErrorObject> error) {
        if (error.size() > 0) { // Show error messages
            for (EpocTestFieldType fieldType : error.keySet()) {
                ErrorObject errorObject = error.get(fieldType);
                View view = errorObject.getView();
                String errorMessage = errorObject.getErrorMessage();
                TextView textView = (TextView) view.findViewById(R.id.tv_error_message);
                textView.setText(errorMessage);
                textView.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Show error message for a given view.
     *
     * @param view  the View
     * @param error the error message to display
     */
    @Override
    public void showErrorMessage(View view, String error) {
        TextView textView = (TextView) view.findViewById(R.id.tv_error_message);
        textView.setText(error);
        textView.setVisibility(View.VISIBLE);
    }

    /**
     * Hide error message if it is showing.
     *
     * @param view the View where the error message is being hidden
     */
    @Override
    public void hideErrorMessage(View view) {
        TextView textView = (TextView) view.findViewById(R.id.tv_error_message);
        textView.setVisibility(View.GONE);
    }

    @Override
    public void updateSubtitle(String subTitle, int stepNumber) {
        mVerticalStepperInputLayout.updateSubtitle(subTitle, stepNumber);
    }

    @Override
    public void onStepOpening(int stepNumber) {
        resetAllSubFieldsOfAStep();
    }

    @Override
    public void setDateTime(String text, View view) {
        TextView tvFieldValueText = (TextView) view.findViewById(R.id.text_view_field_value_text);
        EditText etValueView = (EditText) view.findViewById(R.id.field_value);
        etValueView.setText(text);
        tvFieldValueText.setText(text);
    }

    /**
     * This method gets the custom test panels from the multi select dialog and sets it in the
     * TestUIDriver. In this case, we also save it at the same time though user has not pressed
     * Confirm yet. This is because if the user opens the custom panels dialog again, he should not
     * see a new list with all the Analytes selected.
     *
     * @param analyteNames the EnumSet of the analyte names
     */
    void setCustomTestInclusions(EnumSet<AnalyteName> analyteNames) {
        mTestUIDriver.setCustomTestInclusions(analyteNames);
        EnumSet<EpocTestFieldType> epocTestFieldTypes = EnumSet.noneOf(EpocTestFieldType.class);
        epocTestFieldTypes.add(EpocTestFieldType.TESTSELECTION);
        mTestUIDriver.saveTestData(epocTestFieldTypes);
    }

//    public void setMockTestPanels() {
//        int value = EpocTestFieldGroupType.TESTSELECTION_OPTIONAL.value;
//        for (StepModel stepModel : mStepModels) {
//            if (stepModel.getParentFieldValue() == value) {
//                View view = mStepModels.get(stepModel.getStepNumber()).getViewGroup();
//                RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
//                TextView textView = (TextView) view.findViewById(R.id.text_view_field_value_radio);
//                TextView textViewTitle = (TextView) view.findViewById(R.id.field_title);
//                radioGroup.removeAllViews();
//                ArrayList<EpocTestPanelType> testPanels = new ArrayList<>();
//                EpocTestPanelType selectedPanel = mTestUIDriver.getSelectedPanel();
//                boolean customPanelsOnly = false;
//
//                // Show only the custom test panel if that's what the UI driver wants
//                if (mTestUIDriver.showCustomTestSelectionOnly()) {
//                    testPanels.add(EpocTestPanelType.CUSTOM);
//                    customPanelsOnly = true;
//                } else {
//                    testPanels = mTestUIDriver.getTestPanelsMock();
//                }
//
//                if (testPanels != null) {
//                    final boolean customPanel = customPanelsOnly;
//                    textView.setVisibility(View.GONE);
//                    titleFloatingState(textViewTitle);
//                    SparseIntArray array = StringResourceValues.setTestPanels();
//
//                    for (EpocTestPanelType panelType : testPanels) {
//                        Integer panel = array.get(panelType.value);
//                        String panelName = "Resource value missing";
//                        try {
//                            panelName = mContext.getString(panel);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        } finally {
//                            RadioButton radioButton = new RadioButton(mContext);
//                            // For the custom test selections radio item
//                            if (panelType == EpocTestPanelType.CUSTOM) {
//                                radioButton.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        ArrayList<AnalyteOption> analyteOptions = mTestUIDriver.getCustomSelectedTests();
//                                        CustomTestSelectionDialog dialog = CustomTestSelectionDialog.newInstance(analyteOptions, customPanel);
//                                        dialog.setParent(VerticalStepper.this);
//                                        dialog.show(mFragmentManager, "");
//                                    }
//                                });
//                            }
//
//                            radioButton.setText(panelName);
//                            if (panelType == selectedPanel) radioButton.setChecked(true);
//                            radioGroup.addView(radioButton);
//                        }
//                    }
//                    radioGroup.setVisibility(View.VISIBLE);
//                    break;
//                } else {
//                    Log.d("HOST4", "No test panels to display");
//                    radioGroup.setVisibility(View.GONE);
//                    textView.setText(mContext.getString(R.string.msg_card_needed));
//                    textView.setVisibility(View.VISIBLE);
//                    titleRestingState(textViewTitle);
//                }
//            }
//        }
//    }

    /**
     * //todo: this is called multiple times
     * Set the test panels dynamically
     */
    public void setTestPanels() {
        for (StepModel stepModel : mStepModels) {
            if (stepModel.getEpocTestFieldGroupType() == EpocTestFieldGroupType.TESTSELECTION_OPTIONAL) {
                View view = mStepModels.get(stepModel.getStepNumber()).getViewGroup();
                RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
                TextView textView = (TextView) view.findViewById(R.id.text_view_field_value_radio);
                TextView textViewTitle = (TextView) view.findViewById(R.id.field_title);
                if (radioGroup != null)
                    radioGroup.removeAllViews();
                ArrayList<EpocTestPanelType> testPanels = new ArrayList<>();
                EpocTestPanelType selectedPanel = mTestUIDriver.getSelectedPanel();
                boolean isCalculationDone = false;

                // Show only the custom test panel if that's what the UI driver wants
                if (mTestUIDriver.showCustomTestSelectionOnly()) {
                    testPanels.add(EpocTestPanelType.CUSTOM);
                    selectedPanel = EpocTestPanelType.CUSTOM;
                    isCalculationDone = true;
                } else {
                    testPanels = mTestUIDriver.getTestPanels();
                }

                if (testPanels != null) {
                    final boolean calculationDone = isCalculationDone;
                    textView.setVisibility(View.GONE);
                    titleFloatingState(textViewTitle);
                    SparseIntArray array = StringResourceValues.setTestPanels();
                    for (EpocTestPanelType panelType : testPanels) {
                        Integer panel = array.get(panelType.value);
                        String panelName = "Resource value missing";
                        try {
                            panelName = mContext.getString(panel);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            RadioButton radioButton = new RadioButton(mContext);
                            radioButton.setId(radioButton.generateViewId());
                            // For the custom test selections radio item
                            if (panelType == EpocTestPanelType.CUSTOM) {
                                radioButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ArrayList<AnalyteOption> analyteOptions = mTestUIDriver.getCustomSelectedTests();
                                        CustomTestSelectionDialog dialog = CustomTestSelectionDialog.newInstance(analyteOptions, calculationDone);
                                        dialog.setParent(VerticalStepper.this);
                                        dialog.show(mFragmentManager, "");
                                        mActiveDialogFragment = dialog;
                                    }
                                });
                            }

                            radioButton.setText(panelName);
                            if (panelType == selectedPanel) radioButton.setChecked(true);
                            if (radioGroup != null) radioGroup.addView(radioButton);
                        }
                    }
                    if (radioGroup != null) radioGroup.setVisibility(View.VISIBLE);
                    break;
                } else {
                    Log.d("HOST4", "No test panels to display");
                    if (radioGroup != null) radioGroup.setVisibility(View.GONE);
                    if (textView != null) textView.setText(mContext.getString(R.string.msg_card_needed));
                    if (textView != null) textView.setVisibility(View.VISIBLE);
                    titleRestingState(textViewTitle);
                }
            }
        }
    }

    @Override
    public void disableDocumentResults() {
        hideStep(getStepNumber(EpocTestFieldGroupType.DOCUMENTRESULTS_OPTIONAL));
    }

    @Override
    public void enableDocumentResults() {
        enableStep(getStepNumber(EpocTestFieldGroupType.DOCUMENTRESULTS_OPTIONAL));
    }

    @Override
    public void openDocumentResults() {
        final Integer stepNumber = getStepNumber(EpocTestFieldGroupType.DOCUMENTRESULTS_OPTIONAL);
        if (stepNumber != null) {
            mVerticalStepperInputLayout.setAnimationCallback(new StepperAnimationCallback() {
                @Override
                public void onSlideDownAnimationStart() {

                }

                @Override
                public void onSlideDownAnimationEnd() {
                    mVerticalStepperInputLayout.makeConfirmVisible(stepNumber);
                    mVerticalStepperInputLayout.setAnimationCallback(null);
                }

                @Override
                public void onSlideUpAnimationStart() {

                }

                @Override
                public void onSlideUpAnimationEnd() {
                    mVerticalStepperInputLayout.setAnimationCallback(null);
                }
            });
            mVerticalStepperInputLayout.goToStep(stepNumber);
            mVerticalStepperInputLayout.scrollToHeader(stepNumber);
        }
    }

    private void titleRestingState(TextView tvTitleView) {
        tvTitleView.setTextColor(ContextCompat.getColor(mContext, R.color.colorAlereMediumGray));
        tvTitleView.setTextSize(13);
    }

    private void titleFloatingState(TextView tvTitleView) {
        tvTitleView.setTextColor(ContextCompat.getColor(mContext, R.color.primaryBlueNew));
        tvTitleView.setTextSize(11);
    }

    private Integer getStepNumber(EpocTestFieldGroupType epocTestFieldGroupType) {
        for (StepModel stepModel : mStepModels) {
            EpocTestFieldGroupType type = stepModel.getEpocTestFieldGroupType();
            if (type == epocTestFieldGroupType) {
                return stepModel.getStepNumber();
            }
        }
        return null;
    }

    private void setOnClickRejectTest(View view) {
        final RadioGroup group = (RadioGroup) view.findViewById(R.id.radio_group);
        RadioButton buttonYes = (RadioButton) group.getChildAt(1);
        buttonYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AlertDialog dialog = new AlertDialog.Builder(mContext)
                            .setMessage(mContext.getString(R.string.msg_rejecting_test))
                            .setPositiveButton(mContext.getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            })
                            .setNegativeButton(mContext.getString(R.string.no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    group.check(group.getChildAt(0).getId()); // Check button No
                                }
                            })
                            .create();

                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            onDialogDismissed(dialog);
                        }
                    });
                    final Window dialogWindow = dialog.getWindow();
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
                    lp.dimAmount = 0.2f;  // 20% dim
                    dialogWindow.setAttributes(lp);

                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.primaryBlueNew));
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.primaryBlueNew));
                    mActiveDialog = dialog;
                }
            }
        });
    }

    /**
     * This method re-reads the TestRecord, fetch values and updates the Stepper form.
     *
     * @param testRecord the TestRecord object
     */
    public void updatePatientData(TestRecord testRecord) {
        for(Map.Entry<EpocTestFieldGroupType,List<StepperItem>> entry : mStepperItemBuilder.getGroups().entrySet()) {
            List<StepperItem> vsItems = entry.getValue();
            for (StepperItem vsi : vsItems) {
                updateView(vsi);
            }
        }
    }

    private void updateFieldValueText(View view, String text, int stepNumber) {
        TextView tvFieldValueText = (TextView) view.findViewById(R.id.text_view_field_value_text);
        TextView tvFieldValueRadio = (TextView) view.findViewById(R.id.text_view_field_value_radio);
        TextView tvFieldValueCombo = (TextView) view.findViewById(R.id.text_view_field_value_combo);
        EditText etValueView = (EditText) view.findViewById(R.id.field_value);
        if (tvFieldValueText != null) tvFieldValueText.setText(text);
        if (tvFieldValueRadio != null) tvFieldValueRadio.setText(text);
        if (tvFieldValueCombo != null) tvFieldValueCombo.setText(text);
        if (etValueView != null) etValueView.setText(text);
        if (stepNumber != -1) {
            updateSubtitle(text, stepNumber);
        }
    }

    /**
     * This method checks if a step is editable. This is needed for the mandatory steps, so that we
     * don't expand them if they are not editable.
     *
     * @return the boolean value
     */
    @Override
    public boolean isEditable(int stepNumber) {
        for (StepModel stepModel : mStepModels) {
            int number = stepModel.getStepNumber();
            if (number == stepNumber) {
                return stepModel.isEditable();
            }
        }
        return false;
    }

    /**
     * This methods takes out all the fields in this list and marks them as uneditable for the stepper
     *
     * @param nonEditableFields The list of fields
     */
    @Override
    public void updateFieldsEditability(ArrayList<WorkflowField> nonEditableFields) {

        for (WorkflowField workflowField : nonEditableFields) {
            EpocTestFieldType fieldType = workflowField.getEpocTestFieldType();

            for (StepModel stepModel : mStepModels) {
                if (stepModel.getChildFields().size() == 0) { // stepper with one item
                    EpocTestFieldType fieldType1 = stepModel.getEpocTestFieldType();
                    if (fieldType1 == fieldType) {
                        View view = stepModel.getViewGroup();
                        stepModel.setEditable(false);
                        changeEditability(view, false);
                        if (stepModel.isMandatory())
                            mVerticalStepperInputLayout.markMandatoryStepAsUneditable(stepModel.getStepNumber());
                        else
                            changeEditability(view, false);
                    }
                } else { // Group fields
                    ArrayList<StepModel.ChildFields> childFields = stepModel.getChildFields();
                    for (StepModel.ChildFields childField : childFields) {
                        EpocTestFieldType fieldType2 = childField.getEpocTestFieldType();
                        if (fieldType2 == fieldType) {
                            View view = childField.getView();
                            childField.setEditable(false);
                            changeEditability(view, false);
                        }
                    }
                }
            }
        }
    }

    private void changeEditability(View view, boolean editability) {
        TextView tvFieldValue1 = (TextView) view.findViewById(R.id.text_view_field_value_text);
        TextView tvFieldValue2 = (TextView) view.findViewById(R.id.text_view_field_value_radio);
        TextView tvFieldValue3 = (TextView) view.findViewById(R.id.text_view_field_value_combo);
        if (tvFieldValue1 != null) {
            tvFieldValue1.setClickable(editability);
            tvFieldValue1.setEnabled(editability);
        }
        if (tvFieldValue2 != null) {
            tvFieldValue2.setClickable(editability);
            tvFieldValue2.setEnabled(editability);
        }
        if (tvFieldValue3 != null) {
            tvFieldValue3.setClickable(editability);
            tvFieldValue3.setEnabled(editability);
        }
    }

    @Override
    public void updateFieldsMakeAllEditable() {
        for (StepModel stepModel : mStepModels) {
            if (stepModel.getChildFields().size() == 0) { // Mandatory field
                View view = stepModel.getViewGroup();
                stepModel.setEditable(true);
                changeEditability(view, true);
                if (stepModel.isMandatory())
                    mVerticalStepperInputLayout.markMandatoryStepAsEditable(stepModel.getStepNumber());
                else
                    changeEditability(view,true);
            } else { // Group fields
                ArrayList<StepModel.ChildFields> childFields = stepModel.getChildFields();
                for (StepModel.ChildFields childField : childFields) {
                    View view = childField.getView();
                    childField.setEditable(true);
                    changeEditability(view, true);
                }
            }
        }
    }

    public void dismissAllActiveDialog() {
        if (mActiveDialogFragment != null) {
            mActiveDialogFragment.dismiss();
            mActiveDialogFragment = null;
        }
        if (mActiveDialog != null) {
            mActiveDialog.dismiss();
            mActiveDialog = null;
        }
    }

    public void onDialogDismissed(DialogFragment fragDialog) {
        mActiveDialogFragment = null;
    }
    public void onDialogDismissed(DialogInterface dialog) {
        mActiveDialog = null;
    }

//    private void testTP() {
//        ArrayList<AnalyteOption> analyteOptions = new ArrayList<AnalyteOption>();
//        int order = 0;
//        for (AnalyteName aName : AnalyteName.values()) {
//            order++;
//            AnalyteOption testOpt = new AnalyteOption();
//            testOpt.setAnalyteName(AnalyteName.convert(aName.value));
//            testOpt.setDisplayOrder(order);
//            testOpt.setOptionType(EnabledSelectedOptionType.EnabledSelected);
//            analyteOptions.add(testOpt);
//        }
//
//        CustomTestSelectionDialog dialog = CustomTestSelectionDialog.newInstance(analyteOptions, false);
//        dialog.setParent(VerticalStepper.this);
//        dialog.show(mFragmentManager, "");
//    }
}
