package com.epocal.testhistoryfeature;

import android.util.Log;

import com.epocal.testhistoryfeature.data.type.SelectionType;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.realm.Realm;

/**
 * This class implements Presenter for Main Screen.
 */
public class THPresenter implements THContract.Presenter {
    private static final String TAG = THPresenter.class.getSimpleName();

    private THContract.Model mModel;
    private THContract.View  mView;

    public THPresenter(THContract.Model mModel, THContract.View mView) {
        this.mModel = mModel;
        this.mView = mView;
    }

    @Override
    public void load() {
        mView.showSearchResult(mModel.fetchPatientTests(), null);
    }

    @Override
    public void load(boolean isQATest) {
        mModel.setTestHistoryDataType(isQATest);
        load();
    }

    @Override
    public void loadBySyncState(boolean isSent) {
        mView.showSearchResult(mModel.fetchBySyncState(isSent), null);
    }

    @Override
    public void loadMatchingPartialString(String partialStringToMatch) {
        mView.showSearchResult(mModel.fetchMatchAny(partialStringToMatch), partialStringToMatch);
    }

    @Override
    public void observeSearchViewInput(Observable<String> searchViewObservable) {

        searchViewObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String partialStringToMatch) {
                Log.v(TAG, "onNext = " + partialStringToMatch);
                // TODO: This gets called with partialstringToMatch="" right after SearchView collapsed.
                // TODO: Need to stop subscribe at that point.
                if ((partialStringToMatch == null) || (partialStringToMatch.length() <= 2)) {
                    mView.showSearchResult(null, null);
                } else {
                    loadMatchingPartialString(partialStringToMatch);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onTestSelected(long id) {
        mView.showTestDetails(id);
    }

    public void onTestSelectedForMultiselectMode(long id) {
        mView.showMultiSelectSearchResult(id);
    }

    @Override
    public void onHeaderSelectedForMultiselectMode(int dateRangeValue) {
        mView.showMultiSelectSearchResultWithDateRange(dateRangeValue);
    }

    @Override
    public void onSearchResultsSelectedForMultiselectMode(SelectionType selectionType) {
        mView.showMultiSelectSearchResultWithSelectionType(selectionType);

    }

    @Override
    public void actionDeleteAll(List<Long> idList, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError) {
        mModel.delete(idList, onSuccess, onError);
    }

    @Override
    public void actionSetSentAll(List<Long> idList, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError) {
        mModel.markSent(idList, onSuccess, onError);
    }

    @Override
    public void actionSetUnsentAll(List<Long> idList, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError) {
        mModel.markUnsent(idList, onSuccess, onError);
    }

}
