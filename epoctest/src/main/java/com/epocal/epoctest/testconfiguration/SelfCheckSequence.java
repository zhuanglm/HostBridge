package com.epocal.epoctest.testconfiguration;

import com.epocal.common.types.ReaderType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dning on 9/19/2017.
 */

public class SelfCheckSequence {
    public ReaderType readerType;
    public List<ChannelConfig> selfCheckSequenceBlock;

    public SelfCheckSequence()
    {
        selfCheckSequenceBlock = new ArrayList<ChannelConfig>();
    }
}
