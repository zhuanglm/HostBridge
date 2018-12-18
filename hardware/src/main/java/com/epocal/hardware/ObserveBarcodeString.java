package com.epocal.hardware;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * This class listens to the barcode scanner results and updates the classes observing it.
 *
 * Created by Zeeshan A Zakaria on 6/29/2017.
 * (Feb_2018_bm: defectuous singleton definition, example evidence. Reason: one could create n instances of this "singleton")
 */

public class ObserveBarcodeString {

    private static ObserveBarcodeString instance;

    private PublishSubject<String> mBarcodeString = PublishSubject.create();

    /**
     * Singleton
     */
    public static ObserveBarcodeString getInstance() {
        if (instance == null) {
            instance = new ObserveBarcodeString();
        }
        return instance;
    }

    public void setBarcodeString(String barcodeString) {
        mBarcodeString.onNext(barcodeString);
    }

    public Observable<String> getBarcodeString() {
        return mBarcodeString;
    }

}