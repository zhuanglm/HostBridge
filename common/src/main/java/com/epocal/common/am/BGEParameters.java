package com.epocal.common.am;

import com.epocal.common.types.am.FluidType;
import com.epocal.common.am.SensorReadings;

public class BGEParameters {
    private double parameter0;                      // bubbleDetect
    private double parameter1;                      // sampleDetect
    private float parameter2;                       // ambientTemperature
    private float parameter3;                       // ambientPressure
    private boolean parameter4;                     // applyMultiplicationFactor
    private FluidType parameter5; // fluidType - byte (source) -> AnalyticalManager.FluidType?
    private SensorReadings parameter6;              // topHeaterPower
    private int parameter7;                         // ageOfCard
    private boolean parameter8;                     // applyHemodilution
    private double parameter9;                      // bubbleWidth
    private int parameter10;                        // sibVersionMajor
    private double parameter11;                     // anionInsanityLow
    private double parameter12;                     // anionInsanityHigh
    private double parameter13;                     // bicarbInsanityLow
    private double parameter14;                     // bicarbInsanityHigh
    private boolean parameter15;                    // applymTCO2
    private boolean parameter16;                    // sampleFailedQC

    private int count;

    public BGEParameters() {
        parameter0 = Double.MIN_VALUE;
        parameter1 = Double.MIN_VALUE;
        parameter2 = Float.MIN_VALUE;
        parameter3 = Float.MIN_VALUE;
        parameter4 = false;
        parameter5 = FluidType.Unknown;
        parameter6 = null;
        parameter7 = Integer.MIN_VALUE;
        parameter8 = false;
        parameter9 = Double.MIN_VALUE;
        parameter10 = Integer.MIN_VALUE;
        parameter11 = Double.MIN_VALUE;
        parameter12 = Double.MIN_VALUE;
        parameter13 = Double.MIN_VALUE;
        parameter14 = Double.MIN_VALUE;
        parameter15 = false;
        parameter16 = false;
        count = 0;
    }

    public BGEParameters(double bubbleDetect,
                         double sampleDetect,
                         float ambientTemperature,
                         float ambientPressure,
                         boolean applyMultiplicationFactor,
                         FluidType  fluidType,
                         SensorReadings topHeaterPower,
                         int ageOfCard,
                         boolean applyHemodilution,
                         double bubbleWidth,
                         int sibVersionMajor,
                         double anionInsanityLow,
                         double anionInsanityHigh,
                         double bicarbInsanityLow,
                         double bicarbInsanityHigh,
                         boolean applymTCO2,
                         boolean sampleFailedQC) {
        parameter0 = bubbleDetect;
        parameter1 = sampleDetect;
        parameter2 = ambientTemperature;
        parameter3 = ambientPressure;
        parameter4 = applyMultiplicationFactor;
        parameter5 = fluidType;
        parameter6 = topHeaterPower;
        parameter7 = ageOfCard;
        parameter8 = applyHemodilution;
        parameter9 = bubbleWidth;
        parameter10 = sibVersionMajor;
        parameter11 = anionInsanityLow;
        parameter12 = anionInsanityHigh;
        parameter13 = bicarbInsanityLow;
        parameter14 = bicarbInsanityHigh;
        parameter15 = applymTCO2;
        parameter16 = sampleFailedQC;
        count = 17;
    }

    void setCount(int indexOfAddedParam)
    {
        int newCount = indexOfAddedParam + 1;
        if (count < newCount)
            count = newCount;
    }

    public int count() {
        return count;
    }

    public boolean addParam(double value) {
        return setParam(count, value);
    }

    public boolean setParam(int index, double value) {
        boolean added = true;
        switch (index) {
            case 0:
                parameter0 = value;
                break;

            case 1:
                parameter1 = value;
                break;

            case 9:
                parameter9 = value;
                break;

            case 11:
                parameter11 = value;
                break;

            case 12:
                parameter12 = value;
                break;

            case 13:
                parameter13 = value;
                break;

            case 14:
                parameter14 = value;
                break;

            default:
                added = false;
                break;
        }

        if (added)
            setCount(index);

        return added;
    }

    public double getParamDouble(int index) throws IndexOutOfBoundsException, IllegalArgumentException  {
        if (index >= count) {
            throw new IndexOutOfBoundsException("index="+index+" does not exist in BGEParameters");
        }

        boolean found = true;
        double value = Double.MIN_VALUE;
        switch (index) {
            case 0:
                value = parameter0;
                break;

            case 1:
                value = parameter1;
                break;

            case 9:
                value = parameter9;
                break;

            case 11:
                value = parameter11;
                break;

            case 12:
                value = parameter12;
                break;

            case 13:
                value = parameter13;
                break;

            case 14:
                value = parameter14;
                break;

            default:
                found = false;
                break;
        }

        if (!found) {
            throw new IllegalArgumentException("The value stored at index "+index+" is not type double in BGEParameters.");
        }
        return value;
    }


    public boolean addParam(float value) {
        return setParam(count, value);
    }

    public boolean setParam(int index, float value) {
        boolean added = true;
        switch (index) {
            case 2:
                parameter2 = value;
                break;

            case 3:
                parameter3 = value;
                break;

            default:
                added = false;
                break;
        }

        if (added)
            setCount(index);

        return added;
    }

    public float getParamFloat(int index) throws IndexOutOfBoundsException, IllegalArgumentException  {
        if (index >= count) {
            throw new IndexOutOfBoundsException("index="+index+" does not exist in BGEParameters");
        }

        boolean found = true;
        float value = Float.MIN_VALUE;
        switch (index) {
            case 2:
                value = parameter2;
                break;

            case 3:
                value = parameter3;
                break;

            default:
                found = false;
                break;
        }

        if (!found) {
            throw new IllegalArgumentException("The value stored at index "+index+" is not type float in BGEParameters.");
        }
        return value;
    }

    public boolean addParam(boolean value) {
        return setParam(count, value);
    }

    public boolean setParam(int index, boolean value) {
        boolean added = true;
        switch (index) {
            case 4:
                parameter4 = value;
                break;

            case 8:
                parameter8 = value;
                break;

            case 15:
                parameter15 = value;
                break;

            case 16:
                parameter16 = value;
                break;

            default:
                added = false;
            break;
        }

        if (added)
            setCount(index);

        return added;
    }

    public boolean getParamBoolean(int index) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (index >= count) {
            throw new IndexOutOfBoundsException("index=" + index + " does not exist in BGEParameters");
        }

        boolean found = true;
        boolean value = false;
        switch (index) {
            case 4:
                value = parameter4;
                break;
            case 8:
                value = parameter8;
                break;
            case 15:
                value = parameter15;
                break;
            case 16:
                value = parameter16;
                break;
            default:
                found = false;
                break;
        }

        if (!found) {
            throw new IllegalArgumentException("The value stored at index " + index + " is not type boolean in BGEParameters.");
        }
        return value;
    }

    public boolean addParam(FluidType value) {
        return setParam(count, value);
    }

    public boolean setParam(int index, FluidType value) {
        boolean added = true;
        switch (index) {
            case 5:
                parameter5 = value;
                break;

            default:
                added = false;
                break;
        }

        if (added)
            setCount(index);

        return added;
    }

    public FluidType getParamFluidType(int index) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (index >= count) {
            throw new IndexOutOfBoundsException("index=" + index + " does not exist in BGEParameters");
        }

        if (index != 5) {
            throw new IllegalArgumentException("The value stored at index " + index + " is not type FluidType in BGEParameters.");
        }

        return parameter5;
    }

    public boolean addParam(SensorReadings value) {
        return setParam(count, value);
    }

    public boolean setParam(int index, SensorReadings value) {
        boolean added = true;
        switch (index) {
            case 6:
                parameter6 = value;
                break;

            default:
                added = false;
                break;
        }

        if (added)
            setCount(index);

        return added;
    }

    public SensorReadings getParamSensorReadings(int index) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (index >= count) {
            throw new IndexOutOfBoundsException("index=" + index + " does not exist in BGEParameters");
        }

        if (index != 6) {
            throw new IllegalArgumentException("The value stored at index " + index + " is not type SensorReadings in BGEParameters.");
        }

        return parameter6;
    }

    public boolean addParam(int value) {
        return setParam(count, value);
    }

    public boolean setParam(int index, int value) {
        boolean added = true;
        switch (index) {
            case 7:
                parameter7 = value;
                break;

            case 10:
                parameter10 = value;
                break;

            default:
                added = false;
                break;
        }

        if (added)
            setCount(index);

        return added;
    }

    public int getParamInt(int index) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (index >= count) {
            throw new IndexOutOfBoundsException("index=" + index + " does not exist in BGEParameters");
        }

        boolean found = true;
        int value = Integer.MIN_VALUE;
        switch (index) {
            case 7:
                value = parameter7;
                break;

            case 10:
                value = parameter10;
                break;

            default:
                found = false;
                break;
        }

        if (!found) {
            throw new IllegalArgumentException("The value stored at index " + index + " is not type boolean in BGEParameters.");
        }
        return value;
    }
}