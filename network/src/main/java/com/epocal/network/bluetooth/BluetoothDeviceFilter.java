package com.epocal.network.bluetooth;

/**
 * Created by dning on 5/2/2017.
 */

public class BluetoothDeviceFilter {
    private int MinimumDeviceNameLength = 6;
    private int SerialCharPositionInDeviceNameOffset = 0;
    private int SerialCharLength = 5;
    private int ChecksumCharPositionInDeviceNameOffset = 5;
    private int ChecksumCharLength = 1;
    private int AliasCharPositionInDeviceNameOffset = SerialCharLength + ChecksumCharLength;

    public BluetoothDeviceFilter() {
    }

    public Boolean isAcceptedReaderDevice(String deviceName) {
        if (deviceName == null) {
            return false;
        }
        if (deviceName.length() < MinimumDeviceNameLength) {
            return false;
        }

        String dnSerialNum = getSerialNumberFromDeviceName(deviceName);

        if (!dnSerialNum.matches("^[0-9]*$")) {
            return false;
        }

        String dnChecksum = getSerialNumberCheckumFromDeviceName(deviceName);
        String calcChecksum = calculateSerialNumberChecksum(dnSerialNum);
        return Integer.getInteger(dnChecksum) == Integer.getInteger(calcChecksum);
    }

    public void readerUtilNextGen() {
        MinimumDeviceNameLength = 7;
        SerialCharPositionInDeviceNameOffset = 0;
        SerialCharLength = 5;
        ChecksumCharPositionInDeviceNameOffset = 5;
        ChecksumCharLength = 3;
        AliasCharPositionInDeviceNameOffset = (SerialCharLength + ChecksumCharLength);
    }

    private void readerUtilLegacy() {
        MinimumDeviceNameLength = 6;
        SerialCharPositionInDeviceNameOffset = 0;
        SerialCharLength = 5;
        ChecksumCharPositionInDeviceNameOffset = 5;
        ChecksumCharLength = 1;
        AliasCharPositionInDeviceNameOffset = (SerialCharLength + ChecksumCharLength);
    }

    private String getSerialNumberFromDeviceName(String deviceName) {
        return deviceName.substring(SerialCharPositionInDeviceNameOffset, SerialCharPositionInDeviceNameOffset + SerialCharLength);
    }

    private String getSerialNumberCheckumFromDeviceName(String deviceName) {
        return deviceName.substring(ChecksumCharPositionInDeviceNameOffset, ChecksumCharPositionInDeviceNameOffset + ChecksumCharLength);
    }

    private String calculateSerialNumberChecksum(String serialNumber) {
        int tempInt = 0;
        for (Character digit : serialNumber.toCharArray()) {
            tempInt += digit - '0';
        }
        tempInt = tempInt % 10;

        return Integer.toString(tempInt);
    }
}
