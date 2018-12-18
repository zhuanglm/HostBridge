package com.epocal.testhistoryfeature.data.patientdata;

import android.util.ArrayMap;

import com.epocal.common.CU;
import com.epocal.common.realmentities.QASampleInfo;
import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.types.EpocTestFieldType;
import com.epocal.common.types.NotifyActionType;
import com.epocal.common.types.TestType;
import com.epocal.common.types.am.AllensTest;
import com.epocal.common.types.am.BloodSampleType;
import com.epocal.common.types.am.DeliverySystem;
import com.epocal.common.types.am.DrawSites;
import com.epocal.common.types.am.Gender;
import com.epocal.common.types.am.RespiratoryMode;

import java.util.Date;

/**
 * This class represents Model object for Patient data.
 * It is a POJO to hold Patient Blood Test input data parsed from TestRecord.
 *
 * <h4>Note:</h4>
 * <p>
 * For convenience, the mapping from EpocTestFieldType to string representation of
 * corresponding value is provided for the value which does not require the localization.
 *
 * (e.g) id (subjectId) is a user inputted sequence of characters and does not require the localization.
 *
 * In the mapping, Boolean value is represented as follows and the caller is expected to localize the value.
 * Boolean is represented as: null -> an empty string, true -> "1", false -> "0".
 *
 * In the mapping, Integer and Double value is represented as follows and the caller is expected to localize the value.
 * Integer is represented as: null -> an empty string, Integer -> integerObject.toString()
 * Double is represented as: null -> an empty string, Double -> doubleObject.toString()
 *
 * In the mapping, Date value is represented as follows.
 * Date is represented as: null -> an empty string, CU.dateToEpocDateString(dateObject);
 *
 * Enum type values are not in the mapping. The caller must localize the value to string.
 * </p>
 */
public class PatientTestInputData {
    public String id;
    public String id2;
    public Boolean hemodilution;
    public Double temperature;
    public Gender gender;                 // Enum
    public Integer age;
    public AllensTest allensTest;             // Enum
    public String fi02;
    public RespiratoryMode respiratoryMode;   // Enum
    public String vt;
    public String rr;
    public String tr;
    public String peep;
    public String ps;
    public String it;
    public String et;
    public String pip;
    public String map;
    public String flow;
    public String hertz;
    public String deltap;
    public String noppm;
    public String rq;
    public String comments;
    public String location;
    public String collectedBy;
    public Date collectionDate;
    public String collectionTime;
    public String orderingPsysician;
    public Date orderDate;
    public String orderTime;
    public Boolean readBack;
    public Boolean isRejected;
    public BloodSampleType sampleType;     // Enum
    public DrawSites drawSite;             // Enum

    public DeliverySystem deliverySystem;  // Enum
    public NotifyActionType notifyAction;  // Enum
    public String notifyName;
    public Date notifyDate;
    public String notifyTime;
    public String lotNumber;
    public QASampleInfo fluidType;    // Enum
    public TestType testType;            // Enum

    public ArrayMap<EpocTestFieldType, String> testFieldTypeToValueMap;

    public PatientTestInputData(TestRecord testRecord) {
        this.id = testRecord.getSubjectId();
        this.id2 = testRecord.getId2();
        this.hemodilution = testRecord.getTestDetail().getHemodilutionApplied();
        this.temperature = testRecord.getTestDetail().getPatientTemperature();
        this.gender = testRecord.getGender();
        this.age = testRecord.getPatientAge();
        this.allensTest = testRecord.getRespiratoryDetail().getRespAllensType();
        this.fi02 = testRecord.getRespiratoryDetail().getFiO2();
        this.respiratoryMode = testRecord.getRespiratoryDetail().getRespiratoryMode();
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
        this.collectionDate = testRecord.getTestDetail().getCollectionDate();
        this.collectionTime = testRecord.getTestDetail().getCollectionTime();
        this.orderingPsysician = testRecord.getTestDetail().getOrderingPhysician();
        this.orderDate = testRecord.getTestDetail().getOrderDate();
        this.orderTime = testRecord.getTestDetail().getOrderTime();
        this.sampleType = testRecord.getTestDetail().getSampleType();
        this.drawSite = testRecord.getRespiratoryDetail().getDrawSite();
        this.deliverySystem = testRecord.getRespiratoryDetail().getDeliverySystem();
        this.notifyAction = testRecord.getTestDetail().getNotifyActionType();
        this.notifyName = testRecord.getTestDetail().getNotifyName();
        this.notifyDate = testRecord.getTestDetail().getNotifyDate();
        this.notifyTime = testRecord.getTestDetail().getNotifyTime();
        this.lotNumber = testRecord.getCardLot().getLotNumber();
        this.fluidType = testRecord.getTestDetail().getQaSampleInfo();
        this.testType = testRecord.getType();
        this.readBack = testRecord.getTestDetail().getReadBack();
        this.isRejected = testRecord.getRejected();

        testFieldTypeToValueMap = new ArrayMap<>();
        createFieldValueHashMap();
    }

    /**
     * Map of tEpocTestFieldType Enums to its corresponding value in String representation.
     * If String value holds null, then it is replaced by an empty string.
     */
    private void createFieldValueHashMap() {
        testFieldTypeToValueMap.put(EpocTestFieldType.PATIENTID, nullCheck(id));
        testFieldTypeToValueMap.put(EpocTestFieldType.PATIENTID2, nullCheck(id2));
        testFieldTypeToValueMap.put(EpocTestFieldType.PATIENTTEMPERATURE, nullCheck(temperature));
        testFieldTypeToValueMap.put(EpocTestFieldType.PATIENTAGE, nullCheck(age));
        testFieldTypeToValueMap.put(EpocTestFieldType.PATIENTLOCATION, nullCheck(location));

        testFieldTypeToValueMap.put(EpocTestFieldType.FIO2, nullCheck(fi02));
        testFieldTypeToValueMap.put(EpocTestFieldType.VT, nullCheck(vt));
        testFieldTypeToValueMap.put(EpocTestFieldType.RR, nullCheck(rr));
        testFieldTypeToValueMap.put(EpocTestFieldType.TR, nullCheck(tr));
        testFieldTypeToValueMap.put(EpocTestFieldType.PEEP, nullCheck(peep));
        testFieldTypeToValueMap.put(EpocTestFieldType.PS, nullCheck(ps));
        testFieldTypeToValueMap.put(EpocTestFieldType.IT, nullCheck(it));
        testFieldTypeToValueMap.put(EpocTestFieldType.ET, nullCheck(et));
        testFieldTypeToValueMap.put(EpocTestFieldType.PIP, nullCheck(pip));
        testFieldTypeToValueMap.put(EpocTestFieldType.MAP, nullCheck(map));
        testFieldTypeToValueMap.put(EpocTestFieldType.FLOW, nullCheck(flow));
        testFieldTypeToValueMap.put(EpocTestFieldType.HERTZ, nullCheck(hertz));
        testFieldTypeToValueMap.put(EpocTestFieldType.DELTAP, nullCheck(deltap));
        testFieldTypeToValueMap.put(EpocTestFieldType.NOPPM, nullCheck(noppm));
        testFieldTypeToValueMap.put(EpocTestFieldType.RQ, nullCheck(rq));
        testFieldTypeToValueMap.put(EpocTestFieldType.COMMENTS, nullCheck(comments));
        testFieldTypeToValueMap.put(EpocTestFieldType.ORDERINGPHYSICIAN, nullCheck(orderingPsysician));
        testFieldTypeToValueMap.put(EpocTestFieldType.COLLECTEDBY, nullCheck(collectedBy));
        testFieldTypeToValueMap.put(EpocTestFieldType.NOTIFYNAME, nullCheck(notifyName));
        testFieldTypeToValueMap.put(EpocTestFieldType.LOTNUMBER, nullCheck(lotNumber));

        // Boolean type
        testFieldTypeToValueMap.put(EpocTestFieldType.HEMODILUTION, nullCheck(hemodilution));
        testFieldTypeToValueMap.put(EpocTestFieldType.REJECTTEST, nullCheck(isRejected));
        testFieldTypeToValueMap.put(EpocTestFieldType.READBACK, nullCheck(readBack));

        // Date type
        testFieldTypeToValueMap.put(EpocTestFieldType.COLLECTIONDATE, getDate(collectionDate));
        testFieldTypeToValueMap.put(EpocTestFieldType.ORDERDATE, getDate(orderDate));
        testFieldTypeToValueMap.put(EpocTestFieldType.NOTIFYDATE, getDate(notifyDate));

        // Time
        testFieldTypeToValueMap.put(EpocTestFieldType.COLLECTIONTIME, getTime(collectionTime));
        testFieldTypeToValueMap.put(EpocTestFieldType.ORDERTIME, getTime(orderTime));
        testFieldTypeToValueMap.put(EpocTestFieldType.NOTIFYTIME, getTime(notifyTime));

        // Enum cannot be String-tized because it requires the localization
//        testFieldTypeToValueMap.put(EpocTestFieldType.PATIENTGENDER, gender);
//        testFieldTypeToValueMap.put(EpocTestFieldType.ALLENSTEST, allensTest);
//        testFieldTypeToValueMap.put(EpocTestFieldType.MODE, respiratoryMode);
//        testFieldTypeToValueMap.put(EpocTestFieldType.FLUIDTYPE, fluidType);
//        testFieldTypeToValueMap.put(EpocTestFieldType.TESTTYPE, testType);
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
     * This method takes a Boolean value as an input and returns a string representation of value
     * as follows:
     *
     * - when the value is null: return an empty string.
     * - when the value is true: return a string  "1"
     * - when the value is false: return a string "0"
     *
     * Caller of the method is expected to localize the return value suitable for UI display.
     *
     * @param input the input value
     * @return the output value
     */
    private String nullCheck(Boolean input) {
        String output = "";
        if (input != null) {
            output = input ? "1" : "0";
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