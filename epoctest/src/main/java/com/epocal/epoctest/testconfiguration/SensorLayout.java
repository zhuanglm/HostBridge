package com.epocal.epoctest.testconfiguration;

/**
 * Created by dning on 9/15/2017.
 */

public class SensorLayout
{
    public Sensor SensorObject;
    public byte ChannelType;
    public int SensorLayoutNumber;

    public SensorLayout(){
        SensorObject = new Sensor();
    }
}
