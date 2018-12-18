package com.epocal.epoctestprocedure.stepper;

import android.content.Context;
import android.util.Log;

import com.epocal.common.realmentities.CustomTestInputField;
import com.epocal.common.realmentities.InputFieldConfig;
import com.epocal.common.realmentities.QASampleInfo;
import com.epocal.common.realmentities.TestInputField;
import com.epocal.common.types.EpocTestFieldType;
import com.epocal.common.types.EpocTestPanelType;
import com.epocal.common.types.InputFieldType;
import com.epocal.common.types.NotifyActionType;
import com.epocal.common.types.TestType;
import com.epocal.common.types.am.AllensTest;
import com.epocal.common.types.am.BloodSampleType;
import com.epocal.common.types.am.DeliverySystem;
import com.epocal.common.types.am.DrawSites;
import com.epocal.common.types.am.Gender;
import com.epocal.common.types.am.RespiratoryMode;
import com.epocal.common_ui.util.StringResourceValues;
import com.epocal.datamanager.InputFieldConfigModel;
import com.epocal.datamanager.QASampleInfoModel;
import com.epocal.epoctest.uidriver.TestUIDriver;
import com.epocal.epoctestprocedure.R;
import com.epocal.epoctestprocedure.stepper.transformers.IStepperValueTransformer;
import com.epocal.epoctestprocedure.stepper.transformers.Transformers;
import com.epocal.epoctestprocedure.stepper.types.StepperHintType;
import com.epocal.epoctestprocedure.stepper.types.StepperType;
import com.epocal.epoctestprocedure.stepper.validators.IStepperValueValidator;
import com.epocal.epoctestprocedure.stepper.validators.Validators;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class StepperItem implements IStepperItem {
    private static final String TAG = StepperItem.class.getSimpleName();
    private StepperType dataType;
    private String title;
    private String errorMessage;
    private StepperHintType hintType;
    private String[] optionsArray;
    private Context context;
    private EpocTestFieldType testFieldType;
    private Integer displayOrder;
    private Boolean isCustomField;
    private IStepperItemBehavior behavior;

    //region setters and getters
    public StepperType getDataType() {
        return dataType;
    }

    public String getTitle() {
        return title;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public StepperHintType getHintType() {
        return hintType;
    }

    public String[] getOptionsArray() {
        return optionsArray;
    }

    public IStepperValue getValue(TestUIDriver testUIDriver) {
        return behavior.load(testUIDriver);
    }


    public EpocTestFieldType getTestFieldType() {
        return testFieldType;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }
    
    
    public Boolean isCustom() {
        return isCustomField;
    }
    //endregion

    //region IStepperItem methods
    @Override
    public String getSubtitleValue(TestUIDriver testUIDriver) {
        return behavior.subtitle(testUIDriver);
    }

    @Override
    public Boolean isValidated(TestUIDriver testUIDriver) {
        return behavior.getValidator().validate(getValue(testUIDriver).getValue());
    }

    @Override
    public Boolean validate(Object data) {
        return behavior.getValidator().validate(data);
    }

    @Override
    public Boolean validateAndSave(TestUIDriver testUIDriver, Object data) {
        if (behavior.getValidator().validate(data)) {
            behavior.save(testUIDriver, data);
            return true;
        }
        return false;
    }
    //endregion

    public StepperItem(Context context, TestInputField testInputField) {
        reset();
        this.context = context;
        populate(testInputField);
    }

    public StepperItem(Context context, CustomTestInputField customTestInputField) {
        reset();
        this.context = context;
        populate(customTestInputField);
    }

    public void reset() {
        dataType = StepperType.VS_STRING;
        hintType = StepperHintType.VS_HINT_ENTER;
        title = "";
        errorMessage = "";
        isCustomField = false;
        this.behavior = defaultBehavior();
    }

    private void setParams(StepperType dataType, StepperHintType hintType, String errorMessage, String[] optionsArray) {
        this.dataType = dataType;
        this.hintType = hintType;
        this.errorMessage = errorMessage;
        this.optionsArray = optionsArray;
    }

    public void populate(CustomTestInputField customTestInputField) {
        this.title = customTestInputField.getName();
        this.isCustomField = true;
        this.displayOrder = customTestInputField.getDisplayOrder();
        this.behavior = customTestFieldBehavior(customTestInputField.getFieldType());
    }

    public void populate(TestInputField testInputField) {
        this.title = context.getString(StringResourceValues.setEpocTestFieldType().get(testInputField.getFieldType().value));
        this.displayOrder = testInputField.getDisplayOrder();
        this.testFieldType = testInputField.getFieldType();

        switch(testInputField.getFieldType()) {
            case UNKNOWN:
                break;
            case PATIENTID:
                behavior = patientIdBehavior();
                break;
            case PATIENTID2:
                behavior = patientId2Behavior();
                break;
            case PATIENTHEIGHT:
                behavior = patientHeightBehavior();
                break;
            case TESTSELECTION:
                behavior = testSelectionBehavior();
                break;
            case SAMPLETYPE:
                behavior = sampleTypeBehavior();
                break;
            case PATIENTTEMPERATURE:
                behavior = patientTemperatureBehavior();
                break;
            case REJECTTEST:
            case READBACK:
            case HEMODILUTION:
                behavior = yesnoBehavior(testInputField.getFieldType());
                break;
            case COMMENTS:
                behavior = commentsBehavior();
                break;
            case DRAWSITE:
                behavior = drawSitesBehavior();
                break;
            case ALLENSTEST:
                behavior = allensTestBehavior();
                break;
            case DELIVERYSYSTEM:
                behavior = deliverySystemBehavior();
                break;
            case MODE:
                behavior = modeBehavior();
                break;
            case PATIENTAGE:
                behavior = patientAgeBehavior();
                break;
            case FLOW:
            case HERTZ:
            case DELTAP:
            case NOPPM:
            case RQ:
            case FIO2:
            case VT:
            case RR:
            case TR:
            case PEEP:
            case PS:
            case IT:
            case ET:
            case PIP:
            case MAP:
                behavior = numericBehavior(testInputField.getFieldType());
                break;
            case ORDERDATE:
            case COLLECTIONDATE:
            case NOTIFYDATE:
                behavior = dateBehavior(testInputField.getFieldType());
                break;
            case ORDERINGPHYSICIAN:
            case COLLECTEDBY:
            case PATIENTLOCATION:
            case NOTIFYNAME:
                behavior = stringBehavior(testInputField.getFieldType());
                break;
            case ORDERTIME:
            case NOTIFYTIME:
            case COLLECTIONTIME:
                behavior = timeBehavior(testInputField.getFieldType());
                break;
            case PATIENTGENDER:
                behavior = genderBehavior();
                break;
            case NOTIFYACTION:
                behavior = notifyActionBehavior();
                break;
            case LOTNUMBER:
                behavior = lotNumberBehavior();
                break;
            case FLUIDTYPE:
                behavior = fluidTypeBehavior();
                break;
            case TESTTYPE:
                behavior = testTypeBehavior();
                break;
            case CUSTOM:
                break;
        }
        behavior.init();
    }

    //region behaviors
    private IStepperItemBehavior testSelectionBehavior() {
        return new IStepperItemBehavior() {
            @Override
            public void init() {
                setParams(StepperType.VS_RADIO, StepperHintType.VS_HINT_ENTER,
                        "", new String[0]);
            }

            @Override
            public String subtitle(TestUIDriver testUIDriver) {
                return context.getString(StringResourceValues.setEpocTestFieldType().get(testFieldType.value));
            }

            @Override
            public IStepperValue load(TestUIDriver testUIDriver) {
                try {
                    return new ArrayStepperValue(testUIDriver.getSelectedPanel().value);
                } catch(Exception e) {
                    return new NoStepperValue();
                }
            }

            @Override
            public void save(TestUIDriver testUIDriver, Object data) {
                EpocTestPanelType value = (EpocTestPanelType) getTransformer().transform(data);
                testUIDriver.setTestInclusions(value);
            }

            @Override
            public IStepperValueTransformer getTransformer() {
                return new IStepperValueTransformer() {
                    @Override
                    public Object transform(Object data) {
                        try {
                            return EpocTestPanelType.fromInt((Integer) data);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return EpocTestPanelType.UNKNOWN;
                    }
                };
            }

            @Override
            public IStepperValueValidator getValidator() {
                return new IStepperValueValidator() {
                    @Override
                    public boolean validate(Object data) {
                        try {
                            return (Integer)data != -1;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG,String.format("Validation failed for type: %s -- value: %s",getTestFieldType(),data));
                        return false;
                    }
                };
            }
        };
    }

    private IStepperItemBehavior customTestFieldBehavior(EpocTestFieldType fieldType) {
        return new IStepperItemBehavior() {
            @Override
            public void init() {
                setParams(StepperType.VS_STRING, StepperHintType.VS_HINT_ENTER, context.getString(R.string.error), null);
            }

            @Override
            public String subtitle(TestUIDriver testUIDriver) {
                return context.getString(StringResourceValues.setEpocTestFieldType().get(testFieldType.value));
            }

            @Override
            public IStepperValue load(TestUIDriver testUIDriver) {
                return new NoStepperValue();
            }

            @Override
            public void save(TestUIDriver testUIDriver, Object data) {
                String value = (String)getTransformer().transform(data);
                //getTestUIDriver().saveCustomVariable(customFieldName, value);
            }

            @Override
            public IStepperValueTransformer getTransformer() {
                return Transformers.getStringTransformer();
            }

            @Override
            public IStepperValueValidator getValidator() {
                return new IStepperValueValidator() {
                    @Override
                    public boolean validate(Object data) {
                        return true;
                    }
                };
            }
        };
    }

    private IStepperItemBehavior notifyActionBehavior() {
        return new IStepperItemBehavior() {
            @Override
            public void init() {
                setParams(StepperType.VS_RADIO, StepperHintType.VS_HINT_ENTER,
                        "", StringResourceValues.getStringValues(context, StringResourceValues.getValues(StringResourceValues.setNotifyActionTypes())));
            }

            @Override
            public String subtitle(TestUIDriver testUIDriver) {
                return context.getString(StringResourceValues.setEpocTestFieldType().get(testFieldType.value));
            }

            @Override
            public IStepperValue load(TestUIDriver testUIDriver) {
                try {
                    return new ArrayStepperValue(testUIDriver.getTestData().getTestDetail().getNotifyActionType().value);
                } catch(Exception e) {
                    return new NoStepperValue();
                }
            }

            @Override
            public void save(TestUIDriver testUIDriver, Object data) {
                NotifyActionType value = (NotifyActionType)getTransformer().transform(data);
                testUIDriver.getTestData().getTestDetail().setNotifyActionType(value);
            }

            @Override
            public IStepperValueTransformer getTransformer() {
                return new IStepperValueTransformer() {
                    @Override
                    public Object transform(Object data) {
                        try {
                            return NotifyActionType.fromInt((Integer) data);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return NotifyActionType.None;
                    }
                };
            }

            @Override
            public IStepperValueValidator getValidator() {
                return new IStepperValueValidator() {
                    @Override
                    public boolean validate(Object data) {
                        try {
                            return (Integer)data != -1;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG,String.format("Validation failed for type: %s -- value: %s",getTestFieldType(),data));
                        return false;
                    }
                };
            }
        };
    }

    private IStepperItemBehavior genderBehavior() {
        return new IStepperItemBehavior() {
            @Override
            public void init() {
                setParams(StepperType.VS_RADIO, StepperHintType.VS_HINT_ENTER,
                        "", context.getResources().getStringArray(R.array.gender));
            }

            @Override
            public String subtitle(TestUIDriver testUIDriver) {
                return context.getString(StringResourceValues.setEpocTestFieldType().get(testFieldType.value));
            }

            @Override
            public IStepperValue load(TestUIDriver testUIDriver) {
                try {
                    return new ArrayStepperValue(testUIDriver.getTestData().getGender().value);
                } catch(Exception e) {
                    return new NoStepperValue();
                }
            }

            @Override
            public void save(TestUIDriver testUIDriver, Object data) {
                Gender value = (Gender)getTransformer().transform(data);
                testUIDriver.getTestData().setGender(value);
            }

            @Override
            public IStepperValueTransformer getTransformer() {
                return new IStepperValueTransformer() {
                    @Override
                    public Object transform(Object data) {
                        Gender gender = Gender.Unknown;
                        try {
                            gender = Gender.fromInt((Integer) data);
                            if (gender == Gender.ENUM_UNINITIALIZED)
                                gender = Gender.Unknown;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return gender;
                    }
                };
            }

            @Override
            public IStepperValueValidator getValidator() {
                return new IStepperValueValidator() {
                    @Override
                    public boolean validate(Object data) {
                        boolean result = false;
                        try {
                            if (Validators.isDefaultValue(data,-1))
                                return true;
                            Gender value = (Gender)getTransformer().transform(data);
                            result = value != Gender.ENUM_UNINITIALIZED;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (result == false)
                            Log.d(TAG,String.format("Validation failed for type: %s -- value: %s",getTestFieldType(),data));
                        return result;
                    }
                };
            }
        };
    }

    private IStepperItemBehavior defaultBehavior() {
        return new IStepperItemBehavior() {
            @Override
            public void init() {
                setParams(StepperType.VS_STRING, StepperHintType.VS_HINT_ENTER,
                        context.getString(R.string.error), null);
            }

            @Override
            public String subtitle(TestUIDriver testUIDriver) {
                return context.getString(StringResourceValues.setEpocTestFieldType().get(testFieldType.value));
            }

            @Override
            public IStepperValue load(TestUIDriver testUIDriver) {
                return new NoStepperValue();
            }

            @Override
            public void save(TestUIDriver testUIDriver, Object data) {
            }

            @Override
            public IStepperValueTransformer getTransformer() {
                return Transformers.getDefaultTransformer();
            }

            @Override
            public IStepperValueValidator getValidator() {
                return Validators.getDefaultValidator();
            }
        };
    }

    private IStepperItemBehavior timeBehavior(final EpocTestFieldType fieldType) {
        return new IStepperItemBehavior() {
            @Override
            public void init() {
                setParams(StepperType.VS_TIME, StepperHintType.VS_HINT_ENTER,
                        context.getString(R.string.error), null);
            }

            @Override
            public String subtitle(TestUIDriver testUIDriver) {
                return context.getString(StringResourceValues.setEpocTestFieldType().get(testFieldType.value));
            }

            @Override
            public IStepperValue load(TestUIDriver testUIDriver) {
                try {
                    String value = "";
                    switch (fieldType) {
                        case COLLECTIONTIME:
                            value = testUIDriver.getTestData().getTestDetail().getCollectionTime();
                            break;
                        case NOTIFYTIME:
                            value = testUIDriver.getTestData().getTestDetail().getNotifyTime();
                            break;
                        case ORDERTIME:
                            value = testUIDriver.getTestData().getTestDetail().getOrderTime();
                            break;
                    }
                    return new StringStepperValue(value);
                } catch(Exception e) {
                    return new NoStepperValue();
                }
            }

            @Override
            public void save(TestUIDriver testUIDriver, Object data) {
                String value = (String)getTransformer().transform(data);
                if (value != null) {
                    switch (fieldType) {
                        case COLLECTIONTIME:
                            testUIDriver.getTestData().getTestDetail().setCollectionTime(value); break;
                        case NOTIFYTIME:
                            testUIDriver.getTestData().getTestDetail().setNotifyTime(value); break;
                        case ORDERTIME:
                            testUIDriver.getTestData().getTestDetail().setOrderTime(value); break;
                    }
                } else {
                    //TODO: log error
                }
            }

            @Override
            public IStepperValueTransformer getTransformer() {
                return Transformers.getStringTransformer();
            }

            @Override
            public IStepperValueValidator getValidator() {
                return Validators.getDefaultValidator();
            }
        };
    }

    private IStepperItemBehavior fluidTypeBehavior() {
        return new IStepperItemBehavior() {
            List<QASampleInfo> matchingTypes = new QASampleInfoModel().getUnmanagedMatchingQASamples(new String[] {"Default","Unknown"});

            @Override
            public void init() {
                String[] fluidTypes = new String[matchingTypes.size()];
                for (int i=0; i<matchingTypes.size(); i++) {
                    fluidTypes[i] = matchingTypes.get(i).getName();
                }
                setParams(StepperType.VS_RADIO, StepperHintType.VS_HINT_ENTER,"", fluidTypes);
            }

            @Override
            public String subtitle(TestUIDriver testUIDriver) {
                return context.getString(StringResourceValues.setEpocTestFieldType().get(testFieldType.value));
            }

            @Override
            public IStepperValue load(TestUIDriver testUIDriver) {
                try {
                    int arrayIndex = -1;
                    int cachedCount = 0; //TODO: check if options are cached
                    if (cachedCount != 0) {
                        //TODO select matching types based on cached values
                        // matchingTypes = new QASampleInfoModel().getUnmanagedMatchingQASamples(SEARCH BY TYPE);
                    }

                    for (int i = 0; i < matchingTypes.size(); i++) {
                        if (matchingTypes.get(i).getName().equals(testUIDriver.getTestData().getTestDetail().getQaSampleInfo().getName()))
                            arrayIndex = i;
                    }
                    return new ArrayStepperValue(arrayIndex);
                } catch(Exception e) {
                    return new NoStepperValue();
                }
            }

            @Override
            public void save(TestUIDriver testUIDriver, Object data) {
                QASampleInfo value = (QASampleInfo)getTransformer().transform(data);
                if(value != null && testUIDriver.getTestData().getTestDetail().getQaSampleInfo() != null &&
                        !testUIDriver.getTestData().getTestDetail().getQaSampleInfo().getName().equals(value.getName())) {
                    testUIDriver.getTestData().getTestDetail().setQaSampleInfo(value);
                    testUIDriver.getTestData().getTestDetail().setSampleType(BloodSampleType.Unspecified); //TODO: should be NONE but enum not defined (check with dean)
                }
            }

            @Override
            public IStepperValueTransformer getTransformer() {
                return new IStepperValueTransformer() {
                    @Override
                    public Object transform(Object data) {
                        try {
                            return matchingTypes.get((Integer)data);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };
            }

            @Override
            public IStepperValueValidator getValidator() {
                return new IStepperValueValidator() {
                    @Override
                    public boolean validate(Object data) {
                        QASampleInfo value = (QASampleInfo)getTransformer().transform(data);
                        boolean result = value != null;
                        if (result == false)
                            Log.d(TAG,String.format("Validation failed for type: %s -- value: %s",getTestFieldType(),data));
                        return result;
                    }
                };
            }
        };
    }

    private IStepperItemBehavior testTypeBehavior() {
        return new IStepperItemBehavior() {
            @Override
            public void init() {
                setParams(StepperType.VS_RADIO, StepperHintType.VS_HINT_ENTER,
                        "", StringResourceValues.getStringValues(context, StringResourceValues.getValues(StringResourceValues.setTestTypeForTestProcedure())));

            }

            @Override
            public String subtitle(TestUIDriver testUIDriver) {
                return context.getString(StringResourceValues.setEpocTestFieldType().get(testFieldType.value));
            }

            @Override
            public IStepperValue load(TestUIDriver testUIDriver) {
                try {
                    int arrayIndex = -1;
                    // cannot use TestType.value() because some enums are disabled in StringResourceValues.setTestType()
                    // future enhancement to handle "hidden" enum types needed
                    if (testUIDriver.getTestData().getType() == TestType.QualityControl) {
                        arrayIndex = 0;
                    } else if (testUIDriver.getTestData().getType() == TestType.CalVer) {
                        arrayIndex = 1;
                    } else if (testUIDriver.getTestData().getType() == TestType.Proficiency) {
                        arrayIndex = 2;
                    } else if (testUIDriver.getTestData().getType() == TestType.Other) {
                        arrayIndex = 3;
                    }
                    return new ArrayStepperValue(arrayIndex);
                } catch(Exception e) {
                    return new NoStepperValue();
                }
            }

            @Override
            public void save(TestUIDriver testUIDriver, Object data) {
                TestType value = (TestType)getTransformer().transform(data);
                testUIDriver.getTestData().setType(value);
            }

            @Override
            public IStepperValueTransformer getTransformer() {
                return new IStepperValueTransformer() {
                    @Override
                    public Object transform(Object data) {
                        try {
                            switch((Integer) data) {
                                case 0: return TestType.QualityControl;
                                case 1: return TestType.CalVer;
                                case 2: return TestType.Proficiency;
                                case 3: return TestType.Other;
                                default:
                                    return TestType.Unknown;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return TestType.Unknown;
                    }
                };
            }

            @Override
            public IStepperValueValidator getValidator() {
                return new IStepperValueValidator() {
                    @Override
                    public boolean validate(Object data) {
                        TestType testType = (TestType)getTransformer().transform(data);
                        boolean result = testType != TestType.Unknown;
                        if (result == false)
                            Log.d(TAG,String.format("Validation failed for type: %s -- value: %s",getTestFieldType(),data));
                        return result;
                    }
                };
            }
        };
    }

    private IStepperItemBehavior stringBehavior(final EpocTestFieldType fieldType) {
        return new IStepperItemBehavior() {
            @Override
            public void init() {
                setParams(StepperType.VS_STRING, StepperHintType.VS_HINT_ENTER,
                        context.getString(R.string.error), null);
            }

            @Override
            public String subtitle(TestUIDriver testUIDriver) {
                return context.getString(StringResourceValues.setEpocTestFieldType().get(testFieldType.value));
            }

            @Override
            public IStepperValue load(TestUIDriver testUIDriver) {
                try {
                    String value = "";
                    switch (fieldType) {
                        case COLLECTEDBY:
                            value = testUIDriver.getTestData().getTestDetail().getCollectedBy();
                            break;
                        case PATIENTLOCATION:
                            value = testUIDriver.getTestData().getTestDetail().getPatientLocation();
                            break;
                        case ORDERINGPHYSICIAN:
                            value = testUIDriver.getTestData().getTestDetail().getOrderingPhysician();
                            break;
                        case NOTIFYNAME:
                            value = testUIDriver.getTestData().getTestDetail().getNotifyName();
                            break;
                    }
                    return new StringStepperValue(value);
                } catch(Exception e) {
                    return new NoStepperValue();
                }
            }

            @Override
            public void save(TestUIDriver testUIDriver, Object data) {
                String value = (String)getTransformer().transform(data);
                if (value != null) {
                    switch (fieldType) {
                        case COLLECTEDBY:
                            testUIDriver.getTestData().getTestDetail().setCollectedBy(value); break;
                        case PATIENTLOCATION:
                            testUIDriver.getTestData().getTestDetail().setPatientLocation(value); break;
                        case ORDERINGPHYSICIAN:
                            testUIDriver.getTestData().getTestDetail().setOrderingPhysician(value); break;
                        case NOTIFYNAME:
                            testUIDriver.getTestData().getTestDetail().setNotifyName(value); break;
                    }
                } else {
                    //TODO: log error
                }
            }

            @Override
            public IStepperValueTransformer getTransformer() {
                return Transformers.getStringTransformer();
            }

            @Override
            public IStepperValueValidator getValidator() {
                return new IStepperValueValidator() {
                    @Override
                    public boolean validate(Object data) {
                        return true;
                    }
                };
            }
        };
    }

    private IStepperItemBehavior dateBehavior(final EpocTestFieldType fieldType) {
        return new IStepperItemBehavior() {
            @Override
            public void init() {
                setParams(StepperType.VS_DATE, StepperHintType.VS_HINT_ENTER, null, null);
            }

            @Override
            public String subtitle(TestUIDriver testUIDriver) {
                return context.getString(StringResourceValues.setEpocTestFieldType().get(testFieldType.value));
            }

            @Override
            public IStepperValue load(TestUIDriver testUIDriver) {
                try {
                    String value = "";
                    switch (fieldType) {
                        case ORDERDATE:
                            value = testUIDriver.getTestData().getTestDetail().getOrderDate().toString();
                            break;
                        case COLLECTIONDATE:
                            value = testUIDriver.getTestData().getTestDetail().getCollectionDate().toString();
                            break;
                        case NOTIFYDATE:
                            value = testUIDriver.getTestData().getTestDetail().getNotifyDate().toString();
                            break;
                    }
                    return new StringStepperValue(value);
                } catch(Exception e) {
                    return new NoStepperValue();
                }
            }

            @Override
            public void save(TestUIDriver testUIDriver, Object data) {
                Date value = (Date)getTransformer().transform(data);
                if (value != null) {
                    switch (fieldType) {
                        case ORDERDATE:
                            testUIDriver.getTestData().getTestDetail().setOrderDate(value);
                            break;
                        case COLLECTIONDATE:
                            testUIDriver.getTestData().getTestDetail().setCollectionDate(value);
                            break;
                        case NOTIFYDATE:
                            testUIDriver.getTestData().getTestDetail().setNotifyDate(value);
                            break;
                    }
                } else {
                    //TODO: log error
                }
            }

            @Override
            public IStepperValueTransformer getTransformer() {
                return Transformers.getDateTransformer();
            }

            @Override
            public IStepperValueValidator getValidator() {
                return new IStepperValueValidator() {
                    @Override
                    public boolean validate(Object data) {
                        if (Validators.isDefaultValue(data,""))
                            return true;

                        Date value = (Date)getTransformer().transform(data);
                        boolean result = value != null;
                        if (result == false)
                            Log.d(TAG,String.format("Validation failed for type: %s -- value: %s",getTestFieldType(),data));
                        return result;
                    }
                };
            }
        };
    }

    private IStepperItemBehavior commentsBehavior() {
        return new IStepperItemBehavior() {
            @Override
            public void init() {
                setParams(StepperType.VS_STRING, StepperHintType.VS_HINT_ENTER,
                        context.getString(R.string.error), null);
            }

            @Override
            public String subtitle(TestUIDriver testUIDriver) {
                return context.getString(StringResourceValues.setEpocTestFieldType().get(testFieldType.value));
            }

            @Override
            public IStepperValue load(TestUIDriver testUIDriver) {
                try {
                    return new StringStepperValue(testUIDriver.getTestData().getTestDetail().getComment());
                } catch(Exception e) {
                    return new NoStepperValue();
                }
            }

            @Override
            public void save(TestUIDriver testUIDriver, Object data) {
                String value = (String)getTransformer().transform(data);
                testUIDriver.getTestData().getTestDetail().setComment(value);
            }

            @Override
            public IStepperValueTransformer getTransformer() {
                return Transformers.getStringTransformer();
            }

            @Override
            public IStepperValueValidator getValidator() {
                return Validators.getInputFieldValidator(InputFieldType.COMMENT, true);
            }
        };
    }

    private IStepperItemBehavior numericBehavior(final EpocTestFieldType fieldType) {
        return new IStepperItemBehavior() {
            @Override
            public void init() {
                setParams(StepperType.VS_INTEGER, StepperHintType.VS_HINT_ENTER,
                        context.getString(R.string.error), null);
            }

            @Override
            public String subtitle(TestUIDriver testUIDriver) {
                return context.getString(StringResourceValues.setEpocTestFieldType().get(testFieldType.value));
            }

            @Override
            public IStepperValue load(TestUIDriver testUIDriver) {
                try {
                    String value = "";
                    switch (fieldType) {
                        case FIO2:
                            value = testUIDriver.getTestData().getRespiratoryDetail().getFiO2();
                            if (value.equals(String.valueOf(Double.NaN)))
                                value = "";
                            break;
                        case VT:
                            value = testUIDriver.getTestData().getRespiratoryDetail().getTidalVolume();
                            break;
                        case RR:
                            value = testUIDriver.getTestData().getRespiratoryDetail().getRespiratoryRate();
                            break;
                        case TR:
                            value = testUIDriver.getTestData().getRespiratoryDetail().getTotalRespiratoryRate();
                            break;
                        case PEEP:
                            value = testUIDriver.getTestData().getRespiratoryDetail().getPeep();
                            break;
                        case PS:
                            value = testUIDriver.getTestData().getRespiratoryDetail().getPs();
                            break;
                        case IT:
                            value = testUIDriver.getTestData().getRespiratoryDetail().getInspiratoryTime();
                            break;
                        case ET:
                            value = testUIDriver.getTestData().getRespiratoryDetail().getExpiratoryTime();
                            break;
                        case PIP:
                            value = testUIDriver.getTestData().getRespiratoryDetail().getPeakInspiratoryPressure();
                            break;
                        case MAP:
                            value = testUIDriver.getTestData().getRespiratoryDetail().getMeanAirWayPressure();
                            break;
                        case FLOW:
                            value = testUIDriver.getTestData().getRespiratoryDetail().getFlow();
                            if (value.equals(String.valueOf(Double.NaN)))
                                value = "";
                            break;
                        case HERTZ:
                            value = testUIDriver.getTestData().getRespiratoryDetail().getHertz();
                            if (value.equals(String.valueOf(Double.NaN)))
                                value = "";
                            break;
                        case DELTAP:
                            value = testUIDriver.getTestData().getRespiratoryDetail().getDeltaP();
                            if (value.equals(String.valueOf(Double.NaN)))
                                value = "";
                            break;
                        case NOPPM:
                            value = testUIDriver.getTestData().getRespiratoryDetail().getNoPPM();
                            if (value.equals(String.valueOf(Double.NaN)))
                                value = "";
                            break;
                        case RQ:
                            value = testUIDriver.getTestData().getRespiratoryDetail().getRq();
                            if (value.equals(String.valueOf(Double.NaN)))
                                value = "";
                            break;
                    }
                    return new StringStepperValue(value);
                } catch(Exception e) {
                    return new NoStepperValue();
                }
            }

            @Override
            public void save(TestUIDriver testUIDriver, Object data) {
                String value = (String)getTransformer().transform(data);
                switch(fieldType) {
                    case FIO2:
                        testUIDriver.getTestData().getRespiratoryDetail().setFiO2(value); break;
                    case VT:
                        testUIDriver.getTestData().getRespiratoryDetail().setTidalVolume(value); break;
                    case RR:
                        testUIDriver.getTestData().getRespiratoryDetail().setRespiratoryRate(value); break;
                    case TR:
                        testUIDriver.getTestData().getRespiratoryDetail().setTotalRespiratoryRate(value); break;
                    case PEEP:
                        testUIDriver.getTestData().getRespiratoryDetail().setPeep(value); break;
                    case PS:
                        testUIDriver.getTestData().getRespiratoryDetail().setPs(value); break;
                    case IT:
                        testUIDriver.getTestData().getRespiratoryDetail().setInspiratoryTime(value); break;
                    case ET:
                        testUIDriver.getTestData().getRespiratoryDetail().setExpiratoryTime(value); break;
                    case PIP:
                        testUIDriver.getTestData().getRespiratoryDetail().setPeakInspiratoryPressure(value); break;
                    case MAP:
                        testUIDriver.getTestData().getRespiratoryDetail().setMeanAirWayPressure(value); break;
                    case FLOW:
                        testUIDriver.getTestData().getRespiratoryDetail().setFlow(value); break;
                    case HERTZ:
                        testUIDriver.getTestData().getRespiratoryDetail().setHertz(value); break;
                    case DELTAP:
                        testUIDriver.getTestData().getRespiratoryDetail().setDeltaP(value); break;
                    case NOPPM:
                        testUIDriver.getTestData().getRespiratoryDetail().setNoPPM(value); break;
                    case RQ:
                        testUIDriver.getTestData().getRespiratoryDetail().setRq(value); break;
                }
            }

            @Override
            public IStepperValueTransformer getTransformer() {
                return Transformers.getStringTransformer();
            }

            @Override
            public IStepperValueValidator getValidator() {
                switch(fieldType) {
                    case FIO2:
                        return Validators.getDoubleValidator(21.0, 100.0, false);
                    case RQ:
                        return Validators.getDoubleValidator(0.01, 2.00, true);
                    case VT:
                    case RR:
                    case TR:
                    case PEEP:
                    case PS:
                    case IT:
                    case ET:
                    case PIP:
                    case MAP:
                    case FLOW:
                    case HERTZ:
                    case DELTAP:
                    case NOPPM:
                        return Validators.getNumericValidator(true);
                }
                return Validators.getDefaultValidator(); //TODO: log as warning
            }
        };
    }

    private IStepperItemBehavior modeBehavior() {
        return new IStepperItemBehavior() {
            @Override
            public void init() {
                setParams(StepperType.VS_COMBO, StepperHintType.VS_HINT_ENTERORSELECT,
                        "", StringResourceValues.getStringValues(context, StringResourceValues.getValues(StringResourceValues.setRespiratoryMode())));
            }

            @Override
            public String subtitle(TestUIDriver testUIDriver) {
                return context.getString(StringResourceValues.setEpocTestFieldType().get(testFieldType.value));
            }

            @Override
            public IStepperValue load(TestUIDriver testUIDriver) {
                try {
                    return new ArrayStepperValue(testUIDriver.getTestData().getRespiratoryDetail().getRespiratoryMode().value);
                } catch(Exception e) {
                    return new NoStepperValue();
                }
            }

            @Override
            public void save(TestUIDriver testUIDriver, Object data) {
                RespiratoryMode value = (RespiratoryMode)getTransformer().transform(data);
                testUIDriver.getTestData().getRespiratoryDetail().setRespiratoryMode(value);
            }

            @Override
            public IStepperValueTransformer getTransformer() {
                IStepperValueTransformer enumTypeTransformer = new IStepperValueTransformer() {
                    @Override
                    public Object transform(Object data) {
                        try {
                            return RespiratoryMode.fromInt((Integer) data);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return RespiratoryMode.ENUM_UNINITIALIZED;
                    }
                };
                return Transformers.getStringToFieldTypeTransformer(StringResourceValues.setRespiratoryMode(), enumTypeTransformer, context);
            }

            @Override
            public IStepperValueValidator getValidator() {
                return new IStepperValueValidator() {
                    @Override
                    public boolean validate(Object data) {
                        boolean result = false;
                        try {
                            if (Validators.isDefaultValue(data, ""))
                                return true;
                            RespiratoryMode value = (RespiratoryMode) getTransformer().transform(data);
                            result = value != RespiratoryMode.ENUM_UNINITIALIZED;
                        } catch (Exception e) {}
                        if (result == false)
                            Log.d(TAG,String.format("Validation failed for type: %s -- value: %s",getTestFieldType(),data));
                        return result;
                    }
                };
            }
        };
    }

    private IStepperItemBehavior deliverySystemBehavior() {
        return new IStepperItemBehavior() {
            @Override
            public void init() {
                setParams(StepperType.VS_COMBO, StepperHintType.VS_HINT_ENTERORSELECT,
                        "", StringResourceValues.getStringValues(context, StringResourceValues.getValues(StringResourceValues.setDeliverySystem())));
            }

            @Override
            public String subtitle(TestUIDriver testUIDriver) {
                return context.getString(StringResourceValues.setEpocTestFieldType().get(testFieldType.value));
            }

            @Override
            public IStepperValue load(TestUIDriver testUIDriver) {
                try {
                    return new ArrayStepperValue(testUIDriver.getTestData().getRespiratoryDetail().getDeliverySystem().value);
                } catch(Exception e) {
                    return new NoStepperValue();
                }
            }

            @Override
            public void save(TestUIDriver testUIDriver, Object data) {
                DeliverySystem value = (DeliverySystem)getTransformer().transform(data);
                testUIDriver.getTestData().getRespiratoryDetail().setDeliverySystem(value);
            }

            @Override
            public IStepperValueTransformer getTransformer() {
                IStepperValueTransformer enumTypeTransformer = new IStepperValueTransformer() {
                    @Override
                    public Object transform(Object data) {
                        try {
                            return DeliverySystem.fromInt((Integer) data);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return DeliverySystem.ENUM_UNINITIALIZED;
                    }
                };
                return Transformers.getStringToFieldTypeTransformer(StringResourceValues.setDeliverySystem(), enumTypeTransformer, context);
            }

            @Override
            public IStepperValueValidator getValidator() {
                return new IStepperValueValidator() {
                    @Override
                    public boolean validate(Object data) {
                        if (Validators.isDefaultValue(data,""))
                            return true;
                        boolean result = false;
                        try {
                            DeliverySystem value = (DeliverySystem) getTransformer().transform(data);
                            result = value != DeliverySystem.ENUM_UNINITIALIZED;
                            if (result == false)
                                Log.d(TAG, String.format("Validation failed for type: %s -- value: %s", getTestFieldType(), data));
                        } catch (Exception e) {}
                        return result;
                    }
                };
            }
        };
    }

    private IStepperItemBehavior allensTestBehavior() {
        return new IStepperItemBehavior() {
            @Override
            public void init() {
                setParams(StepperType.VS_RADIO, StepperHintType.VS_HINT_ENTERORSELECT,
                        "", StringResourceValues.getStringValues(context, StringResourceValues.getValues(StringResourceValues.setAllensTest())));
            }

            @Override
            public String subtitle(TestUIDriver testUIDriver) {
                return context.getString(StringResourceValues.setEpocTestFieldType().get(testFieldType.value));
            }

            @Override
            public IStepperValue load(TestUIDriver testUIDriver) {
                try {
                    return new ArrayStepperValue(testUIDriver.getTestData().getRespiratoryDetail().getRespAllensType().value);
                } catch(Exception e) {
                    return new NoStepperValue();
                }
            }

            @Override
            public void save(TestUIDriver testUIDriver, Object data) {
                AllensTest allensTest = (AllensTest)getTransformer().transform(data);
                testUIDriver.getTestData().getRespiratoryDetail().setRespAllensType(allensTest);
            }

            @Override
            public IStepperValueTransformer getTransformer() {
                return new IStepperValueTransformer() {
                    @Override
                    public Object transform(Object data) {
                        try {
                            return AllensTest.fromInt((Integer) data);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return AllensTest.ENUM_UNINITIALIZED;
                    }
                };
            }

            @Override
            public IStepperValueValidator getValidator() {
                return new IStepperValueValidator() {
                    @Override
                    public boolean validate(Object data) {
                        if (Validators.isDefaultValue(data,-1))
                            return true;
                        AllensTest value = (AllensTest)getTransformer().transform(data);
                        boolean result = value != AllensTest.ENUM_UNINITIALIZED;
                        if (result == false)
                            Log.d(TAG,String.format("Validation failed for type: %s -- value: %s",getTestFieldType(),data));
                        return result;
                    }
                };
            }
        };
    }

    private IStepperItemBehavior drawSitesBehavior() {
        return new IStepperItemBehavior() {
            @Override
            public void init() {
                setParams(StepperType.VS_COMBO, StepperHintType.VS_HINT_ENTERORSELECT,
                        "", StringResourceValues.getStringValues(context, StringResourceValues.getValues(StringResourceValues.setDrawSite())));
            }

            @Override
            public String subtitle(TestUIDriver testUIDriver) {
                return context.getString(StringResourceValues.setEpocTestFieldType().get(testFieldType.value));
            }

            @Override
            public IStepperValue load(TestUIDriver testUIDriver) {
                try {
                    return new ArrayStepperValue(testUIDriver.getTestData().getRespiratoryDetail().getDrawSite().value);
                } catch(Exception e) {
                    return new NoStepperValue();
                }
            }

            @Override
            public void save(TestUIDriver testUIDriver, Object data) {
                DrawSites drawSite = (DrawSites)getTransformer().transform(data);
                testUIDriver.getTestData().getRespiratoryDetail().setDrawSite(drawSite);
            }

            @Override
            public IStepperValueTransformer getTransformer() {
                IStepperValueTransformer enumTypeTransformer = new IStepperValueTransformer() {
                    @Override
                    public Object transform(Object data) {
                        try {
                            return DrawSites.fromInt((Integer) data);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return DrawSites.ENUM_UNINITIALIZED;
                    }
                };
                return Transformers.getStringToFieldTypeTransformer(StringResourceValues.setDrawSite(), enumTypeTransformer, context);
            }

            @Override
            public IStepperValueValidator getValidator() {
                return new IStepperValueValidator() {
                    @Override
                    public boolean validate(Object data) {
                        boolean result = false;
                        try {
                            if (Validators.isDefaultValue(data, ""))
                                return true;

                            DrawSites value = (DrawSites) getTransformer().transform(data);
                            result = value != DrawSites.ENUM_UNINITIALIZED;
                        } catch (Exception e) {}
                        if (result == false)
                            Log.d(TAG,String.format("Validation failed for type: %s -- value: %s",getTestFieldType(),data));
                        return result;
                    }
                };
            }
        };
    }

    private IStepperItemBehavior yesnoBehavior(final EpocTestFieldType testFieldType) {
        return new IStepperItemBehavior() {
            @Override
            public void init() {
                setParams(StepperType.VS_RADIO, StepperHintType.VS_HINT_ENTER,
                        "", context.getResources().getStringArray(R.array.yes_no));
            }

            @Override
            public String subtitle(TestUIDriver testUIDriver) {
                IStepperValue value = load(testUIDriver);
                if (value instanceof NoStepperValue)
                    return "n/a";
                else
                    return ((Integer)value.getValue()) == 1?"Yes":"No"; //TODO: localize
            }

            @Override
            public IStepperValue load(TestUIDriver testUIDriver) {
                try {
                    Boolean value = false;
                    switch (testFieldType) {
                        case HEMODILUTION:
                            value = testUIDriver.getTestData().getTestDetail().getHemodilutionApplied();
                            break;
                        case READBACK:
                            value = testUIDriver.getTestData().getTestDetail().getReadBack();
                            break;
                        case REJECTTEST:
                            value = testUIDriver.getTestData().getRejected();
                            break;
                    }
                    if (value != null) {
                        int arrayIndex = -1;
                        if (value == true)
                            arrayIndex = 1;
                        else
                            arrayIndex = 0;
                        return new ArrayStepperValue(arrayIndex);
                    } else {
                        return new NoStepperValue();
                    }
                } catch(Exception e) {
                    return new NoStepperValue();
                }
            }

            @Override
            public void save(TestUIDriver testUIDriver, Object data) {
                Boolean result = (Boolean)getTransformer().transform(data);
                switch(testFieldType) {
                    case HEMODILUTION:
                        testUIDriver.getTestData().getTestDetail().setHemodilutionApplied(result); break;
                    case READBACK:
                        testUIDriver.getTestData().getTestDetail().setReadBack(result); break;
                    case REJECTTEST:
                        testUIDriver.getTestData().setRejected(result); break;
                }
            }

            @Override
            public IStepperValueTransformer getTransformer() {
                return Transformers.getBooleanTransformer();
            }

            @Override
            public IStepperValueValidator getValidator() {
                return Validators.getYesnoValidator(testFieldType != EpocTestFieldType.HEMODILUTION);
            }
        };
    }

    private IStepperItemBehavior patientTemperatureBehavior() {
        return new IStepperItemBehavior() {
            @Override
            public void init() {
                setParams(StepperType.VS_INTEGER, StepperHintType.VS_HINT_ENTER,
                        context.getString(R.string.error_patient_temperature), null);
            }

            @Override
            public String subtitle(TestUIDriver testUIDriver) {
                return context.getString(StringResourceValues.setEpocTestFieldType().get(testFieldType.value));
            }

            @Override
            public IStepperValue load(TestUIDriver testUIDriver) {
                try {
                    return new StringStepperValue(Objects.toString(testUIDriver.getTestData().getTestDetail().getPatientTemperature(),""));
                } catch(Exception e) {
                    return new NoStepperValue();
                }
            }

            @Override
            public void save(TestUIDriver testUIDriver, Object data) {
                Double temperature = (Double)getTransformer().transform(data);
                if(getValidator().validate(temperature)) {
                    if (temperature == -1)
                        testUIDriver.getTestData().getTestDetail().setPatientTemperature(null);
                    else
                        testUIDriver.getTestData().getTestDetail().setPatientTemperature(temperature);
                }
            }

            @Override
            public IStepperValueTransformer getTransformer() {
                return Transformers.getDoubleTransformer();
            }

            @Override
            public IStepperValueValidator getValidator() {
                return Validators.getDoubleValidator(10.0, 44.0, true);
            }
        };
    }

    private IStepperItemBehavior patientAgeBehavior() {
        return new IStepperItemBehavior() {
            @Override
            public void init() {
                setParams(StepperType.VS_INTEGER, StepperHintType.VS_HINT_ENTER,
                        context.getString(R.string.error_patient_age), null);
            }

            @Override
            public String subtitle(TestUIDriver testUIDriver) {
                return context.getString(StringResourceValues.setEpocTestFieldType().get(testFieldType.value));
            }

            @Override
            public IStepperValue load(TestUIDriver testUIDriver) {
                try {
                    Integer age = testUIDriver.getTestData().getPatientAge();
                    if (age != null && age >= 0)
                        return new StringStepperValue(age.toString());
                    else
                        return new NoStepperValue();
                } catch(Exception e) {
                    return new NoStepperValue();
                }
            }

            @Override
            public void save(TestUIDriver testUIDriver, Object data) {
                Integer value = (Integer)getTransformer().transform(data);
                testUIDriver.getTestData().setPatientAge(value);
            }

            @Override
            public IStepperValueTransformer getTransformer() {
                return Transformers.getIntegerTransformer();
            }

            @Override
            public IStepperValueValidator getValidator() {
                return Validators.getIntegerValidator(0, 130, true);
            }
        };
    }

    private IStepperItemBehavior patientHeightBehavior() {
        return new IStepperItemBehavior() {
            @Override
            public void init() {
                setParams(StepperType.VS_INTEGER, StepperHintType.VS_HINT_ENTER,
                        "", null);
            }

            @Override
            public String subtitle(TestUIDriver testUIDriver) {
                return context.getString(StringResourceValues.setEpocTestFieldType().get(testFieldType.value));
            }

            @Override
            public IStepperValue load(TestUIDriver testUIDriver) {
                try {
                    /*
                    Integer height = testUIDriver.getTestData().getPatientHeight();
                    if (height != null)
                        return new StringStepperValue(height.toString());
                    else
                    */
                        return new NoStepperValue();
                } catch(Exception e) {
                    return new NoStepperValue();
                }
            }

            @Override
            public void save(TestUIDriver testUIDriver, Object data) {
                Integer value = (Integer)getTransformer().transform(data);
                //testUIDriver.getTestData().setPatientHeight(value);
            }

            @Override
            public IStepperValueTransformer getTransformer() {
                return Transformers.getIntegerTransformer();
            }

            @Override
            public IStepperValueValidator getValidator() {
                return Validators.getIntegerValidator(0, 100000, true);
            }
        };
    }

    private IStepperItemBehavior sampleTypeBehavior() {
        return new IStepperItemBehavior() {
            @Override
            public void init() {
                setParams(StepperType.VS_COMBO, StepperHintType.VS_HINT_ENTERORSELECT,
                        context.getString(R.string.error), StringResourceValues.getStringValues(context, StringResourceValues.getValues(StringResourceValues.setSampleType())));
            }

            @Override
            public String subtitle(TestUIDriver testUIDriver) {
                return context.getString(StringResourceValues.setEpocTestFieldType().get(testFieldType.value));
            }

            @Override
            public IStepperValue load(TestUIDriver testUIDriver) {
                try {
                    return new ArrayStepperValue(testUIDriver.getTestData().getTestDetail().getSampleType().value);
                } catch(Exception e) {
                    return new NoStepperValue();
                }
            }

            @Override
            public void save(TestUIDriver testUIDriver, Object data) {
                BloodSampleType bloodSampleType = (BloodSampleType)getTransformer().transform(data);
                testUIDriver.getTestData().getTestDetail().setSampleType(bloodSampleType);
            }

            @Override
            public IStepperValueTransformer getTransformer() {
                IStepperValueTransformer enumTypeTransformer = new IStepperValueTransformer() {
                    @Override
                    public Object transform(Object data) {
                        BloodSampleType sampleType = BloodSampleType.Unspecified;
                        try {
                            sampleType = BloodSampleType.fromInt((Integer) data);
                            if (sampleType == BloodSampleType.ENUM_UNINITIALIZED)
                                sampleType = BloodSampleType.Unspecified;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return sampleType;
                    }
                };
                return Transformers.getStringToFieldTypeTransformer(StringResourceValues.setSampleType(), enumTypeTransformer, context);
            }

            @Override
            public IStepperValueValidator getValidator() {
                return new IStepperValueValidator() {
                    @Override
                    public boolean validate(Object data) {
                        if (Validators.isDefaultValue(data,""))
                            return true;
                        boolean result = false;
                        try {
                            BloodSampleType value = (BloodSampleType) getTransformer().transform(data);
                            result = value != BloodSampleType.ENUM_UNINITIALIZED;
                            if (result == false)
                                Log.d(TAG, String.format("Validation failed for type: %s -- value: %s", getTestFieldType(), data));
                        } catch (Exception e) {}
                        return result;
                    }
                };
            }
        };
    }

    private IStepperItemBehavior patientId2Behavior() {
        return new IStepperItemBehavior() {
            @Override
            public void init() {
                setParams(StepperType.VS_STRING, StepperHintType.VS_HINT_ENTER,
                        context.getString(R.string.error), null);
            }

            @Override
            public String subtitle(TestUIDriver testUIDriver) {
                return context.getString(StringResourceValues.setEpocTestFieldType().get(testFieldType.value));
            }

            @Override
            public IStepperValue load(TestUIDriver testUIDriver) {
                try {
                    return new StringStepperValue(testUIDriver.getTestData().getId2());
                } catch(Exception e) {
                    return new NoStepperValue();
                }
            }

            @Override
            public void save(TestUIDriver testUIDriver, Object data) {
                String id2 = (String)getTransformer().transform(data);
                testUIDriver.getTestData().setId2(id2);
            }

            @Override
            public IStepperValueTransformer getTransformer() {
                return Transformers.getStringTransformer();
            }

            @Override
            public IStepperValueValidator getValidator() {
                return Validators.getInputFieldValidator(InputFieldType.ID_2, true);
            }
        };
    }

    private IStepperItemBehavior patientIdBehavior() {
        return new IStepperItemBehavior() {
            @Override
            public void init() {
                InputFieldConfig inputFieldConfig = new InputFieldConfigModel().getInputFieldConfig(InputFieldType.PATIENT_ID);
                setParams(StepperType.VS_BARCODE, StepperHintType.VS_HINT_ENTERORSCAN,
                        context.getString(R.string.error_patient_id, inputFieldConfig.getMinimumLength(), inputFieldConfig.getMaximumLength()), null);
            }

            @Override
            public String subtitle(TestUIDriver testUIDriver) {
                return context.getString(StringResourceValues.setEpocTestFieldType().get(testFieldType.value));
            }

            @Override
            public IStepperValue load(TestUIDriver testUIDriver) {
                try {
                    return new StringStepperValue(testUIDriver.getTestData().getSubjectId());
                } catch(Exception e) {
                    return new NoStepperValue();
                }
            }

            @Override
            public void save(TestUIDriver testUIDriver, Object data) {
                String patientId = (String)getTransformer().transform(data);
                testUIDriver.getTestData().setSubjectId(patientId);
            }

            @Override
            public IStepperValueTransformer getTransformer() {
                return Transformers.getStringTransformer();
            }

            @Override
            public IStepperValueValidator getValidator() {
                return Validators.getInputFieldValidator(InputFieldType.PATIENT_ID, false);
            }
        };
    }

    private IStepperItemBehavior lotNumberBehavior() {
        return new IStepperItemBehavior() {
            @Override
            public void init() {
                InputFieldConfig inputFieldConfig = new InputFieldConfigModel().getInputFieldConfig(InputFieldType.PATIENT_OR_LOT_ID);
                setParams(StepperType.VS_BARCODE, StepperHintType.VS_HINT_ENTERORSCAN,
                        context.getString(R.string.error_patientorlot_id, inputFieldConfig.getMinimumLength(), inputFieldConfig.getMaximumLength()), null);
            }

            @Override
            public String subtitle(TestUIDriver testUIDriver) {
                return context.getString(StringResourceValues.setEpocTestFieldType().get(testFieldType.value));
            }

            @Override
            public IStepperValue load(TestUIDriver testUIDriver) {
                try {
                    return new StringStepperValue(testUIDriver.getTestData().getSubjectId());
                } catch(Exception e) {
                    return new NoStepperValue();
                }
            }

            @Override
            public void save(TestUIDriver testUIDriver, Object data) {
                String patientId = (String)getTransformer().transform(data);
                testUIDriver.getTestData().setSubjectId(patientId);
            }

            @Override
            public IStepperValueTransformer getTransformer() {
                return Transformers.getStringTransformer();
            }

            @Override
            public IStepperValueValidator getValidator() {
                return Validators.getInputFieldValidator(InputFieldType.PATIENT_OR_LOT_ID, false);
            }
        };
    }
    //endregion
}
