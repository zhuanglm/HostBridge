package com.epocal.epoctest.teststate.legacyepoctest.type;

import com.epocal.common.epocobjects.AnalyteOption;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rzhuang on Aug 10 2018.
 */

public class TestCardInsertedInfo
{
    public String getLotInfo() {
        return mLotInfo;
    }

    public void setLotInfo(String mLotInfo) {
        this.mLotInfo = mLotInfo;
    }

    private String mLotInfo;

    private List<AnalyteOption> mAnalyteOptions;

    public void setAnalyteOptions(List<AnalyteOption> a) {
        mAnalyteOptions = a;
    }

    public List<AnalyteOption> getAnalyteOptions() {

        if (mAnalyteOptions == null) {
            mAnalyteOptions = new ArrayList<AnalyteOption>(1);
        }
        return mAnalyteOptions;
    }
}