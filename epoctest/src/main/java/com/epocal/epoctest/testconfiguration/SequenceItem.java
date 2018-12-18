package com.epocal.epoctest.testconfiguration;

/**
 * Created by dning on 9/22/2017.
 */

public class SequenceItem {
    public int SequenceNumber;
    public ChannelConfig ChannelConfigSetting;

    public SequenceItem()
    {
        ChannelConfigSetting = new ChannelConfig();
    }
}
