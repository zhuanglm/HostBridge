package com.epocal.epoctest.testprocess;

import android.util.Log;

import com.epocal.common.androidutil.RxUtil;
import com.epocal.common.realmentities.TestRecord;
import com.epocal.epoctest.di.component.DaggerTestProcessComponent;
import com.epocal.epoctest.di.component.TestProcessComponent;
import com.epocal.epoctest.di.module.TestProcessModule;

import javax.inject.Inject;

/**
 * Created by dning on 10/17/2017.
 */

public class PerformTestCalculation {
    // TODO: Remove later.
    // TODO: This is a debug flag to enable test calculation from previously saved Analytical Manager input buf files.
    // TODO: When set it to true, mCooxTestCalculation.calculateTestResultFromAnalyticalManagerFile() is called
    // TODO: instead of regular method.
    // TODO: Use it with Analytical Manager's flags: READ_REQUEST_FROM_FILE_CALCULATE_BGE = true and READ_REQUEST_FROM_FILE_CALCULATE_COOX_RESULTS = true
    // TODO: Use it for DEBUGGING ONLY!
    private static final boolean CALCULATE_TEST_FROM_ANALYTICAL_MANAGER_FILE = false;

    @Inject
    BGETestCalculation mBGETestCalculation;

    private boolean mCalculating = false;
    private TestStateDataObject mTestStateDataObject;

    private com.epocal.common.androidutil.RxUtil<TestRecord> mRxUtilStatus;

    public com.epocal.common.androidutil.RxUtil<TestRecord> getRxUtilStatus() {
        return mRxUtilStatus;
    }

    public void setRxUtilStatus(com.epocal.common.androidutil.RxUtil<TestRecord> mRxUtilStatus) {
        this.mRxUtilStatus = mRxUtilStatus;
    }

    public void unsubscribe() {
        mRxUtilStatus.unsubscribe();
    }

    public PerformTestCalculation(TestStateDataObject testStateDataObject) {
        mRxUtilStatus = new RxUtil().create(null);
        mTestStateDataObject = testStateDataObject;
        TestProcessComponent bc = DaggerTestProcessComponent.builder()
                .testProcessModule(new TestProcessModule())
                .build();
        bc.inject(this);
    }

    private class CalculationWorker extends Thread {
        @Override
        public void run() {
            if (mCalculating) {
                return;
            }
            try {
                mCalculating = true;
                mTestStateDataObject.getTestDataProcessor().prepareDataBeforeCalculation();
                boolean calculatedSuccess = true;
                if (!mBGETestCalculation.calculateTestResult(mTestStateDataObject.getTestDataProcessor())) {
                    calculatedSuccess = false;//do something when failed during the test calculation
                    Log.d("BGECalculation", "Failed");
                }

                mTestStateDataObject.getTestDataProcessor().prepareResultAfterCalculation(mTestStateDataObject);
                mRxUtilStatus.onNext(mTestStateDataObject.getTestDataProcessor().getTestRecord());
            } catch (Exception e) {
                Log.d("Test Calculation", e.getMessage());
                e.printStackTrace();
            } finally {
                mCalculating = false;
            }
        }
    }

    public void startCalculation() {
        CalculationWorker calculationWorker = new CalculationWorker();
        new Thread(calculationWorker).start();
    }

    public void stopCalculation() {
        mBGETestCalculation.terminateCalculation();
    }
}
