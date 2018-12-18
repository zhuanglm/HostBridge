package com.epocal.network.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.epocal.common.androidutil.RxUtil;
import com.epocal.common.globaldi.GlobalAppModule;
import com.epocal.network.bluetooth.di.component.BluetoothComponent;
import com.epocal.network.bluetooth.di.component.DaggerBluetoothComponent;
import com.epocal.network.bluetooth.di.module.BluetoothModule;
import com.epocal.network.bluetooth.di.module.InternalModule;

import java.io.IOException;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by dning on 5/5/2017.
 */

public class BluetoothConnector {
    public enum ConnectionStatus {
        Disconnect,
        Connected,
        Connecting,
    }

    @Inject
    BluetoothAdapter mBluetoothAdapter;
    @Inject
    Provider<BluetoothConnection> mBluetoothConnection;
    @Inject
    Context mContext;

    private BluetoothDevice mDevice;
    private Object stateChanged = new Object();

    //SPP UUID of Android Bluetooth serial board
    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private String mAddress;

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    private BluetoothConnection mConnection;

    private long mTimeoutMillis;

    public long getTimeoutMillis() {
        return mTimeoutMillis;
    }

    public void setTimeoutMillis(long timeoutMillis) {
        this.mTimeoutMillis = timeoutMillis;
    }

    public Context getContext() {
        return mContext;
    }

    private RxUtil<BluetoothConnection> mRxUtil;

    public RxUtil<BluetoothConnection> getRxUtil() {
        return mRxUtil;
    }

    public void setRxUtil(RxUtil<BluetoothConnection> mRxUtil) {
        this.mRxUtil = mRxUtil;
    }

    public BluetoothConnector() {
        BluetoothComponent bc = DaggerBluetoothComponent.builder()
                .bluetoothModule(new BluetoothModule())
                .internalModule(new InternalModule())
                .globalAppModule(new GlobalAppModule())
                .build();
        bc.inject(this);
        setTimeoutMillis(10000);
    }

    public void create(String address, long timeout) {
        mAddress = address;
        mTimeoutMillis = timeout;

        mRxUtil = new RxUtil().create(new RxUtil.Action() {
            @Override
            public void invoke() {
                doInBackground();
            }
        });
    }

    public void unsubscribeRx() {
        mRxUtil.unsubscribe();
    }

    protected BluetoothConnection doInBackground() {
        mBluetoothAdapter.cancelDiscovery();

        mDevice = mBluetoothAdapter.getRemoteDevice(getAddress());
        createPiring();
        synchronized (stateChanged) {
            try {
                stateChanged.wait(getTimeoutMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        BluetoothSocket mSocket = null;
        try {
            mSocket = mDevice.createInsecureRfcommSocketToServiceRecord(SPP_UUID);
            mSocket.connect();
            mConnection = mBluetoothConnection.get();
            mConnection.setAddress(getAddress());
            mConnection.setSocket(mSocket);
            mConnection.initialConnection();
            //mRxUtilCommunicationInfo.onNext(connection);
            mRxUtil.onNext(mConnection);
            mRxUtil.onComplete();
            return mConnection;
        } catch (IllegalArgumentException | IOException e) {
            try {
                mSocket.close();
            } catch (Exception eclose) {
                eclose.printStackTrace();
            }
            e.printStackTrace();
            mRxUtil.onNext(null);
            mRxUtil.onComplete();
            return null;
        }
    }

    public boolean createPiring() {
        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        getContext().registerReceiver(mReceiver, filter);
        return true;
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.bluetooth.device.action.PAIRING_REQUEST")) {
                abortBroadcast();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
                BluetoothDevice mBluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                try {
                    mBluetoothDevice.setPin("1111".getBytes());
                    mBluetoothDevice.createBond();
                    if (stateChanged != null) {
                        synchronized (stateChanged) {
                            stateChanged.notifyAll();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (intent.getAction().equals("android.bluetooth.device.action.ACL_DISCONNECTED")) {
                try {
                    if (mConnection != null)
                        mConnection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
