package com.epocal.testhistoryfeature.ui;

import android.support.v7.widget.SearchView;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * This class creates Observable (RxAndroid/RxJava) attaches to SearchView
 * and emits the event when search term text changes.
 *
 * @since 2018-10-06
 *
 * <h4>Note:</h4>
 * <p>
 * It is recommended that the user attaches debounce(), distinctUntilChanged() to
 * minimize the number of text change emit events.
 * </p>
 *
 */
class SearchViewTextObservable {

    static Observable<String> fromView(SearchView searchView) {
        final PublishSubject<String> publishSubject = PublishSubject.create();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                publishSubject.onComplete();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                publishSubject.onNext(newText);
                return true;
            }
        });

        return publishSubject;
    }
}
