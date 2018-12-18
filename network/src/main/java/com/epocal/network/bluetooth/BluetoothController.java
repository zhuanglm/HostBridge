package com.epocal.network.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;

import com.epocal.common.eventmessages.ReaderDevice;
import com.epocal.network.bluetooth.di.component.BluetoothComponent;
import com.epocal.network.bluetooth.di.component.DaggerBluetoothComponent;
import com.epocal.network.bluetooth.di.module.BluetoothModule;
import com.epocal.network.bluetooth.di.module.InternalModule;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * The Bluetooth controller class
 * <p>
 * Created by dning on 4/27/2017.
 */

public class BluetoothController implements IBluetoothController.IDiscovery, IBluetoothController.ISetting {
    public static final int MSG_DISCOVERY_BEGIN = 1;
    public static final int MSG_DISCOVERY_NEW_DEVICE = 2;
    public static final int MSG_DISCOVERY_END = 3;
    public static final String DATA_DEVICE_NAME = "Device name";
    public static final String DATA_DEVICE_ADDRESS = "Device address";

    @Inject
    BluetoothAdapter mBluetoothAdapter;
    @Inject
    BluetoothBindServiceConnection mServiceConnection;
    @Inject
    BluetoothDeviceFilter mBluetoothDeviceFilter;

    private Context mContext;

    private boolean mDiscoveryServiceConnected = false;
    private final Messenger mMessenger = new Messenger(new IncomingHandler());

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int msgType = msg.what;
            switch (msgType) {
                case MSG_DISCOVERY_NEW_DEVICE: {
                    Bundle bundle = msg.getData();
                    String deviceName = bundle.getString(BluetoothController.DATA_DEVICE_NAME);
                    String deviceAddr = bundle.getString(BluetoothController.DATA_DEVICE_ADDRESS);
                    if (!mBluetoothDeviceFilter.isAcceptedReaderDevice(deviceName)) {
                        break;
                    }
                    try {
                        ReaderDevice message = new ReaderDevice();
                        message.setDeviceName(deviceName);
                        message.setDeviceAddress(deviceAddr);
                        EventBus.getDefault().post(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case MSG_DISCOVERY_END: {
                    terminateDeviceDiscovery();
                    break;
                }
                case MSG_DISCOVERY_BEGIN: {
                    mDiscoveryServiceConnected = true;
                    break;
                }
                default: {
                    super.handleMessage(msg);
                    break;
                }
            }
        }
    }

    public BluetoothController(Context context) {
        mContext = context;
        BluetoothComponent bc = DaggerBluetoothComponent.builder()
                .bluetoothModule(new BluetoothModule())
                .internalModule(new InternalModule())
                .build();
        bc.inject(this);
    }

    public Boolean isBluetoothSupported() {
        return (mBluetoothAdapter != null);
    }

    public Boolean isEnabled() {
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isEnabled()) {
                return true;
            }
        }
        return false;
    }

    public Boolean setupBluetooth(boolean enabled) {
        if (mBluetoothAdapter != null) {
            if (enabled) {
                return mBluetoothAdapter.enable();
            } else {
                return mBluetoothAdapter.disable();
            }
        }
        return false;
    }

    public Boolean performDeviceDiscovery() {
        if (!mDiscoveryServiceConnected) {
            mServiceConnection.setCallbackMessenger(mMessenger);
            return mContext.bindService(new Intent(mContext, BluetoothDiscovery.class), mServiceConnection, Context.BIND_AUTO_CREATE);
        }
        return false;
    }

    public void terminateDeviceDiscovery() {
        if (mDiscoveryServiceConnected) {
            mContext.stopService(new Intent(mContext, BluetoothBindServiceConnection.class));
            mContext.unbindService(mServiceConnection);
            mDiscoveryServiceConnected = false;
        }
    }
}