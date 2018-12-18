package com.epocal.epoctest.testconfiguration;

import com.epocal.common.types.ReaderType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dning on 9/12/2017.
 */

public class TestConfigurationSet {

    private Map<ReaderType, List<TestConfiguration>> mSet = new HashMap<ReaderType, List<TestConfiguration>>();

    public TestConfigurationSet(){}

    public void addTestConfiguration(ReaderType readerType, TestConfiguration testConfiguration)
    {
        List<TestConfiguration> testConfigurations = null;
        if (mSet.containsKey(readerType))
        {
            testConfigurations = mSet.get(readerType);
        }
        else
        {
            testConfigurations = new ArrayList<TestConfiguration>();
            mSet.put(readerType, testConfigurations);
        }
        testConfigurations.add(testConfiguration);
    }

    public TestConfiguration getTestConfiguration(ReaderType readerType, int cardType)
    {
        List<TestConfiguration> testConfigurations = mSet.get(readerType);
        for (TestConfiguration config: testConfigurations)
        {
            if(config.CardType == cardType)
            {
                return config;
            }
        }
        if (testConfigurations.size()> 0) {
            return testConfigurations.get(0);
        }
        return null;
    }

    public List<TestConfiguration> getTestConfigurationList(ReaderType readerType)
    {
        readerType = ReaderType.BGE_READER;
        List<TestConfiguration> testConfigurations = mSet.get(readerType);
        return testConfigurations;
    }
}
