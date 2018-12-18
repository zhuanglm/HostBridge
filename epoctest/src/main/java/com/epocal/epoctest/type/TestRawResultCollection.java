package com.epocal.epoctest.type;

import com.epocal.common.am.FinalResult;

import java.util.ArrayList;

/**
 * Created by dning on 10/19/2017.
 */

public class TestRawResultCollection {
    private ArrayList<FinalResult> mMeasuredResults;
    private ArrayList<FinalResult> mCalculatedResults;
    private ArrayList<FinalResult> mCorrectedResults;
    private boolean mCNCBecauseFailedIQC = false;
    private boolean mFailedIQC = false;

    public ArrayList<FinalResult> getMeasuredResults() {
        if (mMeasuredResults == null) {
            mMeasuredResults = new ArrayList<FinalResult>();
        }
        return mMeasuredResults;
    }

    public ArrayList<FinalResult> getCalculatedResults() {
        if (mCalculatedResults == null) {
            mCalculatedResults = new ArrayList<FinalResult>();
        }
        return mCalculatedResults;
    }

    public ArrayList<FinalResult> getCorrectedResults() {
        if (mCorrectedResults == null) {
            mCorrectedResults = new ArrayList<FinalResult>();
        }
        return mCorrectedResults;
    }

    public void setMeasuredResults(ArrayList<FinalResult> measuredResults) {
        if (mMeasuredResults == null) {
            mMeasuredResults = new ArrayList<FinalResult>();
        }
        this.mMeasuredResults.clear();
        for (int i = 0; i < measuredResults.size(); i++) {
            this.mMeasuredResults.add(measuredResults.get(i));
        }
    }

    public void setCalculatedResults(ArrayList<FinalResult> calculatedResults) {
        if (mCalculatedResults == null) {
            mCalculatedResults = new ArrayList<FinalResult>();
        }
        this.mCalculatedResults.clear();
        for (int i = 0; i < calculatedResults.size(); i++) {
            this.mCalculatedResults.add(calculatedResults.get(i));
        }
    }

    public void setCorrectedResults(ArrayList<FinalResult> correctedResults) {
        if (mCorrectedResults == null) {
            mCorrectedResults = new ArrayList<FinalResult>();
        }
        this.mCorrectedResults.clear();
        for (int i = 0; i < correctedResults.size(); i++) {
            this.mCorrectedResults.add(correctedResults.get(i));
        }
    }

    public boolean isCNCBecauseFailedIQC() {
        return mCNCBecauseFailedIQC;
    }

    public void setCNCBecauseFailedIQC(boolean mCNCBecauseFailedIQC) {
        this.mCNCBecauseFailedIQC = mCNCBecauseFailedIQC;
    }

    public void setFailedIQC(boolean mFailedIQC) {
        this.mFailedIQC = mFailedIQC;
    }

    public boolean isFailedIQC() {
        return mFailedIQC;
    }

    public TestRawResultCollection() {
        mMeasuredResults = new ArrayList<FinalResult>();
        mCalculatedResults = new ArrayList<FinalResult>();
        mCorrectedResults = new ArrayList<FinalResult>();
    }
}
