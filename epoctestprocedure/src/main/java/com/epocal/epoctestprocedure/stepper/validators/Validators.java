package com.epocal.epoctestprocedure.stepper.validators;

import android.util.Log;

import com.epocal.common.realmentities.InputFieldConfig;
import com.epocal.common.types.InputFieldType;
import com.epocal.datamanager.InputFieldConfigModel;
import com.epocal.epoctestprocedure.stepper.StepperItem;
import com.epocal.epoctestprocedure.stepper.transformers.Transformers;

/**
 * Static list of predefined validators
 */
public class Validators {
    private static final String TAG = StepperItem.class.getSimpleName();
    private static IStepperValueValidator defaultValidator;

    public static IStepperValueValidator getYesnoValidator(final boolean acceptDefault) {
        return new IStepperValueValidator() {
            @Override
            public boolean validate(Object data) {
                boolean result = false;
                try {
                    // for yes/no check if value is not -1
                    if (acceptDefault && Validators.isDefaultValue(data,-1))
                        return true;

                    Integer value = (Integer)Transformers.getIntegerTransformer().transform(data);
                    result = (value != -1);
                } catch (Exception e) {
                }
                if (result == false)
                    Log.d(TAG,String.format("YesNoValidator failed value: %s",data));
                return result;
            }
        };
    }

    public static IStepperValueValidator getDefaultValidator() {
        return defaultValidator;
    }

    public static IStepperValueValidator getIntegerValidator(final int min, final int max, final boolean allowEmpty) {
        return new IStepperValueValidator() {
            @Override
            //can validate a string representation of an integer or an actual integer
            public boolean validate(Object data) {
                boolean result = false;
                try {
                    if(allowEmpty && data.equals(""))
                        return true;

                    Integer value = (Integer) Transformers.getIntegerTransformer().transform(data);
                    result = (value >= min && value <= max);
                } catch (Exception e) {
                }
                if (result == false)
                    Log.d(TAG,String.format("IntegerValidator failed value: %s",data));
                return result;
            }
        };
    }

    public static IStepperValueValidator getNumericValidator(final boolean allowEmpty) {
        return new IStepperValueValidator() {
            @Override
            public boolean validate(Object data) {
                boolean result = false;
                try {
                    if(allowEmpty && data.equals(""))
                        return true;

                    Double value = (Double)Transformers.getDoubleTransformer().transform(data);
                    result = value != Double.NaN;
                } catch (Exception e) {
                }
                if (result == false)
                    Log.d(TAG,String.format("NumericValidator failed value: %s",data));
                return result;
            }
        };
    }

    public static IStepperValueValidator getDoubleValidator(final double min, final double max, final boolean allowEmpty) {
        return new IStepperValueValidator() {
            @Override
            //can validate a string representation of an double or an actual double
            public boolean validate(Object data) {
                boolean result = false;
                try {
                    if(allowEmpty && data.equals(""))
                        return true;

                    Double value = (Double)Transformers.getDoubleTransformer().transform(data);
                    result = (value >= min && value <= max);
                } catch (Exception e) {
                }
                if (result == false)
                    Log.d(TAG,String.format("DoubleValidator failed value: %s",data));
                return result;
            }
        };
    }

    public static IStepperValueValidator getInputFieldValidator(final InputFieldType inputFieldType, final boolean allowEmpty) {
        return new IStepperValueValidator() {
            @Override
            public boolean validate(Object data) {
                boolean result = false;
                try
                {
                    if (allowEmpty && (data == null || data.equals("")))
                        return true;

                    if (data == null)
                        return false;

                    String newValue = (String)data;
                    InputFieldConfig inputFieldConfig = new InputFieldConfigModel().getInputFieldConfig(inputFieldType);
                    if (inputFieldConfig != null && newValue != null) {
                        result = (newValue.length() >= inputFieldConfig.getMinimumLength() && newValue.length() <= inputFieldConfig.getMaximumLength());
                    }
                } catch (Exception e) {
                }
                if (result == false)
                    Log.d(TAG,String.format("InputFieldValidator failed value: %s",data));
                return result;
            }
        };
    }

    public static boolean isDefaultValue(Object data, Object defaultValue) {
        if (data == null && defaultValue == null)
            return true;
        else if (data != null && defaultValue == null)
            return false;
        else if (data == null && defaultValue != null)
            return false;
        else
            return data.equals(defaultValue);
    }
    //region static initialization
    static {
        // static validators have no parameters and the same instance can be reused
        defaultValidator = new IStepperValueValidator() {
            @Override
            public boolean validate(Object data) {
                return true;
            }
        };
    }
    //endregion
}
