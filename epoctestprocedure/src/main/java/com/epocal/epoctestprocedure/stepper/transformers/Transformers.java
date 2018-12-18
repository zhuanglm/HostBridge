package com.epocal.epoctestprocedure.stepper.transformers;

import android.content.Context;
import android.util.SparseIntArray;

import com.epocal.common.CU;
import com.epocal.common_ui.util.StringResourceValues;

/**
 * Static list of predefined tranformers
 */
public class Transformers {
    private static IStepperValueTransformer stringTransformer;
    private static IStepperValueTransformer integerTransformer;
    private static IStepperValueTransformer doubleTransformer;
    private static IStepperValueTransformer booleanTransformer;
    private static IStepperValueTransformer defaultTransformer;
    static {

        defaultTransformer = new IStepperValueTransformer() {
            @Override
            public Object transform(Object data) {
                return data;
            }
        };

        stringTransformer = new IStepperValueTransformer() {
            @Override
            public String transform(Object data) {
                String value = "";
                try {
                    value = (String)data;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return value;
            }
        };

        integerTransformer = new IStepperValueTransformer() {
            @Override
            public Integer transform(Object data) {
                Integer value = -1;
                try {
                    if (data != null) {
                        if (data instanceof Integer)
                            value = (Integer) data;
                        else if (data instanceof Double)
                            value = ((Double) data).intValue();
                        else {
                            if (data.equals("")) data = "0";
                            value = Integer.valueOf((String) data);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return value;
            }
        };

        doubleTransformer = new IStepperValueTransformer() {
            @Override
            public Double transform(Object data) {
                Double value = Double.NaN;
                try {
                    if (data != null) {
                        if (data instanceof Double)
                            value = (Double) data;
                        else if (data instanceof Integer)
                            value = ((Integer) data).doubleValue();
                        else
                            value = Double.valueOf((String) data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return value;
            }
        };

        booleanTransformer = new IStepperValueTransformer() {
            @Override
            public Boolean transform(Object data) {
                Boolean value = false;
                try {
                    if (data != null) {
                        if (data instanceof Boolean)
                            value = (Boolean) data;
                        else
                            value = (Integer) data == 1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return value;
            }
        };
    }

    /**
     * @return string if the object is a string, or empty string
     */
    public static IStepperValueTransformer getStringTransformer() {
        return stringTransformer;
    }

    /**
     *
     * @return integer if the object is a parsable string integer, or -1
     */
    public static IStepperValueTransformer getIntegerTransformer() {
        return integerTransformer;
    }

    /**
     *
     * @return double if the object is a parsable string double value, or -1.0
     */
    public static IStepperValueTransformer getDoubleTransformer() {
        return doubleTransformer;
    }

    /**
     *
     * @return true if the Object is an integer and value is 1. otherwise false
     */
    public static IStepperValueTransformer getBooleanTransformer() {
        return booleanTransformer;
    }

    /**
     * This transformer converts a string to an enum type base on
     * the EpocTestFieldType. It will return an Integer object with value -1
     * if no transformation was performed.
     */
    public static IStepperValueTransformer getStringToFieldTypeTransformer(final SparseIntArray array, final IStepperValueTransformer enumTypeTransformer, final Context context) {
        return new IStepperValueTransformer() {
            @Override
            public Object transform(Object data) {
                int key = StringResourceValues.getKey(array, (String) data, context);
                return enumTypeTransformer.transform(key);
            }
        };
    }

    public static IStepperValueTransformer getDateTransformer() {
        return new IStepperValueTransformer() {
            @Override
            public Object transform(Object data) {
                try {
                    CU.epocDateStringToDate((String)data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    public static IStepperValueTransformer getDefaultTransformer() {
        return defaultTransformer;
    }
}
