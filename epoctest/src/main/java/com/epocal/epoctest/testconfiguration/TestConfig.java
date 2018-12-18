package com.epocal.epoctest.testconfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dning on 9/22/2017.
 */

public class TestConfig {

    public VersionObject testSequenceVersion;
    public List<SequenceBlock> SequenceBlocks;
    public List<SensorLayout> SensorLayouts;
    public List<SensorDescriptor> SensorDescriptors;

    public TestConfig()
    {
        testSequenceVersion = new VersionObject();
        SequenceBlocks = new ArrayList<SequenceBlock>();
        SensorLayouts = new ArrayList<SensorLayout>();
        SensorDescriptors = new ArrayList<SensorDescriptor>();
    }
}
