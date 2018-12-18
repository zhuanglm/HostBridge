package com.epocal.network.bluetooth;

/**
 * Created by dning on 6/14/2017.
 */

public interface IDataReceivedCallback {
    void dataReceived(byte[] data, int received);
}
