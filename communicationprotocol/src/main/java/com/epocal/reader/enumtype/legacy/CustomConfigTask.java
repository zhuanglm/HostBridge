package com.epocal.reader.enumtype.legacy;

/**
 * Created by rzhuang on Sep 18 2018.
 */

public enum CustomConfigTask
{
    eCustomConfigTaskTBD ((byte)0x0),
    MotorJitterInvestigation ((byte)0x01),
    SampleDeliveryInvestigation ((byte)0x02),
    PumpTimeOutDB ((byte)0x03),
    QCResultsTable ((byte)0x04);

    public final byte value;
    CustomConfigTask(byte value)
    {
        this.value = Byte.valueOf(value);
    }
    public static CustomConfigTask convert(byte value) {return CustomConfigTask.values()[value];}
}