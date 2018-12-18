package com.epocal.reader.type;

import com.epocal.reader.common.DataStreamReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/26/2017.
 */

public class MaintenanceTestRecordNumber extends DataFragment {

    private ConfigurationHeader mHeader;
    private byte mMaintenanceNumber;

    public ConfigurationHeader getHeader() {
        return mHeader;
    }

    public void setHeader(ConfigurationHeader mHeader) {
        this.mHeader = mHeader;
    }

    public byte getMaintenanceNumber() {
        return mMaintenanceNumber;
    }

    public void setMaintenanceNumber(byte mMaintenanceNumber) {
        this.mMaintenanceNumber = mMaintenanceNumber;
    }

    public MaintenanceTestRecordNumber()
    {
        mHeader = new ConfigurationHeader();
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(mMaintenanceNumber);
        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();

        mHeader.setData(bytes);
        return mHeader.toBytes();
    }

    @Override
    public void readBytes(DataStreamReader dsr) throws IOException {
        mHeader.readBytes(dsr);
        mMaintenanceNumber = dsr.readByte();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Header: " + mHeader.toString());
        sb.append("MaintenanceNumber: " + mMaintenanceNumber);
        return sb.toString();
    }

}
