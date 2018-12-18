package com.epocal.reader.type.Legacy;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.EpocVersion;
import com.epocal.util.UnsignedType;

import java.text.NumberFormat;
import java.util.Arrays;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class LegacyInfo {

    private String mDAMfirmwareId;
    private String mARMfirmwareId;
    private String mHardwareId;
    private String mMechanicId;
    private byte[] mSerialNumber;
    private short mHardwareCompensationId;
    private EpocVersion mSelfCheckVer;
    private EpocVersion mDryCardCheckVer;
    private EpocVersion mTestSequenceVer;

    private String mMscHardwareProductCode;
    private byte mMscHardwareVersion;
    private String mArmProductId;
    private String mSibId;
    private byte mSibCid;

    private UnsignedType.UInt16 mCompensationTableType;
    private String mReaderFirmwareVersion;
    private byte mReaderFinalCID;
    private static String HostReaderInterface = "2.11";
    private final static String SupportReaderInterface = "2.15";
    private byte mReaderType;

    private byte[] mMainBoardSerialNumber;
    private byte[] mSibSerialNumber;
    private byte[] mProductVariant;
    private UnsignedType.UInt32 mCsrBuildNumber;
    private byte mTdkBuildNumber;
    private byte[] mTdkVersionNumber;
    private byte[] mBluetoothAddress;
    private byte[] mBluetoothDeviceManufacturer;
    private byte[] mBluetoothChipsetManufacturer;
    private byte mLinkSupervisionTimeout;

    private byte mQCTableFormatVersion = 0;
    private UnsignedType.UInt16 mQCTableContentVersion;

    private NumberFormat mNumFormat;

    public LegacyInfo() {
        mMainBoardSerialNumber = new byte[10];
        mSibSerialNumber = new byte[10];
        mProductVariant = new byte[25];
        mTdkVersionNumber = new byte[6];
        mBluetoothAddress = new byte[12];
        mBluetoothDeviceManufacturer = new byte[23];
        mBluetoothChipsetManufacturer = new byte[3];

        mNumFormat = NumberFormat.getNumberInstance();
    }

    private boolean isSupportReaderQCTable(String readerInterface, byte qcTableFormatVersion) {
        return ((readerInterface.compareTo(SupportReaderInterface) >= 0) && ((int) qcTableFormatVersion > 0));
    }

    public String getDAMfirmwareId() {
        return mDAMfirmwareId;
    }

    public String getARMfirmwareId() {
        return mARMfirmwareId;
    }

    public String getHardwareId() {
        return mHardwareId;
    }

    public String getMechanicId() {
        return mMechanicId;
    }

    public boolean isSupportQCTable() {
        return isSupportReaderQCTable(HostReaderInterface, mQCTableFormatVersion);
    }

    public String getHostReaderInterface() {
        return HostReaderInterface;
    }

    public byte getQCTableFormatVersion() {
        return mQCTableFormatVersion;
    }

    public UnsignedType.UInt16 getQCTableContentVersion() {
        return mQCTableContentVersion;
    }

    public String getReaderFirmwareVersion() {
        return mReaderFirmwareVersion;
    }

    public String getSibId() {
        return mSibId;
    }

    public byte getSibCid() {
        return mSibCid;
    }

    public void loadFromBuf(byte[] buffer) {
        int nCount = buffer.length;

        mDAMfirmwareId = Integer.toString(buffer[0] & 0xff) + "." + Integer.toString(buffer[1] & 0xff)
                + "." + Integer.toString(buffer[2] & 0xff);
        mARMfirmwareId = Integer.toString(buffer[3] & 0xff) + "." + Integer.toString(buffer[4] & 0xff)
                + "." + Integer.toString(buffer[5] & 0xff);
        mHardwareId = Integer.toString(buffer[6] & 0xff) + "." + Integer.toString(buffer[7] & 0xff);
        mMechanicId = Integer.toString(buffer[8] & 0xff) + "." + Integer.toString(buffer[9] & 0xff);

        mSerialNumber = Arrays.copyOfRange(buffer, 10, 16);
        byte[] val = Arrays.copyOfRange(buffer, 16, 18);
        mHardwareCompensationId = BigEndianBitConverter.byte2short(val);

        mSelfCheckVer = new EpocVersion(buffer[18] & 0xff, buffer[19] & 0xff, buffer[20] & 0xff);
        mDryCardCheckVer = new EpocVersion(buffer[21] & 0xff, buffer[22] & 0xff, buffer[23] & 0xff);
        mTestSequenceVer = new EpocVersion(buffer[24] & 0xff, buffer[25] & 0xff, buffer[26] & 0xff);

        if (nCount >= 27) {
            // the extra stuff
            mMscHardwareProductCode = String.format("%02x.%02x", (buffer[27] & 0xff), buffer[28] & 0xff);
            mMscHardwareVersion = buffer[29];

            mArmProductId = String.format("%02x.%02x.%02x.%02x.%02x.%02x.%02x.%02x",
                    buffer[30] & 0xff, buffer[31] & 0xff, buffer[32] & 0xff, buffer[33] & 0xff,
                    buffer[34] & 0xff, buffer[35] & 0xff, buffer[36] & 0xff, buffer[37] & 0xff);

            if (nCount >= 39) {
                mSibId = Integer.toString(buffer[38] & 0xff) + "." + Integer.toString(buffer[39] & 0xff);
                mSibCid = buffer[38];

                if (nCount >= 40) {
                    mCompensationTableType = new UnsignedType.UInt16(buffer[40]);

                    if (nCount >= 43) {
                        mReaderFirmwareVersion = buffer[42] + "." + buffer[43] + "." +
                                buffer[44] + "." + buffer[45];

                        if (nCount >= 145) {
                            mReaderFinalCID = buffer[46];
                            HostReaderInterface = buffer[47] + "." + buffer[48];
                            mReaderType = buffer[49];

                            int i;

                            for (i = 0; i < 10; i++) {
                                mMainBoardSerialNumber[i] = buffer[50 + i];
                            }

                            for (i = 0; i < 10; i++) {
                                mSibSerialNumber[i] = buffer[60 + i];
                            }

                            for (i = 0; i < 25; i++) {
                                mProductVariant[i] = buffer[70 + i];
                            }

                            mCsrBuildNumber = new UnsignedType.UInt32(buffer[95]);
                            mTdkBuildNumber = buffer[99];

                            for (i = 0; i < 6; i++) {
                                mTdkVersionNumber[i] = buffer[100 + i];
                            }

                            for (i = 0; i < 12; i++) {
                                mBluetoothAddress[i] = buffer[106 + i];
                            }

                            for (i = 0; i < 23; i++) {
                                mBluetoothDeviceManufacturer[i] = buffer[118 + i];
                            }

                            for (i = 0; i < 3; i++) {
                                mBluetoothChipsetManufacturer[i] = buffer[141 + i];
                            }

                            mLinkSupervisionTimeout = buffer[144];

                            if (nCount >= 146) {
                                mQCTableFormatVersion = buffer[145];
                                val = Arrays.copyOfRange(buffer, 146, 148);
                                mQCTableContentVersion = new UnsignedType.UInt16(BigEndianBitConverter.byte2short(val));
                            }

                        }//if (count >= 145)

                    } else    //if (count >= 43)
                        mReaderFirmwareVersion = "?.?.?.?";
                } else    //if (count >= 40)
                    mCompensationTableType = new UnsignedType.UInt16((short) 0);
            } else    //if (count >= 39)
                mSibId = "";

        }
    }

    public String toString() {
        String tempString = "DeviceIdResponse: DAM: " + mDAMfirmwareId +
                " ARM: " + mARMfirmwareId +
                " ARM PC: " + mArmProductId +
                " Hw: " + mHardwareId +
                " Mech: " + mMechanicId +
                " Serial: " + mNumFormat.format(mSerialNumber[0]) + " "
                + mNumFormat.format(mSerialNumber[1]) + " "
                + mNumFormat.format(mSerialNumber[2]) + " "
                + mNumFormat.format(mSerialNumber[3]) + " "
                + mNumFormat.format(mSerialNumber[4]) + " "
                + mNumFormat.format(mSerialNumber[5]) +
                " CT: " + mNumFormat.format(mHardwareCompensationId) + " "
                + mNumFormat.format(mCompensationTableType.getValue()) +
                " Reader firmware " + mReaderFirmwareVersion +
                " ReaderFinalCID: " + mNumFormat.format(mReaderFinalCID) +
                " HostReaderInterface: " + HostReaderInterface +
                " ReaderType: " + mNumFormat.format(mReaderType) +
                " CSRBuildNumber: " + mNumFormat.format(mCsrBuildNumber.getValue()) +
                " TDKBuildNumber: " + mNumFormat.format(mTdkBuildNumber) +
                " LinkSupervisionTimeout: " + mNumFormat.format(mLinkSupervisionTimeout) +
                " QCTableFormatVersion: " + mNumFormat.format(mQCTableFormatVersion) /*+
                " QCTableContentVersion: " + mNumFormat.format(mQCTableContentVersion.getValue())*/;

        tempString += AddToTempString(mMainBoardSerialNumber, " MBSerial: ");
        tempString += AddToTempString(mSibSerialNumber, " SIBSerial: ");
        tempString += AddToTempString(mProductVariant, " productVariant: ");
        tempString += AddToTempString(mTdkVersionNumber, " TDKVersion: ");
        tempString += AddToTempString(mBluetoothAddress, " BTAddress: ");
        tempString += AddToTempString(mBluetoothDeviceManufacturer, " BTDeviceManufacturer: ");
        tempString += AddToTempString(mBluetoothChipsetManufacturer, " BTChipsetManufacturer: ");

        return tempString;
    }

    private String AddToTempString(byte[] toBeAdded, String prefaceString) {
        StringBuilder tempString = new StringBuilder(prefaceString);

        for (byte b : toBeAdded) {
            tempString.append(mNumFormat.format(b)).append(" ");
        }

        return tempString.toString();
    }
}
