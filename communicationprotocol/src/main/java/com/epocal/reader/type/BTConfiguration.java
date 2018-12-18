package com.epocal.reader.type;

import com.epocal.reader.common.BigEndianBitConverter;
import com.epocal.reader.common.DataStreamReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by dning on 6/26/2017.
 */

public class BTConfiguration extends DataFragment
{
    private short mHostReaderBaudRate;
    private short mLinkSupervisionTimeout;
    private short mPageScanInterval;
    private short mPageScanWindow;
    private short mInquiryScanInterval;
    private short mInquiryScanWindow;

    public short getHostReaderBaudRate() {
        return mHostReaderBaudRate;
    }

    public void setHostReaderBaudRate(short mHostReaderBaudRate) {
        this.mHostReaderBaudRate = mHostReaderBaudRate;
    }

    public short getLinkSupervisionTimeout() {
        return mLinkSupervisionTimeout;
    }

    public void setLinkSupervisionTimeout(short mLinkSupervisionTimeout) {
        this.mLinkSupervisionTimeout = mLinkSupervisionTimeout;
    }

    public short getPageScanInterval() {
        return mPageScanInterval;
    }

    public void setPageScanInterval(short mPageScanInterval) {
        this.mPageScanInterval = mPageScanInterval;
    }

    public short getPageScanWindow() {
        return mPageScanWindow;
    }

    public void setPageScanWindow(short mPageScanWindow) {
        this.mPageScanWindow = mPageScanWindow;
    }

    public short getInquiryScanInterval() {
        return mInquiryScanInterval;
    }

    public void setInquiryScanInterval(short mInquiryScanInterval) {
        this.mInquiryScanInterval = mInquiryScanInterval;
    }

    public short getInquiryScanWindow() {
        return mInquiryScanWindow;
    }

    public void setInquiryScanWindow(short mInquiryScanWindow) {
        this.mInquiryScanWindow = mInquiryScanWindow;
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        output.write(BigEndianBitConverter.getBytes(mHostReaderBaudRate));
        output.write(BigEndianBitConverter.getBytes(mLinkSupervisionTimeout));
        output.write(BigEndianBitConverter.getBytes(mPageScanInterval));
        output.write(BigEndianBitConverter.getBytes(mPageScanWindow));
        output.write(BigEndianBitConverter.getBytes(mInquiryScanInterval));
        output.write(BigEndianBitConverter.getBytes(mInquiryScanWindow));

        output.flush();
        byte[] bytes = output.toByteArray();
        output.close();
        return bytes;
    }

    @Override
    public void readBytes(DataStreamReader dsr) throws IOException {
        mHostReaderBaudRate = BigEndianBitConverter.toInt16(dsr, 2, 0);
        mLinkSupervisionTimeout = BigEndianBitConverter.toInt16(dsr, 2, 0);
        mPageScanInterval = BigEndianBitConverter.toInt16(dsr, 2, 0);
        mPageScanWindow = BigEndianBitConverter.toInt16(dsr, 2, 0);
        mInquiryScanInterval = BigEndianBitConverter.toInt16(dsr, 2, 0);
        mInquiryScanWindow = BigEndianBitConverter.toInt16(dsr, 2, 0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HostReaderBaudRate: " + mHostReaderBaudRate);
        sb.append("LinkSupervisionTimeout: " + mLinkSupervisionTimeout);
        sb.append("PageScanInterval: " + mPageScanInterval);
        sb.append("PageScanWindow: " + mPageScanWindow);
        sb.append("InquiryScanInterval: " + mInquiryScanInterval);
        sb.append("InquiryScanWindow: " + mInquiryScanWindow);
        return sb.toString();
    }
}
