package com.epocal.reader.type;

import com.epocal.reader.common.DataStreamReader;
import com.epocal.reader.enumtype.AccessType;
import com.epocal.reader.enumtype.MaintenanceFlagType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/22/2017.
 */

public class ReaderStatus extends DataFragment
{
    public AccessType mAccess;
    public MaintenanceFlagType mMaintenanceFlag;

    public AccessType getAccess() {
        return mAccess;
    }

    public void setAccess(AccessType access) {
        mAccess = access;
    }

    public MaintenanceFlagType getMaintenanceFlag() {
        return mMaintenanceFlag;
    }

    public void setMaintenanceFlag(MaintenanceFlagType maintenanceFlag) {
        mMaintenanceFlag = maintenanceFlag;
    }

    public ReaderStatus(){}

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(mAccess.value);
        output.write(mMaintenanceFlag.value);
        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();
        return bytes;
    }

    @Override
    public void readBytes(DataStreamReader dsr) throws IOException {
        mAccess = AccessType.convert(dsr.readByte());
        mMaintenanceFlag = MaintenanceFlagType.convert(dsr.readByte());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AccessType: " + mAccess.toString());
        sb.append("MaintenanceFlagType: " + mMaintenanceFlag.toString());
        return sb.toString();
    }
}
