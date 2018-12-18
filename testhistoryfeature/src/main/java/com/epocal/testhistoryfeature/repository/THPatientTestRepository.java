package com.epocal.testhistoryfeature.repository;

import android.util.ArrayMap;

import com.epocal.common.realmentities.TestRecord;
import com.epocal.common.types.TestType;
import com.epocal.common.types.am.TestMode;
import com.epocal.datamanager.TestRecordModel;
import com.epocal.testhistoryfeature.THContract;
import com.epocal.testhistoryfeature.search.THSearchFilters;
import com.epocal.testhistoryfeature.search.THSort;

import java.util.List;
import java.util.Map;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * This class is a helper class to interface with Realm Model class
 * to perform db query and update for TestRecord object.
 */
public class THPatientTestRepository implements THContract.Repository<TestRecord> {
    private TestMode mTestMode;
    private TestType mTestType; // Sub-type of TestMode.

    private THSort mDefaultSortParams;

    public THPatientTestRepository() {
        this.mTestMode = TestMode.BloodTest;
        this.mTestType = TestType.Blood;
        setDefaultSortParams();
    }

    private void setDefaultSortParams() {
        this.mDefaultSortParams = new THSort();
    }

    /**
     * Build a map of search criteria Map<String, String> stores "column-name" -> "column-value"
     * with "equalTo" relationship.
     *
     * @param searchFilters -- Map of search criteria to add to the default search criteria.
     * @return Map of search criterion
     */
    private Map<String, String> getSearchCriterion(THSearchFilters searchFilters) {
        ArrayMap<String, String> searchCriterion = new ArrayMap<>();
        if (searchFilters != null) {
            searchCriterion.putAll(searchFilters.getFilters());
        }
        searchCriterion.put(THSearchFilters.TESTMODE, String.valueOf(mTestMode.value));
        searchCriterion.put(THSearchFilters.TESTTYPE, String.valueOf(mTestType.value));
        return searchCriterion;
    }

    /**
     * Fetch TestRecord matching give id
     * @param realm -- Realm instance managed by Activity.
     * @param id -- TestRecord.id
     * @return -- TestRecord (managed object)
     */
    public TestRecord fetch(Realm realm, long id) {
        return TestRecordModel.fetch(realm, id);
    }

    /**
     * Query database for the list of TestRecord with no user specified search filter.
     * Use it to fetch all Patient Tests in Test History.
     *
     * @param realm -- Realm instance managed by Activity.
     * @return RealmResults<TestRecord> -- live results managed by Realm.
     */
    @Override
    public RealmResults<TestRecord> fetchAll(Realm realm) {
        final Map<String, String> searchCriterion = getSearchCriterion(new THSearchFilters());
        final String sortkey = mDefaultSortParams.getSortKey();
        final Sort sortOrder = mDefaultSortParams.isAscending() ? Sort.ASCENDING : Sort.DESCENDING;

        return TestRecordModel.fetch(realm, searchCriterion, sortkey, sortOrder);
    }

    /**
     * Query database for the list of TestRecord with query criteria specified in filters.
     * If filter is null, then use the last saved filter object in this class.
     *
     * NOTE: This method is currently note used but keep it for Advanced Search feature.
     *
     * @param realm -- Realm instance managed by Activity.
     * @param filters -- hold key-value pair of search criteria
     * @param sort -- sort column name and sort order
     * @return RealmResults<TestRecord> -- live results managed by Realm.
     */
    public RealmResults<TestRecord> fetchAll(Realm realm, THSearchFilters filters, THSort sort) {
        final Map<String, String> searchCriterion = getSearchCriterion(filters);
        final String sortKey;
        final Sort   sortOrder;
        if (sort == null) {
            sortKey   = mDefaultSortParams.getSortKey();
            sortOrder = mDefaultSortParams.isAscending() ? Sort.ASCENDING : Sort.DESCENDING;
        } else {
            sortKey = sort.getSortKey();
            sortOrder = sort.isAscending() ? Sort.ASCENDING : Sort.DESCENDING;
        }
        return TestRecordModel.fetch(realm, searchCriterion, sortKey, sortOrder);
    }

    @Override
    public RealmResults<TestRecord> fetchBySyncState(Realm realm, boolean isSent) {
        final String sortkey = mDefaultSortParams.getSortKey();
        final Sort sortOrder = mDefaultSortParams.isAscending() ? Sort.ASCENDING : Sort.DESCENDING;
        return TestRecordModel.fetchBySyncState(realm, isSent, false, sortkey, sortOrder);
    }

    public RealmResults<TestRecord> fetchMatchAny(Realm realm, String partialString) {
        final String sortkey = mDefaultSortParams.getSortKey();
        final Sort sortOrder = mDefaultSortParams.isAscending() ? Sort.ASCENDING : Sort.DESCENDING;
        return TestRecordModel.fetchMatchAnyPatientBloodTest(realm, partialString, sortkey, sortOrder);
    }

    @Override
    public void delete(Realm realm, List<Long> idList,
                               Realm.Transaction.OnSuccess onSuccess,
                               Realm.Transaction.OnError onError)
    {
        TestRecordModel.delete(realm, idList.toArray(new Long[idList.size()]), onSuccess, onError);
    }

    @Override
    public void markSent(Realm realm, List<Long> idList,
                            Realm.Transaction.OnSuccess onSuccess,
                            Realm.Transaction.OnError onError)
    {
        TestRecordModel.updateSyncState(realm, true, idList.toArray(new Long[idList.size()]), onSuccess, onError);
    }

    @Override
    public void markUnsent(Realm realm, List<Long> idList,
                              Realm.Transaction.OnSuccess onSuccess,
                              Realm.Transaction.OnError onError)
    {
        TestRecordModel.updateSyncState(realm, false, idList.toArray(new Long[idList.size()]), onSuccess, onError);
    }
}
