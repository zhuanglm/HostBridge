package com.epocal.epoctestprocedure.fragments.dataentry;

import android.view.View;
import android.view.ViewGroup;

import com.epocal.common.types.EpocTestFieldGroupType;
import com.epocal.common.types.EpocTestFieldType;
import com.epocal.epoctestprocedure.stepper.IStepperItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The model object for the Stepper's steps
 * <p>
 * Created by Zeeshan A Zakaria on 10/24/2017.
 */

class StepModel {

    private String mTitle;
    private boolean mMandatory;
    private boolean mEditable;
    private boolean mCompleted;
    private int mStepNumber;
    private int mFieldOrder;
    private ViewGroup mViewGroup;
    private EpocTestFieldGroupType mEpocTestFieldGroupType;
    private EpocTestFieldType mEpocTestFieldType;
    private HashMap<String, View> mCustomChildFieldValues; // Used for compliance and custom fields
    private ArrayList<ChildFields> mChildFields;
    private IStepperItem mStepperItem;

    StepModel(String title, boolean mandatory, boolean editable, EpocTestFieldGroupType testFieldGroupType, EpocTestFieldType fieldType, int stepNumber) {
        mTitle = title;
        mMandatory = mandatory;
        mEditable = editable;
        mEpocTestFieldGroupType = testFieldGroupType;
        mEpocTestFieldType = fieldType;
        mStepNumber = stepNumber;
        mCompleted = false;
        mCustomChildFieldValues = new HashMap<>();
        mChildFields = new ArrayList<>();
    }

    void setEditable(boolean editable) {
        mEditable = editable;
    }

    EpocTestFieldGroupType getEpocTestFieldGroupType() {
        return mEpocTestFieldGroupType;
    }

    void setEpocTestFieldGroupType(EpocTestFieldGroupType epocTestFieldGroupType) {
        mEpocTestFieldGroupType = epocTestFieldGroupType;
    }

    ViewGroup getViewGroup() {
        return mViewGroup;
    }

    void setViewGroup(ViewGroup viewGroup) {
        mViewGroup = viewGroup;
    }

    /**
     * Return the child field items which are sorted by their display order.
     *
     * @return the array list of child field objects
     */
    ArrayList<ChildFields> getChildFields() {
        return mChildFields;
    }

    void sortChildFields() {
        ArrayList<ChildFields> sortedList = new ArrayList<>();

        // Using insertion-sort algorithm because it is a very small list TODO: This might not be needed here if it is not impacting the view
        int order = 1;
        int size = mChildFields.size();
        while (order <= size) {
            for (ChildFields field : mChildFields) {
                if (field.getOrder() == order) {
                    sortedList.add(field);
                    order++;
                    break;
                }
            }
        }

        mChildFields = sortedList;
    }

    void addToChildFields(ChildFields childFields) {
        mChildFields.add(childFields);
    }

    String getTitle() {
        return mTitle;
    }

    boolean isMandatory() {
        return mMandatory;
    }

    boolean isOptional() {
        return !mMandatory;
    }

    boolean isEditable() {
        return mEditable;
    }

    public EpocTestFieldType getEpocTestFieldType() {
        return mEpocTestFieldType;
    }

    int getStepNumber() {
        return mStepNumber;
    }

    boolean isCompleted() {
        return mCompleted;
    }

    void setCompleted(boolean completed) {
        mCompleted = completed;
    }

    HashMap<String, View> getCustomChildFieldValues() {
        return mCustomChildFieldValues;
    }

    void addCustomFieldValue(String fieldType, View view) {
        mCustomChildFieldValues.put(fieldType, view);
    }


    public int getFieldOrder() {
        return mFieldOrder;
    }

    void setFieldOrder(int fieldOrder) {
        mFieldOrder = fieldOrder;
    }

    public IStepperItem getStepperItem() {
        return mStepperItem;
    }

    public void setStepperItem(IStepperItem stepperItem) {
        this.mStepperItem = stepperItem;
    }

    /**
     * This class contains the metadata of the field items of a step.
     */
    class ChildFields {
        EpocTestFieldType mEpocTestFieldType;
        View mView;
        int mOrder;
        boolean mCustom;
        boolean mEditable;
        String mCustomFieldName;
        private IStepperItem mStepperItem;

        ChildFields(EpocTestFieldType epocTestFieldType, View view, int order, boolean custom) {
            mEpocTestFieldType = epocTestFieldType;
            mView = view;
            mOrder = order;
            mCustom = custom;
            mCustomFieldName = "";
        }

        String getCustomFieldName() {
            return mCustomFieldName;
        }

        void setCustomFieldName(String customFieldName) {
            mCustomFieldName = customFieldName;
        }

        public boolean isCustom() {
            return mCustom;
        }

        public void setCustom(boolean custom) {
            mCustom = custom;
        }

        EpocTestFieldType getEpocTestFieldType() {
            return mEpocTestFieldType;
        }

        public View getView() {
            return mView;
        }

        int getOrder() {
            return mOrder;
        }

        boolean isEditable() {
            return mEditable;
        }

        void setEditable(boolean editable) {
            mEditable = editable;
        }

        public IStepperItem getStepperItem() {
            return mStepperItem;
        }

        public void setStepperItem(IStepperItem stepperItem) {
            this.mStepperItem = stepperItem;
        }

    }
}