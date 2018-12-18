package com.epocal.epoctest.testprocess;

/**
 * Created by dning on 10/17/2017.
 */

public class ITestCalculation {
    interface ICalcuate {
        boolean calculateTestResult(TestDataProcessor testDataProcessor);

        boolean terminateCalculation();
    }
}
