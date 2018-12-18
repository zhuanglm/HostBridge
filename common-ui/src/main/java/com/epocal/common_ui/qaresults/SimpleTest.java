package com.epocal.common_ui.qaresults;

public class SimpleTest {
    private String mName;
    private Boolean mPassed;

    public SimpleTest(String testName, Boolean hasPassedTest) {
        this.mName = testName;
        this.mPassed = hasPassedTest;
    }

    public String getName() {
        return mName;
    }
    public Boolean passed() {
        return mPassed;
    }

}
