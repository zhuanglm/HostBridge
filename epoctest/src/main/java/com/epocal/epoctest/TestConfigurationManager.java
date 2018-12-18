package com.epocal.epoctest;

import com.epocal.common.types.CardType;
import com.epocal.common.types.ReaderType;
import com.epocal.epoctest.testconfiguration.TestConfiguration;
import com.epocal.epoctest.testconfiguration.TestConfigurationSet;
import com.epocal.epoctest.testconfiguration.loader.TestConfigurationLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dning on 7/26/2017.
 */

public class TestConfigurationManager
{
    private static TestConfigurationManager instance = null;
    private TestConfigurationSet mTestConfigurationSet;
    private ReaderType mReaderType = ReaderType.BGE_READER;

    protected TestConfigurationManager(){
    }

    public static TestConfigurationManager getInstance()
    {
        if(instance == null) {
            instance = new TestConfigurationManager();
        }
        return instance;
    }

    public boolean loadConfig()
    {
        TestConfigurationLoader loader = new TestConfigurationLoader();
        mTestConfigurationSet = loader.loadXMLConfiguration(mReaderType);
        return true;
    }

    public TestConfiguration getTestConfiguration(int cardType)
    {
        return mTestConfigurationSet.getTestConfiguration(mReaderType, cardType);
    }
}

