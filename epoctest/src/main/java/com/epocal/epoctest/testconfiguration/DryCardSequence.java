package com.epocal.epoctest.testconfiguration;

import com.epocal.common.types.ReaderType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dning on 9/19/2017.
 */

public class DryCardSequence {
    public ReaderType readerType;
    public List<ChannelConfig> dryCardSequenceBlock;

    public DryCardSequence() {
        dryCardSequenceBlock = new ArrayList<ChannelConfig>();
    }
}
