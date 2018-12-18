package com.epocal.epoctest.testprocess;

import android.content.Context;

import com.epocal.common.androidutil.RxUtil;
import com.epocal.common.globaldi.GlobalAppModule;
import com.epocal.epoctest.TestEventInfo;
import com.epocal.epoctest.TestEventInfoType;
import com.epocal.epoctest.TestStatusType;
import com.epocal.epoctest.di.component.DaggerNetworkComponent;
import com.epocal.epoctest.di.component.NetworkComponent;
import com.epocal.epoctest.di.module.NetworkModule;
import com.epocal.network.bluetooth.BluetoothConnection;
import com.epocal.network.bluetooth.BluetoothConnector;
import com.epocal.network.bluetooth.IDataReceivedCallback;
import com.epocal.reader.IMessageParserCallback;
import com.epocal.reader.IMsgPayload;
import com.epocal.reader.enumtype.ParsingMode;
import com.epocal.reader.parser.MessageParser;
import com.epocal.util.DateUtil;
import com.epocal.util.RefWrappers;
import com.epocal.util.UnsignedType;

import java.util.Calendar;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * Created by dning on 5/25/2017.
 */
@SuppressWarnings("unchecked")
public class TestCommunication extends TestStateDataObject implements Consumer<BluetoothConnection>, IMessageParserCallback, IDataReceivedCallback {
    @Inject
    BluetoothConnector mBluetoothConnector;
    @Inject
    Context mContext;
    @Inject
    MessageParser mMessageParser;

    private com.epocal.common.androidutil.RxUtil<TestEventInfo> mRxUtilStatus;

    public com.epocal.common.androidutil.RxUtil<TestEventInfo> getRxUtilStatus() {
        return mRxUtilStatus;
    }

    public void setRxUtilStatus(com.epocal.common.androidutil.RxUtil<TestEventInfo> mRxUtilStatus) {
        this.mRxUtilStatus = mRxUtilStatus;
    }

    private com.epocal.common.androidutil.RxUtil<IMsgPayload> mRxUtilMessage;

    public com.epocal.common.androidutil.RxUtil<IMsgPayload> getRxUtilMessage() {
        return mRxUtilMessage;
    }

    public void setRxUtilMessage(com.epocal.common.androidutil.RxUtil<IMsgPayload> mRxUtilMessage) {
        this.mRxUtilMessage = mRxUtilMessage;
    }

    private String mAddress;

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public Calendar getConnectionTime() {
        return mConnectionTime;
    }

    public void setConnectionTime(Calendar connectionTime) {
        this.mConnectionTime = connectionTime;
    }

    public String getReaderSerialNumber() {
        return mReaderSerialNumber;
    }

    public void setReaderSerialNumber(String readerSerialNumber) {
        this.mReaderSerialNumber = readerSerialNumber;
    }

    private BluetoothConnection mBluetoothConnection;
    private long mTimeout = 5000;
    private int mMessageNumber = 0;
    private byte[] mSendBuffer;
    private Calendar mConnectionTime;
    private String mReaderSerialNumber = "";

    public TestCommunication(String address) {
        mAddress = address;
        NetworkComponent bc = DaggerNetworkComponent.builder()
                .networkModule(new NetworkModule(ParsingMode.Response))
                .globalAppModule(new GlobalAppModule())
                .build();
        bc.inject(this);
        mBluetoothConnector.create(mAddress, mTimeout);
        mRxUtilStatus = new RxUtil<TestEventInfo>().create(null);
        mRxUtilMessage = new RxUtil<IMsgPayload>().create(null);
        mMessageParser.setIMessageParserCallback(this);
        mConnectionTime = DateUtil.now();

//        LegacyMessageParser mLegacyParser = new LegacyMessageParser();
//        mLegacyParser.setParserInterface(new LegacyMessageParser.IParserGetConfig() {
//            @Override
//            public int getBuffersize(int blocknumber) {
//                //testconfig.getcc(blocknumber);
//                return 0;
//            }
//        });

    }

    public void unsubscribe() {
        mRxUtilStatus.unsubscribe();
    }

    public void connecting() {
        mBluetoothConnector.getRxUtil().subscribe(this);
        TestEventInfo eventInfo = new TestEventInfo();
        eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
        eventInfo.setTestStatusType(TestStatusType.CONNECTING);
        mRxUtilStatus.onNext(eventInfo);
    }

    public void disconnect() {
        if (mBluetoothConnection != null && mBluetoothConnection.isConnected()) {
            try {
                mBluetoothConnection.disconnect();
            } catch (Exception e) {
                android.util.Log.d("TestCommunication", e.getMessage());
            }
        }
    }

    public boolean isConnected() {
        return mBluetoothConnection != null && mBluetoothConnection.isConnected();
    }

    public void accept(BluetoothConnection connection) {
        onProgress(connection);
    }

    private void onProgress(BluetoothConnection connection) {
        if (connection == null || (connection != null && !connection.isConnected())) {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
            eventInfo.setTestStatusType(TestStatusType.DISCONNECTED);
            mRxUtilStatus.onNext(eventInfo);
        } else if (connection != null && connection.isConnected()) {
            mConnectionTime = DateUtil.now();
            mBluetoothConnection = connection;
            mBluetoothConnection.setIDataReceivedCallback(this);
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
            eventInfo.setTestStatusType(TestStatusType.CONNECTED);
            mRxUtilStatus.onNext(eventInfo);
            mSendBuffer = new byte[MessageParser.MAX_SENDBUFFER];
        } else {
            TestEventInfo eventInfo = new TestEventInfo();
            eventInfo.setTestEventInfoType(TestEventInfoType.STATUS_INFO);
            eventInfo.setTestStatusType(TestStatusType.CONNECTING);
            mRxUtilStatus.onNext(eventInfo);
        }
    }

    public boolean sendMessage(IMsgPayload message) {
        RefWrappers<byte[]> refW = new RefWrappers<>(mSendBuffer);
        int size = mMessageParser.parseMessage(message, mMessageNumber, refW);
        mSendBuffer = new byte[refW.getRef().length];
        System.arraycopy(refW.getRef(), 0, mSendBuffer, 0, refW.getRef().length);
        boolean ret = mBluetoothConnection.sendData(mSendBuffer, size);
        mMessageNumber++;
        return ret;
    }

    @Override
    public void parsedMessageReceived(IMsgPayload message) {
        mRxUtilMessage.onNext(message);
    }

    @Override
    public void dataReceived(byte[] data, int received) {
        if (data == null ) {
            onProgress(null);
            return;
        }

        byte[] temByte = new byte[received];
        System.arraycopy(data, 0, temByte, 0, received);

        StringBuilder s = new StringBuilder();
        for (byte aTemByte : temByte) {
            s.append(Integer.toHexString(UnsignedType.UInt16.ConvertToUInt16(aTemByte))).append(" ");
        }
        mMessageParser.addData(temByte, 0, received);
        IMsgPayload msg = mMessageParser.parse(null);
    }
}

