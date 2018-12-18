package com.epocal.reader.protocolcommontype;

import com.epocal.util.UnsignedType;

/**
 * Created by dning on 7/7/2017.
 */

public class Sequence
{
    public byte ChannelType;
    public byte SensorType;
    public byte SensorDescriptorNumber;
    public byte Inputs;
    public byte MUXControl;
    public byte ADCMUX;
    public short VAPP1;
    public short VAPP2;
    public short VAPP3;
    public short VAPP4;
    public int AddToWhichReading;
    public byte Inputs2;
    public byte NumSamples;

    public Sequence()
    {
    }
}
