package com.epocal.reader.enumtype.legacy;

/**
 * Created by rzhuang on Aug 1st 2018.
 */

public enum ReaderMode {
    eDeviceIdleState ((byte)0),           // device is in power down state
    eDeviceStandbyState ((byte)1),        // device is in standby state
    eDeviceReadyState ((byte)2),          // device is connected to host
    eDeviceCardDetectedState ((byte)3),   // device detected card
    eDeviceCardApprovedState ((byte)4),   // host approved card
    eDeviceCalibrationState ((byte)5),    // device detected handle
    eDeviceBubbleDetectedState ((byte)6), // device detected bubble
    eDeviceSampleCollectionState ((byte)7), // device detected blood
    eDeviceDoneState ((byte)8),           // test completed
    eDeviceFirmwareUpgrade ((byte)9),     // device upgrade in progress
    eDeviceOldTestUpload ((byte)10),
    eDeviceSelfCheckState ((byte)11),
    eDeviceDoneErrorState ((byte)12),
    eDeviceConnectedState ((byte)13),
    eDeviceCompensationTableUpgradeState ((byte)14),
    eDeviceNumberOfStates ((byte)15);

    private final byte value;
    ReaderMode(byte val) {this.value = val;}

    public static ReaderMode convert(byte value) {return ReaderMode.values()[value];}
}


