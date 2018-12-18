package com.epocal.testhistoryfeature.search;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArrayMap;

import com.epocal.datamanager.TestRecordModel;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This class represents the map of Search Criteria to search Test History objects
 * (Realm TestRecord). Filter key name corresponds to the column name of TestRecord object.
 *
 * Search key is a column name and the value is evaluated as equal for the query.
 * (e.g) query.equal("column-name", "value");
 */
public class THSearchFilters implements Parcelable {

    // Column names of TestRecord object
    public static final String ID             = "subjectId";
    public static final String READERSN       = "reader.serialNumber";
    public static final String TESTDATE       = "testDateTime";
    public static final String TESTDATE_FROM  = TestRecordModel.TESTDATE_FROM;
    public static final String TESTDATE_TO    = TestRecordModel.TESTDATE_TO;
    public static final String TESTMODE       = TestRecordModel.TESTMODE;
    public static final String TESTTYPE       = TestRecordModel.TESTTYPE;
    public static final String TESTDATE_RANGE = "TESTDATE_RANGE";
    public static final String PATIENTID      = THSearchFilters.ID;
    public static final String CARDLOT        = "cardLot.lotNumber";
    public static final String SYNC_STATE     = TestRecordModel.SYNC_STATE;

    private static final List<String> mValidFilterKeys = Arrays.asList(PATIENTID, SYNC_STATE, CARDLOT, READERSN, TESTDATE_FROM, TESTDATE_TO);

    protected Map<String, String> mFilters;

    public THSearchFilters() {
        mFilters = new ArrayMap<String, String>();
    }

    public void clearFilters() {
        mFilters.clear();
    }

    public Map<String, String> getFilters() {
        return mFilters;
    }

    public void addFilter(String key, String value) {
        if ((key == null) || (key.isEmpty())) return;
        if ((value == null) || (value.isEmpty())) return;

        if (mValidFilterKeys.contains(key)) {
            mFilters.put(key, value);
        }
    }

    /**
     * List of supported search filter keys
     * The keys correspond to the column names of
     * the TestRecord realm object used for
     * data query.
     * @return -- list of valid filter keys (column names).
     */
    public static List<String> getValidFilterKeys() {
        return mValidFilterKeys;
    }

    /////////////////////////////
    // Parcelable implementation
    /////////////////////////////
    private THSearchFilters(Parcel in) {
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            String key = in.readString();
            String val = in.readString();
            mFilters.put(key, val);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mFilters.size());
        for (Map.Entry<String, String> entry : mFilters.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<THSearchFilters> CREATOR
            = new Creator<THSearchFilters>() {
        @Override
        public THSearchFilters createFromParcel(Parcel in) {
            return new THSearchFilters(in);
        }

        @Override
        public THSearchFilters[] newArray(int size) {
            return new THSearchFilters[size];
        }
    };
}
