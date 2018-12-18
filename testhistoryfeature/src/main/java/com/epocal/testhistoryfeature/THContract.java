package com.epocal.testhistoryfeature;

import com.epocal.common.realmentities.TestRecord;
import com.epocal.testhistoryfeature.data.type.SelectionType;

import java.util.List;
import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Realm.Transaction.OnSuccess;
import io.realm.Realm.Transaction.OnError;

/**
 * MVP Contract for Test History Main Screen.
 */
public interface THContract {
    interface View {
        void showNoSearchResult();
        void showTestDetails(long id);
        void showMultiSelectSearchResult(long id);
        void showMultiSelectSearchResultWithDateRange(int dateRangeValue);
        void showMultiSelectSearchResultWithSelectionType(SelectionType selectionType);
        void showSearchResult(RealmResults<TestRecord> searchResult, String partialStringToMatch);

    }

    interface Presenter {
        void load(boolean isQATest);
        void load();
        void loadMatchingPartialString(String partialStringToMatch);
        void loadBySyncState(boolean isSent);
        void observeSearchViewInput(Observable<String> searchViewObservable);
        void onTestSelected(long id);
        void onTestSelectedForMultiselectMode(long id);
        void onHeaderSelectedForMultiselectMode(int dateRangeValue);
        void onSearchResultsSelectedForMultiselectMode(SelectionType selectionType);
        void actionDeleteAll(List<Long> idList, OnSuccess onSuccess, OnError onError);
        void actionSetSentAll(List<Long> idList, OnSuccess onSuccess, OnError onError);
        void actionSetUnsentAll(List<Long> idList, OnSuccess onSuccess, OnError onError);
    }

    //com.epocal.common.realmentities.TestResult;
    //com.epocal.common.realmentities.TestRecord;
    //com.epocal.common.realmentities.TestDetail;
    interface Model {
        void setTestHistoryDataType(boolean isQATest);
        RealmResults<TestRecord> fetchMatchAny(String partialString);
        RealmResults<TestRecord> fetchPatientTests();
        RealmResults<TestRecord> fetchBySyncState(boolean isSent);

        // Actions
        void delete(List<Long> idList, OnSuccess onSuccess, OnError onError);
        void markSent(List<Long> idList, OnSuccess onSuccess, OnError onError);
        void markUnsent(List<Long> idList, OnSuccess onSuccess, OnError onError);
    }

    interface Repository<T> {
        T fetch(Realm reaml, long id);
        RealmResults<TestRecord> fetchAll(Realm realm);
        RealmResults<TestRecord> fetchBySyncState(Realm realm, boolean isSent);
        void delete(Realm realm, List<Long> idList, OnSuccess onSuccess, OnError onError);
        void markSent(Realm realm, List<Long> idList, OnSuccess onSuccess, OnError onError);
        void markUnsent(Realm realm, List<Long> idList, OnSuccess onSuccess, OnError onError);
    }
}
