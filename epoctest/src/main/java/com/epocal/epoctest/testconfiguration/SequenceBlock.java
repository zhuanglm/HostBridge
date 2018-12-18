package com.epocal.epoctest.testconfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dning on 9/19/2017.
 */

public class SequenceBlock {
    public int BlockNumber;
    public int BlockLength;
    public List<SequenceItem> Sequences;

    public SequenceBlock() {
        Sequences = new ArrayList<SequenceItem>();
    }
}
