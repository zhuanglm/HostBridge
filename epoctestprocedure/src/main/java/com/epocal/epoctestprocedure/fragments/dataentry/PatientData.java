package com.epocal.epoctestprocedure.fragments.dataentry;

import android.util.Log;

import com.epocal.common.CU;
import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.types.EpocTestFieldGroupType;
import com.epocal.common.types.EpocTestFieldType;
import com.epocal.common.types.am.DrawSites;
import com.epocal.common.types.am.Gender;

import java.util.Date;
import java.util.HashMap;

/**
 * This is a model object which has all the test data related to a patient which is displayed
 * in the Stepper. Updates to this data is done via the Stepper
 * <p>
 * Created by Zeeshan A Zakaria on 10/16/2017.
 */

class PatientData {

    // Using no getters or setters for clarity sake. Also for better readability not using 'm'
    // prefix with member variables and using 'this' keyword instead.

    String age;
    String id;
    String id2;
    String temperature;
    String fi02;
    String respiratoryMode;
    String vt;
    String rr;
    String tr;
    String peep;
    String ps;
    String it;
    String et;
    String pip;
    String map;
    String flow;
    String hertz;
    String deltap;
    String noppm;
    String rq;
    String comments;
    String location;
    String collectedBy;
    String collectionDate;
    String collectionTime;
    String orderingPsysician;
    String orderDate;
    String orderTime;
    String drawSite;
    String sampleType;
    String deliverySystem;
    String notifyAction;
    String notifyName;
    String notifyDate;
    String notifyTime;
    Integer allensTest;
    Integer gender;
    Integer hemodilution;
    String lotNumber;
    String fluidType;
    String testType;
    private HashMap<EpocTestFieldType, String> fieldValues;

    PatientData(TestRecord testRecord) {
        this.id = nullCheck(testRecord.getSubjectId());
        this.hemodilution = nullCheck(testRecord.getTestDetail().getHemodilutionApplied());
        this.id2 = nullCheck(testRecord.getId2());
        this.temperature = nullCheck(testRecord.getTestDetail().getPatientTemperature());
        this.gender = validateGender(testRecord.getGender());
        this.age = age(nullCheck(testRecord.getPatientAge()));
        this.allensTest = testRecord.getRespiratoryDetail().getRespAllensType().value;
        this.fi02 = testRecord.getRespiratoryDetail().getFiO2();
        this.respiratoryMode = getMode(testRecord);
        this.vt = testRecord.getRespiratoryDetail().getTidalVolume();
        this.rr = testRecord.getRespiratoryDetail().getRespiratoryRate();
        this.tr = testRecord.getRespiratoryDetail().getTotalRespiratoryRate();
        this.peep = testRecord.getRespiratoryDetail().getPeep();
        this.ps = testRecord.getRespiratoryDetail().getPs();
        this.it = testRecord.getRespiratoryDetail().getInspiratoryTime();
        this.et = testRecord.getRespiratoryDetail().getExpiratoryTime();
        this.pip = testRecord.getRespiratoryDetail().getPeakInspiratoryPressure();
        this.map = testRecord.getRespiratoryDetail().getMeanAirWayPressure();
        this.flow = testRecord.getRespiratoryDetail().getFlow();
        this.hertz = testRecord.getRespiratoryDetail().getHertz();
        this.deltap = testRecord.getRespiratoryDetail().getDeltaP();
        this.noppm = testRecord.getRespiratoryDetail().getNoPPM();
        this.rq = testRecord.getRespiratoryDetail().getRq();
        this.comments = testRecord.getTestDetail().getComment();
        this.location = testRecord.getTestDetail().getPatientLocation();
        this.collectedBy = testRecord.getTestDetail().getCollectedBy();
        this.collectionDate = getDate(testRecord.getTestDetail().getCollectionDate());
        this.collectionTime = getTime(testRecord.getTestDetail().getCollectionTime());
        this.orderingPsysician = testRecord.getTestDetail().getOrderingPhysician();
        this.orderDate = getDate(testRecord.getTestDetail().getOrderDate());
        this.orderTime = getTime(testRecord.getTestDetail().getOrderTime());
        this.sampleType = testRecord.getTestDetail().getSampleType().name();
        this.drawSite = getDrawSite(testRecord);
        this.deliverySystem = getDeliverySystem(testRecord);
        this.notifyAction = getNotifyAction(testRecord);
        this.notifyName = testRecord.getTestDetail().getNotifyName();
        this.notifyDate = getDate(testRecord.getTestDetail().getNotifyDate());
        this.notifyTime = getTime(testRecord.getTestDetail().getNotifyTime());
        this.lotNumber = nullCheck(testRecord.getCardLot().getLotNumber());
        this.fluidType = nullCheck(testRecord.getTestDetail().getQaSampleInfo().getName());
        this.testType = getType(testRecord);
        fieldValues = new HashMap<>();
        createFieldValueHashMap();
    }

    private String age(String age) {
        if (age.equals("-1")) return "";
        return age;
    }

    private String getDrawSite(TestRecord testRecord) {
        String value = testRecord.getRespiratoryDetail().getDrawSite().name();
        if (value.equals(DrawSites.ENUM_UNINITIALIZED.name())) {
            value = "";
        }
        return value;
    }

    private String getDeliverySystem(TestRecord testRecord) {
        String value = testRecord.getRespiratoryDetail().getDeliverySystem().name();
        if (value.equals(DrawSites.ENUM_UNINITIALIZED.name())) {
            value = "";
        }
        return value;
    }

    private String getNotifyAction(TestRecord testRecord) {
        return testRecord.getTestDetail().getNotifyActionType().name();
    }

    private String getMode(TestRecord testRecord) {
        String value = testRecord.getRespiratoryDetail().getRespiratoryMode().toString();
        if (value.equals(DrawSites.ENUM_UNINITIALIZED.name())) {
            value = "";
        }
        return value;
    }

    private String getType(TestRecord testRecord) {
        String value = testRecord.getType().toString();
        return value;
    }

    /**
     * HashMap of the field value Enums and their values
     */
    private void createFieldValueHashMap() {
        fieldValues.put(EpocTestFieldType.PATIENTID, nullCheck(id));
        fieldValues.put(EpocTestFieldType.HEMODILUTION, nullCheck(hemodilution));
        fieldValues.put(EpocTestFieldType.PATIENTID2, nullCheck(id2));
        fieldValues.put(EpocTestFieldType.PATIENTTEMPERATURE, nullCheck(temperature));
//        fieldValues.put(EpocTestFieldType.PATIENTGENDER, gender);
        fieldValues.put(EpocTestFieldType.PATIENTAGE, nullCheck(age));
//        fieldValues.put(EpocTestFieldType.ALLENSTEST, allensTest);
//        fieldValues.put(EpocTestFieldType.TESTSELECTION, testSelection);
        fieldValues.put(EpocTestFieldType.FIO2, fi02);
        fieldValues.put(EpocTestFieldType.MODE, respiratoryMode);
        fieldValues.put(EpocTestFieldType.VT, vt);
        fieldValues.put(EpocTestFieldType.RR, rr);
        fieldValues.put(EpocTestFieldType.TR, tr);
        fieldValues.put(EpocTestFieldType.PEEP, peep);
        fieldValues.put(EpocTestFieldType.PS, ps);
        fieldValues.put(EpocTestFieldType.IT, it);
        fieldValues.put(EpocTestFieldType.ET, et);
        fieldValues.put(EpocTestFieldType.PIP, pip);
        fieldValues.put(EpocTestFieldType.MAP, map);
        fieldValues.put(EpocTestFieldType.FLOW, flow);
        fieldValues.put(EpocTestFieldType.HERTZ, hertz);
        fieldValues.put(EpocTestFieldType.DELTAP, deltap);
        fieldValues.put(EpocTestFieldType.NOPPM, noppm);
        fieldValues.put(EpocTestFieldType.RQ, rq);
        fieldValues.put(EpocTestFieldType.COMMENTS, comments);
        fieldValues.put(EpocTestFieldType.PATIENTLOCATION, location);
        fieldValues.put(EpocTestFieldType.COLLECTEDBY, collectedBy);
        fieldValues.put(EpocTestFieldType.COLLECTIONDATE, collectionDate);
        fieldValues.put(EpocTestFieldType.COLLECTIONTIME, collectionTime);
        fieldValues.put(EpocTestFieldType.ORDERINGPHYSICIAN, orderingPsysician);
        fieldValues.put(EpocTestFieldType.ORDERDATE, orderDate);
        fieldValues.put(EpocTestFieldType.ORDERTIME, orderTime);
        fieldValues.put(EpocTestFieldType.LOTNUMBER, lotNumber);
        fieldValues.put(EpocTestFieldType.FLUIDTYPE, fluidType);
        fieldValues.put(EpocTestFieldType.TESTTYPE, testType);
    }

    public String[] getData() {
        String[] data = new String[10];

        data[0] = id;

        return data;
    }

    /**
     * This method takes a String value as an input and returns an empty String if it is null.
     *
     * @param input the input value
     * @return the output value
     */
    private String nullCheck(String input) {
        String output = "";
        if (input != null) {
            output = input;
        }

        return output;
    }

    /**
     * This method takes a Boolean value as an input and returns a true value if it is null.
     *
     * @param input the input value
     * @return the output value
     */
    private Integer nullCheck(Boolean input) {
        Integer output = null;
        if (input != null) {
            output = (input ? 1 : 0);
        }

        return output;
    }

    /**
     * This method takes an Integer value as an input and returns an empty String if it is null.
     *
     * @param input the input value
     * @return the output value
     */
    private String nullCheck(Integer input) {
        String output = "";
        if (input != null) {
            output = Integer.toString(input);
        }

        return output;
    }

    /**
     * This method takes a Double value as an input and returns an empty String if it is null.
     *
     * @param input the input value
     * @return the output value
     */
    private String nullCheck(Double input) {
        String output = "";
        if (input != null) {
            output = Double.toString(input);
        }

        return output;
    }

    /**
     * Return the correct value for the gender or return null for the unspecified
     *
     * @param gender the enum value for the Gender type
     * @return the male or female, or null for unspecified
     */
    private Integer validateGender(Gender gender) {
        int value = gender.value;

        if (value > 1) {
            return null;
        } else {
            return value;
        }
    }

    /**
     * Get the subtitle value for the stepper headers
     *
     * @param value the Enum value
     * @return the returned String
     */
    String getSubtitleValue(int value, boolean isMandatory) {

        if (isMandatory) {
            switch (EpocTestFieldType.fromInt(value)) {
                case HEMODILUTION:
                    if (hemodilution == null) {
                        return "n/a"; // TODO: Strings to be localized
                    } else {
                        return (hemodilution == 1 ? "Yes" : "No"); // TODO: Strings to be localized
                    }
                default:
                    Log.d("", EpocTestFieldType.fromInt(value).toString());
                    return fieldValues.get(EpocTestFieldType.fromInt(value));
            }
        } else {
            switch (EpocTestFieldGroupType.fromInt(value)) {
                case COMPLIANCE:
                    return "Required by the Hospital Policy"; // TODO: Strings to be localized
//                case TESTSELECTION_OPTIONAL:
//                    return getSelectedTestSelection();
                default:
                    return "Optional"; // TODO: Strings to be localized
            }
        }
    }

    /**
     * Format the date if not a null object, if null, return an empty string
     *
     * @param date input date
     * @return the formatted time
     */
    private String getDate(Date date) {
        return (date == null) ? "" : CU.dateToEpocDateString(date);
    }

    /**
     * Change to an empty string when the input is a null object, else, return as is.
     *
     * @param time the input time
     * @return time string
     */
    private String getTime(String time) {
        return (time == null) ? "" : time;
    }
}
