package com.epocal.network.bluetooth;

/**
 * Created by dning on 4/28/2017.
 */

public interface IBluetoothController {
    interface IDiscovery {
        Boolean performDeviceDiscovery();

        void terminateDeviceDiscovery();
    }

    interface ISetting {
        Boolean isBluetoothSupported();

        Boolean isEnabled();

        Boolean setupBluetooth(boolean enabled);
    }
}
