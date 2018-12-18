package com.epocal.common.androidutil;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * The generic Rx implementation
 * <p>
 * Created by dning on 5/31/2017.
 */

public class RxUtil<T> {
    public interface Action {
        void invoke();
    }

    private FlowableEmitter<T> mEmitter;
    private Flowable<T> mFlowable;
    private Disposable mDisposable;

    public RxUtil<T> create(final Action doInBackground) {
        mFlowable = Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> emitter) throws Exception {
                mEmitter = emitter;
                if (doInBackground != null) {
                    doInBackground.invoke();
                }
            }
        }, BackpressureStrategy.BUFFER);
        return this;
    }

    public void subscribeOn(Consumer<T> consumer, Scheduler scheduler){
        unsubscribe();
        mDisposable = mFlowable
                .observeOn(scheduler)
                .subscribe(consumer);
    }

    public void subscribe(Consumer<T> subscriber) {
        unsubscribe();
        mDisposable = mFlowable.subscribeOn (Schedulers.newThread()).subscribe(subscriber);
    }

    public void unsubscribe() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    public boolean onNext(T t) {
        if (mEmitter != null && t != null && mEmitter.requested()>0) {
            try {
                mEmitter.onNext(t);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }

    public void onComplete() {
        if (mEmitter != null)
            try {
                mEmitter.onComplete();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
    }
}
